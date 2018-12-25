<html>

<head>
    <title>test</title>
</head>

<body>
<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
<script>
    function login() {
        var myData = {
            "username": "12233",
            "password": "32211",
            "id": 1
        };
        $.ajax({
            type: "POST",
            url: "user/json",
            dataType:"json",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(myData)
        });
    }
</script>
<h1>login</h1>
<input type="button" value="click me to login" onclick="login()">
</body>

</html>
