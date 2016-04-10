package com.mg.controller;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.FileKit;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.mg.model.Photo;
import com.mg.vo.ImageBean;

import sun.security.krb5.internal.tools.Kinit;
/**
 * 图片controller
 * @author meigang
 * @date 2016-02-12 21:47
 *
 */
@ControllerBind(controllerKey = "/photo")
public class PhotoController extends BaseController{
	/**
	 * 跳转到watermark页面
	 */
	public void watermark(){
		this.setAttr("root", this.getRequest().getContextPath());
		render(HTML_BASE_PATH + "/photo/watermark.html");
	}
	/**
	 * 生存水印
	 */
	public void moreMark(){
		List<UploadFile> files = this.getFiles();
		List<ImageBean> lib = new ArrayList<ImageBean>();
		Photo photoService = new Photo();
		String realPath = PathKit.getWebRootPath()+"/"+FILE_UPLOAD_PATH;
		for(UploadFile file : files){
			ImageBean ib = new ImageBean();
			ib.setBaseImagePath(photoService.saveBaseImage(file.getFile(),file.getFileName(),FILE_UPLOAD_PATH,realPath));
			ib.setWaterImagePath(photoService.saveWaterImage(file.getFile(),file.getFileName(),FILE_UPLOAD_PATH,realPath));
			lib.add(ib);
		}
		this.setAttr("images", lib);
		render(HTML_BASE_PATH + "/photo/showWater.html");
	}
	/**
	 * 返回文件信息
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void show() throws Exception{
		String path = this.getPara("path");
		File file = new File(path);
		Image image = ImageIO.read(file);
		ImageIO.write((RenderedImage) image, "JPEG", this.getResponse().getOutputStream());
		renderNull();
	}
}
