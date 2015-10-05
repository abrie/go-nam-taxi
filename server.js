#!/usr/bin/env node
// Abrie loved this project.

var Dispatch = require('dispatch');
var Fs = require('fs');
var Http = require('http');
var Request = require('request');
var WebSocketServer = require('ws').Server;
var Dns = require('dns');
var Os = require('os');

var Util = require('./util');
var Admin = require('./admin');
var Secrets = Util.tryRequire('./private/api_keys');
var Tickets = Util.tryRequire("./tickets/table");

var socketServer = new WebSocketServer({ port:9090 });
var clientManager = new Admin.ClientManager();
var transactionManager = new TransactionManager();

socketServer.on('connection', function(socket) {
    var client = clientManager.add(new Admin.Client(socket));

    socket.on('close', function() { 
        clientManager.remove(client); 
    });
});

var server = Http.createServer(
    Dispatch({
        '/till/received/cash/:id/:lon/:lat': processCashPayment,
        '/till/received/coupon/:id/:code/:lon/:lat': processCouponPayment,
        '/client': serveClient,
        '.*': serve404
    })
);

function processCashPayment(req, res, taxi_id, lon, lat) {
    console.log("till %s received cash.", taxi_id);
    var json = JSON.stringify({
        "content":"cash payment acknowledged",
        "taxi_id":taxi_id,
        "cash_payment":"cash",
        "time": Date.now(),
        "longitude": lon,
        "latitude": lat,
        "is_valid":"---",
    });

    res.writeHead(200, {'Content-Type': 'text/html'});
    res.write(json);
    res.end();

    clientManager.broadcast(json)
}

function processCouponPayment(req, res, taxi_id, code, lon, lat) {
    var lastScan = transactionManager.transact(code);
    console.log("till %s received coupon: %s : %s", taxi_id, code, lastScan);
    console.log(lat, lon);

    var json = JSON.stringify({
        "content":"coupon payment acknowledged",
        "taxi_id":taxi_id,
        "ticket_payment":"ticket id-"+code,
        "time": transactionManager.transactionTimestamp(code),
        "age": transactionManager.age(code),
        "longitude": lon,
        "latitude": lat,
        "is_valid": lastScan === 0,
    });

    res.writeHead(200, {'Content-Type': 'text/html'});
    res.write(json);
    res.end();

    clientManager.broadcast(json)
}

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

function TransactionManager() {
    function transact(code) {
        var lastScan = Tickets[code];
        if (lastScan === 0) {
            Tickets[code] = Date.now();
            return 0;
        }
        return lastScan;
    }

    function age(code) {
        if (Tickets[code] !== 0) {
            return Date.now() - Tickets[code];
        }
        else {
            return 0;
        }
    }

    function transactionTimestamp(code) {
        return Tickets[code];
    }

    return {
        transact: transact,
        transactionTimestamp: transactionTimestamp,
        age: age
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

function showIpAddress(server) {
    Dns.lookup(Os.hostname(), function (err, add, fam) {
        if (err) {
            console.log("Error attempting to discover ip:", err);
        }
        if (add) {
            console.log("Serving on localhost, aka: %s:%s", add, server.address().port);
        }
    });
}

console.log("#GoNamTaxi Prototype Server");
server.listen(8080);
showIpAddress(server);
