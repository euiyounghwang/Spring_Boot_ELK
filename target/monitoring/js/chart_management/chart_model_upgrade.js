/**
 * 서버 실시간 조회
 */



$( document ).ready(function() {
//	make_graph();
	show_graph();
//	displayLineChart();

	$(document.getElementById("butt_stop")).trigger('click');
});




function show_graph_1() {
//	var data = [1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
//	var labels =  ["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
	var data = [];
	var lables = [];
	var params = {};
	params['version_params'] = $('#version_params').val();
	params['EVENT_ID'] = 'get_model_upgrade_transaction';
	params['start'] = '0';
	params['length'] = '200';
	params['start_date'] = dateAddDel(getTimeStamp(), -120, 'd');
	$.ajax({
		type : 'POST',
    url:'get_model_upgrade_transaction',
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
    	$.each(datas,function(key,value) {
			    	$.each(value,function(keys,values) {
//			    		console.log('native', keys, values.data, values.label);
			    		pos_f1 = values.pos_f1;
			    		pos_accuracy = values.pos_accuracy;
			    		pos_precision = values.pos_precision;
			    		pos_recall = values.pos_recall;
			    		labels = values.label;
			    		console.log('split', labels, pos_f1.split(","), labels.split(","), typeof(labels),
			    				typeof(pos_f1.split(",")), typeof(labels.split(",")));
			    	});
    		});
    	
	    	// 실시간 AI Delay Time 조회
	    	new Chart(document.getElementById("line-chart-positive"), {
	    		"type" : "line",
	    		"data" : {
	    			"labels" : labels.split(","),
	    			"datasets" : [ {
	    				"label" : "AI Detection Delay Time (수동검출)",
	    				"data" : pos_f1.split(","),
	    				"fill" : false,
	    				"borderColor" : "rgb(102, 204, 255)",
	    				"lineTension" : 0.5
		    			} ]
		    		},
		    		"options" : {}
		    	});
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



function show_graph() {
//	var data = [1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
//	var labels =  ["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
	var data = [];
	var lables = [];
	var params = {};
	params['version_params'] = $('#version_params').val();
	params['EVENT_ID'] = 'get_model_upgrade_transaction';
	params['start'] = '0';
	params['length'] = '200';
	params['start_date'] = dateAddDel(getTimeStamp(), -60, 'd');
	$.ajax({
		type : 'POST',
    url:'get_model_upgrade_transaction',
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
    	$.each(datas,function(key,value) {
			    	$.each(value,function(keys,values) {
//			    		console.log('native', keys, values.data, values.label);
			    		pos_f1 = values.pos_f1;
			    		pos_accuracy = values.pos_accuracy;
			    		pos_precision = values.pos_precision;
			    		pos_recall = values.pos_recall;
			    		
			    		neg_f1 = values.neg_f1;
			    		labels = values.label;
			    		console.log('split', labels, pos_f1.split(","), labels.split(","), typeof(labels),
			    				typeof(pos_f1.split(",")), typeof(labels.split(",")));
			    	});
    		});
    	
    	var config = {
    			type: 'line',
    			data: {
//    				labels: ['2020-07-02 23:32:16','2020-07-02 23:31:56','2020-07-02 23:31:33','2020-07-01 13:45:46'],
    				labels: labels.split(","),
    				datasets: [
    					{
	    					label: 'F1 Score Positive',
	    					fill: false,
//	    					backgroundColor: window.chartColors.red,
	    					"backgroundColor" : "rgb(102, 204, 255)",
//	    					borderColor: window.chartColors.red,
	    					"borderColor" : "rgb(102, 204, 255)",
	    					data : pos_f1.split(",")
//	    					data: ['0.79','0.52','0.79','0.29']
    					},
    					{
	    					label: 'F1 Score Negative',
	    					fill: false,
	    					backgroundColor: window.chartColors.red,
//	    					"backgroundColor" : "rgb(102, 204, 255)",
	    					borderColor: window.chartColors.red,
//	    					"borderColor" : "rgb(102, 204, 255)",
	    					data : neg_f1.split(",")
//	    					data: ['0.79','0.52','0.79','0.29']
    					}
    			 ]
    			},
    			options: {
					responsive: true,
					scales: {
						yAxes: [{
							scaleLabel: {
								display: true,
								labelString: 'Model Performance Ratio (%)'
						      }
						    }]
					} ,
					title: {
						display: true,
						text: 'AI Model Upgrade Positive Peformance Maxtrix History(FastText)'
					},
				}
    			
    		};
    	
    	var ctx = document.getElementById('line-chart-positive').getContext('2d');
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



function show_graph_backup() {
//	var data = [1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
//	var labels =  ["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
	var data = [];
	var lables = [];
	var params = {};
	params['version_params'] = $('#version_params').val();
	params['EVENT_ID'] = 'get_model_upgrade_transaction';
	params['start'] = '0';
	params['length'] = '200';
	params['start_date'] = dateAddDel(getTimeStamp(), -30, 'd');
	$.ajax({
		type : 'POST',
    url:'get_model_upgrade_transaction',
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
    	$.each(datas,function(key,value) {
			    	$.each(value,function(keys,values) {
//			    		console.log('native', keys, values.data, values.label);
			    		pos_f1 = values.pos_f1;
			    		pos_accuracy = values.pos_accuracy;
			    		pos_precision = values.pos_precision;
			    		pos_recall = values.pos_recall;
			    		labels = values.label;
			    		console.log('split', labels, pos_f1.split(","), labels.split(","), typeof(labels),
			    				typeof(pos_f1.split(",")), typeof(labels.split(",")));
			    	});
    		});
    	
    	var config = {
    			type: 'line',
    			data: {
//    				labels: ['2020-07-02 23:32:16','2020-07-02 23:31:56','2020-07-02 23:31:33','2020-07-01 13:45:46'],
    				labels: labels.split(","),
    				datasets: [{
	    					label: 'F1 Score',
	    					fill: false,
	    					backgroundColor: window.chartColors.red,
	    					borderColor: window.chartColors.red,
	    					data : pos_f1.split(",")
//	    					data: ['0.79','0.52','0.79','0.29']
    						},
    						{
	    					label: 'Accuracy',
	    					fill: false,
	    					backgroundColor: window.chartColors.blue,
	    					borderColor: window.chartColors.blue,
	    					data : pos_accuracy.split(",")
//	    					data: ['0.39','0.42','0.89','0.89']
		    				},
		    				{
	    					label: 'Precision',
	    					fill: false,
	    					backgroundColor: window.chartColors.orange,
	    					borderColor: window.chartColors.orange,
	    					data : pos_precision.split(",")
//	    					data: ['0.49','0.92','0.29','0.39']
		    				},
		    				{
		    					label: 'Recall',
		    					fill: false,
		    					backgroundColor: window.chartColors.green,
		    					borderColor: window.chartColors.green,
		    					data : pos_recall.split(",")
//		    					data: ['0.49','0.92','0.29','0.39']
			    		    }
    			 ]
    			},
    			options: {
    				responsive: true,
    				title: {
    					display: true,
    					text: 'AI Model Upgrade Positive Peformance Maxtrix History(FastText)'
    				},
    			}
    		};
    	
    	var ctx = document.getElementById('line-chart-positive').getContext('2d');
    	new Chart(ctx, config);
    	
    	var config = {
    			type: 'line',
    			data: {
//    				labels: ['2020-07-02 23:32:16','2020-07-02 23:31:56','2020-07-02 23:31:33','2020-07-01 13:45:46'],
    				labels: labels.split(","),
    				datasets: [{
	    					label: 'F1 Score',
	    					fill: false,
	    					backgroundColor: window.chartColors.red,
	    					borderColor: window.chartColors.red,
	    					data : pos_f1.split(",")
//	    					data: ['0.79','0.52','0.79','0.29']
    						},
    						{
	    					label: 'Accuracy',
	    					fill: false,
	    					backgroundColor: window.chartColors.blue,
	    					borderColor: window.chartColors.blue,
	    					data: ['0.39','0.42','0.89','0.89']
		    				},
		    				{
	    					label: 'Precision',
	    					fill: false,
	    					backgroundColor: window.chartColors.orange,
	    					borderColor: window.chartColors.orange,
	    					data: ['0.49','0.92','0.29','0.39']
		    				},
		    				{
		    					label: 'Recall',
		    					fill: false,
		    					backgroundColor: window.chartColors.green,
		    					borderColor: window.chartColors.green,
//		    					data : pos_recall.split(",")
		    					data: ['0.49','0.92','0.29','0.39']
			    		    }
    			 ]
    			},
    			options: {
    				responsive: true,
    				title: {
    					display: true,
    					text: 'AI Model Upgrade Negative Peformance Maxtrix History(FastText)'
    				},
    			}
    		};
    	
    	var ctx = document.getElementById('line-chart-negative').getContext('2d');
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


function displayLineChart() 
{
	  var data = {
		labels : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
		datasets : [ {
			label : "Prime and Fibonacci",
			fillColor : "rgba(220,220,220,0.2)",
			strokeColor : "rgba(220,220,220,1)",
			pointColor : "rgba(220,220,220,1)",
			pointStrokeColor : "#fff",
			pointHighlightFill : "#fff",
			pointHighlightStroke : "rgba(220,220,220,1)",
			data : [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
		}, {
			label : "My Second dataset",
			fillColor : "rgba(151,187,205,0.2)",
			strokeColor : "rgba(151,187,205,1)",
			pointColor : "rgba(151,187,205,1)",
			pointStrokeColor : "#fff",
			pointHighlightFill : "#fff",
			pointHighlightStroke : "rgba(151,187,205,1)",
			data : [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
		} ]
	};
	var ctx = document.getElementById("line-chart").getContext("2d");
	var options = {};
	var lineChart = new Chart(ctx).Line(data, options);
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
