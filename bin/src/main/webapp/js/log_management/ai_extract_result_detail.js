/**
 * was 서버 로그 조회
 */

//document.write("<script src='/monitoring/js/search_monitoring/search_engine_node_list.js'></script>");
//<script src='/monitoring/js/search_monitoring/search_engine_node_list.js'></script>
//require("/monitoring/js/search_monitoring/search_engine_node_list.js");
var table;
var excel = false;

function includeJs(jsFilePath) {
    var js = document.createElement("script");

    js.type = "text/javascript";
    js.src = jsFilePath;

    document.body.appendChild(js);
}


$( document ).ready(function() {
	
	table = $('#center .grid_table').DataTable( {
		//ordering: false,
        "serverSide": true,
        "paging": true,
        "autoWidth": false, //자동 열 너비 계산을 사용 또는 사용하지 않도록 설정한다.(true일 경우 td에 width값이 px단위로 고정되어 window resize 이벤트에 대응 안됨)
        pagingType: "full_numbers_no_ellipses",
        columns : [
        	{data : 'LAW_NAME'},
        	{data : 'SUB_LAW_NAME'},
        	{data : 'SEQ'},
        	{data : 'DETECT_SENTENCE'},
			       	 {
							data : 'DETECT_UNFAIR_YN',
							render: function (data, type, full, meta) {
								var dataval = "";
								if(data.indexOf("POS")>-1){
									dataval ="공정";
								}else{
									dataval ="불공정";
								}
								return dataval;
							}
						},
					{
						data : 'START_DATE',
						render: function (data, type, full, meta) {
							return '<input type="hidden" name="START_DATE" value="'+data+'"/>';
						}
					},
					{
						data : 'END_DATE',
						render: function (data, type, full, meta) {
							return '<input type="hidden" name="END_DATE" value="'+data+'"/>';
						}
					}
		],
		searching:true, 
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		info:true,
		columnDefs:[
			{targets:[0,1,3,4,5,6], className:'dt-body-center'},
			{ "orderable": false, "targets": [0,1,3,5,6] },
		],
		"rowCallback": function( row, data, index ) {
		    if ( data["LOG_LEVEL"] == "ERROR" ){
		        $('td', row).css('background-color', '#F5BCA9');
		    }
		},
		order:[2,'asc'],
		"ajax": {
			type : 'POST',
            url: "findAIExtractResultDetail",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
            	showProgress();
            },
            data: function ( d ) {
            	if(excel){
            		d['start']  = 0;
            		d['length'] = table.settings()[0]._iRecordsDisplay;
            	} else {
            		if(d['draw']!=1)
            			d['start'] = table.settings()[0]._iDisplayStart;
            	}
            	d['EVENT_ID'] = 'findAIExtractResultDetail';
            	d['SEQ'] = $('#seq').val();
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
        	hideProgress();
        },
        lengthMenu: [[10, 50, 100], [10, 50, 100]],
        dom: '<"top"li>t<"bottom"p><"clear">'
    });
	
	table.on( 'page.dt', function () {
		$('#center').animate({scrollTop: 0}, 100);
	} );
	
	$('#btn_refresh').click(function(){
		search();
	});
	
	$('#input_search').on('keypress', function (e) {
	       if(e.which === 13){
    	   		search();
	       }
	});
});


function search(){
	$('input[type="search"]').val('').keyup();
	table.ajax.reload();
}
