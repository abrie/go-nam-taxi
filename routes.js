module.exports = {
    HandlerTable:HandlerTable
}

function HandlerTable() {
    var handlers = {};

    function addRoute(route) {
        handlers[route.path] = route.handler;
    }

    return {
        handlers:handlers,
        add:addRoute,
    }
}
