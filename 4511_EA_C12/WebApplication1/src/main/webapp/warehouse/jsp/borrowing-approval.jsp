<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>借用审批 - AIB系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <link href="${pageContext.request.contextPath}/warehouse/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/warehouse/dashboard">
                <i class="bi bi-house-door-fill"></i> AIB仓库管理系统
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/warehouse/dashboard">
                            <i class="bi bi-speedometer2"></i> 仪表板
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/warehouse/inventory">
                            <i class="bi bi-box-seam"></i> 库存管理
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/warehouse/reservations">
                            <i class="bi bi-calendar-check"></i> 预订管理
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/warehouse/borrowings">
                            <i class="bi bi-arrow-left-right"></i> 借用管理
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/warehouse/deliveries">
                            <i class="bi bi-truck"></i> 配送管理
                        </a>
                    </li>
                </ul>
                <div class="d-flex">
                    <div class="dropdown">
                        <button class="btn btn-light dropdown-toggle" type="button" id="userDropdown" data-bs-toggle="dropdown">
                            <i class="bi bi-person-circle"></i> ${staff.fullName}
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/warehouse/profile">
                                <i class="bi bi-person"></i> 个人资料
                            </a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/warehouse/logout">
                                <i class="bi bi-box-arrow-right"></i> 退出登录
                            </a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </nav>

    <!-- 主要内容 -->
    <div class="container mt-4">
        <!-- 错误消息提示 -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="bi bi-check-square"></i> 借用审批</h5>
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">借用详情</h5>
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <p><strong>借用ID:</strong> ${borrowing.borrowingId}</p>
                                <p><strong>借用人:</strong> ${borrowing.borrowerName}</p>
                                <p><strong>水果名称:</strong> ${borrowing.fruitName}</p>
                                <p><strong>借用数量:</strong> ${borrowing.quantity}</p>
                                <p><strong>用途:</strong> ${borrowing.purpose}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>预期归还日期:</strong> <fmt:formatDate value="${borrowing.expectedReturnDate}" pattern="yyyy-MM-dd" /></p>
                                <p><strong>状态:</strong> 
                                    <span class="badge bg-warning">待审批</span>
                                </p>
                                <p><strong>当前库存数量:</strong> ${currentStock}</p>
                            </div>
                        </div>
                        
                        <c:if test="${not sufficientStock}">
                            <div class="alert alert-danger">
                                <i class="bi bi-exclamation-triangle"></i> 警告：当前库存不足以满足借用需求。
                            </div>
                        </c:if>
                        
                        <div class="row mb-3">
                            <div class="col-12">
                                <h5>备注</h5>
                                <p>${not empty borrowing.remarks ? borrowing.remarks : '暂无备注'}</p>
                            </div>
                        </div>
                        
                        <hr/>
                        
                        <h5 class="card-title">处理借用申请</h5>
                        <form action="${pageContext.request.contextPath}/warehouse/borrowing-approval" method="post">
                            <input type="hidden" name="borrowingId" value="${borrowing.borrowingId}">
                            
                            <div class="mb-3">
                                <label for="remarks" class="form-label">添加备注/原因</label>
                                <textarea class="form-control" id="remarks" name="remarks" rows="3"></textarea>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <button type="submit" name="action" value="reject" class="btn btn-danger me-md-2">
                                    <i class="bi bi-x-circle"></i> 拒绝借用
                                </button>
                                <button type="submit" name="action" value="approve" class="btn btn-success" ${not sufficientStock ? 'disabled' : ''}>
                                    <i class="bi bi-check-circle"></i> 批准借用
                                </button>
                                <a href="${pageContext.request.contextPath}/warehouse/borrowings" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> 返回列表
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 页脚 -->
    <footer class="bg-light mt-5 py-3">
        <div class="container text-center">
            <p class="text-muted mb-0">© 2025 Acer International Bakery. 保留所有权利。</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 