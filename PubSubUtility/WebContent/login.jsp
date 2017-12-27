<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<style>
html {
	font-family: "Lato", sans-serif;
}
.title {
	padding: 8px;
	background-color: #00aaff;
    font-weight:bold;
}
</style>
</head>
<body>
	<div class=title><b>PubSub Utility Login</b></div>
	<form action="dbconnection"  method="post">
		<table>
			<tr>
				<td>Enter username: </td>
				<td><input type="text" name="username"></td>
			</tr>
			<tr>
				<td>Enter password:</td>
				<td><input type="password" name="password"></td>
			</tr>
		</table>
		<input type="submit" />
	</form>
</body></html>