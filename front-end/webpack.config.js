const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
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
            },
            {
                test: /\.(jpe?g|png|svg|gif|woff|woff2|eot|ttf)$/i,
                loader: 'file-loader',
                options: {
                    name: '[name].[ext]',
                    outputPath: 'fonts/'
                }
            }
        ],


    },
    plugins: [
        new HtmlWebpackPlugin({
            template: '!!ejs-loader!index-template.ejs'
        })
    ],
    node: {
        net: 'empty'
    }
};