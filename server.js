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
var TicketTracker = require('./tickettracker.js');

var socketServer = new WebSocketServer({ port:9090 });
var clientManager = new Admin.ClientManager();
var transactionManager = new TicketTracker.TransactionManager();

socketServer.on('connection', function(socket) {
    var client = clientManager.addSocket(socket);

    socket.on('close', function() { 
        clientManager.removeSocket(socket); 
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
