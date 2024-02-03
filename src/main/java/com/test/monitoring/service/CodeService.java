package com.test.monitoring.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.logging.log4j.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import com.test.monitoring.constants.WebConstants;
//import com.test.monitoring.repository.CodeMapper;

@Service
public class CodeService {
//	@Autowired
//	CodeMapper codeMapper;
	
	private static Logger logger = Logger.getLogger(CodeService.class);
	

	
	public Map<String, Object> pythonProcess(Map<String, Object> params){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try{
			String argv = "monitoring";
			String fk_cd_tp = (String) params.get("FK_CD_TP");
			logger.info("start pythonProcess : fk_cd_tp="+fk_cd_tp);
			
			String pythonPath = WebConstants.PYTHON_PATH;//python 실행파일 경로
			String pyFilePath = WebConstants.PY_FILE_PATH;//python 프로젝트경로	
			 
			ProcessBuilder pb = new ProcessBuilder(pythonPath+"python",pyFilePath+"Data_Main_Collect.py",""+argv,""+fk_cd_tp);
			Process p = pb.start();
			String storedFileName = null;
			 
			try (InputStream psout = p.getInputStream()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(psout));
				String thisLine = null;
				while ((thisLine = in.readLine()) != null) {
					logger.info(thisLine);
					if(thisLine.startsWith("Finish_return=")) {
						storedFileName = thisLine.substring(thisLine.indexOf("=")+1);//마지막파일 전체경로
					}
				}
	        }
			String filenum = storedFileName.substring(storedFileName.lastIndexOf("_")+1);//파일개수
			String headFileName = storedFileName.substring(0,storedFileName.lastIndexOf("_"));//전체경로 파일명 숫자앞부분
			
			logger.info("pythonProcess storedFileName:"+storedFileName);
			logger.info("pythonProcess filenum:"+filenum);
			logger.info("pythonProcess headFileName:"+headFileName);
			resultMap.put("filename", headFileName);
			resultMap.put("filenum", filenum);
			resultMap.put("msg", "success");
			
			}catch(Exception e){
				resultMap.put("msg", "error");
	        	logger.error(e.getMessage(), e.getCause());
			}

		   return resultMap;
	}
	
	public Map<String,Object> fileDownload(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try{
			String storedFileName = request.getParameter("filename");			
			logger.info("fileDownload storedFileName:"+storedFileName);

			//마지막 파일은 받아와서 브라우저로 보낸다.
		    byte fileByte[] = FileUtils.readFileToByteArray(new File(storedFileName));
			String originalFileName = storedFileName.substring(storedFileName.lastIndexOf("/")+1);//마지막파일명

	        //파일 다운로드를 위해 컨테츠 타입을 application/download 설정
	        response.setContentType("application/octet-stream; charset=utf-8");
		    response.setContentLength(fileByte.length);
		    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";");
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    response.getOutputStream().write(fileByte);		     
		    response.getOutputStream().flush();
		    response.getOutputStream().close();
			
			resultMap.put("msg", "success");
			
			}catch(Exception e){
				resultMap.put("msg", "error");
	        	logger.error(e.getMessage(), e.getCause());
			}

		   return resultMap;
	}
	
//	public List<Map<String, Object>> selectTable102(Map<String, Object> params){
//		return codeMapper.selectTable102(params);
//	}
}
