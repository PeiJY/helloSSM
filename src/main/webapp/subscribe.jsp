<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>subscribe</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/dommodity/subscribe.action">
    <table border="1">
        <tr>
            <td>userid</td>
            <td><input type="text" name="temporaryid" value="-1"></td>
        </tr>
        <tr>
            <td>dommodityid</td>
            <td><input type="text" name="dommodityid" value="-1"></td>
        </tr>
        <tr>
            <td><input type="submit" value="subscribe"></td>
        </tr>
    </table>
</form>
</body>
</html>
