window.onload=function(){
    layui.use('element',function(){
        var $=layui.jquery,
            element=layui.element();
        var layid = location.hash.replace(/^#test=/, '');
        element.tabChange('test', layid);
        element.on('tab(test)', function(elem){
            location.hash = 'test='+ $(this).attr('lay-id');
        });
       

        //获取user_id
       /* var allcookies = document.cookie;
        function getCookie(user_id)
        {
            var allcookies = document.cookie;
            var cookie_pos = allcookies.indexOf(user_id);   
            if (cookie_pos != -1)
            {
                cookie_pos += user_id.length + 1;   
                var cookie_end = allcookies.indexOf(";", cookie_pos);
                if (cookie_end == -1)
                {
                    cookie_end = allcookies.length;
                }
                var value = unescape(allcookies.substring(cookie_pos, cookie_end)); 
            }
            return value;
        }*/
        /*  var cookie_val = getCookie("user_id");*/
        
        //消息列表
        var cookieVal=window.utils.getCookie("user_id");
        var obj=new Object();
        obj.messageTo=cookieVal;obj.showCount=10;obj.currentPage=1;
        var dataJson=$.toJSON(obj);
        $.ajax({
        	url:'/message/getPageMessages',
        	type:'POST',
        	data:dataJson,
        	dataType:"json",
            contentType: "application/json",
            success:function(data){
            	if(data.successful){
            		var str='';
            		var datas=data.items;
            		$.each(datas,function(index,item){
            			str+='<p>'+item.content+'<span>'+item.createTime+'</span></p>'
            			/*if(item.userStatus == 1){
            				str+='<p>'+item.content+'<span>'+item.createTime+'</span></p>'
            			}else if(item.userStatus == 3){
            				str+='<p>'+item.content+'<a href="/html/supplyInfo.html" class="examine">请重新提交</a></p>'
            			}*/
            		})
            		$(".layui-list").html(str);
            	}
            }
        })
        
        

    })
}