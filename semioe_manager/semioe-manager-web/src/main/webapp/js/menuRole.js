/**
 * Created by shiyq on 2017/5/25.
 */
layui.use(['element', 'form', 'layer', 'jquery'], function() {
	var $ = layui.jquery,form = layui.form(),element = layui.element();
	var layer = layui.layer;
	var obj = new Object();
	obj.itemName="";
	layer.open({
	    type: 3,
	    content: ""
	});
$.ajax({
	url:'/role/getRoleInfo',
	type:'POST',
	ansyc:true,
	data:$.toJSON(obj),
	contentType: "application/json",
	success : function(roleList){
		var roles = "";
		for(var k=0; k < roleList.data.length; k++){
			if(k == 0){
				roles +='<li class="layui-this"><input type="hidden" value="'+roleList.data[k].id+'">'+roleList.data[k].itemName+'</li>';
			}else{
				roles +='<li><input type="hidden" value="'+roleList.data[k].id+'">'+roleList.data[k].itemName+'</li>';
			}
		}
		$(".layui-tab-title").html(roles);
		//初始化表格
		var firstRoleId = $(".layui-tab-title li:first-child input").val();
		loadTable(firstRoleId);
		//加载完角色选项卡后加载layui 选项卡所需的模块
		layui.use('element', function(){ 
			var element = layui.element();
			//监听Tab切换，选择不同角色
			element.on('tab(docDemoTabBrief)', function(data){
			   var tabRoleId = $(".layui-tab-title li:eq("+data.index+")").children("input").val();
			   loadTable(tabRoleId);
			});
		});
		

	},
	error : function(data){
		layer.close(layer.index);//取消遮罩
		layui.use('layer', function(){
			var layer = layui.layer;
		    layer.msg('请求失败！')
		});
	}
});
//表格加载
function loadTable(roleId){
	layui.use('form', function(){
	    var form = layui.form();
		$.ajax({
			url:'/menu/query',
			type:'POST',
			ansyc:false,
			data:{"roleId":roleId},
			dataType:'json',
			success : function(menuList){
				layer.close(layer.index);//取消遮罩
				var list="";
				for(var i=0;i<menuList.data.length;i++){
				    list+='<tr style="background-color: #f2f2f2;"><td colspan="2"><input type="hidden" value="'+menuList.data[i].id+'"/>'+menuList.data[i].name+'</td></tr>'
				    for(var j=0;j<menuList.data[i].children.length;j++){
				        list+='<tr><td>';
				        if(menuList.data[i].children[j].type == "1"){
				        	list+='<input type="checkbox" name="" checked="checked" lay-skin="primary">'
				        }else{
				        	list+='<input type="checkbox" name="" lay-skin="primary">'
				        }
				        list+='</td><td><input type="hidden" value="'+menuList.data[i].children[j].id+'"/>'+menuList.data[i].children[j].name+'</td></tr>'
				    }
				}
				$("tbody").html(list);
				form.render('checkbox');
			},
			error : function(data){
				layer.close(layer.index);//取消遮罩
				layui.use('layer', function(){
					var layer = layui.layer;
				    layer.msg('请求失败！')
				});
			}
		});
	});
}
//保存事件
$("#save").on("click",function(){
	var _this =$(this);
	_this.addClass("layui-btn-disabled");
	var menuIdList = "";
	$(".layui-table tbody tr").each(function(){
		if($(this).children("td:eq(1)").length >0){
			var checkBox = $(this).children("td:eq(0)").children("div").attr("class");
			if(checkBox.indexOf("layui-form-checked")> -1){
				if(menuIdList !=""){
					menuIdList+=",";
				}
				menuIdList+=$(this).children("td:eq(1)").children("input").val();
			}
		}
		
	});
	var roleId = $(".layui-tab-title li.layui-this input").val();
	$.ajax({
		url:'/menu/addrel',
		type:'POST',
		ansyc:false,
		data:{"roleId":roleId,"menuIdList":menuIdList},
		success : function(data){
			_this.removeClass("layui-btn-disabled");
		    layer.msg('保存成功！')
		    /*parent.location.reload();*/
		},
		error : function(data){
			_this.removeClass("layui-btn-disabled");
		    layer.msg('请求失败！')
		}
	});
})
})

