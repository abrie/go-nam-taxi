module.exports = {
    HandlerTable:HandlerTable
}

function HandlerTable() {
    var handlers = {};

    function addRoute(route) {
        route.paths.forEach( function(path) {
            handlers[path] = route.handler;
        })
    }

    return {
        handlers:handlers,
        add:addRoute,
    }
}
