$(function () {
	$("#new_user").validate({
		rules: {
			"user[team_ids]": {required: true},
			"user[name]": {required: true},
			"user[email]": {required: true, email: true},
			"user[password]": {required: true, minlength: 6},
		},
		framework: 'bootstrap',
		icon: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		errorClass:'text-danger'
	});
	$("#new_scaner").validate({
		rules: {                
			"scaner[project_title]": {required: true},
			"scaner[source]": {required: true},
			"scaner[project_target]": {required: true},
			"scaner[team_id]": {required: true},
			"scaner[repo_id]": {required: true},
			"scaner[target]": {required: true,validIpUrl: true},
			"scaners[project_target]": {required: true}
		},      
		framework: 'bootstrap',
		icon: {                 
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},                              
		errorClass:'text-danger'
	})
	$("#corporate_scan_form").validate({             
		rules: {                        
			"scaners[team_id][]": {required: true},
			"scaners[repo_id][]": {required: true},
		},                      
		framework: 'bootstrap', 
		icon: {                 
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},                              
		errorClass:'text-danger'        
	});
	 $("#upload_scan_form").validate({          
                rules: {                        
                        "scaners[team_id]": {required: true},
                        "scaners[repo_id]": {required: true}, 
			"scaners[scan_type]": {required: true},
			"scaners[project_target]": {required: true}
                },                                              
                framework: 'bootstrap',                 
                icon: {                         
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                },                                              
                errorClass:'text-danger'        
        });    
});
jQuery.validator.addMethod('validIpUrl', function(value) {
	var is_valid_url = validate_domain(value);
	var message = "";
	if(is_valid_url)
	{
		return is_valid_url;
	}
	else
	{
		message = "Invalid URL";
		var is_valid_ip = validate_ip(value);
		if(!is_valid_ip)
		{
			message = "Invalid IP";
			return false;
		}
		return true;
	}

}, 'Invalid Domain/IP');
function validate_domain(value)
{
	var urlregex = new RegExp("^(http:\/\/|https:\/\/|ftp:\/\/|www.){1}([0-9A-Za-z]+\.)");
	return urlregex.test(value);
}

function validate_ip(value)
{
	var split = value.split('.');
	if (split.length != 4)
		return false;

	for (var i=0; i<split.length; i++) {
		var s = split[i];
		if (s.length==0 || isNaN(s) || s<0 || s>255)
			return false;
	}
	return true;
}
