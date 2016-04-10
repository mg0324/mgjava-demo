package com.mg.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.plugin.activerecord.DbKit;
import com.mg.util.DBType;
import com.mg.util.DataSourceManager;
import com.mg.util.MGConstant;
import com.mg.util.StringUtils;
import com.mg.vo.ConDB;

public class GCode {
	static Logger log = Logger.getLogger(GCode.class);
	/**
	 * 得到数据库中所有表名
	 * @param con 
	 * 
	 * @return
	 */
	public static String getAllTables(ConDB con,String key) {
		JSONArray arr = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			arr = new JSONArray();
			JSONArray childArr = new JSONArray();
			String url = DBType.getUrl(con.getHost(), con.getPort(), con.getDbName(), con.getDbType());
			if(url==null){
				log.info("不支持的数据库类型");
				return null;
			}
			JSONObject res = DataSourceManager.initDataSource(url, con.getUsername(), con.getPassword(), con.getDbType(), key);
			if(res==null){
				//得到数据元查询表集合
				conn = DbKit.getConfig(key).getConnection();
				rs = conn.getMetaData().getTables(null, null, null,
						new String[] { "TABLE" });
				JSONObject f = new JSONObject();
				f.put("id", 0);
				f.put("name", con.getDbName());
				f.put("expanded",true);
				
				int i=1;
				while (rs.next()) {
					JSONObject json = new JSONObject();
					json.put("id", i);
					json.put("name",rs.getString(3));
					childArr.add(json);
					i++;
				}
				f.put("children", childArr);
				arr.add(f);
			}
			
		} catch (SQLException e) {
			log.info("数据库连接出错，请检查连接属性。");
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.info("关闭连接出错");
				}
				log.info("关闭连接conn");
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.info("结果集关闭出错");
				}
				log.info("关闭结果集rs");
			}
		}
		return JsonKit.toJson(arr);
	}
	/**
	 * 根据表明
	 * @param tableName
	 * @return
	 */
	public static JSONObject getBeanData(String tableName,JSONObject js) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DbKit.getConfig(MGConstant.GEN_CODE_DS_KEY).getConnection();
			rs = conn.prepareStatement("select * from "+tableName).executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			js.put("class", tableName);
			String key = js.getString("key");
			JSONArray arr = new JSONArray();

			for(int i=1;i<=rsmd.getColumnCount();i++){

				String colName = rsmd.getColumnName(i);
				int colType = rsmd.getColumnType(i);
				//封装到Json
				JSONObject json = new JSONObject();
				if(key == null || !colName.toLowerCase().equals(key.toLowerCase())){
					if(StringUtils.isAllUpper(colName)){
						json.put("property", colName.toLowerCase());
					}else{
						json.put("property",StringUtils.firstLower(colName));
					}
					json.put("type", colType);
					log.info("add property:"+colName+" : "+colType );
					arr.add(json);
				}
			}
			js.put("list", arr);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.info("关闭连接出错");
				}
				log.info("关闭连接conn");
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.info("结果集关闭出错");
				}
				log.info("关闭结果集rs");
			}
		}
		return js;
	}
	/**
	 * 根据数据和模板生成javabean的字符串
	 * @param beanData
	 * @param template
	 * @return
	 */
	public static String doVoBean(JSONObject beanData, Prop template) {
		String voString = "";
		String pi = "";//import java.util.Date
		/**
		 * class : tableName,
		 * list : [{property,type},...]
		 */
		String start = template.get("start");
		String end = template.get("end");
		//替换$Class
		start = start.replace("$Class",StringUtils.firstUpper(beanData.getString("class")));
		//属性段
		String ps = "";
		//set,get方法段
		String funs = "";
		
		JSONArray list = beanData.getJSONArray("list");
		for(int i=0;i<list.size();i++){
			String p = template.get("p");
			String s1 = template.get("s1");
			String s2 = template.get("s2");
			String s3 = template.get("s3");
			
			String g1 = template.get("g1");
			String g2 = template.get("g2");
			String g3 = template.get("g3");
			
			JSONObject temp = (JSONObject) list.get(i);
			String property = temp.getString("property");
			int type = temp.getIntValue("type");
			String javaType = DBType.getJavaType(type);
			if(javaType.equals("Date")){
				pi = "import java.util.Date;";
			}
			//1-------- 属性渲染 -------------------
			//替换$tab
			p = p.replace("$tab", "\t");
			//替换$type
			p = p.replace("$type", javaType);
			//替换$property
			p = p.replace("$property", property);
			ps+=p+"\r\n";//加上换行
			//2-------- set,get渲染 -----------------
			
			s1 = s1.replace("$tab", "\t");
			s1 = s1.replace("$Property", StringUtils.firstUpper(property));
			s1 = s1.replace("$type", javaType);
			s1 = s1.replace("$property", property);
			
			s2 = s2.replace("$tab", "\t");
			s2 = s2.replace("$property", property);
			
			s3 = s3.replace("$tab", "\t");
			
			g1 = g1.replace("$tab", "\t");
			g1 = g1.replace("$Property", StringUtils.firstUpper(property));
			g1 = g1.replace("$type", javaType);

			
			g2 = g2.replace("$tab", "\t");
			g2 = g2.replace("$property", property);
			
			g3 = g3.replace("$tab", "\t");
			funs += s1 + "\r\n" + s2 + "\r\n" + s3 +
					"\r\n"+
					g1 + "\r\n" + g2 + "\r\n" + g3 +
					"\r\n";
			
		}
		if(pi.length()>1){
			voString+=pi+"\r\n";
		}
		voString += start +"\r\n"+ ps + funs + end;
		return voString;
	}
	/**
	 * 替换属性为主键
	 * @param tableName
	 * @return
	 */
	public static JSONObject insertPrimaryKey(String tableName,JSONObject json) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DbKit.getConfig(MGConstant.GEN_CODE_DS_KEY).getConnection();
			rs = conn.getMetaData().getPrimaryKeys(null, null, tableName);
			rs.next();
			String key = rs.getString(4);
			if(StringUtils.isAllUpper(key)){
				key = key.toLowerCase();
			}else{
				key = StringUtils.firstLower(key);
			}
			json.put("key", key);
			log.info("add key:"+key);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.info("关闭连接出错");
				}
				log.info("关闭连接conn");
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.info("结果集关闭出错");
				}
				log.info("关闭结果集rs");
			}
		}
		return json;
	}
	/**
	 * 得到属性，带有主键
	 * @param tableName
	 * @return
	 */
	public static JSONObject getBeanDataWithPrimaryKey(String tableName) {
		JSONObject js = new JSONObject();
		js = insertPrimaryKey(tableName, js);
		js = getBeanData(tableName, js);
		return js;
	}
	/**
	 * 生成 resultMap
	 * @param data
	 * @param template
	 * @return
	 */
	public static String doResultMapCode(JSONObject data, Prop template) {
		String ret = "";
		/**
		 * class : tableName,
		 * list : [{property,type},...]
		 */
		String start = template.get("start");
		String end = template.get("end");
		//替换$Class
		start = start.replace("$Class",StringUtils.firstUpper(data.getString("class")));
		//主键段
		String key = template.get("key");
		key = key.replace("$tab", "\t");
		key = key.replace("$key", data.getString("key"));
		//result段
		String results = "";
		
		JSONArray list = data.getJSONArray("list");
		for(int i=0;i<list.size();i++){
			String result = template.get("result");
			
			JSONObject temp = (JSONObject) list.get(i);
			String property = temp.getString("property");
			//替换$tab
			result = result.replace("$tab", "\t");
			
			//替换$property
			result = result.replace("$property", property);
			results+=result+"\r\n";//加上换行
		}
		
		ret += start +"\r\n"+ key + "\r\n" + results + end;
		return ret;
	}
	/**
	 * 生成cud 代码
	 * @param data
	 * @param template
	 * @param px
	 * @return
	 */
	public static String doCudCode(JSONObject data, Prop template,int px) {
		String all = "";
		all += doCCode(data,template,px);
		all += doUCode(data,template,px);
		all += doDCode(data,template,px);
		return all;
	}
	/**
	 * 生成c 代码
	 * @param data
	 * @param template
	 * @param px
	 * @return
	 */
	private static String doCCode(JSONObject data, Prop template,int px) {
		String start = template.get("c_start");
		start = start.replace("$Class", StringUtils.firstUpper(data.getString("class")));
		String insert = template.get("c_insert");
		insert = insert.replace("$tab", "\t");
		insert = insert.replace("$class", data.getString("class"));
		String key = data.getString("key");
		String ps = "";
		String vals = "";
		String key_px = StringUtils.getProperty(key,px);
		ps += "\t\t"+key_px;
		vals += "\t\t#{"+key+"}";
		JSONArray list = data.getJSONArray("list");
		for(int i=0;i<list.size();i++){
			JSONObject temp = (JSONObject) list.get(i);
			//col列
			String p = template.get("c_p");
			p = p.replace("$tab", "\t");
			String p_by_px = StringUtils.getProperty(temp.getString("property"),px);
			p = p.replace("$Property", p_by_px);
			ps += ",\r\n" + p;
			
			//values值
			String p_val = template.get("c_p_val");
			p_val = p_val.replace("$tab", "\t");
			p_val = p_val.replace("$property", temp.getString("property"));
			vals += ",\r\n" + p_val;
		}
		//values段
		String values = template.get("c_values");
		values = values.replace("$tab", "\t");
		String insert_end = template.get("c_insert_end");
		insert_end = insert_end.replace("$tab", "\t");
		String end = template.get("c_end");
		String br = "\r\n";
		return start + br 
				+ insert + br 
				+ ps + br 
				+ values + br 
				+ vals + br 
				+ insert_end 
				+ br 
				+ end + br;
	}
	/**
	 * 生成u 代码
	 * @param data
	 * @param template
	 * @param px
	 * @return
	 */
	private static String doUCode(JSONObject data, Prop template,int px) {
		String start = template.get("u_start");
		start = start.replace("$Class", StringUtils.firstUpper(data.getString("class")));
		String update = template.get("u_update");
		update = update.replace("$tab", "\t");
		update = update.replace("$class", data.getString("class"));
		String key = data.getString("key");
		String ps = "";
		String key_px = StringUtils.getProperty(key,px);
		JSONArray list = data.getJSONArray("list");
		for(int i=0;i<list.size();i++){
			JSONObject temp = (JSONObject) list.get(i);
			//col列
			String p = template.get("u_p");
			p = p.replace("$tab", "\t");
			String p_by_px = StringUtils.getProperty(temp.getString("property"),px);
			p = p.replace("$Property", p_by_px);
			p = p.replace("$property", temp.getString("property"));
			ps += ",\r\n" + p;
		}
		ps = ps.substring(3);
		//u_where段
		String where = template.get("u_where");
		where = where.replace("$tab", "\t");
		where = where.replace("$Key", key_px);
		where = where.replace("$key", key);
		String end = template.get("u_end");
		
		String br = "\r\n";
		return start + br 
				+ update + br 
				+ ps + br 
				+ where 
				+ br 
				+ end + br;
	}
	/**
	 * 生成d 代码
	 * @param data
	 * @param template
	 * @param px
	 * @return
	 */
	private static String doDCode(JSONObject data, Prop template,int px) {
		String start = template.get("d_start");
		start = start.replace("$Class", StringUtils.firstUpper(data.getString("class")));
		String key = data.getString("key");
		String key_px = StringUtils.getProperty(key,px);
		
		//d_p段
		String body = template.get("d_p");
		body = body.replace("$tab", "\t");
		body = body.replace("$class", data.getString("class"));
		body = body.replace("$Key", key_px);
		body = body.replace("$key", key);
	
		String end = template.get("d_end");
		
		String br = "\r\n";
		return start + br 
				+ body + br 
				+ end + br;
	}
}
