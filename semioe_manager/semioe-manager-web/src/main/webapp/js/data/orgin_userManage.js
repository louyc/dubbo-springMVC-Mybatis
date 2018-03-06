layui.use(['form','laypage','layer','jquery','laydate'], function(){
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,layer = layui.layer, laydate = layui.laydate;
    var pageSum = 1;
    
    var start = {
	    min: '1900-01-01 00:00:00'
	    ,max: '2099-06-16 23:59:59'
    	,format: 'YYYY-MM-DD hh:mm:ss'
	    ,istoday: true
	    ,istime: true
	    ,choose: function(datas){
	      end.min = datas; //开始日选好后，重置结束日的最小日期
	      end.start = datas //将结束日的初始值设定为开始日
	    }
    };
    	  
	var end = {
	    min: '1900-01-01 00:00:00'
	    ,max: '2099-06-16 23:59:59'
	    ,format: 'YYYY-MM-DD hh:mm:ss'
	    ,istoday: true
	    ,istime: true
	    ,choose: function(datas){
	      start.max = datas; //结束日选好后，重置开始日的最大日期
	    }
	};
	  
	document.getElementById('startTime').onclick = function(){
	    start.elem = this;
	    laydate(start);
	}
	document.getElementById('endTime').onclick = function(){
	    end.elem = this
	    laydate(end);
	}
    
    getDisease();
    //获取疾病史下拉
    function getDisease(){
    	$.get('/api/contracte/queryDictionary?typeId=3',function(res){
			if(res.successful){
				if(res.data){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.itemId+'">'+item.itemName+'</option>'
					})
					$('#diseaseId').html('<option value="all">全部</option>').append(option);
					form.render();
				}
			}else{
				layer.msg('疾病史获取失败！');
			}
		})
    };
    //获取签约医生
    getDoctors();
    function getDoctors(){
    	var obj = new Object();
	    obj.showCount = 1000;
	    obj.currentPage = 1;
	    obj.name = "";
	    obj.signOrgId = window.utils.getCookie("managerId");
	    obj.signType = 1;
	    obj.validTime = "";
    	$.ajax({
 	       url:'/jyDoctorInfo/getJySignDoctorInfo',
 	       type:'POST',
 	       data: $.toJSON(obj),
 	       dataType:"json",
 	       contentType: "application/json",
 	       success : function(res){
 	    	   if(res.items != []){
 	    		   var option = '';
 	    		   $.each(res.items,function(index,item){
 	    			   option += '<option value="'+item.managerId+'">'+item.name+'</option>'
 	    		   })
 	    		   $('#doctorId').html('<option value="all">全部</option>').append(option);
 	    		   form.render();
 	    	   }
 	       },
	        error : function(data){
	        	layer.msg('失败！');
	        },
	    });
    };
    var pageSum= 0,pageNum=1,pageSize=10;
    //列表查询
    getData(pageNum,pageSize);
    function getData(pageNum,pageSize){
    	var obj = new Object();
	    obj.mobile = $("#mobile").val();
	    obj.name = $("#name").val();
	    obj.ageId = $("#ageId").val()=="all"?"":$("#ageId").val();
	    obj.diseaseId = $("#diseaseId").val()=="all"?"":$("#diseaseId").val();
	    obj.inUse = $("#inUse").val()=="all"?"":$("#inUse").val();
	    obj.doctorId = $("#doctorId").val()=="all"?"":$("#doctorId").val();
	    obj.startTime = $("#startTime").val();
	    obj.endTime = $("#endTime").val();
	    obj.buildType = $("#buildType").val();
	    obj.orgId = window.utils.getCookie("managerId");
	    obj.currentPage = pageNum?pageNum:1;
	    obj.showCount = pageSize?pageSize:10;
	    $.ajax({
	        url:'/api/contracte/queryAllUser',
	        type:'POST',
	        data: $.toJSON(obj),
	        dataType:"json",
	        contentType: "application/json",
	        success : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	var list = data.items;
	        	pageSum = data.pagesCount;
	            if(data.successful){
	            	var str = "";
	            	for(var i=0;i<list.length;i++){
	            		str+='<tr><td><input type="checkbox" name="" data-id="'+list[i].managerId+'" lay-skin="primary"><input type="hidden" value="'+list[i].id+'"/></td>';
	            		var mobile = list[i].mobile?list[i].mobile:""
	            		str+='<td>'+mobile+'</td>';
	            		var name = list[i].name?list[i].name:""
	            		str+='<td>'+name+'</td>';
	            		var doctorName = list[i].doctorName?list[i].doctorName:""
	            		str+='<td>'+doctorName+'</td>';
	            		var parentName = list[i].parentName?list[i].parentName:"";
	            		str+='<td>'+parentName+'</td>';
	            		var parentMobile = list[i].parentMobile?list[i].parentMobile:"";
	            		str+='<td>'+parentMobile+'</td>';
	            		var roleName = list[i].roleName?list[i].roleName:"";
	            		str+='<td>'+roleName+'</td>';
	        			str+='<td><button data-type="'+list[i].doctorId+'" class="layui-btn layui-btn-small dataInfo">数据详情</button>';
        				str+='<a href="healthyFiles.html?managerId='+list[i].managerId+'" target="_blank" data-type="'+list[i].managerId+'" class="layui-btn layui-btn-small layui-btn-normal">健康档案</a>';
        				//str+='<button class="layui-btn layui-btn-small service-push" data-id="'+list[i].managerId+'">分配服务</button>';
    					//str+='<button class="layui-btn layui-btn-small layui-btn-normal message-push" data-id="'+list[i].managerId+'">消息推送</button>';
        				str+='</td></tr>';
	            	}
	            	laypage({
		        		cont: 'pagging'
	        			, pages: pageSum
	        			, curr: pageNum||1
	        			,jump: function(obj, first){
	        				if(!first){
	        					pageNum = obj.curr;
	        					getData(pageNum,pageSize);
	        				}
	        			}
		        	});
	            	$(".layui-table tbody").html(str);
	            	form.render();
	            	$(".message-push").on("click",function(){
	            		var userId=$(this).attr("data-id");
	            		messagePush(userId);
	            	});
	            	$(".service-push").on("click",function(){
	            		var userId=$(this).attr("data-id");
	            		$("#userIds").val(userId);
	            		distributeService();
	            	})
	            	//全选
	            	form.on('checkbox(allChoose)', function(data){
	            	    var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');
	            	    child.each(function(index, item){
	            	      item.checked = data.elem.checked;
	            	    });
	            	    form.render('checkbox');
	            	});
	            	$(".dataInfo").on("click",function(){
	            		var managerId = $(this).next("a").attr("data-type");
	            		var doctorId = $(this).attr("data-type");
	            		getDataInfo(managerId,doctorId);
	            	});
	            }
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }
    //搜索
    $("#search").on("click",function(){
    	getData(pageNum,pageSize)
    })
    //服务搜索
    $("#_search").on("click",function(){
    	_pagging();
    })
    //选择服务
    $("#_chooseService").on("click",function(){
    	var serviceIds = [];
    	$("#table_choose tbody tr").each(function(){
    		var checkInput = $(this).find(".layui-form-checkbox")[0];
    		var dataid = $(this).children("td:eq(0)").children("input[type='checkbox']")
    		if(checkInput.className.indexOf("layui-form-checked") >=0){
    			serviceIds.push(dataid.attr("data-id"));
    		}
    	});
    	if(serviceIds==''){
    		layer.msg('请选择服务！',{time: 2000,zIndex: layer.zIndex});
    		return;
    	};
    	var userIds = $("#userIds").val().split(",");
    	var doctorId = window.utils.getCookie("managerId");
    	$.ajax({
			url:'/orderInfo/doctorRecService?doctorId='+doctorId+'&userIds='+userIds+'&serviceIds='+serviceIds,
			type: 'POST',
        	success:function(res){
        		if(res.successful && res.resultCode.code == 'SUCCESS'){
        			layer.msg("推荐成功！",{time: 2000,zIndex: layer.zIndex},function(){
        				layer.closeAll();
        			});
        		}else{
        			layer.msg("推荐失败！",{time: 2000,zIndex: layer.zIndex})
        			return false;
        		}
	   		},
	   		error:function(err){
	   			layer.msg("推荐失败！",{time: 2000,zIndex: layer.zIndex})
	   			return false;
	   		}
		})
    })
    //批量分配服务
    $("#distributeService").on("click",function(){
    	var serviceIds = [];
    	$("#uers tbody tr").each(function(){
    		var checkInput = $(this).find(".layui-form-checkbox")[0];
    		var dataid = $(this).children("td:eq(0)").children("input[type='checkbox']");
    		if(checkInput.className.indexOf("layui-form-checked") >=0){
    			serviceIds.push(dataid.attr("data-id"));
    		}
    	})
    	if(serviceIds==''){
    		layer.msg('请选择用户！');
    		return;
    	}else{
    		$("#userIds").val(serviceIds);
    		distributeService();
    	}
    });
    //批量消息推送
    $("#messagePush").on("click",function(){
    	var serviceIds = [];
    	$("#uers tbody tr").each(function(){
    		var checkInput = $(this).find(".layui-form-checkbox")[0];
    		var dataid = $(this).children("td:eq(0)").children("input[type='checkbox']");
    		if(checkInput.className.indexOf("layui-form-checked") >=0){
    			serviceIds.push(dataid.attr("data-id"));
    		}
    	})
    	if(serviceIds==''){
    		layer.msg('请选择用户！');
    		return;
    	}
    	layer.open({
    		type:2
    		,title:'消息推送'
			,area: ['500px','500px']
    		,content: 'ueEditMessage.html'
	    	,btn: ['确定','关闭'] //按钮
    		,yes: function(index){
    			var UE = $(".layui-layer-iframe").find("iframe")[0].contentWindow.UE;
    			var goodsDesc = UE.getEditor('container').getContent();
    			var imgAry = "";
    			var isGo = true;
    			var doctorId = window.utils.getCookie("managerId");
    			
    			$.ajax({
	        			url:'/message/sendDoctorMessageList?doctorId='+doctorId+'&toUserIds='+serviceIds+'&context='+goodsDesc,
	        			type: 'POST',
	    	        	success:function(res){
	    	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
	    	        			layer.msg("消息推送成功！",{time: 2000,zIndex: layer.zIndex});
	    	        			layer.closeAll();
	    	        		}else{
	    	        			isGo = false;
	    	        			layer.msg("消息推送失败！",{time: 2000,zIndex: layer.zIndex})
	    	        			return;
	    	        		}
   	        		},
   	        		error:function(err){
   	        			isGo = false;
   	        			layer.msg("消息推送失败！",{time: 2000,zIndex: layer.zIndex})
   	        			return;
   	        		}
     			})
    			$(goodsDesc).find('img').each(function(){
   	             	var src = $(this).attr('src');
   	             	imgAry += src + ",";
                })
         		if(imgAry != "" && imgAry.length > 0 ){
         			if(isGo){
         				$.ajax({
       	        			url:'/file/upFile',
       	        			type: 'POST',
       	        			async:false,
       	    				data: {files:imgAry.substring(0,imgAry.length-1)},
       	    	        	success:function(res){
       	    	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
       	    	        			
       	    	        		}else{
       	    	        			isGo = false;
       	    	        			layer.msg('图片'+res.resultCode.message,{time: 2000,zIndex: layer.zIndex})
       	    	        			return;
       	    	        		}
           	        		},
           	        		error:function(err){
           	        			isGo = false;
           	        			layer.msg('图片'+res.resultCode.message,{time: 2000,zIndex: layer.zIndex})
           	        			return;
           	        		}
             			});
         			}
         		}
    			
    		}
    		,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	        }
    	})
    });
    //获取数据详情
    function getDataInfo(managerId,doctorId){
    	layer.open({
            type: 2 //2为弹出iframe
            ,title: '数据详情'
            ,area: ['80%', '80%']
    		,shade: 0.5
            ,content: 'dataInfo.html?managerId='+managerId+'&doctorId='+doctorId
            ,btn: ['关闭'] //按钮
	    	,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          layer.setTop(layero); //重点2
	        }
    	});
    }
  //分配服务接口
    function distributeService(){
    	layer.open({
    		type:1
    		,title:'选择服务'
			,area: ['1000px','450px']
    		,shade: 0.5
    		,content: $('#add_services')
	    	,btn: ['关闭'] //按钮
	        ,success: function(layero){
	        	_getData(_pageNum,_pageSize);
	        	layer.setTop(layero); //重点2
	        }
    	})
    }
    var _pageSum= 0,_pageNum=1,_pageSize=10;
    function _getData(_pageNum,_pageSize){
    	var obj = new Object();
    	obj.showCount = _pageSize;
    	obj.name = $("#name").val();
	    obj.currentPage = _pageNum;
	    obj.createUserId = window.utils.getCookie("managerId");
	    $.ajax({
	    	url:'/service/getAllotServices',
            type:'POST',
            async: false,
            data: $.toJSON(obj),
            dataType:"json",
            contentType: "application/json",
            success : function(data){
            	var list = data.items;
            	_pageSum = data.pagesCount;
                if(data.resultCode.code=="SUCCESS"){
                	var str = "";
                	for(var i=0;i<list.length;i++){
                		str+='<tr><td><input type="checkbox" name="" data-id="'+list[i].id+'" lay-skin="primary"></td>';
                		var name = list[i].name?list[i].name:"";
                		str+='<td>'+name+'</td>';
                		var description = list[i].description?list[i].description:"";
                		str+='<td>'+description+'</td>';
                		var keywordsName = list[i].keywordsName?list[i].keywordsName:"";
                		str+='<td>'+keywordsName+'</td>';
                	}
                	laypage({
		        		cont: 'pagging'
	        			, pages: _pageSum
	        			, curr: _pageNum||1
	        			,jump: function(obj, first){
	        				if(!first){
	        					_pageNum = obj.curr;
	        					getData(_pageNum,_pageSize);
	        				}
	        			}
		        	});
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
    //消息推送接口
    function messagePush(userId){//managerId为用户Id
    	layer.open({
    		type:2
    		,title:'消息推送'
			,area: ['500px','500px']
    		,content: 'ueEditMessage.html'
	    	,btn: ['确定','关闭'] //按钮
    		,yes: function(index){
    			var UE = $(".layui-layer-iframe").find("iframe")[0].contentWindow.UE;
    			var goodsDesc = UE.getEditor('container').getContent();
    			var imgAry = "";
    			var isGo = true;
    			var doctorId = window.utils.getCookie("managerId");
    			$(goodsDesc).find('img').each(function(){
   	             	var src = $(this).attr('src');
   	             	imgAry += src + ",";
                });
                $.ajax({
        			url:'/message/sendDoctorMessage?doctorId='+doctorId+'&toUserId='+userId+'&context='+goodsDesc,
        			type: 'POST',
    	        	success:function(res){
    	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
    	        			layer.msg("消息推送成功！",{time: 2000,zIndex: layer.zIndex},function(){
    	        				layer.closeAll();
    	        			});
    	        		}else{
    	        			isGo = false;
    	        			layer.msg("消息推送失败！",{time: 2000,zIndex: layer.zIndex});
    	        			return;
    	        		}
   	        		},
   	        		error:function(err){
   	        			isGo = false;
   	        			layer.msg("消息推送失败！",{time: 2000,zIndex: layer.zIndex});
   	        			return;
   	        		}
     			})
         		if(imgAry != "" && imgAry.length > 0 ){
         			if(isGo){
         				$.ajax({
       	        			url:'/file/upFile',
       	        			type: 'POST',
       	        			async:false,
       	    				data: {files:imgAry.substring(0,imgAry.length-1)},
       	    	        	success:function(res){
       	    	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
       	    	        			
       	    	        		}else{
       	    	        			layer.msg('图片'+res.resultCode.message,{time: 2000,zIndex: layer.zIndex})
       	    	        			return;
       	    	        		}
           	        		},
           	        		error:function(err){
           	        			layer.msg('图片'+res.resultCode.message,{time: 2000,zIndex: layer.zIndex})
           	        			return;
           	        		}
             			});
         			}
         		}
    		}
    		,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	        	
	        }
    	})
    };
});
