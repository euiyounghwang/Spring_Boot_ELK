package com.test.monitoring.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.monitoring.constants.GlobalValues;
import com.test.monitoring.util.ESUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ChartService {
//	@Autowired
//	MonitoringMapper monitoringMapper;
	@Autowired
	ElasearchService elasearchService;
	
	@Autowired
	CodeService codeService;
	
	private static Logger logger = Logger.getLogger(ChartService.class);
	
	
	public Map<String, Object> get_realtime_master_error_flag_Chart(Map<String, Object> params)
	{
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/unfair-master-manual-*/_search");
		params.put("indexId", "unfair-master-manual-*");
		System.out.println("get_realtime_mater_error_flag_Chart_params >> " +  params);
		
		 try
	    	{
//			 		Calendar cal = Calendar.getInstance();
//			 		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			 		cal.add(Calendar.MONDATETH, 2);
//			 		cal.add(Calendar.DATE, 0);
//			 		System.out.println("after: " + df.format(cal.getTime()));
//			 		cal.add(Calendar.DATE, -1);
//			 		System.out.println("after: " + df.format(cal.getTime()));
//			 		cal.add(Calendar.DATE, -13);
//			 		System.out.println("after: " + df.format(cal.getTime()));
			 
	    		JSONObject  jsonObj = elasearchService.handleEla(params);
	    		
	    		JSONArray jArr = jsonObj.getJSONObject("aggregations").getJSONObject("metrics_by_data").getJSONArray("buckets");
	    		
	    		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    		
	    		String date_label = "";
	    		String data_value = "";
	    		
	    		String data = "";
	    		String labels = "";
	    		
	    		Map<String, Object> map = new HashMap<>();
	    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    		for(int i=0; i<jArr.size() ;i++)
	  		  		{
							if (jArr.getJSONObject(i).has("key_as_string")) {
								
								Date day1 = dateFormat.parse((String)params.get("start_date"));
								Date day2 = dateFormat.parse((String)jArr.getJSONObject(i).getString("key_as_string"));
								
									if (day1.compareTo(day2) > 0) {
										System.out.println("@@@@@ BIG@@@@");
										labels = "";
										labels += date_label + ",";
									}
									else {
									
										date_label = (String) jArr.getJSONObject(i).getString("key_as_string");
										labels += date_label + ",";
									} 
							}
							else { // LAW_NAME 없을때는 초기화
									labels = "";
								}
	  		  		
	    				
	    				if( jArr.getJSONObject(i).has("doc_count")){
	    					data_value = (String) jArr.getJSONObject(i).getString("doc_count");
		    				data += data_value + ",";
		    			} else { //LAW_NAME 없을때는 초기화
		    				data = "";
		    			     }
		    		
		       	  }
	    		
	    		System.out.println("data, labels >> " +  data + " >> " + labels);
	    		
	    			
//	    		String[] labels_merge = labels.split(",");
////	    		for(int i=0; i<labels_merge.length; i++) {
//	    		for(int i=0; i<30; i++) {
//	    			Calendar cal = Calendar.getInstance();
//    				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			 			cal.add(Calendar.DATE, -i);
//			 			
//			 			for(int j=0; i<labels_merge.length; j++) 
//			 			{
//	    				if (df.format(cal.getTime()).equals(labels_merge[j])) 
//	    					{
//	    						System.out.println("labels_merge >> " +  labels_merge[j] + " >> " + labels_merge);
//	    					} else {
//	    						System.out.println("df.format(cal.getTime()) >> " +  df.format(cal.getTime()));
//	    					}
//			 			}
//	    			}
			 	
		    	map.put("data", data);
	    		map.put("label", labels);

	    		list.add(map);
	    		results.put("datas", list);
	    		System.out.println("get_realtime_master_error_flag_Chart_return >> " +  results);
//	    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
	    		
	    	}catch(Exception e)    	{
	    		logger.error(e.getMessage(), e.getCause());
	    	}
	    	return results;
	}
	
	public Map<String, Object> getRealTimeTransaction_Chart(Map<String, Object> params)
	{
		
		try {
//			System.out.println("@@@UTC@@ " + ESUtil.utcConvertToLocal("2020-07-17T08:55:44.053Z"));
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
		
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/unfair-slave-manual-*/_search");
		params.put("indexId", "unfair-slave-manual-*");
		System.out.println("getRealTimeTransaction_Chart_params >> " +  params);
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
       
    		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		Map<String, Object> map = new HashMap<>();
    		
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
				String delay_time = "";
				String timestamp = "";
	
				String data = "";
				String labels = "";
    		

	    	for(int i=0; i<jArr.size() ;i++)
	    		  {
	    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
	    			type = jArr.getJSONObject(i).getString("_type");
	    			id = jArr.getJSONObject(i).getString("_id"); //ID
	
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("DELAY_TIME")){
	    				delay_time = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DELAY_TIME");
	    				data += delay_time + ",";
	    			} else { //LAW_NAME 없을때는 초기화
	    				delay_time = "";
	    			     }
	    			
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TIMESTAMP")){
	    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");
	    				labels += timestamp + ",";
	    			} else { //LAW_NAME 없을때는 초기화
	    				timestamp = "";
	    			     }
	       	  }
//	    	map.put("data", data);
//    		map.put("label", labels);
    		map.put("data", data.substring(0, data.length()-1));
    		map.put("label", labels.substring(0, labels.length()-1));
    		System.out.println("getRealTimeTransaction_Chart_delay_time >> " +  delay_time);
    		
//    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
//    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//    				[1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
//    				["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
    
	 		
//    		String data = "2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99";
//    		String labels =  "06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03";
//    		map.put("data", data);
//    		map.put("label", labels);
    			
//    		map.put("data", "1.74,1.1");
//    		map.put("label", "06-18 09:48:33, 06-18 09:48:33");
    		list.add(map);
    		results.put("datas", list);
    		System.out.println("getRealTimeTransaction_Chart_return >> " +  results);
//    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	public Map<String, Object> getLoginHistory_Chart(Map<String, Object> params)
	{
		System.out.println("\n\n");
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/was-server-log-*/_search");
		params.put("indexId", "was-server-log-*");
		System.out.println("getLoginHistory_Chart_params >> " +  params);
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		Integer TotalCount = Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value"));
    		
    		JSONArray jArr = null;
    		if (TotalCount > 0) {
    			jArr = jsonObj.getJSONObject("aggregations").getJSONObject("metrics_by_data").getJSONArray("buckets");
    		    }
    		else {
    			System.out.println("NUILL");
    		   }
    		
    		
    		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		
    		String date_label = "";
    		String data_value = "";
    		
    		String data = "";
    		String labels = "";
    		
    		Map<String, Object> map = new HashMap<>();
    		
    		if (TotalCount > 0) {
		    		for(int i=0; i<jArr.size() ;i++)
		  		  		{
		    				if( jArr.getJSONObject(i).has("key_as_string")){
			    				date_label = (String) jArr.getJSONObject(i).getString("key_as_string");
			    				labels += date_label + ",";
			    			} else { //LAW_NAME 없을때는 초기화
			    				labels = "";
			    			     }
		    				
		    				if( jArr.getJSONObject(i).has("doc_count")){
		    					data_value = (String) jArr.getJSONObject(i).getString("doc_count");
			    				data += data_value + ",";
			    			} else { //LAW_NAME 없을때는 초기화
			    				data = "";
			    			     }
		    				
			       	  }
    		}
    		
    	System.out.println("data, labels >> " +  data + " >> " + labels);
    		
    	if (jArr != null && jArr.size() > 0) {
    		 map.put("data", data.substring(0, data.length()-1));
 	    	 map.put("label", labels.substring(0, labels.length()-1));
    		}
    	else {
				map.put("data", "");
				map.put("label", "");
    	   }
    	
          /*
    	 if (data.length() > 1) {
    			  map.put("data", data.substring(0, data.length()-1));
    	    	map.put("label", labels.substring(0, labels.length()-1));
    		   }
    		else {
    				map.put("data", data);
        		map.put("label", labels);
    		  }
    		  */
    		
    		list.add(map);
    		results.put("datas", list);
    		System.out.println("getLoginHistory_Chart_return >> " +  results);
//    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	public Map<String, Object> get_predict_label_transaction_Chart(Map<String, Object> params)
	{
		System.out.println("\n\n");
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/unfair-master-manual-*/_search");
		params.put("indexId", "/unfair-master-manual-*");
		System.out.println("get_predict_label_transaction_Chart_params >> " +  params);
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
				String total_cnt = "";
				String neg_cnt = "";
	
				String all = "";
				String all_whole = "";
				String pos = "";
				String pos_whole = "";
				String neg = "";
				String neg_whole = "";
				String timestamp = "";
				String labels = "";
    		

	    	for(int i=0; i<jArr.size() ;i++)
	    		 {
	    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
	    			type = jArr.getJSONObject(i).getString("_type");
	    			id = jArr.getJSONObject(i).getString("_id"); //ID
	
	    			//all 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TOTAL_CNT")){
	    				all = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TOTAL_CNT");
	    				all_whole += all + ",";
	    			} else { 
	    				all = "";
	    			     }
	    			
	    			//NEG_CNT 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("NEG_CNT")){
	    				neg = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("NEG_CNT");
	    				neg_whole += neg + ",";
	    				
	    				pos = String.valueOf(Integer.parseInt((String) jArr.getJSONObject(i).getJSONObject("_source").getString("TOTAL_CNT")) - Integer.parseInt((String) jArr.getJSONObject(i).getJSONObject("_source").getString("NEG_CNT")));
	    				pos_whole += pos + ",";
	    				
	    			} else { 
	    				neg = "";
	    			     }
	    			

    				//TIMESTAMP 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TIMESTAMP")){
	    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");
	    				labels += timestamp + ",";
	    			} else { 
	    				timestamp = "";
	    			     }
	    		
	    		  }
       
    		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		Map<String, Object> map = new HashMap<>();
    		
//    		String all = "5.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99";
//    		String pos = "2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99";
//    		String neg = "0.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99";
//    		String labels =  "06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03";
    		
//    			System.out.println("djdljd" + all_whole + " >> " + all_whole.length());
    		if (all_whole.length() > 1) {
    			map.put("all", all_whole.substring(0, all_whole.length()-1));
					map.put("pos", pos_whole.substring(0, pos_whole.length()-1));
					map.put("neg", neg_whole.substring(0, neg_whole.length()-1));
	    		map.put("label", labels.substring(0, labels.length()-1));
	  		   }
	  		else {
	  			map.put("all", all_whole);
					map.put("pos", pos_whole);
					map.put("neg", neg_whole);
	    		map.put("label", labels);
	  		  }
	   

    		list.add(map);
    		results.put("datas", list);
    		System.out.println("get_predict_label_transaction_Chart_return >> " +  results);
//    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	
	public Map<String, Object> get_model_upgrade_transaction_Chart(Map<String, Object> params)
	{
		System.out.println("\n\n");
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/ai_performance_maxtrix/_search");
		params.put("indexId", "/ai_performance_maxtrix");
		System.out.println("get_model_upgrade_transaction_Chart_params >> " +  params);
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
			
				String pos_f1 = "";
				String pos_f1_all = "";
				String pos_accuracy = "";
				String pos_precision = "";
				String pos_recall = "";
				String neg_f1_all = "";
				String neg_f1 = "";
				String neg_accuracy = "";
				String neg_precision = "";
				String neg_recall = "";
				String timestamp = "";
				String labels = "";
    		

	    	for(int i=0; i<jArr.size() ;i++)
	    		 {
	    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
	    			type = jArr.getJSONObject(i).getString("_type");
	    			id = jArr.getJSONObject(i).getString("_id"); //ID
	
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("POS_F1")){
	    				pos_f1 = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("POS_F1");
	    				pos_f1_all += pos_f1 + ",";
	    			} else { 
	    				pos_f1 = "";
	    			     }
	    			
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("NEG_F1")){
	    				neg_f1 = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("NEG_F1");
	    				neg_f1_all += neg_f1 + ",";
	    			
	    			} else { 
	    				neg_f1 = "";
	    			     }
	    			

    				//TIMESTAMP 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TIMESTAMP")){
	    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");
	    				labels += timestamp + ",";
	    			} else { 
	    				timestamp = "";
	    			     }
	    		
	    		  }
	    	
	    	
       
    		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		Map<String, Object> map = new HashMap<>();
    		
//    		String pos_f1 = "91,92,88,87,98,91,92,88,87,98,91,92,88,87,98,91,92,88,87,98";
//    		String pos_accuracy = "71,72,78,77,78,71,72,78,77,78,71,72,78,77,78,71,72,78,77,78";
//    		String pos_precision = "61,62,68,67,68,61,62,68,67,68,61,62,68,67,68,61,62,68,67,68";
//    		String pos_recall = "81,82,88,87,88,81,82,88,87,88,81,82,88,87,88,81,82,88,87,88";
//    		String label =  "07-18 09:48:33,07-18 11:14:55,07-18 11:15:03,07-18 11:25:00,06-18 12:08:55,06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,07-18 09:48:33,07-18 11:14:55,07-18 11:15:03,07-18 11:25:00,06-18 12:08:55,06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55";
    		
    			if (jArr.size() >0) {
			    	map.put("pos_f1", pos_f1_all.substring(0, pos_f1_all.length()-1));
						map.put("pos_accuracy", pos_accuracy);
						map.put("pos_precision", pos_precision);
						map.put("pos_recall", pos_recall);
						map.put("neg_f1", neg_f1_all.substring(0, neg_f1_all.length()-1));
						map.put("neg_accuracy", neg_accuracy);
						map.put("neg_precision", neg_precision);
						map.put("neg_recall", neg_recall);
		    		map.put("label", labels.substring(0, labels.length()-1));
    			}  else {
	    			map.put("pos_f1", "");
						map.put("pos_accuracy", "");
						map.put("pos_precision", "");
						map.put("pos_recall", "");
						map.put("neg_f1", "");
						map.put("neg_accuracy", "");
						map.put("neg_precision", "");
						map.put("neg_recall", "");
		    		map.put("label", "");
    			}

    		list.add(map);
    		results.put("datas", list);
    		System.out.println("get_model_upgrade_transaction_Chart_return >> " +  results);
//    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	

	public Map<String, Object> get_chart_resource_realtime_transaction_Chart(Map<String, Object> params)
	{
		System.out.println("\n\n");
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("this.local_elasticsearch >> " + GlobalValues.getSearchEngineInfo());
		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/metricbeat-7.6.1-*/_search");
		params.put("indexId", "/metricbeat-7.6.1-*");
		System.out.println("get_chart_resource_realtime_transaction_Chart_params >> " +  params);
		
   try
    	{
	   		params.put("EVENT_ID", "get_chart_resource_aggs_transaction");
	 			// aggs 값을 통해 host_name별로 cpu, memory 값 조회
	     JSONObject  jsonAggsObj = elasearchService.handleEla(params);
	     JSONArray jArrAggs =jsonAggsObj.getJSONObject("aggregations").getJSONObject("HOST_NAME")
	    		 .getJSONObject("HOST_NAME").getJSONArray("buckets");
	     
	     	  
	     String[] host_name_array = new String[jArrAggs.size()];
		   for (int i = 0; i < jArrAggs.size(); i++) {
			   		host_name_array[i] = (String)jArrAggs.getJSONObject(i).getString("key");
			   		System.out.println("@@@" + jArrAggs.getJSONObject(i).getString("key"));
		     	}
	     
		   	params.remove("EVENT_ID");
	     params.put("EVENT_ID", "get_chart_resource_realtime_transaction");
//	     params.put("HOST_NAME", "get_chart_resource_realtime_transaction");
//    		JSONObject  jsonObj = elasearchService.handleEla(params);
//    		
//    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
			
				String cpu = "";
				String cpu_all = "";
				
				String memory = "";
				String memory_all = "";
				
				String timestamp = "";
				String labels = "";
				
				String host_name = "";
				String host_name_all = "";
    		
				ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				// 1. aggregation 호스트명 검색 (호스트명수만큼 loop)
				//   2. 호스트명마다 cpu, memory 값 받아오기
				for(int k=0; k<host_name_array.length;k++)
				{
						cpu = "";
						cpu_all = "";
						memory = "";
						memory_all = "";
						timestamp = "";
						labels = "";
						host_name = "";
						host_name_all = "";
						
					 params.put("RESOURCE", "cpu");
					 params.put("HOST_NAME", (String)host_name_array[k]);
					 System.out.println("####" + host_name_array[k]);
				   JSONObject  jsonObj = elasearchService.handleEla(params);
				   JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
						
						for (int i = 0; i < jArr.size(); i++) 
						{
							index = jArr.getJSONObject(i).getString("_index"); // 인덱스
							type = jArr.getJSONObject(i).getString("_type");
							id = jArr.getJSONObject(i).getString("_id"); // ID
		
							if (jArr.getJSONObject(i).getJSONObject("_source").has("system")) {
								cpu = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONObject("system").getJSONObject("cpu").getJSONObject("total").getString("pct");
								cpu_all += cpu + ",";
//								cpu_all = cpu + "," + cpu_all;
							} else {
								cpu = "";
							}
		
							if (jArr.getJSONObject(i).getJSONObject("_source").has("host")) {
								host_name = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONObject("host").getString("name");
								host_name_all += host_name + ",";
							} else {
								host_name = "";
							}
		
							// TIMESTAMP 존재 체크
							if (jArr.getJSONObject(i).getJSONObject("_source").has("@timestamp")) {
								timestamp = ESUtil.utcConvertToLocal((String) jArr.getJSONObject(i).getJSONObject("_source").getString("@timestamp"));
								labels += timestamp + ",";
//								labels = timestamp + "," + labels;
							} else {
								timestamp = "";
							}
		
						}
						
//						# MEMORY
						params.put("RESOURCE", "memory");
						params.put("HOST_NAME", (String)host_name_array[k]);
						JSONObject  jsonObjMemory = elasearchService.handleEla(params);
					  JSONArray jArrMem =jsonObjMemory.getJSONObject("hits").getJSONArray("hits");
					  
					 for (int i = 0; i < jArrMem.size(); i++) 
						{
							index = jArrMem.getJSONObject(i).getString("_index"); // 인덱스
							type = jArrMem.getJSONObject(i).getString("_type");
							id = jArrMem.getJSONObject(i).getString("_id"); // ID
		
							if (jArrMem.getJSONObject(i).getJSONObject("_source").has("system")) {
								memory = (String) jArrMem.getJSONObject(i).getJSONObject("_source").getJSONObject("system").getJSONObject("memory").getJSONObject("actual").getJSONObject("used").getString("pct");
//								memory_all += String.valueOf(Float.parseFloat(memory)*100.0) + ",";
								memory_all += memory + ",";
							} else {
								memory = "";
							}
						}
							
		
						Map<String, Object> map = new HashMap<>();
		
						// String pos_f1 =
						// "91,92,88,87,98,91,92,88,87,98,91,92,88,87,98,91,92,88,87,98";
						// String pos_accuracy =
						// "71,72,78,77,78,71,72,78,77,78,71,72,78,77,78,71,72,78,77,78";
						// String pos_precision =
						// "61,62,68,67,68,61,62,68,67,68,61,62,68,67,68,61,62,68,67,68";
						// String pos_recall =
						// "81,82,88,87,88,81,82,88,87,88,81,82,88,87,88,81,82,88,87,88";
						// String label = "07-18 09:48:33,07-18 11:14:55,07-18 11:15:03,07-18
						// 11:25:00,06-18 12:08:55,06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18
						// 11:25:00,06-18 12:08:55,07-18 09:48:33,07-18 11:14:55,07-18 11:15:03,07-18
						// 11:25:00,06-18 12:08:55,06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18
						// 11:25:00,06-18 12:08:55";
		
						map.put("cpu", cpu_all);
						map.put("memory", memory_all);
						map.put("host", host_name_all);
						map.put("label", labels.substring(0, labels.length() - 1));
			
						list.add(map);
				}
    		results.put("datas", list);
    		System.out.println("get_chart_resource_realtime_transaction_Chart return >> " +  results);
//    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
}

