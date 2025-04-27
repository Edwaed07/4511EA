<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>错误 - AIB 仓库管理系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
            padding-top: 50px;
        }
        .error-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 30px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .error-icon {
            font-size: 72px;
            color: #dc3545;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-container">
            <div class="error-icon">
                <i class="bi bi-exclamation-triangle-fill"></i>
                &#9888;
            </div>
            <h1 class="mb-4">出错了！</h1>
            
            <% if (response.getStatus() == 404) { %>
                <p class="lead">找不到请求的页面。</p>
                <p>您尝试访问的页面不存在或已被移动。</p>
            <% } else { %>
                <p class="lead">处理您的请求时出现错误。</p>
                <p>请稍后再试或联系系统管理员。</p>
                
                <% if (exception != null) { %>
                <div class="mt-4 text-start bg-light p-3 rounded">
                    <h6>错误详情：</h6>
                    <p class="text-danger"><%= exception.getMessage() %></p>
                </div>
                <% } %>
            <% } %>
            
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/warehouse/dashboard" class="btn btn-primary me-2">返回仪表板</a>
                <a href="${pageContext.request.contextPath}/warehouse/login" class="btn btn-outline-secondary">返回登录</a>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 