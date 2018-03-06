layui.use(['element', 'form', 'layer', 'jquery', 'laypage'],
function() {
    var $ = layui.jquery,
    form = layui.form(),
    element = layui.element();
    var layer = layui.layer,
    laypage = layui.laypage;
    //导出
    $('#exportBtn').on('click',function() {
        var name = $.trim($('#serviceName').val());
        var createName = $.trim($('#unit').val());
        var status = $('#auditStatus').val() == 'auto' ? '': $('#auditStatus').val();
        var query = '?';
        query += 'name=' + name + '&';
        query += 'createName=' + createName + '&';
        query += 'status=' + status + '&';
        $(this).attr('href', '/excelExport/service' + query);

    });
    function getData(pageNum, pageSize) {
        var serviceName = $("#serviceName").val(); //服务名称
        var unit = $("#unit").val(); //专家机构
        var auditStatus = $("#auditStatus").val() == "auto" ? "": $("#auditStatus").val();
        $.ajax({
            url: '/service/query?name=' + serviceName + '&status=' + auditStatus + '&createName=' + unit + '&pageNumber=' + pageNum + '&pageSize=' + pageSize + '',
            type: 'POST',
            async: false,
            success: function(data) {
                if (data.successful) {
                    var list = data.items;
                    var pageSum = list.length;
                    var tbody = "";
                    for (var i = 0; i < list.length; i++) {
                        var createName = list[i].createName ? list[i].createName: "";
                        var description = list[i].description ? list[i].description: "";
                        var keywordsName = list[i].keywordsName ? list[i].keywordsName: "";
                        var opinion = list[i].opinion ? list[i].opinion: "";
                        tbody += '<tr><td>' + list[i].name + '</td>';
                        tbody += '<td>' + createName + '</td>';
                        tbody += '<td>' + description + '</td>';
                        tbody += '<td  class="stage"><p>' + keywordsName + '</p><p><i class="reviseStock" data-id="' + list[i].id + '" data-keywords="'+list[i].keywordsIdList+'"></i></p></td>';
                        tbody += '<td>' + opinion + '</td>';
                        if (list[i].status == "1") {
                            tbody += '<td><button class="layui-btn layui-btn-small edit-node">通过</button>';
                            tbody += '<input type="hidden" value="' + list[i].id + '"/>';
                            tbody += '<button class="layui-btn layui-btn-danger layui-btn-small del-node" style="margin-left: 10px;">驳回</button></td><tr>';
                        } else {
                            tbody += '<td><button class="layui-btn layui-btn-small layui-btn-disabled" disabled>通过</button>';
                            tbody += '<input type="hidden" value="' + list[i].id + '"/>';
                            tbody += '<button class="layui-btn layui-btn-disabled layui-btn-small del-node" disabled style="margin-left: 10px;">驳回</button></td><tr>';
                        }
                    }
                    tbody += '<tr><td>总计：</td><td colspan="5">' + data.totalItemsCount + '</td></tr>';
                    $("table tbody").html(tbody);
                    $(".edit-node").on("click", function() {
                        var _this = $(this);
                        _this.addClass("layui-btn-disabled");
                        var id = $(this).next("input").val();
                        $.post('/service/examine?id=' + id + '&status=2&opinion=通过',
                        function(res) {
                            _this.removeClass("layui-btn-disabled");
                            if (res.successful) {
                                layer.msg("审核通过！", {
                                    time: 2000,
                                    zIndex: layer.zIndex
                                }, function() {
                                    pagging();
                                });
                            } else {
                                layer.msg(res.resultCode.message, {
                                    time: 2000,
                                    zIndex: layer.zIndex
                                });
                            }
                        })
                    });
                    $(".del-node").on("click", function() {
                        var id = $(this).prev("input").val();
                        layer.open({
                            type: 1,
                            title: '填写驳回原因',
                            area: ['500px', '400px'],
                            shade: 0.5
                            /*,maxmin: true*/
                            //缩放功能
                            ,
                            offset: [ //居中显示
                            $(window).height() / 2 - 200, $(window).width() / 2 - 250],
                            content: $("#suggestion"),
                            btn: ['确定', '关闭'] //按钮
                            ,
                            yes: function(index, layero) {
                                $('.layui-layer-btn').prepend('<span class="opa0"></span>');
                                var auditMessage = $("#auditMessage").val();
                                if (!auditMessage) {
                                    $('.opa0').remove();
                                    layer.msg("驳回原因不能为空!", {
                                        time: 2000,
                                        zIndex: layer.zIndex
                                    });
                                    return;
                                }
                                $.post('/service/examine?id=' + id + '&opinion=' + auditMessage + '&status=4',function(res) {
                                    if (res.successful) {
                                        layer.msg("驳回成功！", {
                                            time: 2000,
                                            zIndex: layer.zIndex
                                        },
                                        function() {
                                            layer.closeAll();
                                            pagging();
                                        });
                                    } else {
                                        layer.msg(res.resultCode.message, {
                                            time: 2000,
                                            zIndex: layer.zIndex
                                        },
                                        function() {
                                            layer.closeAll();
                                        });
                                    }
                                })
                            },
                            zIndex: layer.zIndex //重点1
                            ,
                            success: function(layero) {
                                layer.setTop(layero); //重点2
                                $("#auditMessage").focus();
                            }
                        });
                    })
                } else {
                    layer.msg(data.resultCode.message);
                }
            },
            error: function(data) {
                layer.msg('加载失败！');
            },
        });
    };
    //分页
    pagging();
    function pagging() {
        var obj = new Object();
        var serviceName = $("#serviceName").val(); //服务名称
        var unit = $("#unit").val(); //专家机构
        var auditStatus = $("#auditStatus").val() == "auto" ? "": $("#auditStatus").val();
        var pageSize = 1000;
        $.ajax({
            url: '/service/query?name=' + serviceName + '&status=' + auditStatus + '&createName=' + unit + '&pageNumber=1&pageSize=' + pageSize + '',
            type: 'POST',
            async: false,
            success: function(data) {
                if (!data.items) {
                    layer.msg('暂无数据！');
                    return false;
                }
                var pageSum = data.items.length;
                laypage({
                    cont: 'pagging',
                    pages: Math.ceil(pageSum / 10),
                    skip: false,
                    jump: function(obj, first) {
                        //得到了当前页，用于向服务端请求对应数据
                        pageNum = obj.curr;
                        getData(pageNum, 10);
                    }
                });
            },
            error: function(data) {
                layer.msg('失败！');
            },
        });

    }
    //搜索
    $("#search").on("click",function() {
        pagging();
    });
    // 获取标签
    function getLabels(ids){
    	$.ajax({
            url: '/keywords/getKeywordsByType',
            type: 'POST',
            data:$.toJSON({keyType:2}),
            dataType:"json",
            contentType: "application/json",
            success: function(res) {
                if (res.successful) {
                	var data=res.data,checkBoxStr="";
            		$.each(data,function(index,item){
            			checkBoxStr+='<input type="checkbox" name="'+item.id+'" title="'+item.name+'">';
            		})
            		$("#labels div").html(checkBoxStr);
            		form.render();
                } else {
                	console.error(res.resultCode);
                    layer.msg(res.resultCode.message);
                }
            },
            error: function(error) {
            	console.error(error);
                layer.msg('查询标签失败!');
            }
        })
    };
    getLabels('');
    // 修改标签
    $(".layui-table").delegate(".reviseStock", "click", function() {
        var _this = $(this);
        var id = _this.attr("data-id"), keywords = _this.attr('data-keywords');
        console.log(id,keywords)
        if(keywords){
        	var keywordsArr = keywords.split(',');
        	$("#labels input").each(function(index,ele){
        		for(var i=0;i<keywordsArr.length;i++){
					if(keywordsArr[i]==$(ele).attr('name')){
						console.log(ele);
	    				$(ele).prop('checked',true);
	    			}
        		}
        	});
        	form.render();
        }
        layer.open({
            title: '机构标签',
            type: 1,
            area: ['550px', '400px'],
            //宽高
            shade: 0.5,
            content: $('#labels'),
            btn: ['确定', '关闭'],
            //按钮
            yes: function(index) {
                var tags = "";
                $("#labels input").each(function() {
                    if ($(this).is(":checked")) {
                        if (tags) {
                            tags += ",";
                        }
                        tags += $(this).attr("name");
                    }
                });
                _this.addClass("layui-btn-disabled");
                $.get('/service/keyUpdate?id=' + id + '&keywordsIdList='+tags,function(res) {
                    _this.removeClass("layui-btn-disabled");
                    if (res.successful) {
                    	layer.closeAll();
                        layer.msg("添加成功！", {
                            time: 2000,
                            zIndex: layer.zIndex
                        }, function() {
                            pagging();
                        });
                    } else {
                    	layer.closeAll();
                        layer.msg(res.resultCode.message, {
                            time: 2000,
                            zIndex: layer.zIndex
                        });
                    }
                });
                
            },
            success: function(layero) {
                layer.setTop(layero); //重点2
            },
            end:function(){
            	$("#labels input").each(function(index,ele){
                    $(ele).prop("checked",false); 
            	});
            	form.render();
            }
        });
    });
});