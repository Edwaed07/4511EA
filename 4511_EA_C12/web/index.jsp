<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AIB Web System - Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Welcome to AIB Web System</h1>
    <h2>Please Log In</h2>
    <form action="LoginServlet" method="post">
        <label>Username:</label>
        <input type="text" name="username"><br>
        <label>Password:</label>
        <input type="password" name="password"><br>
        <input type="submit" value="Login">
    </form>
    <p>Don't have an account? <a href="createAccount.jsp">Create Account</a></p>
</body>
</html>