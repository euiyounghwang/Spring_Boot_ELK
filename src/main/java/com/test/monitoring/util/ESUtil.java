package com.test.monitoring.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;



/**
 * @FileName	: ESUtil.java
 * @Author      : fe3388
 * @Date        : 2012. 6. 20.
 * @Version		:
 * @Description	:
 */
public class ESUtil {
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(ESUtil.class);
	
	
	public static String utcConvertToLocal(String datetime) {
		ZoneId z = ZoneId.of( "Asia/Seoul" );
		Instant instant = Instant.parse(datetime);
		
		Date date = Date.from(instant);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

//		System.out.println(" euiyoung >> " + date + ", >> " + format1.format(date));
		
		return format1.format(date);
		
	}

	public static String utcToLocaltime(String datetime) throws Exception {
		String locTime = null;
		// TimeZone tz = TimeZone.getTimeZone("GMT+08:00"); ÇØ´ç ±¹°¡ ÀÏ½Ã È®ÀÎ ÇÒ ¶§,
		// ÇÑ³kÀº +9
		TimeZone tz = TimeZone.getDefault();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date parseDate = sdf.parse(datetime);
			long milliseconds = parseDate.getTime();
			int offset = tz.getOffset(milliseconds);
			locTime = sdf.format(milliseconds + offset);
			locTime = locTime.replace("+0000", "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
		System.out.println("@@@ utcToLocaltime" + locTime);

		return locTime;
	}


	/**
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static String getString(Map<String, Object> params, String key) {
		/*
		 * Tomcat / WebLogic mode
		 */
//		return getString(params, key, true);
		return getString(params, key, false);
	}
	/** 
	* @Name : 		getString 
	* @History      : 
	* @Description	: 
	* @param ctx
	* @param key
	* @param autoEncoding
	* @return 
	*/
	public static String getString(Map<String, Object> params, String key, boolean autoEncoding) {
		
		String return_str;
		Object obj = params.get(key);
		
		if (obj == null){

			return null;	
		}		
		else if (obj.getClass().getName().startsWith("[L")) {
			Object[] a = (Object[]) obj;
			return_str=autoEncoding ? toUTF8(a[0].toString()) : a[0].toString();
		}else{
			return_str=autoEncoding ? toUTF8(obj.toString()) : obj.toString();
		}
		return_str = (return_str!=null)?return_str.replaceAll("<", " ").replaceAll(">", " "):return_str;//XSS tag 제거

		return return_str;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String toUTF8(String str) {
		if (str == null) return "";
		
		try {
			return new String(str.getBytes("8859_1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn(e);
		}
		return null;
	}
	/**
	* @Name : 		isEmpty
	* @History      :
	* @Description	:
	* @param str
	* @return
	*/
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	/**
	 * @Name : 		isEmpty
	 * @History      :
	 * @Description	:
	 * @param ctx
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Map<String, Object> params, String key) {
		String str = getString(params, key);
		return isEmpty(str);
	}

	/**
	 * @Name : 		isEmpty
	 * @History      :
	 * @Description	:
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !((str == null) || (str.length() == 0));
	}

	/**
	 * @Name : 		isEmpty
	 * @History      :
	 * @Description	:
	 * @param ctx
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(Map<String, Object> params, String key) {
		String str = getString(params, key);
		return isNotEmpty(str);
	}


	 /**
	  * & 문자 변환
	  *
	  * @param target
	  * @return
	  * @throws Exception
	  */
	 public static String removeAmp(String target) throws Exception{

		 return target.replaceAll("&", "&amp;");

	 }//removeSpeChar

	 /**
	  * 특수문자 변환
	  *
	  *
	  * @param target
	  * @return
	  * @throws Exception
	  */
	 public static String removeSpeChar(String target) throws Exception{

		 return target.replaceAll("&", "&amp;").replaceAll("\\”", "&quot;")
			      .replaceAll("\"", "&quot;").replaceAll("'", "&apos;")
			      .replaceAll("“", "&quot;").replaceAll("`", "&apos;")
			      .replaceAll("\\'", "&apos;").replaceAll("<", "&lt;")
			      .replaceAll(">", "&gt;").replaceAll("‘", "&apos;")
			      .replaceAll("’", "&apos;");

	 }//removeSpeChar

	 /**
	  * 특수문자 디코드
	  *
	  *
	  * @param target
	  * @return
	  * @throws Exception
	  */
	 public static String decodeSpeChar(String target) throws Exception{

		 return target.replaceAll("&amp;", "&")
				      .replaceAll("&quot;", "\"")
				      .replaceAll("&apos;", "'")
				      .replaceAll("&lt;b&gt;", "<b>")
				      .replaceAll("&lt;/b&gt;", "</b>")
				      .replaceAll("&lt;B&gt;", "<B>")
				      .replaceAll("&lt;/B&gt;", "</B>")
				      .replaceAll("&lt;br&gt;", " ");

	 }//removeSpeChar

	 /**
	  * xml 문자 제거
	  *
	  * @param target
	  * @return
	  * @throws Exception
	  */
	 public static String removeXML(String target) throws Exception{

		 return target.replaceAll("<o:p>", "").replaceAll("</o:p>", "").replaceAll("(<\\?(xml|XML))[^>]*/>", "");

	 }//removeXML

	 /**
	  * Html 문자 제거
	  *
	  * @param target
	  * @return
	  * @throws Exception
	  */
	 public static String removeHtml(String target) throws Exception{

	   	return target.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

	 }//removeHtmlV2



   /**
	* XSS 취약성 제거 후 반환
	* @param str
	* @return
	*/
	public static String stripXSSTag(String str) {
			return str.replace("<", "")
					.replace(">", "")
					.replace("\"", "")
					.replace("\'", "")
					.replace("\\", "");
	}



   /**
	* XSS 취약성 제거 후 반환("는 제외함)
	* @param str
	* @return
	*/
	public static String stripXSSTag2(String str) {
			return str.replace("<", "")
					.replace(">", "")
					.replace("\'", "")
					.replace("\\", "");
	}


   /**
	* 특수문자 인코딩
	* @param str
	* @return
	* @throws Exception
	*/
	public static String EncodeSpecialLetter(String str) throws Exception {
			return str.replace("+", URLEncoder.encode("+", "UTF-8"));
		}


	/**
	 * String 을 byte 길이 만큼 자르기
	 * @param str
	 * @param byteLength
	 * @return
	 */
	public static String subStringBytes(String str, int byteLength) {
		// String 을 byte 길이 만큼 자르기.
		int retLength = 0;
		int tempSize = 0;
		int asc;

		if(str == null || "".equals(str) || "null".equals(str)){
			str = "";
		}

		int length = str.length();

		for (int i = 1; i <= length; i++) {
			asc = (int) str.charAt(i - 1);
			if (asc > 127) {
				if (byteLength >= tempSize + 3) {
					tempSize += 3;
					retLength++;
				} else {
					return str.substring(0, retLength) + "...";
				}
			} else {
				if (byteLength > tempSize) {
					tempSize++;
					retLength++;
				}
			}
		}

		return str.substring(0, retLength);
	}

	/**
	 * 스트링 숫자 천단위 콤마(,) 표시
	 * @param s
	 * @return
	 */
	public static String countComma(String s) {
		return NumberFormat.getInstance().format(Integer.parseInt(s));
	}

	
	public static String getMetaDatas(JSONObject obj, String Meta) {
		String str = "";
		try
		{
			if (obj.has("CONSULTANT_NAME")) {
				str = (String) obj.getJSONArray(Meta).get(0);
				}
			else {
				str = "";
			}
		
		}catch(Exception ee){
			str = (String) obj.getString(Meta);
		}
		return str;
	}


}//class








