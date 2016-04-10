package com.mg.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.mg.util.FileUtils;
import com.mg.util.LogRoot;
/**
 * 主controller
 * @author meigang
 * @date 2015-12-19 11:44
 *
 */
@ControllerBind(controllerKey = "/browser")
public class BrowserController extends BaseController{
	/**
	 * 加载层
	 */
	public void loading(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/browser/loading.html");
	}
	/**
	 * 测试加载大数据
	 */
	public void testLoading(){
		try {
			//休眠3秒
			Thread.sleep(3000);
			renderJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson();
	}
	/**
	 * 测试加载grid
	 */
	public void testGrid(){
		try {
			
			String con = FileUtils.loadFileContent(PathKit.getRootClassPath()+"/json/student.json");
			/**
			 * {"page":1,"records":0,"rows":[],"total":0}
			 */
			
			JSONArray arr = JSONArray.parseArray(con);
			int rows = this.getParaToInt("rows");
			int page = this.getParaToInt("page");
			JSONObject res = new JSONObject();
			int records = arr.size();
			int total = 0;
			if(records % rows == 0){
				total = records / rows;
			}else{
				total = records / rows +1;
			}
			res.put("page", page);
			res.put("records", records);
			//对数据分页
			Object sub = arr.subList((page-1)*rows, records - page*rows > 0 ? rows : records);
			res.put("rows", sub);
			LogRoot.info(sub);
			res.put("total", total);
			renderJson(res);
			return ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson();
	}
	/**
	 * 配置
	 */
	public void handle(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/browser/handleEvent.html");
	}
	/**
	 * 配置
	 */
	public void input_muti(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/browser/input_muti.html");
	}
	/**
	 * 跳转到自动补全页
	 */
	public void autocomplete(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/browser/autocomplete.html");
	}
	/**
	 * 后台返回autocomplete数据
	 */
	public void autoRemote(){
		/*
			suggestions: [
			 
	            { "value": "United Arab Emirates", "data": "AE" },
	            { "value": "United Kingdom",       "data": "UK" },
	            { "value": "United States",        "data": "US" }
	        ]
        */
		JSONObject ret = new JSONObject();
		JSONArray arr = new JSONArray();
		for(int i=0;i<100;i++){
			JSONObject json = new JSONObject();
			json.put("value", "colum"+i);
			json.put("data", "data"+i);
			arr.add(json);
		}
		ret.put("suggestions", arr);
		renderJson(ret);
	}
}
