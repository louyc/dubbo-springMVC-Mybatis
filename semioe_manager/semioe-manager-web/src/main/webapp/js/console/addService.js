layui.use(['element', 'form', 'layer', 'jquery'], function() {
	var $ = layui.jquery,form = layui.form(),element = layui.element();
	var layer = layui.layer;
	/*$('#layui-breadcrumb', parent.document).children("a").children("cite").text("添加服务");
	$('#menuList dd', parent.document).removeClass("layui-this");	*/
	function getGoods() {
		$("body").append("<div id='goods' style='display: none;'></div>");
		$.ajax({
	    	url:'/keywords/getAllKeywordsForFlow',
	        type:'POST',
	        dataType:"json",
	        contentType: "application/json",
	        success : function(data){
	        	if(data.successful){
	        		var oldLabel = $("#labelIds").val();
	        		if(data.data){
	        			var html='<form class="layui-form" id="labelForm" style="padding: 15px;"><div class="layui-form-item" pane="">';
	        			for(var i =0;i<data.data.length;i++){
	        				if(!!oldLabel && oldLabel.length>0 ){
	        					if(checkLabel(str)){
	        						if(data.data[i].inUse=="1"){
			        					html+='<input type="checkbox" id="'+data.data[i].id+'" title="'+data.data[i].name+'" checked="">';
			        				}
	        					}else{
	        						if(data.data[i].inUse=="1"){
			        					html+='<input type="checkbox" id="'+data.data[i].id+'" title="'+data.data[i].name+'">';
			        				}
	        					}
							}else{
								html+='<input type="checkbox" id="'+data.data[i].id+'" title="'+data.data[i].name+'">';
							}
	        				
	        			}
	        			html+='</div></form>';
	        			$("#labels").html(html);
	        			form.render("checkbox");
	        		}
	        	};
	        	layer.open({
                    type: 1 
                    ,title: '请选择标签'
                    ,area: ['80%', '80%']
            		,shade: 0.5
                    /*,maxmin: true*///缩放功能
                    ,content: $("#labels")
                    ,btn: ['确定','关闭'] //按钮
                    ,yes: function(index, layero){
                    	var labelIds = "",labelNames = "";
                    	$("#labelForm .layui-form-item .layui-unselect").each(function(){
                    		if($(this).attr("class").indexOf("layui-form-checked")>0){
                    			if(labelIds){
                    				labelIds +=",";
                    			}
                    			labelIds += $(this).next("input").attr("id");
                    			if(labelNames){
                    				labelNames +=",";
                    			}
                    			labelNames += $(this).next("input").attr("title");
                    		}
                    	});
                    	$("#labelBox").html("<input type='hidden' id='labelIds' value='"+labelIds+"'/><textarea style='margin-top: 10px;' disabled id='label_list' class='layui-textarea'>"+labelNames+"</textarea>");
                    	layer.closeAll();
                    }
                    ,zIndex: layer.zIndex //重点1
                    ,success: function(layero){
                        layer.setTop(layero); //重点2
                    }
                });
	        },
	        error : function(data){
	        	layer.msg('失败！');
	        },
	    });
	}
});
