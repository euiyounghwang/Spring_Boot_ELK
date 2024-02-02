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
							<td><table>
							<tr>
							<td><button class="btn1" id="btn_refresh">조회</button></td>
							</tr>
							</table></td>
						</tr>
					</tbody>
				</table>
</div>	
<br>
  
<div id="content">
  
	<table class="grid_table">
		<colgroup>
			 <col>
			 <col style="width: 200px;">
			 <col style="width: 120px;">
			 <col style="width: 200px;">
			 <col style="width: 200px;">
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col style="width: 1px;">
			 <col style="width: 1px;">
			 <col style="width: 120px;">
		</colgroup>
		<thead>
			<tr>
				<th>문서명</th>
				<th>SEQ</th>
				<th>소유자</th>
				<th>소속부서</th>
				<th>검출일자</th>
				<th>불공정건수</th>
				<th>전체건수</th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->

<script src="/monitoring/js/log_management/ai_extract_result_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>