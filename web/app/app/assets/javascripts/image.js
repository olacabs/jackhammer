$(document).on("page:fetch", function(){
	$(".spinner").show();
});

$(document).on("page:receive", function(){
	$(".spinner").hide();
});
