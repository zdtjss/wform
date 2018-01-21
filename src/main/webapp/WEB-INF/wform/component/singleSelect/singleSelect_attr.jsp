<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title>页面设计</title>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
</head>
<body>
	<div>
<input type="checkbox" name="initType" value="sql"> sql
<input type="checkbox" name="initType" value="dic"> dic
<br>
<input id="dic" name="dic" style="display: none">
<textarea id="sql" name="sql" style="display: none"></textarea>
</div>
<script type="text/javascript">
	$("[name=initType]").on("click", function() {
		$("[name=initType]").attr("checked", false);
		this.checked = true;
		if(this.value == "sql") {
			$("#dic").val("");
			$("#dic").hide();
			$("#sql").show();
		}
		else if(this.value == "dic") {
			$("#sql").val("");
			$("#sql").hide();
			$("#dic").show();
		}
	});
	
	function onReady() {
		
		if($("#dic").val()) {
			$("#dic").show();
		}
		
		if($("#sql").val()) {
			$("#sql").show();
		}
	}
</script>
</body>