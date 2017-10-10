/**
 * http://usejsdoc.org/
 */

var tabulate = function(data, columns) {
	//class display for paginator
	var table = d3.select('.leaderboard').append('table').classed('paging display', true)
	var thead = table.append('thead')
	var tbody = table.append('tbody')

	thead.append('tr').selectAll('th').data(columns).enter().append('th').text(
			function(d) {
				return d
			})

	var rows = tbody.selectAll('tr').data(data).enter().append('tr')

	var cells = rows.selectAll('td').data(function(row) {
		return columns.map(function(column) {
			return {
				column : column,
				value : row[column]
			}
		})
	}).enter().append('td').text(function(d) {
			return d.value
	})
	
	//filter out 'Rank' so that color change based on value
//	cells.filter(function(d, i) {return d.column ==="Rank"})
//	.attr("style", function(d){
//		if ( d.value == 1)
//			return "color: #FFD700;"	//gold
//		else if (d.value == 2)
//			return "color: #E6E8FA;"	//silver
//		else if (d.value == 3)
//			return "color: #8C7853;"	//BRONZE
//	})
//	.html(function(d){
//		return d.value
//	})
	
	return table;
}

//d3.csv('/csv/leaderboard.csv', function(data) {
//	var columns = [ 'Rank', 'User', 'Points']
//	
//	tabulate(data, columns)
//})

//wait for csv
setTimeout(function(){
	d3.csv('/csv/leaderboard.csv', function(data) {
		var columns = [ 'Rank', 'User', 'Points']
		
		tabulate(data, columns)
	})
}, 500);

/* delay the pagination function as
 * document.ready is inconsistent
 */ 
setTimeout(function(){
	$(".paging").DataTable( {
	    "pagingType": "full_numbers"
	} );
	document.getElementById("tableStatus").innerHTML = null;
}, 800);