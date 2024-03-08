package com.web.project.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/resources/*", "/resource/*",},
initParams = {
		@WebInitParam(name="key",value="값")
})
public class ResourceMapping extends HttpServlet{
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
String info = req.getPathInfo();
String token= info.substring(info.lastIndexOf('.')+1); 
if(token.equalsIgnoreCase("json")) {  //js파일들 가져오기
resp.setContentType("application/json"); 
try(OutputStream out = resp.getOutputStream();  
		InputStream in = getServletContext().getResourceAsStream("/WEB-INF/resources/" + token+info);){
	out.write(in.readAllBytes());
}catch(Exception e) {}

}
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
resp.setContentType("text/html"); //내용물의 MIME정보(내가 보는 것의 형식)를 세팅하는 메서드(기본값)
resp.setCharacterEncoding("utf-8"); //언어세팅
}

}
