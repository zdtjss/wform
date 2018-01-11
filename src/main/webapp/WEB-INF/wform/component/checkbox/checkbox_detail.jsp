<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="fieldName" value="${param.fieldName}_init" />

<c:forEach var="map" items="${dataModel[fieldName] }">
	<c:forEach var="stored" items="${dataModel[param.fieldName] }">
		<c:if test="${map.key eq stored.value}">
			<span>${map.value}</span>
		</c:if>
	</c:forEach>
</c:forEach>