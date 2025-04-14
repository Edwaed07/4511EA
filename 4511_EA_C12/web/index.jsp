<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AIB Web System - Role Selection</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <h1>Choose Your Role</h1>
        <div class="role-buttons">
            <button onclick="window.location.href='login.jsp?role=shopStaff'">Bakery Shop Staff</button>
            <button onclick="window.location.href='login.jsp?role=warehouseStaff'">Warehouse Staff</button>
            <button onclick="window.location.href='login.jsp?role=seniorManagement'">Senior Management</button>
        </div>
    </div>
</body>
</html>