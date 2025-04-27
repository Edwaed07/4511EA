<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>库存管理 - AIB系统</title>
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/warehouse/inventory">
                            <i class="bi bi-box-seam"></i> 库存管理
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/warehouse/reservations">
                            <i class="bi bi-calendar-check"></i> 预订管理
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/warehouse/borrowings">
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
        <!-- 成功消息提示 -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>
        
        <div class="row">
            <div class="col-md-12">
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="bi bi-box-seam"></i> 库存管理</h5>
                        <div>
                            <a href="${pageContext.request.contextPath}/warehouse/check-in" class="btn btn-success">
                                <i class="bi bi-plus-circle"></i> 入库
                            </a>
                            <a href="${pageContext.request.contextPath}/warehouse/check-out" class="btn btn-warning">
                                <i class="bi bi-dash-circle"></i> 出库
                            </a>
                            <a href="${pageContext.request.contextPath}/warehouse/transactions" class="btn btn-info">
                                <i class="bi bi-clock-history"></i> 交易记录
                            </a>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>水果名称</th>
                                        <th>来源地</th>
                                        <th>当前库存</th>
                                        <th>最低库存</th>
                                        <th>最高库存</th>
                                        <th>最后更新</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty inventoryList}">
                                            <tr>
                                                <td colspan="9" class="text-center">暂无库存记录</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="inventory" items="${inventoryList}">
                                                <tr>
                                                    <td>${inventory.inventoryId}</td>
                                                    <td>${inventory.fruitName}</td>
                                                    <td>${inventory.warehouseLocation}</td>
                                                    <td>${inventory.quantity}</td>
                                                    <td>${inventory.minimumStock}</td>
                                                    <td>${inventory.maximumStock}</td>
                                                    <td><fmt:formatDate value="${inventory.lastUpdated}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${inventory.quantity <= inventory.minimumStock}">
                                                                <span class="badge bg-danger">库存不足</span>
                                                            </c:when>
                                                            <c:when test="${inventory.quantity >= inventory.maximumStock}">
                                                                <span class="badge bg-warning">库存过多</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-success">正常</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <a href="${pageContext.request.contextPath}/warehouse/check-in?fruitId=${inventory.fruitId}" class="btn btn-outline-success">
                                                                <i class="bi bi-plus-circle"></i>
                                                            </a>
                                                            <a href="${pageContext.request.contextPath}/warehouse/check-out?inventoryId=${inventory.inventoryId}" class="btn btn-outline-warning">
                                                                <i class="bi bi-dash-circle"></i>
                                                            </a>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 低库存提醒 -->
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header bg-danger text-white">
                        <h5 class="mb-0"><i class="bi bi-exclamation-triangle"></i> 低库存提醒</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>水果名称</th>
                                        <th>仓库位置</th>
                                        <th>当前库存</th>
                                        <th>最低库存</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty lowStockList}">
                                            <tr>
                                                <td colspan="5" class="text-center">没有低库存记录</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="inventory" items="${lowStockList}">
                                                <tr>
                                                    <td>${inventory.fruitName}</td>
                                                    <td>${inventory.warehouseLocation}</td>
                                                    <td>${inventory.quantity}</td>
                                                    <td>${inventory.minimumStock}</td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/warehouse/check-in?fruitId=${inventory.fruitId}" class="btn btn-sm btn-success">
                                                            <i class="bi bi-plus-circle"></i> 入库
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 页脚 -->
    <footer class="bg-light mt-5 py-3">
        <div class="container text-center">
            <p class="text-muted mb-0"></p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 