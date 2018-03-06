(function(window, document) {
	window.base = {
		/**
		 * 选择标签
		 */
		getLabel : function() {
			layui.use(['element', 'form', 'layer', 'jquery'], function() {
				var $ = layui.jquery,form = layui.form(),element = layui.element();
				var layer = layui.layer;
				
				function checkLabel(str){
					var ifTrue = false;
					var oldLabel = $("#labelIds").val();
					if(oldLabel.length>0){
    					var arr = new Array();
    					arr = oldLabel.split(",");
						for(var i=0;i<arr.length;i++){
    						if(str == arr[i]){
    							ifTrue = true;
    							break;
    						}
    					}
    				}
					return ifTrue;
				}
				
				$("body").append("<div id='labels' style='display: none;'></div>");
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
			        					if(checkLabel(data.data[i].id)){	
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
                            			labelIds += $(this).prev("input").attr("id");
                            			if(labelNames){
                            				labelNames +=",";
                            			}
                            			labelNames += $(this).prev("input").attr("title");
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
			});
		},
		/**
		 * 选择商品
		 */
		getGoods : function() {
			layui.use(['layer', 'jquery'], function() {
				var $ = layui.jquery;
				var layer = layui.layer;
				
				layer.open({
	                type: 2 //2为弹出iframe
	                ,title: '选择商品'
	                ,area: ['100%', '100%']
	        		,shade: 0.5
	                /*,maxmin: true*///缩放功能
	                /*,offset: [ //居中显示
	                   $(window).height()/2-350
	                  ,$(window).width()/2-350
	                ] */
	                ,content: 'selectGoods.html?state=PEDING'
	                ,btn: ['确定','关闭'] //按钮
	                ,yes: function(){
	                	var goodId = $("iframe").contents().find("#goodId").val();
	                	var goodName = $("iframe").contents().find("#goodName").val();
	                	var goodImgUrl = $("iframe").contents().find("#goodImgUrl").val();
	                	var goodsImg = "";
	                	for(var i =0;i<goodId.split(",").length;i++){
	                		goodsImg+='<img alt="图片加载出错" src="'+goodImgUrl.split(",")[i]+'" title="'+goodName.split(",")[i]+'">';
	                	}
	                	$("#goods_img").html(goodsImg);
	                	$("#goods_id").val(goodId);
	                	layer.closeAll();
	                }
					,zIndex: layer.zIndex //重点1
	                ,success: function(layero){
	                	layer.setTop(layero); //重点2
	                }
                });
			});
		}
	}
})(window, document);