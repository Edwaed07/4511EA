<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Senior Management Home - AIB Web System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Welcome to Senior Management Dashboard</h1>
        <c:if test="${not empty sessionScope.message}">
            <p class="${sessionScope.messageType}">${sessionScope.message}</p>
            <% session.removeAttribute("message"); %>
            <% session.removeAttribute("messageType"); %>
        </c:if>
        <p>Hello, ${sessionScope.name}!</p>
        <div class="dashboard-options">
            <h2>Management Options</h2>
            <ul>
                <li><a href="InventoryReportServlet">View Inventory Report</a></li>
                <li><a href="SalesReportServlet">View Sales Report</a></li>
                <li><a href="StaffManagementServlet">Manage Staff</a></li>
            </ul>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>