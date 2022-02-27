package com.test.monitoring.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.monitoring.constants.GlobalValues;

import com.test.monitoring.util.ESUtil;

//import Documents.Office.Poi.ApachePoiTextRead;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Service
public class MonitoringService {
//	@Autowired
//	MonitoringMapper monitoringMapper;
	@Autowired
	ElasearchService elasearchService;
	
	@Autowired
	AnalysisService analysisService;
	
	@Autowired
	CodeService codeService;
	
	private String search_engine_ip = "";
	
	@Value("${server.running.flag}")
	private String server_running_flag = "";
	
	@Value("${local.search_engine}")
	private String local_elasticsearch = "";
	
	@Value("${server.search_engine}")
	private String server_elasticsearch = "";
	
	
//	private static Boolean isPolling = false;
	
	private static int[] CPU = new int[10000];
	private static int[] JVM = new int[10000];
	
	private static Logger logger = Logger.getLogger(MonitoringService.class);
	
	
	public Map<String, Object> getSearchEngineIndicesList(Map<String, Object> params)
	{
//		logger.info("getSearchEngineIndicesList >> " + params);
//		logger.info("getSearchEngineIndicesList >> " + params.get("start"));
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where = search.get("value");
//		if(search.get("value") != null && !"".equals(search.get("value")))
//			where=search.get("value");
//			params.put("where", where);
			
//		params.put("start", (Integer) params.get("start"));
		
		String key ="";
		String value="";
		Map<String, Object> results = new HashMap<>();
		
//		{recordsFiltered=3, data=[{JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=green, SHARDS=7, LOAD=0.09, NODE=ES_DATA_18,10.132.57.81:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}], recordsTotal=3}
		
		if (params.get("version_params") != null) {
			if (server_running_flag.equals("Y")) {
				search_engine_ip = this.server_elasticsearch;
			}
			else {
				search_engine_ip = this.local_elasticsearch;
			}
		}
		else {
			return null;
		}
		
		// MASTER 여부 확인
		String result="";
		params.put("elaIP", "http://" + search_engine_ip + "/_cat/indices?format=txt");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
				result= elasearchService.handleElaCommonString(params);
				String[] getIndicesResults = result.split("\n");
//				for(int i=0; i < getIndicesResults.length; i++) {
//					String[] values = getIndicesResults[i].split(" ");
//					for(int h=0; h < values.length; h++)
//						if (values[h] != " " && values[h] != null && !values[h].isEmpty())
//							System.out.println(h+ "," + values[h]);
//				}
				results.put("recordsTotal", getIndicesResults.length);
				results.put("recordsFiltered",getIndicesResults.length);
				String[] results_str = new String[100];
				for(int i=(int)params.get("start"); i < getIndicesResults.length; i++) {
					Map<String, Object> map = new HashMap<>();
					String[] values = getIndicesResults[i].split(" ");
					int k=0;
					for(int h=0; h < values.length; h++)
						if (values[h] != " " && values[h] != null && !values[h].isEmpty()) {
							results_str[k++] = values[h];
//							System.out.println(h+ "," + values[h]);
						}
					if(where != "" && where != null) {
						if(getIndicesResults[i].split(" ")[2].startsWith(where)) {
//							System.out.println("OK");
							map.put("STATUS", results_str[0]);
							map.put("INDICES", results_str[2]);
							map.put("UID", results_str[3]);
							map.put("PR_RE_SET", results_str[4] + '/' + results_str[5]);
							map.put("TOTAL_COUNT", results_str[6]);
//							map.put("PR_RE_SET", getIndicesResults[i].split(" ")[4] + '/' + getIndicesResults[i].split(" ")[5]);
							map.put("DELETED", results_str[7]);
							map.put("STORED", results_str[8].toUpperCase());
							list.add(map);
						}
					}
					else {
//						System.out.println("FAIL");
						map.put("STATUS", results_str[0]);
						map.put("INDICES", results_str[2]);
						map.put("UID", results_str[3]);
						map.put("PR_RE_SET", results_str[4] + '/' + results_str[5]);
						map.put("TOTAL_COUNT", results_str[6]);
						map.put("DELETED", results_str[7]);
						map.put("STORED", results_str[8].toUpperCase());
						list.add(map);
					}
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		if (list.size() > 0)
			results.put("data", list);

		logger.info("getSearchEngineIndicesList >> " + results.toString());
		return results;
	}
	
	public Map<String, Object> getSearchEngineNodeList(Map<String, Object> params)
	{
//		isPolling = false;
		
		String key ="";
		String value="";
		Map<String, Object> results = new HashMap<>();
		
//		System.out.println(isPolling);
//		{recordsFiltered=3, data=[{JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=green, SHARDS=7, LOAD=0.09, NODE=ES_DATA_18,10.132.57.81:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}], recordsTotal=3}
		
		if (params.get("version_params") != null) {
			if (server_running_flag.equals("Y")) {
				search_engine_ip = this.server_elasticsearch;
			}
			else {
				search_engine_ip = this.local_elasticsearch;
			}
		}
		else {
			return null;
		}
		
		// MASTER 여부 확인
		String isMasterInfo = "";
		params.put("elaIP", "http://" + search_engine_ip + "/_cat/master?format=txt");
		try {
				String result= elasearchService.handleElaCommonString(params);
				isMasterInfo = result.split(" ")[1];
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}

		params.put("elaIP", "http://"+search_engine_ip+"/_nodes/stats?human&pretty");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
    	try
	    {
    			JSONObject jsonObj  = elasearchService.handleElaCommon(params);
	    		ObjectMapper mapper = new ObjectMapper();
	    		Object json = mapper.readValue(jsonObj.toString(), Object.class);
	    		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
;
	    		JSONObject jsonObjSub = JSONObject.fromObject(jsonObj.getString("nodes"));
	    		results.put("recordsTotal", jsonObjSub.size());
    			results.put("recordsFiltered", jsonObjSub.size());
    			    
    			String[] node_key = new String[jsonObjSub.size()];
    			String[] node_ip = new String[jsonObjSub.size()];

    			Iterator iter = jsonObjSub.keys();
    			int i=0;
				while (iter.hasNext()) {
					key = (String) iter.next();
//					node_key[i] = key;
					value = jsonObjSub.getString(key);
					JSONObject jsonObjdetail = JSONObject.fromObject(value);
					node_ip[i++] = jsonObjdetail.getString("host").toString() + "," + key;
				}
				
				Arrays.sort(node_ip);
				for (int j = 0; j < node_ip.length; j++)  {
					node_key[j] = node_ip[j].split(",")[1];
//					System.out.println(node_ip[j]);
				}
				
				for (int j = 0; j < node_key.length; j++) 
				{
					value = jsonObjSub.getString(node_key[j]);
					JSONObject jsonObjdetail = JSONObject.fromObject(value);
					Iterator iter1 = jsonObjdetail.keys();
					while (iter1.hasNext()) {
						Map<String, Object> map = new HashMap<>();
						key = (String) iter1.next();
						value = jsonObjdetail.getString(key);
						if (key.equals("name")) {
							map.put("NODE",jsonObjdetail.getString("name") + "," + jsonObjdetail.getString("transport_address"));
							JSONArray orderOne = (JSONArray) jsonObjdetail.get("roles");
							String node_roles = "";
							for (int k = 0; k < orderOne.size(); k++) {
								node_roles += orderOne.getString(k).toString().toUpperCase();
								if (k != orderOne.size()-1) {
									node_roles += ",";
								}
							}
							
							for (int k = 0; k < orderOne.size(); k++) {
								if (orderOne.getString(k).toString().toUpperCase().equals("MASTER")) {
									if (jsonObjdetail.getString("host").equals(isMasterInfo)) {
										map.put("MASTER", "★");
									}
									else {
										map.put("MASTER", "☆");
									}
									break;
								} else {
									map.put("MASTER", "");
								}
							}
							map.put("ROLES", node_roles);
							
//							System.out.println(JSONObject.fromObject(jsonObjdetail.getJSONObject("indices").getJSONObject("docs")).getString("count"));
							map.put("INDICES", JSONObject.fromObject(jsonObjdetail.getJSONObject("indices").getJSONObject("docs")).getString("count") + "<br />"
											+ "(deleted:" + JSONObject.fromObject(jsonObjdetail.getJSONObject("indices").getJSONObject("docs")).getString("deleted")
											+ ")"
									);
							
							JSONObject obj_cpu = JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getString("cpu"));
							if ( CPU[j] < Integer.parseInt(obj_cpu.getString("percent").toString())) {
								map.put("CPU_STATUS", "↑");
							}
							else if ( CPU[j] > Integer.parseInt(obj_cpu.getString("percent").toString())) {
								map.put("CPU_STATUS", "↓");
							}
							else {
								map.put("CPU_STATUS", "");
							}
							CPU[j] = Integer.parseInt(obj_cpu.getString("percent").toString());
							map.put("CPU", obj_cpu.getString("percent").toString() + "%");
							map.put("LOAD", JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getJSONObject("cpu").getJSONObject("load_average")).getString("1m") + "<br />"
									+ "(5m:" + JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getJSONObject("cpu").getJSONObject("load_average")).getString("5m")
									+ "->15m:" + JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getJSONObject("cpu").getJSONObject("load_average")).getString("15m")
									+ ")"
									
									);
							JSONObject obj_jvm = JSONObject.fromObject(jsonObjdetail.getJSONObject("jvm").getString("mem"));
							
							String JVM_STATUS = "";
							if ( JVM[j] < Integer.parseInt(obj_jvm.getString("heap_used_percent").toString())) {
								JVM_STATUS = "↑";
							}
							else if ( JVM[j] > Integer.parseInt(obj_jvm.getString("heap_used_percent").toString())) {
								JVM_STATUS = "↓";
							}
							else {
								JVM_STATUS = "";
							}
							JVM[j] = Integer.parseInt(obj_jvm.getString("heap_used_percent").toString());
							
							map.put("JVM", "<b>" + obj_jvm.getString("heap_used_percent").toString() + "% " + JVM_STATUS + "</b><br />("
											+ obj_jvm.getString("heap_used").toString().toUpperCase() + " / "
											+ String.format("%.2f",(Double.parseDouble(obj_jvm.getString("heap_max_in_bytes").toString())/ (1024 * 1024 * 1024)))
											+ "GB)");
							
							JSONObject obj_fs = JSONObject.fromObject(jsonObjdetail.getJSONObject("fs").getString("total"));
							map.put("FS", String.format("%.2f",(Double.parseDouble(obj_fs.getString("available_in_bytes").toString()) / Double.parseDouble(obj_fs.getString("total_in_bytes").toString()) * 100.0))
									+ "% <br />(" + obj_fs.getString("free").toString().toUpperCase() + " / "
									+ obj_fs.getString("total").toString().toUpperCase() + ")");
							list.add(map);
	
						}
					}
					results.put("data", list);
				}
	    			
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	
//    	logger.info("getSearchEngineNodeList >> " + results.toString());
		return results;
	}
	
	 //pretty print a map
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() 
				+ " Value : " + entry.getValue());
        }
    }
	
	public Map<String, Object> getSearchEngineStatus(Map<String, Object> params)
	{
//		isPolling = false;
		
		String key ="";
		String value="";
		Map<String, Object> results = new HashMap<>();
		
//		params.put("version_params", params.get("version_params"));
		
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		if (this.server_running_flag.toUpperCase().equals("Y")) {
//			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
//		} else {
//			search_engine_ip = this.cluster_elasticsearch;
//		}
		
		if (params.get("version_params") != null) {
			if (server_running_flag.equals("Y")) {
				search_engine_ip = this.server_elasticsearch;
			}
			else {
				search_engine_ip = this.local_elasticsearch;
			}
		} else {
			return null;
		}

		  params.put("elaIP", "http://"+search_engine_ip+"/_cluster/health");
    	try
	    	{
	    		JSONObject jsonObj  = elasearchService.handleElaCommon(params);
	    		ObjectMapper mapper = new ObjectMapper();
	    		Object json = mapper.readValue(jsonObj.toString(), Object.class);
	    		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//	    		System.out.println(indented);
//	    		System.out.println(jsonObj.get("cluster_name").toString());
	    		Map<String,String> map = new HashMap<String,String>();
	    		Iterator iter = jsonObj.keys();
	    		while(iter.hasNext()) {
	    		   key = (String)iter.next();
	    		   value = jsonObj.getString(key);
	    		   results.put(key,value);
	    		}
//	    		System.out.println("############################################");
//	    		System.out.println("cluster_health >> " + results.toString());
//	    		System.out.println("############################################");
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	
    	params.put("elaIP", "http://"+search_engine_ip+"/_cluster/stats?human&pretty");
    	try
	    	{
	    		JSONObject jsonObj  = elasearchService.handleElaCommon(params);
	    		ObjectMapper mapper = new ObjectMapper();
	    		Object json = mapper.readValue(jsonObj.toString(), Object.class);
	    		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//	    		System.out.println(indented);
//	    		System.out.println(jsonObj.get("jvm").get.toString());
	    		Map<String,String> map = new HashMap<String,String>();
	    		JSONObject jsonObjSub = JSONObject.fromObject(jsonObj.getString("indices"));
	    		Iterator iter = jsonObjSub.keys();
	    		while(iter.hasNext()) {
	    		   key = (String)iter.next();
	    		   value = jsonObjSub.getString(key);
//	    		   System.out.println("key " + key + ", value " + value);
	    		   results.put(key,value);
	    			}
	    		
	    		JSONObject jsonObj_nodes = JSONObject.fromObject(jsonObj.getString("nodes"));
	    		iter = jsonObj_nodes.keys();
	    		while(iter.hasNext()) {
	    		   key = (String)iter.next();
	    		   value = jsonObj_nodes.getString(key);
//	    		   System.out.println("key " + key + ", value " + value);
	    		   if (key.equals("count") ) {
	    		   	    	  results.put("nodes"+key,value);
	    		   	      }else {
	    		   	    	results.put(key,value);
	    		   	      }
	    		   }
	    			
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	
//    	results.put("data", "elastic");
//    	logger.info("getSearchEngineStatus >> " + results.toString());
    	return results;
	}

	
	public Map<String, Object> findWasServerLog(Map<String, Object> params)
	{
//		isPolling = false;
		
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		if (this.server_running_flag.toUpperCase().equals("Y")) {
//			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
//		} else {
//			search_engine_ip = this.local_elasticsearch;
//		}
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/was-server-log-*/_search");
		params.put("indexId", "was-server-log-*");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String timestamp="";
    		String logPath = ""; //로그패스
    		String host=""; //호스트명
    		String logLevel="";  //로그레벨
    		String handler="";  //핸들러
    		String message=""; //메시지
    		String sso_user_name ="";
    		String sso_login_id="";
    		String sso_emp_no="";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			logPath = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PATH"); //로그패스

    			host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명

    			//LOG_LEVEL 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("LOG_LEVEL")){
    				logLevel = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_LEVEL");
    			} else { //LOG_LEVEL 없을때는 초기화
    				logLevel = "";
    			}

    			//HANDLER 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HANDLER")){
    				handler = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HANDLER");
    			} else{ //HANDLER 없을때는 초기화
    				handler = "";
    			}

    			//SSO_USER_NAME 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_USER_NAME")){
    				sso_user_name = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_USER_NAME");
    			}else{ //MAIL_ID 없을때는 초기화
    				sso_user_name = "";
    			}

    			//SSO_LOGIN_ID 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_LOGIN_ID")){
    				sso_login_id = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_LOGIN_ID");
    			}else{ //MAIL_ID 없을때는 초기화
    				sso_login_id = "";
    			}

    			//SSO_EMP_NO 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_EMP_NO")){
    				sso_emp_no = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_EMP_NO");
    			}else{ //MAIL_ID 없을때는 초기화
    				sso_emp_no = "";
    			}

    			try{
    				if (jArr.getJSONObject(i).getJSONObject("_source").has("LOG_TEXT")) {
    					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("LOG_TEXT").get(0);
    					}
    				else {
    					message = "";
    				}
					
				}catch(Exception ee){
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_TEXT");
    			}
    			
    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

        			map.put("INDEX", index);
        			map.put("ID", id);
        			map.put("LOG_PATH", logPath);
        			map.put("TIMESTAMP", timestamp);
        			map.put("HOST", host);
        			map.put("LOG_LEVEL", logLevel);
        			map.put("HANDLER", handler);
        			map.put("SSO_LOGIN_ID", sso_login_id);
        			map.put("SSO_EMP_NO", sso_emp_no);
        			map.put("SSO_USER_NAME", sso_user_name);
        			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));

    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findAIExtractResultList(Map<String, Object> params)
	{
//		isPolling = false;
		
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		if (this.server_running_flag.toUpperCase().equals("Y")) {
//			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
//		} else {
//			search_engine_ip = this.local_elasticsearch;
//		}
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/UNFAIR-MASTER-MANUAL/_search");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String timestamp="";
    		String docTitle="";  //문서명
    		String ownerName="";    		//소유자
    		String ownerDeptName="";    		//소속부서
    		String updatedTimestamp="";    		//검출일자
    		String negCnt="";    		//불공정건수
    		String totalCnt="";    		//전체건수
    		String seq="";    		//키값
    		String progressStatus="";	//값이 T일 경우 정상

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID

    			//DOC_TITLE 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("DOC_TITLE")){
    				docTitle = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DOC_TITLE");
    			} else { //DOC_TITLE 없을때는 초기화
    				docTitle = "";
    			     }

    			//OWNER_NAME 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("OWNER_NAME")){
    				ownerName = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("OWNER_NAME");
    			} else { //OWNER_NAME 없을때는 초기화
    				ownerName = "";
    			     }

    			//OWNER_DEPT_NAME 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("OWNER_DEPT_NAME")){
    				ownerDeptName = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("OWNER_DEPT_NAME");
    			} else { //OWNER_DEPT_NAME 없을때는 초기화
    				ownerDeptName = "";
    			     }

    			//UPDATED_TIMESTAMP 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("UPDATED_TIMESTAMP")){
    				updatedTimestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("UPDATED_TIMESTAMP");
    			} else { //UPDATED_TIMESTAMP 없을때는 초기화
    				updatedTimestamp = "";
    			     }

    			//NEG_CNT 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("NEG_CNT")){
    				negCnt = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("NEG_CNT");
    			} else { //NEG_CNT 없을때는 초기화
    				negCnt = "";
    			     }

    			//TOTAL_CNT 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TOTAL_CNT")){
    				totalCnt = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TOTAL_CNT");
    			} else { //NEG_CNT 없을때는 초기화
    				totalCnt = "";
    			     }

    			//SEQ 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SEQ")){
    				seq = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SEQ");
    			} else { //SEQ 없을때는 초기화
    				seq = "";
    			     }
    			
    			//PROGRESS_STATUS 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("PROGRESS_STATUS")){
    				progressStatus = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PROGRESS_STATUS");
    			} else { //SEQ 없을때는 초기화
    				progressStatus = "";
    			     }
    			
    			

    			
				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

    			map.put("DOC_TITLE", docTitle);
    			map.put("OWNER_NAME", ownerName);
    			map.put("OWNER_DEPT_NAME", ownerDeptName);
    			map.put("UPDATED_TIMESTAMP", updatedTimestamp);
    			map.put("NEG_CNT", negCnt);
    			map.put("TOTAL_CNT", totalCnt);
    			map.put("SEQ", seq);
    			map.put("START_DATE", params.get("START_DATE"));
    			map.put("END_DATE", params.get("END_DATE"));
    			map.put("PROGRESS_STATUS", progressStatus);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));

    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findAIExtractResultDetail(Map<String, Object> params)
	{
//		isPolling = false;
		
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
		
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		
		params.put("orderName", columns.get(column).get("data"));
		params.put("orderVal", (String) order.get(0).get("dir"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		if (this.server_running_flag.toUpperCase().equals("Y")) {
//			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
//		} else {
//			search_engine_ip = this.local_elasticsearch;
//		}
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/UNFAIR-SLAVE-MANUAL/_search");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String timestamp="";
    		String lawName="";
    		String subLawName="";
    		String detectSentence="";
    		String detectUnfairYN="";
    		String seq="";    		//키값

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID

    			//LAW_NAME 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("LAW_NAME")){
    				lawName = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LAW_NAME");
    			} else { //LAW_NAME 없을때는 초기화
    				lawName = "";
    			     }

    			//SUB_LAW_NAME 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SUB_LAW_NAME")){
    				subLawName = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SUB_LAW_NAME");
    			} else { //SUB_LAW_NAME 없을때는 초기화
    				subLawName = "";
    			     }

    			//DETECT_SENTENCE 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("DETECT_SENTENCE")){
    				detectSentence = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DETECT_SENTENCE");
    			} else { //DETECT_SENTENCE 없을때는 초기화
    				detectSentence = "";
    			     }

    			//DETECT_UNFAIR_YN 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("DETECT_UNFAIR_YN")){
    				detectUnfairYN = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DETECT_UNFAIR_YN");
    			} else { //DETECT_UNFAIR_YN 없을때는 초기화
    				detectUnfairYN = "";
    			     }

    			//SEQ 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SEQ")){
    				seq = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SEQ");
    			} else { //SEQ 없을때는 초기화
    				seq = "";
    			     }
    			
    		 	timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

    			map.put("LAW_NAME", lawName);
    			map.put("SUB_LAW_NAME", subLawName);
    			map.put("DETECT_SENTENCE", detectSentence);
    			map.put("DETECT_UNFAIR_YN", detectUnfairYN);
    			map.put("SEQ", seq);
    			map.put("START_DATE", params.get("START_DATE"));
    			map.put("END_DATE", params.get("END_DATE"));

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));

    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	public Map<String, Object> findAIServerLog(Map<String, Object> params)
	{
//		isPolling = false;
		
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		if (this.server_running_flag.toUpperCase().equals("Y")) {
//			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
//		} else {
//			search_engine_ip = this.local_elasticsearch;
//		}
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/ai-server-log-*/_search");
		params.put("indexId", "ai-server-log-*");
		
    	try
    	{
    		System.out.println("@@@@@@@@@@@@@AI SERVER " + params);
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String timestamp="";
    		String logPath = ""; //로그패스
    		String host=""; //호스트명
    		String logLevel="";  //로그레벨
    		String handler="";  //핸들러
    		String message=""; //메시지
    		String sso_user_name ="";
    		String sso_login_id="";
    		String sso_emp_no="";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			logPath = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PATH"); //로그패스

    			host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명

    			//LOG_LEVEL 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("LOG_LEVEL")){
    				logLevel = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_LEVEL");
    			} else { //LOG_LEVEL 없을때는 초기화
    				logLevel = "";
    			}

    			//HANDLER 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HANDLER")){
    				handler = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HANDLER");
    			} else{ //HANDLER 없을때는 초기화
    				handler = "";
    			}

    			//SSO_USER_NAME 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_USER_NAME")){
    				sso_user_name = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_USER_NAME");
    			}else{ //MAIL_ID 없을때는 초기화
    				sso_user_name = "";
    			}

    			//SSO_LOGIN_ID 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_LOGIN_ID")){
    				sso_login_id = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_LOGIN_ID");
    			}else{ //MAIL_ID 없을때는 초기화
    				sso_login_id = "";
    			}

    			//SSO_EMP_NO 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_EMP_NO")){
    				sso_emp_no = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_EMP_NO");
    			}else{ //MAIL_ID 없을때는 초기화
    				sso_emp_no = "";
    			}

    			try{
    				if (jArr.getJSONObject(i).getJSONObject("_source").has("LOG_TEXT")) {
    					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("LOG_TEXT").get(0);
    					}
    				else {
    					message = "";
    				}
					
				}catch(Exception ee){
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_TEXT");
    			}
    			
    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

        			map.put("INDEX", index);
        			map.put("ID", id);
        			map.put("LOG_PATH", logPath);
        			map.put("TIMESTAMP", timestamp);
        			map.put("HOST", host);
        			map.put("LOG_LEVEL", logLevel);
        			map.put("HANDLER", handler);
        			map.put("SSO_LOGIN_ID", sso_login_id);
        			map.put("SSO_EMP_NO", sso_emp_no);
        			map.put("SSO_USER_NAME", sso_user_name);
        			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));

    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	

	public Map<String, Object> findAITraindataLog(Map<String, Object> params)
	{
//		isPolling = false;
		
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		if (this.server_running_flag.toUpperCase().equals("Y")) {
//			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
//		} else {
//			search_engine_ip = this.local_elasticsearch;
//		}
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/unfair-slave-manual-*/_search");
		params.put("indexId", "unfair-slave-manual-*");
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String consultant_name="";
    		String last_update_date="";
    		String detect_sentence="";
    		String cousult_opinion = "";
    		String predict_lable="";
    		String consult_lable="";
    		String message = "";
    	      		    

    	 Map<String, Object> map;
    	 for(int i=0; i<jArr.size() ;i++)
    		   {
    			map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			
    			detect_sentence = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DETECT_SENTENCE");
    			predict_lable = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DETECT_UNFAIR_YN");
    			consult_lable = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("CONSULT_UNFAIR_YN");
    			
    			message =  ESUtil.getMetaDatas(jArr.getJSONObject(i).getJSONObject("_source"), "DETECT_SENTENCE");
    			cousult_opinion =  ESUtil.getMetaDatas(jArr.getJSONObject(i).getJSONObject("_source"), "CONSULT_OPINION");
    			consultant_name =  ESUtil.getMetaDatas(jArr.getJSONObject(i).getJSONObject("_source"), "CONSULTANT_NAME");
    			last_update_date =  ESUtil.getMetaDatas(jArr.getJSONObject(i).getJSONObject("_source"), "LAST_UPDATE_TIMESTAMP");
	  		
    		  map.put("INDEX", index);
	       map.put("ID", id);
	       map.put("CONSULTANT_NAME", consultant_name);
	       map.put("DETECT_SENTENCE", detect_sentence);
	       map.put("CONSULT_OPINION", cousult_opinion);
	       map.put("LAST_UPDATE_DATE", last_update_date);
	       map.put("PREDICT_LABEL", predict_lable);
	       map.put("CONSULT_LABEL", consult_lable);
	       map.put("MESSAGE", message);
	       	       
	       list.add(map);
	       
	     
//	       if (cousult_opinion.length() > 0)  {
//	    	   		  map = new HashMap<>();
//	    	   			System.out.println("@@@@@@@@@ ADD @@@@@@@@" + cousult_opinion);
//			    	    map.put("INDEX", index);
//				       map.put("ID", id);
//				       map.put("CONSULTANT_NAME", consultant_name);
//				       map.put("DETECT_SENTENCE", cousult_opinion);
//				       map.put("LAST_UPDATE_DATE", last_update_date);
//				       map.put("LABEL", lable);
//				       map.put("MESSAGE", cousult_opinion);
//				       list.add(map);
//	       			}
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));

    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		
    		results.put("data", list);
    		
    		System.out.println("was_return >> " +  results);
    	
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	

	public Map<String, Object> findAITraindata_Ratio(Map<String, Object> params)
	{
		String Extracts = "";
		System.out.println("callTextIF on MonitoringService");
   try
    	{

    		System.out.println("Call");
    	
    	 String source = "/ES/18032432902295348.docx";

    		//Extracts = new ApachePoiTextRead(source).CommonTextMain();
    		System.out.println("\n\n@@@@");
    		System.out.println(Extracts);
    		System.out.println("\n\n@@@@");
    	}
    catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
			
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		params.put("length", 10000);
		params.put("start", 0);
				
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/unfair-slave-manual-*/_search");
		params.put("indexId", "unfair-slave-manual-*");
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();


    		String predict_lable="";
    		String consult_lable="";
    	  Integer error_count = 0; 
    		    

    	 Map<String, Object> map;
    	 for(int i=0; i<jArr.size() ;i++)
    		   {
    			map = new HashMap<>();

    			predict_lable = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DETECT_UNFAIR_YN");
    			consult_lable = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("CONSULT_UNFAIR_YN");
   
	       map.put("PREDICT_LABEL", predict_lable);
	       map.put("CONSULT_LABEL", consult_lable);
	       	       
	       list.add(map);
	       
	      if (!(predict_lable.equals(consult_lable))) {
	    	  					System.out.println("arrSize > " + jArr.size() + ", !(predict_lable.equals(consult_lable) >> " + String.valueOf(i) + " > " + predict_lable + " > " + consult_lable);
	            	  	error_count += 1;
	              }
	       
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
    			results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));

    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		
    		results.put("data", list);
    		    
    		Integer Total_Count = Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value"));
    		
    		if (Total_Count < 1) {
    			  results.put("error_ratio", (float)(0));
    		    }
    		else {
    			results.put("error_ratio", ((float)((Total_Count-error_count)/(float)Total_Count)*100.0));
    		  }
    	
    		System.out.println("findAITraindata_Ratio was_return >> " +  results);
    		System.out.println("findAITraindata_Ratio error_count >> " +  error_count);
    		System.out.println("findAITraindata_Ratio >> " +  Math.round(((float)((Total_Count-error_count)/(float)Total_Count))*100.0));
    		System.out.println("\n\n\n");
    		
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	public Map<String, Object> callTextIF(Map<String, Object> params) {
		Map<String, Object> results = new HashMap<>();
		System.out.println("callTextIF on MonitoringService");
    try
    	{
    		
    		params.put("elaIP", "http://"+ GlobalValues.getRESTServer() +"/classification/analysis/");
    		System.out.println("server.rest_url: >> " + GlobalValues.getRESTServer());
    		
    		JSONObject jsonObj = analysisService.handleAnalysis(params);
    		
    		JSONArray jArr =jsonObj.getJSONArray("data");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		
    		String sentence = "";
    		String predict = "";
    		String probability = "";


    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			//sentence 존재 체크
    			if( jArr.getJSONObject(i).has("sentence")){
    				sentence = (String) jArr.getJSONObject(i).getString("sentence");
    			} else { //sentence 없을때는 초기화
    				sentence = "";
    			     }

    			//predict 존재 체크
    			if( jArr.getJSONObject(i).has("predict")){
    				predict = (String) jArr.getJSONObject(i).getString("predict");
    			} else { //predict 없을때는 초기화
    				predict = "";
    			     }

    			//probability 존재 체크
    			if( jArr.getJSONObject(i).has("probability")){
    				probability = (String) jArr.getJSONObject(i).getString("probability");
    			} else { //probability 없을때는 초기화
    				probability = "";
    			     }	

    			map.put("sentence", sentence);
    			map.put("predict", predict);
    			map.put("probability", probability);

    			list.add(map);
    			}
    		results.put("result_code", "success");    		
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String,Object> fileUpload(MultipartHttpServletRequest mReq){
		   Map<String,Object> resultMap = new HashMap<String,Object>();
		   Map<String,Object> params = new HashMap<String,Object>();
//		   String line;
		   String uploadPath = GlobalValues.getUploadPath();
		   String uploadFileText = "test";
		 
		   Iterator<String> iter = mReq.getFileNames();
		   //String upload_file_name = mReq.getParameter("uploadfilenm");
		   
		   if(iter.hasNext() == false){
		      resultMap.put("message", "파일이 없습니다.");
		   } else {
			   while (iter.hasNext()) {
				   String uploadFileNm = iter.next();
				   MultipartFile mFile = mReq.getFile(uploadFileNm);
				   String originalFileName = mFile.getOriginalFilename();
				   String saveFileName = originalFileName;
				   
				   if(ESUtil.isNotEmpty(saveFileName)) {
					   if(new File(uploadPath + saveFileName).exists()) {
						   saveFileName = saveFileName + "_" + System.currentTimeMillis();
						   }
					   }
				   
				   logger.info("uploadPath + saveFileName="+uploadPath + saveFileName);
				   
				   try{
					   
					   File file = new File(uploadPath + saveFileName);

					   mFile.transferTo(file);
					   //uploadFileText = new ApachePoiTextRead(source).CommonTextMain();
					   
/*					   InputStream iStream = mFile.getInputStream();
					   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
					   StringBuffer sb = new StringBuffer();
//					   System.out.println("originalFileName:"+originalFileName);
					   while ((line = bufferedReader.readLine()) != null) {
//						   System.out.println(line);
						   sb.append(line);
						   }
					   bufferedReader.close();*/

					   params.put("textParam", uploadFileText);
					   params.put("fileName", originalFileName);
					   
					   if(file.exists()) {
						   if(file.delete()){
							   logger.info("file delete success!!");
						   }else {
							   logger.info("file delete error!!");
							   }
						   }
					   
					   } catch(Exception e) {
						   logger.error(e.getMessage(), e.getCause());
						  }
				   }
			   }
		   
		   resultMap = callTextIF(params);
		   resultMap.put("msg", "success");
		   return resultMap;
		}
	

	
	/**
	 * elastic_query.xml에 작성 되어있는 쿼리 정보를 EngineQueryVO 객체에 담아 리턴한다,
	 * @return Node
	 * @throws Exception
	 */
/*	@SuppressWarnings("resource")
	protected synchronized String getSystemQueryInfo(String comCode) throws Exception {
		logger.info("##### GETSYSTEMQUERYNODE START #####");
		String rtn_metalist = null;
		StringBuffer sb =  new StringBuffer();
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		   
		final String queryPath = "http://uswpes.posco.net:7091/pic/system_query";
		String strURL = queryPath + "/" + "elastic_query.xml";
		URL url = new URL(strURL);
		
		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			String elastic_query = "";
			
			
			OutputStream out = httpConn.getOutputStream();
			out.write(elastic_query.getBytes("UTF-8"));
			out.flush();
			out.close();
			out = null;
			
			br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() UnknownHostException : "+ uhe.getMessage() );
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() FileNotFoundException : "+ fnfe.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() Exception : "+ e.getMessage() );
		}
		
		String read = "";
		
		while ((read = br.readLine()) != null) {
			sb.append(read);			       
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));
					
		NodeList sysList = doc.getElementsByTagName("system");
		Node sysNode = null;
		int nodeSize = sysList.getLength();
		for (int i=0; i<nodeSize; i++) {
			Element tmp = (Element) sysList.item(i);
			
			 * elastic_query.xml에 사용자의 회사 코드로 작성 되어진 쿼리 목록을 가져온다.
			 * <system id="POSCO" name="POSCO" companyCode="30">
			 *     <query></query>
			 *     ....
			 * </system>
			 
			if (tmp.getAttribute("companyCode").equals(comCode)) {
				sysNode = sysList.item(i);
				break;
			}
		}
		
		
		 * elastic_query.xml에 회사 코드로 쿼리를 만들어 줘야 함
		 * <system id="POSCO" name="POSCO" companyCode="30"></system>
		 
		if (sysNode != null) {
			Element element = (Element) sysNode;				
			NodeList metaList = element.getElementsByTagName("meta");
			StringBuffer sb1 = new StringBuffer();
			for (int i=0; i<metaList.getLength(); i++) {
				Element tmp = (Element) metaList.item(i);
				if(sb1.length()>0) sb1.append(",");
				sb1.append(tmp.getAttribute("id"));
			}
			if(sb1.length()>0) rtn_metalist = sb1.toString();
		}
		
		logger.info("rtn_metalist="+rtn_metalist);
		logger.info("##### GETSYSTEMQUERYNODE END #####");
		
		return rtn_metalist;
	}*/
	
	
	/**
	 * elastic_query_PATT.xml에 작성 되어있는 쿼리 정보를 EngineQueryVO 객체에 담아 리턴한다,
	 * @return Node
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	protected synchronized String getSystemQueryInfo(String comCode,String systemID) throws Exception {
		logger.info("##### GETSYSTEMQUERYNODE START #####");
		String rtn_metalist = null;
		StringBuffer sb =  new StringBuffer();
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		   
		final String queryPath = "http://uswpes.posco.net:7091/pic/system_query";
		String fileName = "";
		if(systemID.contains("patt")) fileName = "elastic_query_PATT.xml";
		else fileName = "elastic_query.xml";
		
		String strURL = queryPath + "/" + fileName;
		URL url = new URL(strURL);
		
		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			String elastic_query = "";
			
			
			OutputStream out = httpConn.getOutputStream();
			out.write(elastic_query.getBytes("UTF-8"));
			out.flush();
			out.close();
			out = null;
			
			br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() UnknownHostException : "+ uhe.getMessage() );
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() FileNotFoundException : "+ fnfe.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() Exception : "+ e.getMessage() );
		}
		
		String read = "";
		
		while ((read = br.readLine()) != null) {
			sb.append(read);			       
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));
					
		NodeList sysList = doc.getElementsByTagName("system");
		Node sysNode = null;
		int nodeSize = sysList.getLength();
		for (int i=0; i<nodeSize; i++) {
			Element tmp = (Element) sysList.item(i);
			/*
			 * elastic_query.xml에 사용자의 회사 코드로 작성 되어진 쿼리 목록을 가져온다.
			 * <system id="POSCO" name="POSCO" companyCode="30">
			 *     <query></query>
			 *     ....
			 * </system>
			 */
			if (tmp.hasAttribute("companyCode")) {
				if(tmp.getAttribute("companyCode").equals(comCode)) {
					sysNode = sysList.item(i);
					break;
				}
			}else {
				sysNode = sysList.item(i);
			}
		}
		
		/*
		 * elastic_query.xml에 회사 코드로 쿼리를 만들어 줘야 함
		 * <system id="POSCO" name="POSCO" companyCode="30"></system>
		 */
		if (sysNode != null) {
			Element element = (Element) sysNode;				
			NodeList metaList = null;
			if(systemID.contains("patt")) metaList = element.getElementsByTagName("field");
			else metaList =element.getElementsByTagName("meta");
			StringBuffer sb1 = new StringBuffer();
			for (int i=0; i<metaList.getLength(); i++) {
				Element tmp = (Element) metaList.item(i);
				if(sb1.length()>0) sb1.append(",");
				sb1.append(tmp.getAttribute("id"));
			}
			if(sb1.length()>0) rtn_metalist = sb1.toString();
		}
		
		logger.info("rtn_metalist="+rtn_metalist);
		logger.info("##### GETSYSTEMQUERYNODE END #####");
		
		return rtn_metalist;
	}
	
	//Error Message 200(byteLength)byte 로 자르기
	public String subStrb(String str, int byteLength , String type){
		
		int retLength = 0;
		int tempSize = 0;
		int asc;
		if(str == null || "".equals(str) || "null".equals(str)){
			str = null;
			return str;
		}
		
		
		String tmp = str;
		if(type.equals("error")){
			tmp = str.substring(str.indexOf("ORA"));
		} else{
			tmp=str;
		}
		
		int length = tmp.length();
				
		for(int i = 0 ; i < length; i++){
			asc = (int)tmp.charAt(i);
			if(asc>127){
				if(byteLength >= tempSize + 3){
					tempSize += 3;
					retLength++;
				} else{
					return tmp.substring(0, retLength); 
				}
			} else{
				if(byteLength > tempSize){
					tempSize++;
					retLength++;
				}
			}
		}
		
		return tmp.substring(0, retLength);
	}
}

