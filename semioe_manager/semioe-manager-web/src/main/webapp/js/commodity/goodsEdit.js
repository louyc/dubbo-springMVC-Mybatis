$(function(){
	layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        
//		实例化编辑器 
		var ue = UE.getEditor('container',{
	   	 autoHeightEnabled: true,
	        autoFloatEnabled: true,
	        //initialFrameWidth: 690,
	        initialFrameHeight:300,
	        elementPathEnabled:false
	   })
		function GetRequest() {
	        var url = location.search;
	        var theRequest = new Object();
	        if (url.indexOf("?") != -1) {
	            var str = url.substr(1);
	            strs = str.split("&");
	            for (var i = 0; i < strs.length; i++) {
	                theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
	            }
	        }
	        return theRequest;
	    }
	  	//获取单个商品信息
	    var goodsId = GetRequest().id;
	    var isModify = false;
	    if(goodsId){
	    		isModify = true;
		    	$.get('/goodsInfo/getGoodsInfoById?id='+goodsId,function(res){
		        	if(res.successful){
		        		var data = res.data;
			    		$('#goodsName').val(data.goodsName);
			    		$('#goodsType').val(data.goodsType);
					$('#supplier').val(data.supplierName);
					$('#price').val(data.price);
		        		$('#unit').val(data.unit);
		        		$('#stock').val(data.stock);
		        		$('.imgBox img').attr('src',data.imgUrl);
		        		if(data.keywordsIds){
		  	        		$('#labelBox').html('<input type="hidden" id="labelIds" value="'+data.keywordsIds+'"><textarea style="margin-top: 10px;" disabled="" id="label_list" class="layui-textarea">'+data.keywordsNames+'</textarea>')
		        		}
		        		ue.ready(function() {//编辑器初始化完成再赋值  
		        			ue.setContent(data.goodsDesc); //赋值给UEditor  
		        		});  
		        		$('#save').html('修改').attr('modify',true);
		        		if(data.type == 1){
		        			$('#upGoods').hide();
		        			$('#downGoods').show();
		        		}else{
		        			$('#upGoods').show();
		        			$('#downGoods').hide();
		        		}
		        		form.render('select')
	        		}else{
	        			layer.msg("商品信息获取失败");
	        		}
	        })
	    }else{
	    		isModify = false;
	    }
	    //选择标签
	    $("#checkLabel").on("click",function(){
	   		window.base.getLabel();
	   		return false;
	   	});

	 	//上传图片
	    var imgUrl=null;
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
					}else{
						$('.imgBox img').attr('src','')
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
	    //添加／修改／上架／下架商品
	    $('#save,#upGoods,#downGoods').on('click',function(){
	    		var thisId = $(this).attr('id');
	        	var goodsName = $.trim($('#goodsName').val()),
	        		keywordsIds=$('#labelIds').val(),
	        		keywordsNames = $('#label_list').val(),
	        		goodsType = $('#goodsType').val(),
	        		supplier = $.trim($('#supplier').val()),
	        		price = $('#price').val(),
	        		unit = $.trim($('#unit').val()),
	        		stock = $('#stock').val(),
	        		goodsDesc = UE.getEditor('container').getContent();
	        	if(isModify){
	        		imgUrl = imgUrl || $('.imgBox img').attr('src');
	        	}
        		if(goodsName == ''){
        			layer.msg('商品名称不能为空');
        			return;
        		}
        		if(!keywordsIds||!keywordsNames){
        			layer.msg('商品标签不能为空');
        			return;
        		}
        		if(goodsType == ''){
        			layer.msg('商品类型不能为空');
        			return;
        		}
        		if(supplier == ''){
        			layer.msg('供应商不能为空');
        			return;
        		}
        		if(price == ''){
        			layer.msg('商品单价不能为空');
        			return;
        		}
        		if(unit == ''){
        			layer.msg('商品单位不能为空');
        			return;
        		}
        		if(stock == ''){
        			layer.msg('商品库存不能为空');
        			return;
        		}
        		if(!imgUrl){
        			layer.msg('请上传商品简图');
        			return;
        		}
        		if(goodsDesc == ''){
        			layer.msg('商品详情不能为空');
        			return;
        		}
        		var imgAry = "";
            var isGo = true;
            $(goodsDesc).find('img').each(function(){
	             var src = $(this).attr('src');
	             imgAry += src + ",";
            })
      		if(imgAry != "" && imgAry.length > 0 ){
          		isGo = false;
      			$.ajax({
	        			url:'/file/upFile',
	        			type: 'POST',
	        			async:false,
	    				data: {files:imgAry.substring(0,imgAry.length-1)},
	    	        	success:function(res){
	    	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
	    	        			isGo = true;
	    	        		}else{
	    	        			layer.msg('详情图片'+res.resultCode.message)
	    	        			return;
	    	        		}
    	        		},
    	        		error:function(err){
    	        			layer.msg('详情图片'+res.resultCode.message)
    	        			return;
    	        		}
      			})
      		}
        		$(this).attr('disabled',true);
        		var getUrl = null;
        		var dataObj = {goodsName:goodsName,goodsType:goodsType,supplierName:supplier,imgUrl:imgUrl,goodsDesc:goodsDesc,price:price,unit:unit,stock:stock,type:0,keywordsIds:keywordsIds,keywordsNames:keywordsNames};
        		if(isModify){
        			dataObj.id = goodsId;
        			getUrl = '/goodsInfo/updateGoodsInfo';
        		}else{
        			getUrl = '/goodsInfo/addGoodsInfo';
        		}
        		if(thisId == 'upGoods'){
        			dataObj.type = 1;
			}else if(thisId == downGoods){
				dataObj.type = 2;
			}
        		if(isGo){
            		$.ajax({
            			url:getUrl,
            			type: 'POST',
        	        		async:false,
        				data: $.toJSON(dataObj),
        				dataType: "json",
        				contentType: "application/json",
	    	    	        	success:function(res){
	    	    	        		if(res.successful){
	        	        				if(thisId == 'save'){
	            						if(isModify){
	    		    	        				layer.msg("修改成功");
	    		    	        			}else{
	    		    	        				layer.msg("添加成功");
	    		    	        			}
	        	        				}else if(thisId == 'upGoods'){
	        	        					layer.msg("上架成功");
	        	        				}else if(thisId == downGoods){
	        	        					layer.msg("下架成功");
	        	        				}
	    	    	        			setTimeout(function(){
	    	    	        				window.location.href="/html/commodity/goodsManage.html";               
	    	    	        			},1500);
	    	    	        		}else{
	    	    	        			if(thisId == 'save'){
	    		    	        			if(isModify){
	    		    	        				layer.msg("修改失败,"+res.resultCode.message);
	    		    	        			}else{
	    		    	        				layer.msg("添加失败,"+res.resultCode.message);
	    		    	        			}
	        	        				$('#save').removeAttr('disabled');
	    	    	        			}else if(thisId == 'upGoods'){
	        	        					layer.msg("上架失败");
	    	    	        				$('#upGoods').removeAttr('disabled');
	        	        				}else if(thisId == downGoods){
	        	        					layer.msg("下架失败");
	    	    	        				$('#downGoods').removeAttr('disabled');
	        	        				}
	    	    	        		}
	    	    	        	},
	    	    	        	error:function(error){
	    	    	        		if(thisId == 'save'){
	    	    	        			if(isModify){
	    	    	        				layer.msg("修改失败");
	    	    	        			}else{
	    	    	        				layer.msg("添加失败");
	    	    	        			}
	    	        				$('#save').removeAttr('disabled');
	        	        			}else if(thisId == 'upGoods'){
	    	        					layer.msg("上架失败");
	        	        				$('#upGoods').removeAttr('disabled');
	    	        				}else if(thisId == downGoods){
	    	        					layer.msg("下架失败");
	        	        				$('#downGoods').removeAttr('disabled');
	            				}
	    	    	        	}
            		})
        		}
        })
	})
})