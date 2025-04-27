<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Warehouse Staff Registration - AIB Warehouse Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
        }
        .register-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .register-logo {
            text-align: center;
            margin-bottom: 30px;
        }
        .register-logo h1 {
            font-weight: bold;
            color: #0d6efd;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <div class="register-logo">
                <h1>AIB Warehouse Management</h1>
                <p class="text-muted">Warehouse Staff Registration</p>
            </div>
            
            <% if (request.getAttribute("errorMsg") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("errorMsg") %>
            </div>
            <% } %>
            
            <form action="${pageContext.request.contextPath}/warehouse/register" method="post">
                <input type="hidden" name="action" value="register">
                
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                
                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>
                
                <div class="mb-3">
                    <label for="location" class="form-label">Warehouse Location (Country)</label>
                    <select class="form-select" id="location" name="location" required>
                        <option value="">Please select...</option>
                        <option value="Japan">Japan</option>
                        <option value="USA">USA</option>
                        <option value="Hong Kong">Hong Kong</option>
                    </select>
                </div>
                
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Register</button>
                </div>
            </form>
            
            <div class="mt-3 text-center">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/warehouse/login">Login</a></p>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 