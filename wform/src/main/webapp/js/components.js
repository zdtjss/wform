/**
 * 初始化方法
 * 
 * @param cmpInfo
 *            组件信息
 */
function init_text_js(formData, componentName) {
	console.log(formData[componentName] + "  页面js");
	$("#l_"+componentName).html("<input id=\""+componentName+"\" name=\""+componentName+"\" type=\"text\">");
}

function init_text_file(formId, componentName) {

}

function setValue_text(data, componentName) {
	$("#"+componentName).val(data[componentName]);
}

function init_select_file(staticData, componentName) {
	console.log("静态数据文件");
	var html = "<select id=\""+componentName+"\" name=\""+componentName+"\">"
	
	for(var i = 0;i < staticData.length ; i++) {
		
		html += "<option value=\""+staticData[i].value+"\">"+staticData[i].text+"</option>";
	}
	
	html += "</select>";
	
	$("#l_"+componentName).html(html);
}

function init_select_js(formData, componentName) {
	console.log(componentName);
}

function setValue_select(data, componentName) {
	$("#"+componentName+" option").each(function() {
		for(var i = 0;i < data[componentName].length;i++) {
			if(this.value == data[componentName][i]) {
				this.selected = "selected";
				return;
			}
		}
	});
}

function init_label_js(formData, componentName) {
	
}

function init_label_file(formId, componentName) {

}

function setValue_label(data, componentName) {
}