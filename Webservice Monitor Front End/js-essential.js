var host = "http://localhost:8315"

function loadTable() {
	var url = host + "/api/service/get";
	var xmlHttp = new XMLHttpRequest();
    
	xmlHttp.onreadystatechange=function() {
		if (xmlHttp.readyState==4 && xmlHttp.status==200) {
			var json = JSON.parse(xmlHttp.responseText);
			var table = document.getElementById("table");
			
			//Remove table contents
			var tableRows = table.getElementsByTagName('tr');
			var rowCount = tableRows.length;
			for (var x=rowCount-1; x>0; x--) {
			   table.removeChild(tableRows[x]);
			}
			
			//Add new contents
			for (i = 0; i < json.length; i++) {
				var row = table.insertRow(1);
				var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				var cell3 = row.insertCell(2);
				var cell4 = row.insertCell(3);
				var cell5 = row.insertCell(4);
				cell1.innerHTML = json[i].name;
				cell2.innerHTML = json[i].metrics.responseCode;
				if(json[i].metrics.responseCode != 200){
					cell2.style.background = 'red';
				}
				cell3.innerHTML = json[i].metrics.responseTime;
				cell4.innerHTML = new Date(json[i].metrics.date).toUTCString();
				cell5.innerHTML = "<img src=\"res/x_blue.png\" id=\"delete\" onClick=\"removeEntry('" + json[i].name + "')\">";
				
				var option = document.createElement("option");
				option.text = json[i].name;				
			}
			
			//Update LastUpdated
			var lastUpdated = document.getElementById("last-updated");
			lastUpdated.innerHTML = "Last Updated: " + (new Date()).toLocaleTimeString();
		}
	}
	xmlHttp.open("GET", url, true);
    xmlHttp.send();
}

function removeEntry(serviceName) {
	if(confirm("Are you sure you want to delete " + serviceName + "?")){
		var validateUrl = host + "/api/service/delete?service-name=" + serviceName;
		var xmlHttp = new XMLHttpRequest();
		
		xmlHttp.onreadystatechange=function() {
			if (xmlHttp.readyState==4) {
				if(xmlHttp.status == 200){
					alert("Successfully removed");
					loadTable();
				}else{
					alert("Could not remove webservice");
				}
			}
		}
		xmlHttp.open("POST", validateUrl, true);
		xmlHttp.send();
	}
}