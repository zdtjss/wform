<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<jsp:include page="/WEB-INF/jsp/include.jsp"/>
<title>${formPage.title!"" }</title>
</head>
<body>
	<input id="pageId" name="pageId" type="hidden" value="${formPage.id}">
	<#list formPage.fielsGroups as group>
		<#if group.displayType == 1>
		<form id="${group.name }" class="page_group">
			<input name="groupName" type="hidden" value="${group.name }">
			<table>
				<#assign rowNum = 1>
				<tr>
				<#list group.fields as field>
					<#if rowNum != field.rowNum> 
						</tr>
						<tr>
						<#assign rowNum = field.rowNum>
					</#if>
					<#-- 独占一行的不现实 <th> -->
					<#if field.colSpan != group.maxColumnNum >
						<th id="${field.name }_label">${field.display }</th>
					</#if>
					<td id="${field.name }_text" colspan="${field.colSpan!1 }">
						<jsp:include page="/WEB-INF/wform/component/${field.type }/${field.type }_edit.jsp">
							<jsp:param name="groupId" value="${group.id}"/>
							<jsp:param name="fieldName" value="${field.name}"/>
						</jsp:include>
					</td>
				</#list>
				<tr>
			</table>
			<div id="processbar">
				<a href="javascript:void(0)" onclick="query()">查询</a>
			</div>
			</form>
		</#if>
		<#if group.displayType == 2>
			<table id="${group.name }"></table>
			<script type="text/javascript">
				var queryParam = $($(".page_group")[0]).serializeObject(); 
				queryParam["pageId"] = $("#pageId").val();
				$('#${group.name }').datagrid({
					striped:true,
					fitColumns:true,
					pagination:true,
					rownumbers:true,
					singleSelect:true,
					ctrlSelect:true,
				    url:contextPath + "/form/listData",
				    queryParams : queryParam,
				    columns:[[
				        <#list group.fields as field>
					        {field:'${field.name }',title:'${field.display }',width:100}${field?has_next?then(',','')}
					    </#list>
				    ]]
				});
			</script>
		</#if>
	</#list>
	<script type="text/javascript">
	
	</script>
</body>
</html>