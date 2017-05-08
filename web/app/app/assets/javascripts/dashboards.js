$(document).ready(function(){
	datetime = $('.time h1');
	datetime2 = $('.time p');
	update();
	setInterval(update, 1000);
});
var update = function () {
	date = moment(new Date())
		datetime.html(date.format('HH:mm'));
	datetime2.html(date.format('dddd, MMMM Do YYYY'));
};
