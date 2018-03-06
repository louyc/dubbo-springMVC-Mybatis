layui.use(['form','laypage','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,layer = layui.layer;
    var managerId = window.utils.getCookie("managerId");
    
    function getData(pageNum,pageSize){
    	var obj = new Object();
    	obj.serviceName = $("#serviceName").val();
	    obj.shareName = $("#shareName").val();
	    obj.orgId = managerId;
	    obj.currentPage = pageNum;
	    obj.showCount = pageSize;
	    $.ajax({
	    	url:'/shareService/getShareServices',
            type:'POST',
            data: $.toJSON(obj),
            dataType:"json",
            contentType: "application/json",
            success : function(data){
            	layer.close(layer.index);//取消遮罩
            	var list = data.items;
                if(data.resultCode.code=="SUCCESS"){
                	var str = "";
                	for(var i=0;i<list.length;i++){
                		var serviceName = list[i].serviceName?list[i].serviceName:"";
                		str+='<tr><td>'+serviceName+'</td>';
                		var shareName = list[i].shareName?list[i].shareName:"";
                		str+='<td>'+shareName+'</td>';
                		var description = list[i].description?list[i].description:"";
                		str+='<td>'+description+'</td>';
            			str+='<td><a class="layui-btn layui-btn-danger layui-btn-small deleteShare" href="#" data-id="'+list[i].id+'">删 除</a></td></tr>'
                	}
                	$("#table_chooseed tbody").html(str);
                	//删除共享服务
                    $(".deleteShare").on("click",function(){
                    	var obj = new Object();
                     	obj.id = $(this).attr("data-id");
                     	$.ajax({
                 	    	url:'/shareService/removeShareServices',
                             type:'POST',
                             data: $.toJSON(obj),
                             dataType:"json",
                             contentType: "application/json",
                 	        success : function(data){
                 	        	layer.close(layer.index);//取消遮罩
                 	        	if(data.successful){
                 	        		layer.msg('删除成功！',{time: 2000},function(){
                 	        			window.location.reload();
                 	        		});
                 	        	}else{
                 	        		layer.msg('删除失败！');
                 	        	}
                 	        },
                 	        error : function(data){
                 	        	layer.close(layer.index);//取消遮罩
                 	        	layer.msg('删除失败！');
                 	        },
                 	    });
                    })
                	form.render();
                }
            },
            error : function(data){
            	layer.close(layer.index);//取消遮罩
            	layer.msg('失败！');
            },
        });
    };
    //分页
    pagging();
    function pagging(){
    	layer.open({type: 3,content: ""});
    	var obj = new Object();
    	obj.pageSize = 1000;
    	obj.serviceName = $("#serviceName").val();
	    obj.shareName = $("#shareName").val();
	    obj.orgId = managerId;
	    obj.pageNum = 1;
	    $.ajax({
	    	url:'/shareService/getShareServices',
            type:'POST',
            data: $.toJSON(obj),
            dataType:"json",
            contentType: "application/json",
	        success : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	var pageSum = data.items.length;
	        	laypage({
	        		cont: 'pagging'
        			, pages: Math.ceil(pageSum/10)
        			, skip: false
        			,jump: function(obj, first){
        				//得到了当前页，用于向服务端请求对应数据
        				layer.open({type: 3,content: ""});
        				pageNum = obj.curr;
        				getData(pageNum,10);
        			}
	        	});
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }
    function _getData(pageNum,pageSize){
    	var obj = new Object();
    	obj.showCount = pageSize;
    	obj.name = $("#name").val();
	    obj.createName = $("#createName").val();
	    obj.serviceShare = 1;
	    obj.currentPage = pageNum;
	    $.ajax({
	    	url:'/service/getAllShareServices',
            type:'POST',
            data: $.toJSON(obj),
            dataType:"json",
            contentType: "application/json",
            success : function(data){
            	var list = data.items;
                if(data.resultCode.code=="SUCCESS"){
                	var str = "";
                	for(var i=0;i<list.length;i++){
                		str+='<tr><td><input type="checkbox" name="" data-id="'+list[i].id+'" lay-skin="primary"></td>';
                		var serviceName = list[i].serviceName?list[i].serviceName:"";
                		str+='<td>'+serviceName+'</td>';
                		var shareName = list[i].shareName?list[i].shareName:"";
                		str+='<td>'+shareName+'</td>';
                		var description = list[i].description?list[i].description:"";
                		str+='<td>'+description+'</td>';
                	}
                	$("#table_choose tbody").html(str);
                	form.render();
                	//全选
	            	form.on('checkbox(allChoose)', function(data){
	            	    var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');
	            	    child.each(function(index, item){
	            	      item.checked = data.elem.checked;
	            	    });
	            	    form.render('checkbox');
	            	});
                }
            },
            error : function(data){
            	layer.msg('失败！');
            },
        });
    };
    //弹出分页
    _pagging();
    function _pagging(){
    	var obj = new Object();
    	obj.showCount = 1000;
    	obj.name = $("#name").val();
    	obj.serviceShare = 1;
	    obj.createName = $("#createName").val();
	    obj.currentPage = 1;
	    $.ajax({
	    	url:'/service/getAllShareServices',
            type:'POST',
            data: $.toJSON(obj),
            dataType:"json",
            contentType: "application/json",
	        success : function(data){
	        	var pageSum = data.items.length;
	        	laypage({
	        		cont: '_pagging'
        			, pages: Math.ceil(pageSum/10)
        			, skip: false
        			,jump: function(obj, first){
        				//得到了当前页，用于向服务端请求对应数据
        				layer.open({type: 3,content: ""});
        				pageNum = obj.curr;
        				_getData(pageNum,10);
        			}
	        	});
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }
    //搜索
    $("#search").on("click",function(){
    	pagging();
    })
    //共享服务的搜索
    $("#_search").on("click",function(){
    	_pagging();
    })
    //选择服务
    $("#chooseService").on("click",function(){
    	layer.open({
    		type:1
    		,title:'选择服务'
    		,content: $('#add_service')
			,area: ['1000px','450px']
    	})
    })
    //确定选择服务
    $("#_chooseService").on("click",function(){
    	var serviceIds = "";
    	$("#table_choose tbody tr").each(function(){
    		var checkInput = $(this).find(".layui-form-checkbox")[0];
    		var dataid = $(this).children("td:eq(0)").children("input[type='checkbox']")
    		if(checkInput.className.indexOf("layui-form-checked") >=0){
    			if(serviceIds){
    				serviceIds+=","
    			}
    			serviceIds += dataid.attr("data-id");
    		}
    	});
    	if(serviceIds==""){
    		layer.msg('请选择服务！'); return;
    	}
    	var obj = new Object();
    	obj.idList = serviceIds;
    	obj.orgId =  window.utils.getCookie("user_id");
    	$.ajax({
	    	url:'/shareService/addShareServices',
            type:'POST',
            data: $.toJSON(obj),
            dataType:"json",
            contentType: "application/json",
	        success : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	if(data.successful){
	        		layer.msg('添加成功！',{time: 2000},function(){
	        			window.location.reload();
	        		});
	        	}else{
	        		layer.msg('添加失败！');
	        	}
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('删除失败！');
	        },
	    });
    })
});
