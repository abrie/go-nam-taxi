var secrets = try_require('./private/api_keys');
var fs = require('fs');
var http = require('http');
var dispatch = require('dispatch');
var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({ port:8080 });

wss.on('connection', function connection(ws) {
    console.log("connection detected.");
    ws.on('message', function incoming(message) {
        console.log(message);
    });

    ws.send('a response', function ack(error) {
        if (error) {
            console.log(error);
        }
    });
});

var server = http.createServer(
    dispatch({
        '/client': serveClientPage,
        '.*': serve404
    })
);

function serveClientPage(req, res) {
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

function try_require(path) {
    try {
        return require(path);
    }
    catch (e) {
        console.log("FATAL: required path not found: %s", path);
        process.exit(1);
    }
}

server.listen(8081);
console.log("#GoNamTaxi2015 Prototype Server");
console.log(secrets);
