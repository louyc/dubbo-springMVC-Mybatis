$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        form.render('select')
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
	    	//获取订单列表
	    	var pageSize = 10,pageNum=1,pageSum=1;
	    	getOrderList(pageNum);
	    	function getOrderList(pageNum){
	    		var dataObj = new Object();
	        	dataObj.showCount = pageSize;
	        	dataObj.currentPage = pageNum;
	        	dataObj.orderCode = $.trim($('#orderNum').val());
	        	dataObj.relationName = $.trim($('#goods').val());
	        	dataObj.relationSupplier = $.trim($('#supplier').val());
	        	dataObj.startDate = $('#startTime').val();
	        	dataObj.endDate =  $('#endTime').val();
	        	dataObj.type = $('#order-type').val()=='all' ? '' : $('#order-type').val();
	        	dataObj.payStatus = $('#order-status').val()=='all' ? '' : $('#order-status').val();
	        	dataObj.sourceType =$('#order-soure').val()=='all' ? '' : $('#order-soure').val();
	        	
	    		$.ajax({
	    			url:'/orderInfo/getOrderInfoArray',
		        	type: 'POST',
					data: $.toJSON(dataObj),
					dataType: "json",
					contentType: "application/json",
		        	success:function(res){
	        		var data = res.items;
	        		var str = '';
	        		if(res.successful){
	        			pageSum = res.pagesCount;
	        			if(data.length>0){
	        				$.each(data,function(index,item){
	        					str += '<tr id="'+item.id+'" relationId="'+item.relationId+'"><td>'+(item.orderCode?item.orderCode:'')+'</td><td>'+(item.relationName?item.relationName:'')+'</td><td>'+(item.relationSupplier?item.relationSupplier:'')+'</td><td>'+(item.relationPrice?item.relationPrice:0)+'</td><td>'+(item.orderCount?item.orderCount:0)+'</td><td>'+(item.price?item.price:0)+'</td><td>'+(item.userName?item.userName:'')+'</td><td>'+getStatus(item.payStatus)+'</td><td>'+item.createTime+'</td>'+
	        					'<td>'+(item.sourceType == '0' ? '广告位' : (item.sourceType == '1') ? '医生主页' : (item.sourceType == '2' ? '推荐服务' : ''))+'</td><td>'+(item.sourceUser?item.sourceUser:'')+'</td>';
	        					//var userAddress = eval('(' + item.userAddress + ')');
	        					//str+='<td>'+userAddress.name+'</td><td>'+userAddress.mobile+'</td><td>'+userAddress.address+'</td>';
	        					str+='<td><button class="layui-btn layui-btn-small viewBtn" orderType="'+item.type+'">查看</button></td></tr>';
//					        str+='<td><button class="layui-btn viewBtn '+(item.type==1?'layui-btn-disabled" disabled>':'">')+'查看</button></td></tr>';
			        		})
	        			}else{
		        			str += '<tr><td colspan="12">暂无数据</td></tr>'
		        		}
	        			//分页
		        		laypage({
				        cont: 'pagging'
			    		, pages: pageSum
				        , curr: res.pageNumber||1
				        ,jump: function(obj, first){
					        	if(!first){
					            pageNum = obj.curr;
					            getOrderList(pageNum);
				            }
				        }
				    });
	        		}else{
	        			str += '<tr><td colspan="12">列表获取失败</td></tr>'
	        		}
	        		$('.list-table tbody').html(str);
	        	},
	        	error:function(error){
	        		$('.list-table tbody').html('<tr><td colspan="12">列表获取失败</td></tr>');
	        	}
	        })
	    }
	    	function getStatus(str){
	    		var status = '';
	    		if(str == 0){
	    			status = '待支付';
	    		}else if(str == 1){
	    			status = '已支付';
	    		}
	    		return status;
	    	}
        $('#search').on('click',function(){
        		getOrderList(1);
        })
    
        //获取订单详情
        $('.list-table').delegate('.viewBtn','click',function(){
    		var id = $(this).parents('tr').attr('id');
    		var type = $(this).attr('orderType');
    		$.get('/orderInfo/getOrderDetailById?id='+id,function(res){
    			var data = res.data;
    			if(!!data.userName){
    				$('.userName').html(data.userName);
    			}else{
    				$('.userName').html(data.userObject.mobile);
    			}
    			$('.payStatus').html(getStatus(data.payStatus));
    			$('.orderNum').html(data.orderCount);
    			$('.orderPrice').html(data.price);
    			$('.orderOrigin').html(data.sourceType == '0' ? '广告位' : (data.sourceType == '1') ? '医生主页' : (data.sourceType == '2' ? '推荐服务' : ''));
    			$('.referee').html(data.sourceUser ? data.sourceUser : '');
    			if(!data.userAddress){
    				$('.consignee,.mobile,.address,.addressCode').html('暂无信息');
    			}else{
        			var address = eval('('+data.userAddress+')');
        			$('.consignee').html(address.name);
        			$('.mobile').html(address.mobile);
        			$('.address').html(address.address+' '+address.detailedAddress);
        			$('.addressCode').html(address.zipCode);
    			}
        		if(type == 1){
        			$('#service-info-box').show();
        			$('#goods-info-box').hide();
        			var goodsInfoList = data.goodsList;
    				var listHtml = '';
    				$('#service-info-box .workflow').html(data.relationCodeName);
        			$('#service-info-box .serviceName').html(data.relationName);
        			$('#service-info-box .price').html(data.price);
        			$('#service-info-box .serviceDescribe').html(data.relationDesc);
        			
        			if(goodsInfoList && goodsInfoList.length > 0){
        				$.each(goodsInfoList,function(index,item){
        					listHtml += '<div class="goodsItem" goodsId="'+item.id+'"><div class="img-box"><img src="'+item.imgUrl+'" alt="" /></div><p>'+item.goodsName+'</p></div>';
        				})
        			}else{
        				listHtml += '暂无商品信息';
        			}
        			$('#service-info-box .goodsList').html(listHtml);
        		}else if(type == 0){
        			$('#goods-info-box').show();
        			$('#service-info-box').hide();
        			$('.goodsName').html(data.relationName);
					$('.supplierName').html(data.relationSupplier);
					$('.goodsPrice').html(data.price);
	        		$('.goodsImg img').attr('src',data.relationImgUrl);
	        		$('.goodsDetail').html(data.relationDesc); 
        		}
    			
    			layer.open({
	        		type:1
	        		,title:'订单详情'
	        		,content: $('#order-info-box')
        			,area: ['600px','450px']
	        	})
    		})
        })
//        $('#service-info-box').delegate('.goodsItem','click',function(){
//        		var goodsId = $(this).attr('goodsId');
//        		getGoodsDetail(goodsId);
//        })
        //导出文件
        $('#export').on('click',function(){
    		var query = '?';
    		var orderCode = $.trim($('#orderNum').val());
	        var relationName = $.trim($('#goods').val());
	        var relationSupplier = $.trim($('#supplier').val());
	        var startDate = $('#startTime').val();
        	var endDate =  $('#endTime').val();
	        var type = $('#order-type').val()=='all' ? '' : $('#order-type').val();
        	var payStatuns = $('#order-status').val()=='all' ? '' : $('#order-status').val();
        	if(orderCode)  query += 'orderCode='+orderCode+'&';
        	if(relationName)  query += 'relationName='+relationName+'&';
        	if(relationSupplier)  query += 'relationSupplier='+relationSupplier+'&';
        	if(startDate)  query += 'startDate='+startDate+'&';
        	if(endDate)  query += 'endDate='+endDate+'&';
        	if(payStatuns)  query += 'payStatus='+payStatuns+'&';
        	if(type)  query += 'type='+type+'&';

        	$(this).attr('href','/orderInfo/exportOrderInfo'+query);
        });
    })
})
