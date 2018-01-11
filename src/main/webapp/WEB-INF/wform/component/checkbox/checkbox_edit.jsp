<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="map" items="${dataModel[param.fieldName +'_init'] }">
	<span><span>${map.value }</span><input type="checkbox" name="${param.fieldName }" value="${map.key }"></span>
</c:forEach>

<script type="text/javascript">
	var values = [];
	<c:forEach var="value" items="${dataModel[param.fieldName] }" varStatus="status">
		values[${status.index}] = "${value}";
	</c:forEach>
	$.each(values, function() {
	    
	    $("[value='"+this+"']").attr("checked", true);
	});
</script>
