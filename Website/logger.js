
const EventEmmiter = require('events');


var url = 'hhtp://mylogger.io/log';

class Logger extends EventEmmiter{
	log(message){
	//send an HTTP request
	console.log(message);

	//Raise an event
	this.emit('WelcomeToNodeJs', {id:1, url:'http://'});
	}
}



module.exports = Logger;

