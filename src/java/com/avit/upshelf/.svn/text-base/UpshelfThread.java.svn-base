package com.avit.upshelf;

import com.avit.upshelf.bean.AutoAction;

public class UpshelfThread extends Thread {

	private String id;
	private String operationCode;
	DBConnect jdbc = null;
	AutoAction auto = null;
	public UpshelfThread(String id, String operationCode){
		this.id = id;
		this.operationCode = operationCode;
	}
	public void run(){
		try{
			jdbc = new DBConnect();
			//0.获取AutoAction对象信息
			auto = jdbc.getAutoActionById(id);
			if(auto != null){
				
				if(auto.getUptreeFlag() == 1){//允许上架
					UpshelfService upshelf = new UpshelfService();
					upshelf.init(jdbc, operationCode);
					upshelf.dataUpshelf("",auto.getMenuid());
				}else{
					System.out.println("uptreeflag set can't uptree! opCode="+operationCode+" poid="+auto.getPoid());
				}
			}
			jdbc.closeConnection();
			jdbc = null;
		}catch (Exception e) {
			System.out.println("UpshelfThread error poid=" + auto.getPoid());
			e.printStackTrace();
		}finally{
			//关闭数据库连接
			if(jdbc != null){
				jdbc.closeConnection();
				jdbc = null;
			}
		}
		
	}
}
