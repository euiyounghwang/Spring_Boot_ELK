<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div class="search_box">

				<table>
					<tbody>
						<tr>
							<td><span class="label">날짜</span></td>
							<td><input type="text" class="datetime" id="startDate"></td>
							<td>-</td>
							<td><input type="text" class="datetime" id="endDate"></td>
							<td><span class="label">학습데이터 (공정/불공정) 여부</span></td>
							<td>
							<select style="width:140px;" id="sel_label_level">							
							<option value="">전체</option>
							<option value="INFO">공정</option>
							<option value="ERROR">불공정</option>
							</select>
							</td>
						</tr>
						<tr>
							<td><span class="label">검색어</span></td>
							<td colspan="11">
							<table>
							<tr>
							<td  style="width:350px;"><input type="text" style="width:350px;" id="input_search"/></td>
							<td><button class="btn1" id="btn_refresh">조회</button></td>
							</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td><span class="label"><b>Precision Ratio</b></span></td>
							<td olspan="10"><label id='error_ratio' /></td>
						</tr>
					</tbody>
				</table>
</div>	
<br>
  
<div id="content">
  
	<table class="grid_table">
		<colgroup>
			 <col style="width: 150px;">
			 <col style="width: 120px;">
			 <col style="width: 200px;">
			 <col style="width: 200px;">
			 <col style="width: 200px;">
			 <col style="width: 200px;">
		</colgroup>
		<thead>
			<tr>
				<th>인덱스</th>
				<th>ID</th>
				<th>PREDICT_LABEL</th>
				<th>CONSULTANT_NAME</th>
				<th>CONSULT_LABEL</th>
				<th>LAST_UPDATE_TIMESTAMP</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->

<script src="/monitoring/js/log_management/traindata_log_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>
