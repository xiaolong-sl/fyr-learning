<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <p>This is login page</p>
    <form action="/login" method="post">
        <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}">
        <table>
            <tr>
                <th>用户名：</th>
                <td><input type="text" id="username" name="username"></td>
            </tr>
            <tr>
                <th>密码：</th>
                <td><input type="password" id="password" name="password"></td>
            </tr>
            <tr>
                <th>验证码：</th>
                <td><input type="text" id="captcha" name="captcha"></td>
            </tr>
        </table>
        <input type="submit" value="登录">
    </form>
</body>
</html>