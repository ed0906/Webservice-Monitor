var host = "http://localhost:8315"

function openOptions() {
	document.getElementById('abc').style.display = "block";
	document.getElementById('canvas').style.display = "none";
	
	var url = host + "/api/service/get";
	var xmlHttp = new XMLHttpRequest();
    
	xmlHttp.onreadystatechange=function() {
		if (xmlHttp.readyState==4 && xmlHttp.status==200) {
			var json = JSON.parse(xmlHttp.responseText);
			var select1 = document.getElementById("parent");
			var select2 = document.getElementById("child");
			
			//Add new contents
			for (i = 0; i < json.length; i++) {
				var option1 = document.createElement("option");
				var option2 = document.createElement("option");
				option1.innerHTML = json[i].name;
				option2.innerHTML = json[i].name;
				select1.appendChild(option1);
				select2.appendChild(option2);
			}
		}
	}
	xmlHttp.open("GET", url, true);
    xmlHttp.send();
}

function closeOptions(){
	document.getElementById('abc').style.display = "none";
	document.getElementById('canvas').style.display = "block";
}

function validateOptions() {
	var parent = document.getElementById("parent");
	var child = document.getElementById("child");
	
	var option1 = parent.options[parent.selectedIndex].value;
	var option2 = child.options[child.selectedIndex].value;
	
	var validateUrl = host + "/api/service/dependency/add?parent=" + option1 + "&child=" + option2;
	var xmlHttp = new XMLHttpRequest();
		
	xmlHttp.onreadystatechange=function() {
		if (xmlHttp.readyState==4) {
			if(xmlHttp.status == 200){
				alert("Successfully Added");
			}else{
				alert("Could not add dependency");
			}
		}
	}
	xmlHttp.open("POST", validateUrl, true);
	xmlHttp.send();
}