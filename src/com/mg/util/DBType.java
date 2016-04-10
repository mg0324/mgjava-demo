package com.mg.util;
/**
 * 数据库类型，对应驱动名
 * @author meigang
 *
 */
public class DBType {
	public final static String MYSQL = "mysql";
	public final static String SQL_SERVER = "sqlserver";
	public final static String ORACLE = "oracle";
	/**
	 * 得到驱动类
	 * @param dbType
	 * @return
	 */
	public static String getDriverClass(String dbType){
		String driverClass = "";
		if(dbType.equals(MYSQL)){
			driverClass = "com.mysql.jdbc.Driver";
		}else if(dbType.equals(ORACLE)){
			driverClass = "oracle.jdbc.driver.OracleDriver";
		}else if(dbType.equals(SQL_SERVER)){
			driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}else{
			//不支持的数据库类型
			driverClass = null;
		}
		return driverClass;
	}
	/**
	 * 得到链接数据库的url，支持mysql,oracle,sqlserver
	 * @param host 主机
	 * @param port  端口
	 * @param dbname 数据库名称
	 * @param dbType 数据库类型
	 * @return
	 */
	public static String getUrl(String host,String port,String dbname,String dbType){
		String url = null;
		/**
		 *  1.Mysql : jdbc:mysql://127.0.0.1:3306/dbname
			2.SqlServer : jdbc:sqlserver://127.0.0.1:1433;DatabaseName=test
			3.Oracle: jdbc:oracle:thin:@127.0.0.1:1521:ORCL
		 */
		if(dbType.equals(MYSQL)){
			url = "jdbc:mysql://"+host+":"+port+"/"+dbname;
		}else if(dbType.equals(ORACLE)){
			url = "jdbc:oracle:thin:@"+host+":"+port+":"+dbname;
		}else if(dbType.equals(SQL_SERVER)){
			url = "jdbc:sqlserver://"+host+":"+port+";DatabaseName="+dbname;
		}else{
			//不支持的数据库类型
			url = null;
		}
		return url;
	}
	/**
	 * 得到java类型的字符串，与java.sql.Types对比
	 * @param type
	 * @return
	 */
	public static String getJavaType(int type) {
		String javaType = "Object";
		
		if(type == -7 || type==-6 || type==5 || type==4 || type==-5 ){//int
			javaType = "int";
		}else if(type == 6 || type == 2 || type == 3 || type == 8){//float
			javaType = "float";
		}else if(type == 91 || type == 92 || type == 93){//Date
			javaType = "Date";
		}else if(type == 1 || type == -1 || type == 12 || type == -15 || type == -9 || type == -16){//String
			javaType = "String";
		}
		return javaType;
	}
	
}
