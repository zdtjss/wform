function init_text_js(formData, componentName) {
	console.log(formData[componentName] + "  页面js");
	$("#l_"+componentName).html("<input id=\""+componentName+"\" name=\""+componentName+"\" type=\"text\">");
}

function init_text_file(formId, componentName) {

}

function setValue_text(data, componentName) {
	$("#"+componentName).val(data[componentName]);
}