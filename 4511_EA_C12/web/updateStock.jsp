<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>4511 Web System - Update Stock</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">4511 Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                <li><a href="updateStockServlet">Update Stock</a></li>
                <li><a href="CheckReserveServlet">Check Reservations</a></li>
                <li><a href="FruitServlet">Fruit List</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Update Stock</h1>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <h2>Stock for Branch: ${sessionScope.employeeBranch}</h2>

        <c:choose>
            <c:when test="${empty stockList}">
                <p>No stock data available for this branch.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>Fruit Name</th>
                        <th>Stock Level</th>
                        <th>Action</th>
                    </tr>
                    <c:forEach var="stock" items="${stockList}">
                        <tr>
                            <td>${stock.fruitName}</td>
                            <td>${stock.stockLevel}</td>
                            <td>
                                <form action="updateStockServlet" method="post">
                                    <input type="hidden" name="fruitName" value="${stock.fruitName}">
                                    <input type="number" name="newStockLevel" min="0" value="${stock.stockLevel}" required>
                                    <input type="submit" value="Update">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>

        <p style="text-align: center; margin-top: 15px;">
            <a href="index.jsp">Back to Home</a>
        </p>
    </div>
    <footer>
        © 2025 4511 Bakery. All rights reserved.
    </footer>
</body>
</html>