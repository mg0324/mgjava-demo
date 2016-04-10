package com.mg;  
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.xml.sax.SAXException;  
  
public class AdminServer {  
	private static Logger log = Logger.getLogger(AdminServer.class);
	
    public static void main(String[] args) {  
        try {  
            // 服务器的监听端口  
        	int port = 12345;
            Server server = new Server(port);  
            // 关联一个已经存在的上下文  
            WebAppContext context = new WebAppContext();  
            // 设置描述符位置  
            context.setDescriptor("./WebContent/WEB-INF/web.xml"); 
            //开启js不加锁在jetty启动后
            context.setDefaultsDescriptor("./WebContent/WEB-INF/webdefault.xml");
            // 设置Web内容上下文路径  
            context.setResourceBase("./WebContent");  
            // 设置上下文路径  
            context.setContextPath("/");  
            context.setParentLoaderPriority(true);  
            server.setHandler(context);  
            // 启动  
            log.info("start server at 127.0.0.1:"+port);
            server.start();
            server.join();
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (SAXException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  