window.onload=function(){
	layui.use(['element','layer','form','laypage'],function(){
		var $=layui.jquery,layer = layui.layer,form = layui.form();
		var laypage = layui.laypage;
		//获取机构列表
		var pageExplicit=5,currExplicit=1;
		function organzia(currExplicit){
			var name=$('#mechanism').val();
			var signType=$('#auditStatus').val()=='all' ? '' : $('#auditStatus').val();
			var managerId=window.utils.getCookie("managerId");
			var data={showCount:pageExplicit,currentPage:currExplicit,doctorId:managerId,signType:signType,name:name};
			var dataJson=$.toJSON(data);
			$.ajax({
				url:'/jyOrgInfo/getJyDoctorSignOrg',
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
	        					if(item.signType==0){
	        						signText='待通过'
	        					}
	        					if(item.signType==1){
	        						signText='已加入';
	        					}
	        					if(item.signType==2){
	        						signText='已拒绝'
	        					}
	        					
	        					str+='<tr id='+item.signId+'><td>'+(item.name?item.name:'未填写')+'</td>';
	        					str+='<td>'+(item.mobile?item.mobile:'未填写')+'</td>';
	        					str+='<td>'+(item.signTime?item.signTime:'')+'</td>';
	        					str+='<td>'+(item.outTime?item.outTime:'')+'</td>';
	        					str+='<td>'+(item.inviteTime?item.inviteTime:'')+'</td>';
	        					str+='<td> '+signText+'</td>';
	        					if(item.signType==1 || item.signType==2){
	        						str+='<td><button class="layui-btn layui-btn-disabled agree" disabled>同意</button><button class="layui-btn  layui-btn-normal layui-btn-disabled refuse" disabled>拒绝</button></td></tr>'
	        					}else if(item.signType==0){
	        						str+='<td><button class="layui-btn agree">同意</button><button class="layui-btn  layui-btn-normal refuse">拒绝</button></td></tr>';
	        					}
	        					
	        				})
        				}
            			$('.layui-table tbody').html(str);
        				laypage({
                      	    cont: 'pagging'
                      	    ,pages: data.pagesCount
                      	    ,groups: 5 
                      	    ,curr:data.pageNumber || 1
                      	    ,jump: function(obj, first){
                      	    	if(!first){
                      	    		currExplicit = obj.curr;
                      	    		organzia(currExplicit);
                      	    	}
                     		}
                      	});
        			}
        		},error:function(){
        			$('.layui-table tbody').html('<tr><td colspan="10">获取列表失败</td></tr>');
        		}
			})
		}
		organzia(currExplicit);
		//搜索
		$('#search').on('click',function(){
			organzia(1);
		})
		
		//同意
		$('.layui-table').delegate('.agree','click',function(){
			var relId=$(this).parents('tr').attr('id');
			$.ajax({
				url:'/jyDoctorInfo/agreeInvite?relId='+relId,
				type:'GET',
        		success:function(data){
        			$(this).parents('tr').children().eq(5).html('已加入');
        			layer.msg(data.resultCode.message);
        			setTimeout(function(){
        				window.location.reload(true)
            		},1500)
        		}
			})
		})
		
		//拒绝
		$('.layui-table').delegate('.refuse','click',function(){
			var relId=$(this).parents('tr').attr('id');
			$.ajax({
				url:'/jyDoctorInfo/refuseInvite?relId='+relId,
				type:'GET',
        		success:function(data){
        			$(this).parents('tr').children().eq(5).html('已拒绝');
        			layer.msg(data.resultCode.message);
        			setTimeout(function(){
        				window.location.reload(true)
            		},1500)
        		}
			})
		})
		
	})
}