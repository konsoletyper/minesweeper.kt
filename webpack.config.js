var path = require('path');
var webpack = require('webpack');
var root = path.resolve('./out/production/');

module.exports = {
    entry: "./out/production/webui/webui.js",
    output: {
        path: __dirname + "/out/production",
        filename: "bundle.js"
    },
    resolve: {
        root: root,
        alias: {
            webui: root + "/webui/webui.js",
            game: root + "/game/game.js",
            commons: root + "/commons/commons.js",
            kotlin: root + "/lib/kotlin.js",
            builtins: root + "/lib/builtins.js",
            stdlib: root + "/lib/stdlib.js"
        }
    },
    plugins: [
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false
            }
        })
    ]
};