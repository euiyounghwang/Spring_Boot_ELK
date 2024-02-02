<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div id="content">
<input type="hidden" id="start_date" value="${start_date}"/><input type="hidden" id="end_date" value="${end_date}"/><input type="hidden" id="seq" value="${seq}"/>
  <div class="btns_bar">
  	<div class="left">
  		<button class="btn2" onclick="loadCenterPage('log_management/ai_extract_result_list', 'Y');"><span>목록으로</span></button>
  		<span class="sep"></span>
  		<button class="btn2" id="btn_refresh"><span>새로고침</span></button>
  	</div>
 	</div>
 	<h2>${doc_title}(${owner_name})</h2>
	<table class="grid_table">
		<colgroup>
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col>
			 <col style="width: 90px;">
			 <col style="width: 1px;">
			 <col style="width: 1px;">
		</colgroup>
		<thead>
			<tr>
				<th>법률</th>
				<th>유형</th>
				<th>SEQ</th>
				<th>검출내용</th>
				<th>불공정여부</th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->

 <script src="/monitoring/js/log_management/ai_extract_result_detail.js"></script> 

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>