window.onload=function(){
layui.use(['element','layer','form'],function(){
    var $=layui.jquery,layer = layui.layer,form = layui.form(),element=layui.element();
    var id = window.utils.getRequestParam("id");
    var isEdit = false;
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
					$('#serviceType').html('<option value="0">全部</option>').append(option);
					form.render();
				}
			}else{
				layer.msg('服务分类获取失败！');
			}
		})
    };
    //获取商品
	function getGoods(){
    	var obj=new Object();
    	obj.goodsName='';obj.supplierName='';obj.type=null;obj.keywordsIds=null;obj.keywordsNames=null;obj.showCount=100;obj.currentPage=1;obj.type=1;
    	var dataJson=$.toJSON(obj);
        $.ajax({
        	url:'/goodsInfo/getGoodsInfoArray',
        	type:'POST',
        	async:false,
        	data:dataJson,
        	contentType: "application/json",
        	success:function(items){
        		var str='',data=items.items;
        		$.each(data,function(index,item){
        			str+='<option value="'+item.id+'">'+item.goodsName+'</option>'
        		})
        		$('select[name="chance_commodity"]').html('<option value="">选择商品</option>').append(str);
        		form.render();
        	},
        	error:function(error){
        		layer.msg('加载失败');
        	}
        })
       
    }
  	//获取user_id
    var cookieVal = window.utils.getCookie("user_id");
    //获取服务
    function getService(){
        $.ajax({
        	url:'/service/queryAll?createUserId='+cookieVal,   
        	type:'GET',
        	async:false,
        	success:function(data){
        		if(data.successful){
        			var str='',data=data.data;
            		$.each(data,function(index,item){
            			str+='<option value="'+item.id+'">'+item.name+'</option>'
            		})
            		$('select[name="chance_service"]').html('<option value="">请选择服务</option>').append(str);
            		form.render();
        		}
        	},
        	error:function(error){
        		layer.msg('加载失败');
        	}
        })
    }
    getGoods();
   	getService();
   	//服务或者商品 的全局变量
   	var serviceId="";
    //选择商品
    form.on('select(chance_commodity)', function(data){
		 var goodsVal=data.elem[data.elem.selectedIndex].text;
		 var goodsId='/goodsInfo/getGoodsInfoById?id='+data.value;  
		 serviceId=goodsId;
    })
    //选中服务
    form.on('select(chance_service)', function(data){
		 var serviceVal=data.elem[data.elem.selectedIndex].text;
		 var serviceIds='/service/queryGoodsById?id='+data.value;   
		 serviceId=serviceIds;
    })
    //如果有ID那么就是修改
    if(!!id){
    	$.get('/api/contracte/queryDictionary?typeId=31',function(res){
			if(res.successful){
				if(res.data){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.itemId+'">'+item.itemName+'</option>'
					})
					$('#serviceType').html('<option value="0">全部</option>').append(option);
					loadData()
					form.render();
				}
			}else{
				layer.msg('服务分类获取失败！');
			}
		})
    }
    //编辑 带出数据
    function loadData(){
    	var obj = new Object();
        obj.id=id;
        obj.implementId='';
        obj.title='';
        obj.serviceType='';
        obj.currentPage=1;
        obj.showCount=1000;
        //	带出数据
     	$.ajax({
    		url:'/implementDetail/queryByEntity',
    		type:'POST',
    		data: $.toJSON(obj),
    		dataType: "json",
    		contentType: "application/json",
    		success:function(res){
    			if(res.successful){
    				var data=res.items[0];
    				$('.imgBox img').attr('src',data.imgUrl);
    				$('#serviceType').val(data.serviceType);
    				$('#title').val(data.title);
    				$('#introduce').val(data.description);
    				$('#displayOrder').val(data.displayOrder);
    				if(data.goodsList){
    					$("#chance_commodity").val(data.goodsList[0].id);
    				}
    				if(data.serviceList){
    					$("#chance_service").val(data.serviceList[0].id);
    				}
    				serviceId=data.contentUrl;
    				form.render();
    			}else{
        			layer.msg("详细信息查询失败!");
    			}
    		},
    		error:function(error){
    			layer.msg("详细信息查询失败!");
    		}
    	})
    }
	//	新增、修改
	function addFinder(){
		$('.layui-layer-btn').prepend('<span class="opa0"></span>');
		//图片上传
		var imgUrlCheck = $('.imgBox img').attr('src');
        if(imgUrlCheck==''){
        	layer.msg('图片不能为空');$('.opa0').remove();
        	return;
        }
        var serviceType = $('#serviceType').val();
		if(serviceType==''){
        	layer.msg('请选择服务类别');$('.opa0').remove();
        	return;
        }
		var title = $('#title').val();
		if(title==''){
			layer.msg('标题不能为空');$('.opa0').remove();
			return;
		}
		var introduce = $('#introduce').val();
		if(introduce==''){
			layer.msg('介绍不能为空');$('.opa0').remove();
			return;
		}
		if(serviceId==""){
			layer.msg('请选择服务或者商品 ');$('.opa0').remove();
			return;
		}
		var displayOrder = $('#displayOrder').val();
		if(displayOrder==''){
			layer.msg('显示顺序不能为空');$('.opa0').remove();
			return;
		}
		var sendData = new Object();
		if(!id){//新增
			ajaxUrl = "/implementDetail/addDetail";
		}else{//修改
			ajaxUrl = "/implementDetail/updateDetail";
			sendData.id=id;
			isEdit =true;
		}
		sendData.implementId = window.utils.getRequestParam("id");
		sendData.serviceType = serviceType;
		sendData.imgUrl = imgUrl;
		sendData.title = title;
		sendData.description = introduce;
		sendData.displayOrder = displayOrder;
		sendData.contentUrl = serviceId;
    	$.ajax({
        	url:ajaxUrl,
        	type:'POST',
        	data:$.toJSON(sendData),
        	dataType:"json",
            contentType: "application/json",
            success:function(data){
            	$('.opa0').remove();
            	if(data.successful){
            		layer.closeAll();
                	layer.msg(isEdit?'编辑成功':'添加成功',{time: 2000},function(){
                		window.location.href="healthManageList.html";
                	});
            	}else{
            		layer.msg(data.resultCode.message);
            	}
            	
            },
            error:function(error){
            	$('.opa0').remove();
        		layer.msg(isEdit?'编辑失败':'添加失败!');
            }
        })
	}
	$("#submit").on("click",function(){
		addFinder();
	})
	var imgUrl=null;
    //上传图片
	 $('#upload').on('change',function(){
 		preview(this);
     	var formData = new FormData(document.getElementById("uploadFile"));
		$.ajax({
			type: "POST",
			url: "/file/upload",
			data: formData,
			cache: false,
			processData: false,
			contentType: false,
			dataType: "json",
			success: function(res) {
				if(res.successful){
					imgUrl = res.data;
				}
				layer.msg(res.resultCode.message);
			},
			error: function(error) {
				layer.msg("图片上传到服务器失败");
			}
		});
    })
	//图片预览，兼容各个浏览器
    function preview(file) {
		if(file.files && file.files[0]) {
			var reader = new FileReader();
			reader.onload = function(evt) {
				$('.imgBox img').attr('src', evt.target.result);
			}
			reader.readAsDataURL(file.files[0]);
		} else {
			$('.imgBox img').attr('src', file.value);
		}
	};
}) 
}
