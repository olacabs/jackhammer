//$(document).ready(function(){
//	$(".nav li a").on("click", function(){
//		$(".nav").find(".active").removeClass("active");
//		$(this).parent().addClass("active");
//	});
//});
window.setTimeout(function() { // hide alert message
	$(".alert").fadeOut('3000'); 
}, 3000);
$(document).ready(function(){
    $('[data-toggle="popover"]').popover();   
});
