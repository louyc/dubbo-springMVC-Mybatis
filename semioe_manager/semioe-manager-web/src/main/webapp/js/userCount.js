window.onload=function(){
	layui.use(['form', 'layedit', 'laydate','element','laypage','layer'], function(){
		  var form = layui.form(),layer = layui.layer,layedit = layui.layedit,
		      laydate = layui.laydate ,laypage = layui.laypage
		      element=layui.element();
        //用户分析
        $('.userTab li').on('click',function(){
	        var type=$(this).attr('type');
	        user(type);
	    })
	    $("#userSearch").click(function(){
        	//开始时间  
	    	var startTime=$("#beginTime").val();
  	       //结束时间
  	        var endTime=$("#overTime").val();
          	if(!startTime || !endTime){
          		layer.msg('请选择开始和结束时间');
          		return;
          	}
          	user(null);
        })
	    user(0);
       function user(type){
        	var times = new Date();
        	var managerId = window.utils.getCookie("managerId");
        	var nowTime = $("#overTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
        	var beforeTime =$("#beginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
           	type = type || $('.userTab .layui-this').attr('type');
            var data = {startDate:beforeTime,endDate:nowTime,type:type,managerId:managerId};
            var dataJson=$.toJSON(data);
            $.ajax({
            	url:'/api/contracte/userAnalysis',
            	type:'POST',
            	data:dataJson,
            	dataType:"json",
                contentType: "application/json",
                success:function(data){
                	if(data.successful){
                  		var orderDateAry=[],numObj=new Object(),orderManNumAry=[],orderWomanNumAry=[],orderSecretNumAry=[],childrenCount=[],juvenileCount=[],youthCount=[],middleAgeCount=[],oldAgeCount=[],noAgeCount=[];
                  		var countDateList=[],proAry=[];
                  		var hbpCount=[],dmCount=[],chdCount=[],copdCount=[],mtCount=[],cerebralApoplexyCount=[],mentalDisordeCount=[],
                  		tbCount=[],aihCount=[],otherNotifiableDiseasesCount=[],ohCount=[],otherCount=[],noCount=[];
                 		 $.each(data.data,function(index,item){
 	        				orderDateAry.push(window.utils.simpleDateFormat(item.countDate,'yyyy-MM-dd'));
 	        				if(type == 0){
 	        					orderManNumAry.push(item.manCount);
 		        				orderWomanNumAry.push(item.womanCount);
 		        				orderSecretNumAry.push(item.secretCount);
 	        				}else if(type == 1){
 	        					hbpCount.push(item.hbpCount);// 高血压
 	        					dmCount.push(item.dmCount);// 糖尿病
 	        					chdCount.push(item.chdCount);// 冠心病
 	        					copdCount.push(item.copdCount);// 慢性阻塞性肺疾病
 	        					mtCount.push(item.mtCount);// 恶性肿瘤
 	        					cerebralApoplexyCount.push(item.cerebralApoplexyCount);// 脑卒中
 	        					mentalDisordeCount.push(item.mentalDisordeCount);// 严重精神障碍
 	        					tbCount.push(item.tbCount);// 结核病
 	        					aihCount.push(item.aihCount);// 肝炎
 	        					otherNotifiableDiseasesCount.push(item.otherNotifiableDiseasesCount);// 其他法定传染病
 	        					ohCount.push(item.ohCount);// 职业病
 	        					otherCount.push(item.otherCount);// 其他
 	        					noCount.push(item.noCount);// 无
 	        				}else if(type == 2){
 	        					childrenCount.push(item.childrenCount);
 		        				juvenileCount.push(item.juvenileCount);
 		        				youthCount.push(item.youthCount);
 		        				middleAgeCount.push(item.middleAgeCount);
 		        				oldAgeCount.push(item.oldAgeCount);
 		        				noAgeCount.push(item.noAgeCount);
 	        				}
 		        				
                		 })
                		numObj = {man:orderManNumAry,woman:orderWomanNumAry,secret:orderSecretNumAry,
                 			children:childrenCount,juven:juvenileCount,youth:youthCount,middleAge:middleAgeCount,oldAge:oldAgeCount,noAge:noAgeCount,
                 			hbpCount:hbpCount,dmCount:dmCount,chdCount:chdCount,copdCount:copdCount,mtCount:mtCount,cerebralApoplexyCount:cerebralApoplexyCount,
                 			mentalDisordeCount:mentalDisordeCount,tbCount:tbCount,aihCount:aihCount,otherNotifiableDiseasesCount:otherNotifiableDiseasesCount,
                 			ohCount:ohCount,otherCount:otherCount,noCount:noCount,
             			};
               		 	if(type == 0){
                   			dataAry = [{name: '男',data: numObj.man},{name: '女',data: numObj.woman},{name: '保密',data: numObj.secret}];
                   			columnSexChart('first-container',orderDateAry,dataAry);
                   		}else if(type == 1){
                   			dataAry = [
                   				{name: '高血压',data: numObj.hbpCount},{name: '糖尿病',data: numObj.dmCount},{name: '冠心病',data: numObj.chdCount},{name: '慢性阻塞性肺疾病',data: numObj.copdCount},
           					    {name: '恶性肿瘤',data: numObj.mtCount},{name: '脑卒中',data: numObj.cerebralApoplexyCount},
           					    {name: '严重精神障碍',data: numObj.mentalDisordeCount},{name: '结核病',data: numObj.tbCount},
           					    {name: '肝炎',data: numObj.aihCount},{name: '其他法定传染病',data: numObj.otherNotifiableDiseasesCount},
           					    {name: '职业病',data: numObj.ohCount},{name: '其他',data: numObj.otherCount},
           					    {name: '无',data: numObj.noCount}
       					    ];
                   			columnAreaChart('second-container',orderDateAry,dataAry);
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
        //疾病史
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
	})
}