package com.avit.test;

import java.io.IOException;

import javax.servlet.http.HttpServlet;

import com.avit.util.InitConfig;

public class TestThread extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init(){
		try {
			//初始化配置信息
			getConfig();
			
			for(int i=0;i<50;i++){
				RunThread t = new RunThread();
				t.start();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			System.out.println("TestThread exception");
			e.printStackTrace();
		} 
	}
	
	private void getConfig() throws IOException{
		InitConfig config = new InitConfig();	
		config.initConfig();
	}

}
