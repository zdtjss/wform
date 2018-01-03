<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="efn" uri="http://wform.platform.nway.com/jsp/tag/functions"%>

<c:set var="width" value="${fieldAttr[param.groupId][param.fieldName]['width'] }"/>
<c:set var="height" value="${fieldAttr[param.groupId][param.fieldName]['height'] }"/>

<textarea id="${param.fieldName }" name="${param.fieldName }" style="${efn:css('width', width)}${efn:css('height', height)}">${dataModel[param.groupId][param.fieldName] }</textarea>