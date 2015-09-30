var Dispatch = require('dispatch');
var Fs = require('fs');
var Http = require('http');
var Request = require('request');
var Secrets = tryRequire('./private/api_keys');
var ShortId = require('shortid');
var WebSocketServer = require('ws').Server;

var socketServer = new WebSocketServer({ port:8080 });
var clientManager = new ClientManager();

socketServer.on('connection', function (socket) {
    var client = new Client(socket);
    clientManager.add(client); 
    socket.on('close', function close() { 
        clientManager.remove(client); 
        console.log("bye client: %s", client.clientId);
    });

    client.sendHello();
    console.log("new client: %s", client.clientId);
});

var server = Http.createServer(
    Dispatch({
        '/client': serveClient,
        '.*': serve404
    })
);

function serveClient(req, res) {
    Fs.readFile('client.html', 'utf8', function (err, html) {
        if (err) {
            serve500(req, res);
            console.log("%s", err);
        }
        else {
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(html);
            res.end();
        }
    });
}

function serve404(req, res) {
    res.writeHead(404, {'Content-Type': 'text/html'});
    res.write("that does not exist.");
    res.end();
}

function serve500(req, res) {
    res.writeHead(500, {'Content-Type': 'text/html'});
    res.write("there was a server error.");
    res.end();
}

function tryRequire(path) {
    try {
        return require(path);
    }
    catch (e) {
        console.log("FATAL: required path not found: %s", path);
        process.exit(1);
    }
}

function Client(socket) {
    var fields = {
        socket: socket,
        clientId: ShortId.generate(),
        sendHello: sendHello,
        sendUpdate: sendUpdate,
    }

    function ackHandler(error) {
        if (error) {
            console.log(error);
        }
    }

    function sendHello() {
        var raw = JSON.stringify({
            type: 0,
            clientId: fields.clientId, 
            google_maps_browser_key: Secrets.google_maps_browser_key,
        });

        socket.send(raw, ackHandler);
    }

    function sendUpdate(content) {
        var raw = JSON.stringify({
            type: 1, 
            clientId: fields.clientId, 
            content: content
        });

        socket.send(raw, ackHandler);
    }

    socket.on('message', function incoming(message) {
        console.log("client say: %s : %s", fields.clientId, message);
    });

    return fields;
}

function ClientManager() {
    var clients = [];
    var updateCounter = 0;

    function add(client) {
        clients.push(client);
    }

    function remove(client) {
        var index = clients.indexOf(client);
        var removed_client = clients.splice(index, 1);
    }

    function tick() {
        clients.forEach( function(client) {
            client.sendUpdate(updateCounter++);
        });
    }

    return {
        add:add,
        remove:remove,
        tick: tick
    }
}

function buildRequest(baseUrl, params) {
    var result = undefined;

    for (var name in params) {
        result = result === undefined ? baseUrl : result.concat("&");
        result = result.concat(name + "=" + params[name]);
    }

    return result;
}

function snapToRoads(path) {
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
        }
        else if(response.statusCode !== 200) {
            console.log(
                "%s: %s", reqUrl, response.statusCode);
        }
        else {
            console.log(
                "snapToRoads() returned with success");
        }
    });
}

console.log("#GoNamTaxi2015 Prototype Server");
server.listen(8081);
