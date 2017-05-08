$("#add_permission").modal('toggle');
$("#permission_message").addClass("alert alert-success fade in flash-setting roles-flash");
$("#permission_message").html("<a href='#' class='close' data-dismiss='alert'>&times;</a> <strong class='text-center'><%=@msg%></strong> ");
$("#permission_table > tbody").append("<tr><td><%=@permissions_count%></td><td><%=@permission.name%></td><td><%=@permission.status%></td><td><%=@permission.created_at.to_formatted_s(:short)%></td><td><%=@permission.updated_at.to_formatted_s(:short)%></td><td><%=j (link_to "<i class='fa fa-times text-danger' aria-hidden='true'></i>".html_safe,@permission,method: :DELETE,data: { confirm: 'Are you sure?' } )%></td></tr>")
window.setTimeout(function() { // hide alert message
        $(".alert").fadeOut('3000');
}, 3000);
