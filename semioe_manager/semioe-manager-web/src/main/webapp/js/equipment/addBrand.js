layui.use(['element','form','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form(),element = layui.element();
    var layer = layui.layer;
    /*$('#layui-breadcrumb', parent.document).children("a").children("cite").text("添加品牌");
	$('#menuList dd', parent.document).removeClass("layui-this");*/
    //获取url上面的参数
    function GetQueryString(name){
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = window.location.search.substr(1).match(reg);
         if(r!=null)return  unescape(r[2]); return null;
    }
    //加载厂商select
    $.ajax({
        url:'/deviceFirms/getDeviceFirms',
        type:'POST',
        data:{},
        dataType:"json",
        success : function(data){
        	var list = data.data;
        	var str = "";
        	str+='<option value="noFirm">请选择</option>';
        	for(var i=0;i<list.length;i++){
        		str+='<option value="'+list[i].id+'">'+list[i].firmName+'</option>';
        	}
        	$("#deviceFirm").html(str);
        	form.render();
        },
        error : function(data){
        	layer.msg('厂商数据加载失败！');
        },
    });
    //如果id不为空，则为修改
    var id = GetQueryString("id");
    //监听提交
    form.on('submit(formDemo)', function(data){
    	$(".layui-btn").addClass("layui-btn-disabled");
    	var obj = new Object();
        obj.brandName = $("#brandName").val();//名称
        obj.brandDesc = $("#brandDesc").val();//描述
        obj.deviceFirmid = $("#deviceFirm").val()!="noFirm"?$("#deviceFirm").val():"";//地址
        if(!obj.brandName){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("品牌名称不能为空！");return false;
        }
        if(obj.brandName.length > 16){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("品牌名称长度不能超过20！");return false;
        }
        if(!obj.brandDesc){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("品牌描述不能为空！");return false;
        }
        if(obj.brandDesc.length > 50){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("品牌描述长度不能超过60！");return false;
        }
        if(!obj.deviceFirmid){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商不能为空！");return false;
        }
        if(!!id){//修改
        	obj.id=id;
        	$.ajax({
                url:'/deviceBrands/updateDeviceBrand',
                type:'POST',
                data:$.toJSON(obj),
        		dataType:"json",
        		contentType: "application/json",
                /*data:{'id': id,'brandName':obj.brandName,'brandDesc':obj.brandDesc,'deviceFirmid ':obj.deviceFirm},
                dataType:"json",*/
                success : function(data){
                	$(".layui-btn").removeClass("layui-btn-disabled");
                	layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="brandList.html";
                	});
                },
                error : function(data){
                	$(".layui-btn").removeClass("layui-btn-disabled");
                	layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="brandList.html";
                	});
                },
            });
        }else{//新增
        	$.ajax({
                url:'/deviceBrands/insertDeviceBrand',
                type:'POST',
                data:$.toJSON(obj),
//                data:{'brandName':obj.brandName,'brandDesc':obj.brandDesc,'deviceFirmid':obj.deviceFirmid},
        		dataType:"json",
        		contentType: "application/json",
                success : function(data){
                	$(".layui-btn").removeClass("layui-btn-disabled");
                	layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="brandList.html";
                	});
                },
                error : function(data){
                	$(".layui-btn").removeClass("layui-btn-disabled");
                	layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="brandList.html";
                	});
                },
            });
        }
        return false;
    });
    //带出数据
    if(id && id!=0){
    	layer.open({type: 3,content: ""});
    	var obj = new Object();
		obj.id = id;//名称
    	$.ajax({
            url:'/deviceBrands/getDeviceBrandById?id='+id,
            type:'POST',
            dataType:"json",
            contentType: "application/json",
            success : function(data){
            	layer.close(layer.index);//取消遮罩
            	var list = data.data;
                if(data.resultCode.code=="SUCCESS"){
                	$("#brandName").val(list.brandName?list.brandName:"");//名称
                    $("#brandDesc").val(list.brandDesc?list.brandDesc:"");//描述
                    $("#deviceFirm").val(list.deviceFirmid?list.deviceFirmid:"");//厂商
                    form.render("select");
                }
            },
            error : function(data){
            	layer.close(layer.index);//取消遮罩
            	layer.msg('失败！');
            },
        });
    }
    
    /*删除
    $("#deleteVender").on('click', function(){
    	if(id && id!=0){
    		$.ajax({
                url:'/deviceFirms/deleteDeviceFirm?id='+id,
                type:'POST',
                dataType:"json",
                contentType: "application/json",
                success : function(data){
                	layer.msg('删除成功',function(){window.history.go(-1)});
                },
                error : function(data){
                	layer.msg('失败！');
                },
            });
    	}
    });*/
});
