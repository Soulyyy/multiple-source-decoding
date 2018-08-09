var path = require('path');
var packageJSON = require('./package.json');
var webpack = require('webpack');


const PATHS = {
  build: path.join(__dirname, 'build', 'webpack', 'webjars')
};

module.exports = {
  mode: 'development',
  entry: './src/js/app.js',
  output: {
    //path: path.resolve(__dirname, 'build/js'),
    path: PATHS.build,
    filename: 'bundle.js'
  }
};