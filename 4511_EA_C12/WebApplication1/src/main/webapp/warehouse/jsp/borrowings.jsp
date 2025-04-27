<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>借用管理 - AIB系统</title>
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
        <!-- 成功消息提示 -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>
        
        <!-- 错误消息提示 -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <!-- 逾期借用警告 -->
        <c:if test="${not empty overdueBorrowings && overdueBorrowings.size() > 0}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong><i class="bi bi-exclamation-triangle"></i> 注意！</strong> 有 ${overdueBorrowings.size()} 条借用记录已经逾期未归还。
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="bi bi-arrow-left-right"></i> 借用管理</h5>
                    </div>
                    <div class="card-body">
                        <!-- 过滤器 -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <form action="${pageContext.request.contextPath}/warehouse/borrowings" method="get" class="d-flex">
                                    <select class="form-select" name="status">
                                        <option value="">所有状态</option>
                                        <option value="pending" ${statusFilter eq 'pending' ? 'selected' : ''}>待审批</option>
                                        <option value="approved" ${statusFilter eq 'approved' ? 'selected' : ''}>已批准</option>
                                        <option value="borrowed" ${statusFilter eq 'borrowed' ? 'selected' : ''}>已借出</option>
                                        <option value="returned" ${statusFilter eq 'returned' ? 'selected' : ''}>已归还</option>
                                        <option value="rejected" ${statusFilter eq 'rejected' ? 'selected' : ''}>已拒绝</option>
                                        <option value="overdue" ${statusFilter eq 'overdue' ? 'selected' : ''}>已逾期</option>
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
                                        <th>借用人</th>
                                        <th>水果名称</th>
                                        <th>数量</th>
                                        <th>借用日期</th>
                                        <th>预计归还日期</th>
                                        <th>实际归还日期</th>
                                        <th>用途</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty borrowings}">
                                            <tr>
                                                <td colspan="10" class="text-center">暂无借用记录</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="borrowing" items="${borrowings}">
                                                <tr>
                                                    <td>${borrowing.borrowingId}</td>
                                                    <td>${borrowing.borrowerName}</td>
                                                    <td>${borrowing.fruitName}</td>
                                                    <td>${borrowing.quantity}</td>
                                                    <td><fmt:formatDate value="${borrowing.borrowDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                    <td><fmt:formatDate value="${borrowing.expectedReturnDate}" pattern="yyyy-MM-dd" /></td>
                                                    <td>
                                                        <c:if test="${not empty borrowing.actualReturnDate}">
                                                            <fmt:formatDate value="${borrowing.actualReturnDate}" pattern="yyyy-MM-dd HH:mm" />
                                                        </c:if>
                                                        <c:if test="${empty borrowing.actualReturnDate}">-</c:if>
                                                    </td>
                                                    <td>${borrowing.purpose}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${borrowing.status eq 'pending'}">
                                                                <span class="badge bg-warning">待审批</span>
                                                            </c:when>
                                                            <c:when test="${borrowing.status eq 'approved'}">
                                                                <span class="badge bg-success">已批准</span>
                                                            </c:when>
                                                            <c:when test="${borrowing.status eq 'borrowed'}">
                                                                <c:set var="now" value="<%= new java.util.Date() %>" />
                                                                <c:choose>
                                                                    <c:when test="${borrowing.expectedReturnDate.time < now.time}">
                                                                        <span class="badge bg-danger">已逾期</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-primary">已借出</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:when test="${borrowing.status eq 'returned'}">
                                                                <span class="badge bg-info">已归还</span>
                                                            </c:when>
                                                            <c:when test="${borrowing.status eq 'rejected'}">
                                                                <span class="badge bg-danger">已拒绝</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${borrowing.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group btn-group-sm">
                                                            <c:if test="${borrowing.status eq 'pending'}">
                                                                <a href="${pageContext.request.contextPath}/warehouse/borrowing-approval?id=${borrowing.borrowingId}" class="btn btn-outline-primary">
                                                                    <i class="bi bi-check-square"></i> 审批
                                                                </a>
                                                            </c:if>
                                                            <c:if test="${borrowing.status eq 'borrowed'}">
                                                                <a href="${pageContext.request.contextPath}/warehouse/borrowing-return?id=${borrowing.borrowingId}" class="btn btn-outline-info">
                                                                    <i class="bi bi-arrow-return-left"></i> 归还
                                                                </a>
                                                            </c:if>
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