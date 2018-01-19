<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setAttribute("contextPath", request.getContextPath());
%>
<link rel="stylesheet" type="text/css" href="${contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${contextPath }/js/easyui/themes/icon.css">
<script src="${contextPath }/js/easyui/jquery.min.js"></script>
<script src="${contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script src="${contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="${contextPath }/js/json2.js"></script>
<script src="${contextPath }/js/commons.js"></script>

<script type="text/javascript">
	var contextPath = "${contextPath }";
</script>
