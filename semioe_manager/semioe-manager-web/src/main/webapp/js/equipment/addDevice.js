window.onload=function(){
	 layui.use(['element','layer','form','laypage'],function(){
		 var $=layui.jquery,layer = layui.layer,form = layui.form();
		 var laypage = layui.laypage,fid='',bid='';
		 
		 form.on('select(networkingType)', function (data) {
			 if(data.value == "1"){
				 $("#machine_code").hide();
				 $("#build_machine_code").show();
			 }else{
				 $("#machine_code").show();
				 $("#build_machine_code").hide();
			 }
         });
		 $("#buildCode").on("click",function(){
			 layer.open({
                 type: 1 
                 ,title: '生成机器号'
                 ,area: ['500px', '300px']
			 	 ,btnAlign: 'c'
         		 ,shade: 0.5
                 /*,maxmin: true*///缩放功能
                 ,offset: [ //居中显示
                    $(window).height()/2-200
                   ,$(window).width()/2-250
                 ] 
                 ,content: $("#suggestion")
                 ,btn: ['确定','关闭'] //按钮
                 ,yes: function(index, layero){
                 	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
                 	var productId = $("#productId").val();
                 	var macAddress = $("#macAddress").val().replace(/:/g,"");
                 	if(!productId){
                 		$('.opa0').remove();
                 		layer.msg("产品id不能为空!",{time: 2000,zIndex: layer.zIndex});return;
                 	}
                 	if(!macAddress){
                 		$('.opa0').remove();
                 		layer.msg("mac地址不能为空!",{time: 2000,zIndex: layer.zIndex});return;
                 	}
                 	$.post('/api/WeiXin/wx_device.action?productId='+productId+'&mac='+macAddress,function(res){
                 		var res = eval("("+res+")");
                 		if(res.errcode == 0){
                 			layer.msg("生成成功！",{time: 2000,zIndex: layer.zIndex},function(){
                 				$("#deviceCode_build").val(res.deviceid);
                 				$("#devicePicUrl").val(res.qrticket);
                 				layer.closeAll();
             				});
                 		}else{
                 			$('.opa0').remove();
                 			layer.msg("生成失败，请重新输入！",{time: 2000,zIndex: layer.zIndex},function(){
                 				/*layer.closeAll();*/
             				});
                 		}
                 	})
                 }
                 ,zIndex: layer.zIndex //重点1
                 ,success: function(layero){
                     layer.setTop(layero); //重点2
                 }
			 })
			 return false;
		 })
		 //设备类别
		 function device(){
			 $.ajax({
				 url:'/deviceType/getAllDeviceType',
				 type:'POST',
				 data:$.toJSON({}),
				 dataType:"json",
	        	 contentType: "application/json",
				 success:function(data){
					 if(data.successful){
	             			var str='';
	             			var datas=data.data;
	                		$.each(datas,function(index,item){
	                 			str+='<option value="'+item.id+'">'+item.deviceTypeName+'</option>'
	                 		})
	             		}
	             		$('#deviceType').html('<option value="">请选择设备类别</option>').append(str);
	             		form.render();
				 }
			 })
		 }
		 device();
		 //设备定义
		 function deviceType(fid,bid){  
			 $.ajax({
             	url:'/deviceDefinition/getDeviceDefinitionList',
             	type:'POST',
             	async:false,
             	data:$.toJSON({deviceFirmid:fid,deviceBrandid:bid}),
             	contentType: "application/json",
             	success:function(data){
             		if(data.successful){
             			var str='';
             			var datas=data.items;
                		$.each(datas,function(index,item){
                 			str+='<option value="'+item.id+'">'+item.definitionName+'</option>'
                 		})
             		}
             		$('#deviceClass').html('<option value="">请选择设备定义</option>').append(str);
             		form.render();
             	}
             })
		 }
		 //厂商
		function facturerList(){
			$.ajax({
	             	url:'/deviceFirms/getDeviceFirms',
	             	type:'POST',
	             	data:{},
	             	success:function(data){
	             		if(data.successful){
	             			var str='',data=data.data;
		             		$.each(data,function(index,item){
		             			str+='<option value="'+item.id+'">'+item.firmName+'</option>'
		             		})
		             		$('#facturerList').html('<option value="">请选择厂商</option>').append(str);	             		
		             		form.render();
	             		}
	             		
	             	}
	             })
		 }
		 facturerList();
		 form.on('select(facturerList)',function(data){
			 fid = data.value;
			 brandList(fid);
		 })
		 //品牌
		 function brandList(fid){
			 $.ajax({
	             	url:'/deviceBrands/selectDeviceBrandsListPage',
	             	type:'POST',
	             	async:false,
	             	data:{firmId:fid,pageSize :1000,currentsPage :1},
	             	success:function(data){
	             		if(data.successful){
	             			var str='',data=data.items;
		             		$.each(data,function(index,item){
		             			str+='<option value="'+item.id+'">'+item.brandName+'</option>'
		             		})
		             		$('#brand').html('<option value="">请选择品牌</option>').append(str);
		             		form.render();
	             		}
	             		
	             	}
	             })
		 }
		 form.on('select(brand)',function(data){
			 bid = data.value;
			 deviceType(fid,bid);
		 })
	  //获取url上面的参数
		    function GetQueryString(name){
		         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		         var r = window.location.search.substr(1).match(reg);
		         if(r!=null)return  unescape(r[2]); return null;
		    }
		 var id = GetQueryString("id");
		 if(id){
			 $.post('/deviceInfo/getDeviceInfoById',{id:id},function(res){
				 var list=res.data;
				 if(res.successful){
					 if(list.userName==null){
						 list.userName=''
					 }
					 if(list.userMobile==null){
						 list.userMobile=''
					 }
					 if(list.deviceLocation==null){
						 list.deviceLocation=''
					 }
					 if(list.deviceStock==null){
						 list.deviceStock=0;
					 }
					 if(list.deviceRental==null){
						 list.deviceRental=0
					 }
					 $('#networkingType').val(list.networkingType);
					 if(list.networkingType == "1"){
						 $("#deviceCode_build").val(list.deviceCode);//设备编码
						 $("#devicePicUrl").val(list.devicePicUrl);
						 $("#machine_code").hide();
						 $("#build_machine_code").show();
					 }else{
						 $("#deviceCode").val(list.deviceCode);//设备编码
					 }
					 $("#brandName").val(list.deviceName);
					 $(".brandDes").val(list.deviceDesc);//设备描述
					 $("#standard").val(list.deviceSpec);//设备规格
					 $("#facturerList").val(list.firmId)//厂商
					 brandList(list.firmId);
					 $("#brand").val(list.brandId)//品牌
					 deviceType(list.firmId,list.brandId);
					 $('#deviceClass').val(list.definitionId);//设备定义
					 $(".stock").val(list.deviceStock)//库存
					 $(".deviceRent").val(list.deviceRental)//设备租金
					 
					 $('#deviceType').val(list.deviceType);//设备类别
					 $('.delivery_throw').val(list.userMobile+' '+list.userName+' '+list.deviceLocation).attr('marId',list.managerId);//设备投放
					 $('input:radio[name="rent"][value="'+list.isHired+'"]').prop('checked',true);//租赁
					 $('input:radio[name="frame"][value="'+list.isPutaway+'"]').prop('checked',true);//上下架
					 form.render();
				 }
				 
			 })
		 }
		 //提交添加
		 form.on('submit(formDemo)', function(data){
			 	layer.open({
	        	    type: 3,
	        	    content: ""
	    	    });
			 	var networkingType=$('#networkingType').val();
	        	var deviceDes=$.trim($(".brandDes").val());
	        	var standard=$("#standard").val();
	        	var facturerList=$("#facturerList").val();
	        	var brandId=$("#brand").val();
	        	var stock=$.trim($(".stock").val());
	        	var deviceRent=$.trim($(".deviceRent").val());
	        	var frame=$("input[name='frame']:checked").val();
	        	var rent=$("input[name='rent']:checked").val();
	        	var deviceName=$.trim($('#brandName').val());
	        	var devicePicUrl ="";
	        	if(networkingType == "1"){
	        		var deviceCode=$.trim($('#deviceCode_build').val());
	        		devicePicUrl =$("#devicePicUrl").val();
	        	}else{
	        		var deviceCode=$.trim($('#deviceCode').val());
	        	}
	        	var deviceClass=$('#deviceClass').val();//设备定义
	        	var deviceType=$('#deviceType').val();//设备类别
	        	var deliveryThrow = $.trim($('.delivery_throw').val());
	        	var delivery_throw=deliveryThrow ? $.trim($('.delivery_throw').attr('marId')) : '';//设备投放
	        	var data={deviceCode:deviceCode,managerId:delivery_throw,deviceName:deviceName,devicePicUrl:devicePicUrl,definitionId:deviceClass,firmId:facturerList,brandId:brandId,isPutaway:frame,isHired:rent,deviceDesc:deviceDes,deviceStock:stock,deviceRental:deviceRent,deviceDeposit:'',deviceType:deviceType,deviceSpec:standard,networkingType:networkingType}
	        	var sendUrl='';	
	        	
	        	if(!standard){
	        		layer.msg('请填写设备规格');
	        		return;
	        	}
	        	if(!facturerList){
	        		layer.msg('请选择厂商');
	        		return;
	        	}
	        	if(!brandId){
	        		layer.msg('请选择品牌');
	        		return;
	        	}
	        	if(!deviceType){
	        		layer.msg('请选择设备类别');
	        		return;
	        	}
	        	if(!stock){
	        		layer.msg('请输入设备库存');
	        		return;
	        	}
	        	if(!deviceRent){
	        		layer.msg('请输入设备租金');
	        		return;
	        	}
	        	if(!frame){
	        		layer.msg('请填写是否上架');
	        		return;
	        	}
	        	if(!rent){
	        		layer.msg('请填写是否租赁');
	        		return;
	        	}
	        	if(!deviceName){
	        		layer.msg('请填写设备名称');
	        		return;
	        	}
	        	if(!deviceCode){
	        		layer.msg('请填写机器号');
	        		return;
	        	}
	        	if(!deviceClass){
	        		layer.msg('请选择设备定义');
	        		return;
	        	}
	        	
	        	if(id){
        			sendUrl='/deviceInfo/updateDeviceInfo';
	        		data.id=id;
        		}
        		else{
	        		sendUrl='/deviceInfo/insertDeviceInfo';
        		}
	        	var dataJson=$.toJSON(data);
	        	$.ajax({
	        		url:sendUrl,
	        		type:'POST',
	        		data:dataJson,
	        		dataType:"json",
	        		contentType: "application/json",
	        		success:function(data){
	        				layer.close(layer.index);
		        			if(data.successful && data.resultCode.code==='SUCCESS'){
		        				if(id){
		                    		layer.msg('修改成功!');
		                        }
		        				else{
		                    		layer.msg('添加成功!');
		                    		
		                        };
		        				setTimeout(function(){
		            				window.location.href='/html/equipment/deviceList.html';
		                		},1500)
		                		
		        			}else{
		        				layer.msg(data.resultCode.message);
		        			}
		        		},
	        		error:function(error){
	        			layer.close(layer.index);
	        			if(!id){
                    		layer.msg('添加失败!');
                        }
        				else{
                    		layer.msg('修改失败!');
                        }
	        		}

	        	})	
	        	return false;
	      });
		 $('.delivery_throw').click(function(){
			 var marId = $(this).attr('marId');
			 more(1,marId);
			 layer.open({
           	  type:1
           	   ,title:'投放'
           	   ,content: $('#goods-info-box')
               ,area: ['700px','400px']
               ,btn:['确定']
			   ,cancel : function(){
				   
			   }
			   
           })
		 })
		 var pageExplicit=5,currExplicit=1;
		 function more(currExplicit,marId){
			 var roles="1,2",name=$('#name').val(),mobile=$('#phone').val();
			 var data={roles:roles,name:name,mobile:mobile,showCount:pageExplicit,currentPage:currExplicit};
			 var dataJson=$.toJSON(data);
			 $.ajax({
				 url:'/backstage/queryByRole',
				 type:'POST',
				 data:dataJson,
				 dataType:'json',
				 contentType: "application/json",
				 success:function(data){
					 var str='';
					 if(data.successful){
						 var datas=data.items;
						 $.each(datas,function(index,item){
//							 str+='<tr'+(item.managerId==marId ?' style="background:#eee"': '')+' class="deviceThrow" id='+item.managerId+'><td>'+(item.mobile?item.mobile:'未填写')+'</td>';
							 str+='<tr class="deviceThrow" id='+item.managerId+'><td>'+(item.mobile?item.mobile:'未填写')+'</td>';
							 str+='<td>'+(item.name?item.name:'未填写')+'</td>';
							 str+='<td>'+(item.address?item.address:'未填写')+'</td>';
							 str+='</tr>';
//							 var curPage = Math.ceil(Number(index)+1)/pageExplicit;
//							 console.log(curPage)
//						 	 if(curPage > 1){
//								 data.pageCount = item.managerId==marId ? curPage : data.pageCount;
//						 		 more(data.pageCount,marId);
//						 	 }
						 })
					 }
					 laypage({
                  	    cont: 'tableBox'
                  	    ,pages: data.pagesCount
                  	    ,groups: 5 
                  	    ,curr:data.pageNumber || 1
                  	    ,jump: function(obj, first){
                  	    	if(!first){
                  	    		currExplicit = obj.curr;
                  	    		more(currExplicit);
                  	    	}
                 		}
                  	});
					 $('.explicit tbody').html(str);
				 },
				 error:function(error){
					 $('.explicit tbody').html('<tr><td colspan="8">列表获取失败</td></tr>');
				 }
			 })
		 }
		//搜索
		  $('#findMore').click(function(){
			  more(1);
		  })
		  $('.explicit').delegate('.deviceThrow','click',function(){
			  var  name=$(this).children().first().text();
			  var  mobile=$(this).children().eq(1).text();
			  var adress=$(this).children().eq(2).text();
			  var val=$(this).attr('id');
			  $('.delivery_throw').val(mobile+' '+name+' '+adress).attr('marId',val);
			  /*layer.closeAll();*/
		  })
		  //重置
		 $('#resets').on('click',function(){
			 $('.delivery_throw').val('');
			 return false;
		 })
		
		 
	 })
}
