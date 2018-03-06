layui.use(['element','form','laypage','layer','jquery','laydate'], function(){
    var element = layui.element(), $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,laydate = layui.laydate, layer = layui.layer;
    var pageSum = 1;
    var doctorId = window.utils.getRequestParam("doctorId");
    var managerId = window.utils.getRequestParam("managerId");
    
    var dataAry = [
		{name:"urine_VitC",value:"维生素C",unit:''},
        {name:"urine_leukocytes",value:"白细胞",unit:''},
        {name:"urine_bilirubin",value:"胆红素",unit:''},
        {name:"urobilinogen",value:"尿胆原",unit:''},
        {name:"urine_glucose",value:"葡萄糖",unit:''},
        {name:"urine_specific_gravity",value:"比重",unit:''},
        {name:"urine_protein",value:"蛋白质",unit:''},
        {name:"urine_ketone",value:"酮体",unit:''},
        {name:"urine_occult_blood",value:"潜血",unit:''},
        {name:"urine_nitrite",value:"亚硝酸盐",unit:''},
        {name:"urine_PH",value:"酸碱度",unit:''},
        {name:"degreased_body_weight",value:"去脂体重",unit:'kg'},
        {name:"protein_weight",value:"蛋白质重量",unit:'kg'},
        {name:"health_score",value:"健康评分",unit:''},
        {name:"water_percentage",value:"水分比",unit:'%'},
        {name:"weight",value:"体重",unit:'kg'},
        {name:"fat_control",value:"脂肪控制量",unit:'kg'},
        {name:"basic_metabolism_rate",value:"基础代谢率",unit:'Kcal/d'},
        {name:"fat_content",value:"脂肪",unit:'kg'},
        {name:"bone_weight",value:"骨质重",unit:'kg'},
        {name:"body_type",value:"身体类型",unit:'kg'},
        {name:"visceral_fat_index",value:"内脏脂肪指数",unit:''},
        {name:"fat_level",value:"肥胖等级",unit:''},
        {name:"muscle_content",value:"肌肉",unit:'kg'},
        {name:"weight_control",value:"体重控制量",unit:'kg'},
        {name:"standard_weight",value:"标准体重",unit:'kg'},
        {name:"muscle_control",value:"肌肉控制量",unit:'kg'},
        {name:"height",value:"身高",unit:'cm'},
        {name:"BMI",value:"健康指数",unit:''},
    		{name:"body_fat_percentage",value:"体脂百分比",unit:'%'},
    		{name:"HDL",value:"高密度蛋白胆固醇",unit:'mmol/L'},
    		{name:"CE",value:"总胆固醇与高密度脂蛋白比值",unit:''},
    		{name:"TG",value:"甘油三酯",unit:'mmol/L'},
    		{name:"LDL",value:"低密度脂蛋白胆固醇",unit:'mmol/L'},
    		{name:"chol",value:"总胆固醇",unit:'mmol/L'},
    		{name:"hct",value:"红细胞比容",unit:'%'},
    		{name:"hb",value:"血红蛋白",unit:'g/dL'},
    		{name:"ECG_t_axis",value:"t_axis",unit:'degree'},
    		{name:"ECG_sv1",value:"sv1",unit:'uV'},
    		{name:"ECG_qtc",value:"qtc",unit:'ms'},
    		{name:"ECG_rv5",value:"rv5",unit:'uV'},
    		{name:"ECG_qrs",value:"qrs",unit:'ms'},
    		{name:"ECG_rv5_svl",value:"rv5_svl",unit:'uV'},
    		{name:"ECG_p_r",value:"p_r",unit:'ms'},
    		{name:"ECG_qrs_axis",value:"qrs_axis",unit:'degree'},
    		{name:"ECG_rr",value:"rr",unit:'ms'},
    		{name:"ECG_qt",value:"qt",unit:'ms'},
    		{name:"ECG_p_axis",value:"p_axis",unit:'degree'},
    		{name:"heart_rate",value:"心率",unit:'bpm'}
   ]
    //列表查询
    function getData(pageNum,pageSize){
    	var dataType = $("#dataType").val() == 'all' ? '' : $("#dataType").val();
    	var startTime = $("#startTime").val();
    	var endTime = $("#endTime").val();
    	var obj = new Object();
    	var query = '?pageSize='+pageSize+'&currentsPage='+pageNum+'&dataType='+dataType+'&startTime='+startTime+'&endTime='+endTime+'&doctorId='+doctorId+'&managerId='+managerId
    	$.ajax({
	        url:'/api/contracte/queryUserCeShi'+query,
	        type:'GET',
			success:function(res){
	        	layer.close(layer.index);//取消遮罩
	            if(res.successful){
	            	if(res.items.length<=0){
	            		layer.msg("暂无数据！");
	            		return;
	            	}
	            	var str = "";
	            	var data = res.items[0],age = data.age, sex = data.sex;
					age = age?age:'无';
					sex = sex == 0 ? '未知' :(sex == 1 ? '男' : (sex == 2 ? '女' : (sex == 3 ? '未说明' : '无')));
					$('.dataTit .username').text(data.name?data.name:'无');
					$('.dataTit .usersex').text(sex);
					$('.dataTit .userage').text(age);
					$.each(res.items,function(index,item){
						var itemData = eval("("+item.dataValue+")");
						var d_value = itemData.value ? itemData.value : (itemData.wp ? itemData.wp : '');
						if(item.dataType == 'BODY'){
							d_value = eval("("+item.dataValue+")");
							itemData.name = '体脂秤';
							itemData.date = window.utils.simpleDateFormat(item.createTime,'yyyy-MM-dd HH:mm:ss');
						}
						else if(item.dataType == 'BF'){
							d_value = eval("("+item.dataValue+")");
							delete d_value.date; delete d_value.dataType; delete d_value.name; delete d_value.units;
							itemData.name = '血脂五项';
							itemData.date = window.utils.simpleDateFormat(item.createTime,'yyyy-MM-dd HH:mm:ss');
						}
						else if(item.dataType == 'BHB'){
							d_value = eval("("+item.dataValue+")");
							delete d_value.date; delete d_value.dataType; delete d_value.name; delete d_value.units;
							itemData.name = '血红蛋白';
							itemData.date = window.utils.simpleDateFormat(item.createTime,'yyyy-MM-dd HH:mm:ss');
						}
						if(window.utils.isObj(d_value)){
							var val1 = '';
							for(var val in d_value){
								$.each(dataAry,function(index,itemVal){
									if(itemVal.name == val){
										val1 += '<p>'+itemVal.value+': '+d_value[val]+''+itemVal.unit+'</p>';
									}
								})
							}
							d_value = val1;
						}else{
							d_value = d_value+''+itemData.units;
						}
						var name = itemData.name?itemData.name:""
							str+='<tr><td>'+name+'</td>';
							str+='<td>'+d_value+'</td>';
						var date = itemData.date?itemData.date:""
							str+='<td>'+date+'</td></tr>';
					})
	            	$("#data_table tbody").html(str);
	            }
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }
    //测量数据分页
    pagging_data();
    function pagging_data(){
    	layer.open({type: 3,content: ""});
    	var dataType = $("#dataType").val() == 'all' ? '' : $("#dataType").val();
    	var startTime = $("#startTime").val();
    	var endTime = $("#endTime").val();
    	var obj = new Object();
    	var pageNum = 1;
    	var pageSize = 1000;
    	var query = '?pageSize='+pageSize+'&currentsPage='+pageNum+'&dataType='+dataType+'&startTime='+startTime+'&endTime='+endTime+'&doctorId='+doctorId+'&managerId='+managerId
	    $.ajax({
	        url:'/api/contracte/queryUserCeShi'+query,
	        type:'GET',
			success:function(data){
	        	layer.close(layer.index);//取消遮罩
	        	var pageSum=1;
	        	if(data.items){
	        		pageSum = data.items.length;
	        	}else{
	        		pageSum = 0;
	        	}
	        	laypage({
	        		cont: 'pagging_data'
        			, pages: Math.ceil(pageSum/10)
        			, skip: false
        			,jump: function(obj, first){
        				layer.open({type: 3,content: ""});
        				//得到了当前页，用于向服务端请求对应数据
        				pageNum = obj.curr;
        				getData(pageNum,10);
        			}
	        	});
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    };
    /*//服务分页
    pagging_service();
    function pagging_service(){
    	layer.open({type: 3,content: ""});
    	var serviceName = $("#serviceName").val();
    	var startTime = $("#startTime_service").val();
    	var endTime = $("#endTime_service").val();
    	var qurey = '?userId='+managerId+'&doctorId='+doctorId+'&serviceName='+serviceName+'&buyStartTime='+startTime+'&buyEndTime='+endTime;
   	 	$.ajax({
	        url:'/jyOrderInfo/getJyValidUserOrder'+qurey,
	        type:'GET',
			success:function(data){
	        	layer.close(layer.index);//取消遮罩
	        	var pageSum = data.items.length;
	        	laypage({
	        		cont: 'pagging_service'
        			, pages: Math.ceil(pageSum/10)
        			, skip: false
        			,jump: function(obj, first){
        				layer.open({type: 3,content: ""});
        				//得到了当前页，用于向服务端请求对应数据
        				pageNum = obj.curr;
        				getData_service(pageNum,10);
        			}
	        	});
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }*/
    	
    getData_service();
    function getData_service(){
    	layer.open({type: 3,content: ""});
		var serviceName = $("#serviceName").val();
    	var startTime = $("#startTime_service").val();
    	var endTime = $("#endTime_service").val();
    	var qurey = '?userId='+managerId+'&doctorId='+doctorId+'&serviceName='+serviceName+'&buyStartTime='+startTime+'&buyEndTime='+endTime;
   	 	$.ajax({
	        url:'/jyOrderInfo/getBackValidUserOrder'+qurey,
 	        type:'GET',
 			success:function(data){
 	        	layer.close(layer.index);//取消遮罩
 	        	if(data.successful){
 	        		var list = data.data;
 	        		var str = "";
 	        		for(var i =0;i < list.length;i++){
 	        			str +='<tr><td>'+list[i].relationName+'</td>';
 	        			str +='<td>'+list[i].createTime+'</td>';
 	        			str +='<td><button class="layui-btn layui-btn-small serviceVerdict" data-code="'+list[i].relationCode+'" data-id="'+list[i].id+'">服务结论</button></td></tr>'
 	        		}
 	        		$("#service_table tbody").html(str);
 	        		$(".serviceVerdict").on("click",function(){
 	        			var id = $(this).attr("data-id");
 	        			var relationCode = $(this).attr("data-code");
 	        			openServiceVerdict(id,relationCode);
 	        		})
 	        	}
 			 },
 	        error : function(data){
 	        	layer.close(layer.index);//取消遮罩
 	        	layer.msg('失败！');
 	        },
 	    });
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
    getItems();
    //获取项目名称列表
	function getItems(){
		$.get('/deviceData/queryDeviceItems',function(res){
			if(res.successful){
				var option = '';
				$.each(res.data,function(index,item){
					option += '<option value="'+item.deviceItemId+'">'+(item.deviceItemName).replace(/\"/g,'')+'</option>'
				})
				$('#dataType').html('<option  value="all">全部</option>').append(option);
				form.render('select');
			}else{
				layer.msg('检测项目获取失败！');
			}
		})
	}
    //搜索
    $("#search_service").on("click",function(){
    	getData_service();
    });
    //搜索
    $("#search").on("click",function(){
    	pagging_data();
    })
});
