package com.mg.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
/**
 * 抽象controller
 * @author meigang
 * @date 2016-02-12 21:49
 */
public class BaseController extends Controller{
	/**
	 * 页面前缀
	 */
	protected static String HTML_BASE_PATH;
	/**
	 * 项目名称
	 */
	protected static String APP_TITLE;
	/**
	 * 上传文件路径
	 */
	protected static String FILE_UPLOAD_PATH;
	
	
	/**
	 * 静态块加载
	 */
	static{
		init();
	}
	/**
	 * 加载配置文件，初始化属性值
	 */
	private static void init() {
		Prop prop = PropKit.use("constant.properties", "utf-8");
		HTML_BASE_PATH = prop.get("html.base.path");
		APP_TITLE = prop.get("app.title");
		FILE_UPLOAD_PATH = prop.get("file.upload.path");
	}
}
