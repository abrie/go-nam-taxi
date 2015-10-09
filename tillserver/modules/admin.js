var ShortId = require('shortid');
var Util = require('./util.js');
var Secrets = Util.tryRequire('../private/api_keys');

module.exports = {
    ClientManager:ClientManager,
} 

function ClientManager() {
    var clients = [];
    var updateCounter = 0;

    function addSocket(socket) {
        var client = new Client(socket);
        clients.push(client);
        return client.init();
    }

    function removeSocket(socket) {
        var clients_on_socket = clients.filter(function(client) {
            return client.getSocket() === socket;
        });

        clients_on_socket.forEach( function(client) {
            var index = clients.indexOf(client);
            clients.splice(index, 1);
            client.dispose();
        });
    }

    function broadcast(message) {
        clients.forEach( function(client) {
            client.sendMessage(message);
        });
    }

    return {
        addSocket: addSocket,
        removeSocket: removeSocket,
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
        getSocket: function() { return state.socket; },
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
