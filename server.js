var WebSocketServer = require('ws').Server
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
