/**
 * http://usejsdoc.org/
 */

/** source: https://gist.github.com/jfreels/6814721 */

var tabulate = function(data, columns) {
	/* select id="companyList" */
	var table = d3.select('.companyList').append('table').classed('paging display', true);
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
		if (d.value == "")
			return "NA";
		else
			return d.value
	})
	
	cells.filter(function(d, i) {return d.column ==="Change"})
	.attr("style", function(d){
		if ( d.value < 0)
			return "color: red;";
		else
			return "color: green;";
	})
	.html(function(d){
		return (d.value);
	})

	return table;
}

d3.csv('/csv/ASXListedCompanies.csv', function(data) {
	var columns = [ 'Company name', 'ASX code', 'Price', 'High', 'Low', 'Change', 'Volume' ]
		
	tabulate(data, columns)
})