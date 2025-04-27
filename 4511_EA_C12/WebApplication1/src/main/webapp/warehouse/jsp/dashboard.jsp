<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>仓库管理仪表板 - AIB系统</title>
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/warehouse/dashboard">
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
                    <div class="card-header bg-light">
                        <h4 class="mb-0">欢迎回来，${staff.fullName}</h4>
                        <p class="text-muted mb-0">仓库位置：${staff.warehouseLocation}</p>
                    </div>
                    <div class="card-body">
                        <!-- 待处理任务部分 -->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header bg-light">
                                        <h5 class="mb-0">待处理任务</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="table-responsive">
                                            <table class="table table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>任务类型</th>
                                                        <th>详情</th>
                                                        <th>请求日期</th>
                                                        <th>状态</th>
                                                        <th>操作</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td><span class="badge bg-success">预订</span></td>
                                                        <td>苹果 x 50 (面包店A)</td>
                                                        <td>2025-04-10</td>
                                                        <td><span class="badge bg-warning">待审批</span></td>
                                                        <td>
                                                            <button class="btn btn-sm btn-primary"><i class="bi bi-eye"></i> 查看</button>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><span class="badge bg-warning">借用</span></td>
                                                        <td>草莓 x 20 (面包店B -> 面包店C)</td>
                                                        <td>2025-04-11</td>
                                                        <td><span class="badge bg-warning">待审批</span></td>
                                                        <td>
                                                            <button class="btn btn-sm btn-primary"><i class="bi bi-eye"></i> 查看</button>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><span class="badge bg-danger">配送</span></td>
                                                        <td>橙子 x 100 (美国中央仓库 -> 香港中央仓库)</td>
                                                        <td>2025-04-09</td>
                                                        <td><span class="badge bg-info">运输中</span></td>
                                                        <td>
                                                            <button class="btn btn-sm btn-primary"><i class="bi bi-eye"></i> 查看</button>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
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