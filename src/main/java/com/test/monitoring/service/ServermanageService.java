package com.test.monitoring.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.test.monitoring.util.ESUtil;
import com.test.monitoring.util.MntHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.test.monitoring.constants.GlobalValues;
import com.test.monitoring.constants.WebConstants;

@Service
public class ServermanageService {
	@Autowired
	ElasearchService elasearchService;
	@Autowired
//	CodeService codeService;	
	@Value("${spring.task.name}")
	private String taskName;
	
	private static Logger logger = Logger.getLogger(ServermanageService.class);

	
	public List<Map<String, String>> getCodeList(Map<String, Object> params){
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		params.put("length", 0);
		params.put("start", 0);

		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
		
		params.put("elaIP", "http://"+GlobalValues.getSearchEngineInfo()+"/server-management/_search");
		params.put("indexId", "server-management");
		params.put("SERVER_TYPE", (String) params.get("P_SERVER_TYPE"));
		params.put("INFRA_TYPE", (String) params.get("P_INFRA_TYPE"));
		params.put("HOST_NAME", (String) params.get("P_HOST_NAME"));
		
		System.out.println("\n\n@@@@euiyoung" + (String) params.get("P_HOST_NAME"));
		
		if(params.containsKey("P_SERVER_TYPE") && params.get("P_SERVER_TYPE").equals("WAS")) params.put("MUST_NOT", "StickyLB");//????????? HOST_NAME ??????. ?????? ???????????? ?????? , ??? ???????????? ???????????????.
		else if(params.containsKey("P_SERVER_TYPE") && params.get("P_SERVER_TYPE").equals("CONNECTOR")) params.put("MUST_NOT", "StickyLB");//????????? HOST_NAME ??????. ?????? ???????????? ?????? , ??? ???????????? ???????????????.
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		String sel_code_type = (String) params.get("P_SEL_CODE_TYPE");
    		
    		JSONArray jArr =jsonObj.getJSONObject("aggregations").getJSONObject(sel_code_type).getJSONObject(sel_code_type).getJSONArray("buckets");
    		

    		String key="";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, String> map = new HashMap<>();    			
    			key = jArr.getJSONObject(i).getString("key");
    			if(ESUtil.isNotEmpty(key)) {
    				map.put("KEY", key);
        			results.add(map);
    			}
    		}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findServerList(Map<String, Object> params){
		
		logger.info("findServerList");
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data");
		params.put("orderBy", orderBy);
		params.put("dir", dir);
		
				//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
		
//		logger.info("server info get " + GlobalValues.getSearchEngineInfo());
		
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo()+"/server-management/_search");
		params.put("indexId", "server-management");
		params.put("SERVER_TYPE", (String) params.get("P_SERVER_TYPE"));
		params.put("INFRA_TYPE", (String) params.get("P_INFRA_TYPE"));
		params.put("HOST_NAME", (String) params.get("P_HOST_NAME"));
		
		//System.out.println("url : http://"+result.get(0).get("CD_TP_MEANING")+"/server-management/_search");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //?????????
    		String type="";
    		String id="";
    		String severtype="";
    		String infratype="";
    		String hostname="";
    		String servername="";
    		String posconatip="";
    		String realip="";
    		String serviceip="";
    		String port="";
    		String userid="";
    		String userpw="";
    		String description="";

    	for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //?????????
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); 			
    			severtype = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SERVER_TYPE");
    			infratype = jArr.getJSONObject(i).getJSONObject("_source").has("INFRA_TYPE")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("INFRA_TYPE"):"";
    			hostname = jArr.getJSONObject(i).getJSONObject("_source").has("HOST_NAME")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST_NAME"):"";
    			servername = jArr.getJSONObject(i).getJSONObject("_source").has("SERVER_NAME")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SERVER_NAME"):"";
    			posconatip = jArr.getJSONObject(i).getJSONObject("_source").has("POSCO_NAT_IP")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("POSCO_NAT_IP"):"";
    			realip = jArr.getJSONObject(i).getJSONObject("_source").has("REAL_IP")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("REAL_IP"):"";
    			serviceip = jArr.getJSONObject(i).getJSONObject("_source").has("SERVICE_IP")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SERVICE_IP"):"";
    			port = jArr.getJSONObject(i).getJSONObject("_source").has("PORT")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PORT"):"";
    			userid = jArr.getJSONObject(i).getJSONObject("_source").has("USER_ID")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("USER_ID"):"";
    			userpw = jArr.getJSONObject(i).getJSONObject("_source").has("USER_PW")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("USER_PW"):"" ;
    			description = jArr.getJSONObject(i).getJSONObject("_source").has("DESCRIPTION")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DESCRIPTION"):"";
    			
    			severtype = severtype.replace("??????", "");
    			infratype = infratype.replace("??????", "");
    			hostname = hostname.replace("??????", "");
    			
    			map.put("INDEX", index);
    			map.put("TYPE", type);
    			map.put("ID", id);
    			map.put("SERVER_TYPE", severtype);
    			map.put("INFRA_TYPE", infratype);
    			map.put("HOST_NAME", hostname);
    			map.put("SERVER_NAME", servername);
    			map.put("POSCO_NAT_IP", posconatip);
    			map.put("REAL_IP", realip);
    			map.put("SERVICE_IP", serviceip);
    			map.put("PORT", port);
    			map.put("USER_ID", userid);
    			map.put("USER_PW", userpw);
    			map.put("DESCRIPTION", description);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public String getConfigData(Map<String, Object> params){
		
		String url = params.get("url").toString();
		URL connect;
		OutputStream os = null;
		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		StringBuffer data = new StringBuffer();
    	try
    	{
			connect = new URL("http://"+url);
			httpConn = (HttpURLConnection)connect.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);    		
			//request write
			os = httpConn.getOutputStream();

			os.flush();
			os.close();
			os = null;

			reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
			
			String line;
			while (null != (line = reader.readLine())) {
			data.append(line);
			data.append('\n');
		   }
			httpConn.disconnect();
			httpConn = null;
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return data.toString();
	}

	public String insertServerInfo(Map<String, Object> params){
		
		String rtnmsg = null;
		String parent = null;
		
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
		
		params.put("elaIP", "http://"+GlobalValues.getSearchEngineInfo()+"/_bulk");
		
		//String url="http://10.132.17.121:9200/_bulk/";
//		String url="http://"+result.get(0).get("CD_TP_MEANING")+"/_bulk";
		String url="http://"+GlobalValues.getSearchEngineInfo()+"/_bulk";
		
		
		System.out.println("params : "+params);
		
		//params.put("SERVER_TYPE", (String) params.get("P_SERVER_TYPE"));
		//params.put("INFRA_TYPE", (String) params.get("P_INFRA_TYPE"));
		//params.put("HOST_NAME", (String) params.get("P_HOST_NAME"));
		 	 
		try
		{
			
			HttpClient httpClient = new DefaultHttpClient();

    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();			    		 
				 
			String serverInfoId = (String)params.get("id");
			String serverType = (String)params.get("server_type");
			String infraType = (String)params.get("infra_type");
			String hostName = (String)params.get("host_name");
			String serverName = (String)params.get("server_name");
			String poscoNatIp = (String)params.get("posco_nat_ip");             
			String serverRealIp = (String)params.get("real_ip");
			String serviceIp = (String)params.get("service_ip");
			String port = (String)params.get("port");
			String userId = (String)params.get("user_id");
			String userPw = (String)params.get("user_pw");
			String description = (String)params.get("description");
			  
			if (serverInfoId == "") {
				parent = "{\"index\":{\"_index\":\"server-management\",\"_type\":\"_doc\"}}\n";
			}else {
				parent = "{\"update\":{\"_index\":\"server-management\",\"_type\":\"_doc\",\"_id\":\"" + serverInfoId + "\"}}\n";
				parent += "{\"doc\":";
			}
			  
			parent += "{\"SERVER_TYPE\": \"" + serverType + "\",\"INFRA_TYPE\": \"" + infraType + "\", \"HOST_NAME\": \"" + hostName + "\",\"SERVER_NAME\": \"" + serverName +"\",";
			parent += "\"POSCO_NAT_IP\": \"" + poscoNatIp +"\",\"REAL_IP\": \"" + serverRealIp + "\",\"SERVICE_IP\": \"" + serviceIp + "\",";
			parent += "\"PORT\": \"" + port +"\",\"USER_ID\": \"" + userId +"\",\"USER_PW\": \"" + userPw +"\",\"DESCRIPTION\": \"" + description +"\"";             
			 
			if (serverInfoId == "") {
				parent += "}\n";
			}else {
				parent += "}}\n";
			}

			try {
				HttpPost request = new HttpPost(url);
				    
				System.out.println(parent);
				     
				HttpEntity entity = new ByteArrayEntity(parent.getBytes("UTF-8"));
				
				request.setEntity(entity);
				request.setHeader("Authorization", "Basic cG9zcm9lc2Q6Z3NhYWRtaW4=");
				request.setHeader("Content-Type", "application/json");
				
				HttpResponse response = httpClient.execute(request);
				entity =  response.getEntity();				
				
				if (entity != null) {
					InputStream instream = entity.getContent();					
					rtnmsg = convertStreamToString(instream);
					
					System.out.println("rtnmsg : "+rtnmsg);
					instream.close();
				}
				
				org.apache.http.Header[] headers = response.getAllHeaders();
				for (int i=0 ; i < headers.length; i++) {
					System.out.println(headers[i]);
				}
		
			// handle response here...
			} catch (Exception ex) {
			// handle exception here
			} finally {
				httpClient.getConnectionManager().shutdown();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ServermanageService.insertServerInfo() Exception : " +e.getMessage(), e.getCause());
		} finally {
			try{
			}
			catch(Exception e)
			{
			logger.error(e.getMessage(),e);
			}
		}
		return rtnmsg;
	}
	
	public String deleteServerInfo(Map<String, Object> params){
		
		String rtnmsg = null;
		String parent = "";
				
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
		
		params.put("elaIP", "http://"+GlobalValues.getSearchEngineInfo()+"/_bulk");
		
		//String url="http://10.132.17.121:9200/_bulk/";
		String url="http://"+GlobalValues.getSearchEngineInfo()+"/_bulk";
		
		System.out.println("params : "+params);

		try
		{
			
			HttpClient httpClient = new DefaultHttpClient();
			String retrun = null;

    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();			    		 
				 
			String serverInfoId = (String)params.get("id");
			
			String[] item = serverInfoId.split(",");
			
			for(int i=0; i<item.length; i++) {
				parent += "{\"delete\":{\"_index\":\"server-management\",\"_type\":\"_doc\",\"_id\":\"" + item[i] + "\"}}\n";
			}			

			System.out.println(parent);
			
			try {
				HttpPost request = new HttpPost(url);
				    
				System.out.println(parent);
				     
				HttpEntity entity = new ByteArrayEntity(parent.getBytes("UTF-8"));
				
				request.setEntity(entity);
				request.setHeader("Authorization", "Basic cG9zcm9lc2Q6Z3NhYWRtaW4=");
				request.setHeader("Content-Type", "application/json");
				
				HttpResponse response = httpClient.execute(request);
				entity =  response.getEntity();				
				
				if (entity != null) {
					InputStream instream = entity.getContent();					
					rtnmsg = convertStreamToString(instream);
					
					System.out.println("rtnmsg : "+rtnmsg);
					instream.close();
				}
				
				org.apache.http.Header[] headers = response.getAllHeaders();
				for (int i=0 ; i < headers.length; i++) {
					System.out.println(headers[i]);
				}				
		
			// handle response here...
			} catch (Exception ex) {
			// handle exception here
			} finally {
				httpClient.getConnectionManager().shutdown();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ServermanageService.insertServerInfo() Exception : " +e.getMessage(), e.getCause());
		} finally {
			try{
			}
			catch(Exception e)
			{
			logger.error(e.getMessage(),e);
			}
		}
		return rtnmsg;
	}
	
	public String convertStreamToString(InputStream str) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(str));
		StringBuffer data = new StringBuffer();
		
		String line = null;
    	try
    	{
			while (null != (line = reader.readLine())) {
			data.append(line);
			data.append('\n');
		   }    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return data.toString();
	}


}














