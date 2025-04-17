<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
            </ul>
        </nav>
        <h1>Login as <%= request.getParameter("role") %></h1>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <form action="LoginServlet" method="post">
            <input type="hidden" name="role" value="<%= request.getParameter("role") %>">
            <label>Email:</label>
            <input type="email" name="email" required>
            <label>Password:</label>
            <input type="password" name="password" required>
            <input type="submit" value="Login">
        </form>
        <p style="text-align: center; margin-top: 15px;">
            <a href="createAccount.jsp?role=<%= request.getParameter("role") %>">Create new account</a> | 
            <a href="index.jsp">Back to Role Selection</a>
        </p>
    </div>
    <footer>
        &copy; 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>