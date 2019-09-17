
'use strict';
// Template version: 1.1.3
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path');

module.exports = {
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: './',
    productionSourceMap: false,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: require('./dev.env'),
    port: process.env.PORT || 8082,
    autoOpenBrowser: true,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      // '/xgov/gov/login': {
      //   // target: 'http://10.221.121.4:9027/',
      //   target: 'http://10.221.121.4:9027/',
      //   changeOrigin: true,
      //   pathRewrite: {
      //     '^/xgov/gov/login': '/login'
      //   }
      // },
      '/xgov': {
        // target: 'http://10.221.121.4:8080', //'http://172.22.48.169:8080/', //'http://172.31.100.195:8080',
        //本机：http://localhost:8080/   172.16.1.180:8086
        //测试：'http://10.221.121.2:8080'
        //  http://172.16.1.211:8080  http://10.221.101.127:8080  代颖超
        //  http://172.16.1.212:8080 http://10.221.101.104:8080    李阳
        // target: 'http://10.221.121.4:12345',
        target: 'http://172.32.1.25:8080',
        // target: 'http://10.221.121.3:8087',
        // target: 'http://10.221.121.3:9768',
        // target: 'http://10.221.121.6:5737',
        changeOrigin: true,
        pathRewrite: {
          '^/xgov': '/'
        }
      },
      '/auth/api': {
          target: 'http://10.221.121.4:9027/',
          changeOrigin: true,
          pathRewrite: {
              '^/auth/api': '/'
          }
      },
      '/metadata/api': {
          target: 'http://10.221.121.5:8803',
          changeOrigin: true,
          pathRewrite: {
              '^/metadata/api': '/'
          }
      },
    },
    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false
  }
};
