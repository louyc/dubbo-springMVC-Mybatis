<!DOCTYPE html>
<head>
    <title></title>
    <meta charset="utf-8"/>
    <script src="../js/qrcode.js"></script>
    <style>
        #qrcode{
            position: absolute;
		    top: 0;
		    left: 0;
		    bottom: 0;
		    right: 0;
		    margin: auto;
		    width: 288px;
		    height: 288px;
        }
    </style>
</head>
<body>
	<div id="content">
		<div id="qrcode"></div>
	</div>
	<script>
	    window.onload =function(){
	        var qrcode = new QRCode(document.getElementById("qrcode"), {
	            width : 288,//设置宽高
	            height : 288
	        });
	        var deviceCode = "<%=request.getParameter("deviceCode")%>";
	        var erweima = deviceCode;
	        qrcode.makeCode(erweima);
	    }
	</script>
</body>
</html>