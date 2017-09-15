const path = require('path');
const HtmlPlugin    = require('html-webpack-plugin');
const offset = '../back-end/src/main/resources/static';
module.exports = {
    entry: './index.js',
    output: {
        path: path.resolve(__dirname, offset),
        filename: "bundle.js"
    },
    module: {
        loaders: [
            {test: /\.css$/, loader: "style-loader!css-loader"},
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    plugins: [
        new HtmlPlugin({
            title: 'Test APP',
            filename: 'index.html',
            inject: 'body'
        })
    ]
};