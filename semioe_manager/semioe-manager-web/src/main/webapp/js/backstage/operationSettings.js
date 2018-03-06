window.onload=function(){
layui.use(['element','layer','form'],function(){
    var $=layui.jquery,layer = layui.layer,form = layui.form(),element=layui.element();
    var layid = location.hash.replace(/^#test=/, '');
    element.tabChange('test', layid);
    element.on('tab(test)', function(elem){
        location.hash = 'test='+ $(this).attr('lay-id');
    });
    
    //tab切换
    var checkType = 0;
    getListData('Banner设置',0);
    $('.tab-ui li').on('click',function(){
		var val=$(this).html();
		checkType=$(this).attr('type');
     	getListData(val,checkType);  
    })
    var type= window.utils.getRequestParam("type");
    if(!type){
		getListData('Banner设置',0);
    }else{
    	$('.tab-ui li').each(function(){
        	if($(this).attr("type") == type){
        		$(this).click();
        	}
    	})
    }
    //获取商品
	function getGoods(){
    	var obj=new Object();
    	obj.goodsName='';obj.supplierName='';obj.type=null;obj.keywordsIds=null;obj.keywordsNames=null;obj.showCount=100;obj.currentPage=1;obj.type=1;
    	var dataJson=$.toJSON(obj);
        $.ajax({
        	url:'/goodsInfo/getGoodsInfoArray',
        	type:'POST',
        	data:dataJson,
        	contentType: "application/json",
        	success:function(items){
        		var str='',data=items.items;
        		$.each(data,function(index,item){
        			str+='<option value="'+item.id+'">'+item.goodsName+'</option>'
        		})
        		$('select[name="chance_commodity"]').html('<option value="">选择商品</option>').append(str);
        		form.render();
        	},
        	error:function(error){
        		layer.msg('加载失败');
        	}
        })
       
    }
  	//获取user_id
    var cookieVal = window.utils.getCookie("user_id");
    //获取服务
    function getService(){
        $.ajax({
            	url:'/service/queryAll?createUserId='+cookieVal,   
            	type:'GET',
            	success:function(data){
            		if(data.successful){
            			var str='',data=data.data;
                		$.each(data,function(index,item){
                			str+='<option value="'+item.id+'">'+item.name+'</option>'
                		})
                		$('select[name="chance_service"]').html('<option value="">请选择服务</option>').append(str);
                		form.render();
            		}
            	},
            	error:function(error){
            		layer.msg('加载失败');
            	}
        })
    }
    getGoods();
   	getService();
    //选择商品
    form.on('select(chance_commodity)', function(data){
		 var goodsVal=data.elem[data.elem.selectedIndex].text;
		 var goodsId='/goodsInfo/getGoodsInfoById?id='+data.value;  
		 $("#address").val(goodsId);
    })
    //选中服务
    form.on('select(chance_service)', function(data){
		 var serviceVal=data.elem[data.elem.selectedIndex].text;
		 var serviceId='/service/queryGoodsById?id='+data.value;   
		 $("#address").val(serviceId);
   })
   //列表
   function getListData(val,type){
    	$.ajax({
    		url:'/implement/query?type='+type,
    		type:'GET',
    		success:function(data){
    			var str='';
    			if(data.successful){
    				var data=data.data;
    				if(data.length <=0){
    					layer.msg("暂无数据!");
    				}else{
    					if(type == 3){
    						$("#addLogo").hide();
    						$('.list-table thead tr').html('<th>内容区名称</th><th style="width: 200px;">操作</th>');
    						$.each(data,function(index,item){
    							str+='<tr id="'+item.id+'"><td>'+item.name+'</td><td style="width: 200px;">';
    							if(item.name == "健康管理"){
    								str+='<a href="healthManageList.html?type='+item.id+'" class="layui-btn layui-btn-small">查看</a>';
    							}else{
    								str+='<a href="personalSetList.html?type='+item.id+'" class="layui-btn layui-btn-small">查看</a>';
    							}
    							/*str+='<button class="layui-btn layui-btn-danger layui-btn-small delBtn">删除</button>';*/
    							str+='<input type="hidden" class="tabId" value="'+item.id+'"/></td></tr>'
    						})
    					}else{
    						$('.list-table thead tr').html('<th>图片</th><th>地址</th><th>操作</th>');
    						$.each(data,function(index,item){
    							str+='<tr id="'+item.id+'"><td><img src="'+item.imgUrl+'" class="imgUrl"></td>';
    							str+='<td>'+item.name+'</td><td>';
    							if(type == 1){
    								$('.list-table th').eq(1).html('标题');
    								str+='<button class="layui-btn layui-btn-small editBtn">编辑</button>'
    							}else{
    								$('.list-table th').eq(1).html('地址');
    							}
    							str+='<button class="layui-btn layui-btn-danger layui-btn-small delBtn">删除</button>';
    							str+='<input type="hidden" class="tabId" value="'+item.id+'"/></td></tr>'
    						})
    						if(data.length>=5){
    	                		$("#addLogo").hide();
    	                	}else{
    	                		$("#addLogo").show();
    	                	}
    					}
    				}
    				$(".list-table tbody").html(str);
    			}else{
        			layer.msg("列表获取失败!");
    			}
    		},
    		error:function(error){
    			layer.msg("列表获取失败!");
    		}
    	})
    }
   
    //点击添加
    var active = {
        setTop: function(){
            var that = this;
        }
        ,offset: function(othis){
            layer.open({
                type: 1
                ,title:['添加','font-size:18px;background-color:#eee;']
                ,id: 'LAY_demo'
                ,content: $("#addUser")
                ,btn: ['确定','取消']
                ,shade: 0.8
                ,area: ['600px','450px']
                ,yes: function(index,layero){
                	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
                	var roleId = $(".layui-tab-title.tab-title.tab-ui li.layui-this").attr("type");//位置
                    var address=$.trim($("#address").val());//地址
                    var chance_commodity=$("#chance_commodity").val();//商品
                    var chance_service=$("#chance_service").val();//服务
                    if(roleId==''){
                    	layer.msg('请选择类型');$('.opa0').remove();
                    	return;
                    }
                		//图片上传
                    var imgUrl = uploadImg('uploadFile');
                    if(imgUrl==''){
	                    	layer.msg('图片不能为空');$('.opa0').remove();
	                    	return;
                    }
					var data={name:address,type:roleId,imgUrl:imgUrl}
                    $.ajax({
                    	url:'/implement/add',
                    	type:'POST',
                    	data:$.toJSON(data),
                    	dataType:"json",
                        contentType: "application/json",
                        success:function(data){
                        	$('.opa0').remove();
                        	if(data.successful){
                        		var type= $(".layui-tab-title.tab-title.tab-ui li.layui-this").attr("type");
                        		layer.closeAll();
                            	layer.msg('添加成功',{time: 2000},function(){
                            		window.location.href="operationSettings.html?type="+type;
                            	});
                        	}else{
                        		layer.msg(data.resultCode.message);
                        	}
                        	
                        },
                        error:function(error){
                        	$('.opa0').remove();
                    		layer.msg('添加失败!');
                        }
                    })
                }
	            ,btn2: function(){
	                layer.closeAll();
	                $('#addUser select,#addUser input[type="text"]').val('');
	                $('#addUser img').attr('src','');
	                form.render();
	            }
	            ,cancel:function(){
	            		layer.closeAll();
	            		$('#addUser select,#addUser input[type="text"]').val('');
	            		$('#addUser img').attr('src','');
	            		form.render();
	            }
            });
        }
    };
    function uploadImg(ele){
    		var formData = new FormData(document.getElementById(ele));
    		var imgUrl = '';
		$.ajax({
			type: "POST",
			url: "/file/upload",
			data: formData,
			async: false,
			cache: false,
			processData: false,
			contentType: false,
			dataType: "json",
			success: function(res) {
				$('.opa0').remove();
				if(res.successful){
					imgUrl = res.data;
				}else{
					layer.msg(res.resultCode.message);
					$('.opa0').remove();
					imgUrl = '';
				}
			},
			error: function(error) {
				$('.opa0').remove();
				layer.msg("图片上传到服务器失败");
				imgUrl = '';
			}
		});
		return imgUrl;
    }

    //实例化编辑器 
    var editorOption = {
      	    initialFrameWidth: 495,
      	    initialFrameHeight:250,
      	    elementPathEnabled:false,
//      	    wordCount:false,
      	    zIndex:20000000
      	};
//    var ue = UE.getEditor('discribe')
  	
    //添加
    $('#addLogo').on('click', function(){
        var othis = $(this), method = othis.data('method');
        if(checkType == 1){
        		isEdit=false;
        		addFinder('');
	        	return false;
        }
        active[method] ? active[method].call(this, othis) : '';
        form.render();
    });
//	编辑
	$('.layui-table').delegate('.editBtn','click',function(){
		var tId = $(this).parents('tr').attr('id');
		isEdit=true;
		var editor = new baidu.editor.ui.Editor(editorOption);
		editor.render('discribe');
	 	$.ajax({
	    		url:'/implement/queryDetailFind',
	    		type:'POST',
	    		async:false,
	    		data: $.toJSON({id:tId}),
			dataType: "json",
			contentType: "application/json",
	    		success:function(res){
	    			var str='';
	    			if(res.successful){
	    				var data=res.data;
	    				implementList = data.implementList;
	    				$('#addFinder .imgBox img').attr('src',data.imgUrl);
	    				$('#title').val(data.name);
	    				var liStr = '';
	    				$.each(data.implementList,function(index,item){
	    					liStr += '<li id="'+item.id+'"><img src="'+item.imgUrl+'" /><span>地址：'+item.name+'</span><button class="layui-btn layui-btn-small layui-btn-primary delSetBtn"><i class="layui-icon" style="color:#36a094; font-size:24px !important;">&#xe640;</i></button></li>';
	    				})
	    				$('.setList').html(liStr);
	    			}else{
	        			layer.msg("详细信息查询失败!");
	    			}

    				editor.ready(function() {//编辑器初始化完成再赋值  
    					editor.setContent(data.description); //赋值给UEditor  
                }); 
	    		},
	    		error:function(error){
	    			layer.msg("详细信息查询失败!");
	    		}
	    	})
	    	$('.disNone').show();
	    	addFinder(tId);
	})
	//	添加发现
	var implementList = new Array(),num=0,isEdit=false;
	function addFinder(tId){
		layer.open({
            type: 1
            ,title:['添加','font-size:18px;background-color:#eee;']
            ,id: 'LAY_demo'
            ,content: $("#addFinder")
            ,btn: ['确定','取消']
            ,shade: 0.8
            ,area: ['600px','450px']
            ,yes: function(index,layero){
            		var title = $.trim($('#title').val());
            		var description = isEdit && UE.getEditor('discribe').getContent();
            		//图片上传
            		var imgUrl = uploadImg('uploadFile1');
                 if(imgUrl==''){
  	                	layer.msg('顶图图片不能为空');$('.opa0').remove();
  	                	return;
                 }
            		if(title==''){
	                	layer.msg('标题不能为空');$('.opa0').remove();
	                	return;
                }
            		$('.layui-layer-btn').prepend('<span class="opa0"></span>');
            		var sendUrl = isEdit ? '/implement/addDetailFind' : '/implement/addFind';
            		var sendData = isEdit ? {id:tId,name:title,imgUrl:imgUrl,description:description,implementList:implementList} : {name:title,imgUrl:imgUrl};
	            	$.ajax({
                    	url:sendUrl,
                    	type:'POST',
                    	data:$.toJSON(sendData),
                    	dataType:"json",
                    contentType: "application/json",
                    success:function(data){
	                    	$('.opa0').remove();
	                    	if(data.successful){
	                    		layer.closeAll();
	                        	layer.msg(isEdit?'编辑成功':'添加成功',{time: 2000},function(){
	                        		window.location.href="operationSettings.html?type="+checkType;
	                        	});
	                    	}else{
	                    		layer.msg(data.resultCode.message);
	                    	}
                    	
                    },
                    error:function(error){
                    	$('.opa0').remove();
                		layer.msg(isEdit?'编辑失败':'添加失败!');
                    }
                })
            	}
	        ,btn2: function(){
	            layer.closeAll();
	            $('#addFinder select,#addFinder input[type="text"]').val('');
	        		$('#addFinder img').attr('src','');
	        		$('#addFinder .disNone').hide();
	        		$('#discribe').html('');
	        		form.render();
	        }
	        ,cancel:function(){
	        		layer.closeAll();
	        		$('#addFinder select,#addFinder input[type="text"]').val('');
            		$('#addFinder img').attr('src','');
            		$('#addFinder .disNone').hide();
            		$('#discribe').html('');
            		form.render();
	        }
        	})
	}
	//添加推荐设置
	$('#addBtn').on('click',function(){
		layer.open({
			 type: 1
			,title:['编辑','font-size:18px;background-color:#eee;']
			,id: 'LAY_demo1'
			,content: $("#addUser")
			,btn: ['确定','取消']
			,btnAlign: 'c'
			,shade: 0.8
			,area: ['600px','450px']
			,yes: function(index,layero){
				var address=$.trim($("#address").val());//地址
				var imgUrl = uploadImg('uploadFile');
				if(imgUrl==''){
	                	layer.msg('推荐设置图片不能为空');$('.opa0').remove();
	                	return;
				}
				var addObj = new Object();
				addObj.name = address;
				addObj.type = checkType;
				addObj.imgUrl = imgUrl;
				implementList.push(addObj);
//				console.log(implementList,'-----')
				num++;
				$('.setList').append('<li sId="s'+num+'"><img src="'+imgUrl+'" /><span>地址：'+address+'</span><button class="layui-btn layui-btn-small layui-btn-primary delSetBtn"><i class="layui-icon" style="color:#36a094; font-size:24px !important;">&#xe640;</i></button></li>');
				layer.close(index);
				$('#addUser img').attr('src','');
				$('#addUser select,#addUser input[type="text"]').val('');
				form.render();
			}
			,btn2: function(index){
				layer.close(index);
                $('#addUser select,#addUser input[type="text"]').val('');
                $('#addUser img').attr('src','');
                form.render();
            }
            ,cancel:function(index){
            		layer.close(index);
            		$('#addUser select,#addUser input[type="text"]').val('');
            		$('#addUser img').attr('src','');
            		form.render();
            }
		})
	})
	//删除推荐设置
	$('.setList').delegate('.delSetBtn','click',function(){
		var $par = $(this).parents('li');
		var id = $par.attr('id');
		var sId = $par.attr('sId');
		$par.remove();
		$.each(implementList,function(index,item){
			if(id==item.id || sId==item.id){
				implementList.splice(index,1);
			}
		})
	})
    //修改----目前版本没有此功能
    /*$('.layui-table').delegate('.revise','click',function(){
    	var imgUrl = $(this).parents("tr").find("img")[0].src;
    	var type = $(".layui-tab-title.tab-title.tab-ui li.layui-this").attr("type");
    	var address = $(this).parents("tr").children("td:eq(1)").text();
    	layer.open({
             type: 1
            ,title:['修改','font-size:18px;background-color:#eee;']
            ,id: 'LAY_demo'
            ,content: $("#addUser")
            ,btn: ['确定','取消']
            ,btnAlign: 'c'
            ,shade: 0.8
            ,area: ['600px','500px']
            ,yes: function(index,layero){
	            	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
	            	var roleId = type;//位置
                var address=$.trim($("#address").val());//地址
                var chance_commodity=$("#chance_commodity").val();//商品
                var chance_service=$("#chance_service").val();//服务
                if(roleId==''){
	                	layer.msg('请选择类型');$('.opa0').remove();
	                	return;
                }
            		//图片上传
                var imgUrl = uploadImg('uploadFile')
                if(imgUrl==''){
                	layer.msg('图片不能为空');$('.opa0').remove();
                	return;
                }
				var data={name:address,type:roleId,imgUrl:imgUrl}
                $.ajax({
                	url:'/implement/update',
                	type:'POST',
                	data:$.toJSON(data),
                	dataType:"json",
                    contentType: "application/json",
                    success:function(data){
                    	layer.close(layer.index);
                    	if(data.successful){
                    		layer.msg('添加成功!');
                    		setTimeout(function(){
            				window.location.reload(true)
                		},1500)
                    	}else{
                    		layer.msg(data.resultCode.message);
                    	}
                    	
                    },
                    error:function(error){
                    	layer.close(layer.index);
                		layer.msg('添加失败!');
                    }
                })
            }
            ,btn2: function(){
                layer.closeAll();
                $('#addFinder,#addUser').reload();
            }
            ,cancel:function(){
            		layer.closeAll();
            		$('#addFinder,#addUser').reload();
            }
            ,success: function(){
	            	$("#address").val(address);
	            	$("#search-audit-status").val(type).hide();
	            	form.render("select");
            }
        });
    });
	*/
    //删除
    $('.layui-table').delegate('.delBtn','click',function(){
    		var tabId = $(this).next('.tabId').val();
	    	layer.confirm('确认要删除？',{ 
	        	btn:['确认','取消']
	        },function(){
	        	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
	    		$.ajax({
	    			url:'/implement/remove?id='+tabId,
	    			type:'POST',
	    			dataType:"json",
	                contentType: "application/json",
	                success:function(data){
	                	$('.opa0').remove();
	                    if(data.successful){
	                    	var type= $(".layui-tab-title.tab-title.tab-ui li.layui-this").attr("type");
	                    	var val= $(".layui-tab-title.tab-title.tab-ui li.layui-this").text();
	                    	layer.msg('删除成功');
	                    	getListData(val,type);
	                    }
	                }
	    			,error:function(error){
	    				$('.opa0').remove();
	    				layer.msg('删除失败');
	    			}
	    		})
	    	})
    })
        
    //上传图片
    $('#upload,#upload1').on('change',function(){
    		var parId = $(this).attr('id');
    		var ele = parId == 'upload' ? 'addUser' : 'addFinder';
		preview(this,ele);
    })
	//图片预览，兼容各个浏览器
	function preview(file,ele) {
		if(file.files && file.files[0]) {
			var reader = new FileReader();
			reader.onload = function(evt) {
				$('#'+ele+' .imgBox img').attr('src', evt.target.result);
			}
			reader.readAsDataURL(file.files[0]);
		} else {
			$('#'+ele+' .imgBox img').attr('src', file.value);
		}
	};
}) 
}
