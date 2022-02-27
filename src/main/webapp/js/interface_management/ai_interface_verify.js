var jsonparameter="";
$(document).ready(function(){
	setDashboard();
	$(window).unbind('resize.setDashboard').bind('resize.setDashboard',setDashboard);
	$('#right').unbind('show.setDashboard hide.setDashboard').bind('show.setDashboard hide.setDashboard',setDashboard);
	function setDashboard(){
		if($('#content .ifmoniter')[0]){
			var dashboard = $('#content .ifmoniter');
			if(dashboard.width() < 1300){
				dashboard.addClass('col2');
			}else{
				dashboard.removeClass('col2');
			}
		}
	}
	  
	$('#ask').click(function(){
		textChk(); 
	});
		
});




function replaceAll(str,search, replace) {
    return str.split(search).join(replace);
} 

function textChk(){

	try{
		
		/*
		 * 1.문장직접입력 또는 파일 업로드 선택 재확인
		 * 2.문장입력여부 확인 또는 파일선택여부 확인
		 * 3. 문장입력인 경우 바로 IF ajax 호출
		 * 4. 파일 업로드인 경우 파일내용 추출 라이브러리IF ajax 호출
		*/
		var data = document.getElementById('textParam').value;	
		
		if($('input[name=textMode]:checked').val()==="file"){
			fnCallFileIF();
			
		}else{
			fnCallTextIF();	
		}
		
	
	}catch (e){
		 alert("error : "+e.message);
	}
		
	
}

function fnCallTextIF() {
	showProgress();
	 var params = {};
	 params['textParam'] = $('#textParam').val();
	 $.ajax({
		type : 'POST',
        url:'callTextIF',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(result){
        	if(result.result_code=='success'){ //draw meta table
        		if(result.data != undefined){
        			
        			var table = $('#center .grid_table').DataTable({
        						ordering: false,
        						destroy: true,
        						scrollY : 0,
        						searching : false,
        						paging : false,
        						autoWidth : false,
        						language: {
        						    info : '조회현황 : _TOTAL_ 건'
        						 		},
        						info:true,
        		        columns : [
	        					{data : 'predict'},
	        					{data : 'probability'},
	        					{data : 'sentence'}
        								],
        						dom: '<"top"i>t<"bottom"><"clear">',
        						columnDefs:[
        							{targets:[0], className:'dt-body-center'}
        								]
        					
        					});
        			table.clear();
        			table.rows.add(result.data);
        			table.draw();
	        			}
	        		}

        		},
        complete : function(){
        	hideProgress();
        		},
        error : function(e){ 
        	console.log("callTextIF Error");
        		}
     });
} 


var fileTarget = $('.filebox .upload-hidden');

fileTarget.on('change', function(){
    if(window.FileReader){
        var filename = $(this)[0].files[0].name;
    } else {
        var filename = $(this).val().split('/').pop().split('\\').pop();
    }

    $(this).siblings('.upload-name').val(filename);
});



function fnCallFileIF() {
	if(confirm('파일크기에 따라 다소 시간이 소요될 수 있습니다. 업로드 하시겠습니까?')){
	showProgress();
	var option = {
      type: "POST"
      ,url : "fileUpload"
      ,dataType : "json"
      ,success : function (result){
    	  if(result.msg=='success'){
    		  $('.upload-name').val('');
      		if(result.data != undefined){
      			
      			var table = $('#center .grid_table').DataTable({
      						ordering: false,
      						destroy: true,
      						scrollY : 0,
      						searching : false,
      						paging : false,
      						autoWidth : false,
      						language: {
      						    info : '조회현황 : _TOTAL_ 건'
      						 		},
      						info:true,
      		        columns : [
        					{data : 'predict'},
        					{data : 'probability'},
        					{data : 'sentence'}
      								],
      						dom: '<"top"i>t<"bottom"><"clear">',
      						columnDefs:[
      							{targets:[0], className:'dt-body-center'}
      								]
      					
      					});
      			table.clear();
      			table.rows.add(result.data);
      			table.draw();
        			}
      		}
    	  },
      complete : function(){
    	  hideProgress();
    	  },
    	error : function(error) {
   		  alert("파일 업로드에 실패하였습니다.");
   		  console.log(error);
   		  console.log(error.status);
   		  }
   };
   $("#fileForm").ajaxSubmit(option);
	}

}
