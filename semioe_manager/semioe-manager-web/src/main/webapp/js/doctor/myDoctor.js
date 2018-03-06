window.onload=function(){
	layui.use(['element','layer','form','laypage'],function(){
		var $=layui.jquery,layer = layui.layer,form = layui.form();
		var laypage = layui.laypage;
		
		//获取医生列表
		var pageExplicit=5,currExplicit=1;
		function doctorList(currExplicit){
			var doctor=$('#doctor').val();
			
			/*var signOrgId='0e880463-c362-4727-90ad-18008b87163d';*/
			
			var signOrgId=window.utils.getCookie("managerId");
			var roleType = window.utils.getCookie('type');
			var signType=$('#auditStatus').val()=='all' ? '' : $('#auditStatus').val();
			var validTime='';
			var data={showCount:pageExplicit,currentPage:currExplicit,signType:signType,signOrgId:signOrgId,validTime:'',name:doctor};
			var dataJson=$.toJSON(data);
			$.ajax({
				url:'/jyDoctorInfo/getJySignDoctorInfo',
				type:'POST',
				data:dataJson,
				dataType:"json",
        		contentType: "application/json",
        		success:function(data){
        			if(data.successful){
    					var str='';
        				var datas=data.items;
        				if(datas.length <= 0){
            				str+='<tr><td colspan="7" style="text-align:center">暂无数据</td></tr>';
        				}else{
            				$.each(datas,function(index,item){
            					var signText = '';
            					if(item.signType===1){
            						signText='已加入';
            					}
            					if(item.signType===0){
            						signText='待通过';
            					}
            					if(item.signType===2){
            						signText='已拒绝';
            					}
            					str+='<tr id='+item.id+'><td><img src="'+item.imageUrl+'" style="width:80px;height:80px;margin-right:30px">'+(item.name?item.name:'未填写')+'</td>';
            					str+='<td>'+(item.company?item.company:'未填写')+'</td>';
            					str+='<td>'+(item.mobile?item.mobile:'未填写')+'</td>'
            					/*str+='<td>'+(item.signTime?item.signTime:'未填写')+'</td>';
            					str+='<td>'+(item.outTime?item.outTime:'未填写')+'</td>';
            					str+='<td>'+(item.inviteTime?item.inviteTime:'未填写')+'</td>';*/
            					str+='<td>';
            					if(roleType == 4){
            						if(!!item.jySignTypes){
            							str+='<div class="layui-input-block qianyueType" style="margin-left: 0;margin-right: 0;" data-id="'+item.signId+'"><input type="checkbox" class="qianyue" name="1" lay-skin="checkbox" lay-filter="filter" title="家医签约" '+(item.jySignTypes.indexOf(1)>=0?"checked":"")+'>';
            							str+='<input type="checkbox" class="qianyue" name="3" lay-skin="checkbox" lay-filter="filter" title="孕产签约" '+(item.jySignTypes.indexOf(3)>=0?"checked":"")+'></div>';
            						}else{
            							str+='<div class="layui-input-block qianyueType" style="margin-left: 0;margin-right: 0;" data-id="'+item.signId+'"><input type="checkbox" class="qianyue" lay-skin="checkbox" lay-filter="filter" name="1" title="家医签约">';
            							str+='<input type="checkbox" class="qianyue" lay-skin="checkbox" lay-filter="filter" name="3" title="孕产签约"></div>';
            						}
            					}
            					str+='</td>';
            					str+='<td>'+signText+'</td></tr>'
            				})
        				}
            			$('#doctorBox tbody').html(str);
            			form.render();
            			form.on('checkbox(filter)', function(data){
        				    var signId = data.othis.parent(".qianyueType").attr("data-id");
        					var type="";
        					data.othis.parent(".qianyueType").children('.qianyue').each(function(){
        						if($(this).is(":checked")){
        							if(!!type){
        								type+=",";
        							}
        							type+=$(this).attr("name");
        						}
        					})
        				    $.ajax({
        						  url:'/jyDoctorInfo/changeSignType',
        						  type:'POST',
        						  async:false,
        						  data:{signId: signId,types: type},
        						  dataType:"json",
        			        	  success:function(data){
        			        			if(data.successful){
        			        				layer.msg(data.resultCode.message);
        			        			}else{
        			        				layer.msg('失败');
        			        			}
        			        			
        			        		},error:function(error){
        			        			layer.msg('失败');
        			        		}
        					  })
        				});    
        				laypage({
                      	    cont: 'pagging'
                      	    ,pages: data.pagesCount
                      	    ,groups: 5 
                      	    ,curr:data.pageNumber || 1
                      	    ,jump: function(obj, first){
                      	    	if(!first){
                      	    		currExplicit = obj.curr;
                      	    		doctorList(currExplicit);
                      	    	}
                     		}
                      	});
        			}
        		},error:function(){
        			$('#doctorBox tbody').html('<tr><td colspan="7">获取列表失败</td></tr>');
        		}
			})
		}
		doctorList(currExplicit);
		//搜索
		$('#search').on('click',function(){
			doctorList(1);
		})
		//邀请医生
		$('#invite').on('click',function(){
			layer.open({
           	  type:1
           	   ,title:'邀请医生'
           	   ,content: $('#goods-info-box')
               ,area: ['500px','330px']
				,cancel:function(){
					$('.detail').hide();
					$('#doctorNum').val('');
				}
           })
		})
		//医生搜索
		$('#searchDoctor').on('click',function(){
			$('.detail').show();
			var doctorVal=$('#doctorNum').val();
			var mobile='';
			var managerId='';
			var data={name:doctorVal,mobile:mobile,managerId:managerId}
			var dataJson=$.toJSON(data);
			$.ajax({
				url:'/jyDoctorInfo/findJyDoctorInfo',
				type:'POST',
				data:dataJson,
				dataType:"json",
        		contentType: "application/json",
        		success:function(data){
        			if(data.successful){
        				var str='';
        				datas=data.data;
        				$.each(datas,function(index,item){
        					str+='<dl id='+item.managerId+'><dt><img src="'+item.imageUrl+'" style="width:80px;height:80px"></dt>';
        					str+='<dd><p>'+(item.name+'</br>'+item.company+'</br>'+item.title)+'</p></dd>';
        					str+='</dl>';
        					if(item.doctorSignType==1){
        						str+='<a href="javascript:;" class="layui-btn layui-btn-normal layui-btn-disabled" disabled'+'>邀请加入</a>';
        					}else{
        						str+='<a href="javascript:;" class="layui-btn layui-btn-normal inviteJoin"'+'>邀请加入</a>';
        					}
//    					str+=item.doctorSignType==1 ? '<a href="javascript:;" class="layui-btn layui-btn-normal layui-btn-disabled" disabled' : '<a href="javascript:;" class="layui-btn layui-btn-normal inviteJoin"'+'>邀请加入</a>';
       				})
        				$('.detail').html(str);
        			}
        		},error:function(error){
        			layer.msg('搜索失败');
        		}
			})
		})
		 $('.detail').delegate('.inviteJoin','click',function(){
			  var doctorId=$(this).parents('.detail').children().eq(0).attr('id');
			 /* var orgId='0e880463-c362-4727-90ad-18008b87163d';*/
			  var orgId=window.utils.getCookie("managerId");
			  var data={doctorId:doctorId,orgId:orgId};
			  var dataJson=$.toJSON(data);
			  $.ajax({
				  url:'/jyOrgInfo/inviteDoctor',
				  type:'POST',
				  data:dataJson,
				  dataType:"json",
	        		contentType: "application/json",
	        		success:function(data){
	        			if(data.successful){
	        				layer.msg(data.resultCode.message);
	        				setTimeout(function(){
		        				window.location.reload(true)
		            		},1500)
	        			}else{
	        				layer.msg('失败');
	        			}
	        			
	        		},error:function(error){
	        			layer.msg('失败');
	        		}
			  })
			  
		  })
		
	})
}