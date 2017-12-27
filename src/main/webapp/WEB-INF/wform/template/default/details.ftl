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
				<input id="${group.name }Pkid" name="pkId" type="hidden" value="${r"${dataModel['"}${group.id }${r"']['pkId']}"}">
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
						<td id="${field.name }_view" colspan="${field.colSpan!1 }">
							<jsp:include page="/WEB-INF/wform/component/${field.type }/${field.type }_detail.jsp">
								<jsp:param name="groupId" value="${group.id}"/>
								<jsp:param name="fieldName" value="${field.name}"/>
							</jsp:include>
						</td>
					</#list>
					<tr>
				</table>
				</form>
			</#if>
			<#if group.displayType == 2>
			</#if>
			<div id="processbar">
			</div>
	</#list>
	<script type="text/javascript">
	</script>
	<@exists path="/WEB-INF/jsp/${formPage.moduleName}/${formPage.name}_details.js">
		<jsp:include page="/WEB-INF/jsp/${formPage.moduleName}/${formPage.name}_details.js"/>
	</@exists>
</body>
</html>