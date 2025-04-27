<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>库存交易记录 - AIB系统</title>
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
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="bi bi-clock-history"></i> 库存交易记录</h5>
                        <div>
                            <a href="${pageContext.request.contextPath}/warehouse/inventory" class="btn btn-light">
                                <i class="bi bi-arrow-left"></i> 返回库存管理
                            </a>
                        </div>
                    </div>
                    <div class="card-body">
                        <!-- 过滤器 -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <form action="${pageContext.request.contextPath}/warehouse/transactions" method="get" class="d-flex">
                                    <select class="form-select" name="type">
                                        <option value="">所有交易类型</option>
                                        <option value="check_in" ${typeFilter eq 'check_in' ? 'selected' : ''}>入库</option>
                                        <option value="check_out" ${typeFilter eq 'check_out' ? 'selected' : ''}>出库</option>
                                        <option value="transfer" ${typeFilter eq 'transfer' ? 'selected' : ''}>转移</option>
                                    </select>
                                    <button type="submit" class="btn btn-primary ms-2">筛选</button>
                                </form>
                            </div>
                        </div>
                        
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>交易类型</th>
                                        <th>水果名称</th>
                                        <th>数量</th>
                                        <th>来源</th>
                                        <th>目的地</th>
                                        <th>操作人</th>
                                        <th>交易日期</th>
                                        <th>状态</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty transactions}">
                                            <tr>
                                                <td colspan="9" class="text-center">暂无交易记录</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="transaction" items="${transactions}">
                                                <tr>
                                                    <td>${transaction.transactionId}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${transaction.transactionType eq 'check_in'}">
                                                                <span class="badge bg-success">入库</span>
                                                            </c:when>
                                                            <c:when test="${transaction.transactionType eq 'check_out'}">
                                                                <span class="badge bg-warning">出库</span>
                                                            </c:when>
                                                            <c:when test="${transaction.transactionType eq 'transfer'}">
                                                                <span class="badge bg-info">转移</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${transaction.transactionType}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${transaction.fruitName}</td>
                                                    <td>${transaction.quantity}</td>
                                                    <td>${transaction.sourceLocation}</td>
                                                    <td>${transaction.destinationLocation}</td>
                                                    <td>${transaction.staffName}</td>
                                                    <td><fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${transaction.status eq 'pending'}">
                                                                <span class="badge bg-warning">待处理</span>
                                                            </c:when>
                                                            <c:when test="${transaction.status eq 'approved'}">
                                                                <span class="badge bg-primary">已批准</span>
                                                            </c:when>
                                                            <c:when test="${transaction.status eq 'completed'}">
                                                                <span class="badge bg-success">已完成</span>
                                                            </c:when>
                                                            <c:when test="${transaction.status eq 'cancelled'}">
                                                                <span class="badge bg-danger">已取消</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${transaction.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
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