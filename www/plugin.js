
var exec = require('cordova/exec');

var PLUGIN_NAME = 'SunmiCardReader';

var SunmiCardReader = {
  echo: function(minutes, cb) {
    exec(cb, null, PLUGIN_NAME, 'echo', [minutes]);
  },
  paffo: function(minutes, cb) {
    exec(cb, null, PLUGIN_NAME, 'echo', [minutes]);
  }
};

module.exports = SunmiCardReader;
