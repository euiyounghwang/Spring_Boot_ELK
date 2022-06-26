package com.test.monitoring.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.test.monitoring.service.MonitoringService;

@Controller
class MonitoringController {
	@Autowired
	MonitoringService monitoringService;
	
//	@Value("${server.running.flag}")
//	private String server_running_flag = "";
//	
//	@Value("${local.search_engine}")
//	private String local_elasticsearch = "";
	
		
	@RequestMapping(value = "/findWasServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findWasServerLog(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		System.out.println("findWasServerLog");
		return monitoringService.findWasServerLog(params);
	}
	
	@RequestMapping(value = "/findAIServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAIServerLog(@RequestBody Map<String, Object> params)	{
		System.out.println("findAIServerLog");
		return monitoringService.findAIServerLog(params);
	}
	
	@RequestMapping(value = "/findAIExtractResultList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAIExtractResultList(@RequestBody Map<String, Object> params)	{
		System.out.println("findAIExtractResultList");
		return monitoringService.findAIExtractResultList(params);
	}
	
	@RequestMapping(value = "/findAIExtractResultDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAIExtractResultDetail(@RequestBody Map<String, Object> params)	{
		System.out.println("findAIExtractResultDetail");
		return monitoringService.findAIExtractResultDetail(params);
		
	}
	
	@RequestMapping(value = "/findAITraindataLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAITraindataLog(@RequestBody Map<String, Object> params)	{
		System.out.println("findAITraindataLog");
		return monitoringService.findAITraindataLog(params);
	}
	
	@RequestMapping(value = "/findAITraindata_Ratio", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAITraindata_Ratio(@RequestBody Map<String, Object> params)	{
		System.out.println("findAITraindata_Ratio");
		return monitoringService.findAITraindata_Ratio(params);
	}
	
	@RequestMapping(value = "/callTextIF", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> callTextIF(@RequestBody Map<String, Object> params)	{
		System.out.println("callTextIF");
		return monitoringService.callTextIF(params);
	}
	
	
	@RequestMapping("/fileUpload")
	@ResponseBody
	public Map<String,Object> fileUpload(HttpServletRequest req, HttpServletResponse rep) 
			throws FileNotFoundException, IOException {
		MultipartHttpServletRequest mReq = (MultipartHttpServletRequest) req;
		Map<String,Object> resultMap = monitoringService.fileUpload(mReq);
		return resultMap;
	}
	
}
