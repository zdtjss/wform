<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<jsp:include page="/WEB-INF/jsp/include.jsp"/>
<title>${formPage.title!"" }</title>
</head>
<body>
	<#--<c:forEach var="group" items="${formPage.fielsGroups }">-->
	<#list formPage.fielsGroups as group>
		<div id="${group.name }" class="page_group">
			<#if group.displayType == 1>
				<table>
					<#--<c:set var="rowNum" value="1"/>-->
					<#assign rowNum = 1>
					<tr>
					<#list group.fields as field>
						<#if rowNum != field.rowNum> 
							</tr>
							<tr>
							<#--<c:set var="rowNum" value="${field.rowNum }"/>-->
							<#assign rowNum = field.rowNum + rowNum>
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
							<#--<c:import url="${componentPath }/${field.type }/${field.type }_edit.js">
								<c:param name="attributes" value="{\"name\":121}"/>
							</c:import>-->
						</td>
					</#list>
					<tr>
				</table>
			</#if>
			<#if group.displayType == 2>
			</#if>
		</div>
	</#list>
	<@exists path="/WEB-INF/wform/${formPage.moduleName}/${formPage.name}_create.js">
		<jsp:include page="/WEB-INF/wform/${formPage.moduleName}/${formPage.name}_create.js"/>
	</@exists>
</body>
</html>