function loadTable() {
	var url = "http://localhost:8315/api/service";
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
				cell5.innerHTML = "<img src=\"res/x_blue.png\" onClick=\"removeEntry('" + json[i].name + "')\">";
				
			}
			
			//Update LastUpdated
			var lastUpdated = document.getElementById("last-updated");
			lastUpdated.innerHTML = "Last Updated: " + (new Date()).toLocaleTimeString();
		}
	}
	xmlHttp.open("GET", url, true);
    xmlHttp.send();
}

function openOptions() {
	document.getElementById('abc').style.display = "block";
}

function closeOptions(){
	document.getElementById('abc').style.display = "none";
}

function validateOptions() {
	var name = document.getElementById("name").value;
	var url = document.getElementById("url").value;
	if (name == "" || url == "") {
		alert("Both name & Url fields must be filled");
	} else {
		var validateUrl = "http://localhost:8315/api/service?service-name=" + name + "&url=" + url;
		var xmlHttp = new XMLHttpRequest();
		
		xmlHttp.onreadystatechange=function() {
			if (xmlHttp.readyState==4) {
				if(xmlHttp.status == 200){
					alert("Successfully Added");
					closeOptions();
					loadTable();
				}else{
					alert("Could not add webservice, please check name & url are valid (Note: url's should have http:// at the start)");
				}
			}
		}
		xmlHttp.open("POST", validateUrl, true);
		xmlHttp.send();
	}
}

function removeEntry(serviceName) {
	if(confirm("Are you sure you want to delete " + serviceName + "?")){
		var validateUrl = "http://localhost:8315/api/service/delete?service-name=" + name;
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