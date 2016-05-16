/**
 * 初始化方法
 * 
 * @param cmpInfo
 *            组件信息
 */
function init_text_js(formData, componentName) {
	console.log(formData[componentName] + "  页面js");
	$("#l_"+componentName).text("后初始化 js 赋值");
}

function init_text_file(formId, componentName) {

}

function init_select_file(formId, componentName) {
	console.log("静态数据文件");
	$("#l_"+componentName).text("后初始化静态数据文件");
	var html = "<select><option value='1'>请选择</option><option value='2'>通过</option></select>";
	$("#l_"+componentName).html(html);
}

function init_select_js(formData, componentName) {
	console.log(componentName);
}