<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User login</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/user/login.action">
    <table border="1">
        <tr>
            <td>Username(-1 if login by email)</td>
            <td><input type="text" name="username" value="-1"></td>
        </tr>
        <tr>
            <td>Email Address(-1 if login by username)</td>
            <td><input type="text" name="email" value="-1"></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="text" name="password"></td>
        </tr>
        <tr>
            <td><input type="submit" value="login"></td>
        </tr>
    </table>
</form>
<a href="${pageContext.request.contextPath }/register.jsp">register</a>
<a href="${pageContext.request.contextPath }/logout.jsp">logout</a>
</body>
</html>
