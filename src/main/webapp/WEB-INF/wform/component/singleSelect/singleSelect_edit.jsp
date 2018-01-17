<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="fieldName" value="${param.fieldName}_init" />

<select id="${param.fieldName }" name="${param.fieldName }" class="easyui-combobox">
	<c:forEach var="map" items="${dataModel[fieldName] }">
		<option value="${map.key }">${map.value }</option>
	</c:forEach>
<select id="${param.fieldName }" name="${param.fieldName }">

<script type="text/javascript">
	var values = [];
	<c:forEach var="stored" items="${dataModel[param.fieldName] }" varStatus="status">
		values["${status.index}"] = "${stored}";
	</c:forEach>
	$.each(values, function() {
	    
	    $("#${param.fieldName } [value='"+this+"']").attr("selected", true);
	});
</script>
