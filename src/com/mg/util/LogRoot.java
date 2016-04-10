package com.mg.util;

import org.apache.log4j.Logger;

/**
 * 日志器 2015-12-19
 * @author meigang
 *
 */
public class LogRoot {
	private static Logger log = Logger.getLogger(LogRoot.class);
	
	public static void info(Object msg){
		log.info(msg);
	}
}
