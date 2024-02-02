<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- #content -->
<div id="content">	
	<!-- .ifmoniter -->
	<div class="ifmoniter">
		
		<!-- .panel -->
		<div class="panel">
			<div class="section half">
				<h2><input type="radio" name="textMode" value="text" checked> 문장 직접입력&nbsp;&nbsp;&nbsp;</h2>
				<div  class="connter">
				<textarea name="textParam" id="textParam" rows="17" style="width: 99%;">
0.0 0.0.0 제 1 장   총  칙

1.1 [목 적]
1) 본 계약은 포스코의 조업안정과 경쟁력을 확보하고 수탁운영사의 자율경영과 책임작업 수행체제를 정착하기 위하여 계약기간 동안 상호간에 준수 하여야 할 사항을 규정하기 위함이다.
2 본 계약은 포스코의 마그네슘제련공장 등 위탁설비(이하 “위탁설비”)의 운영, 가동 및 유지, 보전에 필요한 일상점검, 정기점검, 수리 등 위탁업무 전반에 대해 포스코와 수탁운영사간의 상호 역할과 책임, 사고발생시 대응책임과 손해배상, 생산성 향상을 위한 성과보상의 범위에 관하여 규정한다.

1.2 [용어정의]
1.2.1	위탁운영 : 마그네슘제련공장 제품생산을 위한 공장설비의 주공정 운전작업 및 이에 수반되는  일체의 부대작업을 수탁운영사가 수행하는 것을 의미하며  그와 관련된 외주협력 또는 외주작업을 포함한다.
가) 자본적지출 : 고정자산의 내용연수를 연장시키거나 자산의 가치를 증가시키는 수선비를 말한다.
1.2.3	수익적지출 : 설비의 정상적인 사용 중에 발생하는 원상 회복 및 일상적인 정비비를 말한다.
가) 테스트
나) 테스트
다) 테스트
라) 테스트
마) 테스트
바) 테스트
사) 테스트
아) 테스트
자) 테스트
차) 테스트
카) 테스트
타) 테스트
파) 테스트
하) 테스트
				</textarea>
				</div>
				<p class="line">&nbsp;</p>
				<h2><input type="radio" name="textMode" value="file"> File Upload</h2>
				<div class="connter">
				<div class="right">
  		<form id ="fileForm" method ="post" enctype ="multipart/form-data" action="">
		    <div class="filebox bs3-primary">
				<input class="upload-name" id="uploadfilenm" name="uploadfilenm" value="" readonly="readonly">
		
				<label for="file">파일선택</label> 
			    <input type="file" name ="file" id="file" class="upload-hidden"> 
			    <input type="button" id="btn_upload" class="upload-hidden"/> 
			</div>
	    </form> 		
  		
  	</div>
				</div>
			</div>
			
			<div class="section half">
				<button type="button" id="ask" class="btn1">검출 요청 >></button>
			</div>
		</div>
		<!-- //.panel -->
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>검출결과</h2>
				<div  class="connter">
  				
	<table class="grid_table">
		<colgroup>
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col>
		</colgroup>
		<thead>
			<tr>
				<th>predict</th>
				<th>probability</th>
				<th>검출내용</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
				</div>
				</div>
			</div>
		</div>
		<!-- //.panel -->
	</div>
</div>

<script src="/monitoring/js/interface_management/ai_interface_verify.js"></script>