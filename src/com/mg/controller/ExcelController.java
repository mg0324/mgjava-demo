package com.mg.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.mg.util.FileUtils;
import com.mg.util.LogRoot;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * 主controller
 * @author meigang
 * @date 2015-12-19 11:44
 *
 */
@ControllerBind(controllerKey = "/doExcel")
public class ExcelController extends BaseController{
	/**
	 * 加载json数据
	 */
	public void loadStudentJson(){
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
	 * jxl导出excel 2015-12-28
	 */
	public void jxlExport(){
		try {
			String filename = "student";
			HttpServletResponse res = this.getResponse();
			res.setHeader("Content-Disposition", "attachment; filename="+filename+ ".xls"); // 设置下载头信息 
			WritableWorkbook wb = Workbook.createWorkbook(res.getOutputStream());
			WritableSheet ws = wb.createSheet(filename, 0);
			String con = FileUtils.loadFileContent(PathKit.getRootClassPath()+"/json/student.json");
			/**
			 * {"page":1,"records":0,"rows":[],"total":0}
			 */
			JSONArray arr = JSONArray.parseArray(con);
			//添加表头
			ws.addCell(new Label(0, 0, "学号"));
			ws.addCell(new Label(1, 0, "姓名"));
			ws.addCell(new Label(2, 0, "性别"));
			ws.addCell(new Label(3, 0, "年龄"));
			ws.addCell(new Label(4, 0, "爱好"));
			for(int i=0;i<arr.size();i++){
				ws.addCell(new Label(0, i+1, arr.getJSONObject(i).getString("id")));
				ws.addCell(new Label(1, i+1, arr.getJSONObject(i).getString("name")));
				ws.addCell(new Label(2, i+1, arr.getJSONObject(i).getString("sex")));
				ws.addCell(new Label(3, i+1, arr.getJSONObject(i).getString("age")));
				ws.addCell(new Label(4, i+1, arr.getJSONObject(i).getString("hobby")));
			}
			wb.write();
			wb.close();
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * poi导出excel 2015-12-28
	 */
	public void poiExport(){
		try {
			String filename = "student";
			HttpServletResponse res = this.getResponse();
			res.setHeader("Content-Disposition", "attachment; filename="+filename+ ".xls"); // 设置下载头信息 
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(filename);
			//添加头
			HSSFRow row = sheet.createRow(0);
			String[] headers = new String[]{"学号","姓名","性别","年龄","爱好"};
			for(int i=0;i<headers.length;i++){
				row.createCell(i).setCellValue(headers[i]);
			}
			String con = FileUtils.loadFileContent(PathKit.getRootClassPath()+"/json/student.json");
			/**
			 * {"page":1,"records":0,"rows":[],"total":0}
			 */
			JSONArray arr = JSONArray.parseArray(con);
			String[] keys = new String[]{"id","name","sex","age","hobby"};
			//添加数据
			for(int j=0;j<headers.length;j++){
				HSSFRow temp = sheet.createRow(j+1);
				for(int a=0;a<keys.length;a++){
					temp.createCell(a).setCellValue(arr.getJSONObject(j).getString(keys[a]));
				}
			}
			wb.write(res.getOutputStream());
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
