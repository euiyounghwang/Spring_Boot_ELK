<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import = "java.util.ResourceBundle" %>

<%
	ResourceBundle resource = ResourceBundle.getBundle("application");
	String deploy_company_code = resource.getString("deploy.company_code");
	
  String deploy_company_name = "";
  if (deploy_company_code.equals("17")) { 
	  deploy_company_name = "약관 공정화 시스템(LAW)-건설";
    }
  else if (deploy_company_code.equals("02")) {
	  deploy_company_name = "약관 공정화 시스템(LAW)-케미칼";
  	}

%>

<!-- #content -->
<div id="content">
	
	<div class="btns_bar">
	<h2 style="display:inline"><%=deploy_company_name%></h2> [Glue 4.2.13, Java 1.8/Spring]
 	</div>
 	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<%
					if (deploy_company_code.equals("17")) {
			%>
			<tr>
				<th>건설 망 [가동계 테스트용]</th>
				<td><a href="http://203.245.160.154:8191/LAW/common/devADM.jsp" target="_blank">건설AI_WAS #1 (http://203.245.160.154:8191/LAW/common/devADM.jsp)</a></td>
				<td></td>
			</tr>
			<tr>
				<th>건설 망 [고객 실제 사용]</th>
				<td><a href="http://203.245.160.154:7191/LAW/common/devADM.jsp" target="_blank">건설AI_WAS #1 (http://203.245.160.154:7191/LAW/common/devADM.jsp)</a></td>
				<td><a href="http://203.245.160.155:7191/LAW/common/devADM.jsp" target="_blank">건설AI_WAS #2 (http://203.245.160.155:7191/LAW/common/devADM.jsp)</a></td>
			</tr>
			<% 
				}
					else if (deploy_company_code.equals("02")) {
			%>
				<tr>
				<th>케미칼 망 [가동계 테스트용]</th>
				<td><a href="http://192.168.100.140:8191/LAW/common/devADM" target="_blank">케미칼AI_WAS #1 (http://192.168.100.140:8191/LAW/common/devADM)</a></td>
				<td></td>
			</tr>
			<tr>
				<th>케미칼 망 [고객 실제 사용]</th>
				<td><a href="http://192.168.100.140:7191/LAW/common/devADM" target="_blank">케미칼AI_WAS #1 (http://192.168.100.140:7191/LAW/common/devADM)</a></td>
				<td><a href="http://192.168.100.141:7191/LAW/common/devADM" target="_blank">케미칼AI_WAS #2 (http://192.168.100.141:7191/LAW/common/devADM)</a></td>
			</tr>
			<% 
				}
			%>
		</tbody>
	</table>

</div>
<!-- //#content -->
</br>
</br>
</br>
