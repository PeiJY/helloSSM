<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Commodity create</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/dommodity/createDommodity.action">
    <table border="1">
        <tr>
            <td>name</td>
            <td><input type="text" name="name"></td>
        </tr>
        <tr>
            <td>description</td>
            <td><input type="text" name="description"></td>
        </tr>
        <tr>
            <td>temporaryid</td>
            <td><input type="text" name="temporaryid"></td>
        </tr>
        <tr>
            <td>dtype</td>
            <td><input type="text" name="type"></td>
        </tr>
        <tr>
            <td>status</td>
            <td><input type="text" name="status"></td>
        </tr>
        <tr>
            <td>putawayTime</td>
            <td><input type="text" name="putawaytime"></td>
        </tr>
        <tr>
            <td>availableTime</td>
            <td><input type="text" name="availabletime"></td>
        </tr>
        <tr>
            <td>price</td>
            <td><input type="text" name="price"></td>
        </tr>
        <tr>
            <td>address</td>
            <td><input type="text" name="address"></td>
        </tr>
        <tr>
            <td>paytype</td>
            <td><input type="text" name="paytype"></td>
        </tr>
        <tr>
            <td><input type="submit" value="create"></td>
        </tr>
    </table>
</form>
<a href="${pageContext.request.contextPath }/login.jsp">back to login</a>
<a href="${pageContext.request.contextPath }/emailLink.jsp">email link</a>
<a href="${pageContext.request.contextPath }/dommoditydisable.jsp">disable dommodity</a>
</body>
</html>
