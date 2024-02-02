/**
 * 서버 실시간 조회
 */



$( document ).ready(function() {
//	make_graph();
	show_graph();
//	displayLineChart();

	$(document.getElementById("butt_stop")).trigger('click');
//	$(document.getElementById("butt_start")).trigger('click');
});






function show_graph() {
//	var data = [1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
//	var labels =  ["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
	var data = [];
	var lables = [];
	var params = {};
	var dynamic_results = [];
	var dynamic_mem_results = [];
	var elements = [];
	var color_arr = [];
	color_arr[0] = "rgb(102, 204, 255)";
	color_arr[1] = "rgb(153, 102, 255)";
	color_arr[2] = "rgb(255, 159, 64)";
	color_arr[3] = "rgb(75, 192, 192)";
	params['version_params'] = $('#version_params').val();
	params['EVENT_ID'] = 'get_chart_resource_realtime_transaction';
	params['start'] = '0';
	params['length'] = '100';
	params['start_date'] = dateAddDel(getTimeStamp(), -7, 'd');
	$.ajax({
		type : 'POST',
    url:'get_chart_resource_realtime_transaction',
    data : JSON.stringify(params),
    dataType:'json',
    contentType: 'application/json',
    beforeSend: function(){
    	 $("#LoadingImage").show();
//    		showProgress();
        },
    success:function(datas){
//    	alert(data);
//    	alert(labels);
//      alert(JSON.stringify(datas['data']));
    	
    	   var loop = 0;
    	$.each(datas,function(key,value) {
//    			alert(key + '@' + value);
			    	$.each(value,function(keys,values) {
			    		cpu = values.cpu;
			    		memory = values.memory;
			    		host = values.host;
			    		labels = values.label;
			    		
			    		
			    		dynamic_results.push(	{
								label : host.split(",")[0],
								fill : false,
								backgroundColor : color_arr[loop],
								borderColor : color_arr[loop],
//								backgroundColor : "rgb(" + Math.floor(Math.random() * 255) + ", " + Math.floor(Math.random() * 255) + "," + Math.floor(Math.random() * 255) + ")",
//								borderColor : "rgb(" + Math.floor(Math.random() * 255) + ", " + Math.floor(Math.random() * 255) + "," + Math.floor(Math.random() * 255) + ")",
								data : cpu.split(",")
							} );
			    		
			    		dynamic_mem_results.push(	{
							label : host.split(",")[0],
							fill : false,
							backgroundColor : color_arr[loop],
							borderColor : color_arr[loop],
							data : memory.split(",")
							} );
		    		
			    		
			    		loop += 1;
			    		
//			    	dynamic_results = [
//			    			{
//								label : host.split(",")[0],
//								fill : false,
//								"backgroundColor" : "rgb(102, 204, 255)",
//								"borderColor" : "rgb(102, 204, 255)",
//								data : cpu.split(",")
//							} ,
//							{
//								label : 'a',
//								fill : false,
//								"backgroundColor" : "rgb(102, 204, 123)",
//								"borderColor" : "rgb(102, 204, 123)",
//								data : [2,2,2,2,2,2,2,2,2,2,2,2]
//							} ]
			    		
			    		$.each(dynamic_results,function(keys,values) {
			    		  console.log('dict', dynamic_results[keys], values);
			    	});
			    		
		    	});
    		});

			var config = {
			type : 'line',
					data : {
						labels : labels.split(","),
						datasets : dynamic_results
				},
				options: {
					responsive: true,
					scales: {
						yAxes: [{
							scaleLabel: {
								display: true,
								labelString: 'CPU Used Percent (%)'
						      }
						    }]
					} ,
					title: {
						display: true,
						text: 'Server Resource CPU RealTime Chart'
					},
				}
			};
		
			var ctx = document.getElementById('line-chart-cpu').getContext('2d');
			new Chart(ctx, config);
			
			
			var config = {
					type : 'line',
							data : {
								labels : labels.split(","),
								datasets : dynamic_mem_results
						},
						options: {
							responsive: true,
							scales: {
								yAxes: [{
									scaleLabel: {
										display: true,
								    labelString: 'Memory Used Percent (%)'
								      }
								    }]
							} ,
							title: {
								display: true,
								text: 'Server Resource Memory RealTime Chart'
							},
						}
					};
				
					var ctx = document.getElementById('line-chart-memory').getContext('2d');
					new Chart(ctx, config);
    	
        },
    error : function(e){ 
        },
    complete : function() {
//        	hideProgress();   
//        	$("#loading").empty();
        	$("#LoadingImage").hide();
//    	alert(labels);
        }
     });
}



function make_graph() 
{
// alert('ok');
// labels = JSON.parse({{ labels | tojson }})
// data = JSON.parse({{ data | tojson }})
	var data = [1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
	var labels =  ["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
//	var labels =  [06-18 09:48:33, 06-18 11:14:55, 06-18 11:15:03, 06-18 11:25:00, 06-18 12:08:55, 06-18 12:09:22, 06-18 12:11:55, 06-18 12:12:10, 06-18 12:12:50, 06-18 12:14:00, 06-18 12:14:56, 06-18 12:17:29, 06-18 12:18:15, 06-18 12:24:23, 06-18 12:26:50, 06-18 12:27:27, 06-18 12:27:53, 06-18 12:28:47, 06-18 12:31:10, 06-18 12:34:10, 06-18 12:44:45, 06-18 12:45:51, 06-18 12:47:06, 06-18 12:47:10, 06-18 12:48:04, 06-18 12:49:54, 06-18 12:52:35, 06-18 13:21:51, 06-18 13:22:22, 06-18 13:23:56, 06-18 13:24:34, 06-18 15:23:52, 06-18 15:24:14, 06-18 15:25:19, 06-18 15:26:21, 06-18 15:32:47, 06-18 15:32:59, 06-18 15:34:42, 06-18 15:35:19, 06-18 15:35:27, 06-18 15:36:59, 06-18 15:37:04, 06-18 15:37:09, 06-18 15:37:31, 06-18 23:06:25, 06-18 23:06:28, 06-18 23:06:46, 06-18 23:07:03, 06-18 23:08:10, 06-18 23:08:16, 06-18 23:08:24, 06-18 23:08:28, 06-22 18:59:09, 06-22 18:59:11, 06-23 10:40:19, 06-23 13:35:59, 06-23 13:36:59, 06-23 13:42:13, 06-23 15:34:23, 06-23 15:37:08, 06-23 15:38:54, 06-23 15:48:45, 06-24 11:07:24, 06-24 13:59:48, 06-24 14:00:06, 06-24 14:00:09, 06-24 14:01:51, 06-24 14:01:55, 06-26 16:47:20, 06-26 17:39:37, 06-29 14:53:03];
//	alert(data);
	new Chart(document.getElementById("line-chart"), {
		"type" : "line",
		"data" : {
			"labels" : labels,
			"datasets" : [ {
				"label" : "AI Detection Delay Time (수동검출)",
				"data" : data,
				"fill" : false,
				"borderColor" : "rgb(102, 204, 255)",
				"lineTension" : 0.5
			} ]
		},
		"options" : {}
	});
 
 }
