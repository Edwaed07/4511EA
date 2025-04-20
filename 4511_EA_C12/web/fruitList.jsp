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
                <li><a href="staffHome.jsp">Home</a></li>
                <li><a href="updateStockServlet">Shop Stock</a></li>
                <li><a href="FruitServlet">Reserve Fruit</a></li>
                <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                <li><a href="CheckReserveServlet">Check Reservations</a></li>
                <li><a href="FruitManagementServlet">Manage Fruits</a></li>
                <li><a href="ApproveServlet">Approve Requests</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Fruit List</h1>

        <!-- 顯示資料庫連接狀態 -->
        <p class="success"><strong>Database Connection Status:</strong> ${connectionStatus}</p>

        <!-- 顯示員工分店 -->
        <h2>Borrow Branch: ${sessionScope.employeeBranch}</h2>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Source City</th>
                <th>Stock (box)</th>
                <th>Action</th>
            </tr>
            <c:forEach var="fruit" items="${fruits}">
                <tr>
                    <td>${fruit.id}</td>
                    <td>${fruit.name}</td>
                    <td>${fruit.sourceCity}</td>
                    <td>${fruit.stockLevel}</td>
                    <td>
                        <form action="FruitServlet" method="post" class="action-form">
                            <input type="hidden" name="page" value="reserveFruit">
                            <input type="hidden" name="fruitId" value="${fruit.id}">
                            <div class="stock-group">
                                <input type="number" name="quantity" min="1" max="${fruit.stockLevel}" required>
                                <input type="submit" value="Reserve">
                            </div>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p style="text-align: center; margin-top: 15px;">
            <a href="staffHome.jsp">Back to Home</a>
        </p>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>