<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Check Reserve Records - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .back-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-align: center;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 15px;
            cursor: pointer;
        }
        .back-button:hover {
            background-color: #45a049;
        }
    </style>
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
        <h1>Check Reserve Records</h1>

        <p class="success"><strong>Database Connection Status:</strong> ${sessionScope.connectionStatus}</p>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>


        <form action="CheckReserveServlet" method="get" class="filter-form">
            <label for="filter">Filter Records:</label>
            <select name="filter" id="filter" onchange="this.form.submit()">
                <option value="both" ${param.filter == 'both' || empty param.filter ? 'selected' : ''}>Show Both</option>
                <option value="borrow" ${param.filter == 'borrow' ? 'selected' : ''}>Show Borrow Records</option>
                <option value="reserve" ${param.filter == 'reserve' ? 'selected' : ''}>Show Reserve Records</option>
            </select>
        </form>

        <h2>Records for Branch: ${sessionScope.employeeBranch}</h2>


        <c:if test="${param.filter != 'reserve' || empty param.filter}">
            <h3>Borrow Records</h3>
            <c:if test="${empty borrowRecords}">
                <p class="error">No borrow records found for this branch.</p>
            </c:if>
            <c:if test="${not empty borrowRecords}">
                <table>
                    <tr>
                        <th>Fruit Name</th>
                        <th>Borrow Branch</th>
                        <th>Lender Branch</th>
                        <th>Quantity</th>
                        <th>Borrow Date</th>
                        <th>Status</th>
                    </tr>
                    <c:forEach var="record" items="${borrowRecords}">
                        <tr>
                            <td>${record.fruitName}</td>
                            <td>${record.borrowBranch}</td>
                            <td>${record.lenderBranch}</td>
                            <td>${record.quantity}</td>
                            <td>${record.borrowDate}</td>
                            <td>${record.status}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </c:if>


        <c:if test="${param.filter != 'borrow' || empty param.filter}">
            <h3>Reserve Records</h3>
            <c:if test="${empty reserveRecords}">
                <p class="error">No reserve records found for this branch.</p>
            </c:if>
            <c:if test="${not empty reserveRecords}">
                <table>
                    <tr>
                        <th>Fruit Name</th>
                        <th>Branch</th>
                        <th>Source City</th>
                        <th>Quantity</th>
                        <th>Reserve Date</th>
                        <th>Status</th>
                    </tr>
                    <c:forEach var="record" items="${reserveRecords}">
                        <tr>
                            <td>${record.fruitName}</td>
                            <td>${record.branch}</td>
                            <td>${record.sourceCity}</td>
                            <td>${record.quantity}</td>
                            <td>${record.reserveDate}</td>
                            <td>${record.status}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </c:if>


        <div style="text-align: center;">
            <a href="staffHome.jsp" class="back-button">Back to Home</a>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>