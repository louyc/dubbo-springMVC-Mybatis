$(document).ready(function(){
	layui.use(['element','form','layer','jquery'], function(){
		var $ = layui.jquery, form = layui.form(),element = layui.element();
	    var layer = layui.layer;
	    login();
	    function login(){
	    	$("#loginBox").css("bottom","calc(50% - 110px)");
	    	$(".fd").css("top","5%");
	    };
	    /*登录*/
	    $("#login").on("click",function(){
	    	var login_username = $("#login_username").val();
			var login_password = $("#login_password").val();
			if(!login_username){
				layer.msg("账号不能为空");
				return ;
			}
			if(!login_password){
				layer.msg("密码不能为空");
				return ;
			}
			$(this).attr('disabled',true);
			$.ajax({
	            url:'/user/login/submit',
	            type:'POST',
	            data: {'userName': login_username,'password': login_password},
	            success : function(data){
	            	var resCode = data.resultCode.code;
            		var resMsg = data.resultCode.message;
        			if(resCode == 'ACCOUNT_EXISTS'){//未補全個人信息
                		var managerId = data.data.managerId;
            			layer.msg(resMsg,{time:2000},function(){
	            			window.location.href='/html/supplyInfo.html?managerId='+managerId+'&state=ACCOUNT_EXISTS';
            			});   
            		}else if(resCode == 'INIT'){//已注册但未完善资料
            			var managerId = data.data.managerId;
            			layer.msg(resMsg,{time:2000},function(){
	            			window.location.href='/html/supplyInfo.html?managerId='+managerId+'&state=INIT';
            			});
        			}else if(resCode == 'PEDING'){//已注册并完善资料,待审核
        				var managerId = data.data.managerId;
        				layer.msg(resMsg,{time:2000},function(){
        					window.location.href='/html/supplyInfo.html?managerId='+managerId+'&state=PEDING';
            			});
        			}else if(resCode == 'REJECT'){ 
        				var managerId = data.data.managerId;
        				layer.msg('您的账号申请被退回，请重新完善个人资料',{time:2000},function(){
	            			window.location.href='/html/supplyInfo.html?managerId='+managerId+'&state=REJECT';
            			}); 
        			}else if(resCode == 'PWD_ERROR'){
        				layer.msg('账号密码错误，请重新填写');
        			}else if(resCode == 'DISABLE'){
        				layer.msg('您的账号被禁用，已被禁止登陆');
        			}else if(resCode == 'ACCOUNT_NULL'){
        				layer.msg('您输入的账号不存在，请重新输入');
        			}else if(resCode == 'SUCCESS'){
        				window.utils.removeCookie('managerId');
            			window.utils.removeCookie('userName');
            			window.utils.removeCookie('roleId');
            			window.utils.removeCookie('imageUrl');
            			var newData = data.data;
            			window.utils.setCookie('managerId',newData.managerId,30);
            			window.utils.setCookie('userName',newData.name,30);
            			if(!!newData.type){
            				window.utils.setCookie('type',newData.type,30);
            			}else{
            				window.utils.setCookie('type',-1,30);
            			}
            			
            			if(newData.roleId == 1){
            				if(newData.doctorSignType == 1){
            					//如果是家庭医生 doctor_typ = 1
            					window.utils.setCookie('doctor_type',1,30);
            				}else{
            					window.utils.setCookie('doctor_type',0,30);
            				}
            			}
            			window.utils.setCookie('roleId',newData.roleId,30);
            			window.utils.setCookie('imageUrl',newData.image_url,30);
            			window.location.href="/html/main.html";
        			}else{
            			layer.msg(resMsg);
            		}
	            	/*if(data.successful){*/
//	            		window.location.href="/html/main.html";
	            	/*}else{
	            		layer.msg(data.resultCode.message);
	            	}*/
            		$('#login').removeAttr('disabled');
	            },
	            error : function(data){
	            	layer.msg('登录失败！');
	            	$('#login').removeAttr('disabled');
	            },
	        });
	    });
	    /*绑定回车*/
	    $('#login_password').keydown(function(e){
	    	var e = e || window.event || arguments.callee.caller.arguments[0];
	    	if(e && e.keyCode==13){
	    		$("#login").click();
	    	}
	    });
	    /*调出注册页面*/
	    $("#register").on("click",function(){
	    	$("#loginBox").css("top","110%");
	    	$("#registerBox").css("bottom","calc(50% - 140px)");
	    });
	    /*调出找回密码页面*/
	    $("#lostPassword").on("click",function(){
	    	$("#loginBox").css("top","110%");
	    	$("#retrievePassword").css("bottom","calc(50% - 102px)");
	    });
	    $("#getPassword").on("click",function(){
	    	var password_name = $("#password_name").val();
	    	var randCode = $("#randCode").val();
	    	var set_password = $("#set_password").val();
	    	if(!randCode){
	    		layer.msg('验证码不能为空！');return;
	    	}
	    	if(!set_password){
	    		layer.msg('设置密码不能为空！');return;
	    	}
	    	var data = new Object();
	    	$.ajax({
	            url:'/user/password/forget?mobile='+password_name+'&randCode='+randCode+'&password='+set_password,
	            type:'GET',
	            success : function(data){
	            	var resCode = data.resultCode.code;
            		var resMsg = data.resultCode.message;
            		if(resCode == 'SUCCESS'){
            			layer.msg(resMsg,{time:2000},function(){
	            			window.location.reload();
            			});
            		}else{
            			layer.msg(data.resultCode.message);
            		}
	            },
	            error : function(data){
	            	layer.msg(data.resultCode.message);
	            }
	    	});
	    });
	    /*注册*/
	    $("#registerBox #confirm_register").on("click",function(){
	    	var username = $("#username").val();
			var password = $("#password").val();
			var cellphoneCode = $("#cellphoneCode").val();
			var confirmPassword = $("#confirmPassword").val();
			
			var regMobile = /^1[0-9]{10}$/;
			if(!regMobile.test(username)){
				layer.msg("账号必须为正确格式的手机号！");
				return ;
			}
			if(!username){
				layer.msg("账号不能为空！");
				return ;
			}
			if(!password){
				layer.msg("密码不能为空！");
				return ;
			}
			if(!confirmPassword){
				layer.msg("确认密码不能为空！");
				return ;
			}
			if(confirmPassword !=password ){
				layer.msg("密码和确认密码不！");
				return ;
			}
			if(!cellphoneCode){
				layer.msg("手机验证码不能为空！");
				return ;
			}
			if(!$("#agreement").is(':checked')){
				layer.msg("请认真阅读用户协议！");
				return ;
			}
			var data = new Object();
			if(username.indexOf("@")>0){
				data.email = username;
			}else{
				data.mobile = username;
			}
			data.password = password;
			data.randCode = cellphoneCode;
			$(this).attr('disabled',true);
			$.ajax({
	            url:'/user/regist/submit',
	            type:'POST',
	            data: $.toJSON(data),
	            dataType:"json",
	            contentType: "application/json",
	            success : function(data){
            		var resCode = data.resultCode.code;
            		var resMsg = data.resultCode.message;
            		if(resCode == 'SUCCESS'){
            			layer.msg(resMsg + ' 即将跳转...',{time:2000},function(){
            				window.utils.removeCookie('managerId');
	            			window.utils.setCookie('managerId',data.data,30);
	            			window.location.href='/html/supplyInfo.html?managerId='+data.data;
            			});
        			}else if(resCode == 'ACCOUNT_REGISTERED'){
        				layer.msg(resMsg,{time:2000},function(){
        					window.location.reload()
        				});
        			}else{
            			layer.msg(resMsg);
            		}
        			$('#confirm_register').removeAttr('disabled');
	            },
	            error : function(data){
	            	layer.msg('注册失败！');
	            	$('#confirm_register').removeAttr('disabled');
	            }
	        });
	    });
	});
})