<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	// 图片本地存储路径: /usr/local/upFile/upFileEd    tomcat映射路径： /upFileEd/
	String saveRootPath = "/usr/local/upFile/";
	
	out.write( new ActionEnter( request, saveRootPath , rootPath ).exec() );
	
%>