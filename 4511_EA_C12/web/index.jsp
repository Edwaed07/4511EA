<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AIB Web System - Role Selection</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="logo">Acer International Bakery</div>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
            </ul>
        </nav>
        <h1>Welcome to AIB Web System</h1>
        <p>Please select your role to proceed:</p>
        <div class="role-selection">
            <p><a href="login.jsp?role=shopStaff">Bakery Shop Staff</a></p>
            <p><a href="login.jsp?role=warehouseStaff">Warehouse Staff</a></p>
            <p><a href="login.jsp?role=seniorManager">Senior Management</a></p>
        </div>
    </div>
    <footer>
        © 2025 Acer International Bakery. All rights reserved.
    </footer>
</body>
</html>