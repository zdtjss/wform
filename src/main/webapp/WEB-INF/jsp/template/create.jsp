<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.nway.wform.design.entity.FieldGroup" %>
<%
	pageContext.setAttribute("displayForm", FieldGroup.DISPLAY_TYPE_FORM);
	pageContext.setAttribute("displayList", FieldGroup.DISPLAY_TYPE_LIST);
	pageContext.setAttribute("componentPath", "/WEB-INF/jsp/component");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<jsp:include page="/WEB-INF/jsp/include.jsp"/>
<title>${formPage.title }</title>
</head>
<body>
	<c:forEach var="group" items="${formPage.fielsGroups }">
		<div id="${group.name }" class="page_group">
			<c:if test="${group.displayType eq displayForm}">
				<table>
					<c:set var="rowNum" value="1"/>
					<tr>
					<c:forEach var="field" items="${group.fields }">
						<c:if test="${rowNum ne field.rowNum }"> 
							</tr>
							<tr>
							<c:set var="rowNum" value="${field.rowNum }"/>
						</c:if>
						<th id="${field.name }_label">${field.display }</th>
						<td id="${field.name }_text" colspan="${field.colSpan }">
							<c:import url="${componentPath }/${field.type }/${field.type }_edit.jsp">
								<c:param name="attributes" value="{\"name\":121}"/>
							</c:import>
						</td>
					</c:forEach>
					<tr>
				</table>
			</c:if>
			<c:if test="${group.displayType eq displayList}">
			</c:if>
		</div>
	</c:forEach>
</body>
</html>