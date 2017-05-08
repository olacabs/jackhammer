//# Place all the behaviors and hooks related to the matching controller here.
//# All this logic will automatically be available in application.js.
//# You can use CoffeeScript in this file: http://coffeescript.org/

function change_status(finding_id)
{
	var status_type = $("#status_type").val();
	if(status_type != '')
	{
		if(status_type == 'Close')
		{
			if(!confirm("Do you want close this vulnerability?"))

			{
				return false;
			}
		}
		$.ajax({
			url: "change_status",
			type: "POST",
			data: 'finding_id='+ finding_id +'&status='+ status_type
		});
	}
}

function change_positive_type(finding_id)
{
	$.ajax({
		url: "change_false_positive",
	type: "POST",
	data: 'finding_id='+ finding_id
	}); 

}
function change_to_not_exploitable(finding_id)
{               
        $.ajax({
	                url: "change_to_not_exploitable",
	        type: "POST",
	        data: 'finding_id='+ finding_id
	        });             
                
}    
function publish_to_jira(finding_id)
{
	 $("#spinner_section").spin({
                lines: 12, // The number of lines to draw
                length: 7, // The length of each line
                width: 9, // The line thickness
                radius: 30, // The radius of the inner circle
                color: '#000', // #rgb or #rrggbb
                speed: 1, // Rounds per second
                trail: 60, // Afterglow percentage
                shadow: true // Whether to render a shadow
        });

	$.ajax({
		url: "publish_to_jira",                   
		type: "POST",                                           
		data: 'finding_id='+ finding_id                 
	});  
}
function open_message_box(event)
{
	event.preventDefault();
	$(this).addClass('hide');
	$('.glyphicon-comment').parent().addClass('hide');
	$("#new_comment").removeClass('hide'); 
}
function edit_message_box(event,id)
{
	event.preventDefault();
	$('.glyphicon-comment').parent().addClass('hide');
	$("#edit_comment").removeClass('hide');
	$("#edit_comment_id").val(id);
	$("#new_comment").addClass("hide")
}
function close_message_box(event)
{
	event.preventDefault(); 
	$("#new_comment,#edit_comment").addClass('hide');
	$('.glyphicon-comment').parent().removeClass('hide');
}

function open_new_tag_modal()
{
	$('#newTagModal').modal('show');
}

function open_add_tag_modal()
{
	$('#addTagModal').modal('show');
//	$('#tag_id').multiselect({
//		includeSelectAllOption: true,
//		enableFiltering: true
//	});

}

function open_comment_modal()
{
	$('#commentModal').modal('show');
}

function open_upload_modal()
{
	$('#uploadModal').modal('show');
}
