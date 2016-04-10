package com.mg.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.druid.DruidPlugin;

public class DataSourceManager {
	private static Map<String,DruidPlugin> dsList = new HashMap<String,DruidPlugin>();
	/**
	 * 初始化数据源
	 * @param url
	 * @param username
	 * @param pwd
	 * @param dbType
	 * @param key
	 * @return
	 */
	public static JSONObject initDataSource(String url,String username,String pwd,String dbType,String key){
		JSONObject json = new JSONObject();
		DruidPlugin dp = new DruidPlugin(url,username,pwd);

		String fromDriverClass = DBType.getDriverClass(dbType);
		if(fromDriverClass != null){
			//支持数据库
			dp.setDriverClass(fromDriverClass);
		}else{
			json.put("msg", "不支持的数据库类型!");
			return json;
		}
		dp.start();
		dsList.put(key, dp);
		Log.info("新建数据源"+key);
		DbKit.addConfig(new Config(key, dp.getDataSource()));
		return null;
	}
	
	public static Map<String, DruidPlugin> getDsList() {
		return dsList;
	}
	
}
