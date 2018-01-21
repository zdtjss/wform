	$(function(){
		initComponent();
		initDrag();
		initHiddenDrag();
		initContextmenu();
		initFieldKey();
        $(".baseAttrs input").on("blur", changeBaseAttr);
        $(".extAttrs input").on("blur", changeExtAttr);
        $("#canvas").find("th,td").on("mousedown", selectCell);
        
    });
	
	function initComponent() {
		var componentsHtml = [];
		$.each(components, function() {
			
			componentsHtml.push("<div class=\"component\"><span name=\""+this.name+"\">"+this.desc+"</span></div>");
		});
		$("#components").html(componentsHtml.join(""));
	}
	function initFieldKey() {
		
		var keyId = new Date().getTime();
		var key = $("<span id=\""+keyId+"\"name=\"key\" row=\"1\" column=\"1\">主键</span>");
		
		$(".hiddenField").append(key);
		
		initPageFields(key);
		
		pageFields[keyId].baseAttr.name = "主键";
		pageFields[keyId].baseAttr.display = "主键";
		pageFields[keyId].extAttr.isHidden = "1";
	}
	function initHiddenDrag() {
		
		$('.hiddenField').droppable({
            accept: 'span',
            onDragEnter:function(){
                $(this).addClass('over');
            },
            onDragLeave:function(){
                $(this).removeClass('over');
            },
            onDrop:function(e,source){
                $(this).removeClass('over');
                if ($(source).hasClass('assigned')){
                    $(this).append(source);
                } else {
                    var c = $(source).clone().addClass('assigned');
                    $(this).append(c);
                    c.draggable({
                        revert:true
                    });
                }
                resetWidth($(".hiddenField"));
            }
        });
	}
	function initDrag() {
		 $('#components span').draggable({
	            revert:true,
	            proxy:'clone'
	        });
	        $('#canvas td').droppable({
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
	                    $(this).append(source);
	                    initPageFields($(source));
	 	                showAttr($(source));
	                } else {
	                    var c = $(source).clone().addClass('assigned');
	                    c.attr("row", position.row);
	 	                c.attr("column", position.column);
	 	                c.attr("id", new Date().getTime());
	                    $(this).empty().append(c);
	                    initPageFields(c);
	 	                showAttr(c);
	                    c.draggable({
	                        revert:true
	                    });
	                    c.on("click", showAttr);
	                }
	                resetWidth($("#canvas"));
	            }
	        });
	}

	function initContextmenu() {
		
		$("#canvas td").on("contextmenu", function(e) {
        	e.preventDefault();
        	selectRow(e);
        	$('#mm').menu('show', {
        		left: e.pageX,
             	top: e.pageY,
             	onClick : function(item) {
             		var handle = $(item.target).attr("handle");
             		if (typeof window[handle] === "function") {
             			window[handle]();
         		    }
             		initDrag();
             		initContextmenu();
             	}
            });
        });
		
		function selectRow(event) {
			
			var position = findPosition(event.target);
			
			if(position == undefined) {
				alert("无效选择");
				return;
			}
			
			$("#canvas .row_selected").removeClass("row_selected");
			$("#canvas .column_selected").removeClass("column_selected");
			
			$.each($("#canvas tr"), function(index) {
				if(index == position.row - 1) {
					$(this).addClass("row_selected");
				}
				$(this).find("td").eq(position.column - 1).addClass("column_selected");
			})
		}
	}
	function insertRowAfter() {
		
		var source = $("#canvas .row_selected").clone();
		$.each($(source).find("th,td"),function() {
			$(this).empty();
		});
		source.insertAfter($("#canvas .row_selected"));
		$("#canvas .row_selected").removeClass("row_selected");
		$("#canvas .column_selected").removeClass("column_selected");
		refreshRowIndex(findRowNum(source[0]));
	}
	function insertRowBefore() {
		
		var source = $("#canvas .row_selected").clone();
		$.each($(source).find("th,td"),function() {
			$(this).empty();
		});
		source.insertBefore($("#canvas .row_selected"));
		$("#canvas .row_selected").removeClass("row_selected");
		$("#canvas .column_selected").removeClass("column_selected");
		refreshRowIndex(findRowNum(source[0]));
	}
	function insertColumnBefore() {
		var source = $("<th></th><td></td>");
		source.insertBefore($("#canvas .column_selected").prev());
		$("#canvas .row_selected").removeClass("row_selected");
		$("#canvas .column_selected").removeClass("column_selected");
		resetWidth($("#canvas"));
		refreshColumnIndex(findPosition(source[1]).column);
	}
	function insertColumnAfter() {
		
		$("<th></th><td></td>").insertAfter($("#canvas .column_selected"));
		$("#canvas .row_selected").removeClass("row_selected");
		$("#canvas .column_selected").removeClass("column_selected");
		resetWidth($("#canvas"));
	}
	
	function findPosition(srcElement) {
		
		var rows = $("#canvas tr");
		
		for(var row = 0; row < rows.length; row++) {
			
			var columns = $(rows[row]).find("td");
			
			for(var column = 0; column < columns.length; column++) {
				
				if(columns[column] == srcElement) {
					
					return {row:row + 1, column:column + 1};
				}
			}
		}
	}
	function findRowNum(srcElement) {
		
		var rows = $("#canvas tr");
		
		for(var row = 0; row < rows.length; row++) {
			
			if(rows[row] == srcElement) {
				
				return row + 1;
			}
		}
	}
	var currentFieldId = "";
	var pageFields = {};
	function showAttr(ele) {
		
		currentFieldId = ele[0] != undefined ? ele.attr("id") : this.id;
		
		var fieldAttr = pageFields[currentFieldId];
		
		clearFieldAttrsPage($(".baseAttrs input"));
		clearFieldAttrsPage($(".extAttrs input"));
		
		$("#customAttrs").attr("src", contextPath + "/formDesign/showCustomAttr?type="+fieldAttr.baseAttr.type);
		
		for(var attr in fieldAttr.baseAttr) {
			
			setFieldAttr($(".baseAttrs [name='"+attr+"'"), fieldAttr.baseAttr[attr]);
		}
		for(var attr in fieldAttr.extAttr) {
			
			setFieldAttr($(".extAttrs [name='"+attr+"'"), fieldAttr.extAttr[attr]);
		}
		$("#customAttrs").on("load", function() {
			
			$("input,textarea", this.contentDocument).on("blur", parent.changeCustomAttr);
			
			for(var attr in fieldAttr.customAttr) {
				
				parent.setFieldAttr($("[name='"+attr+"'", this.contentDocument), fieldAttr.customAttr[attr]);
			}
			
			if(typeof this.contentWindow.onReady == "function") {
				
				this.contentWindow.onReady();
			}
		});
		
		function clearFieldAttrsPage(source) {
			
			$.each(source, function() {
					
				if(this.type == undefined || this.type.toLowerCase() == "text" ) {
					
					this.value = "";
				}
				else if(this.type.toLowerCase() == "checkbox") {
					
					this.checked = false;
				}
			});
		}
	}
	
	function setFieldAttr(attrElement, value) {
		
		if(attrElement.length == 0 || value == undefined) {
			return;
		}
		
		$.each(attrElement, function() {
			
			if(this.nodeName.toUpperCase() == "TEXTAREA" || $(this).attr("type") == 'text' || $(this).attr("type") == undefined) {
				
				$(this).val(value);
			}
			else if($(this).attr("type") == 'checkbox') {
				
				if($(this).val() == value) {
					
					this.checked = true;
				}
			}
		});
	}
	
	function initPageFields(el) {
		
		var attrs = pageFields[el.attr("id")];
		
		if(attrs != undefined) {
			attrs.extAttr.rowNum = el.attr("row");
			attrs.extAttr.colNum = el.attr("column");
		}
		else {
			var colspan = $("#"+el.attr("id")).parent().attr("colspan") || 1;
			pageFields[el.attr("id")] = {};
			pageFields[el.attr("id")].baseAttr = {type:el.attr("name"),pageId:pageId};
			pageFields[el.attr("id")].extAttr = {rowNum:el.attr("row"),colNum:el.attr("column"),colSpan : colspan,pageId:pageId};
			pageFields[el.attr("id")].customAttr = {};
		}
	}
	
	function changeBaseAttr(event) {
		
		pageFields[currentFieldId].baseAttr[event.currentTarget.name] = event.currentTarget.value;
	}
	
	function changeExtAttr(event) {
		
		var source = event.currentTarget;
		
		if(source.nodeName.toUpperCase() == "INPUT") {
			
			if(source.type == undefined || source.type.toLowerCase() == "text" ) {
				
				pageFields[currentFieldId].extAttr[source.name] = source.value;
			}
			else if(source.type.toLowerCase() == "checkbox") {
				
				pageFields[currentFieldId].extAttr[source.name] = 1;
			}
		}
		else if(source.nodeName.toUpperCase() == "TEXTAREA") {
			
			pageFields[currentFieldId].extAttr[source.name] = source.value;
		}
		
	}
	function changeCustomAttr(event) {
		
		var source = event.currentTarget;
		
		if(source.nodeName.toUpperCase() == "INPUT") {
			
			if(source.type == undefined || source.type.toLowerCase() == "text" ) {
				
				pageFields[currentFieldId].customAttr[source.name] = source.value;
			}
			else if(source.type.toLowerCase() == "checkbox") {
				
				pageFields[currentFieldId].customAttr[source.name] = this.value;
			}
		}
		else if(source.nodeName.toUpperCase() == "TEXTAREA") {
			
			pageFields[currentFieldId].customAttr[source.name] = source.value;
		}
	}
	function syncName(event) {
		
		$("#"+currentFieldId).parent().prev().text(event.currentTarget.value);
	}
	
	function submit() {
		var submitData = [];
		for(var attrs in pageFields) {
			submitData.push(pageFields[attrs]);
		}
		$.ajax({
			type : "post",
			dataType : "json",
			contentType:"application/json",
			data : JSON.stringify(submitData),
			url : contextPath + "/formDesign/saveFields",
			success : function(resp) {
				
			},
			error : function() {
					
			}
		});
	}
	function resetWidth(table) {
		
		var cellCount = table.find("tr").eq(0).find("th,td").length;
		table.find("th, td").attr("width",(100 / cellCount) +"%")
	}
	
	function refreshRowIndex(benchmark) {
		
		for(var attrs in pageFields) {
			for(var extAttr in pageFields[attrs].extAttr) {
				if(extAttr == 'rowNum' && benchmark <= pageFields[attrs].extAttr.rowNum) {
					pageFields[attrs].extAttr.rowNum = new Number(pageFields[attrs].extAttr.rowNum) + 1;
				}
			}
		}
	}
	function refreshColumnIndex(benchmark) {
		
		for(var attrs in pageFields) {
			for(var extAttr in pageFields[attrs].extAttr) {
				if(extAttr == 'colNum' && benchmark <= pageFields[attrs].extAttr.colNum) {
					pageFields[attrs].extAttr.colNum = new Number(pageFields[attrs].extAttr.colNum) + 1;
				}
			}
		}
	}
	function updateColspan(id, colspan) {
		
		if(id != undefined) {
		
			pageFields[id].extAttr.colSpan = colspan;
		}
	}
	
	function selectCell(event) {
		
		$("#canvas .cell_selected").removeClass("cell_selected");
		$(event.target).addClass("cell_selected");
		$(event.target).parent().on("mousemove", function(e) {
			$(e.target).addClass("cell_selected");
		});
		$(event.target).parent().on("mouseup", function(e) {
			var selected = $("#canvas .cell_selected");
			var colspan = selected.length;
			
			if($("#canvas tr").eq(0).find("th,td").length == colspan) {
				$(this).empty().append("<td colspan=\""+colspan+"\"></td>");
				initDrag();
			}
			else {
				if(selected[0].nodeName.toUpperCase() == "TD") {
					$(selected[0]).attr("colspan", colspan)
					selected.slice(1).remove();
				}
				else {
					$(selected[0]).remove();
					$(selected[1]).attr("colspan", colspan)
					selected.slice(2).remove();
				}
			}
			
			updateColspan($("#canvas .cell_selected .assigned").attr("id"), colspan);
			
			selected.removeClass("cell_selected");
			$(this).off("mousemove");
			$(this).off("mouseup");
		});
	}