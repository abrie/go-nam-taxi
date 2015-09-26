var secrets = tryRequire('./private/api_keys');
var shortid = require('shortid');
var fs = require('fs');
var http = require('http');
var dispatch = require('dispatch');
var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({ port:8080 });
var clients = new Clients();

wss.on('connection', function onNewConnection(socket) {
    var client = new Client(socket);
    clients.add(client); 
    socket.on('close', function close() { 
        clients.remove(client); 
        console.log("bye client: %s", client.id);
    });

    console.log("new client: %s", client.id);
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

function Client(socket) {
    socket.on('message', function incoming(message) {
        console.log(message);
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

function Clients() {
    var client_to_id = {};
    var id_to_client = {};

    function add(client) {
        client_to_id[client] = client.id;
        id_to_client[client.id] = client;
    }

    function remove(client) {
        delete client_to_id[client];
        delete id_to_client[client.id];
    }

    return {
        add:add,
        remove:remove
    }
}

server.listen(8081);
console.log("#GoNamTaxi2015 Prototype Server");
console.log(secrets);
