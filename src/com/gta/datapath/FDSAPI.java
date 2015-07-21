package com.gta.datapath;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.HttpHost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder; 
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicNameValuePair;

public class FDSAPI {
	private HttpHost httpHost;
	
	public FDSAPI()
	{
		httpHost = new HttpHost("192.168.101.243",9999,"http");
	}
	
	
	public String getWebTitle(int id)
	{
		String title = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try
		{
			String target = "/threads/" + id;
			HttpGet httpGet = new HttpGet(target);
			CloseableHttpResponse httpResponse = httpClient.execute(httpHost, httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpEntity);
				JSONObject obj = JSONObject.fromObject(entity);
				title = processJSON(obj, "title");
			}
			
		    httpResponse.close();
		    httpClient.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return title;
	}
	
	
	public String getWebContent(int id)
	{
		String content = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try
		{
			String target = "/threads/" + id + "?fieldsets=content";
			HttpGet httpGet = new HttpGet(target);
			CloseableHttpResponse httpResponse = httpClient.execute(httpHost, httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpEntity);
				JSONObject obj = JSONObject.fromObject(entity);
				content = processJSON(obj, "content");
			}
			
		    httpResponse.close();
		    httpClient.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return content;
	}
	
	
	public String getWebTags(int id)
	{
		String tags = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try
		{
			String target = "/threads/" + id + "?fieldsets=tags";;
			HttpGet httpGet = new HttpGet(target);
			CloseableHttpResponse httpResponse = httpClient.execute(httpHost, httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String entity = EntityUtils.toString(httpEntity);
				JSONObject obj = JSONObject.fromObject(entity);
				tags = processJSON(obj, "tags");
			}
			
		    httpResponse.close();
		    httpClient.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return tags;
	}
	
	
	public String processJSON(JSONObject jsonObj, String targetName)
	{
		String element = null;
		if (jsonObj.getString("status").equals("ok"))
		{
		    String dataThread = jsonObj.getString("data");
		    JSONObject thread = JSONObject.fromObject(dataThread);
		    String threadEntity = thread.getString("thread");
		    JSONObject threadElement = JSONObject.fromObject(threadEntity);
		    if (targetName == "content") 
		    {
		    	threadEntity = threadElement.getString("content");
		    	threadElement = JSONObject.fromObject(threadEntity);
		    }
		    element = threadElement.getString(targetName);
		}
		return element;
	}
	
		
	public JSONObject getF(int id)
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", Integer.toString(id)));
//			String param = URLEncodedUtils.format(params, "UTF-8");
			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setParameters(params);
//			String target = "/threads/get"+uriBuilder.build().toString();
			String target = "/threads/"+ id + "?fieldsets=content";
//			String target = "/threads/get?"+param;
			HttpGet httpGet = new HttpGet(target);
			CloseableHttpResponse httpResponse = httpClient.execute(httpHost, httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String entity = EntityUtils.toString(httpEntity);
				JSONObject obj = JSONObject.fromObject(entity);
				httpResponse.close();
				httpClient.close();
				return obj;
			}

			httpResponse.close();
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
/*		
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
*/		
		return new JSONObject();
	}
}
