layui.use(['element', 'form', 'layer', 'jquery'], function() {
	var $ = layui.jquery,form = layui.form(),element = layui.element();
	var layer = layui.layer;
	
    element.on('tab(docDemoTabBrief)', function(data){
	   var status = $(".layui-tab-title li:eq("+data.index+")").children("input").val();
	   loadService(status);
	});
    var roleId = window.utils.getCookie("roleId");
    var src = "";
    if(roleId == 3){
    	src = "/jyservice/query";
    }else{
    	src = "/service/query";
    }
    loadService("");
    function loadService(status){
    	var id = window.utils.getCookie("managerId");
    	var parameter = "";
    	if(status == "share"){
    		parameter = '?id='+id+'&name=&serviceShare=1&status=&createName=&pageNumber=1&pageSize=1000';
    	}else{
    		parameter = '?id='+id+'&name=&serviceShare=&status='+status+'&createName=&pageNumber=1&pageSize=1000';
    	}
    	$.ajax({
	    	url:src+parameter,
	        type:'POST',
	        success : function(data){
	        	if(data.successful){
	        		if(data.items.length<=0){
	        			layer.msg('暂无数据！');
	        			$(".layui-form").html("");
	        			return false;
	        		}
	        		var list = data.items;
	        		var tbody = "";
	        		for(var i=0;i<list.length;i++){
						tbody+='<div class="service_box">';
						tbody+='<div class="service_name_box">';
						tbody+='<div class="service_name">'+list[i].name+'</div></div>';
						tbody+='<div class="check_delete">';
						tbody+='<a href="addService.html?id='+list[i].id+'" class="service_check">查 看</a>';
						tbody+='<a href="#" data-id="'+list[i].id+'" class="service_delete">删 除</a></div>';
						tbody+='<a href="#" class="handle">';
						if(list[i].status == 2){
							tbody+='<div class="up_down_img" onclick="examineService(3,this)" data-id="'+list[i].id+'" >';
							tbody+='<img src="../../image/service_download.png" alt=""/>';
							tbody+='<div class="up_down_text">下架</div></div>';
						}else{
							tbody+='<div class="up_down_img" onclick="examineService(1,this)" data-id="'+list[i].id+'" >';
							tbody+='<img src="../../image/service_upload.png" alt=""/>';
							tbody+='<div class="up_down_text">上架</div></div>';
						}
						if(list[i].serviceShare == 0){
							tbody+='<div class="up_down_img" onclick="shareService(1,this)" data-id="'+list[i].id+'">';
							tbody+='<img src="../../image/share.png" alt=""/>';
							tbody+='<div class="up_down_text">共享</div></div>';
						}else if(list[i].serviceShare == 1){
							tbody+='<div class="up_down_img" onclick="shareService(2,this)" data-id="'+list[i].id+'">';
							tbody+='<img src="../../image/noShare.png" alt=""/>';
							tbody+='<div class="up_down_text">取消共享</div></div>';
						}
						tbody+='</a></div>';
        			}
	        		$(".layui-form").html(tbody);
	        		$(".service_box").each(function(){
	        	        var width = $(this).css("width");
	        	        $(this).css("height",width);
	        	    })
	        	    $(".service_name_box,.handle").hover(function(){
	        	        $(this).parent(".service_box").children(".handle").css("top","0");
	        	    },function(){
	        	        $(this).parent(".service_box").children(".handle").css("top","100%");
	        	    });
	        		$(".service_delete").on("click",function(){
	        			var id = $(this).attr("data-id");
	        			var _this = $(this);
	        			var updownService = $(this).parents(".check_delete").next("a").find(".up_down_text").html();
	        			if(updownService == "下架"){
	        				layer.msg("服务下架后才能被删除！");
	        				return;
	        			}
	        			layer.confirm('确定删除当前服务？', {
	                        shadeClose: true, //开启遮罩关闭
	                        btn: ['确定', '取消'] //按钮
	                    }, function() {
	                    	$(".layui-layer-btn a").addClass("layui-btn-disabled");
	                    	$.post('/service/remove?id='+id,function(res){
	                    		$(".layui-layer-btn a").removeClass("layui-btn-disabled");
		        				if(res.successful){
		        					layer.msg("删除成功！");
		        					_this.parents(".service_box").remove();
		        				}else{
		        					layer.msg(res.resultCode.message);
		        				}
		        			})
	                    }, function() {
	                    	layer.closeAll();
	                    });
	        		})
	        	}else{
	        		layer.msg(data.resultCode.message);
	        	}
	        },
	        error : function(data){
	        	layer.msg('查询失败！');
	        },
	    });
    }
    
});
