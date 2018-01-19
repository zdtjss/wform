<%@ page  contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title>创建表单字段</title>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<style type="text/css">
th {
	font-weight: normal;
	text-align: right;
	padding-right: 5px;
}

.fieldAttr {
	height: 22px;
	line-height: 22px;
}

.easyui-linkbutton {
	padding: 3px 10px;
	margin-top: 5px;
}
</style>
<script type="text/javascript" src="${contextPath }/js/components.js"></script>
</head>
<body>
	<form id="fieldForm">
		<table id="fields">
			<tr>
				<th width="8%">名字</th>
				<td width="17%"><input name="name" width="100%" value="PK_ID" class="textbox fieldAttr"></td>
				<th width="8%">中文名字</th>
				<td width="17%"><input name="display" width="100%" value="主键" readonly="readonly" class="textbox fieldAttr"></td>
				<th width="8%">组件</th>
				<td width="17%"><input width="100%" value="主键" readonly="readonly" class="easyui-combobox"><input name="type" type="hidden" width="100%" value="key" class="fieldAttr"></td>
				<th width="8%">字段长度</th>
				<td width="17%"><input name="size" width="100%" value="36" readonly="readonly" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" clss="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<th>名字</th>
				<td><input name="name" class="textbox fieldAttr"></td>
				<th>中文名字</th>
				<td><input name="display" class="textbox fieldAttr"></td>
				<th>组件</th>
				<td><input name="type" class="fieldAttr"></td>
				<th>字段长度</th>
				<td><input name="size" class="textbox fieldAttr"></td>
			</tr>
			<tr>
				<td colspan="8" align="right">
					<a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton">保存</a>
				</td>
			</tr>
		</table>
	</form>
	<script type="text/javascript">
	
	$.when($.ready).then(function() {
		
		$("[name='type']").each(function(index) {
			if(this.type != 'hidden') {
				$(this).combobox({
				    data:components,
				    valueField:'name',
				    textField:'desc'
				});
			}
		});
	});
	
	function submit() {
		
		var fields = [];
		
		$("#fields tr").each(function(index) {
			
			var field = {
		        name : $(this).find("[name='name']").val(),
		        display : $(this).find("[name='display']").val(),
		        type : $(this).find("[name='type']").val(),
		        size : $(this).find("[name='size']").val()
			};
			
			if(field.name && field.display && field.type) {
				
				field['pageId'] = "${pageId}";
				
				fields[index] = field;
			}
		});
		
		$.ajax({
			type : "post",
			dataType : "json",
			contentType:"application/json",
			data : JSON.stringify(fields),
			url : contextPath + "/formDesign/saveFields",
			success : function(resp) {
				
			},
			error : function() {
					
			}
		});
	}
	</script>
</body>
</html>