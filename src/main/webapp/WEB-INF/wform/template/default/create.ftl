<%@ page contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<title>${page.title!"" }</title>
<style type="text/css">
 table {
	border-collapse: collapse;
 }

 table, td, th {
	border: 1px solid black;
 }
 
 th {
    font-weight: normal;
 }
 
 .easyui-linkbutton {
	padding: 3px 10px;
	margin-top: 5px;
}
</style>
</head>
<body>
	<input id="pageId" name="pageId" type="hidden" value="${page.id}">
	<div>
	<form id="${page.name }" class="page">
		<table>
			<#assign rowNum = 1>
			<tr>
			<#list page.formFields as field>
				<#if field.type == 'key'>
					<input name="${field.name }" type="hidden" value="${r'${dataModel["'}${field.name }${r'"]}' }">
				</#if>
				<#if field.type != 'key'>
				<#if rowNum != field.rowNum> 
				</tr>
				<tr>
				<#assign rowNum = field.rowNum>
				</#if>
				<#-- 独占一行的不现实 <th> -->
				<#if field.colSpan != page.maxColumnNum >
				<th id="${field.name }_label">${field.display }</th>
				</#if>
				<td id="${field.name }_view" colspan="${field.colSpan!1 }">
					<jsp:include page="/WEB-INF/wform/component/${field.type }/${field.type }_edit.jsp">
						<jsp:param name="fieldName" value="${field.name}"/>
					</jsp:include>
				</td>
				</#if>
			</#list>
			<tr>
		</table>
		</form>
		<div id="processbar">
			<@pdKey pageId="${page.id}">
				<#assign hasProcess=true>
				<div id="processDiagram" style="display:none;">
					<img src="${r'${contextPath}'}/workflow/getDiagram?taskId=${r'${workflow.taskId}'}">
				</div>
				<select id="outcome" class="easyui-combobox" style="width:200px;">
					<c:forEach var="outcome" items="${r'${outcomes }'}">
				    	<option value="${r'${outcome}'}">${r'${outcome}'}</option>
				    </c:forEach>
				</select>
			</@pdKey>
			<#if hasProcess!false == false> 
				<a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton">保存</a>
			</#if>
		</div>
	<div>
	<script type="text/javascript">
		function submit(type) {
		
			var pageData = {}; 
			
			pageData["formPage"] = {
					pageId : $("#pageId").val(),
					pageType : "create"
				};
				
			pageData["workflow"] = {
					taskId : "${r'${workflow.taskId}'}",
					action : type || 'forward',
					workItemId : "${r'${workflow.workItemId}'}",
					variables : {
						outcome : $("#outcome :selected").val()
					},
					currentUser : {
						userId : "test",
						cnName : "测试"
					}
				};
				
			pageData["pageData"] = $("#${page.name }").serializeObject();
			
			$.ajax({
				type : "post",
				dataType : "json",
				contentType:"application/json",
				data : JSON.stringify(pageData),
				url : contextPath + "/form/save",
				success : function(resp) {
					
				},
				error : function() {
						
				}
			});
		}
		
		<@exists path="/WEB-INF/jsp/${page.moduleName}/${page.name}_create.js">
			<%@ include file="/WEB-INF/jsp/${page.moduleName}/${page.name}_create.js" %>
		</@exists>
	</script>
</body>
</html>