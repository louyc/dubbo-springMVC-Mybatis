<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
	<script type="text/javascript">
		function requestByJson() {
			alert("开始跳转");
			var name = $("#test").val();
			alert(name);
			$.ajax({
				type : 'post',
				url : 'hello',
				//设置contentType类型为json
				contentType : 'application/json;charset=utf-8',
				//json数据
				data : '{"name":'+name+',"password":"psw"}',
				//请求成功后的回调函数
				success : function(data) {
					alert(data.username);
				}
			});
		}
	</script>
	<span>姓名</span> <input type="text" id="test" name="name" value="" />
	<span>提交</span> <button onclick="requestByJson()" value="确认"></button>
	
	<form action="help">  
	<span>用户名：</span><input name="name"/>  
	<span>密码：</span><input name="pass"/>  
	<input type="submit" value="登陆">  
	</form>  
</body>
</html>
