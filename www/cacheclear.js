var exec = require('cordova/exec');

var cacheClear = {
  getCacheSize: function(success, error) {
    exec(success, error, 'Cache', 'getCacheSize', []);
  },
  clearCache: function(success, error) {
    exec(success, error, 'Cache', 'clearCache', []);
  }
};

module.exports = cacheClear;
