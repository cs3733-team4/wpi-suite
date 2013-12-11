<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Documentation</title>
<style>
/* Basic style. Could really be improved */
h1 {
    border-bottom: 1px solid #000000;
}
ul {
    list-style: none outside none;
}
ul > li a {
    display: inline-block;
    padding: 0.5em;
    text-decoration: none;
}
ul > li a:hover {
    text-decoration: underline;
}
ul > li a:after {
    content: " →";
    font-weight: bold;
}
</style>
</head>
<body>
<%@page import="java.io.*" %>
<%

File jsp = new File(request.getSession().getServletContext().getRealPath(request.getServletPath()));
File dir = jsp.getParentFile();
File[] list = dir.listFiles();
if (list.length > 1)
{
	%><h1>Documentation</h1><ul><%
	for (File f : list)
	{
		if (f.getName().equals("index.jsp"))
			continue;
		%>
		<li><a href="<%= f.getName() %>"><%= f.getName() %></a></li>
		<%
	}
	%></ul><%
}
else
{
	%><h1>Missing Documentation</h1>
	<p>Hmm... Something went wrong. No files in the documentation directory. Did you build correctly?</p>
	<%
}
%>
</body>
</html>