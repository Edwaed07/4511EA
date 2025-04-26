<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - 4511 Web System</title>
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
                <li><a href="ManagementHome.jsp">Home</a></li>
                <li><a href="user_management.jsp">Manage Users</a></li>
                <li><a href="reserve_needs_report.jsp">Reserve report</a></li>
                <li><a href="senior_management_dashboard.jsp">Dashboard</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>User Management</h1>

        <p class="success"><strong>Database Connection Status:</strong> ${sessionScope.connectionStatus}</p>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

        <h2>Existing Users</h2>
        <c:if test="${empty users}">
            <p class="error">No users found.</p>
        </c:if>
        <c:if test="${not empty users}">
            <table>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.role}</td>
                        <td>
                            <a href="EditUserServlet?id=${user.id}">Edit</a>
                            <form action="UserManagementServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="userId" value="${user.id}">
                                <input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete this user?');">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <div style="text-align: center;">
            <a href="AddUser.jsp" class="back-button">Add User</a>
            <a href="staffHome.jsp" class="back-button">Back to Home</a>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery.
    </footer>
</body>
</html>