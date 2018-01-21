<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title>页面设计</title>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<style type="text/css">
th {
	font-weight: normal;
	text-align: right;
}
tr {
	height: 30px;
}
.textbox {
	height: 22px;
	line-height: 22px;
}

.component {
	border-width: 0px 1px 1px 1px;
	border-style: solid;
	padding: 5px 3px;
}

.component span {
	padding: 5px 3px;
}

table {
	border-collapse: collapse;
}

table, th, td {
	border: 1px solid black;
}
.row_selected {
}
.column_selected {
}
.cell_selected {
	background-color: aqua;
}
.hiddenField {
	margin-top: 10px;
    min-height: 100px;
    border: 1px solid;
}
.hiddenField span {
	margin: 3px 5px;
}
</style>
<script type="text/javascript">
	var pageId = "${pageId}";
</script>
<script type="text/javascript" src="${contextPath }/js/components.js"></script>
</head>
<body>
	<div>
		<div id="components" style="width: 19%;float: left;border-top: 1px solid;margin-right: 3px;">
		</div>
		<div id="pageFields" style="float: left;width: 59%">
			<table id="canvas" width="100%" border="1">
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
			</table>
			<div class="hiddenField" width="100%" style="margin-top: 10px;">
			</div>
		</div>
		<div id="fieldAttrs" style="width: 20%;float: left;margin-left: 3px;">
			<div>基础属性框</div>
			<div>
				<table class="baseAttrs">
					<tr>
						<th>名字：</th>
						<td><input name="name" class="textbox"></td>
					</tr>
					<tr>
						<th>中文名字：</th>
						<td><input name="display" class="textbox" onchange="syncName(event)"></td>
					</tr>
					<tr>
						<th>组件：</th>
						<td><input name="type" class="textbox" readonly="readonly"></td>
					</tr>
				</table>
				<div>扩展属性</div>
				<table class="extAttrs">
					<tr>
						<th>行号：</th>
						<td><input name="rowNum" class="textbox" readonly="readonly"></td>
					</tr>
					<tr>
						<th>列号：</th>
						<td><input name="colNum" class="textbox" readonly="readonly"></td>
					</tr>
					<tr>
						<th>跨列数：</th>
						<td><input name="colSpan" class="textbox"></td>
					</tr>
					<tr>
						<th>可编辑：</th>
						<td><input name="editable" class="textbox" type="checkbox" value="1"></td>
					</tr>
					<tr>
						<th>待办可见：</th>
						<td><input name="forWorkItem" class="textbox" type="checkbox" value="1"></td>
					</tr>
					<tr>
						<th>流程可见：</th>
						<td><input name="forWorkflow" class="textbox"  type="checkbox" value="1"></td>
					</tr>
					<tr>
						<th>字段长度：</th>
						<td><input name="size" class="textbox"></td>
					</tr>
				</table>
				<table class="customAttrs">
					<div>自定义属性</div>
					<div>
						<iframe id="customAttrs" src="" width="100%" style="border: 0px;"></iframe>
					</div>
				</table>
			</div>
		</div>
	</div>
	<div>
		<a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton">保存</a>
	</div>
	<div id="mm" class="easyui-menu" style="width:120px;">
	    <div>
	        <span>新加一行</span>
	        <div style="width:150px;">
	            <div handle="insertRowBefore">前</div>
	            <div handle="insertRowAfter">后</div>
	        </div>
	    </div>
	    <div>
	        <span>新加一列</span>
	        <div style="width:150px;">
	            <div handle="insertColumnBefore">前</div>
	            <div handle="insertColumnAfter">后</div>
	        </div>
	    </div>
	</div>
	<script type="text/javascript">
		<c:import url="/WEB-INF/jsp/formDesign/pageDesign.js" charEncoding="UTF8"/>
	</script>
</body>
</html>