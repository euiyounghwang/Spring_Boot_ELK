package com.test.monitoring.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.monitoring.service.ChartService;

@Controller
class ChartController {
	@Autowired
	ChartService ChartService;

	
//	@Value("${server.running.flag}")
//	private String server_running_flag = "";
//	
//	@Value("${local.search_engine}")
//	private String local_elasticsearch = "";
	
	
	@RequestMapping(value = "/get_realtime_master_error_flag", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_realtime_mater_error_flag_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("get_realtime_master_error_flag_Chart");
		return ChartService.get_realtime_master_error_flag_Chart(params);
	}
	
		
	@RequestMapping(value = "/get_realtime_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getRealTimeTransaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("getRealTimeTransaction_Chart");
		return ChartService.getRealTimeTransaction_Chart(params);
	}
	
	@RequestMapping(value = "/get_chart_login_history", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getLoginHistory_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("getLoginHistory_Chart");
		return ChartService.getLoginHistory_Chart(params);
	}
	
	@RequestMapping(value = "/get_predict_label_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_predict_label_transaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("get_predict_label_transaction_Chart");
		return ChartService.get_predict_label_transaction_Chart(params);
	}
	
	@RequestMapping(value = "/get_model_upgrade_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_model_upgrade_transaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("get_model_upgrade_transaction_Chart");
		return ChartService.get_model_upgrade_transaction_Chart(params);
	}
	
	@RequestMapping(value = "/get_chart_resource_realtime_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> chart_resource_realtime_transaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("get_chart_resource_realtime_transaction_Chart");
		return ChartService.get_chart_resource_realtime_transaction_Chart(params);
	}
	
	
}
