package com.mg.model;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;
import com.mg.util.DataSourceManager;

public class Main {
	public static Logger log = Logger.getLogger(Main.class);
	public static DruidPlugin from;
	public static DruidPlugin to;

	/**
	 * 交换数据
	 */
	public static boolean exchangeData(String tableName,String fromKey,String toKey) {
		boolean b = true;
		List<Record> list = Db.use(fromKey).find("select * from " + tableName);
		try {
			for (Record r : list) {
				Db.use(toKey).save(tableName, r);
			}
		} catch (Exception e) {
			log.info("数据交换过程出错：" + e.getMessage());
			b = false;
		} finally {
			//关闭数据源
			DataSourceManager.getDsList().get(fromKey).stop();
			DataSourceManager.getDsList().get(toKey).stop();
			// 去掉数据源
			DbKit.removeConfig(fromKey);
			DbKit.removeConfig(toKey);
		}
		return b;
	}

}
