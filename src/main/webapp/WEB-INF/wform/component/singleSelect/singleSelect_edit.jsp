<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="fieldName" value="${param.fieldName}_init" />

<select id="${param.fieldName }" name="${param.fieldName }" class="easyui-combobox">
	<c:forEach var="map" items="${dataModel[fieldName] }">
		<option value="${map.value }">${map.key }</option>
	</c:forEach>
</select>

<script type="text/javascript">
	    
    $("#${param.fieldName } [value='${dataModel[param.fieldName] }']").attr("selected", true);
</script>
