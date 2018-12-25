<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Commodity create</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/dommodity/disable.action">
    <table border="1">
        <tr>
            <td>usertemporaryid</td>
            <td><input type="text" name="temporaryid"></td>
        </tr>
        <tr>
            <td>dommodityid</td>
            <td><input type="text" name="id"></td>
        </tr>
        <tr>
            <td><input type="submit" value="disable"></td>
        </tr>
    </table>
</form>
<a href="${pageContext.request.contextPath }/dommoditycreate.jsp.jsp">create</a>
</body>
</html>
