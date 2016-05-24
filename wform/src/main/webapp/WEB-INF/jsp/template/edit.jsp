<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String contextPath = request.getContextPath();
	application.setAttribute("contextPath", contextPath);
	StringBuffer requestURL = request.getRequestURL();
	String host = requestURL.substring(0, requestURL.indexOf(request.getRequestURI()));
	pageContext.setAttribute("host", host + contextPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title>编辑模板</title>
<link rel="stylesheet" type="text/css" href="${contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${contextPath }/js/easyui/themes/icon.css">
<script type="text/javascript" src="${contextPath }/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath }/js/easyui/jquery.easyui.min.js"></script>
</head>
<body bgcolor="antiquewhite">
	<form action="${contextPath}/form/saveData">
		<input type="hidden" name="formId" value="${param.formId }">
		<input type="hidden" name="requestVersion" value="${param.requestVersion }">
		<%-- 遍历组件，布局 --%>
		<c:forEach var="component" items="${components }" varStatus="status">
			<%-- 需要静态化的组件 --%>
			<c:if test="${component.renderType == htmlRender}">
				<%-- <jsp:include page="/form/componentStaticPage" flush="true">
					<jsp:param name="formId" value="${param.formId }" />
					<jsp:param name="name" value="${component.name }" />
					<jsp:param name="type" value="${component.type }" />
					<jsp:param name="displayMode" value="${component.editable ? 'edit' : 'detail'}" />
				</jsp:include> --%>
				<c:import url="${host }/form/componentStaticPage">
					<c:param name="formId" value="${param.formId }" />
					<c:param name="name" value="${component.name }" />
					<c:param name="type" value="${component.type }" />
					<c:param name="displayMode" value="${component.editable ? 'edit' : 'detail'}" />
				</c:import>
			</c:if>
			<%-- 动态数据组件或静态数据组件 --%>
			<c:if test="${component.renderType != htmlRender}">
				<div id="l_${component.name }"></div>
			</c:if>
		</c:forEach>
		<input type="submit">
	</form>
	<script type="text/javascript" src="${contextPath }/js/components.js"></script>
	<script type="text/javascript">
		$(function() {
			(function initPage() {
				jQuery.ajax({
					async : false,
					dataType: "json",
					url : "${contextPath}/staticdata/${formName}.json",
					success : function(staticData) {
						<%-- 静态数据组件初始化 --%>
						<c:forEach var="component" items="${components }">
							<c:if test="${component.renderType == fileRender}">
								<c:out value="init_${component.type}_file(staticData['${component.name}'],'${component.name}');" escapeXml="false"></c:out>
							</c:if>
						</c:forEach>
					},
					error : function(xhr,errMsg) {
						jQuery.messager.alert('错误','静态数据加载失败');
					}
				});
				
				jQuery.ajax({
						async : false,
						dataType: "json",
						url : "${contextPath}/form/getData?id=1",
						success : function(formData) {
							<%-- 动态数据组件初始化 --%>
							<c:forEach var="component" items="${components }">
								<c:if test="${component.renderType == jsRender}">
									<c:out value="init_${component.type}_js(formData, '${component.name}');" escapeXml="false"></c:out>
								</c:if>
								<c:out value="setValue_${component.type}(formData, '${component.name}');" escapeXml="false"></c:out>
							</c:forEach>
							console.log("ajax success");
						},
						error : function(xhr,errMsg) {
							jQuery.messager.alert('错误','动态数据加载失败');
						}
					});
				console.log("ajax af");
			}());
			
			<%-- 后置 javascript 效果 --%>
			loadJs("${contextPath }/js/custom/${formName}.js");
			<%-- 后置 css 效果 --%>
			loadCss("${contextPath }/css/custom/${formName}.css");
			
			function loadJs(url){
				var script = document.createElement('script');
				script.type = 'text/javascript';
				script.language = 'javascript';
				script.src = url;
				document.getElementsByTagName("head")[0].appendChild(script);
			}
			
			function loadCss(url){
				var link = document.createElement('link');
				link.rel = 'stylesheet';
				link.type = 'text/css';
				link.media = 'screen';
				link.href = url;
				document.getElementsByTagName('head')[0].appendChild(link);
			}
		});
	</script>
</body>
</html>