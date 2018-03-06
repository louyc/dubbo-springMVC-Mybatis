//var player = videojs('example_video');
//videojs.options.flash.swf = "/js/player/video-js.swf";
layui.use(['element', 'form', 'layer', 'jquery','laypage'], function() {
	var $ = layui.jquery,form = layui.form(),element = layui.element();
	var layer = layui.layer,laypage = layui.laypage;
	var populate = {pageSize:20,currentPage:1,totleCount:10,pageCount:2};
	var global = {audioPlay:true,video:[],audio:[],image:[],type:'image'};
	// 搜索
//	$('.search-query').on('click',function(){
//		populate.currentPage = 1;
//		loading($('#tagName').val(),populate);
//		layer.closeAll();
//	});
//	$('#queryVal').keydown(function(e){
//    	var e = e || window.event || arguments.callee.caller.arguments[0];
//    	if(e && e.keyCode==13){
//    		$('#search-query').click();
//    	}
//    });
	
	// 加载
	function loading(val,populate){
		var token = window.utils.getCookie('token')
		if(token.slice(0,1)=='"'){
			token = token.slice(1);
		}
        if((!global.type) && (!token)){return false;}
		var params = '?type='+global.type+'&pageNumber='+populate.currentPage+'&pageSize=10&token='+token;
		$.ajax({
            url:'/source/query'+params,
            type:'GET',
            dataType:'json',
            success : function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    var list = data.items,html = '';
                    for(var i=0;i<list.length;i++){
                    	var item = list[i];
                    	html += '<tr>';
                        html += '<td>'+(i+1)+'</td>';
                        html += '<td>'+item.username+'</td>';
                        switch(global.type){
                        	case 'image':
//                              图片
                                html += '<td class="blockImages">';
                                html += '<div style="width:80px;height:60px;">';
                                html += '<img src="'+item.uri+'" alt="图片"/>';
                                html += '</div>';
                                html += '</td>';
                                break;
                        	case 'video':
//                              视频
                                html += '<td class="blockVideo">';
                                html += '<div style="width:80px;height:60px;">';
                                html += '<img src="/image/ic_movie_black_24dp_2x.png" alt="图片" customUri="'+item.uri+'"/>';
                                html += '</div>';
                                html += '</td>';
                        		break;
                        	case 'audio':
//                              音频
                            	html += '<td class="blockAudio">';
                            	html += '<div style="width:80px;height:60px;">';
                            	html += '<img src="/image/ic_music_video_black_24dp_2x.png" alt="图片" customUri="'+item.uri+'"/>';
                            	html += '<div id="audioBox"></div>'
                            	html += '</div>';
                            	html += '</td>';
                        		break;
                        	default:return;
                        }
                        html += '<td>'+window.utils.simpleDateFormat(item.created_at,'yyyy-MM-dd HH:mm:ss')+'</td>';
                        html += '<td>';
                        html += '<button class="layui-btn layui-btn-danger layui-btn-small del-node" customId="'+item.id+'">删 除</button>';
                        html += '</td>';
                        html += '</tr>';
                    }
//                    console.log(html);
                	laypage({
                	    cont: 'pageTag'
                	    ,pages: data.pagesCount
                	    ,groups: 5 //连续显示分页数
                	    ,curr:data.pageNumber || 1
                	    ,jump: function(obj, first){
                	    	if(!first){
                	    		//得到了当前页，用于向服务端请求对应数据
                	    		populate.currentPage = obj.curr;
	                		    loading('',populate); //$('#queryVal').val()
                	    	}
                		}
                	});
                	if(!html){return;}
                	if(global.type=="image"){
	                	global.image = list;
                		document.getElementById('imgBody').innerHTML = html;
                    	$('#imgBody tr td.blockImages div img').bind('click',function(e){
                    		layer.photos({
                    		    photos: $(this).parent()
                    		    ,anim: 5 
                    		});
                    	});
                	}else if(global.type=="audio"){
	                	global.audio = list;
                		document.getElementById('audioBody').innerHTML = html;
                		$('#imgBody tr td.blockAudio div img').bind('click',function(e){
                			var uri = $(this).attr('customUri');
                			layer.open({
            			        type: 2,
            			        title: '音频',
            			        shadeClose: true,
            			        shade: false,
            			        maxmin: true, 
            			        area: ['600px', '130px'],
            			        content: uri
                			 });
//                            var index = layer.open({
//                            	title:'video',
//                        	    type: 1,
//                        	    skin: 'layui-layer-rim', 
//                        	    area: ['500px', '150px'], 
//                        	    content: $('#audioShowBox'),
//                        	    cancel: function(index, layero){ 
//                        	    	document.getElementById('audioId').pause();
//                                    document.getElementById('audioBox').removeChild(document.getElementById('audioId'));
//                        	    	layer.close(index);
//                        	    	return false; 
//                    	    	}  
//                        	});
//                            if(!document.getElementById('audioId')){
//                                var audio = document.createElement('audio') //生成一个audio元素 
//                                audio.id = 'audioId'
//                                audio.controls = true; 
//                                audio.src = uri; 		
//                                document.getElementById('audioBox').appendChild(audio) //把它添加到页面中
//                                audio.play();
//                            }else{
//                                document.getElementById('audioId').pause();
//                                document.getElementById('audioBox').removeChild(document.getElementById('audioId'));
//                            }
                			// 'http://onx1uu3do.bkt.clouddn.com/4c04c2c58f03455091ca602c0ddc3d7d'
                    	});
                	}else if(global.type=="video"){
	                	global.video = list;
                		document.getElementById('videoBody').innerHTML = html;
                		$('#imgBody tr td.blockVideo div img').bind('click',function(e){
                			var uri = $(this).attr('customUri');
                			layer.open({
            			        type: 2,
            			        title: '视频',
            			        shadeClose: true,
            			        shade: false,
            			        maxmin: true, 
            			        area: ['893px', '560px'],
            			        content: uri
                			});
//                        	var url = 'http://ojv5qldw3.bkt.clouddn.com/wx_upload_1493109331168.mp4';
//                    		var msgDiv = document.getElementById("msgDiv");
//                            msgDiv.style.marginTop = -55 + document.documentElement.scrollTop + "px";
//                            var index = layer.open({
//                            	title:'video',
//                        	    type: 1,
//                        	    skin: 'layui-layer-rim', 
//                        	    area: ['660px', '330px'], 
//                        	    content: $('#msgDiv'),
//                        	    cancel: function(index, layero){ 
//                        	    	player.pause();
//                        	    	layer.close(index);
//                        	    	return false; 
//                    	    	} 
//                        	});
//                            player.src(uri);
//                            player.play();//   播放
//                        	player.pause();  暂停 player.src(url); 赋值 
                    	});
                	}
                	populate.currentPage = data.pageNumber*1;//当前页数
                    populate.totleCount = data.totalItemsCount*1;//总数
                    populate.pageCount = data.pagesCount*1; //总页数
                	// 删除
                	$('.del-node').on('click',function(event,id){
                		var customId = $(this).attr('customId');
                		var token = window.utils.getCookie('token');
                        if((!customId) && (!token)){return false;}
                        layer.confirm('确定删除当前信息，删除后无法恢复？', {
                            shadeClose: true, //开启遮罩关闭
                            btn: ['取消', '删除'] //按钮
                        }, function() {
                            layer.closeAll();
                        }, function() {
                        	var params = '?id='+customId+'&status=-1&token='+token;
                            $.ajax({
                                url:'/source/delete'+params,
                                type:'GET',
                                dataType:'json',
                                success:function(data){
                                    if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
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
                }else{
                    console.error(data);
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                layer.closeAll();
                console.error(error)
                layer.msg('查询失败!');
            }
        });
	};
	loading('',populate);
	// tab页点击切换
	$('.layui-tab-title li').on('click',function(e){
        var type = $(this).attr('customType');
        global.type = type;
        if((type=='image' && global.image)||(type=='audio' && global.audio)||(type=='video' && global.video)){return;}
		loading('',populate);
	});
});
function stopOrAction(e){
	if(global.audioPlay){
        document.getElementById('audioId').pause();
        $('.three').eq(0).css('display','none');
        $('.two').eq(0).css('display','inline-block');
        $('.audioPlayer').html('等待播放...');
    }else{
        document.getElementById('audioId').play();
        $('.three').eq(0).css('display','inline-block');
        $('.two').eq(0).css('display','none');
        $('.audioPlayer').html('正在播放...');
    }
	global.audioPlay = !global.audioPlay;
}