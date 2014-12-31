var host = "http://localhost:8315"

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
		var validateUrl = host + "/api/service/add?service-name=" + name + "&url=" + url;
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