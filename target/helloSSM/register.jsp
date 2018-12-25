<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User register</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/user/register.action">
    <table border="1">
        <tr>
            <td>Username</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="text" name="password"></td>
        </tr>
        <tr>
            <td>Email</td>
            <td><input type="text" name="email"></td>
        </tr>
        <tr>
            <td><input type="submit" value="register"></td>
        </tr>
    </table>
</form>
<a href="${pageContext.request.contextPath }/login.jsp">back to login</a>
<a href="${pageContext.request.contextPath }/emailLink.jsp">email link</a>
</body>
</html>
