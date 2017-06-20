package com.avit.upshelf;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.avit.util.InitConfig;

public class AutoAllThread extends Thread {
	public void run(){
		DBConnect jdbc = null;
		List<String> ids = null;
		String businessData = null;
		String operationCode = InitConfig.getConfigMap().get("operation_code");
		//http://127.0.0.1:8080/portalms/business/autoReleaseBusiness.action?dataIds=
		//指向ms的上架功能，触发上架用
		String releaseURL = InitConfig.getConfigMap().get("releaseURL");
		try{
			jdbc = new DBConnect();
			//获取AutoAction列表
			// select id from t_autoaction where operation_code=?
			ids = jdbc.getAutoIds(operationCode);
			businessData = jdbc.getBusiness(operationCode); //{ID}-{BUSINESS_NAME} from t_business_info like 1-BS001
			//关闭数据库连接
			jdbc.closeConnection();
			jdbc = null;			
		}catch(Exception ex){
			ex.printStackTrace();
		} finally{
			if(jdbc!=null){
				jdbc.closeConnection();
			}
		}
		while(true){
			try{
				AutoThread[] tArray = new AutoThread[20]; //dsir ????
				int length = 0;
				//自动同步，自动上架
				//一旦auto IDS 大于20 系统将访问数组下表超界 dsir
				for(String id : ids){
					tArray[length] = new AutoThread(id,false);
					tArray[length].start();
					Thread.sleep(2000);
					length++;
				}
				boolean breaks = false;
				//等待开启的同步线程执行完成
				while (!breaks) {
					breaks = true;
					for (int i = 0; i < length; i++) {
						State state = tArray[i].getState();
						if (state != State.TERMINATED) {
							breaks = false;
						}
					}
					Thread.sleep(1000);
				}				
				
				jdbc = new DBConnect();
				
				//清理连续剧垃圾数据
				//delete t from t_asset_transition t where t.OPERATION_CODE='OP_YB' and t.content_type=3 
				 // and t.asset_id not in (select distinct p.content_code from t_rela_conpack_content p where p.OPERATION_CODE='OP_YB')
				jdbc.cleanPackageInfo(operationCode);
				
				//汇聚后自动生成，自动发布 select t.state from t_forest t, t_business_info b where t.tree_id = b.tree_id and b.operator_code=？
				int state = jdbc.getTreeState(operationCode); //1：修改 2：删除
				jdbc.closeConnection();
				jdbc = null;	
				if (state == 1 && StringUtils.isNotEmpty(businessData)) {
					long time = System.currentTimeMillis();
					getResponse(releaseURL+URLEncoder.encode(businessData, "utf-8")); //http://127.0.0.1:8080/portalms/business/autoReleaseBusiness.action?dataIds=1-BS001
					System.out.println("["+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"] " +
							operationCode+">>>>自动发布   business="+businessData + "    用时="+(System.currentTimeMillis()-time)/1000+"s");
				}
				Thread.sleep(Integer.parseInt(InitConfig.getConfigMap().get("sleepTime")));
			}catch(Exception ex){
				System.out.println("auto release error business="+businessData);
				ex.printStackTrace();
			}finally{
				if(jdbc!=null){
					jdbc.closeConnection();
				}
			}
		}
	}
	private String getResponse(String url) throws IOException{
		StringBuffer sb = new StringBuffer();
		try{			
			URL u = new URL(url);		
			URLConnection conn = u.openConnection();
			conn.setConnectTimeout(5000);
			conn.connect();	
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String tmp = "";
			while((tmp = br.readLine())!= null){
				sb.append(tmp);
			}
		}catch(Exception ex){
			
		}
		
		return sb.toString();		
	}

}
