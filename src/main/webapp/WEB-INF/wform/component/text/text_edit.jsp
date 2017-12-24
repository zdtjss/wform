<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formFn" uri="http://wform.nway.com/jsp/tag/functions"%>

<c:set var="width" value="${fieldAttr[param.groupId][param.fieldName]['width'] }"/>
<input type="text" name="${param.fieldName }" value="${dataModel[param.fieldName] }" style="${formFn:css('width','width')}">
<script type="text/javascript">
	var attributes = jQuery.parseJSON('${param.attributes }');
</script>

