package com.mg.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 短信模板controller
 * @author meigang
 * @date 2016-02-29 11:09
 *
 */
@ControllerBind(controllerKey = "/msgtpl")
public class MsgTplController extends BaseController{
	/**
	 * 跳转到watermark页面
	 */
	public void index(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/msgtpl/index.html");
	}
	
	public void renderMsg(){
		String data = this.getPara("data");
		String freemarker = this.getPara("freemarker");
		freemarker = freemarker.replace("mg", "$");
		Configuration cfg = new Configuration(); 
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		String templateName = "mgMsgTemplate";
		stringLoader.putTemplate(templateName, freemarker);
		cfg.setTemplateLoader(stringLoader);
		Map<String,Object> mapdata = JSONObject.parseObject(data, Map.class);
		JSONObject ret = new JSONObject();
		try {
			StringWriter writer = new StringWriter();
			Template template = cfg.getTemplate(templateName,"utf-8");
			template.process(mapdata, writer);
			ret.put("flag", true);
			ret.put("msg", writer.toString());
			this.renderJson(ret);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("flag", false);
			ret.put("msg", e.getMessage());
			this.renderJson(ret);
		}
		
	}
}
