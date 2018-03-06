window.onload = function () {
    layui.use(['laypage', 'layer','form'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, form = layui.form();
        laypage({
        	cont: 'pagging',
            pages: 100,
            skin: '#5FB878', 
            skip: true, 
            jump: function(obj, first){
                  if(!first){
                    layer.msg('第'+ obj.curr +'页');
                  }
            }
        });
        //导出
        $('#exportBtn').on('click',function(){
        	var query='?';
        	var name=$.trim($('#searchName').val());
        	var mobile=$.trim($('#searchPhone').val());
        	var roleId=$('#s-role').val()=='all' ? '': $('#s-role').val();
        	var userStatus=$('#search-audit-status').val()=='all' ? '' : $('#search-audit-status').val();
        	var inUse=$('#search-is-use').val()=='all' ? '' : $('#search-is-use').val();
        	if(name) query+='name='+name+'&';
        	if(roleId) query+='roleId='+roleId+'&';
        	if(userStatus) query+='userStatus='+userStatus+'&';
        	if(inUse) query+='inUse='+inUse+'&';
        	if(mobile) query+='mobile='+mobile+'&';
        	$(this).attr('href','/excelExport/backstageUser'+query);
        })
        
        
        //获取搜索角色列表
        $.get('/role/getRoleInfo',function(res){
        	if(res.successful){
        		var data = res.data;
            	var str = '';
        		$.each(data,function(index,item){
        			str += '<option value="'+item.id+'">'+item.itemName+'</option>';
        		})
        		$('#s-role').html('<option value="all">请选择角色</option>').append(str);
        		form.render();
        	}
        })
        //获取标签
        getLabels("");
        function getLabels(ids){
        	$.ajax({
            	url:'/tagsdic/query?name=',   
            	type:'GET',
            	success:function(data){
            		if(data.successful){
            			var str='',data=data.data,checkBoxStr="";
                		$.each(data,function(index,item){
                			str+='<option value="'+item.id+'">'+item.name+'</option>';
            				if(ids.indexOf(item.id)>=0){
            					checkBoxStr+='<input type="checkbox" name="'+item.id+'" title="'+item.name+'" checked>';
                			}else{
                				checkBoxStr+='<input type="checkbox" name="'+item.id+'" title="'+item.name+'">';
                			}
                		})
                		$("#labels div").html(checkBoxStr);
                		/*$('#tags').html('<option value="">请选择</option>').append(str);*/
                		form.render();
            		}
            	},
            	error:function(error){
            		layer.msg('标签加载失败');
            	}
        	})
        }
        //获取用户列表角色
        function getRole(){
        	$.get('/role/getRoleInfoListByUserManage',function(res){
            	if(res.successful){
            		var data = res.data;
                	var str = '';
            		$.each(data,function(index,item){
            			str += '<option value="'+item.id+'">'+item.itemName+'</option>';
            		})
            		$('#role').html('<option value="all">请选择角色</option>').append(str);
            		form.render();
            	}
            })
        }
        function getStatus(str){
        	var status = '----';
        	if(str == 0){
        		status = '未完善资料';
        	}else if(str == 1){
        		status = '审核通过';
        	}else if(str == 2){
        		status = '待审核';
        	}else if(str == 3){
        		status = '驳回申请';
        	}else if(str == 4){
        		status = '禁用';
        	}
        	return status;
        }
        //搜索
        var sName = '',sPhone = '',sRoleId = '',sUserStatus = '',sInUse = '',pageSize=10,pageNum=1,pageSum=1;
        var sQuery = '';
        $('#search').on('click',function(){
        		getList(1);
        })
        //获取用户列表
        getList(pageNum);
        function getList(pageNum){
	        	layer.open({
	        	    type: 3,
	        	    content: ""
	    	    });
	        	//sQuery = '?pageSize='+pageSize+'&&pageNumber='+pageNum
	        	sRoleId = $('#s-role').val() == 'all' ? '' : $('#s-role').val();
	        	sUserStatus = $('#search-audit-status').val() == 'all' ? '' : $('#search-audit-status').val();
	        	sInUse = $('#search-is-use').val() == 'all' ? '' : $('#search-is-use').val();
	        	sName = $.trim($('#searchName').val());
	        	sPhone = $.trim($('#searchPhone').val());
	        	/*if(sName != ''){
	        		sQuery += sName && '&&name='+sName;
	        	}
	        	if(sPhone != ''){
	        		sQuery += sPhone && '&&mobile='+sPhone;
	        	}
	        	if(sRoleId !=  ''){
	        		sQuery += '&&roleId='+sRoleId;
	        	}
	        	if(sUserStatus != ''){
	        		sQuery += '&&userStatus='+sUserStatus;
	        	}
	        	if(sInUse != ''){
	        		sQuery += '&&inUse='+sInUse;
	        	}*/
	        	var sQuery={name:sName,mobile:sPhone,roleId:sRoleId,userStatus:sUserStatus,inUse:sInUse,pageSize:10,pageNumber:pageNum};
	        	$.ajax({
		        	url:'/backstage/managerList',
		        	type:'POST',
		        	data: sQuery,
		    		dataType: "json",
		        	success:function(res){
	        			layer.close(layer.index);
		            	if(res.successful){
		            		var data = res.items;
		            		var str = '';
		            		pageSum = res.pagesCount;
		            		if(pageSum>1){
		            			$('#pagging').show();
		            		}
		            		$.each(data,function(index,item){
		            			str += '<tr managerId="'+item.managerId+'"><td>'+(item.name?item.name:'----')+'</td><td>'+(item.mobile?item.mobile:'----')+'</td>'+
		            			'<td>'+(item.role?item.role:'----')+'</td>';
		            			if(item.role=="个人专家"||item.role=="机构"){
		            				str += '<td><span>'+(item.tagNames?item.tagNames:"")+'</span><i class="reviseStock" data-id="'+item.tagIds+'"></i></td>';
		            			}else{
		            				str += '<td></td>';
		            			}
		            			str += '<td>'+getStatus(item.userStatus)+'</td><td>'+(item.inUse==0?'否':(item.inUse == 1?'是':'----'))+'</td>'+
		            			'<td><button class="layui-btn layui-btn-small revise'+(item.userStatus==0?' layui-btn-disabled" disabled':'"')+'>修改</button><button class="layui-btn layui-btn-danger layui-btn-small delBtn';
		            			str += item.inUse==0?' layui-btn-disabled" disabled>删除</button>':'">删除</button>';
		            			/*if(item.roleId == 1 || item.roleId == 2 || item.userStatus !=1){
	//	            				str += '</td></tr>';
		            				str += '<button class="layui-btn roleBind layui-btn-disabled layui-btn-small" disabled>角色绑定</button>';
		            			}else{
		            				str += '<button class="layui-btn roleBind layui-btn-small">角色绑定</button>';
		            			}*/
		            			if(item.userStatus=="2"){
		            				str += '<button class="layui-btn canCheck layui-btn-small userAudit">审核</button>';
		            			}else{
		            				str += '<button class="layui-btn layui-btn-disabled layui-btn-small userAudit">审核</button>';
		            			}
		            			str +='</td></tr>';
		            		})
	            			laypage({
				        		cont: 'pagging'
			        			, pages: pageSum
			        			, curr: res.pageNumber||1
			        			,jump: function(obj, first){
			        				if(!first){
			        					pageNum = obj.curr;
				        				getList(pageNum);
			        				}
			        			}
				        	});
		            	}else{
		            		str += '<tr><td colspan="6">暂无数据</td></tr>';
		            	}
	            		$('#userList tbody').html(str);
	            },	
	            error:function(error){
	        		layer.close(layer.index);
	        		$('#userList tbody').html('<tr><td colspan="7">列表获取失败</td></tr>');
	        	}
	        })
        }
	  //修改标签
		$("#userList").delegate(".reviseStock","click",function(){
			var thiz = $(this);
			var manager_id=thiz.parents("tr").attr("managerid");
			var ids = thiz.attr("data-id");
			getLabels(ids);
			layer.open({
	            title:'机构标签',
	            type: 1,
	            area: ['550px', '400px'],//宽高
	            shade: 0.5,
	            content: $('#labels'),
	            btn: ['确定','关闭'], //按钮
	            yes: function(index){
	            	var tags="";
	            	$("#labels input").each(function(){
						if($(this).is(":checked")){
							if(tags){
								tags+="$";
							}
							tags+=$(this).attr("name");
						}
	    			});
	            	 var data={ tags: tags,manager_id: manager_id};
	            	 $.ajax({
                        url:'/backstage/modifyTags',
	      		        type:'POST',
	      		        data: data,
	      		        dataType:"json",
                        success:function(res){
	                        	if(res.successful){
	                        		if(sendStr == 'add'){
	                            		layer.msg('添加成功!');
	    	                        }else{
	                            		layer.msg('修改成功!');
	    	                        }
//	                        		pagging();
	                        		setTimeout(function(){
	                        			getList(pageNum);
		                        		layer.closeAll();
	        	        			},1500);
	                        	}else{
	                        		$('.opa0').remove();
	                        		layer.msg(res.resultCode.message);
	                        	}
	                        },
	                        error:function(error){
	                        	$('.opa0').remove();
	        	        		if(sendStr == 'add'){
                            		layer.msg('添加失败!');
    	                        }else{
                            		layer.msg('修改失败!');
    	                        }
	        	        	}
	                    })
	            }
	            ,success: function(layero){
	  	          	layer.setTop(layero); //重点2
	  	        }
	        });
		});
        var modifyId = '';
        var roleId = '';
        var sendStr = 'add';
        /*form.on('select(add-role)', function(data){
        	if(isBind){
				$('#LAY_demo').css('height','80px');
			}
        });*/
        //添加、修改、绑定角色弹框
        function addFun(){
	        layer.open({
	            type: 1
	            ,title:['添加','font-size:18px;background-color:#eee;']
	            ,id: 'LAY_demo'
	            ,content: $("#addUser")
	            ,btn: ['确定','取消']
	            ,btnAlign: 'c'
	            ,shade: 0.5
	            ,area: '500px'
	            ,yes: function(index){
            		var userName = $.trim($("#userName").val());
            		var userPhone = $('#userPhone').val();
            		var phoneReg = /^1[0-9]{10}$/;
            		var userEmail = $('#userEmail').val();
            		var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	                var address = $.trim($("#userAddress").val());
	                var userDes = $.trim($("#userDes").val());
	                var isUse = $('input[name="is-use"]:checked').val();
	                if($('.role-item').attr('roleId')){
	                    	roleId = $('.role-item').attr('roleId');
	                    }else{
	                    	roleId = $('#role').val();
	                    }	            			
	//	        	        userStatus = $('#audit-status').val();
	                    if(userName == ''){
	                    	layer.msg('姓名不能为空');
	                    	return;
	                    }
	                    if(sendStr != 'modify'){
	                    	if(userPhone == ''){
	                        	layer.msg('手机号码不能为空');
	                        	return;
	                        }else if(!phoneReg.test(userPhone)){
	                        	layer.msg('请输入正确的手机号码');
	                        	return;
	                        }
	                        if(userEmail == ''){
	                        	layer.msg('邮箱地址不能为空');
	                        	return;
	                        }else if(!reg.test(userEmail)){
	                        	layer.msg('请输入正确的邮箱地址');
	                        	return;
	                        }
	                    }
	                    if(roleId == ''||roleId == 'all'){
	                    	layer.msg('请选择角色');
	                    	return;
	                    }
	                    /*if(userStatus == ''){
	                    	layer.msg('请选择审核状态');
	                    	return;
	                    }*/
	                    var data ={name:userName,address:address,roleId:roleId,desc:userDes,inUse:isUse};
	                    if(sendStr == 'add'){
	                    	data.mobile = userPhone;
	                    	data.email = userEmail;
	                    	var sendUrl = '/backstage/add';
	                    }else{
	                    	data.managerId = modifyId;
	                    	var sendUrl = '/backstage/modify';
	                    }
	                		$('.layui-layer-btn').prepend('<span class="opa0"></span>');
	                    $.ajax({
	                        url:sendUrl,
	                        type:'POST',
	                        data:$.toJSON(data),
	                        dataType:"json",
	                        contentType: "application/json",
	                        success:function(res){
	                        	if(res.successful){
	                        		if(sendStr == 'add'){
	                            		layer.msg('添加成功!');
	    	                        }else{
	                            		layer.msg('修改成功!');
	    	                        }
	                        		setTimeout(function(){
	                    				window.location.reload(true)
	                        		},1500)
	                        	}else{
	                        		$('.opa0').remove();
	                        		layer.msg(res.resultCode.message);
	                        	}
	                        },
	                        error:function(error){
	                        	$('.opa0').remove();
	        	        		if(sendStr == 'add'){
                            		layer.msg('添加失败!');
    	                        }else{
                            		layer.msg('修改失败!');
    	                        }
	        	        	}
	                    })
	            	}
		        	,btn2: function(index){
		        		layer.closeAll();
		        	}
		        });
		    }

        $('.site-demo-button .layui-btn').on('click', function(){
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });
        //添加用户
        $('#addBtn').on('click',function(){
	        	sendStr = 'add';
	        	getRole();
	        	addFun();
	        	$('.layui-layer-title').html('添加');
	        	$('#addUser .layui-form-item').show();
	        	$('#audit-status option[value="1"]').prop('selected',true);
	        	$('#LAY_demo').css('height','auto');
	        	form.render();
        })
        //修改
        $('#userList').delegate('.revise','click',function(){
	        	sendStr = 'modify';
	        	getRole();
	        	addFun();
	        	$('.layui-layer-title').html('修改');
	        	$('#addUser .layui-form-item').show();
	        	$('.modifyNone').hide();
	        	$('#LAY_demo').css('height','auto');
	        	modifyId = $(this).parents('tr').attr('managerId');
	        	$.get('/backstage/query?managerId='+modifyId+'&&pageSize=1&&pageNumber=1',function(res){
	        		if(res.successful){
	        			var data = res.data.list[0];
	        			if(data.roleId == 1 || data.roleId == 2){
	        				$('.role-item').hide().attr('roleId',data.roleId);
	        			}else{
	        				$('#role').val(data.roleId);
	        				$('.role-item').removeAttr('roleId');
	        			}
	        			$('#userName').val(data.name);
	        			/*$('#userPhone').val(data.mobile);
	        			$('#userEmail').val(data.email);*/
	        			$('#userAddress').val(data.address);        			
	        			$('#audit-status').val(data.userStatus);
	        			$('#userDes').val(data.desc);
	        			$('input:radio[name="is-use"][value="'+data.inUse+'"]').prop('checked',true);
	        			form.render();
	        		}
	        	})
        })
        //删除
        $('#userList').delegate('.delBtn','click',function(){
        	var managerId = $(this).parents('tr').attr('managerId');
        	layer.confirm('确认要删除？',{
	        	btn:['确认','取消']
        	 	,btnAlign: 'c'
	        },function(){
	        	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
        		$.ajax({
        			url:'/backstage/remove?managerId='+managerId,
        			type:'GET',
                    success:function(res){
                    	$(".layui-layer-btn a").removeClass("layui-btn-disabled");
                        if(res.successful){
	                        	layer.msg('删除成功');
	                        	setTimeout(function(){
	                				window.location.reload(true)
	                    		},1500)
                        }else{
                        		$('.opa0').remove();
                        }
                    },
                    error:function(error){
                    		$('.opa0').remove();
	    	        			layer.msg('删除失败!');
                    }
        		})
        	})
        })
        /*账户审核*/
        $('#userList').delegate('.canCheck','click',function(){
        	var managerId = $(this).parents('tr').attr('managerId');
        	layer.open({
                type: 2 //2为弹出iframe
                ,title: '账号审核'
                ,area: ['100%', '100%']
        		,shade: 0.5
                /*,maxmin: true*///缩放功能
                /*,offset: [ //居中显示
                   $(window).height()/2-350
                  ,$(window).width()/2-350
                ] */
                ,content: 'supplyInfo.html?managerId='+managerId+'&state=PEDING'
                ,btn: ['驳回', '通过','关闭'] //按钮
                ,yes: function(index,layero){
            			layer.open({
	                    type: 1 //2为弹出iframe
	                    ,title: '填写退回原因'
	                    ,area: ['500px', '400px']
	            			,shade: 0.5
	                    /*,maxmin: true*///缩放功能
	                    ,offset: [ //居中显示
	                       $(window).height()/2-200
	                      ,$(window).width()/2-250
	                    ] 
	                    ,content: $("#suggestion")
	                    ,btn: ['确定','关闭'] //按钮
	                    ,yes: function(index){
	                    	var auditMessage = $("#auditMessage").val();
	                    	if(!auditMessage){
                        		$(".opa0").remove();
                        		layer.msg("驳回原因不能为空!",{time: 2000,zIndex: layer.zIndex});return;
                        	}
	                    	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
	                    	$.post('/backstage/audit?managerId='+managerId+'&auditMessage='+auditMessage+'&userStatus=3',function(res){
	                    		if(res.successful){
	                    			if(res.resultCode.code == "SUCCESS"){
	                    				layer.msg("驳回成功！",{time: 2000,zIndex: layer.zIndex},function(){
	                    					window.location.reload();
	                    				});
	                    			}else{
	                    				$('.opa0').remove();
	                    				layer.msg(res.resultCode.message);
	                    			}
	                    		}else{
	                    			$('.opa0').remove();
	                    			layer.msg(res.resultCode.message);
	                    		}
	                    	})
	                    }
	                    ,zIndex: layer.zIndex //重点1
	                    ,success: function(layero){
	                    	layer.setTop(layero); //重点2
	                    	$("#auditMessage").focus();
	                    }
            		});
                },btn2: function(index,layero){
                	$('.layui-layer-btn').prepend('<span class="opa0"></span>');
                	$.post('/backstage/audit?managerId='+managerId+'&auditMessage=&userStatus=1',function(res){
                		if(res.successful){
                			layer.msg("审核成功！",{time: 2000,zIndex: layer.zIndex},function(){
                				window.location.reload();
                			});
                		}else{
                			$('.opa0').remove();
                			layer.msg(res.resultCode.message);
                		}
                	});
                	return false;
                }
                ,zIndex: layer.zIndex //重点1
                ,success: function(layero){
                  layer.setTop(layero); //重点2
                }
              });
        })
		
    });
}
