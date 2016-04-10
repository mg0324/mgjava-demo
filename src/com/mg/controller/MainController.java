package com.mg.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.mg.model.Main;
import com.mg.util.DataSourceManager;
/**
 * 主controller
 * @author meigang
 * @date 2015-10-29 09:55
 *
 */
@ControllerBind(controllerKey = "/")
public class MainController extends BaseController{

	/**
	 * 软件主界面
	 */
	public void index(){
		this.setAttr("root", this.getRequest().getContextPath());
		this.setAttr("title", APP_TITLE);
		render("/index.html");
	}
	/**
	 * top页
	 */
	public void top(){
		this.setAttr("root", this.getRequest().getContextPath());
		this.setAttr("title", APP_TITLE);
		render("/top.html");
	}
	/**
	 * 数据交换功能
	 */
	public void main(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/d1/moveData.html");
	}
	/**
	 * 根据表生成javabean
	 */
	public void doJavaBean(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/d2/doJavaBean.html");
	}
	/**
	 * v2.0
	 */
	public void importByInterface(){
		/**
		 * data = {
 				"from_db_type" : from_db_type,
 				"from_url" : from_url,
 				"from_username" : from_username,
 				"from_password" : from_password,
 				"table_name" : table_name,
 				"to_db_type" : to_db_type,
 				"to_url" : to_url,
 				"to_username" : to_username,
 				"to_password" : to_password
 			};
		 */
		JSONObject json = DataSourceManager.initDataSource(this.getPara("from_url"),
				this.getPara("from_username"),
				this.getPara("from_password"),
				this.getPara("from_db_type"),
				"from");
		if(json!=null){
			renderJson(json);
		}
		json = DataSourceManager.initDataSource(this.getPara("to_url"),
				this.getPara("to_username"),
				this.getPara("to_password"),
				this.getPara("to_db_type"),
				"to");
		if(json!=null){
			renderJson(json);
		}
		json = new JSONObject();
		if(Main.exchangeData(this.getPara("table_name"),"from","to")){
			json.put("msg", "操作成功！");
			renderJson(json);
		}else{
			json.put("msg", "操作失败，原因请查看后台日志！");
			renderJson(json);
		}
	}
	/**
	 * 显示要导出的excel的数据表格
	 */
	public void excel(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/excel/expJqGrid.html");
	}
}
