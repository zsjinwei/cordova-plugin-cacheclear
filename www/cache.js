var exec = require('cordova/exec');

var cache = {
  getCacheSize: function(success, error) {
    exec(success, error, 'Cache', 'getCacheSize', []);
  },
  clearCache: function(success, error) {
    exec(success, error, 'Cache', 'clearCache', []);
  }
};

module.exports = cache;
