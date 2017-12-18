<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%
	pageContext.setAttribute("contextPath", request.getContextPath());
%>
<script type="text/javascript">
	var contextPath = ${contextPath };
</script>
<script src="${contextPath }/js/easyui/jquery.min.js"></script>