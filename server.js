var secrets = tryRequire('./private/api_keys');
var shortid = require('shortid');
var fs = require('fs');
var http = require('http');
var dispatch = require('dispatch');
var request = require('request');
var WebSocketServer = require('ws').Server;
var Plates = require('plates');
var wss = new WebSocketServer({ port:8080 });
var clients = new Clients();

wss.on('connection', function (socket) {
    var client = new Client(socket);
    clients.add(client); 
    socket.on('close', function close() { 
        clients.remove(client); 
        console.log("bye client: %s", client.id);
    });

    client.sendHello();
    console.log("new client: %s", client.id);
});

var server = http.createServer(
    dispatch({
        '/client': serveClient,
        '.*': serve404
    })
);

function serveClient(req, res) {
    fs.readFile('client.html', 'utf8', function (err, html) {
        if (err) {
            serve500(req, res);
            console.log("%s", err);
        }
        else {
            var output = Plates.bind(html, secrets);
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(output);
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
        id: shortid.generate(),
        sendHello: sendHello,
        sendUpdate: sendUpdate,
    }

    function ackHandler(error) {
        if (error) {
            console.log(error);
        }
    }

    function sendHello() {
        var raw = JSON.stringify({id:fields.id, type:0});
        socket.send(raw, ackHandler);
    }

    function sendUpdate(content) {
        var raw = JSON.stringify({id:fields.id, type:1, content:content});
        socket.send(raw, ackHandler);
    }

    socket.on('message', function incoming(message) {
        console.log("client say: %s : %s", fields.id, message);
    });

    return fields;
}

function Clients() {
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

server.listen(8081);
console.log("#GoNamTaxi2015 Prototype Server");

function broadcast() {
    clients.tick();
}

broadcastTimer = setInterval(broadcast, 500);

var r = "https://roads.googleapis.com/v1/snapToRoads?key="+secrets.google_maps_server_key;
r += "&path=-22.571816,17.080843|-22.571103,17.08359";
r += "&interpolate=true";
var testRequest = request.get(r, function(error, response, body) {
    if (!error && response.statusCode == 200) {
        console.log(body); 
    }
    else {
        console.log(error, response);
    }
});
