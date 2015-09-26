var secrets = tryRequire('./private/api_keys');
var shortid = require('shortid');
var fs = require('fs');
var http = require('http');
var dispatch = require('dispatch');
var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({ port:8080 });

wss.on('connection', function onNewConnection(socket) {
    addClient(socket); 
});

var server = http.createServer(
    dispatch({
        '/client': serveClient,
        '.*': serve404
    })
);

function serveClient(req, res) {
    fs.readFile('client.html', 'utf8', function (err, data) {
        if (err) {
            return console.log(err);
        }
        else {
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(data);
            res.end();
        }
    });
}

function serve404(req, res) {
    res.writeHead(404, {'Content-Type': 'text/html'});
    res.write("that does not exist.");
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

var client_to_id = {};
var id_to_client = {};

function Client(socket) {
    socket.on('message', function incoming(message) {
        console.log(message);
    });

    socket.on('close', function close() {
            removeClient(socket);
    });

    socket.send('hello new client', function ack(error) {
        if (error) {
            console.log(error);
        }
    });

    return {
        socket:socket,
        id: shortid.generate()
    }
}

function addClient(ws) {
    var client = new Client(ws);
    client_to_id[client] = client.id;
    id_to_client[client.id] = client;
    console.log("New client connected: %s", client.id);
}

function removeClient(ws) {
    id = client_to_id[ws];
    delete client_to_id[ws];
    delete id_to_client[id];
    console.log("Client disconnected: %s", id);
}

server.listen(8081);
console.log("#GoNamTaxi2015 Prototype Server");
console.log(secrets);
