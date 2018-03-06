$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        
        var pageSize = 10,pageNum=1,pageSum=1;
        //导出
        $('#exportBtn').on('click',function(){
        	var query='?';
        	var goodsName=$.trim($('#goodsName').val());
        	var supplierName=$.trim($('#supplier').val());
        	var type=$('#order-type').val()=='all' ? '' : $('#order-type').val();
        	var keywordsNames=$.trim($('#goodsTags').val());
        	if(goodsName) query+='goodsName='+goodsName+'&';
        	if(supplierName) query+='supplierName='+supplierName+'&';
        	if(type) query+='type='+type+'&';
        	if(keywordsNames) query+='keywordsNames='+keywordsNames+'&';
        	$(this).attr('href','/excelExport/goods'+query);
        })
        //获取商品列表
        getGoodsList(pageNum,false);
        function getGoodsList(pageNum,search){
        	var dataObj = new Object();
        	dataObj.showCount = pageSize;
        	dataObj.currentPage = pageNum;
        	dataObj.goodsName = $.trim($('#goodsName').val());
        	dataObj.supplierName = $.trim($('#supplier').val());
        	if(search){
        		dataObj.keywordsNames = $.trim($('#goodsTags').val());
        	}else{
	        	var keywrodsIds = $('#labelIds').val();
	        	dataObj.keywordsIds = !keywrodsIds?null:keywrodsIds;	
        	}	        	
        	var orderType = $('#order-type').val();
        	dataObj.type = orderType=='all' ? '' : orderType;
	        	
	        $.ajax({
	        	url:'/goodsInfo/getGoodsInfoArray',
	        	type: 'POST',
				data: $.toJSON(dataObj),
				dataType: "json",
				contentType: "application/json",
        		success:function(res){
	    			layer.open({
	            	    type: 3,
	            	    content: ""
	        	    });
	        		var data = res.items;
	        		var str = '';
	        		if(res.successful){
		        		layer.close(layer.index);
	        			pageSum = res.pagesCount;
	        			if(data.length>0){
	        				$.each(data,function(index,item){
			        			str += '<tr id="'+item.id+'"><td>'+item.goodsName+'</td><td>'+item.keywordsNames+'</td><td>'+item.price+'</td><td>'+item.stock+'<i class="reviseStock"></i></td><td>'+item.supplierName+'</td>';
						        str+='<td><button class="layui-btn layui-btn-small'+(item.type==1?' goodsTypeDown">下架':' goodsTypeUp">上架')+'</button>';
			           			str += (item.type==1?'<a href="javascript:;" class="layui-btn layui-btn-small layui-btn-disabled" disabled':'<a href="/html/commodity/goodsEdit.html?id='+item.id+'" class="layui-btn layui-btn-small"')+' class="edit">编辑</a></td></tr>';
			        		})
	        			}else{
		        			str += '<tr><td colspan="6">暂无数据</td></tr>'
		        		}
	        			//分页
		        		laypage({
				        cont: 'pagging'
			    		, pages: pageSum
				        , curr: res.pageNumber||1
				        ,jump: function(obj, first){
					        	if(!first){
					            pageNum = obj.curr;
					            getGoodsList(pageNum);
				            }
				        }
				    });
	        		}else{
	        			str += '<tr><td colspan="6">列表获取失败</td></tr>'
	        		}
	        		$('.list-table tbody').html(str);
        	},
        	error:function(error){
        		layer.close(layer.index);
        		$('.list-table tbody').html('<tr><td colspan="6">列表获取失败</td></tr>');
        	}
	        })
        }
        $('#search').on('click',function(){
    		getGoodsList(1,true);
        })
       //上下架
        $('.list-table').delegate('.goodsTypeUp','click',function(){
        	var typeName = $(this).attr('class');
        	var id = $(this).parents('tr').attr('id');
        	layer.open({
			  type: 1
			  ,title:'提示'
			  ,content: '<p style="text-align: center; padding: 30px;">商品上架后，不允许再进行编辑修改！</p>'
			  ,area: '370px'
			  ,btn: ['确定', '取消']
			  ,btnAlign: 'c'
			  ,yes: function(index, layero){
				  $('.layui-layer-btn').prepend('<span class="opa0"></span>');
				  chageType(id,1,typeName);
			  }
			});
        })
        $('.list-table').delegate('.goodsTypeDown','click',function(){
        	var typeName = $(this).attr('class');
        	var id = $(this).parents('tr').attr('id');
        	$(this).attr('disabled',true);
        	chageType(id,2,typeName);
        })
        function chageType(id,type,typeName){
        	var dataObj = {id:id,type:type,keywordsIds:null};
        	var typeVal = '';
        	if(typeName.indexOf('goodsTypeUp')>-1){
        		typeVal = '上架'
        	}else{
        		typeVal = '下架'
        	}
        	layer.open({
        	    type: 3,
        	    content: ""
    	    });
        	$.ajax({
	        	url:'/goodsInfo/updateGoodsInfo',
	        	type: 'POST',
				data: $.toJSON(dataObj),
				dataType: "json",
				contentType: "application/json",
	        	success:function(res){
	        		if(res.successful){
	        			layer.msg(typeVal+'成功');
	        			setTimeout(function(){
	        				getGoodsList(pageNum);
	        				layer.closeAll();
	        			},1500);
	        		}else{
	        			$('.opa0').remove();
	        			$('.goodsTypeDown').removeAttr('disabled');
	        			layer.msg(typeVal+'失败');
	        		}
        		},
        		error:function(error){
        			$('.opa0').remove();
        			$('.goodsTypeDown').removeAttr('disabled');
        			layer.msg(typeVal+'失败');
        		}
        	})
        }
        //编辑
        $('.list-table').delegate('.edit','click',function(){
        	var id = $(this).parents('tr').attr('id');
        	window.location.href = '/html/commodity/goodsEdit.html?id'+id;
        })
        //修改库存
        $('.list-table').delegate('.reviseStock','click',function(){
    		var id = $(this).parents('tr').attr('id');
    		var stockNum = $(this).parent('td').text();
    		$('#stockNum').val(stockNum);
    		layer.open({
			  type: 1
			  ,title:'修改'
			  ,content: $('#revise-box')
			  ,area: '370px'
			  ,btn: ['确定', '取消']
			  ,btnAlign: 'c'
    			  ,yes: function(index, layero){
    				  var stockNum = $('#stockNum').val();
    				  if(stockNum == ''){
    					  layer.msg('请输入库存数量');
    					  return;
    				  }
    				  layer.open({
	            	      type: 3,
	            	      content: ""
	        	      });
    				  var dataObj = {id:id,stock:stockNum};
    				  $.ajax({
    					  url:'/goodsInfo/updateGoodsInfo',
    						type: 'POST',
    						data: $.toJSON(dataObj),
    						dataType: "json",
    						contentType: "application/json",
    				        	success:function(res){
			        			layer.close(layer.index);
    				        		if(res.successful){
    				        			layer.msg('修改成功');
    				        			setTimeout(function(){
    				        				getGoodsList(pageNum);
    				        			},1500);
    				        		}else{
    				        			layer.msg('修改失败');
    				        		}
    			        		},
    			        		error:function(error){
    			        			layer.close(layer.index);
    			        			layer.msg('修改失败');
    			        		}
    				  }) 				  
  			  }
    			});
        })
    })
})