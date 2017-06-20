package com.avit.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class InitConfig {

	/**
	 * 系统配置
	 */
	private static HashMap<String,String> configMap;
	
	/**
	 * 初始化系统配置
	 */
	public void initConfig(){
		configMap = new HashMap<String,String>();
		try{
			InputStream is=this.getClass().getResourceAsStream("/system.properties");
			Properties prop = new Properties();
	 	    prop.load(is);
	 	    is.close();
	 	   Enumeration<?> en = prop.propertyNames();
	 	  String key = "";
	 	  String value = "";
	 	   while(en.hasMoreElements()){
	 		  key = (String)en.nextElement();
//	 		  value = prop.getProperty(key);
	 		  value = new String(prop.getProperty(key).getBytes("ISO-8859-1"),"utf-8");
	 		  configMap.put(key, value);
	 	   }
		}catch(IOException ie){
			ie.printStackTrace();
		}
	}

	public static HashMap<String, String> getConfigMap() {
		return configMap;
	}

	public static void setConfigMap(HashMap<String, String> configMap) {
		InitConfig.configMap = configMap;
	}
}
