# Sunmi Card Reader Cordova Plugin

This is a wrapper plugin that allows you to read different cards with a Sunmi device through a Cordova App

## Installation

    cordova plugin add https://github.com/PirataFrancis/sunmi-card-reader.git

## Functions covered

### startReadCard(cardsTypeToRead) :open_hands:
Start reading cards info
#### Parameters
- cardsTypeToRead: Array of Integers (one Int for each card type, see Appendix 1)
```javascript
window.SunmiCardReader.startReadCard([8])
.then(function(payload){
	console.log(payload);
}).catch(function (error) {
	console.log(error);
});
```
### authMifare(cardBlockPassword, cardKey, blockNumber) :open_hands:
Authenticate a Mifare Cards
**Should be called after startReadCard**
#### Parameters
- cardBlockPassword: String 
- cardKey: Integer (0 for Key A || 1 for Key B)
- blockNumber: Integer
```javascript
window.SunmiCardReader.authMifare("ffffffffffff",0,5)
.then(function(payload){
	console.log(payload);
}).catch(function (error) {
	console.log(error);
});
```
### readBlockMifare(blockToRead) :open_hands:
Read a card block 
**Should be called after AuthMifare**
#### Parameters
- blockToRead: Integer
```javascript
window.SunmiCardReader.readBlockMifare(5)
.then(function(payload){
	console.log(payload);
}).catch(function (error) {
	console.log(error);
});
```
### writeBlockMifare(blockToWrite,stringToWrite) :open_hands:
Write Hex into a card block 
**Should be called after AuthMifare**
#### Parameters
- blockToRead: Integer
- stringToWrite: String
```javascript
window.SunmiCardReader.writeBlockMifare(5,"12111100000000000000000000000000")
.then(function(payload){
	console.log(payload);
}).catch(function (error) {
	console.log(error);
});
```


## Release History
- 0.0.1
	- Work in progress, pushed first 4 functions
     
## About me
I'm Francesco Borrelli an Italian Android Dev, I love coding, software engineering and pizza so feel free contact me about anything :facepunch:
[Email](mailto:borrellifrm@gmail.com)
[Facebook](https://www.facebook.com/PirataFrancis)

Distributed under the `MIT` license. See `LICENSE` for more information.

## Contributing
1. Fork it (https://github.com/PirataFrancis/sunmi-card-reader/fork)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
