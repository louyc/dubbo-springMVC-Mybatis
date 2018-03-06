layui.use(['form','laypage','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,layer = layui.layer;
    var pageSum = 1;
    //导出
    $('#exportBtn').on('click',function(){
    	var query='?';
    	var firmName=$.trim($('#venderName').val());
    	if(firmName) query+='firmName='+firmName+'&';
    	$(this).attr('href','/excelExport/firm'+query);
    })
    //厂商列表查询
    function getData(pageNum,pageSize){
    	var obj = new Object();
	    obj.firmName = $("#venderName").val();
	    obj.pageNum = pageNum?pageNum:1;
	    obj.pageSize = pageSize?pageSize:10;
	    $.ajax({
	        url:'/deviceFirms/selectDeviceFirmsListPage',
	        type:'POST',
	        data: {'firmName':obj.firmName,'pageSize':obj.pageSize,'currentsPage':obj.pageNum},
	        dataType:"json",
	        success : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	var list = data.items;
	            if(data.resultCode.code=="SUCCESS"){
	            	var str = "";
	            	for(var i=0;i<list.length;i++){
	            		var firmName = list[i].firmName?list[i].firmName:""
	            		str+='<tr><td><a href="addVender.html?id='+list[i].id+'">'+firmName+'</a></td>';
	            		var firmDesc = list[i].firmDesc?list[i].firmDesc:""
	            		str+='<td>'+firmDesc+'</td>';
	            		var firmAdress = list[i].firmAdress?list[i].firmAdress:""
	            		str+='<td>'+firmAdress+'</td>';
	            		var firmPostcode = list[i].firmPostcode?list[i].firmPostcode:""
	            		str+='<td>'+firmPostcode+'</td>';
	            		var firmContact = list[i].firmContact?list[i].firmContact:""
	            		str+='<td>'+firmContact+'</td>';
	            		var firmPhone = list[i].firmPhone?list[i].firmPhone:""
	            		str+='<td>'+firmPhone+'</td>';
	            		var firmResponsible = list[i].firmResponsible?list[i].firmResponsible:""
	            		str+='<td>'+firmResponsible+'</td>';
	            		var firmResponsiblePhone = list[i].firmResponsiblePhone?list[i].firmResponsiblePhone:""
	            		str+='<td>'+firmResponsiblePhone+'</td>';
	            		str+='<td><input type="checkbox" lay-skin="switch" id="'+list[i].id+'" lay-filter="ifcanuse" lay-text="是|否"';
	        			if(list[i].isdel == "T"){
	        				str+='checked';
	            		}
	        			str+='></td>';
	        			str+='<td><a class="layui-btn layui-btn-small" href="addVender.html?id='+list[i].id+'" class="service_check">修 改</a></td></tr>'
	            	}
	            	$(".layui-table tbody").html(str);
	            	form.render();
	            	form.on('switch(ifcanuse)', function(data){
	            		layer.open({type: 3,content: ""});
	          		    var ifcanuse = data.elem.checked; //开关是否开启，“是”是true，“否”是false
	          		    if(ifcanuse){//要删除
	          			    var id = data.elem.getAttribute("id");
			          		$.ajax({
			                      url:'/deviceFirms/deleteDeviceFirm?id='+id,
			                      type:'GET',
			                      dataType:"json",
			                      contentType: "application/json",
			                      success : function(data){
			                    	  layer.close(layer.index);//取消遮罩
			                    	  layer.msg(data.resultCode.message);
			                      },
			                      error : function(data){
			                    	  layer.close(layer.index);//取消遮罩
			                      	  layer.msg('失败！');
			                      },
			                 });
	          		    }else{//要恢复
	          		    	var id = data.elem.getAttribute("id");
	          			    var obj = new Object();
	          			    obj.id = id;//名称
			          		$.ajax({
			                      url:'/deviceFirms/recoverDeviceFirm?id='+id,
			                      type:'GET',
			                      dataType:"json",
			                      contentType: "application/json",
			                      success : function(data){
			                    	  layer.close(layer.index);//取消遮罩
			                      	  llayer.msg(data.resultCode.message);
			                      },
			                      error : function(data){
			                    	  layer.close(layer.index);//取消遮罩
			                      	  layer.msg('失败！');
			                      },
			                 });
	          		    }
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
    	var pageSize = 10;
	    obj.firmName = $("#venderName").val();
	    obj.pageNum = 1;
	    obj.pageSize = 1000;
	    $.ajax({
	        url:'/deviceFirms/selectDeviceFirmsListPage',
	        type:'POST',
	        data: {'firmName':obj.firmName,'pageSize':obj.pageSize,'currentsPage':obj.pageNum},
	        dataType:"json",
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
        				getData(pageNum,pageSize);
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
