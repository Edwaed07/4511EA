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
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="staffHome.jsp">Home</a></li>
                <li><a href="updateStockServlet">Shop Stock</a></li>
                <li><a href="FruitServlet">Reserve Fruit</a></li>
                <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                <li><a href="CheckReserveServlet">Check Reservations</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>View Stock</h1>

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
                        <th>Source City</th>
                        <th>Country</th>                        
                        <th>Stock Level</th>

                    </tr>
                    <c:forEach var="stock" items="${stockList}">
                        <tr>
                            <td>${stock.fruitName}</td>
                            <td>${stock.sourceCity}</td>
                            <td>${stock.country}</td>                            
                            <td>${stock.stockLevel}</td>

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
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>