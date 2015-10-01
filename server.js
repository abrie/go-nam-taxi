#!/usr/bin/env node

var Dispatch = require('dispatch');
var Fs = require('fs');
var Http = require('http');
var Request = require('request');
var Secrets = tryRequire('./private/api_keys');
var ShortId = require('shortid');
var WebSocketServer = require('ws').Server;

var socketServer = new WebSocketServer({ port:9090 });
var clientManager = new ClientManager();

socketServer.on('connection', function(socket) {
    var client = clientManager.add(new Client(socket));

    socket.on('close', function() { 
        clientManager.remove(client); 
    });
});

var server = Http.createServer(
    Dispatch({
        '/client': serveClient,
        '.*': serve404
    })
);

function serveClient(req, res) {
    Fs.readFile('client.html', 'utf8', function(err, html) {
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
    var state = {
        socket: socket,
        clientId: ShortId.generate(),
    }

    var methods = {
        init: init,
        dispose: dispose,
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
            clientId: state.clientId, 
            google_maps_browser_key: Secrets.google_maps_browser_key,
        });

        socket.send(raw, ackHandler);
    }

    function sendUpdate(content) {
        var raw = JSON.stringify({
            type: 1, 
            clientId: state.clientId, 
            content: content
        });

        socket.send(raw, ackHandler);
    }

    function sendSnappedPoints(snappedPoints) {
        var raw = JSON.stringify({
            type: 2,
            clientId: state.clientId,
            content: snappedPoints
        });

        socket.send(raw, ackHandler);
    }

    function receiveMessage(message) {
        json = JSON.parse(message); 
        if (json.path) {
            snapToRoads(json.path, function(err, response) {
                if (response) {
                    sendSnappedPoints(response.snappedPoints);
                }
            });
        }
    }

    function init() {
        socket.on('message', receiveMessage);
        console.log("new client: %s", state.clientId);
        sendHello();
    }

    function dispose() {
        console.log("bye client: %s", state.clientId);
    }

    return methods;
}

function ClientManager() {
    var clients = [];
    var updateCounter = 0;

    function add(client) {
        clients.push(Client);
        client.init();
    }

    function remove(client) {
        var index = clients.indexOf(client);
        clients.splice(index, 1);
        client.dispose();
    }

    function tick() {
        clients.forEach( function(client) {
            client.sendUpdate(updateCounter++);
        });
    }

    return {
        add: add,
        remove: remove,
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

console.log("#GoNamTaxi2015 Prototype Server");
server.listen(8080);
