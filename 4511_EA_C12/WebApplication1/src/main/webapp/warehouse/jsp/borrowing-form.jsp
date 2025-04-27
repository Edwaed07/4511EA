<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>申请借用水果 - AIB系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <link href="${pageContext.request.contextPath}/warehouse/css/style.css" rel="stylesheet">
    <!-- 日期选择器 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
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
        
        <!-- 成功消息提示 -->
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="bi bi-plus-circle"></i> 申请借用水果</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/warehouse/borrowing-form" method="post">
                            <!-- 仓库选择 -->
                            <div class="mb-3">
                                <label for="warehouseId" class="form-label">选择仓库</label>
                                <select class="form-select" id="warehouseId" name="warehouseId" required>
                                    <option value="">-- 请选择仓库 --</option>
                                    <c:forEach items="${warehouses}" var="warehouse">
                                        <option value="${warehouse.warehouseId}" ${selectedWarehouseId == warehouse.warehouseId ? 'selected' : ''}>
                                            ${warehouse.name} (${warehouse.location})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <!-- 水果选择 -->
                            <div class="mb-3">
                                <label for="fruitId" class="form-label">选择水果</label>
                                <select class="form-select" id="fruitId" name="fruitId" required ${empty selectedWarehouseId ? 'disabled' : ''}>
                                    <option value="">-- 请先选择仓库 --</option>
                                    <c:if test="${not empty fruits}">
                                        <c:forEach items="${fruits}" var="fruit">
                                            <option value="${fruit.fruitId}" data-stock="${fruit.availableQuantity}">
                                                ${fruit.name} (库存: ${fruit.availableQuantity})
                                            </option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                                <div id="currentStock" class="form-text text-info d-none">
                                    当前库存: <span id="stockValue">0</span>
                                </div>
                            </div>
                            
                            <!-- 借用数量 -->
                            <div class="mb-3">
                                <label for="quantity" class="form-label">借用数量</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" min="1" required>
                                <div id="quantityFeedback" class="invalid-feedback">
                                    借用数量不能超过可用库存
                                </div>
                            </div>
                            
                            <!-- 预期归还日期 -->
                            <div class="mb-3">
                                <label for="expectedReturnDate" class="form-label">预期归还日期</label>
                                <input type="text" class="form-control datepicker" id="expectedReturnDate" name="expectedReturnDate" required>
                                <div class="form-text">请选择预期归还日期（不能早于今天）</div>
                            </div>
                            
                            <!-- 借用目的 -->
                            <div class="mb-3">
                                <label for="purpose" class="form-label">借用目的</label>
                                <input type="text" class="form-control" id="purpose" name="purpose" required>
                            </div>
                            
                            <!-- 备注 -->
                            <div class="mb-3">
                                <label for="remarks" class="form-label">备注（可选）</label>
                                <textarea class="form-control" id="remarks" name="remarks" rows="3"></textarea>
                            </div>
                            
                            <!-- 提交按钮 -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/warehouse/borrowings" class="btn btn-secondary me-md-2">
                                    <i class="bi bi-x-circle"></i> 取消
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> 提交申请
                                </button>
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
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script>
        // 初始化日期选择器
        flatpickr(".datepicker", {
            minDate: "today",
            dateFormat: "Y-m-d"
        });
        
        // 仓库选择变更事件
        document.getElementById('warehouseId').addEventListener('change', function() {
            const warehouseId = this.value;
            if (warehouseId) {
                // 当选择仓库后，触发AJAX请求获取该仓库的水果列表
                fetch('${pageContext.request.contextPath}/warehouse/api/fruits?warehouseId=' + warehouseId)
                    .then(response => response.json())
                    .then(data => {
                        const fruitSelect = document.getElementById('fruitId');
                        fruitSelect.innerHTML = '<option value="">-- 请选择水果 --</option>';
                        
                        data.forEach(fruit => {
                            const option = document.createElement('option');
                            option.value = fruit.fruitId;
                            option.setAttribute('data-stock', fruit.availableQuantity);
                            option.textContent = `${fruit.name} (库存: ${fruit.availableQuantity})`;
                            fruitSelect.appendChild(option);
                        });
                        
                        fruitSelect.disabled = false;
                    })
                    .catch(error => console.error('获取水果列表失败:', error));
            } else {
                // 如果没有选择仓库，禁用水果选择
                const fruitSelect = document.getElementById('fruitId');
                fruitSelect.innerHTML = '<option value="">-- 请先选择仓库 --</option>';
                fruitSelect.disabled = true;
                document.getElementById('currentStock').classList.add('d-none');
            }
        });
        
        // 水果选择变更事件
        document.getElementById('fruitId').addEventListener('change', function() {
            const option = this.options[this.selectedIndex];
            if (option && option.value) {
                const stock = option.getAttribute('data-stock');
                document.getElementById('stockValue').textContent = stock;
                document.getElementById('currentStock').classList.remove('d-none');
                
                // 设置借用数量上限
                document.getElementById('quantity').max = stock;
            } else {
                document.getElementById('currentStock').classList.add('d-none');
            }
        });
        
        // 验证借用数量
        document.getElementById('quantity').addEventListener('input', function() {
            const fruitSelect = document.getElementById('fruitId');
            const option = fruitSelect.options[fruitSelect.selectedIndex];
            
            if (option && option.value) {
                const stock = parseInt(option.getAttribute('data-stock'));
                const quantity = parseInt(this.value);
                
                if (quantity > stock) {
                    this.classList.add('is-invalid');
                    document.querySelector('button[type="submit"]').disabled = true;
                } else {
                    this.classList.remove('is-invalid');
                    document.querySelector('button[type="submit"]').disabled = false;
                }
            }
        });
    </script>
</body>
</html> 