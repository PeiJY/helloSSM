<%--
  Created by IntelliJ IDEA.
  User: 裴季源
  Date: 2018/12/5
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/picture/downloadPicture.action" method="post" enctype="multipart/form-data">
    <tr>
        <td>picid</td>
        <td><input type="text" name="picid"></td>
    </tr>
    <input type="submit">
</form>
</body>
</html>
