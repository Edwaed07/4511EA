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
    <!-- 数据表格样式 -->
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
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
        <!-- 状态消息 -->
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- 借用列表标题和添加按钮 -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="bi bi-arrow-left-right"></i> 借用管理</h2>
            <a href="${pageContext.request.contextPath}/warehouse/borrowing-form" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> 新增借用申请
            </a>
        </div>

        <!-- 筛选选项 -->
        <div class="card mb-4">
            <div class="card-header bg-light">
                <h5 class="mb-0"><i class="bi bi-funnel"></i> 筛选选项</h5>
            </div>
            <div class="card-body">
                <form id="filterForm" method="get" class="row g-3">
                    <!-- 状态筛选 -->
                    <div class="col-md-3">
                        <label for="status" class="form-label">状态</label>
                        <select class="form-select" id="status" name="status">
                            <option value="" ${empty param.status ? 'selected' : ''}>全部</option>
                            <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>待审批</option>
                            <option value="APPROVED" ${param.status == 'APPROVED' ? 'selected' : ''}>已批准</option>
                            <option value="REJECTED" ${param.status == 'REJECTED' ? 'selected' : ''}>已拒绝</option>
                            <option value="BORROWED" ${param.status == 'BORROWED' ? 'selected' : ''}>已借出</option>
                            <option value="RETURNED" ${param.status == 'RETURNED' ? 'selected' : ''}>已归还</option>
                            <option value="OVERDUE" ${param.status == 'OVERDUE' ? 'selected' : ''}>已逾期</option>
                        </select>
                    </div>
                    
                    <!-- 仓库筛选 -->
                    <div class="col-md-3">
                        <label for="warehouseId" class="form-label">仓库</label>
                        <select class="form-select" id="warehouseId" name="warehouseId">
                            <option value="" ${empty param.warehouseId ? 'selected' : ''}>全部</option>
                            <c:forEach items="${warehouses}" var="warehouse">
                                <option value="${warehouse.warehouseId}" ${param.warehouseId == warehouse.warehouseId ? 'selected' : ''}>
                                    ${warehouse.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- 日期范围筛选 -->
                    <div class="col-md-3">
                        <label for="startDate" class="form-label">开始日期</label>
                        <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}">
                    </div>
                    <div class="col-md-3">
                        <label for="endDate" class="form-label">结束日期</label>
                        <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}">
                    </div>
                    
                    <!-- 筛选按钮 -->
                    <div class="col-12 d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="bi bi-search"></i> 筛选
                        </button>
                        <button type="button" id="resetFilter" class="btn btn-secondary">
                            <i class="bi bi-arrow-counterclockwise"></i> 重置
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- 借用列表 -->
        <div class="card">
            <div class="card-header bg-light">
                <h5 class="mb-0"><i class="bi bi-list-ul"></i> 借用记录列表</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table id="borrowingTable" class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>借用ID</th>
                                <th>水果名称</th>
                                <th>仓库</th>
                                <th>借用人</th>
                                <th>借用数量</th>
                                <th>借用日期</th>
                                <th>预期归还日期</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${borrowings}" var="borrowing">
                                <tr class="${borrowing.status == 'OVERDUE' ? 'table-danger' : (borrowing.status == 'PENDING' ? 'table-warning' : '')}">
                                    <td>${borrowing.borrowingId}</td>
                                    <td>${borrowing.fruitName}</td>
                                    <td>${borrowing.warehouseLocation}</td>
                                    <td>${borrowing.borrowerName}</td>
                                    <td>${borrowing.quantity}</td>
                                    <td><fmt:formatDate value="${borrowing.borrowDate}" pattern="yyyy-MM-dd" /></td>
                                    <td><fmt:formatDate value="${borrowing.expectedReturnDate}" pattern="yyyy-MM-dd" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${borrowing.status == 'PENDING'}">
                                                <span class="badge bg-warning text-dark">待审批</span>
                                            </c:when>
                                            <c:when test="${borrowing.status == 'APPROVED'}">
                                                <span class="badge bg-success">已批准</span>
                                            </c:when>
                                            <c:when test="${borrowing.status == 'REJECTED'}">
                                                <span class="badge bg-danger">已拒绝</span>
                                            </c:when>
                                            <c:when test="${borrowing.status == 'BORROWED'}">
                                                <span class="badge bg-primary">已借出</span>
                                            </c:when>
                                            <c:when test="${borrowing.status == 'RETURNED'}">
                                                <span class="badge bg-info">已归还</span>
                                            </c:when>
                                            <c:when test="${borrowing.status == 'OVERDUE'}">
                                                <span class="badge bg-danger">已逾期</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="btn-group btn-group-sm" role="group">
                                            <a href="${pageContext.request.contextPath}/warehouse/borrowing-details?id=${borrowing.borrowingId}" 
                                               class="btn btn-outline-primary" title="查看详情">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <c:if test="${borrowing.status == 'PENDING' && (staff.role == 'ADMIN' || staff.role == 'MANAGER')}">
                                                <a href="${pageContext.request.contextPath}/warehouse/borrowing-approval?id=${borrowing.borrowingId}" 
                                                   class="btn btn-outline-success" title="审批">
                                                    <i class="bi bi-check-circle"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${borrowing.status == 'APPROVED' && (staff.role == 'ADMIN' || staff.role == 'WAREHOUSE_KEEPER')}">
                                                <a href="${pageContext.request.contextPath}/warehouse/borrowing-handover?id=${borrowing.borrowingId}" 
                                                   class="btn btn-outline-info" title="交付">
                                                    <i class="bi bi-box-arrow-right"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${borrowing.status == 'BORROWED' && (staff.role == 'ADMIN' || staff.role == 'WAREHOUSE_KEEPER')}">
                                                <a href="${pageContext.request.contextPath}/warehouse/borrowing-return?id=${borrowing.borrowingId}" 
                                                   class="btn btn-outline-warning" title="归还">
                                                    <i class="bi bi-box-arrow-in-left"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${borrowing.status == 'PENDING' && (staff.staffId == borrowing.borrowerId || staff.role == 'ADMIN')}">
                                                <button type="button" class="btn btn-outline-danger" 
                                                        onclick="confirmCancel(${borrowing.borrowingId})" title="取消">
                                                    <i class="bi bi-x-circle"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty borrowings}">
                                <tr>
                                    <td colspan="9" class="text-center">没有找到借用记录</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- 取消确认模态框 -->
    <div class="modal fade" id="cancelModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">确认取消</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>您确定要取消此借用申请吗？此操作无法撤销。</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                    <form id="cancelForm" action="${pageContext.request.contextPath}/warehouse/borrowing-cancel" method="post">
                        <input type="hidden" id="cancelBorrowingId" name="borrowingId" value="">
                        <button type="submit" class="btn btn-danger">确认取消</button>
                    </form>
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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    <script>
        // 初始化数据表格
        $(document).ready(function() {
            $('#borrowingTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.11.5/i18n/zh.json"
                },
                "pageLength": 10,
                "order": [[5, "desc"]] // 默认按借用日期降序排序
            });
            
            // 重置筛选按钮
            $('#resetFilter').click(function() {
                $('#status').val('');
                $('#warehouseId').val('');
                $('#startDate').val('');
                $('#endDate').val('');
                $('#filterForm').submit();
            });
        });
        
        // 取消借用申请确认
        function confirmCancel(borrowingId) {
            document.getElementById('cancelBorrowingId').value = borrowingId;
            var cancelModal = new bootstrap.Modal(document.getElementById('cancelModal'));
            cancelModal.show();
        }
    </script>
</body>
</html> 