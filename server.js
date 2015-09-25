var fs = require('fs');
var http = require('http');
var dispatch = require('dispatch');
var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({ port:8080 });

wss.on('connection', function connection(ws) {
    console.log("connection detected");
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
    dispatch({ '/client': serveClientPage })
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

server.listen(8081);
console.log("#GoNamTaxi2015 Prototype Server");
