<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="map" items="${dataModel[param.fieldName] }">
	<span><span>${map.value }</span><input type="checkbox" name="${param.fieldName }" value="${map.key }"></span>
</c:forEach>
