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
<title>${formPage.title }</title>
</head>
<body>
	<c:forEach var="group" items="${formPage.componentGroups }">
		<div id="${group.name }">
			<c:if test="${group.displayType eq displayForm}">
				<table>
				<c:set var="rowNum" value="0"/>
					<tr>
						<c:forEach var="field" items="${group.fields }">
							<th id="${field.name }_label">${field.display }</th>
							<td id="${field.name }_text" colspan="${field.colSpan }">
								<jsp:include page="${componentPath }/${field.type }/${field.type }_edit.jsp">
									<jsp:param value="attributes" name="${field.attributes }"/>
								</jsp:include>
							</td>
							<c:if test="${rowNum ne field.rowNum }"> 
								</tr>
								<tr>
								<c:set var="rowNum" value="${field.rowNum }"/>
							</c:if>
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