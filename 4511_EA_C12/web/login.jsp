<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AIB Web System - Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <h1>Login as <%= request.getParameter("role") %></h1>
        <form action="LoginServlet" method="post">
            <input type="hidden" name="role" value="<%= request.getParameter("role") %>">
            <label>Email:</label>
            <input type="email" name="email" required>
            <label>Password:</label>
            <input type="password" name="password" required>
            <input type="submit" value="Login">
        </form>
        <p><a href="createAccount.jsp?role=<%= request.getParameter("role") %>">Create new account</a></p>
        <p><a href="index.jsp">Back to Role Selection</a></p>
    </div>
</body>
</html>