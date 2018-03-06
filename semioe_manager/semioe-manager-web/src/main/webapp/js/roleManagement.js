layui.use(['laypage', 'layer','form'], function () {
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage, layer = layui.layer;
    var obj=new Object();
    obj.itemName=$("#itemName").val();
    obj.createTime=$("#time").val();
    var dataJson = {
    	"itemName": "",
    	"createTime":''
    };
    $.ajax({
    	url: '/role/getRoleInfo',
    	type: 'POST',
        contentType: "application/json",
    	success:function(result){
    		var sHtml =template('list', {
                list : result.data
            });
            $('#roleTable tbody').html(sHtml)
    	}
    	
    })
});
var roleUpdateId = null;

function saveRole(){
	var updateName = $('#updateRoleValue_' + roleUpdateId).val();
	var obj=new Object();
	obj.itemName=updateName;
	obj.id=roleUpdateId;
	var dataJson=$.toJSON(obj);
	$.ajax({
		url:'/role/updateRoleInfo',
		type:'POST',
		data:dataJson,
		contentType: "application/json",
		success: function (result) {
			console.log(result);
            if(result.status==200 && result.resultCode.code==="SUCCESS"){
            	layer.msg('修改成功！', function(){window.location.reload()});
            }else{
            	layer.msg(result.resultCode.message, function(){window.location.reload()});
            }
            layer.closeAll();
         },
         error:function(result){
        	 layer.msg('失败！');
         }
         
	})
}
function toUpdate(id){
	roleUpdateId = id;
	$.ajax({
	     url:'/role/getRoleInfoById?id='+id,
		 type:'POST',
		 success:function(result){
		 	if(result.status == 200 && result.resultCode.code==="SUCCESS"){
		 		var content = result.data.itemName;
		 		showLayerOpen(content);
	        }
	     }
	})
}

function showLayerOpen(content){
	layer.open({
	    type: 1,
	    title:['修改','font-size:18px;background-color:#eee;'],
		content: '<input type="text"  placeholder="请输入名称"  class="layui-input" value="' + content + '" id="updateRoleValue_' + roleUpdateId + '"   style="width:80%;margin-top:15px;float:right;margin-right:70px"/><button class="layui-btn" style="float:right;margin-top:20px;margin-bottom:20px;margin-right:70px;" onclick="saveRole()">保存</button>',
		shade: 0.5,
		area: ['500px'],
		scrollbar: false,
		yes: function(){
			
		}
	});
}
function addRole(){
	layer.open({
	    type: 1,
	    title:['添加','font-size:18px;background-color:#eee;'],
		content: '<input type="text"  placeholder="请输入名称" class="layui-input" id="addName" style="width:80%;margin-top:15px;float:right;margin-right:70px"/><button class="layui-btn" style="float:right;margin-top:20px;margin-right:70px;margin-bottom:20px" onclick="saveAddRole()">保存</button>',
		shade: 0.5,
		area: ['500px'],
		scrollbar: false,
		yes: function(){
			
		}
	});
}
function saveAddRole(){
	var obj=new Object();
	obj.itemName=$("#addName").val();
	var dataJson = $.toJSON(obj);
	$.ajax({
		url:'/role/addRoleInfo',
		type:'POST',
		data:dataJson,
		contentType: "application/json",
		success:function(result){
			if(result.status==200 && result.resultCode.code==="SUCCESS"){
				layer.msg('添加成功！',function(){window.location.reload()});
			}else{
            	layer.msg(result.resultCode.message, function(){window.location.reload()});
            }
            layer.closeAll();
			
		},
		error:function(){
			 layer.msg('添加失败');
		}
	})
}

