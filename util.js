module.exports = {
    tryRequire: tryRequire
}

function tryRequire(path) {
    try {
        return require(path);
    }
    catch (e) {
        console.log("FATAL: error while trying to require %s", path);
        console.log(e);
        process.exit(1);
    }
}

