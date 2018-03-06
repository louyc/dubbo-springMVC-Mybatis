window.onload=function(){
	layui.use(['form', 'layedit', 'laydate','element','laypage','layer'], function(){
		  var form = layui.form(),layer = layui.layer,layedit = layui.layedit,
		  laydate = layui.laydate ,laypage = layui.laypage, element=layui.element();
		  var start = {
			    min: '1900-01-01'
			    ,max: '2099-06-16'
			    	,format: 'YYYY-MM-DD'
			    ,istoday: true
			    ,istime: true
			    ,choose: function(datas){
			      end.min = datas; //开始日选好后，重置结束日的最小日期
			      end.start = datas //将结束日的初始值设定为开始日
			    }
		    };
		    	  
			var end = {
			    min: '1900-01-01'
			    ,max: '2099-06-16'
			    ,format: 'YYYY-MM-DD'
			    ,istoday: true
			    ,istime: true
			    ,choose: function(datas){
			      start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			//订单统计日期
			$('#orderBeginTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#orderOverTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//用户分析日期
			$('#beginTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#overTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//注册统计日期
			$('#enrollBeginTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#endOverTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//转化率日期
			$('#date1').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#date2').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//推广码日期
			$('#payStartTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#payEndTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			$('#registStartTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#registEndTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//签约统计日期
			$('#sbeginTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#soverTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//家庭组统计日期
			$('#fbeginTime').click(function(){
				start.elem = this;
			    laydate(start);
			})
			$('#foverTime').click(function(){
				end.elem = this;
			    laydate(end);
			})
			//服务&商品统计日期
			$('#sgbeginTime').click(function(){
				start.elem = this;
//				start.format = 'YYYY-MM-DD hh:mm:ss';
			    laydate(start);
			})
			$('#sgoverTime').click(function(){
				end.elem = this;
//				end.format = 'YYYY-MM-DD hh:mm:ss';
			    laydate(end);
			})
          var layid = location.hash.replace(/^#test=/, '');
	          element.tabChange('test', layid);
	          element.on('tab(test)', function(elem){
	              location.hash = 'test='+ $(this).attr('lay-id');
	          });
	          
	        //切换tab
	          $('.user-tab li').click(function(){
		        	  var type = $(this).attr('type');
		        	  if(type == 0){
		        		  getOrderInfo();
		        		  /*getCont(1);*/
		        	  }else if(type == 1){
		        		  user(0);
		        	  }else if(type == 2){
		        		  enroll();
		        	  }else if(type == 3){
		        		  change();
		        	  }else if(type == 4){
		        		  code();
		        	  }else if(type == 5){
		        		  waitFor();
		        	  }else if(type == 6){
		        		  /*jiayCount();*/
		        		  timer = setInterval(function(){
		        	    	   jiayCount();
		        	       },60000);
		        	  }else if(type == 7){
		        		  getFamilyList();
		        	  }else if(type == 8){
		        		  getServiceGoodsList()
		        	  }else if(type == 9){
		        		  $('.itemOne').show();
		        		  $('.itemTwo').hide();
		        		  getSignList();
		        	  }
	          })
	          var pieHeight = $("body").height()-100;
	          $('#pieCharts').height(pieHeight);
	          $('#qyUserCharts').height(pieHeight);
	          $('#lyUserCharts').height(pieHeight);
//	          window.onresize = function(){
//		          var quanWid = $(".quan").width();
//		          $(".quan").height(quanWid);
//	          }
	          
	          /*请求代办*/
	          /*waitFor();*/
	          function waitFor(){
	        	  $.ajax({
	    	    	  url:'/message/scheduleCountMessages',
	    	    	  type:'GET',
	    	          success:function(data){
	    	        	  if(data.successful){
	    	        		  var quanWid = $(".quan").width();
	    	    	          $(".quan").height(quanWid);
	    	    	          window.onresize = function(){
	    	    	        	  window.location.reload();
	    	    	          }
	    	        		  /*圆环加载*/
	    	    	          var chart = new CircleChart({//用户
	    	    		    	    svg: svg1,
	    	    		    	    start: - Math.PI / 2,
	    	    		    	    margin: 1,
	    	    		    	    radius: 100,
	    	    		    	    title: data.data.userCount?data.data.userCount:"0",
	    	    		    	    titleSize: 24,
	    	    		    	    strokeWidth:10,
	    	    		    	    arcs: [
	    	    		    	         {
	    	    		    	            value: 1,
	    	    		    	            color: '#2C7CCA',
	    	    		    	            text: ""
	    	    		    	        }
	    	    		    	    ]
	    	    		       });
	    	    	          var chart = new CircleChart({//服务
	    	    		    	    svg: svg2,
	    	    		    	    start: - Math.PI / 2,
	    	    		    	    margin: 1,
	    	    		    	    radius: 100,
	    	    		    	    title: data.data.serviceCount?data.data.serviceCount:"0",
	    	    		    	    titleSize: 24,
	    	    		    	    strokeWidth:10,
	    	    		    	    arcs: [
	    	    		    	         {
	    	    		    	            value: 1,
	    	    		    	            color: '#F80809',
	    	    		    	            text: ""
	    	    		    	        }
	    	    		    	    ]
	    	    		       });
	    	    	          var chart = new CircleChart({//订单
	    	    		    	    svg: svg3,
	    	    		    	    start: - Math.PI / 2,
	    	    		    	    margin: 1,
	    	    		    	    radius: 100,
	    	    		    	    title: data.data.orderCount?data.data.orderCount:"0",
	    	    		    	    titleSize: 24,
	    	    		    	    strokeWidth:10,
	    	    		    	    arcs: [
	    	    		    	         {
	    	    		    	            value: 1,
	    	    		    	            color: '#1FA404',
	    	    		    	            text: ""
	    	    		    	        }
	    	    		    	    ]
	    	    		       });
	    	        	  }
	    	          }
	    	      })
	          };
           /*getOrderInfo();*/
          function getOrderInfo(){
        	  var pageSize=10,currentPage=1;
   	       	  var times = new Date();
	      	  var nowTime = $("#orderOverTime").val()||window.utils.simpleDateFormat(times,'yyyy-MM-dd');
	      	  var beforeTime = $("#orderBeginTime").val()||window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
	          var data = {showCount:pageSize,currentPage:currentPage,startDate:beforeTime,endDate:nowTime};
	          var type = $('#orderSelect').val();
	          data.type = type== 'all' ? null : type;
	          var dataJson = $.toJSON(data);
        	   //销售总数量
        	  $.ajax({
    	    	  url:'/orderInfo/countTotalAmount',
    	    	  type:'POST',
    	    	  data:dataJson,
    	    	  dataType:"json",
    	          contentType: "application/json",
    	          success:function(data){
    	        	  if(data.successful){
    	        		  var str='';
    	        		  str+='<p>'+(data.data?data.data:' ')+'</p>'
    	        	  }
    	        	  $(".orderCount").html(str);
    	          }
    	      })
	          //销售金额
	           $.ajax({
    	    	  url:'/orderInfo/countTotalPrice',
    	    	  type:'POST',
    	    	  data:dataJson,
    	    	  dataType:"json",
    	          contentType: "application/json",
    	          success:function(data){
    	        	  if(data.successful){
    	        		  var str='';
    	        		  str+='<p>'+(data.data?data.data:' ')+'</p>'
    	        	  }
    	        	  $(".sellCount").html(str);
    	          }
    	      })
    	      getCont(currentPage,pageSize)
          }
       
        //订单管理
         function getCont(currentPage,pageSize){
              var times = new Date();
          	  var nowTime = $("#orderOverTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
          	  var beforeTime = $("#orderBeginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
              var data = {showCount:pageSize,currentPage:currentPage,startDate:beforeTime,endDate:nowTime};
              var type = $('#orderSelect').val();
              data.type = type== 'all' ? null : type;
              var dataJson = $.toJSON(data);
        	  $.ajax({
         		url:'/orderInfo/countOrderInfo',
         		type:'POST',
         		data:dataJson,
         		dataType:"json",
                contentType: "application/json",
                success:function(data){
                 	if(data.successful){
                 		var str='',list=data.items;
                 		if(list.length==0){
                 			$('.layui-orderTable tbody').html('<tr><td colspan="4" style="text-align:center;">暂无数据</td></tr>')
                 			return false;
                 		}
                 		if(list.length>0){
                 			for(var i=0;i<list.length;i++){
	                 			str+='<tr><td>'+(list[i].countDate?list[i].countDate:'')+'</td><td>'+(list[i].countNumber?list[i].countNumber:0)+'</td><td>'+(list[i].countPrice?list[i].countPrice:0)+'</td></tr>'
	                 		}
                 		}
                 		// 分页
                    	laypage({
                    	    cont: 'pageTag'
                    	    ,pages: data.pagesCount
                    	    ,groups: 5 //连续显示分页数
                    	    ,curr:data.pageNumber || 1
                    	    ,jump: function(obj, first){
                    	    	if(!first){
                    	    		//得到了当前页，用于向服务端请求对应数据
                        		    currentPage = obj.curr;
                        		    getCont(currentPage);
                    	    	}
                    		}
                    	});
                    	$('.layui-orderTable tbody').html(str);
                    	currentPage = data.pageNumber*1;
                        totleCount = data.totalItemsCount*1;
                        pageCount = data.pagesCount*1;
                 	}else{
                 		layer.msg(data.resultCode.message);
                 	}
                 },error:function(error){
                     layer.msg('查询失败!');
                 }
         	})
         }    
       //订单搜索
        $(".search").click(function(){
  	        var startDate=$("#orderBeginTime").val();
  	        var endDate=$("#orderOverTime").val();
          	if(!startDate &&endDate){
          		layer.msg('请选择开始时间');
          		return;
          	}else if(startDate&&!endDate){
          		layer.msg('请选择结束时间');
          		return;
          	}
          	getCont(1);
          	getOrderInfo();
        })
        
        
        //注册统计
         $("#enrollSearch").click(function(){
  	        var startDate=$("#enrollBeginTime").val();
  	        var endDate=$("#endOverTime").val();
  	        if(!startDate &&endDate){
	        		layer.msg('请选择开始时间');
	        		return;
	        	}else if(startDate&&!endDate){
	        		layer.msg('请选择结束时间');
	        		return;
	        	}
          	enroll(1);	
        })
       
       var pageSizer=10,currPager=1;
       function  enroll(currPager){
        	  var times = new Date();
          	  var nowTime = $("#endOverTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
          	  var beforeTime =$("#enrollBeginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
              var data = {showCount:pageSizer,currentPage:currPager,startDate:beforeTime,endDate:nowTime};
              var dataJson=$.toJSON(data);
              $.ajax({
            	  url:'/oprationDate/regist',
            	  type:'POST',
	           	  data:dataJson,
	           	  dataType:"json",
                  contentType: "application/json",
                  success:function(data){
                	  $('#orderCountss').html(data.data);
                	  if(data.successful){
                   		var str='',list=data.items;
                   		if(list.length==0){
                   			$('.layui-orderTable tbody').html('<tr><td colspan="4" style="text-align:center;">暂无数据</td></tr>')
                   			return false;
                   		}
                   		if(list.length>0){
                   			for(var i=0;i<list.length;i++){
  	                 			str+='<tr><td >'+(list[i].countDate?list[i].countDate:'')+'</td><td>'+(list[i].countNumber?list[i].countNumber:0)+'</td><td><button class="layui-btn findMore">查看明细</button></td></tr>'
  	                 		}
                   		}
                      	laypage({
                      	    cont: 'pageTwo'
                      	    ,pages: data.pagesCount
                      	    ,groups: 5 
                      	    ,curr:data.pageNumber || 1
                      	    ,jump: function(obj, first){
                      	    	if(!first){
                      	    		currPager = obj.curr;
                          		  enroll(currPager);
                      	    	}
                      		}
                      	});
                      	$('.enroll tbody').html(str);
                   	}else{
                   		layer.msg(data.resultCode.message);
                   	}
                  },error:function(error){
                	  layer.msg('失败');
                  }
              })
        }
        
        //查看注册统计明细
        	$('.layui-table').delegate('.findMore','click',function(){
           		var val=$(this).parents('tr').children('td').eq(0).text();
           		$('#goods-info-box').attr('time',val);
           		more(currExplicit,val);
               	layer.open({
                	  type:1
                	   ,title:'详情'
                	   ,content: $('#goods-info-box')
                       ,area: ['800px','450px']
                })
           	})
           	
        	var pageExplicit=5,currExplicit=1;
           	function more(currExplicit,val){
           		var val = val || $('#goods-info-box').attr('time');
           		var data = {showCount:pageExplicit,currentPage:currExplicit,createTime:val};
           		var dataJson=$.toJSON(data);
       			$.ajax({
               		url:'/oprationDate/detaile',
               		type:'POST',
               		data:dataJson,
               		dataType:"json",
                    contentType: "application/json",
                    success:function(result){
                   	 if(result.successful){
                   		var list=result.items;
                       	var str='';
                       	for(var i=0;i<list.length;i++){
                       		var item=list[i].sex;
                       		if(item==0){
                       			item='保密'
                       		}else if(item==1){
                       			item='男'
                       		}else if(item==2){
                       			item='女'
                       		}
                       		var mobile=list[i].mobile;
                       		if(mobile==null){
                       			mobile='子账户'
                       		}
                       		var itemName=list[i].name;
                       		if(itemName==null){
                       			itemName='未填写'
                       		}
                       		var itemBir=list[i].birthday;
                       		if(itemBir==null){
                       			itemBir='未填写'
                       		}
                       		str+='<tr><td>'+itemName+'</td><td>'+mobile+'</td><td>'+item+'</td><td>'+itemBir+'</td><td>'+(list[i].createTime?list[i].createTime:'')+'</td></tr>';
                       	}
                       	laypage({
                         	    cont: 'pageExplicit'
                         	    ,pages: result.pagesCount
                         	    ,groups: 5 
                         	    ,curr:result.pageNumber || 1
                         	    ,jump: function(obj, first){
                         	    	if(!first){
                         	    		currExplicit = obj.curr;
                        	    		more(currExplicit,val);
                         	    	}
                         		}
                         	});
                       	$('.explicit tbody').html(str);
                   	}
                   }
           	})
       	}
        //转化率
        $("#changeSearch").click(function(){
  	        var startDate=$(".changeStartTime").val();
  	        var endDate=$(".changeEndTime").val();
  	        if(!startDate &&endDate){
	        		layer.msg('请选择开始时间');
	        		return;
	        	}else if(startDate&&!endDate){
	        		layer.msg('请选择结束时间');
	        		return;
	        	}
          	change(1);	
        })
        
        var pageSizez=10,currPage=1;
        function change(currPage){
        	 var times = new Date();
         	 var nowTime = $(".changeEndTime").val()||window.utils.simpleDateFormat(times,'yyyy-MM-dd');
         	 var beforeTime =$(".changeStartTime").val()||window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
             var data = {showCount:pageSizez,currentPage:currPage,startDate:beforeTime,endDate:nowTime};
             var dataJson=$.toJSON(data);
             $.ajax({
            	 url:'/oprationDate/conversion',
            	 type:'POST',
            	 data:dataJson,
            	 dataType:"json",
                 contentType: "application/json",
                 success:function(items){
           		  $(".visitorCount").html(items.data.visitorSum);
           		  $(".purchaserCount").html(items.data.purchaserSum);
           		  $(".conversionRate").html(items.data.conversionSum);
                	  if(items.successful){
                     		var str='',list=items.items;
                     		if(list.length==0){
                     			$('.change_Table tbody').html('<tr><td colspan="4" style="text-align:center;">暂无数据</td></tr>')
                     			return false;
                     		}
                     		if(list.length>0){
                     			for(var i=0;i<list.length;i++){
                     				str+='<tr><td>'+(list[i].countDate?list[i].countDate:'')+'</td><td>'+(list[i].visitorCount?list[i].visitorCount:0)+'</td><td>'+(list[i].purchaserCount?list[i].purchaserCount:0)+'</td><td>'+(list[i].conversionRate?list[i].conversionRate:0)+'</td></tr>'  	                 			
    	                 		}
                     		}
                        	laypage({
                        	    cont: 'pageThree'
                        	    ,pages: items.pagesCount
                        	    ,groups: 5 
                        	    ,curr:items.pageNumber || 1
                        	    ,jump: function(obj, first){
                        	    	if(!first){
                        	    		currPage = obj.curr;
                            		    change(currPage);
                        	    	}
                        		}
                        	});
                        	$('.change_Table tbody').html(str);
                     	}else{
                     		layer.msg('失败');
                     	}
                 	},error:function(error){
                 		layer.msg('失败');
                 	}
             })
        }
        
       
        //推广码
        $("#codeSearch").click(function(){
          	code(1)
        })
        
    	var pageSizet=10,currPaget=1;
        function code(currPaget){
        	 var doctorName=$("#organization").val();
        	 var promotioname=$("#promoter").val();
        	 var times = new Date();
         	 var buyNowTime = $("#payEndTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
         	 var buyBeforeTime =$("#payStartTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
         	 var stopNowTime = $("#registEndTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
        	 var stopBeforeTime =$("#registStartTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
             var data = {showCount:pageSizet,currentPage:currPaget,registStartTime:stopBeforeTime,registEndTime:stopNowTime,payStartTime:buyBeforeTime,payEndTime:buyNowTime,promotionName:promotioname,doctorName:doctorName};
             var dataJson=$.toJSON(data);
             $.ajax({
            	 url:'/oprationDate/qrcode',
            	 type:'POST',
            	 data:dataJson,
            	 dataType:"json",
                 contentType: "application/json",
                 success:function(items){
                	 $('.promotionName').html(items.data.sumPeople);
                	 $('.orderPrice').html(items.data.sumPrice);
                	 if(items.successful){
                  		var str='',list=items.items;
                  		if(list.length==0){
                  			$('.codeTable tbody').html('<tr><td colspan="8" style="text-align:center;">暂无数据</td></tr>')
                  			return false;
                  		}
                  		if(list.length>0){
                  			for(var i=0;i<list.length;i++){
                  				str+='<tr><td class="codeUserId">'+(list[i].userName?list[i].userName:'')+'</td><td id="promotionName">'+(list[i].promotionName?list[i].promotionName:'')+'</td><td>'+(list[i].doctorName?list[i].doctorName:'')+'</td><td id="loginTime">'+(list[i].loginTime?list[i].loginTime:'')+'</td><td id="orderId">'+(list[i].orderCode?list[i].orderCode:'')+'</td><td id="payTime">'+(list[i].payTime?list[i].payTime:'')+'</td><td id="orderName">'+(list[i].orderName?list[i].orderName :'')+'</td><td id="orderPrice">'+(list[i].orderPrice?list[i].orderPrice:0)+'</td></tr>'  	                 			
 	                 		}
                  		}
                     	laypage({
                     	    cont: 'pageFour'
                     	    ,pages: items.pagesCount
                     	    ,groups: 5 
                     	    ,curr:items.pageNumber || 1
                     	    ,jump: function(obj, first){
                     	    	if(!first){
                     	    		currPaget = obj.curr;
                         		   code(currPaget);
                     	    	}
                     		}
                     	});
                     	$('.codeTable tbody').html(str);
                  	}else{
                  		layer.msg('失败');
                  	}
                 },error:function(error){
                	 layer.msg('获取推广码失败');
                 }
             })
        }
       
        //导出详情
       $('#export').on('click',function(){
	   		var query = '?';
	   		var buyStartData = $.trim($('.buyStartData').val());
	        var relationName = $.trim($('.relationName').val());
	        var stopStartData = $.trim($('.stopStartData').val());
	        var stopEndData = $('.stopEndData').val();
	       	var organization =  $('#organization').val();
	        var promoter = $('#promoter').val();
	       	if(buyStartData)  query += 'buyStartData='+buyStartData+'&';
	       	if(relationName)  query += 'relationName='+relationName+'&';
	       	if(stopStartData)  query += 'stopStartData='+stopStartData+'&';
	       	if(stopEndData)  query += 'stopEndData='+stopEndData+'&';
	       	if(organization)  query += 'organization='+organization+'&';
	       	if(promoter)  query += 'promoter='+promoter+'&';
	
	       	$(this).attr('href','/oprationDate/export'+query);
	   });
        
      //用户分析
        $('.userTab li').on('click',function(){
	        var type=$(this).attr('type');
	        user(type);
	    })
	    $("#userSearch").click(function(){
	    	 	var startDate=$("#beginTime").val();
  	        var endDate=$("#overTime").val();
  	        if(!startDate &&endDate){
	      		layer.msg('请选择开始时间');
	      		return;
	      	}else if(startDate&&!endDate){
	      		layer.msg('请选择结束时间');
	      		return;
	      	}
          	user(null);
        })
	    
       function user(type){
        	var times = new Date();
        	var nowTime = $("#overTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
        	var beforeTime =$("#beginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
           	type = type || $('.userTab .layui-this').attr('type');
            var data = {startDate:beforeTime,endDate:nowTime,type:type};
            var dataJson=$.toJSON(data);
            $.ajax({
            	url:'/oprationDate/userAnalysis',
            	type:'POST',
            	data:dataJson,
            	dataType:"json",
                contentType: "application/json",
                success:function(data){
                	if(data.successful){
                  		var orderDateAry=[],numObj=new Object(),orderManNumAry=[],orderWomanNumAry=[],orderSecretNumAry=[],childrenCount=[],juvenileCount=[],youthCount=[],middleAgeCount=[],oldAgeCount=[],noAgeCount=[];
                  		var countDateList=[],proAry=[];
                  		if(type == 1){
        					countDateList=data.data.countDateList;
        					$.each(data.data.proviceList,function(index,item){
        						var dataAry = [];
        						$.each(item.data.split(','),function(index,item){
        							dataAry.push(Number(item));
        						})
        						var proObj = {data:dataAry,name:item.name};
        						proAry.push(proObj);
        					})
        				}else{
                     		 $.each(data.data,function(index,item){
     	        				orderDateAry.push(window.utils.simpleDateFormat(item.countDate,'yyyy-MM-dd'));
     	        				if(type == 0){
     	        					orderManNumAry.push(item.manCount);
     		        				orderWomanNumAry.push(item.womanCount);
     		        				orderSecretNumAry.push(item.secretCount);
     	        				}else if(type == 2){
     	        					childrenCount.push(item.childrenCount);
     		        				juvenileCount.push(item.juvenileCount);
     		        				youthCount.push(item.youthCount);
     		        				middleAgeCount.push(item.middleAgeCount);
     		        				oldAgeCount.push(item.oldAgeCount);
     		        				noAgeCount.push(item.noAgeCount);
     	        				}
     		        				
                    		 })
        				}
                		numObj = {man:orderManNumAry,woman:orderWomanNumAry,secret:orderSecretNumAry,children:childrenCount,juven:juvenileCount,youth:youthCount,middleAge:middleAgeCount,oldAge:oldAgeCount,noAge:noAgeCount};
                   		 if(type == 0){
                   			 dataAry = [{name: '男',data: numObj.man},{name: '女',data: numObj.woman},{name: '保密',data: numObj.secret}];
                   			 columnSexChart('first-container',orderDateAry,dataAry);
                   		 }else if(type == 1){
                   		 	columnAreaChart('second-container',countDateList,proAry);
                   		 }else if(type == 2){
                   			 dataAry = [{name: '儿童',data: numObj.children},{name: '少年',data: numObj.juven},{name: '青年',data: numObj.youth},{name: '中年',data: numObj.middleAge},{name: '老年',data: numObj.oldAge},{name: '未知',data: numObj.noAge}];          
                   			 columnSexChart('third-container',orderDateAry,dataAry);
               		 	}                 	
                  	 }
                },
                error:function(error){
                	layer.msg('获取失败');
                }
            })
        }
      //性别、年龄
        var orderDateAry=[],numObj=new Object();
        function columnSexChart(ele,orderDateAry,dataAry){
           $('#'+ele).highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: ''
                },
                xAxis: {
                    categories: orderDateAry
                },
               yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                   pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0">{point.y}</td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                series: dataAry
           });
          }
        //区域  
       function columnAreaChart(eve,countDateList,proAry){
	        	$('#'+eve).highcharts({
	        		chart: {
	    		        type:'column'
	    		   },
	    		   title: {
	                   text: ''
	               },
	               xAxis: {
	                   categories: countDateList                               
	               },
	               yAxis: {
	                   min: 0,
	                   title: {
	                       text: ''
	                   }
	               },
	               tooltip: {
	                   headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	                   pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                   '<td style="padding:0">{point.y}</td></tr>',
	                   footerFormat: '</table>',
	                   shared: true,
	                   useHTML: true
	               },
	               series: proAry
	        	})
        }
       //签约搜索
       $('#sSearch').click(function(){
    	   		var startDate = $('#sbeginTime').val();
    	   		var endDate = $('#soverTime').val();
    	   		if(!startDate &&endDate){
	      		layer.msg('请选择开始时间');
	      		return;
	      	}else if(startDate&&!endDate){
	      		layer.msg('请选择结束时间');
	      		return;
	      	}
    	   		getSignList();
       })
       //获取签约统计列表
       var signData = {pageSize:10,currPage:1,buildType:0};
       function getSignList(){
    	   		var times = new Date();
	   		var startTime = $('#sbeginTime').val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
	   		var endTime = $('#soverTime').val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
	   		signData.buildType = $('#sign').val();
    	   		var dataObj = {showCount:signData.pageSize,currentPage:signData.currPage,startDate:startTime,endDate:endTime,buildType:signData.buildType};
	    	   $.ajax({
	           	url:'/oprationDate/sign',
	           	type:'POST',
	           	data:$.toJSON(dataObj),
	           	dataType:"json",
	            contentType: "application/json",
	            success:function(res){
	            		if(res.successful){
		            		var signHtml = '';
	            			$.each(res.items,function(index,item){
	            				signHtml += '<tr><td>'+item.countDate+'</td><td>'+item.signCount+'</td><td><button class="layui-btn viewDetail">查看详情</button><a href="javascript:;" class="layui-btn signAnalysis">签约分析</a></td></tr>'
	            			})
	            			if(signHtml == ''){
	            				 $('#sign-box tbody').html('<tr><td colspan="3">暂无数据</td></tr>')
	            			}else{
	            				$('#sign-box tbody').html(signHtml);
	            				$('#sign-box tbody').append('<tr><td>合计</td><td>'+res.data.signSum+'</td><td></td></tr>');
	            			}
	            			$('#sign-info-box').attr('type',signData.buildType);
	            			laypage({
                     	    cont: 'signPage'
                     	    ,pages: res.pagesCount
                     	    ,groups: 5 
                     	    ,curr:res.pageNumber || 1
                     	    ,jump: function(obj, first){
                     	    	if(!first){
                     	    		signData.currPage = obj.curr;
                     	    		getSignList();
                     	    	}
                     		}
                     	});
	            		}else{
	            			$('#sign-box tbody').html('<tr><td colspan="3">请求数据失败</td></tr>')
	            		}
	        	    }
           })
       }
     //查看签约统计详情
       $('#sign-box').delegate('.viewDetail','click',function(){
    	   		viewDetail('#sign-info-box');
    	   		var tdDate = $(this).parents('tr').find('td').eq(0).text();
    	   		getSignDetail(tdDate);
       })
       var signDetailData = {pageSize:10,currPage:1};
       function getSignDetail(tdDate){
    	   		dataObj = {showCount:signDetailData.pageSize,currentPage:signDetailData.currPage,countDate:tdDate,buildType:signData.buildType}
    	   		$.ajax({
	           	url:'/oprationDate/signDetaile',
	           	type:'POST',
	           	data:$.toJSON(dataObj),
	           	dataType:"json",
	            contentType: "application/json",
	            success:function(res){
		            	if(res.successful){
		            		var signHtml = '';
	            			$.each(res.items,function(index,item){
	            				signHtml += '<tr><td>'+(item.cardId?item.cardId:'----')+'</td><td>'+(item.mobile?item.mobile:'----')+'</td><td>'+(item.signName?item.signName:'----')+'</td><td>'+(item.sex == 1?'男':(item.sex == 2 ?'女':'保密'))+'</td><td>'+(item.age?item.age:'保密')+'</td><td>'+(item.presentAddress?item.presentAddress:'----')+'</td><td>'+(item.diseaseNames?item.diseaseNames:'----')+'</td><td>'+(item.doctorName?item.doctorName:'----')+'</td><td>'+(item.orgName?item.orgName:'----')+'</td><td>'+item.createTime+'</td></tr>'
	            			})
	            			if(signHtml == ''){
	            				 $('#sign-info-box tbody').html('<tr><td colspan="9">暂无数据</td></tr>')
	            			}else{
	            				$('#sign-info-box tbody').html(signHtml);
	            			}
	            			$('#sign-info-box').attr('countDate',tdDate);
	            			laypage({
	                 	    cont: 'sInfoPage'
	                 	    ,pages: res.pagesCount
	                 	    ,groups: 5 
	                 	    ,curr:res.pageNumber || 1
	                 	    ,jump: function(obj, first){
	                 	    	if(!first){
	                 	    		signDetailData.currPage = obj.curr;
	                 	    		getSignDetail(tdDate);
	                 	    	}
	                 		}
	                 	});
	            		}else{
	            			$('#sign-info-box tbody').html('<tr><td colspan="9">请求数据失败</td></tr>')
	            		}
	            	}
            })
       }
       //签约分析
       $('#sign-box').delegate('.signAnalysis','click',function(){
	    	   	$('.itemOne').hide();
	    	   	$('.itemTwo').show();
	    	   	$('.itemTwo iframe').attr('src','data/statistics.html');
       })
       //导出签约列表
       $('#sExport').click(function(){
    	   		var times = new Date();
	   		var startTime = $('#sbeginTime').val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
	   		var endTime = $('#soverTime').val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
	   		var buildType = $('#sign').val();
    	   		var query = '/excelExport/sign?startDate='+startTime+'&endDate='+endTime+'&buildType='+buildType;
    	   		$(this).attr('href',query)
       })
       //导出签约详情
       $('#signDetailExport').click(function(){
		   	var countDate = $('#sign-info-box').attr('countDate');
	   		var buildType = $('#sign-info-box').attr('type');
	   		var query = '/excelExport/signDetaile?countDate='+countDate+'&buildType='+buildType;
	   		$(this).attr('href',query)
       })
       //家庭组统计搜索
       $('#fSearch').click(function(){
    	   		var startDate = $('#fbeginTime').val();
	   		var endDate = $('#foverTime').val();
	   		if(!startDate &&endDate){
	     		layer.msg('请选择开始时间');
	     		return;
	     	}else if(startDate&&!endDate){
	     		layer.msg('请选择结束时间');
	     		return;
	     	}
    	   		getFamilyList();
       })
       //获取家庭组统计列表
       var familyData = {pageSize:10,currPage:1};
       function getFamilyList(){	
    	   		var times = new Date();
	   		var startTime = $('#fbeginTime').val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
	   		var endTime = $('#foverTime').val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
   	   		var dataObj = {showCount:familyData.pageSize,currentPage:familyData.currPage,startDate:startTime,endDate:endTime};
	    	   $.ajax({
	           	url:'/oprationDate/familyAnalysis',
	           	type:'POST',
	           	data:$.toJSON(dataObj),
	           	dataType:"json",
	            contentType: "application/json",
	            success:function(res){
	            		if(res.successful){
		            		var signHtml = '';
	            			$.each(res.items,function(index,item){
	            				signHtml += '<tr pId="'+item.parentIds+'"><td>'+item.createTime+'</td><td>'+item.countNumber+'</td><td><button class="layui-btn viewDetail">查看详情</button></td></tr>'
	            			})
	            			if(signHtml == ''){
	            				 $('#family-box tbody').html('<tr><td colspan="3">暂无数据</td></tr>')
	            			}else{
	            				$('#family-box tbody').html(signHtml);
	            			}
	            			laypage({
                     	    cont: 'familyPage'
                     	    ,pages: res.pagesCount
                     	    ,groups: 5 
                     	    ,curr:res.pageNumber || 1
                     	    ,jump: function(obj, first){
                     	    	if(!first){
                     	    		familyData.currPage = obj.curr;
                     	    		getFamilyList();
                     	    	}
                     		}
                     	});
	            		}else{
	            			$('#family-box tbody').html('<tr><td colspan="3">请求数据失败</td></tr>')
	            		}
	        	    }
          })
      }
     //详情搜索
       $('#searchBtn').click(function(){
    	   		var tId = $('#family-info-box').attr('tId');
    	   		getFamilyDetail(tId)
       })
     //查看家庭组统计详情
       $('#family-box').delegate('.viewDetail','click',function(){
    	   		viewDetail('#family-info-box');
    	   		var pId = $(this).parents('tr').attr('pId');
    	   		getFamilyDetail(pId);
       })
       function getFamilyDetail(pId){
    	   		var parentMobile = $.trim($('#regCount').val());
    	   		var parentName = $.trim($('#resName').val());
    	   		dataObj = {parentMobile:parentMobile,parentName:parentName,parentIds:pId,buildType:0}
    	   		$.ajax({
	           	url:'/oprationDate/familyDetail',
	           	type:'POST',
	           	data:$.toJSON(dataObj),
	           	dataType:"json",
	            contentType: "application/json",
	            success:function(res){
		            	if(res.successful){
		            		var signHtml = '';
	            			$.each(res.data,function(index,item){
	            				signHtml += '<tr><td>'+(item.parentMobile?item.parentMobile:'----')+'</td><td>'+(item.parentName?item.parentName:'----')+'</td><td><table class="layui-table" lay-skin="nob"><tbody>';
	            				$.each(item.listApiUser,function(index,user){
	            					signHtml += '<tr><td>'+(user.mobile?user.mobile:'----')+'</td><td>'+(user.name?user.name:'----')+'</td><td>'+(user.roleName?user.roleName:'----')+'</td><td>'+(user.createTime?user.createTime:'----')+'</td></tr>'
	            				})
            					signHtml += '</tbody></table></td></tr>'
	            			})
	            			if(signHtml == ''){
	            				 $('#family-info-box tbody').html('<tr><td colspan="3">暂无数据</td></tr>')
	            			}else{
	            				$('#family-info-box tbody').html(signHtml);
	            			}
	            			$('#family-info-box').attr('tId',pId);
	            		}else{
	            			$('#family-info-box tbody').html('<tr><td colspan="3">请求数据失败</td></tr>')
	            		}
	            	}
            })
       }
      //按照汇总对象赛选展示
//       form.on('select(summaryObject)',function(data){
//    	   		if(data.value == '0'){
//    	   			$('.staticsTable tr').children().show().eq(1).hide();
//    	   		}else if(data.value == '1'){
//    	   			$('.staticsTable tr').children().show().eq(0).hide();
//    	   		}else{
//    	   			$('.staticsTable tr').children().show();
//    	   		}
//       })
     //服务&商品统计搜索
       $('#sgSearch').click(function(){
    	   		var startDate = $('#sgbeginTime').val();
	   		var endDate = $('#sgoverTime').val();
	   		if(!startDate &&endDate){
	     		layer.msg('请选择开始时间');
	     		return;
	     	}else if(startDate&&!endDate){
	     		layer.msg('请选择结束时间');
	     		return;
	     	}
    	   		getServiceGoodsList();
       })   
       //按金额排序
       var sortFlag = false,sortFlagn = false;
       $('.sortByPrice').click(function(){
  			$(this).find('i').toggleClass('tran180');
    	   		var sort = 'ASC';
    	   		if(sortFlag){
    	   			serviceGoodsData.sortVal = 'ASC';
    	   			sortFlag = false;
    	   		}else{
    	   			serviceGoodsData.sortVal = 'DESC'
   				sortFlag = true;
    	   		}
    	   		serviceGoodsData.sortColumn = 'price_count';
    	   		getServiceGoodsList();
       })
       //按数量排序
       $('.sortByCount').click(function(){
    	   		$(this).find('i').toggleClass('tran180');
    	   		var sort = 'ASC';
	   		if(sortFlagn){
	   			serviceGoodsData.sortVal = 'ASC';
	   			sortFlagn = false;
	   		}else{
	   			serviceGoodsData.sortVal = 'DESC'
				sortFlagn = true;
	   		}
	   		serviceGoodsData.sortColumn = 'buy_count';
    	   		getServiceGoodsList();
       })
       //获取服务&商品统计列表
       var serviceGoodsData = {pageSize:10,currPage:1,sortColumn:'buy_count',sortVal:'ASC',summaryObject:null};
       function getDataObj(){
	   		var startTime = $('#sgbeginTime').val();
	   		var endTime = $('#sgoverTime').val();
	   		var staticsType = $('#staticsType').val();
	   		var type = null;
	   		var keyWordsType = null;
	   		if(staticsType == 'all'){
	   			type = null;
	   			keyWordsType = null;
	   		}else{
	   			staticsType = staticsType.split(',');
		   		type = staticsType[0];
		   		keyWordsType = type == 0 ? null : staticsType[1];
	   		} 
	   		staticsType = staticsType == 'all' && null;
	   		var summaryObject = $('#summaryObject').val();
	   		serviceGoodsData.summaryObject = summaryObject == 'all' ? null : summaryObject;
//	   		var displayMode = $('#displayMode').val();
//	   		displayMode = displayMode == 'all' ? null : displayMode;
	   		var serviceGoodsName = $.trim($('#serviceGoodsName').val());
	   		var supplierCreatorName = $.trim($('#supplierCreatorName').val());
   	   		var dataObj = {showCount:serviceGoodsData.pageSize,currentPage:serviceGoodsData.currPage,startTime:startTime,endTime:endTime,relationName:serviceGoodsName,relationSupplier:supplierCreatorName,type:type,keyWordsType:keyWordsType,groupColumn:serviceGoodsData.summaryObject,orderColumn:[{orderColumnName:serviceGoodsData.sortColumn,sortType:serviceGoodsData.sortVal}]};

   	   		serviceGoodsDetailData.serviceDetail = dataObj;
   	   		return dataObj;
       }
       function getServiceGoodsList(){
    	   		var dataObj = getDataObj();
    	   		$('#sendData').val(JSON.stringify(dataObj));
   	   		$.ajax({
	           	url:'/orderStatistic/getOrderPurchaseArray',
	           	type:'POST',
	           	data:$.toJSON(dataObj),
	           	dataType:"json",
	            contentType: "application/json",
	            success:function(res){
	            		if(res.successful){
		            		var signHtml = '';
	            			$.each(res.items,function(index,item){
	            				if(dataObj.groupColumn == 0){
	            					$('.staticsTable col').show().eq(1).hide();
	            					$('.staticsTable tr').children().show().eq(1).hide();
		            				signHtml += '<tr><td relId="'+item.relationId+'">'+item.relationName+'</td>';
	            				}else if(dataObj.groupColumn == 1){
	            					$('.staticsTable col').show().eq(0).hide();
	            					$('.staticsTable tr').children().show().eq(0).hide();
		            				signHtml += '<tr><td supId="'+item.relationSupplierId+'">'+item.relationSupplier+'</td>';
	            				}else{
	            					$('.staticsTable col').show();
	            					$('.staticsTable tr').children().show();
		            				signHtml += '<tr><td relId="'+item.relationId+'">'+item.relationName+'</td><td supId="'+item.relationSupplierId+'">'+item.relationSupplier+'</td>';
	            				}
	            				signHtml += '<td>'+item.buyCount+'</td><td>'+item.priceCount+'</td><td><button class="layui-btn vdGoods">查看详情</button></td></tr>'
	            			})
	            			if(signHtml == ''){
		            			if(dataObj.groupColumn == 0 || dataObj.groupColumn == 1){
		            				 $('#statics-box tbody').html('<tr><td colspan="4">暂无数据</td></tr>')
	            				}else{
		            				 $('#statics-box tbody').html('<tr><td colspan="5">暂无数据</td></tr>')
	            				}
	            			}else{
	            				$('#statics-box tbody').html(signHtml);
	            				if(dataObj.groupColumn == 0 || dataObj.groupColumn == 1){
			            			$('#statics-box tbody').append('<tr><td>合计</td><td>'+res.data.countNumber+'</td><td>'+res.data.countPrice+'</td><td></td></tr>');
	            				}else{
			            			$('#statics-box tbody').append('<tr><td>合计</td><td></td><td>'+res.data.countNumber+'</td><td>'+res.data.countPrice+'</td><td></td></tr>');
	            				}
	            			}
	            			laypage({
                     	    cont: 'staticsPage'
                     	    ,pages: res.pagesCount
                     	    ,groups: 5 
                     	    ,curr:res.pageNumber || 1
                     	    ,jump: function(obj, first){
	                     	    	if(!first){
	                     	    		serviceGoodsData.currPage = obj.curr;
	                     	    		getServiceGoodsList();
	                     	    	}
                     		}
                     	});
	            		}else{
	            			$('#statics-box tbody').html('<tr><td colspan="5">请求数据失败</td></tr>')
	            		}
	        	    }
          })
      }
       //查看服务&商品统计详情
       $('#statics-box').delegate('.vdGoods','click',function(){
    	   		viewDetail('#service-info-box');
			var relationId = '';
			var relationSupplierId = '';
    	   		if(serviceGoodsData.summaryObject == 0){
        	   		relationId = $(this).parents('tr').find('td').eq(0).attr('relId');
			}else if(serviceGoodsData.summaryObject == 1){
	    	   		relationSupplierId = $(this).parents('tr').find('td').eq(0).attr('supId');
			}else{
	    	   		relationId = $(this).parents('tr').find('td').eq(0).attr('relId');
	    	   		relationSupplierId = $(this).parents('tr').find('td').eq(1).attr('supId');
			}
    	   		getServiceGoodsDetail(relationId,relationSupplierId);
       })
       var serviceGoodsDetailData = {pageSize:10,currPage:1,serviceDetail:null};
       function getServiceGoodsDetail(relId,supId){
    	   		dataObj = serviceGoodsDetailData.serviceDetail;
    	   		dataObj.currentPage = serviceGoodsDetailData.currPage;
    	   		dataObj.relationId = relId;
    	   		dataObj.relationSupplierId = supId;
    	   		console.log(relId,supId)
    	   		$('#sendDetailData').val(JSON.stringify(dataObj));
    	   		$.ajax({
	           	url:'/orderStatistic/getPurchaseDetailArray',
	           	type:'POST',
	           	data:$.toJSON(dataObj),
	           	dataType:"json",
	            contentType: "application/json",
	            success:function(res){
		            	if(res.successful){
		            		var signHtml = '';
	            			$.each(res.items,function(index,item){
	            				if(dataObj.groupColumn == 0){
	            					$('.goodsTable col').show().eq(0).hide();
	            					$('.goodsTable tr').children().show().eq(1).hide();
	            					signHtml += '<tr><td>'+(item.relationName?item.relationName:'----')+'</td>';
	            				}else if(dataObj.groupColumn == 1){
	            					$('.goodsTable col').show().eq(1).hide();
	            					$('.goodsTable tr').children().show().eq(0).hide();
	            					signHtml += '<tr><td>'+(item.relationSupplier?item.relationSupplier:'----')+'</td>';
	            				}else{
	            					$('.goodsTable col').show();
	            					$('.goodsTable tr').children().show();
	            					signHtml += '<tr><td>'+(item.relationName?item.relationName:'----')+'</td><td>'+(item.relationSupplier?item.relationSupplier:'----')+'</td>';
	            				}
	            				signHtml += '<td>'+(item.userMobile?item.userMobile:'----')+'</td><td>'+(item.userName?item.userName:'----')+'</td><td>'+(item.orderCode?item.orderCode:'----')+'</td><td>'+(item.orderCount?item.orderCount:'0')+'</td><td>'+(item.price?item.price:'0')+'</td><td>'+item.createTime+'</td></tr>'
	            			})
	            			if(signHtml == ''){
	            				 $('#service-info-box tbody').html('<tr><td colspan="7">暂无数据</td></tr>')
	            			}else{
	            				$('#service-info-box tbody').html(signHtml);
	            			}
	            			laypage({
	                 	    cont: 'sgInfoPage'
	                 	    ,pages: res.pagesCount
	                 	    ,groups: 5 
	                 	    ,curr:res.pageNumber || 1
	                 	    ,jump: function(obj, first){
	                 	    	if(!first){
	                 	    		serviceGoodsDetailData.currPage = obj.curr;
	                 	    		getServiceGoodsDetail();
	                 	    	}
	                 		}
	                 	});
	            		}else{
	            			$('#service-info-box tbody').html('<tr><td colspan="7">请求数据失败</td></tr>')
	            		}
	            	}
            })
       }
       	//查看详情
       	function viewDetail(ele){
           	layer.open({
          	  type:1
          	   ,title:'详情'
          	   ,content: $(ele)
               ,area: ['900px','450px']
           	   ,cancel: function(){ 
           		   $('#regCount,#resName').val('');
                }
          })
       	}
       
       jiayCount();
       clearInterval(timer);
       var timer = setInterval(function(){
    	   jiayCount();
       },60000);
       function jiayCount(){
    	   var seriesData=[];
    	   var qySeriesData=[];
    	   var lySriesData=[];
    	   $.ajax({
 	    	  url:'/sbOrderStatistic/getSbDutiesCount',
 	    	  type:'GET',
 	    	  async:false,
 	          success:function(data){
 	        	  if(data.successful){
 	        		  if(!!data.data){
 	        			  $("#qianyuePeople span").html(data.data.signCount)
 	        			  $("#lvyuePeople span").html(data.data.dutiesCount)
 	        			  var lvyueRate = GetPercent(data.data.dutiesCount, data.data.signCount);
 	        			  $("#lvyueRate span").html(lvyueRate);
 	        			  for(let item of data.data.dutiesList){
 	        				 var obj = new Object();
 	        				 obj.name=item.dutiesName;
        					 obj.y=item.dutiesCount;
    						 obj.url="countPie.html";
    						 obj.dutiesKey=item.dutiesKey;
 	        				 seriesData.push(obj);
 	        			  }
 	        		  }else{
 	        			  layer.msg("暂无数据！")
 	        		  }
 	        	  }
 	          }
 	       })
 	       $.ajax({
 	    	   url:'/sbOrderStatistic/getSbOrgUserCount',
 	    	   type:'GET',
 	    	   async:false,
 	    	   success:function(data){
 	    		   if(data.successful){
 	    			   if(!!data.data){
 	    				   for(let item of data.data){
 	    					   var obj1 = new Object();
 	    					   obj1.name=item.dutiesName;
 	    					   obj1.y=item.dutiesCount;
 	    					   obj1.dutiesKey=item.dutiesKey;
 	    					   qySeriesData.push(obj1);
 	    				   }
 	    			   }else{
 	    				   layer.msg("暂无数据！")
 	    			   }
 	    		   }
 	    	   }
 	       })
 	       $.ajax({
 	    	   url:'/sbOrderStatistic/getSbOrgDutiesCount',
 	    	   type:'GET',
 	    	   async:false,
 	    	   success:function(data){
 	    		   if(data.successful){
 	    			   if(!!data.data){
 	    				   for(let item of data.data){
 	    					   var obj = new Object();
 	    					   obj.name=item.dutiesName;
 	    					   obj.y=item.dutiesCount;
 	    					   obj.dutiesKey=item.dutiesKey;
 	    					   lySriesData.push(obj);
 	    				   }
 	    			   }else{
 	    				   layer.msg("暂无数据！")
 	    			   }
 	    		   }
 	    	   }
 	       })
           $('#pieCharts').highcharts({
               chart: {
                   plotBackgroundColor: null,
                   plotBorderWidth: null,
                   plotShadow: false
               },
               title: {
                   text: '履约服务分布图'
               },
               subtitle: {
    			   text: '单位（人次）'
    		   },
               tooltip: {
                   headerFormat: '{series.name}<br>',
                   pointFormat: '{point.name}: {point.y} ，<b>{point.percentage:.1f}%</b>'
               },
               plotOptions: {
                   pie: {
                       allowPointSelect: true,
                       cursor: 'pointer',
                       dataLabels: {
                           enabled: true,
                           format: '<b>{point.name}: {point.y} ， <b>{point.percentage:.1f} %',
                           style: {
                               color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                           }
                       },
                       events: {
                           click: function(e) {
                        	   location.href = e.point.url+"?dutiesKey="+e.point.dutiesKey+"&dutiesName="+e.point.name; //上面是当前页跳转，如果是要跳出新页面，那就用
                               //window.open(e.point.url);
                               //这里的url要后面的data里给出
                           }
                       }
                   }
               },
               series: [{
                   type: 'pie',
                   name: '履约服务占比',
                   data: seriesData
               }]
           });
    	   $('#qyUserCharts').highcharts({
    		   chart: {
    			   plotBackgroundColor: null,
    			   plotBorderWidth: null,
    			   plotShadow: false
    		   },
    		   title: {
    			   text: '签约用户占比'
    		   },
    		   subtitle: {
    			   text: '单位（人）'
    		   },
    		   tooltip: {
    			   headerFormat: '{series.name}<br>',
    			   pointFormat: '{point.name}: {point.y} ，<b>{point.percentage:.1f}%</b>'
    		   },
    		   plotOptions: {
    			   pie: {
    				   allowPointSelect: true,
    				   cursor: 'pointer',
    				   dataLabels: {
    					   enabled: true,
    					   format: '<b>{point.name}：{point.y} ，</b>{point.percentage:.1f} %',
    					   style: {
    						   color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
    					   }
    				   }
    			   }
    		   },
    		   series: [{
    			   type: 'pie',
    			   name: '签约用户占比',
    			   data: qySeriesData
    		   }]
    	   });
    	   $('#lyUserCharts').highcharts({
    		   chart: {
    			   plotBackgroundColor: null,
    			   plotBorderWidth: null,
    			   plotShadow: false
    		   },
    		   title: {
    			   text: '履约用户占比'
    		   },
    		   subtitle: {
    			   text: '单位（人）'
    		   },
    		   tooltip: {
    			   headerFormat: '{series.name}<br>',
    			   pointFormat: '{point.name}: {point.y} ， <b>{point.percentage:.1f}%</b>'
    		   },
    		   plotOptions: {
    			   pie: {
    				   allowPointSelect: true,
    				   cursor: 'pointer',
    				   dataLabels: {
    					   enabled: true,
    					   format: '<b>{point.name}: {point.y} ，</b>{point.percentage:.1f} %',
    					   style: {
    						   color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
    					   }
    				   }
    			   }
    		   },
    		   series: [{
    			   type: 'pie',
    			   name: '履约用户占比',
    			   data: lySriesData
    		   }]
    	   });
    	   // 海淀区家庭医生服务履约率
    	   $.ajax({
    		   url:'/sbOrderStatistic/getSbOrgStatisticCount',
    		   type:'GET',
    		   async:false,
 	    	   success:function(data){
 	    		   if(data.successful){
 	    			   if(data.data){
 	    				   var temp = '',tempObj = {};
 	    				   for(let item of data.data){
 	    					   if(item.type==0){
 	    						   temp +='<tr>'
 	    						   temp += '<td>'+item.orgName+'</td>';
 	    						   temp += '<td>'+item.signCount+'</td>';
 	 	 	 	    			   temp += '<td>'+item.dutiesCount+'</td>';
 	 	 	 	    			   temp += '<td>'+item.ratio+'</td>';
 	 	 	    				   temp+='</tr>'
 	    					   }else if(item.type==1){
 	    						   tempObj = item;
 	    					   }
 	    				   }
 	    				   if(tempObj && Object.keys(tempObj).length>0){
 	    					   temp +='<tr style="border-top:1px solid #000;">'
    						   temp += '<td>合计：</td>';
    						   temp += '<td>'+tempObj.signCount+'</td>';
	 	 	    			   temp += '<td>'+tempObj.dutiesCount+'</td>';
	 	 	    			   temp += '<td>'+tempObj.ratio+'</td>';
	 	    				   temp+='</tr>'
 	    				   }
 	    				   $('#performanceTable table tbody').html(temp);
 	    			   }else{
 	    				   layer.msg("暂无海淀区家庭医生服务履约数据！")
 	    			   }
 	    		   }
 	    	   }
    	   });
       }
       //计算页面滚动条位置
       function getScrollTop(){   
    	    var scrollTop=0;   
    	    if(document.documentElement&&document.documentElement.scrollTop){   
    	        scrollTop=document.documentElement.scrollTop;   
    	    }else if(document.body){   
    	        scrollTop=document.body.scrollTop;   
    	    }   
    	    return scrollTop;   
    	} 
       //计算两个整数的百分比值 
       function GetPercent(num, total) {
	       num = parseFloat(num); 
	       total = parseFloat(total); 
	       if (isNaN(num) || isNaN(total)) { 
	    	   return "-"; 
	       } 
	       return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%"); 
       } 
	})
}