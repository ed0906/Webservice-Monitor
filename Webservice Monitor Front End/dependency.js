var redraw;
var host = "http://localhost:8315";

/* only do all this when document has finished loading (needed for RaphaelJS) */
window.onload = function() {

      var url = host + "/api/service/dependency";
	  var xmlHttp = new XMLHttpRequest();
	  xmlHttp.onreadystatechange=function() {
		  if (xmlHttp.readyState==4 && xmlHttp.status==200) {
			var json = JSON.parse(xmlHttp.responseText);
			var g = new Graph();
			for(var sNo=0; sNo<json.length; sNo++){
				var name = json[sNo].service.name;
				g.addNode(name);
				
				var dependents = json[sNo].dependantSystems;
				for(var dNo=0; dNo<dependents.length; dNo++){
					var dName = dependents[dNo];
					g.addEdge(name, dName, { directed: true});
				}
			}
			
			var layouter = new Graph.Layout.Spring(g);
	
			var width = 960;
			var height = 600;
	  		var renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
	
	  		redraw = function() {
				layouter.layout();
				renderer.draw();
	  		};
		  }
	  }
	  
	  xmlHttp.open("GET", url, true);
      xmlHttp.send();
};

