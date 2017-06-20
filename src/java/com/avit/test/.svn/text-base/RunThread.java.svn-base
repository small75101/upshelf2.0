package com.avit.test;

import com.avit.upshelf.DBConnect;
import com.avit.upshelf.UpshelfService;
import com.avit.upshelf.bean.AutoAction;

public class RunThread extends Thread {
	
	public void run(){
		DBConnect jdbc = null;
		while(true){
			
			UpshelfService upshelf = null;//new UpshelfService();
			String[] cateIds = {"158349","158355","159996","160086","162393","162919","163031","163164","163333","163738","163887","164215","164220"};
			jdbc = new DBConnect();	
			String preCateId = "";
			for(int i=0;i<100;i++){
				int index = (int)(Math.random()*13);
				String cateId=cateIds[index];
//				System.out.println("i==="+i);
				//System.out.println("index==="+index);
				//System.out.println("cateId==="+cateId);
//				long startTime = System.currentTimeMillis();
				try {
					if(cateId.equals(preCateId)){
						Thread.sleep(1000);
					}
					AutoAction auto = jdbc.getAutoActionByMenuId(Long.parseLong(cateId));
					if(auto != null && auto.getUptreeFlag() == 1){
//						upshelf = new UpshelfService(jdbc, "center");
						upshelf = new UpshelfService();
						upshelf.dataUpshelf("",Long.parseLong(cateId));
					}
					preCateId = cateId;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				System.out.println("use time==="+(System.currentTimeMillis()-startTime));
			}
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				System.out.println("RunThread sleep exception");
				e.printStackTrace();
			}finally{
				if (jdbc!=null) {
					jdbc.closeConnection();
					jdbc = null;
				}
			}
			
		}
	}
}
