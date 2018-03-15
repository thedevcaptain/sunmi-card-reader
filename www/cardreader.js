
var exec = require('cordova/exec');

var PLUGIN_NAME = 'SunmiCardReader';
var SunmiCardReader = {
	startReadCard: function(cardTypes) {
		return new Promise(function(resolve,reject){
			exec(resolve, reject, PLUGIN_NAME, 'startReadCard', cardTypes);
		});
  },
  authMifare: function(blockKey,keyType,blockToRead) {
		return new Promise(function(resolve,reject){
			exec(resolve, reject, PLUGIN_NAME, 'authMifare', [blockKey,keyType,blockToRead]);
	});
  },
  readBlockMifare: function(blockToRead) {
		return new Promise(function(resolve,reject){
			exec(resolve, reject, PLUGIN_NAME, 'readBlockMifare', [blockToRead]);
	});
  },
  writeBlockMifare: function(blockToWrite,stringToWrite) {
		return new Promise(function(resolve,reject){
			exec(resolve, reject, PLUGIN_NAME, 'writeBlockMifare', [blockToWrite,stringToWrite]);
	});
  },
  setPinStatus: function(pinToEdit,statusResolved) {
		return new Promise(function(resolve,reject){
			exec(resolve, reject, PLUGIN_NAME, 'setPinStatus', [pinToEdit,statusResolved]);
	});
  },
  cancelCheckCard: function() {
		return new Promise(function(resolve,reject){
			exec(resolve, reject, PLUGIN_NAME, 'cancelCheckCard', []);
	});
  }
};

module.exports = SunmiCardReader;
