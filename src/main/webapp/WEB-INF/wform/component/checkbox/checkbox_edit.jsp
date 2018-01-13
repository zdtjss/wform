<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="fieldName" value="${param.fieldName}_init" />

<c:forEach var="map" items="${dataModel[fieldName] }">
	<span><span>${map.value }</span><input type="checkbox" name="${param.fieldName }" value="${map.key }"></span>
</c:forEach>

<script type="text/javascript">
	var values = [];
	<c:forEach var="stored" items="${dataModel[param.fieldName] }" varStatus="status">
		values["${status.index}"] = "${stored}";
	</c:forEach>
	$.each(values, function() {
	    
	    $("[value='"+this+"']").attr("checked", true);
	});
</script>
