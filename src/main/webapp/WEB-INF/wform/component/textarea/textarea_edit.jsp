<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="efn" uri="http://wform.platform.nway.com/jsp/tag/functions"%>

<c:set var="width" value="${fieldAttr[param.fieldName]['width'] }"/>
<c:set var="height" value="${fieldAttr[param.fieldName]['height'] }"/>
<c:set var="border_radius" value="${fieldAttr[param.fieldName]['border-radius'] }"/>

<textarea id="${param.fieldName }" name="${param.fieldName }" style="${efn:css('width', not empty width ? width :'100%')}${efn:css('height', height)}${efn:css('border-radius', border_radius)}">${dataModel[param.fieldName] }</textarea>