package com.mg.controller;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.DbKit;
import com.mg.model.GCode;
import com.mg.util.DataSourceManager;
import com.mg.util.MGConstant;
import com.mg.vo.ConDB;
/**
 * 主controller
 * @author meigang
 * @date 2015-11-10 11:44
 *
 */
@ControllerBind(controllerKey = "/gcode")
public class GCodeController extends BaseController{
	static Logger log = Logger.getLogger(GCodeController.class);
	/**
	 * 连接数据库
	 */
	public void condb(){
		//关闭数据源
		if(DbKit.getConfig(MGConstant.GEN_CODE_DS_KEY)!=null){
			DataSourceManager.getDsList().get(MGConstant.GEN_CODE_DS_KEY).stop();
			log.info("关闭数据源"+MGConstant.GEN_CODE_DS_KEY);
			DbKit.removeConfig(MGConstant.GEN_CODE_DS_KEY);
		}
		JSONObject json = JSONObject.parseObject(this.getPara("submitData"));
		ConDB con = JSONObject.toJavaObject(json, ConDB.class);
		String res = GCode.getAllTables(con,MGConstant.GEN_CODE_DS_KEY);
		renderJson(res);
	}
	/**
	 * 生成javabean
	 */
	public void doBean(){
		String tableName = this.getPara("tableName");
		//得到数据
		/**
		 * class : tableName,
		 * list : [{property,type},...]
		 */
		JSONObject js = new JSONObject();
		JSONObject beanData = GCode.getBeanData(tableName,js);
		Prop template = PropKit.use("template/vo.template");
		String voString = GCode.doVoBean(beanData,template);
		JSONObject res = new JSONObject();
		res.put("data", voString);
		renderJson(res);
	}

	/**
	 * 生成resulteMap
	 */
	public void doResultMap(){
		String tableName = this.getPara("tableName");
		//得到数据
		/**
		 * class : tableName,
		 * list : [{property,type},...]
		 */
		JSONObject data = GCode.getBeanDataWithPrimaryKey(tableName);
		Prop template = PropKit.use("template/resultmap.xml.template");
		String ret = GCode.doResultMapCode(data,template);
		JSONObject res = new JSONObject();
		res.put("data", ret);
		renderJson(res);
	}
	/**
	 * 生成cud xml
	 */
	public void doCud(){
		String tableName = this.getPara("tableName");
		int px = this.getParaToInt("px");
		//得到数据
		/**
		 * class : tableName,
		 * list : [{property,type},...]
		 */
		JSONObject data = GCode.getBeanDataWithPrimaryKey(tableName);
		Prop template = PropKit.use("template/cud.xml.template");
		String ret = GCode.doCudCode(data,template,px);
		JSONObject res = new JSONObject();
		res.put("data", ret);
		renderJson(res);
	}
}
