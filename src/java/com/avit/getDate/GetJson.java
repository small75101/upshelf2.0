package com.avit.getDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.params.HttpMethodParams;

import com.avit.util.Http;
import com.avit.util.InitConfig;



public class GetJson {

	/**
	 * 获取VOD树JSON
	 * @param url
	 * @return String
	 * @throws IOException
	 */
	public String getJson(String url) throws IOException{
//		System.out.println("---------------发起BO请求----------------");
//		System.out.println("vodJson_url===="+url);
//		StringBuffer sb = new StringBuffer();
//		URL u = new URL(url);		
//		URLConnection conn = u.openConnection();
//		//conn.setConnectTimeout(Constant.HTTP_TIMEOUT);
//		conn.setConnectTimeout(60000);
//		conn.connect();	
//		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),Constant.HTTP_ENCODING));
//		String tmp = "";
//		while((tmp = br.readLine())!= null){
//			sb.append(tmp);
//		}
//		
//		return sb.toString();

		Integer statusCode = -1;
		String responseXMl = null;
		//PostMethod post = null;
		org.apache.commons.httpclient.methods.GetMethod get = null;
		try{
//			byte[] request = null;
//			request = "".getBytes("utf-8");
//			RequestEntity entity = new ByteArrayRequestEntity(request,"text/xml; charset=" + "utf-8");
			//发送请求		
			get = new org.apache.commons.httpclient.methods.GetMethod(url);
			//post = new PostMethod(url);
			//post.setRequestEntity(entity);
			//统一编码
			HttpMethodParams params=new HttpMethodParams ();			
			params.setHttpElementCharset("utf-8");
			get.setParams(params);
//			post.setParams(params);
			//statusCode = Http.getClient().executeMethod(post);	
			
			statusCode = Http.getClient().executeMethod(get);
			if(statusCode ==200 ){//成功返回
				byte responseByte[] = get.getResponseBody();// post.getResponseBody();
				responseXMl = new String(responseByte,"utf-8");
			}else{//失败返回
				byte responseByte[] = get.getResponseBody();// post.getResponseBody();
				responseXMl = new String(responseByte,"utf-8");
			}
		}
		catch(SocketTimeoutException se){
				responseXMl = "TIMEOUT";
				se.printStackTrace();
			}
		catch(Exception ex){
			responseXMl = "{DataArea:{\"errorCode\":\"1000\"}}";
			ex.printStackTrace();
		}finally{
//			if(post != null){
//				post.releaseConnection();
//			}
			if(get != null){
				get.releaseConnection();
			}
		}
		return responseXMl;
			
	}
	
	/**
	 * 获取连续剧JSON
	 * @param localEntryUID
	 * @param hierarchyUID
	 * @return String
	 * @throws IOException
	 */
	public String getBundleJson(String localEntryUID,String hierarchyUID) throws IOException{
		try{
			String url = InitConfig.getConfigMap().get("bo_addr") + InitConfig.getConfigMap().get("bundle_url");
			url = url+"&local_entry_UID="+localEntryUID+"&hierarchy_UID="+hierarchyUID;
	//		System.out.println("bundle_url===="+url);
			StringBuffer sb = new StringBuffer();
			URL u = new URL(url);		
			URLConnection conn = u.openConnection();
			conn.setConnectTimeout(Integer.parseInt( InitConfig.getConfigMap().get("http_connection_timeout")) /*Constant.HTTP_TIMEOUT*/);
			conn.connect();	
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),Constant.HTTP_ENCODING));
			String tmp = "";
			while((tmp = br.readLine())!= null){
				sb.append(tmp);
			}
		
			return sb.toString();
			
		}catch(SocketTimeoutException se)
		{
			se.printStackTrace();
			return "TIMEOUT";
		}
		catch(Exception ex){
			ex.printStackTrace();
			return  "{DataArea:{\"errorCode\":\"1000\"}}";
		}
	}
}
