window.onload=function(){
layui.use(['element','layer','form'],function(){
    var $=layui.jquery,layer = layui.layer,form = layui.form(),element=layui.element();
    var id = window.utils.getRequestParam("id");
    var isEdit=true;
    if(!!id){
    	var obj = new Object();
    	obj.id=id;
    	obj.implementId='';
    	obj.title='';
    	obj.applyType='';
    	obj.currentPage=1;
    	obj.showCount=1000;
	    //	带出数据
	 	$.ajax({
			url:'/personalTailor/queryByEntity',
			type:'POST',
			async:false,
			data: $.toJSON(obj),
			dataType: "json",
			contentType: "application/json",
			success:function(res){
				if(res.successful){
					var data=res.items[0];
					$('.imgBox img').attr('src',data.imgUrl);
					$('#applyType').val(data.applyType);
					$('#title').val(data.title);
					$('#introduce').val(data.description);
					$('#displayOrder').val(data.displayOrder);
					ue.ready(function() {//编辑器初始化完成再赋值  
	        			ue.setContent(data.richText); //赋值给UEditor  
	        		}); 
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
		var goodsDesc = UE.getEditor('container').getContent();
		var imgAry = "";
		var isGo = true;
		$(goodsDesc).find('img').each(function(){
            	var src = $(this).attr('src');
            	imgAry += src + ",";
        })
 		if(imgAry != "" && imgAry.length > 0 ){
 			if(isGo ==true){
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
        var applyType = $('#applyType').val();
		if(applyType==''||applyType=='all'){
        	layer.msg('请选择申请类别');$('.opa0').remove();
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
		var displayOrder = $('#displayOrder').val();
		if(displayOrder==''){
			layer.msg('显示顺序不能为空');$('.opa0').remove();
			return;
		}
		var sendData = new Object();
		if(!id){//新增
			ajaxUrl = "/personalTailor/addPerson";
			isEdit =false;
		}else{//修改
			ajaxUrl = "/personalTailor/updatePerson";
			sendData.id=id;
			isEdit =true;
		}
		sendData.implementId = window.utils.getRequestParam("id");
		sendData.applyType = applyType;
		sendData.imgUrl = imgUrl;
		sendData.title = title;
		sendData.description = introduce;
		sendData.displayOrder = displayOrder;
		sendData.richText = goodsDesc;
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
                    		window.location.href="personalSetList.html";
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
	$("#submit").click(function(){
		addFinder();
	});
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
