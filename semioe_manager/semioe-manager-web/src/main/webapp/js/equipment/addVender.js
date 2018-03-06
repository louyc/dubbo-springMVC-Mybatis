layui.use(['element','form','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form(),element = layui.element();
    var layer = layui.layer;
    /*$('#layui-breadcrumb', parent.document).children("a").children("cite").text("添加厂商");
	$('#menuList dd', parent.document).removeClass("layui-this");*/
    //监听提交
    form.on('submit(formDemo)', function(data){
    	$(".layui-btn").addClass("layui-btn-disabled");
    	var obj = new Object();
        obj.firmName = $("#firmName").val();//名称
        obj.firmDesc = $("#firmDesc").val();//描述
        obj.firmAdress = $("#firmAdress").val();//地址
        obj.firmPostcode = $("#firmPostcode").val();//邮编
        obj.firmContact = $("#firmContact").val();//厂商联系人
        obj.firmPhone = $("#firmPhone").val();//厂商联系方式
        obj.firmResponsible = $("#firmResponsible").val();//本部联系厂商负责人
        obj.firmResponsiblePhone = $("#firmResponsiblePhone").val();//本部联系厂商负责人联系方式
        if(!obj.firmName){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商名称不能为空！");return false;
        }
        if(obj.firmName.length > 30){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商名称的长度不能为超过30！");return false;
        }
        if(!obj.firmDesc){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商描述不能为空！");return false;
        }
        if(obj.firmDesc > 60){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商描述的长度不能为超过60！");return false;
        }
        if(!obj.firmAdress){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商地址不能为空！");return false;
        }
        if(!obj.firmContact){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商联系人不能为空！");return false;
        }
        if(!obj.firmPhone){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("厂商联系方式不能为空！");return false;
        }
        var re= /^[1-9][0-9]{5}$/
        if(!!obj.firmPostcode && !re.test(obj.firmPostcode)){
        	$(".layui-btn").removeClass("layui-btn-disabled");
        	layer.msg("请填写正确的邮编！");return false;
        }
        if(!!id){//修改
        	obj.id=id;
        	$.ajax({
        		url:'/deviceFirms/updateDeviceFirm',
        		type:'POST',
        		data:$.toJSON(obj),
        		dataType:"json",
        		contentType: "application/json",
//        		data:{'id':id,'firmName':obj.firmName,'firmDesc':obj.firmDesc,'firmAdress':obj.firmAdress,'firmPostcode':obj.firmPostcode,'firmContact':obj.firmContact,'firmPhone':obj.firmPhone,'firmResponsible':obj.firmResponsible,'firmResponsiblePhone':obj.firmResponsiblePhone},
        		success : function(data){
        			$(".layui-btn").removeClass("layui-btn-disabled");
        			layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="venderList.html";
                	});
        		},
        		error : function(data){
        			$(".layui-btn").removeClass("layui-btn-disabled");
        			layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="venderList.html";
                	});
        		},
        	});
        }else{//新增
        	$.ajax({
        		url:'/deviceFirms/insertDeviceFirm',
        		type:'POST',
        		data:$.toJSON(obj),
        		dataType:"json",
        		contentType: "application/json",
        		success : function(data){
        			$(".layui-btn").removeClass("layui-btn-disabled");
        			layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="venderList.html";
                	});
        		},
        		error : function(data){
        			$(".layui-btn").removeClass("layui-btn-disabled");
        			layer.msg(data.resultCode.message,{time: 2000},function(){
                		window.location.href="venderList.html";
                	});
        		},
        	});
        }
        return false;
    });
    //获取url上面的参数
    function GetQueryString(name){
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = window.location.search.substr(1).match(reg);
         if(r!=null)return  unescape(r[2]); return null;
    }
    //如果id不为空，则为修改
    var id = GetQueryString("id");
    if(id && id!=0){
    	layer.open({type: 3,content: ""});
    	$.ajax({
            url:'/deviceFirms/getDeviceFirmById?id='+id,
            type:'POST',
            data:{'id':id},
            dataType:"json",
            success : function(data){
            	layer.close(layer.index);//取消遮罩
            	var list = data.data;
                if(data.resultCode.code=="SUCCESS"){
                	$("#firmName").val(list.firmName?list.firmName:"");//名称
                    $("#firmDesc").val(list.firmDesc?list.firmDesc:"");//描述
                    $("#firmAdress").val(list.firmAdress?list.firmAdress:"");//地址
                    $("#firmPostcode").val(list.firmPostcode?list.firmPostcode:"");//邮编
                    $("#firmContact").val(list.firmContact?list.firmContact:"");//厂商联系人
                    $("#firmPhone").val(list.firmPhone?list.firmPhone:"");//厂商联系方式
                    $("#firmResponsible").val(list.firmResponsible?list.firmResponsible:"");//本部联系厂商负责人
                    $("#firmResponsiblePhone").val(list.firmResponsiblePhone?list.firmResponsiblePhone:"");//本部联系厂商负责人联系方式
                }
            },
            error : function(data){
            	layer.close(layer.index);//取消遮罩
            	layer.msg('失败！');
            },
        });
    }
});
