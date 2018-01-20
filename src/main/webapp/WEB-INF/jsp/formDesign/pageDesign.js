$(function(){
		var componentsHtml = [];
		$.each(components, function() {
			
			componentsHtml.push("<div class=\"component\"><span name=\""+this.name+"\">"+this.desc+"</span></div>");
		});
		$("#components").html(componentsHtml.join(""));
        $('#components span').draggable({
            revert:true,
            proxy:'clone'
        });
        $('#pageFields td').droppable({
            accept: 'span',
            onDragEnter:function(){
                $(this).addClass('over');
            },
            onDragLeave:function(){
                $(this).removeClass('over');
            },
            onDrop:function(e,source){
            	var position = findPosition(this);
                $(this).removeClass('over');
                if ($(source).hasClass('assigned')){
                	$(source).attr("row", position.row);
 	                $(source).attr("column", position.column);
 	                if($(source).parent()[0] != this) {
	 	                var label = $(source).parent().prev();
	 	                $(this).prev().text(label.text());
	 	                label.text("");
 	                }
 	                initPageFields($(source));
 	                showAttr($(source));
                    $(this).append(source);
                } else {
                    var c = $(source).clone().addClass('assigned');
                    c.attr("row", position.row);
 	                c.attr("column", position.column);
 	                c.attr("id", new Date().getTime());
 	                initPageFields(c);
 	                showAttr(c);
                    $(this).empty().append(c);
                    c.draggable({
                        revert:true
                    });
                    c.on("click", showAttr);
                }
            }
        });
        $('#fields').droppable({
            accept:'.assigned',
            onDragEnter:function(e,source){
                $(source).addClass('trash');
            },
            onDragLeave:function(e,source){
                $(source).removeClass('trash');
            },
            onDrop:function(e,source){
                $(source).remove();
            }
        });
        $(".baseAttrs input").on("blur", changeBaseAttr);
        $(".extAttrs input").on("blur", changeExtAttr);
        $(".customAttrs input").on("blur", changeCustomAttr);
    });
	function findPosition(srcElement) {
		
		var rows = $("#pageFields table tr");
		
		for(var row = 0; row < rows.length; row++) {
			
			var columns = $(rows[row]).find("td");
			
			for(var column = 0; column < columns.length; column++) {
				
				if(columns[column] == srcElement) {
					
					return {row:row + 1, column:column + 1};
				}
			}
		}
	}
	var currentFieldId = "";
	var pageFields = {};
	function showAttr(ele) {
		
		currentFieldId = ele[0] != undefined ? ele.attr("id") : this.id;
		
		var fieldAttr = pageFields[currentFieldId];
		
		clearFieldAttrs();
		
		for(var attr in fieldAttr.baseAttr) {
			
			setFieldAttr($(".baseAttrs [name='"+attr+"'"), fieldAttr.baseAttr[attr]);
		}
		for(var attr in fieldAttr.extAttr) {
			
			setFieldAttr($(".extAttrs [name='"+attr+"'"), fieldAttr.extAttr[attr])
		}
		for(var attr in fieldAttr.customAttr) {
			
			setFieldAttr($(".customAttrs [name='"+attr+"'"), fieldAttr.customAttr[attr])
		}
		
		function setFieldAttr(attrElement, attr) {
			
			if(attrElement.attr("type") == 'text' || attrElement.attr("type") == undefined) {
				
				attrElement.val(attr)
			}
			else if(attrElement.attr("type") == 'checkbox') {
				
				if(attrElement.value == attr) {
					
					attrElement.attr("checked", true);
				}
			}
		}
	}
	
	function initPageFields(el) {
		
		var attrs = pageFields[el.attr("id")];
		
		if(attrs != undefined) {
			attrs.extAttr.rowNum = el.attr("row");
			attrs.extAttr.colNum = el.attr("column");
		}
		else {
			pageFields[el.attr("id")] = {};
			pageFields[el.attr("id")].baseAttr = {type:el.attr("name")};
			pageFields[el.attr("id")].extAttr = {rowNum:el.attr("row"),colNum:el.attr("column")};
			pageFields[el.attr("id")].customAttr = {};
		}
	}
	function changeBaseAttr() {
		
		pageFields[currentFieldId].baseAttr[event.target.name] = event.target.value;
	}
	function changeExtAttr() {
		
		pageFields[currentFieldId].extAttr[event.target.name] = event.target.value;
	}
	function changeCustomAttr() {
		
		pageFields[currentFieldId].customAttr[event.target.name] = event.target.value;
	}
	function syncName() {
		
		$("#"+currentFieldId).parent().prev().text(event.target.value);
	}
	function clearFieldAttrs() {
		
		$(".baseAttrs input").val("");
		$(".extAttrs input").val("");
		$(".customAttrs input").val("");
	}
	function submit() {
		var submitData = [];
		for(var attrs in pageFields) {
			submitData.push(pageFields[attrs]);
		}
		alert(JSON.stringify(submitData));
	}