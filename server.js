var secrets = tryRequire('./private/api_keys');
var shortid = require('shortid');
var fs = require('fs');
var http = require('http');
var dispatch = require('dispatch');
var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({ port:8080 });

wss.on('connection', function connection(ws) {
    addClient(ws);

    ws.on('message', function incoming(message) {
        console.log(message);
    });

    ws.on('close', function close() {
        removeClient(ws);
    });

    ws.send('a response', function ack(error) {
        if (error) {
            console.log(error);
        }
    });
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

function addClient(ws) {
    id = shortid.generate(); 
    client_to_id[ws] = id;
    id_to_client[id] = ws;
    console.log("New client connected: %s", id);
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
