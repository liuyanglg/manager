package com.aisino.admin.companyCard.cardMaintain.utils;

import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.global.context.common.utils.SpringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class CmpCardApiUtils {

	@SuppressWarnings("rawtypes")
	public static CmpCardApiResponse get(String code) throws ClientProtocolException, IOException{
		CmpCardApiResponse rs = null;
		if(code!=null&&code.length()==6){
			Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
			String url = "http://"+globalConfig.get("dataserver.ip") 
					+ ":" + globalConfig.get("dataserver.port")
					+ globalConfig.get("dataserver.cmpApi.get");
			url = url.replaceFirst("\\{\\S*\\}", code);
			CloseableHttpClient closeableHttpClient = null;
			try {
				closeableHttpClient = HttpClients.createDefault();  
		        HttpGet httpGet = new HttpGet(url);
		        httpGet.setHeader("Content-type", "application/json");
		        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
		        HttpEntity entity = httpResponse.getEntity();
		        if(entity!=null){
		        	String ens = EntityUtils.toString(entity);
		        	rs = JSON.parseObject(ens, CmpCardApiResponse.class);
		        }
			} finally{
				if(closeableHttpClient!=null){
					closeableHttpClient.close();
				}
			}
		}
		return rs;
	}
	
	@SuppressWarnings("rawtypes")
	public static CmpCardApiResponse post(String json) throws ParseException, IOException{
		CmpCardApiResponse rs = null;
		if(json!=null){
			Map globalConfig = (Map)SpringUtils.getBean("globalConfig");
			String url = "http://"+globalConfig.get("dataserver.ip") 
					+ ":" + globalConfig.get("dataserver.port")
					+ globalConfig.get("dataserver.cmpApi.post");
			CloseableHttpClient closeableHttpClient = null;
			try {
				closeableHttpClient = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(url);
				StringEntity requestEntity = new StringEntity(json,"utf-8");
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setEntity(requestEntity);
				HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
				HttpEntity entity = httpResponse.getEntity();
				if(entity!=null){
					String ens = EntityUtils.toString(entity);
		        	rs = JSON.parseObject(ens, CmpCardApiResponse.class);
				}
			} finally{
				if(closeableHttpClient!=null){
					closeableHttpClient.close();
				}
			}
		}
		return rs;
	}
	
	@SuppressWarnings("rawtypes")
	public static CmpCardApiResponse put(String code,String json) throws ParseException, IOException{
		CmpCardApiResponse rs = null;
		if(json!=null){
			Map globalConfig = (Map)SpringUtils.getBean("globalConfig");
			String url = "http://"+globalConfig.get("dataserver.ip") 
					+ ":" + globalConfig.get("dataserver.port")
					+ globalConfig.get("dataserver.cmpApi.put");
			url = url.replaceFirst("\\{\\S*\\}", code);
			CloseableHttpClient closeableHttpClient = null;
			try {
				closeableHttpClient = HttpClients.createDefault();
				HttpPut httpPut = new HttpPut(url);
				httpPut.setHeader("Content-type", "application/json");
				StringEntity requestEntity = new StringEntity(json,"utf-8");
				httpPut.setEntity(requestEntity);
				HttpResponse httpResponse = closeableHttpClient.execute(httpPut);
				HttpEntity entity = httpResponse.getEntity();
				if(entity!=null){
					String ens = EntityUtils.toString(entity);
		        	rs = JSON.parseObject(ens, CmpCardApiResponse.class);
				}
			} finally{
				if(closeableHttpClient!=null){
					closeableHttpClient.close();
				}
			}
		}
		return rs;
	}
	
	@SuppressWarnings("rawtypes")
	public static CmpCardApiResponse delete(String code) throws ParseException, IOException{
		CmpCardApiResponse rs = null;
		if(code!=null&&code.length()==6){
			Map globalConfig = (Map)SpringUtils.getBean("globalConfig");
			String url = "http://"+globalConfig.get("dataserver.ip") 
					+ ":" + globalConfig.get("dataserver.port")
					+ globalConfig.get("dataserver.cmpApi.delete");
			url = url.replaceFirst("\\{\\S*\\}", code);
			CloseableHttpClient closeableHttpClient = null;
			try {
				closeableHttpClient = HttpClients.createDefault();
				HttpDelete httpDel = new HttpDelete(url);
				httpDel.setHeader("Content-type", "application/json");
		        HttpResponse httpResponse = closeableHttpClient.execute(httpDel);
		        HttpEntity entity = httpResponse.getEntity();
		        if(entity!=null){
		        	String ens = EntityUtils.toString(entity);
		        	rs = JSON.parseObject(ens, CmpCardApiResponse.class);
		        }
			} finally{
				if(closeableHttpClient!=null){
					closeableHttpClient.close();
				}
			}
		}
		return rs;
	}

	@SuppressWarnings("rawtypes")
	public static CmpCardApiResponse check(String code,String json) throws ParseException, IOException{
		CmpCardApiResponse rs = null;
		if(json!=null){
			Map globalConfig = (Map)SpringUtils.getBean("globalConfig");
			String url = "http://"+globalConfig.get("dataserver.ip")
					+ ":" + globalConfig.get("dataserver.port")
					+ globalConfig.get("dataserver.cmpApi.check");
			url = url.replaceFirst("\\{\\S*\\}", code);
			CloseableHttpClient closeableHttpClient = null;
			try {
				closeableHttpClient = HttpClients.createDefault();
				HttpPut httpPut = new HttpPut(url);
				httpPut.setHeader("Content-type", "application/json");
				StringEntity requestEntity = new StringEntity(json,"utf-8");
				httpPut.setEntity(requestEntity);
				HttpResponse httpResponse = closeableHttpClient.execute(httpPut);
				HttpEntity entity = httpResponse.getEntity();
				if(entity!=null){
					String ens = EntityUtils.toString(entity);
					rs = JSON.parseObject(ens, CmpCardApiResponse.class);
				}
			} finally{
				if(closeableHttpClient!=null){
					closeableHttpClient.close();
				}
			}
		}
		return rs;
	}

	@SuppressWarnings("rawtypes")
	public static CmpCardApiResponse modify(String code,String json) throws ParseException, IOException{
		CmpCardApiResponse rs = null;
		if(json!=null){
			Map globalConfig = (Map)SpringUtils.getBean("globalConfig");
			String url = "http://"+globalConfig.get("dataserver.ip")
					+ ":" + globalConfig.get("dataserver.port")
					+ globalConfig.get("dataserver.cmpApi.modify");
			url = url.replaceFirst("\\{\\S*\\}", code);
			CloseableHttpClient closeableHttpClient = null;
			try {
				closeableHttpClient = HttpClients.createDefault();
				HttpPut httpPut = new HttpPut(url);
				httpPut.setHeader("Content-type", "application/json");
				StringEntity requestEntity = new StringEntity(json,"utf-8");
				httpPut.setEntity(requestEntity);
				HttpResponse httpResponse = closeableHttpClient.execute(httpPut);
				HttpEntity entity = httpResponse.getEntity();
				if(entity!=null){
					String ens = EntityUtils.toString(entity);
					rs = JSON.parseObject(ens, CmpCardApiResponse.class);
				}
			} finally{
				if(closeableHttpClient!=null){
					closeableHttpClient.close();
				}
			}
		}
		return rs;
	}
	
}
