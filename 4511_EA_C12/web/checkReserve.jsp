<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Check Reserve Records - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="FruitServlet?page=borrowFruit">Borrow Fruit</a></li>
                <li><a href="updateStock.jsp">Update Stock</a></li>
                <li><a href="CheckReserveServlet">Check Reservations</a></li>
                <li><a href="FruitServlet">Fruit List</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Check Reserve Records</h1>

        <p class="success"><strong>Database Connection Status:</strong> ${sessionScope.connectionStatus}</p>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <h2>Reservation Records for Branch: ${sessionScope.employeeBranch}</h2>

        <c:if test="${empty reserveRecords}">
            <p class="error">No reservation records found for this branch.</p>
        </c:if>

        <c:if test="${not empty reserveRecords}">
            <table>
                <tr>
                    <th>Fruit Name</th>
                    <th>Borrow Branch</th>
                    <th>Lender Branch</th>
                    <th>Quantity</th>
                    <th>Borrow Date</th>
                </tr>
                <c:forEach var="record" items="${reserveRecords}">
                    <tr>
                        <td>${record.fruitName}</td>
                        <td>${record.borrowBranch}</td>
                        <td>${record.lenderBranch}</td>
                        <td>${record.quantity}</td>
                        <td>${record.borrowDate}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <p style="text-align: center; margin-top: 15px;">
            <a href="staffHome.jsp">Back to Home</a>
        </p>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>