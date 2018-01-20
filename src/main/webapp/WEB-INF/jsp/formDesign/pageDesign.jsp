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
</style>
<script type="text/javascript" src="${contextPath }/js/components.js"></script>
</head>
<body>
	<div>
		<div id="components" style="width: 19%;float: left;border-top: 1px solid;margin-right: 3px;">
		</div>
		<div id="pageFields" style="float: left;width: 59%">
			<table width="100%" border="1">
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="1" column="1"></td>
					<th width="15%"></th>
					<td width="35%" row="1" column="2"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="2" column="1"></td>
					<th width="15%"></th>
					<td width="35%" row="2" column="2"></td>
				</tr height="30px">
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="3" column="1"></td>
					<th width="15%"></th>
					<td width="35%" row="3" column="2"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="4" column="1"></td>
					<th width="15%"></th>
					<td width="35%" row="4" column="2"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="5" column="1"></td>
					<th width="15%"></th>
					<td width="35%" row="5" column="2"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="6" column="1"></td>
					<th width="15%"></th>
					<td width="35%" row="6" column="2"></td>
				</tr>
				<tr height="30px">
					<th width="15%"></th>
					<td width="35%" row="1" column="1"></td>
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
						<td><input name="display" class="textbox" onchange="syncName()"></td>
					</tr>
					<tr>
						<th>组件：</th>
						<td><input name="type" class="textbox"></td>
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
						<td><input name="editable" class="textbox" type="checkbox"></td>
					</tr>
					<tr>
						<th>待办可见：</th>
						<td><input name="forWorkItem" class="textbox" type="checkbox"></td>
					</tr>
					<tr>
						<th>流程可见：</th>
						<td><input name="forWorkflow" class="textbox"  type="checkbox"></td>
					</tr>
					<tr>
						<th>字段长度：</th>
						<td><input name="size" class="textbox"></td>
					</tr>
				</table>
				<table class="customAttrs">
					<div>自定义属性</div>
				</table>
			</div>
		</div>
	</div>
	<div>
		<a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton">保存</a>
	</div>
	<script type="text/javascript">
		<jsp:include page="/WEB-INF/jsp/formDesign/pageDesign.js"/>
	</script>
</body>
</html>