var Util = require('./util.js');
var Tickets = Util.tryRequire("../tickets/table");

module.exports = {
    TransactionManager: TransactionManager
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
