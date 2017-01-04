const webpack = require('atool-build/lib/webpack');
const fs = require('fs');
const path = require('path');
const glob = require('glob');

module.exports = function(webpackConfig, env) {
  webpackConfig.babel.babelrc = false;
  webpackConfig.babel.plugins.push('transform-runtime');
  webpackConfig.babel.plugins.push(['import', {
    libraryName: 'antd',
    style: 'css'  // if true, use less
  }]);

  // Support hmr
  if (env === 'development') {
    webpackConfig.devtool = '#eval';
    webpackConfig.babel.plugins.push('dva-hmr');
  } else {
    webpackConfig.babel.plugins.push('dev-expression');
  }

  // Don't extract common.js and common.css
  webpackConfig.plugins = webpackConfig.plugins.filter(function(plugin) {
    return !(plugin instanceof webpack.optimize.CommonsChunkPlugin);
  });

  // Support CSS Modules
  // Parse all less files as css module.
  webpackConfig.module.loaders.forEach(function(loader, index) {
  if (typeof loader.test === 'function' && loader.test.toString().indexOf('\\.less$') > -1) {
       loader.include = [/node_modules/, /antMotion_style\.less/];
       loader.test = /\.less$/;
     }
     if (loader.test.toString() === '/\\.module\\.less$/') {
       loader.exclude = [/node_modules/, /antMotion_style\.less/];
       loader.test = /\.less$/;
     }
     if (typeof loader.test === 'function' && loader.test.toString().indexOf('\\.css$') > -1) {
       loader.include = /node_modules/;
       loader.test = /\.css$/;
     }
     if (loader.test.toString() === '/\\.module\\.css$/') {
       loader.exclude = /node_modules/;
       loader.test = /\.css$/;
     }

  });

  return webpackConfig;
};
