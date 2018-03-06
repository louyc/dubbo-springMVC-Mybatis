layui.use(['form','laypage','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,layer = layui.layer;
    //导出
    $('#exportBtn').on('click',function(){
    	var query='?';
    	var brandName=$.trim($('#brandName').val());
    	var firmName=$.trim($('#venderName').val());
    	if(brandName) query+='brandName='+brandName+'&';
    	if(firmName)  query+='firmName='+firmName+'&';
    	$(this).attr('href','/excelExport/brands'+query);
    })
    function getData(pageNum,pageSize){
    	var obj = new Object();
        obj.brandName = $("#brandName").val();
        obj.firmName = $("#venderName").val();
        obj.pageNum = pageNum?pageNum:1;
	    obj.pageSize = pageSize?pageSize:10;
        $.ajax({
            url:'/deviceBrands/selectDeviceBrandsListPage',
            type:'POST',
            data: {'brandName':obj.brandName,'firmName':obj.firmName,'pageSize':obj.pageSize,'currentsPage':obj.pageNum},
            dataType:"json",
            success : function(data){
            	layer.close(layer.index);//取消遮罩
            	var list = data.items;
                if(data.resultCode.code=="SUCCESS"){
                	var str = "";
                	for(var i=0;i<list.length;i++){
                		var brandName = list[i].brandName?list[i].brandName:"";
                		str+='<tr><td>'+brandName+'</a></td>';
                		var brandDesc = list[i].brandDesc?list[i].brandDesc:"";
                		str+='<td>'+brandDesc+'</td>';
                		var firmName = list[i].firmName?list[i].firmName:"";
                		str+='<td><input type="hidden" value="'+list[i].deviceFirmid+'" />'+firmName+'</td>';
                		str+='<td><input type="checkbox" lay-skin="switch" id="'+list[i].id+'" lay-filter="ifcanuse" lay-text="是|否"';
            			if(list[i].isdel == "T"){
            				str+='checked';
                		}
            			str+='></td>';
            			str+='<td><a class="layui-btn layui-btn-small" href="addBrand.html?id='+list[i].id+'" class="service_check">修 改</a></td></tr>'
                	}
                	$(".layui-table tbody").html(str);
                	form.render();
                	form.on('switch(ifcanuse)', function(data){
                		layer.open({type: 3,content: ""});
              		    var ifcanuse = data.elem.checked; //开关是否开启，“是”是true，“否”是false
              		    var id = data.elem.getAttribute("id");
              		    if(ifcanuse){//要删除
    		          		$.ajax({
    		                      url:'/deviceBrands/deleteDeviceBrand?id='+id,
    		                      type:'POST',
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
    		          		$.ajax({
    		                      url:'/deviceBrands/recoverDeviceBrand?id='+id,
    		                      type:'POST',
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
              		    }
              		});  
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
    	obj.brandName = $("#brandName").val();
	    obj.firmName = $("#venderName").val();
	    obj.pageNum = 1;
	    $.ajax({
	    	url:'/deviceBrands/selectDeviceBrandsListPage',
            type:'POST',
            data: {'brandName':obj.brandName,'firmName':obj.firmName,'pageSize':obj.pageSize,'currentsPage':obj.pageNum},
            dataType:"json",
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
    //搜索
    $("#search").on("click",function(){
    	pagging();
    })
});
