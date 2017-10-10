/**
 * http://usejsdoc.org/
 */

var tabulate = function(data, columns) {
	/* select class="companyList" */
	//class display for paginator
	var table = d3.select('.companyList').append('table').classed('paging display', true)
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
		if (d.value == ""){
			return "NA"			
		}
		else if(d.column === "ASX code")
			return null
		else
			return d.value
	})
	
	//filter out 'Change' so that color change based on value
	cells.filter(function(d, i) {return d.column ==="Change"})
	.attr("style", function(d){
		if ( d.value < 0)
			return "color: red;"
		else if (d.value == "")
			return null
		else
			return "color: green;"
	})
	.html(function(d){
		if (d.value == "")
			return "NA"
		else
			return d.value
	})
	
	//filter out 'code' to link to share detail page
	cells.filter(function(d, i) {return d.column ==="ASX code"})
	.append("a")
	.attr("href", function(d){
		return "CompanyPage?code=" + d.value
	})
	.html(function(d){
		return d.value
	})

	return table;
}

d3.csv('/csv/ASXListedCompanies.csv', function(data) {
	var columns = [ 'ASX code', 'Company name', 'Price', 'High', 'Low', 'Change', 'Volume' ]
	
	tabulate(data, columns)
})

/* delay the pagination function as
 * document.ready is inconsistent
 */ 

setTimeout(function(){
	$(".paging").DataTable( {
	    "scrollX": true,
	    "pagingType": "full_numbers"
	} );
	document.getElementById("tableStatus").innerHTML = null;
}, 500);