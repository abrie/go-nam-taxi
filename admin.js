var ShortId = require('shortid');
var Util = require('./util.js');
var Secrets = Util.tryRequire('./private/api_keys');

module.exports = {
    ClientManager:ClientManager,
    Client:Client
} 

function ClientManager() {
    var clients = [];
    var updateCounter = 0;

    function add(client) {
        clients.push(client);
        return client.init();
    }

    function remove(client) {
        var index = clients.indexOf(client);
        clients.splice(index, 1);
        client.dispose();
    }

    function broadcast(message) {
        clients.forEach( function(client) {
            client.sendMessage(message);
        });
    }

    return {
        add: add,
        remove: remove,
        broadcast: broadcast
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
        sendMessage: sendMessage,
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

    function sendMessage(message) {
        var raw = JSON.stringify({
            type: 1, 
            clientId: state.clientId, 
            content: message
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
        return this;
    }

    function dispose() {
        console.log("bye client: %s", state.clientId);
    }

    return methods;
}
