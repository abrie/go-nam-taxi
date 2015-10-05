var Util = require('./util.js');
var Secrets = Util.tryRequire('./private/api_keys');

module.exports = {
    snapToRoads: snapToRoads
}

function buildRequest(baseUrl, params) {
    var result = undefined;

    for (var name in params) {
        result = result === undefined ? baseUrl : result.concat("&");
        result = result.concat(name + "=" + params[name]);
    }

    return result;
}

function snapToRoads(path, callback) {
    // path is expected as a string: 'lat,lon|lat,lon|etc...'
    // example: "-22.571816,17.080843|-22.571103,17.08359"
    var baseUrl = "https://roads.googleapis.com/v1/snapToRoads?";
    var params = {
        "key": Secrets.google_maps_server_key,
        "interpolate": true,
        "path": path,
    }; 

    var reqUrl = buildRequest(baseUrl, params);

    Request.get(reqUrl, function(error, response, body) {
        if (error) {
            console.log(
                "%s:%s:%s:%s", error.syscall, error.hostname, error.port,error.code);
            callback("error occurred", undefined);
        }
        else if(response.statusCode !== 200) {
            console.log(
                "%s: %s", reqUrl, response.statusCode);
            callback("status not 200", undefined);

        }
        else {
            var json = JSON.parse(body);
            console.log(body);
            callback(undefined, json);
        }
    });
}
