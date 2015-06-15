request = require("request");
var serverIp = "45.55.206.156";	
// Process command line argument
if(process.argv[2]){
	var ip = process.argv[2].toLowerCase();
	if(ip.indexOf("ip=") != -1){
		ip = ip.replace("ip=", "");
		if(ip.split(".").length == 4){
			serverIp = ip;
		}else{
			console.log("Ip provided ("+ip+") is invalid. Using default ip : "+serverIp);
		}
	}
}

var elasticSearchPort = "9200";
var filePath = "/var/www/files";
var riverName = "files";
var indexName = riverName;

var riverSettings = {
		  "type": "fs",
		  "fs": {
			"url": "/var/www/files",
			"includes": ["*.pdf", "*.xls", "*.xlsx", "*.doc", "*.docx", "*.txt"]
		  },
		  "index":{
		  	"index": "files",
		  	"type": "doc"
		  }
		};

var verifyRiverExist = function(){
	var requestSettings = {
		url: "http://"+serverIp+":"+elasticSearchPort+"/_river/"+riverName+"/_search?q=*",
		method: "POST"
	};

	request(requestSettings, function(error,response, body){
		if(error){
			console.log(error);
		}else{
			var jsonBody = JSON.parse(body);
			if(jsonBody.hits.total == 0){
				console.log("River '"+riverName+"' need to be created.");
				createRiver();
			}else{
				// River already exist
				console.log("River '"+riverName+"' already exist.");
				verifyIndexExist(function(indexExist){
					var doRemoveriver = function(){
						removeRiver(function(){
							verifyRiverExist();		// We deleted everything, we recreate everything
						});
					}
					if(indexExist){
						removeIndex(doRemoveriver);
					}else{
						doRemoveriver();
					}
				});
			}
		}
	});
};

var createRiver = function(callback){
	var requestSettings = {
		url: "http://"+serverIp+":"+elasticSearchPort+"/_river/"+riverName+"/_meta",
		method: "PUT",
		json : riverSettings
	};

	request(requestSettings, function(error,response, body){
		if(error){
			console.log(error);
		}else{
			if(body.created){
				console.log("River '"+riverName+"' created.");
			}else{
				console.log("River '"+riverName+"' updated to version : "+body._version);
			}
		}
	});
};

var verifyIndexExist = function(callback){
	var requestSettings = {
		url: "http://"+serverIp+":"+elasticSearchPort+"/"+indexName+"/_search?q=*",
		method: "POST"
	};

	request(requestSettings, function(error,response, body){
		if(error){
			console.log(error);
		}else{
			var jsonBody = JSON.parse(body);
			if(jsonBody.status == 404){
				// Index doesn't exist
				console.log("Index '"+indexName+"' need to be created.");
				if(callback){
					callback(false);
				}
			}else{
				console.log("Index '"+indexName+"' already exist.");
				if(callback){
					callback(true);
				}
			}
		}
	});
};

var removeRiver = function(callback){
	var requestSettings = {
		url: "http://"+serverIp+":"+elasticSearchPort+"/_river/"+riverName,
		method: "DELETE"
	};

	request(requestSettings, function(error,response, body){
		if(error){
			console.log(error);
		}else{
			var jsonBody = JSON.parse(body);
			if(jsonBody.acknowledged){
				console.log("River '"+riverName+"' removed.");
				if(callback){
					callback();
				}
			}else if(jsonBody.status == 404){
				console.log("River '"+riverName+"' doesn't exist.");
				if(callback){
					callback();
				}
			}else{
				console.log("Unable to remove river '"+riverName+"'.");
			}
		}
	});
};

var removeIndex = function(callback){
	var requestSettings = {
		url: "http://"+serverIp+":"+elasticSearchPort+"/"+indexName,
		method: "DELETE"
	};

	request(requestSettings, function(error,response, body){
		if(error){
			console.log(error);
		}else{
			var jsonBody = JSON.parse(body);
			if(jsonBody.acknowledged){
				console.log("Index '"+indexName+"' removed.");
				if(callback){
					callback();
				}
			}else if(jsonBody.status == 404){
				console.log("Index '"+indexName+"' doesn't exist.");
				if(callback){
					callback();
				}
			}else{
				console.log("Unable to remove index '"+indexName+"'.");
			}
			/*if(jsonBody.status == 404){
				// Index doesn't exist
				console.log("Index '"+indexName+"' need to be created.");
			}else{
				console.log("Index '"+indexName+"' already exist.");
			}*/
		}
	});
};

verifyRiverExist();		// If river exist, remove it and recreate it
						// Else, create the river