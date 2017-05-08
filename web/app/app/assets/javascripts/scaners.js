function enable_file_field()
{
	var source = $('#scaner_source').val();
	if(source == 'local')
	{
		$("#branch_field").addClass("hide");
		$(".file-field").attr("required","required");
		$(".repo_field").removeAttr("required");
		$("#repo_field").addClass("hide");
		$("#directory_field").removeClass("hide");
		$(".periodic_schedule_field").addClass("hide");
	}
	else
	{
		$("#branch_field").removeClass("hide");
		$(".file-field").removeAttr("required");
		$(".repo_field").attr("required","required");
		$("#directory_field").addClass("hide");
		$("#repo_field").removeClass("hide");
		$(".periodic_schedule_field").removeClass("hide");
	}
}
var fileUploadErrors = {
	maxFileSize: 'File is too big',
	minFileSize: 'File is too small',
	acceptFileTypes: 'Filetype not allowed',
	maxNumberOfFiles: 'Max number of files exceeded',
	uploadedBytes: 'Uploaded bytes exceed file size',
	emptyResult: 'Empty file upload result'
};
function load_projects(){
	var group_id = $("#scaners_team_id").val();
	$("#scaners_repo_id").empty();
	if(group_id != "")
	{
		$.ajax({
			url: "/scaners/load_projects",
			type: "GET",
			data: 'team_id='+ group_id
		}).done(function (data) {
			change_projects(data);
		});
	}

}

function change_projects(data)
{
	for(var projects=0;projects<data.length;projects++)
	{
		$("#scaners_repo_id").append($("<option />").val(data[projects].id).text(data[projects].name));
	}
	$('#scaners_repo_id').selectpicker('refresh'); //multiselect('rebuild');
}

function load_branches(){       
	var repo_id = $("#scaner_repo_id").val();
	$("#scaner_branch_id").empty();
	if(repo_id != "")
	{
		$.ajax({                
			url: "load_branches",   
			type: "GET",                 
			data: 'repo_id='+ repo_id      
		}).done(function (data) {               
			change_branches(data);
		});
	}	

}                                       

function change_branches(data)                          
{                                                       

	for(var branches=0;branches<data.length;branches++)
	{                               
		$("#scaner_branch_id").append($("<option />").val(data[branches].id).text(data[branches].name));
	}                                               
	$('#scaner_branch_id').selectpicker('refresh');
}
function start_scan_all_repos()
{
	$.ajax({        
		url: "/scaners/start_scan_for_all_repo",
		type: "GET" 
	})
}
function start_truffle_scan()
{
	$.ajax({
		url: "/scaners/run_truffle",
		type: "GET"
	});
}
function bulk_mark_false_positive()
{
	var collect_ids = [];
	$(":checkbox").each(function(){
		if (this.checked) {
			collect_ids.push($(this).attr("id").split("find_")[1]);
		}
	});
	if(collect_ids.length > 0)
	{
		$("#marked_finding").val(collect_ids)
			return true;
	}
	else
	{
		alert("No finding selected!");
		return false;
	}
}

function updateDataTableSelectAllCtrl(table){
	var $table             = table.table().node();
	var $chkbox_all        = $('tbody input[type="checkbox"]', $table);
	var $chkbox_checked    = $('tbody input[type="checkbox"]:checked', $table);
	var chkbox_select_all  = $('thead input[name="select_all"]', $table).get(0);

	// If none of the checkboxes are checked
	if($chkbox_checked.length === 0){
		chkbox_select_all.checked = false;
		if('indeterminate' in chkbox_select_all){
			chkbox_select_all.indeterminate = false;
		}

		// If all of the checkboxes are checked
	} else if ($chkbox_checked.length === $chkbox_all.length){
		chkbox_select_all.checked = true;
		if('indeterminate' in chkbox_select_all){
			chkbox_select_all.indeterminate = false;
		}

		// If some of the checkboxes are checked
	} else {
		chkbox_select_all.checked = true;
		if('indeterminate' in chkbox_select_all){
			chkbox_select_all.indeterminate = true;
		}
	}
}
function add_hidden_values(rows_selected)
{
	if(rows_selected.length == 0)
	{       
		alert("No Findings are selected");
		return false;
	}
	$(".spinner_section").spin({
		lines: 12, // The number of lines to draw
		length: 7, // The length of each line
		width: 9, // The line thickness
		radius: 30, // The radius of the inner circle
		color: '#000', // #rgb or #rrggbb
		speed: 1, // Rounds per second
		trail: 60, // Afterglow percentage
		shadow: true // Whether to render a shadow
	});
	$("#marked_findings").val(rows_selected.join(","));
	$("#check_all").prop('checked', false);
}
