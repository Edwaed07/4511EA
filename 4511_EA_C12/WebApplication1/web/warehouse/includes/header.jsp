<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"warehouse".equals(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/warehouse/login");
        return;
    }
    String currentPage = request.getServletPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Warehouse Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .navbar-brand {
            font-weight: bold;
        }
        .sidebar {
            min-height: calc(100vh - 56px);
            background-color: #f8f9fa;
            padding-top: 20px;
        }
        .content {
            padding: 20px;
        }
        .nav-link.active {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/warehouse/dashboard">AIB Warehouse Management</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.contains("/dashboard.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/warehouse/dashboard">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.contains("/inventory.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/warehouse/inventory">Inventory</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.contains("/reserves.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/warehouse/reserves">Reservations</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= currentPage.contains("/deliveries.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/warehouse/deliveries">Deliveries</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <span class="navbar-text me-3">
                        Welcome, <%= user.getUsername() %> | <%= user.getLocation() %>
                    </span>
                    <a href="${pageContext.request.contextPath}/warehouse/login?action=logout" class="btn btn-outline-light">Logout</a>
                </div>
            </div>
        </div>
    </nav>
    
    <div class="container-fluid mt-3">
        <% if (request.getAttribute("errorMsg") != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= request.getAttribute("errorMsg") %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } %>
        
        <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= request.getAttribute("message") %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } %>
    </div>
</body>
</html> 