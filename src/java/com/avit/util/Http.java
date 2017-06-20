package com.avit.util;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class Http {
	private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	private static HttpClient client = new HttpClient(connectionManager);
	private static Http http = new Http();
	public static synchronized HttpClient getClient() {
		return client;
	}
	private Http() {
		configureClient();
	}
	public static Http getInstance() {
		return http;
	}
	private void configureClient() {
		int maxThreadsTotal = 100;
		int maxThreadsPerHost = 10;
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setConnectionTimeout(Integer.parseInt( InitConfig.getConfigMap().get("http_connection_timeout"))/*10000*/);
		params.setSoTimeout(Integer.parseInt( InitConfig.getConfigMap().get("http_so_timeout"))/*100000*/);
		params.setMaxTotalConnections(maxThreadsTotal);
		if (maxThreadsTotal > maxThreadsPerHost) {
			params.setDefaultMaxConnectionsPerHost(maxThreadsPerHost);
		} else {
			params.setDefaultMaxConnectionsPerHost(maxThreadsTotal);
		}
	}	
}
