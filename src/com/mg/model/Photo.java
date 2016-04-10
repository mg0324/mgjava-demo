package com.mg.model;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * photo service层
 * @author meigang
 * @date 2016-02-13 1:23
 */
public class Photo {
	/**
	 * 上传原图片
	 * @param file
	 * @param fileName
	 * @param reativePath
	 * @param realPath
	 * @return
	 * @throws IOException 
	 */
	public String saveBaseImage(File file, String fileName, String reativePath, String realPath) {
		
		return realPath+"/"+fileName;
	}
	/**
	 * 生成水印
	 * @param file
	 * @param fileName
	 * @param reativePath
	 * @param realPath
	 * @return
	 */
	public String saveWaterImage(File file, String fileName, String reativePath, String realPath) {
		
		Prop prop = PropKit.use("watermark.properties", "utf-8");
		OutputStream os = null;
		fileName = "logo_"+fileName;
		try{
			os = new FileOutputStream(realPath+"/"+fileName);
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			//先画图片
			g.drawImage(image, 0, 0, width, height, null);
			
			float alpha = Float.parseFloat(prop.get("file.water.tmd"));   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,     
                    alpha));
			Image logo = ImageIO.read(new File(realPath +"/"+ prop.get("file.water.logo")));
			int x = prop.getInt("file.water.x");
			int y = prop.getInt("file.water.y");
			float b = Float.parseFloat(prop.get("file.water.sf"));
			int width1 = logo.getWidth(null);
			int height1 = logo.getHeight(null);
			double xz = Double.parseDouble(prop.get("file.water.xz"));
			g.rotate(Math.toRadians(xz));
			x = -x;
			y = -y;
			while(x<width*1.5){
				y = -y;
				while(y<height*1.5){
					//再画水印
					g.drawImage(logo, x, y, (int)(width1*b), (int)(height1*b), null);
					y += 200;
				}
				x += 200;
			}
			g.dispose();
			
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bufferedImage);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return realPath+"/"+fileName;
	}
	
}
