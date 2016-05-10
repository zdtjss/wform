<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title>普通文本组件</title>
</head>
<body>
	<c:forEach var="component" items="${components }" varStatus="status">
		<jsp:include page="/form/component/page">
			<jsp:param name="componentName" value="${component.componentName }" />
			<jsp:param name="componentType" value="${component.componentType }" />
		</jsp:include>
	</c:forEach>
</body>
</html>