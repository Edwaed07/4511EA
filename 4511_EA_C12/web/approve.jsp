<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Approve Borrow Requests - AIB Web System</title>
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
        <h1>Approve Borrow Requests (Branch: ${sessionScope.employeeBranch})</h1>

        <p class="success"><strong>Database Connection Status:</strong> ${sessionScope.connectionStatus}</p>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>


        <h2>Pending Borrow Requests to Approve</h2>
        <c:if test="${empty pendingBorrowRecords}">
            <p class="error">No pending borrow requests to approve.</p>
        </c:if>
        <c:if test="${not empty pendingBorrowRecords}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>From Branch</th>
                    <th>To Branch</th>
                    <th>Fruit Name</th>
                    <th>Source City</th>
                    <th>Quantity</th>
                    <th>Borrow Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="record" items="${pendingBorrowRecords}">
                    <tr>
                        <td>${record.id}</td>
                        <td>${record.lender_branch}</td>
                        <td>${record.borrow_branch}</td>
                        <td>${record.fruit_name}</td>
                        <td>${record.source_city}</td>
                        <td>${record.quantity}</td>
                        <td>${record.borrow_date}</td>
                        <td>${record.status}</td>
                        <td>
                            <form action="ApproveServlet" method="post" class="action-form">
                                <input type="hidden" name="action" value="approve">
                                <input type="hidden" name="recordId" value="${record.id}">
                                <input type="hidden" name="lenderBranch" value="${record.lender_branch}">
                                <input type="hidden" name="borrowBranch" value="${record.borrow_branch}">
                                <input type="hidden" name="fruitId" value="${record.fruit_id}">
                                <input type="hidden" name="fruitName" value="${record.fruit_name}">
                                <input type="hidden" name="sourceCity" value="${record.source_city}">
                                <input type="hidden" name="quantity" value="${record.quantity}">
                                <input type="submit" value="Approve">
                            </form>
                            <form action="ApproveServlet" method="post" class="action-form">
                                <input type="hidden" name="action" value="reject">
                                <input type="hidden" name="recordId" value="${record.id}">
                                <input type="hidden" name="lenderBranch" value="${record.lender_branch}">
                                <input type="hidden" name="borrowBranch" value="${record.borrow_branch}">
                                <input type="hidden" name="fruitId" value="${record.fruit_id}">
                                <input type="hidden" name="fruitName" value="${record.fruit_name}">
                                <input type="hidden" name="sourceCity" value="${record.source_city}">
                                <input type="hidden" name="quantity" value="${record.quantity}">
                                <input type="submit" value="Reject">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>


        <h2>Borrow Requests Awaiting Other Branches' Approval</h2>
        <c:if test="${empty myBorrowRecords}">
            <p class="error">No borrow requests awaiting approval from other branches.</p>
        </c:if>
        <c:if test="${not empty myBorrowRecords}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>From Branch</th>
                    <th>To Branch</th>
                    <th>Fruit Name</th>
                    <th>Source City</th>
                    <th>Quantity</th>
                    <th>Borrow Date</th>
                    <th>Status</th>
                </tr>
                <c:forEach var="record" items="${myBorrowRecords}">
                    <tr>
                        <td>${record.id}</td>
                        <td>${record.lender_branch}</td>
                        <td>${record.borrow_branch}</td>
                        <td>${record.fruit_name}</td>
                        <td>${record.source_city}</td>
                        <td>${record.quantity}</td>
                        <td>${record.borrow_date}</td>
                        <td>${record.status}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>


        <h2>Reserve Requests Awaiting Source City Approval</h2>
        <c:if test="${empty myReserveRecords}">
            <p class="error">No reserve records found for your branch.</p>
        </c:if>
        <c:if test="${not empty myReserveRecords}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Branch</th>
                    <th>Fruit Name</th>
                    <th>Source City</th>
                    <th>Quantity</th>
                    <th>Reserve Date</th>
                    <th>Status</th>
                </tr>
                <c:forEach var="record" items="${myReserveRecords}">
                    <tr>
                        <td>${record.id}</td>
                        <td>${record.branch}</td>
                        <td>${record.fruit_name}</td>
                        <td>${record.source_city}</td>
                        <td>${record.quantity}</td>
                        <td>${record.reserve_date}</td>
                        <td>${record.status}</td>
                    </tr>
                </c:forEach>
            </table>
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