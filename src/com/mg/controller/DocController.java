package com.mg.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.mg.util.FileUtils;
/**
 * doc controller
 * @author meigang
 * @date 2016-03-14 11:07
 *
 */
@ControllerBind(controllerKey = "/doc")
public class DocController extends BaseController{
	/**
	 * 主界面
	 * @throws UnsupportedEncodingException 
	 */
	public void index() throws UnsupportedEncodingException{
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/doc/pdf/index.html");
	}
	/**
	 * 到office上传界面
	 * @throws UnsupportedEncodingException
	 */
	public void officeIndex() throws UnsupportedEncodingException{
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/doc/office/index.html");
	}
	/**
	 * 上传pdf转flash
	 * @throws Exception 
	 */
	public void uploadPdf() throws Exception{
		UploadFile file = this.getFile();
		Prop prop = PropKit.use("constant.properties", "utf-8");
		String cmd = prop.get("command.pdf2swf.path");
		String pdfpath = prop.get("file.pdf.2.swf.path");
		String upload = prop.get("file.upload.path");
		//上传文件路径
		String realPath = PathKit.getWebRootPath()+upload;

		//先成pdf - 保存到了/upload也就是jfinal 默认的位置
		//转swf注意是flash 9 
		String command = cmd + " '" + realPath+"/"+file.getFileName() + "' -o '"   
	                + PathKit.getWebRootPath() + pdfpath + "/" + file.getFileName()+".swf'" + " -f -T 9";
		command = command.replaceAll("'", "\"");
	    System.out.println(command);  
	    Process pro = Runtime.getRuntime().exec(command);
	    BufferedReader bufferedReader = new BufferedReader(  
                new InputStreamReader(pro.getInputStream()));  
        while (bufferedReader.readLine() != null);  
        pro.waitFor();
        String path = this.getRequest().getContextPath()+"/doc/loadFile?path="+pdfpath + "/" + file.getFileName() +".swf";
        path = URLEncoder.encode(path,"utf-8");
		this.setAttr("swfpath", path);
		render(HTML_BASE_PATH + "/doc/show.html");
	}
	/**
	 * 获取pdf流
	 * @return 
	 * @throws IOException 
	 */
	public void loadFile() throws IOException{
		File file = new File(PathKit.getWebRootPath()+this.getPara("path"));
		InputStream is = new FileInputStream(file);
		HttpServletResponse res = this.getResponse();
		res.setContentType("application/x-shockwave-flash");
		res.setHeader("Accept-Ranges", "bytes");
		byte[] b = new byte[(int) file.length()];
		is.read(b);
		res.setContentLength(b.length);
		res.getOutputStream().write(b);
		res.getOutputStream().close();
		res.getOutputStream().flush();
		renderNull();
	}
	/**
	 * 上传office文档
	 * @throws Exception
	 */
	public void uploadOffice() throws Exception{
		UploadFile file = this.getFile();
		Prop prop = PropKit.use("constant.properties", "utf-8");
		String cmd = prop.get("command.pdf2swf.path");
		String pdfpath = prop.get("file.pdf.2.swf.path");
		String upload = prop.get("file.upload.path");
		//上传文件路径
		String realPath = PathKit.getWebRootPath()+upload;
		String pdfPath = PathKit.getWebRootPath() + "/doc/"+file.getFileName()+".pdf";
		
		FileUtils.office2PDF(realPath+"/"+file.getFileName(), pdfPath);
		//先成pdf - 保存到了/upload也就是jfinal 默认的位置
		//转swf注意是flash 9 
		String command = cmd + " '" + pdfPath + "' -o '"   
	                + PathKit.getWebRootPath() + pdfpath + "/" + file.getFileName()+".swf'" + " -f -T 9";
		command = command.replaceAll("'", "\"");
	    System.out.println(command);  
	    Process pro = Runtime.getRuntime().exec(command);
	    BufferedReader bufferedReader = new BufferedReader(  
                new InputStreamReader(pro.getInputStream()));  
        while (bufferedReader.readLine() != null);  
        pro.waitFor();
        String path = this.getRequest().getContextPath()+"/doc/loadFile?path="+pdfpath + "/" + file.getFileName() +".swf";
        path = URLEncoder.encode(path,"utf-8");
		this.setAttr("swfpath", path);
		render(HTML_BASE_PATH + "/doc/show.html");
	}
}
