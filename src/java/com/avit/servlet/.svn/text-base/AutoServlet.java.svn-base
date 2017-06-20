package com.avit.servlet;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.avit.upshelf.AutoThread;
import com.avit.upshelf.DBConnect;
import com.avit.util.InitConfig;

public class AutoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init(){
		DBConnect jdbc = null;
		List<String> ids = null;
		try {
			//初始化配置信息
			getConfig();
			String operationCode = InitConfig.getConfigMap().get("operation_code");
			//获取数据库操作对象
			jdbc = new DBConnect();
			//初始化状态为0
			jdbc.updateAutoState(0, 0, operationCode);
			//初始化正在上架2的修改为上架标识1
			jdbc.updateDefaulUpTreeFlag(operationCode);
			//获取AutoAction列表
			ids = jdbc.getAutoIds(operationCode);
			//关闭数据库连接
			jdbc.closeConnection();
			jdbc = null;
			for(String id : ids){
				AutoThread t = new AutoThread(id,true);
				t.start();
				//while(t.getState()!= State.TERMINATED){
					Thread.sleep(500);
				//}
			}
		} catch (Exception e) {
			System.out.println("AutoServlet exception");
			e.printStackTrace();
		} finally {
			ids = null;
			if (jdbc != null) {
				jdbc.closeConnection();
				jdbc = null;
			}
		}
	}
	
	private void getConfig() throws IOException{
		InitConfig config = new InitConfig();	
		config.initConfig();
	}

}
