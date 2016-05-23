function init_select_file(staticData, componentName) {
	console.log("静态数据文件");
	var html = "<select id=\""+componentName+"\">"
	
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