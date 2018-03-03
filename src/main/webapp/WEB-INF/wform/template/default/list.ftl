<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<jsp:include page="/WEB-INF/jsp/include.jsp"/>
<title>${page.title!"" }</title>
<style type="text/css">
 th {
    font-weight: normal;
 }
 .easyui-linkbutton {
	padding: 3px 10px;
	margin-top: 5px;
}
</style>
</head>
<body>
	<form id="${page.name }_condition" class="page_group">
	<input id="pageId" name="pageId" type="hidden" value="${page.id}">
		<table>
			<#assign rowNum = 1>
			<tr>
			<#list page.listCondition as field>
				<#if rowNum != field.rowNum> 
				</tr>
				<tr>
				<#assign rowNum = field.rowNum>
				</#if>
				<#-- 独占一行的不现实 <th> -->
				<#if field.colSpan != page.maxColumnNum >
				<th id="${field.name }_label">${field.display }</th>
				</#if>
				<td id="${field.name }_view" colspan="${field.colSpan!1 }">
					<jsp:include page="/WEB-INF/wform/component/${field.type }/${field.type }_edit.jsp">
						<jsp:param name="pageId" value="${page.id}"/>
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
		<table id="${page.name }_list"></table>
		<script type="text/javascript">
			var queryParam = $("#${page.name}_condition").serializeObject(); 
			$("#${page.name }_list").datagrid({
				striped:true,
				fitColumns:true,
				pagination:true,
				rownumbers:true,
				singleSelect:true,
				ctrlSelect:true,
			    url:contextPath + "/form/listData",
			    queryParams : queryParam,
			    columns:[[
			        <#list page.listFields as field>
				        <#if field.type == 'key' >
				        	<#assign key=field.name>
				        	{field:'${field.name }',hidden:true}${field?has_next?then(',','')}
				        </#if>
				        <#if field.type != 'key'>
				        	{field:'${field.name }',title:'${field.display }',width:100<#if field.formatter??>,formatter:${field.formatter}</#if>}${field?has_next?then(',','')}
				        </#if>
				    </#list>
			    ]]
			});
			
		function showLink(value, row, index) {
			
			return "<a href=\"${r'${contextPath}'}/form/toUI?pageType=details&pageId=${page.id}&bizId="+row.${key}+"\">"+value+"</a>";
		}
		
		function query() {
			
			$("#${page.name }_list").datagrid("reload", $("#${page.name}_condition").serializeObject());
		}
		<@exists path="/WEB-INF/jsp/${page.moduleName}/${page.name}_list.js">
			<%@ include file="/WEB-INF/jsp/${page.moduleName}/${page.name}_list.js" %>
		</@exists>
	</script>
</body>
</html>