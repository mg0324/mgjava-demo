package com.mg.config;



import java.util.Properties;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.render.ViewType;


/**
 * jfinal主配置类
 * @author meigang 
 * @date 2015-10-29 09:26
 *
 */
public class JFConfig extends JFinalConfig{
	public static Properties datasource;
	public void configConstant(Constants me) {
		loadPropertyFile("constant.properties");
		me.setDevMode(getPropertyToBoolean("devMode", true));
		me.setViewType(ViewType.FREE_MARKER);
		me.setBaseViewPath("/WEB-INF/pages");
	}
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		AutoBindRoutes autoRoute = new AutoBindRoutes();
		me.add(autoRoute);
	}
	/**
	 * 插件配置
	 */
	public void configPlugin(Plugins me) {
		/*datasource = loadPropertyFile("datasource.properties");
		 //a数据源
	    C3p0Plugin from = new C3p0Plugin(
	            getProperty("from.dataSource.url"), 
	            getProperty("from.dataSource.userName"), 
	            getProperty("from.dataSource.password"), 
	            getProperty("from.dataSource.driverClass"));
	    from.setMaxIdleTime(3000);
	    //加载数据库连接池插件
	    me.add(from);
	    ActiveRecordPlugin fromArp = new ActiveRecordPlugin("main",from);
	    fromArp.setShowSql(true);
	    me.add(fromArp);*/
	}
	/**
	 * 拦截器配置
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new SessionInViewInterceptor());
	}
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

	

}
