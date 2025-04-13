<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Account - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Create New Account</h1>
    <form action="CreateAccountServlet" method="post">
        <label>Username:</label>
        <input type="text" name="username"><br>
        <label>Password:</label>
        <input type="password" name="password"><br>
        <label>Confirm Password:</label>
        <input type="password" name="confirmPassword"><br>
        <label>Shop Name:</label>
        <input type="text" name="shopName"><br>
        <label>Shop City:</label>
        <input type="text" name="shopCity"><br>
        <input type="submit" value="Create Account">
    </form>
    <a href="index.jsp">Back to Login</a>
</body>
</html>