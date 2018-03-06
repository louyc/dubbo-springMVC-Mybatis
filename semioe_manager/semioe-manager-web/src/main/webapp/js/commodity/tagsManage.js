layui.use(['element', 'form', 'layer', 'jquery','laypage'], function() {
	var $ = layui.jquery,form = layui.form(),element = layui.element();
	var layer = layui.layer,laypage = layui.laypage;
	var updateId = '';
	var populate = {pageSize:10,currentPage:1,totleCount:10,pageCount:2}

	// 加载
	function loading(val,populate){
		$.ajax({
            url:'/keywords/getAllKeywords',
            type:'POST',
            data:{name:val,pageSize:populate.pageSize,currentsPage:populate.currentPage},
            dataType:'json',
            success : function(data){
            	if(data.successful && (data.status === 200) && (data.resultCode.code === 'SUCCESS')){
                    var list = data.items,html = '';
                    if(!list || list.length==0){
                    	$('#tagsBody').html('<tr><td colspan="4" style="text-align:center;">未查询到任何数据</td></tr>')
                    	return false;
                    }
                    for(var i=0;i<list.length;i++){
                    	var item = list[i];
                    	html += '<tr>';
                        html += '<td>'+(i+1)+'</td>';
                        html += '<td>'+item.name+'</td>';
                        html += '<td>'+item.createTime+'</td>';
                        html += '<td>'+(item.inUse=='1'?'是':'否')+'</td>';
                        html += '<td>';
                        html += '<button class="layui-btn layui-btn-small edit-node" customId="'+item.id+'" customName="'+item.name+'" customUse="'+item.inUse+'">修 改</button>';
                        html += '<button class="layui-btn layui-btn-danger layui-btn-small del-node" customId="'+item.id+'">删 除</button>';
                        html += '</td>';
                        html += '</tr>';
                    }
                    if(html){
                    	html += '<tr><td>总条数:</td><td colspan="5">'+data.totalItemsCount+'</td></tr>';
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
                    		    populate.currentPage = obj.curr;
                    		    loading($('#tagName').val(),populate);
                	    	}
                		}
                	});
                    html && $('#tagsBody').html(html);
                    populate.currentPage = data.pageNumber*1;//当前页数
                    populate.totleCount = data.totalItemsCount*1;//总数
                    populate.pageCount = data.pagesCount*1; //总页数
                    bind();
                }else{
                    console.error(data);
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                console.error(error)
                layer.msg('查询失败!');
            }
        });
	}
	loading('',populate);
	// add
	form.on('submit(addform)', function(data) {
        var data = data.field;
        $.ajax({
            url:'/keywords/createKeywords',
            type:'POST',
            data:{name:data.name},
            dataType:'json',
            success:function(data){
                if(data.successful && data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    layer.closeAll();
                    layer.msg('添加成功!');
                    loading('',populate);
                    $('button[type="reset"]').click();
                }else{
                    console.error(data);
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                console.error(error);
                layer.closeAll();
                layer.msg('添加失败!');
            }
        })
        return false;
    });
	// update
	form.on('submit(updateform)', function(data) {
        var data = data.field;
        $.ajax({
            url:'/keywords/updateKeywords',
            type:'POST',
            data:JSON.stringify({id:updateId,name:data.name,inUse:data.isUse}),
            dataType:'json',
            contentType:'application/json',
            success:function(data){
                if(data.successful && data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    layer.closeAll();
                    layer.msg('修改成功!');
                    loading('',populate);
                    $('button[type="reset"]').click();
                }else{
                    console.error(data);
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                console.error(error);
                layer.closeAll();
                layer.msg('修改失败!');
            }
        });
        updateId = '';
        return false;
    });
	// 弹出add层
	$('.add-node').on('click',function(event){
		layer.open({
            title:'新增商品标签',
            type: 1,
            area: ['650px', '230px'],//宽高
            btnAlign: 'c',
            shade: 0.5,
            content: $('#form_add') 
        });
	});
	// 搜索
	$('#search-query').on('click',function(e){
		populate.currentPage = 1;
		loading($('#tagName').val(),populate);
		layer.closeAll();
	});
	$('#tagName').keydown(function(e){
    	var e = e || window.event || arguments.callee.caller.arguments[0];
    	if(e && e.keyCode==13){
    		$('#search-query').click();
    	}
    });
	function bind(){
		// 弹出update层
		$('.edit-node').on('click',function(event){
			layer.open({
	            title:'修改商品标签',
	            type: 1,
	            area: ['650px', '270px'],//宽高
	            btnAlign: 'c',
	            shade: 0.5,
	            content: $('#form_update') 
	        });
//			$('#form_update').find('input:radio[name="isUse"]').removeAttr('checked');
			updateId = $(this).attr('customId');
			$('#form_update').find('[name="name"]').val($(this).attr('customName'));
			$('#form_update').find('input:radio[name="isUse"][value="'+$(this).attr('customUse')+'"]').get(0).checked = true;
			form.render();
		});
		// 删除
		$('.del-node').on('click',function(event){
			var customId = $(this).attr('customId');
	        layer.confirm('确定删除当前信息，删除后无法恢复？', {
	            shadeClose: true, //开启遮罩关闭
	            btn: ['取消', '删除'] //按钮
	        }, function() {
	            layer.closeAll();
	        }, function() {
	            $.ajax({
	                url:'/keywords/deleteKeywords',
	                type:'POST',
	                data:{id:customId},
	                dataType:'json',
	                success:function(data){
	                    if(data.successful && data.status === 200 && data.resultCode.code === 'SUCCESS'){
	                        loading('',populate);
	                        layer.msg('删除成功!');
	                    }else{
	                        console.error(data);
	                        layer.msg(data.resultCode.message);
	                    }
	                },
	                error:function(error){
	                    console.error(error);
	                    layer.closeAll();
	                    layer.msg('删除失败!');
	                }
	            });
	        });
		});
	}
	
});