<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<jsp:include page="/WEB-INF/jsp/include.jsp"/>
<title>${page.title!"" }</title>
</head>
<body>
	<input id="pageId" name="pageId" type="hidden" value="${page.id}">
	<form id="${page.name }" class="page">
	<table>
		<#assign rowNum = 1>
		<tr>
		<#list page.fields as field>
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
				<jsp:include page="/WEB-INF/wform/component/${field.type }/${field.type }_detail.jsp">
					<jsp:param name="pageId" value="${page.id}"/>
					<jsp:param name="fieldName" value="${field.name}"/>
				</jsp:include>
			</td>
		</#list>
		<tr>
	</table>
	</form>
	<script type="text/javascript">
	</script>
	<@exists path="/WEB-INF/jsp/${page.moduleName}/${page.name}_details.js">
		<jsp:include page="/WEB-INF/jsp/${page.moduleName}/${page.name}_details.js"/>
	</@exists>
</body>
</html>