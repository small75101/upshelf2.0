package com.avit.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.avit.upshelf.AutoAllThread;
import com.avit.upshelf.DBConnect;
import com.avit.util.InitConfig;

public class AutoAllServlet extends HttpServlet {

	private static final long serialVersionUID = 2L;


	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	
	public void init()throws ServletException{
		DBConnect jdbc = null;
		try {
			//初始化配置信息
			getConfig();
			String operationCode = InitConfig.getConfigMap().get("operation_code"); //operation_code 运营商编号
			//获取数据库操作对象
			jdbc = new DBConnect();
			//初始化状态为0 
			//将所有指定运营商的资源同步操作设置到  0等待状态
			// update t_autoaction set state= 0 where id=? and operation_code=?
			jdbc.updateAutoState(0, 0/*id=0 更行所有*/, operationCode);
			//初始化正在上架2的修改为上架标识1
			//select id from t_autoaction where uptree_flag=2 and operation_code=
			//update t_autoaction set uptree_flag=1 where id =?
			jdbc.updateDefaulUpTreeFlag(operationCode);
			//关闭数据库连接
			jdbc.closeConnection();
			
			AutoAllThread aat = new AutoAllThread();
			aat.start();
		} catch (Exception e) {
			System.out.println("AutoServlet exception");
			e.printStackTrace();
		} finally {
			if (jdbc != null) {
				jdbc.closeConnection();
				jdbc = null;
			}
		}
	}
/**
 * 系统起来是自动将配置读入系统  /system.properties
 * 按map<key,value>静态缓存在系统中
 * @throws IOException
 */
	private void getConfig() throws IOException{
		InitConfig config = new InitConfig();	
		config.initConfig();
	}
}
