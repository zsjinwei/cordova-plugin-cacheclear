var exec = require('cordova/exec');

var cacheClear = {
  getCacheSize: function(success, error) {
    exec(success, error, 'cacheclear', 'getCacheSize', []);
  },
  clearCache: function(success, error) {
    exec(success, error, 'cacheclear', 'clearCache', []);
  }
};

module.exports = cacheClear;
