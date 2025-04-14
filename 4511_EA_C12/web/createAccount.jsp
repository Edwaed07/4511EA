<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AIB Web System - Sign Up</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <h1>Sign Up as <%= request.getParameter("role") %></h1>
        <form action="CreateAccountServlet" method="post">
            <input type="hidden" name="role" value="<%= request.getParameter("role") %>">
            <label>Email:</label>
            <input type="email" name="email" required>
            <label>Name:</label>
            <input type="text" name="name" required>
            <label>Password:</label>
            <input type="password" name="password" required>
            <label>Confirm Password:</label>
            <input type="password" name="confirmPassword" required>
            <input type="submit" value="Create Account">
        </form>
        <p><a href="login.jsp?role=<%= request.getParameter("role") %>">Back to Login</a></p>
    </div>
</body>
</html>