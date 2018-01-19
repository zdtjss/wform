<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title>页面设计</title>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
</head>
<body>
	<div>
		<div id="fields" style="width: 20%;float: left;text-align: center;">
			<ul style="list-style-type: none;">
				<c:forEach var="field" items="${fields }">
					<li id="${field.name }">${field.display }</li>
				</c:forEach>
			</ul>
		</div>
		<div id="pageFields" style="float: right;width: 79%">
			<table width="100%" border="1">
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr height="30px">
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%"></td>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
	        $('#fields li').draggable({
	            revert:true,
	            proxy:'clone'
	        });
	        $('#pageFields td').droppable({
	            accept: 'li',
	            onDragEnter:function(){
	                $(this).addClass('over');
	            },
	            onDragLeave:function(){
	                $(this).removeClass('over');
	            },
	            onDrop:function(e,source){
	                $(this).removeClass('over');
	                if ($(source).hasClass('assigned')){
	                    $(this).append(source);
	                } else {
	                    var c = $(source).clone().addClass('assigned');
	                    $(this).empty().append(c);
	                    c.draggable({
	                        revert:true
	                    });
	                }
	            }
	        });
	        $('#fields').droppable({
	            accept:'.assigned',
	            onDragEnter:function(e,source){
	                $(source).addClass('trash');
	            },
	            onDragLeave:function(e,source){
	                $(source).removeClass('trash');
	            },
	            onDrop:function(e,source){
	                $(source).remove();
	            }
	        });
	    });
	</script>
</body>
</html>