<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="fieldName" value="${param.fieldName}_init" />
<c:set var="storedValue" value="${dataModel[param.fieldName]}" />

${dataModel[fieldName][storedValue] }