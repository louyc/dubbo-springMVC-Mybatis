layui.use(['element','form','laypage','layer','jquery','laydate'], function(){
    var element = layui.element(), $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,laydate = layui.laydate, layer = layui.layer;
    var managerId = window.utils.getRequestParam("user_id");
    var roleId = window.utils.getCookie("roleId");
    var type="1";
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
	
	document.getElementById('startTime').onclick = function(){
	    start.elem = this;
	    laydate(start);
	}
	document.getElementById('endTime').onclick = function(){
	    end.elem = this
	    laydate(end);
	}
    //tab切换
    element.on('tab(filter)', function(data){
    	document.getElementById("selectForm").reset();
    	type = this.type;
    	getData(this.type);
	});
    //选择radio
    form.on('radio(radioFilter)', function(data){
	    getData(type);
	});  
    var startTime="",endTime="",buildType="",doctorId="",orgId="";
    //分发tab页获取数据
    getData("1");
    function getData(type){
    	var list="";
    	var times = new Date();
    	
    	startTime = $("#startTime").val()||window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
    	endTime =$("#endTime").val()||window.utils.simpleDateFormat(times,'yyyy-MM-dd');
    	buildType = $("#signType").val();
    	if(roleId=="1"){//专家
        	doctorId= managerId;
        	orgId="";
        	$(".layui-tab-title li[type='4'],.layui-tab-title li[type='3']").hide();
        	$("#family thead th:eq(1),#family thead th:eq(2)").hide();
        }else if((roleId=="2")){//机构
        	doctorId= "";
        	orgId= managerId;
        	$(".layui-tab-title li[type='3']").hide();
        }
    	if(type!="7"){
    		$(".layui-form .my-tab-select ul li.lvye").hide();
    		$("#showType").show();
    		$("#export").hide();
    	}	
    	if(type!="7" && type!="6"){
    		list = statistics(type,1)
    		/*if(!list.data || !list.data.length>0){
   	    		layer.msg('暂无数据');return;
   	    	}*/
    	}
    	switch (type){
	    	case "1":
	    		getSexList(list.data);
	    		break;
	    	case "2":
	    		getAgeList(list.data);
	    		break;
	    	case "3":
	    		getOrginList(list.data);
	    		break;
	    	case "4":
	    		getDoctorList(list.data);
	    		break;
	    	case "5":
	    		getDiseaseList(list.data);
	    		break;
	    	case "6":
	    		getFamilyList(1);
	    		break;
	    	case "7":
	    		$(".layui-form .my-tab-select ul li.lvye").css("display","inline-block");
	    		$("#showType").hide();
	    		$("#export").show();
	    		getAgreeList();
	    		break;
	    	default:
	    		break;
	    }
    	$("#search").attr('type',type);
    }
    var pageSize =10;
    var orderDateAry=[];
    //性别
    function getSexList(list){
    	var manArry=[],womanArry=[],secretArry=[],countDate=[];
    	if(!list || !list.length>0){
    		layer.msg('暂无数据');
    	}else{
    		$.each(list,function(index,item){
    			manArry.push(item.manCount);
    			womanArry.push(item.womanCount);
    			secretArry.push(item.secretCount);
    			countDate.push(item.countDate);
    		});
    	}
    	var dataAry = [{name: '男',data: manArry},{name: '女',data: womanArry},{name: '保密',data: secretArry}];
	    columnSexChart('sex-container',countDate,dataAry);
    }
    //年龄
    function getAgeList(list){
    	var childrenArry=[],juvenileArry=[],youthArry=[],middleAgeArry=[],oldAgeArry=[],noAgeArry=[],countDate=[];
    	if(!list || !list.length>0){
    		layer.msg('暂无数据');
    	}else{
    		$.each(list,function(index,item){
        		childrenArry.push(item.childrenCount);
        		juvenileArry.push(item.juvenileCount);
        		youthArry.push(item.youthCount);
        		middleAgeArry.push(item.middleAgeCount);
        		oldAgeArry.push(item.oldAgeCount);
        		noAgeArry.push(item.noAgeCount);
        		countDate.push(item.countDate);
        	});
    	}
    	var dataAry = [{name: '儿童',data: childrenArry},{name: '少年',data: juvenileArry},{name: '青年',data: youthArry},{name: '中年',data: middleAgeArry},{name: '老年',data: oldAgeArry},{name: '未知',data: noAgeArry}];
	    columnSexChart('age-container',countDate,dataAry);
    }
    //机构
    function getOrginList(list){
    	var dataAry =list.proviceList;
    	if(!list){
    		layer.msg('暂无数据');
    	}else{
    		$.each(dataAry,function(index,item){
    			item.data = item.data.split(",");
    			var len = item.data.length;
    			$.each(item.data,function(index,num){
    				item.data.push(Number(num));
    			})
    			item.data = item.data.splice(len)
        	});
    	}

		console.log(list.countDateList,dataAry)
	    columnSexChart('orgin-container',list.countDateList,dataAry);
    }
    //医生
    function getDoctorList(list){
    	var dataAry =list.proviceList;
    	if(!list){
    		layer.msg('暂无数据');
    	}else{
    		$.each(dataAry,function(index,item){
    			item.data = item.data.split(",");
        		var len = item.data.length;
    			$.each(item.data,function(index,num){
    				item.data.push(Number(num));
    			})
    			item.data = item.data.splice(len)
        	});
    	}
	    columnSexChart('doctor-container',list.countDateList,dataAry);
    }
    //疾病
    function getDiseaseList(list){
    	var hbpArry=[],dmArry=[],chdArry=[],copdArry=[],mtArry=[],cerebralApoplexyArry=[],noArry=[],countDate=[];
    	var mentalDisordeArry=[],tbArry=[],aihArry=[],otherNotifiableDiseasesArry=[],ohArry=[],otherArry=[];
    	if(!list || !list.length>0){
    		layer.msg('暂无数据');
    	}else{
    		$.each(list,function(index,item){
        		hbpArry.push(item.hbpCount);
        		dmArry.push(item.dmCount);
        		chdArry.push(item.chdCount);
        		copdArry.push(item.copdCount);
        		mtArry.push(item.mtCount);
        		cerebralApoplexyArry.push(item.cerebralApoplexyCount);
        		mentalDisordeArry.push(item.mentalDisordeCount);
        		tbArry.push(item.tbCount);
        		aihArry.push(item.aihCount);
        		otherNotifiableDiseasesArry.push(item.otherNotifiableDiseasesCount);
        		ohArry.push(item.ohCount);
        		otherArry.push(item.otherCount);
        		noArry.push(item.noCount);
        		countDate.push(item.countDate);
        	});
    	}
    	var dataAry = [{name: '高血压',data: hbpArry},{name: '糖尿病',data: dmArry},{name: '冠心病',data: chdArry},
    		{name: '慢性阻塞性肺疾病',data: copdArry},{name: '恶性肿瘤',data: mtArry},{name: '脑卒中',data: cerebralApoplexyArry},
    		{name: '严重精神障碍',data: mentalDisordeArry},{name: '结核病',data: tbArry},{name: '肝炎',data: aihArry},
    		{name: '其他法定传染病',data: otherNotifiableDiseasesArry},{name: '职业病',data: ohArry},{name: '其他',data: otherArry},
    		{name: '无',data: noArry}];
	    columnSexChart('disease-container',countDate,dataAry);
    }
    //家庭组
    function getFamilyList(currPage){
    	var list = statistics("6",currPage);
    	var str="";
    	if(!list.items.length>0){
    		layer.msg("暂无数据");
    	}else{
    		$.each(list.items,function(index,item){
        		str+='<tr pId="'+item.parentIds+'">';
        		str+='<td>'+item.signDate+'</td>';
        		if(roleId!="1"){
        			str+='<td>'+item.orgName+'</td>';
        			str+='<td>'+item.doctorName+'</td>';
        		}
        		str+='<td>'+item.countNumber+'</td>';
        		str+='<td><a class="layui-btn layui-btn-small showDetail" href="#" data-id="'+item.parentIds+'">查看明细</a></td>';
        		str+='</tr>';
        	})
    	}
    	laypage({
			cont: 'family-pagging'
				,pages: list.pagesCount
				,groups: 5 
				,curr:list.pageNumber || 1
				,jump: function(obj, first){
					if(!first){
						currPage = obj.curr;
						getFamilyList(currPage)
					}
				}
		});
    	$("#family tbody").html(str);
    }
    //家庭组查看明细
    $("#family").delegate(".showDetail","click",function(){
	    	layer.open({
	        title:'家庭组明细',
	        type: 1,
	        area: ['80%', '90%'],//宽高
	        shade: 0.5,
	        content: $('#labels'),
	    		success: function(layero){
	          	layer.setTop(layero); //重点2	          	
	        },
	        cancel: function(){ 
        		   $('#mainNumber,#mainPeople').val('');
            }
	    	})
	    var pId = $(this).parents('tr').attr('pId');
   		getFamilyDetail(pId)
    });
    $('#family-search').click(function(){
   		var tId = $('#family-detile').attr('tId');
   		getFamilyDetail(tId)
    })
    function getFamilyDetail(pId){
   		var parentMobile = $.trim($('#mainNumber').val());
   		var parentName = $.trim($('#mainPeople').val());
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
	        				 $('#family-detile tbody').html('<tr><td colspan="3">暂无数据</td></tr>')
	        			}else{
	        				$('#family-detile tbody').html(signHtml);
	        			}
	        			$('#family-detile').attr('tId',pId);
	        		}else{
	        			$('#family-detile tbody').html('<tr><td colspan="3">请求数据失败</td></tr>')
	        		}
	        	}
	    })
    }
    //履约情况
    function getAgreeList(currPage){
    	var startCountNum = $("#counts_min").val();
    	var endCountNum = $("#counts_max").val();
    	var keyWordsType = $("#serviceType").val()=="all"?"":$("#serviceType").val();
    	var data = {showCount:10,currentPage:currPage,startDate:startTime,endDate:endTime,doctorId:doctorId,orgId:orgId,startCountNum:startCountNum,endCountNum:endCountNum,buildType:buildType,keyWordsType:keyWordsType};
        var dataJson=$.toJSON(data);
        $.ajax({
       	    url:'/orderStatistic/getDutiesCountArray',
       	    type:'POST',
       	    data:dataJson,
       	    dataType:"json",
       	    contentType: "application/json",
        	success:function(items){
           	    if(items.successful){
           	    	var data = items.items;
           	    	var str = "";
           	    	if(data.length<=0){
           	    		layui.msg("暂无数据！")
           	    	}else{
           	    		$.each(data,function(index,item){
           	    			str +="<tr>";
           	    			str +="<td>"+item.userMobile+"</td>";
           	    			str +="<td>"+item.signUserName+"</td>";
           	    			str +="<td>"+(item.serviceName?item.serviceName:'')+"</td>";
           	    			str +="<td><a class='serviceCode' data-id='"+item.orderId+"' data-code='"+item.orderCode+"'>"+item.reportCount+"</a></td>";
           	    			str +="</tr>";
    					})
           	    		laypage({
           	    			cont: 'lvyue-pagging'
           	    				,pages: items.pagesCount
           	    				,groups: 5 
           	    				,curr:items.pageNumber || 1
           	    				,jump: function(obj, first){
           	    					if(!first){
           	    						currPage = obj.curr;
           	    						getAgreeList(currPage);
           	    					}
           	    				}
           	    		});
           	    		$('#lvyue tbody').html(str);
           	    		$(".serviceCode").on("click",function(){
           	    			var id = $(this).attr("data-id");
     	        			var relationCode = $(this).attr("data-code");
     	        			openServiceVerdict(id,relationCode);
           	    		});
           	    	}
            	}else{
            		layer.msg('失败');
            	}
        	},error:function(error){
        		layer.msg('失败');
        	}
        })
    }
    //服务结论
    function openServiceVerdict(id,relationCode){
    	layer.open({
            type: 2 //2为弹出iframe
            ,title: '服务结论'
            ,area: ['800px', '90%']
    		,shade: 0.5
            ,content: 'serviceVerdict.html?id='+id+'&relationCode='+relationCode
            ,btn: ['关闭'] //按钮
	    	,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          layer.setTop(layero); //重点2
	        }
    	});
    }
    //获取统计数据
    function statistics(type,currPage){
    	var displayType = "";
    	var dataList="";
    	$("#showType .layui-form-radio").each(function(){
    		if($(this).attr("class").indexOf("layui-form-radioed")>=0){
    			displayType =$(this).prev("input").val();
    		}
    	})
    	if(type=="6"){
    		var data = {showCount:10,currentPage:currPage,startDate:startTime,endDate:endTime,buildType:buildType,displayType:displayType,analysisType:type,managerId:""};
    	}else{
    		var data = {showCount:"",currentPage:"",startDate:startTime,endDate:endTime,buildType:buildType,displayType:displayType,analysisType:type,managerId:""};
    	}
        var dataJson=$.toJSON(data);
    	$.ajax({
       	    url:'/oprationDate/signAnalysis',
       	    type:'POST',
       	    async:false,
       	    data:dataJson,
       	    dataType:"json",
       	    contentType: "application/json",
        	success:function(items){
           	    if(items.successful){
       	    		dataList= items;
            	}else{
            		layer.msg('失败');
            	}
        	},error:function(error){
        		layer.msg('失败');
        	}
        })
        return dataList;
    }
    //搜索按钮
    $("#search").click(function(){
    		var startDate=$("#startTime").val();
        var endDate=$("#endTime").val();
        if(!startDate &&endDate){
      		layer.msg('请选择开始时间');
      		return;
      	}else if(startDate&&!endDate){
      		layer.msg('请选择结束时间');
      		return;
      	}
    		getData($(this).attr('type'));
    });
    //导出按钮
    $("#export").click(function(){
    	var query = '?';
    	var startCountNum = $("#counts_min").val();
    	var endCountNum = $("#counts_max").val();
    	var keyWordsType = $("#serviceType").val()=="all"?"":$("#serviceType").val();
    	
    	query += 'showCount=1&';
    	query += 'currentPage=10000&';
    	if(startCountNum)  query += 'startCountNum='+startCountNum+'&';
    	if(endCountNum)  query += 'endCountNum='+endCountNum+'&';
    	if(keyWordsType)  query += 'keyWordsType='+keyWordsType+'&';
    	if(doctorId)  query += 'doctorId='+doctorId+'&';
    	if(orgId)  query += 'orgId='+orgId+'&';
    	if(buildType)  query += 'buildType='+buildType+'&';
    	if(startTime)  query += 'startTime='+startTime+'&';
    	if(endTime)  query += 'endTime='+endTime+'&';
    	
    	$(this).attr('href','/orderStatistic/exportDutiesCountArray'+query);
    });
    //画charts图
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
});
