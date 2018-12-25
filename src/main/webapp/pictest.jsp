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
<form action="${pageContext.request.contextPath }/picture/uploadPicture.action" method="post" enctype="multipart/form-data">
    <tr>
        <td>objectid</td>
        <td><input type="text" name="objectid"></td>
    </tr>
    <tr>
        <td>name</td>
        <td><input type="text" name="name"></td>
    </tr>
    <label>上传头像：</label><input type="File" name="pictureFile"><br>
    <input type="submit">
</form>
</body>
</html>
