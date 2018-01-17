<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="efn" uri="http://wform.platform.nway.com/jsp/tag/functions"%>

<c:set var="width" value="${fieldAttr[param.fieldName]['width'] }"/>
<c:set var="height" value="${fieldAttr[param.fieldName]['height'] }"/>

<input type="text" name="${param.fieldName }" value="${dataModel[param.fieldName] }" class="easyui-textbox" style="${efn:css('width', not empty width ? width :'100%')}${efn:css('height', height)}">

<script type="text/javascript">

</script>

