//get ASXCode by id
var topShare = document.getElementById("topShare").value;
console.log(topShare);

//get json by code
$.getJSON(
	// symbol will be get by click input
	'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=' + topShare +'&apikey=MP9H93RQEUUFGX07',
	function(data) {

		// split the data set into ohlc(open, high, low, close) and volume
		var ohlc = [], volume = [],
		// set the allowed units for data grouping
		groupingUnits = [ [ 'week', // unit name
		[ 1 ] // allowed multiples
		], [ 'month', [ 1, 2, 3, 4, 6 ] ] ];

		//setup data get by api
		var arr = $.map(data, function(el) {
            return el;
        });
		//arr[0] = general
		//arr[1] = all data by dates
		document.getElementById("refreshTime").innerHTML = arr[0]["3. Last Refreshed"];
		
		//map all data by dates
        var arr2 = $.map(arr[1], function(el) {
            return el;
        });
        
        //get all date
        var arr3 = [];
        try{
        	arr3.push([ Object.keys(arr[1]) ]);        
        
	        //value of date need to be from low to high
	        for(var i=arr2.length-1; i>=0; i--){
	            ohlc.push(
	                [
	                //parse data to chart-readable format
	                Date.parse(arr3[0][0][i]),
	                parseFloat(arr2[i]["1. open"]),		//parseFloat as the value appears to be string
	                parseFloat(arr2[i]["2. high"]),
	                parseFloat(arr2[i]["3. low"]),
	                parseFloat(arr2[i]["4. close"])
	            ]);
	            
	            
	            volume.push([ 
	            	Date.parse(arr3[0][0][i]), // the date
	            	parseFloat(arr2[i]["5. volume"]) // the volume
					]);
	        }
        }
        catch(err){
        	document.getElementById("refreshTime").innerHTML = "Seems like there is no data currently available for this share."
        	console.log(err);
        }        
        

		// create the chart
		Highcharts.stockChart('container', {

			rangeSelector : {
				selected : 1
			},

			title : {
				text : arr[0]["2. Symbol"]
			},

			yAxis : [ {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'OHLC'
				},
				height : '60%',
				lineWidth : 2
			}, {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Volume'
				},
				top : '65%',
				height : '35%',
				offset : 0,
				lineWidth : 2
			} ],

			tooltip : {
				split : true
			},

			series : [ {
				type : 'candlestick',
				name : arr[0]["2. Symbol"],
				data : ohlc,
				dataGrouping : {
					units : groupingUnits
				}
			}, {
				type : 'column',
				name : 'Volume',
				data : volume,
				yAxis : 1,
				dataGrouping : {
				units : groupingUnits
			}
		} ]
	});
});