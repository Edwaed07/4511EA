<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fruit List - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="FruitServlet">Fruit List</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Fruit List</h1>

        <!-- 顯示資料庫連接狀態 -->
        <p class="success"><strong>Database Connection Status:</strong> ${connectionStatus}</p>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Source City</th>
                <th>Stock Level</th>
            </tr>
            <c:forEach var="fruit" items="${fruits}">
                <tr>
                    <td>${fruit.id}</td>
                    <td>${fruit.name}</td>
                    <td>${fruit.sourceCity}</td>
                    <td>${fruit.stockLevel}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <footer>
        &copy; 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>