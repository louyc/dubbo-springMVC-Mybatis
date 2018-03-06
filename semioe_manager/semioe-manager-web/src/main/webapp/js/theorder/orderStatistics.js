$(function(){
	layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        
        var start = {
	    	    min: '1900-01-01 00:00:00'
	    	    ,max: '2099-06-16 23:59:59'
		    	,format: 'YYYY-MM-DD hh:mm:ss'
	    	    ,istoday: true
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
        //获取今日，本周，本月销售金额
        $('#staticsInfo dl').each(function(){
        		var index = $(this).index();
        		var text = '';
      		var timeObj = getDateTime();
        		var dataObj = new Object();
        		if(index == 0){
        			text = '今日';
        			dataObj.startDate = timeObj.todayTime;
        			dataObj.endDate = timeObj.todayTime;
        		}else if(index == 1){
        			text = '本周';
        			dataObj.startDate = timeObj.weekTime;
        			dataObj.endDate = timeObj.weekEndTime;
        		}else if(index == 2){
        			text = '本月';
        			dataObj.startDate = timeObj.monthStart;
        			dataObj.endDate = timeObj.monthEnd;
        		}
        		dataObj.backManagerId = window.utils.getCookie('user_id');
        		getAmount(this,text,dataObj);
        })
        //n代表几天前 
        function getDateTime(n){
        		var date = new Date();
        		var day = date.getDay();
        		var nowTime = date.getTime();
        		var weekTimeStr='',monthTimeStr='',weekEnd='';
        		var monthNum = date.getMonth();
        		var monthStart = '',monthEnd = '';
        		var year = date.getFullYear();
        		switch (day) {
				case 0:
					weekEnd = nowTime;
					weekTimeStr = nowTime-6*24*3600*1000;
					break;
				case 1:
					weekTimeStr = nowTime;
					weekEnd = nowTime + 6*24*3600*1000;
					break;
				case 2:
					weekTimeStr = nowTime - 24*3600*1000;
					weekEnd = nowTime + 5*24*3600*1000;
					break;
				case 3:
					weekTimeStr = nowTime - 2*24*3600*1000;
					weekEnd = nowTime + 4*24*3600*1000;
					break;
				case 4:
					weekTimeStr = nowTime - 3*24*3600*1000;
					weekEnd = nowTime + 3*24*3600*1000;
					break;
				case 5:
					weekTimeStr = nowTime - 4*24*3600*1000;
					weekEnd = nowTime + 2*24*3600*1000;
					break;
				case 6:
					weekTimeStr = nowTime - 5*24*3600*1000;
					weekEnd = nowTime + 24*3600*1000;
					break;
			}
			switch(monthNum){
				case 0:
					monthStart = year+'-01-01';
					monthEnd = year+'-01-31';
					break;
				case 1:
					monthStart = year+'-02-01';
					monthEnd = year%4==0?year+'-02-29':year+'-02-28';
					break;
				case 2:
					monthStart = year+'-03-01';
					monthEnd = year+'-03-31';
					break;
				case 3:
					monthStart = year+'-04-01';
					monthEnd = year+'-04-30';
					break;
				case 4:
					monthStart = year+'-05-01';
					monthEnd = year+'-05-31';
					break;
				case 5:
					monthStart = year+'-06-01';
					monthEnd = year+'-06-30';
					break;
				case 6:
					monthStart = year+'-07-01';
					monthEnd = year+'-07-31';
					break;
				case 7:
					monthStart = year+'-08-01';
					monthEnd = year+'-08-30';
					break;
				case 8:
					monthStart = year+'-09-01';
					monthEnd = year+'-09-30';
					break;
				case 9:
					monthStart = year+'-10-01';
					monthEnd = year+'-10-31';
					break;
				case 10:
					monthStart = year+'-11-01';
					monthEnd = year+'-11-30';
					break;
				case 11:
					monthStart = year+'-12-01';
					monthEnd = year+'-12-31';
					break;
			}
			var beforeTime = window.utils.simpleDateFormat(nowTime-(n-1)*24*3600*1000,'yyyy-MM-dd');//n天前日期
        		var todayTime = window.utils.simpleDateFormat(nowTime,'yyyy-MM-dd');//今日日期
        		var weekTime = window.utils.simpleDateFormat(weekTimeStr,'yyyy-MM-dd');//本周开始日期
        		var weekEndTime = window.utils.simpleDateFormat(weekEnd,'yyyy-MM-dd');//本周结束日期
        		var monthTime = window.utils.simpleDateFormat(monthTimeStr,'yyyy-MM-dd');//月份
        		return {todayTime:todayTime,weekTime:weekTime,weekEndTime:weekEndTime,monthStart:monthStart,monthEnd:monthEnd,beforeTime:beforeTime};
        }
        //获取某段时间订单总金额
        function getAmount(ele,txt,dataObj){
	        $.ajax({
	        		url:'/orderInfo/countTotalPrice',
	        		type:'POST',
	        		data: $.toJSON(dataObj),
				dataType: "json",
				contentType: "application/json",
		        	success:function(res){
		        		if(res.successful){
		        			$(ele).find('dt').html(res.data?res.data:0);
		        		}else{
		        			layer.msg(txt+'数据获取失败');
		        		}
	        		},
	        		error:function(error){
        				layer.msg(txt+'数据获取失败');
	        		}
	        })
        }
        //获取最近一个月的订单金额
        var isPrice = false, isAmount = false;
        $('#viewAllPrice').on('click',function(){
        		isPrice = true;
        		$('.data-chart-show,.data-list-show').hide();
        		$('.order-chart-box').show().find('h3').html('所有成交额数据');
        		getOrderNum(isPrice,isAmount,30);
        })
        //获取最近一个月的订单数据
        $('#viewAllAmount').on('click',function(){
        		isAmount = true;
        		$('.data-chart-show,.data-list-show').hide();
        		$('.order-chart-box').show().find('h3').html('所有订单量数据');
        		getOrderNum(isPrice,isAmount,30);
        })
        //按查询日期查询数据
        $('#query').on('click',function(){
        		if(isPrice){
        			getOrderNum(true,false);
        		}
        		if(isAmount){
        			getOrderNum(false,true);
        		}
        })
        
        getOrderNum(false,true,7);
        function getOrderNum(orderPrice,orderNum,n){
        		var timeObj = getDateTime(n);
        		var dataObj = new Object();
        		dataObj.startDate = $('#startTime').val()||timeObj.beforeTime;
    			dataObj.endDate = $('#endTime').val()||timeObj.todayTime;
    			dataObj.backManagerId = window.utils.getCookie('user_id');
    			var dataUrl = '';
    			if(orderPrice){
    				dataUrl = '/orderInfo/countPrice';
    			}
    			if(orderNum){
    				dataUrl = '/orderInfo/countAmount';
    			}
    			
        		$.ajax({
        			url:dataUrl, 
	        		type:'POST',
	        		data: $.toJSON(dataObj),
				dataType: "json",
				contentType: "application/json",
		        	success:function(res){
		        		if(res.successful){
		        			if(res.data && res.data.length<=0){
		        				$('#orderNumChart,#orderChart').html('暂无订单数据');
		        				return;
		        			}
				        var orderDateAry = [];
				        var orderNumAry = [];
				        var dayAry = [];
		        			$.each(res.data,function(index,item){
		        				var day = new Date(item.countDate).getDay();
		        				orderDateAry.push(window.utils.simpleDateFormat(item.countDate,'MM-dd'));
		        				if(orderPrice){
		        					orderNumAry.push(item.countPrice);
		        				}else if(orderNum){
		        					orderNumAry.push(item.countNumber);
		        				}
		        				switch (day) {
								case 0:
									dayAry.push('周日');
									break;
								case 1:
									dayAry.push('周一');
									break;
								case 2:
									dayAry.push('周二');
									break;
								case 3:
									dayAry.push('周三');
									break;
								case 4:
									dayAry.push('周四');
									break;
								case 5:
									dayAry.push('周五');
									break;
								case 6:
									dayAry.push('周六');
									break;
							}
		        			})
		        			if(!isPrice&&!isAmount){
		        				columnChart(dayAry,orderNumAry);
		        			}else{
		        				lineChart(orderDateAry,orderNumAry);
		        			}
		        		}else{
		        			layer.msg('数据获取失败');
		        		}
	        		},
	        		error:function(error){
        				layer.msg('数据获取失败');
	        		}
        		})
        }  
        //订单数量柱状图
	    function columnChart(dayAry,orderNumAry){
		    $('#orderNumChart').highcharts({
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: ''
		        },
		        subtitle: {
		            text: ''
		        },
		        credits: {
		            enabled: false
		        },
		        xAxis: {
		            categories: dayAry,
		            crosshair: true
		        },
		        yAxis: {
		            min: 0,
		            title: {
		                text: ''
		            }
		        },
		        legend: {
		            enabled: false
		        },
		        tooltip: {
		            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		            '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
		            footerFormat: '</table>',
		            shared: true,
		            useHTML: true
		        },
		        plotOptions: {
		            column: {
		                pointPadding: 0.2,
		                borderWidth: 0
		            }
		        },
		        series: [{
		            name: '订单数量',
		            data: orderNumAry
		        }]
		    });
	    }
	    
        //订单详情折线图
        function lineChart(orderDateAry,orderNumAry){
	        $('#orderChart').highcharts({
		        chart: {
		            type: 'line'
		        },
		        title: {
		            text: ''
		        },
		        subtitle: {
		            text: ''
		        },
		        credits: {
		            enabled: false
		        },
		        xAxis: {
		            categories: orderDateAry
		        },
		        yAxis: {
		            title: {
		                text: ''
		            }
		        },
		        legend: {
		            enabled: false
		        },
		        plotOptions: {
		            line: {
		                dataLabels: {
		                    enabled: true          // 开启数据标签
		                },
		                enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
		            }
		        },
		        series: [{
		            name: '',
		            data: orderNumAry
		        }]
		    });
        }
            
      	//获取订单列表
	    	var pageSize = 5,pageNum=1,pageSum=1;
	    	getOrderList(pageNum);
	    	function getOrderList(pageNum){
	        var dataObj = new Object();
	        	dataObj.showCount = pageSize;
	        	dataObj.currentPage = pageNum;
	        	dataObj.orderCode = null;
	        	dataObj.relationName = $.trim($('#searchTxt').val());
	        	dataObj.relationSupplier = null;
	        	dataObj.startDate = null;
	        	dataObj.endDate =  null;
	        	dataObj.type = null;
	        	dataObj.payStatus = 1;
	        	dataObj.backManagerId = window.utils.getCookie('user_id');
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
					        str+='<div class="listItem" id="'+item.id+'"><h3><span class="time">'+item.createTime+'</span><span class="orderNum">订单号：'+(item.orderCode?item.orderCode:'')+'</span></h3> <ul class="itemCont clearfix"><li class="w25"><img src="'+(item.relationImgUrl?item.relationImgUrl:'')+'" alt=""><span>'+(item.relationName?item.relationName:'')+'</span></li><li class="w17">'+(item.relationPrice?item.relationPrice:0)+'</li><li class="w18">'+(item.price?item.price:0)+'</li><li class="w20">'+(item.userName?item.userName:'')+'</li><li class="w20"><button class="layui-btn delBtn">删除</button></li></ul></div>';
			        		})
	        			}else{
		        			str += '<div class="listItem noneData">暂无数据</div>';
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
	        			str += '<div class="listItem noneData">列表获取失败</div>';
	        		}
	        		$('.list-cont').html(str);
	        	},
	        	error:function(error){
	        		$('.list-cont').html('<div class="listItem noneData">列表获取失败</div>');
	        	}
	        })
        }  
	   //搜索
	   $('#search').on('click',function(){
	   		getOrderList(pageNum);
	   })  
	   //删除
	   $('.list-cont').delegate('.delBtn','click',function(){
			var id = $(this).parents('.listItem').attr('id');
			layer.open({
				type: 1
				,title:'提示'
				,content: '<p style="text-align: center; padding: 30px;">确定要删除此订单吗？</p>'
				,area: '370px'
				,btn: ['确定', '取消']
				,btnAlign: 'c'
				,yes: function(index){
					$('.layui-layer-btn').prepend('<span class="opa0"></span>');
		  			   $.get('/orderInfo/deleteOrderInfo?id='+id,function(res){
		  				   if(res.successful){
		  					   layer.msg('删除成功');
		  					   setTimeout(function(){
		  		       				window.location.reload(true);
		  		       			},1500);
		  				   }else{
		  					   $('.opa0').remove();
		  					   layer.msg(res.msg);
		  				   }
		  			   })
  			  		}
			})
	   })
    })
})