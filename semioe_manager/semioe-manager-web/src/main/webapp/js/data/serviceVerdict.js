layui.use(['element','form','laypage','layer','jquery'], function(){
    var $ = layui.jquery, form = layui.form();
    var laypage = layui.laypage,layer = layui.layer,element = layui.element();
    var pageSum = 1;
    /*var relationCode = window.utils.getRequestParam("relationCode");*/
    var id = window.utils.getRequestParam("id");
    var data_list = "";
    var token = window.utils.getCookie('token');
    var host = 'workflow-api.semioe.com';
	if(window.location.host == 'cs.semioe.com'){
		host = 's-workflow-api.semioe.com';
	}
    getData();
    //列表查询
    function getData(){
    	var obj = new Object();
	    obj.orderId = id;
	    obj.currentPage = 1;
	    obj.showCount = 1000;
	    $.ajax({
	        url:'/orderInfo/getReportArray',
	        type:'POST',
	        data: $.toJSON(obj),
	        dataType:"json",
	        contentType: "application/json",
	        success : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	var list = data.items;
	            if(data.successful){
	            	if(list.length <=0){
	            		layer.msg("暂无数据！",{zIndex: layer.zIndex});
	            	}else{
	            		var str = "";
	            		for(var i=0;i<list.length;i++){
	            				str+='<tr><td>'+(i+1)+'</td>';
	            				str+='<td>'+list[i].createTime+'</td>';
	            				str+='<td><button class="layui-btn layui-btn-small serviceVerdict" data-orderId="'+list[i].orderId+'" data-val="'+list[i].reportDesc+'" data-id="'+list[i].dialogId+'">查看报告</button></td></tr>';
	            		}
	            		$(".layui-table tbody").html(str);
	            		$(".serviceVerdict").on("click",function(){
	            			var dialogId = $(this).attr("data-id");
	            			var orderId = $(this).attr("data-orderId");
	            			var reportDesc = $(this).attr("data-val");
	            			if(!dialogId||dialogId=="null"){
	            				layer.open({
	            					type: 1 
	            					,title: '服务结论报告'
	        						,area: ['390px', '260px']
		            				,shade: 0
		            				,maxmin: true
		            				,content: reportDesc
		            				,btn: ['关闭'] //按钮
		            				,zIndex: layer.zIndex //重点1
		            				,success: function(layero){
		            					layer.setTop(layero); //重点2
		            				}
	            				});
	            			}else{
	            				window.open(encodeURI('http://'+host+'/v1/procedure-dialog/result/'+dialogId+'-'+orderId));
	            			}
	            		})
	            	}
	            }
	        },
	        error : function(data){
	        	layer.close(layer.index);//取消遮罩
	        	layer.msg('失败！');
	        },
	    });
    }
});
