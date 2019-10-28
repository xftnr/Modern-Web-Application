function myFunction() {

	var content;
	var url = new XMLHttpRequest();
	url.open("GET", "/assignment6/app/path/get-count/", false);
	url.send();

	document.getElementById("reports").innerHTML = "";
	content=url.response;

    var str = content;
    var res = str.split("+");

    document.getElementById("reports").border = 1;
    document.getElementById("reports").innerHTML += "<td>"+ "Issue" +"</td>" + "<td>"+ "Count" +"</td>";
	for(i=0;i<res.length-1;i+=2)
	{
		if(i+1 != null) {
			document.getElementById("reports").innerHTML += "<td>"+ res[i] +"</td>" + "<td>"+ res[i+1] +"</td>";
		}
	}

}
