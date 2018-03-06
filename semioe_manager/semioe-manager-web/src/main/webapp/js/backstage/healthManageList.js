layui.use(['form','laypage','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,layer = layui.layer;
    var pageSum = 1;
    
    var implementId = window.utils.getRequestParam("type");
    getDisease();
    //获取服务类型
    function getDisease(){
    	$.get('/api/contracte/queryDictionary?typeId=31',function(res){
			if(res.successful){
				if(res.data){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.itemId+'">'+item.itemName+'</option>'
					})
					$('#serviceType').html('<option value="all">全部</option>').append(option);
					form.render();
				}
			}else{
				layer.msg('服务分类获取失败！');
			}
		})
    };
    //健康管理列表查询
    function getData(pageNum,pageSize){
    	var obj = new Object();
    	obj.implementId = "";
	    obj.title = $("#contentName").val();
    	obj.serviceType = $("#serviceType").val()=="all"?"":$("#serviceType").val();
	    obj.currentPage = pageNum?pageNum:1;
	    obj.id = '';
	    obj.showCount = pageSize?pageSize:10;
	    $.ajax({
	        url:'/implementDetail/queryByEntity',
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
	            		var serviceTypeDesc = list[i].serviceTypeDesc?list[i].serviceTypeDesc:"";
	            		str+='<tr><td>'+serviceTypeDesc+'</td>';
	            		
	            		var description = list[i].description?list[i].description:"";
	            		str+='<td>'+description+'</td>';
	            		var displayOrder = list[i].displayOrder?list[i].displayOrder:"";
	            		str+='<td>'+displayOrder+'</td>';
	            		var title = list[i].title?list[i].title:"";
	            		str+='<td>'+title+'</td>';
	        			str+='<td><a class="layui-btn layui-btn-small" href="addHealthManage.html?id='+list[i].id+'" class="service_check">编 辑</a>';
	        			str+='<a class="layui-btn layui-btn-small layui-btn-danger service_delete" data-id="'+list[i].id+'">删 除</a></td></tr>';
	            	}
	            	$(".layui-table tbody").html(str);
	            	form.render();
	            	$(".service_delete").on("click",function(){
	            		var id = $(this).attr("data-id");
	            		$.get('/implementDetail/removeDetail?id='+id,function(res){
	            			if(res.successful){
            					layer.msg('删除成功！',{time:2000},function(){
            						window.location.reload();
            					});
	            			}else{
	            				layer.msg('删除失败！');
	            			}
	            		})
	            	});
	            }
	            return pageSum;
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }
    //分页
    pagging();
    function pagging(){
    	layer.open({type: 3,content: ""});
    	var obj = new Object();
    	obj.implementId = "";
	    obj.title = $("#contentName").val();
    	obj.serviceType = $("#serviceType").val()=="all"?"":$("#serviceType").val();
    	obj.id = '';
	    obj.currentPage = 1;
	    obj.showCount = 1000;
	    $.ajax({
	        url:'/implementDetail/queryByEntity',
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
        				layer.open({type: 3,content: ""});
        				//得到了当前页，用于向服务端请求对应数据
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
    //搜索
    $("#search").on("click",function(){
    	pagging();
    })
});
