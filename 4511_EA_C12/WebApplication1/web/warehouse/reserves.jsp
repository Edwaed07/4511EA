<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Fruit, model.Reserve, java.util.List, java.util.Map, java.util.HashMap" %>
<%@ include file="includes/header.jsp" %>

<div class="container">
    <h1 class="mb-4">Reservation Management</h1>
    
    <div class="card mb-4">
        <div class="card-header bg-warning">
            <h5 class="card-title mb-0">Pending Reservations</h5>
        </div>
        <div class="card-body">
            <% 
                List<Reserve> pendingReserves = (List<Reserve>) request.getAttribute("pendingReserves");
                List<Fruit> fruits = (List<Fruit>) request.getAttribute("fruits");
                
                // Create fruit ID to name mapping
                Map<Integer, String> fruitMap = new HashMap<Integer, String>();
                if (fruits != null) {
                    for (Fruit fruit : fruits) {
                        fruitMap.put(fruit.getId(), fruit.getName());
                    }
                }
                
                if (pendingReserves != null && !pendingReserves.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fruit</th>
                            <th>Quantity</th>
                            <th>Shop</th>
                            <th>City</th>
                            <th>Reservation Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reserve reserve : pendingReserves) { 
                           String fruitName = fruitMap.containsKey(reserve.getFruitId()) ? fruitMap.get(reserve.getFruitId()) : "Unknown";
                        %>
                        <tr>
                            <td><%= reserve.getId() %></td>
                            <td><%= fruitName %></td>
                            <td><%= reserve.getQuantity() %></td>
                            <td><%= reserve.getShopName() %></td>
                            <td><%= reserve.getCity() %></td>
                            <td><%= reserve.getReserveDate() %></td>
                            <td>
                                <span class="badge bg-<%= reserve.getStatus().equals("pending") ? "warning" : 
                                                       (reserve.getStatus().equals("approved") ? "success" : 
                                                       (reserve.getStatus().equals("in-transit") ? "info" : "secondary")) %>">
                                    <%= reserve.getStatus() %>
                                </span>
                            </td>
                            <td>
                                <% if (reserve.getStatus().equals("pending")) { %>
                                <form action="${pageContext.request.contextPath}/warehouse/reserves" method="post" style="display: inline">
                                    <input type="hidden" name="action" value="approve">
                                    <input type="hidden" name="reserveId" value="<%= reserve.getId() %>">
                                    <button type="submit" class="btn btn-sm btn-success">Approve</button>
                                </form>
                                <% } else if (reserve.getStatus().equals("approved")) { %>
                                <form action="${pageContext.request.contextPath}/warehouse/reserves" method="post" style="display: inline">
                                    <input type="hidden" name="action" value="deliver">
                                    <input type="hidden" name="reserveId" value="<%= reserve.getId() %>">
                                    <button type="submit" class="btn btn-sm btn-primary">Arrange Delivery</button>
                                </form>
                                <% } else { %>
                                <button type="button" class="btn btn-sm btn-secondary" disabled>Processed</button>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p class="text-muted">No pending reservations.</p>
            <% } %>
        </div>
    </div>
    
    <div class="card">
        <div class="card-header bg-info text-white">
            <h5 class="card-title mb-0">Reservation Statistics</h5>
        </div>
        <div class="card-body">
            <% if (fruits != null && !fruits.isEmpty()) { %>
            <div class="row">
                <% for (Fruit fruit : fruits) { %>
                <div class="col-md-4 mb-3">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><%= fruit.getName() %></h5>
                            <p class="card-text">
                                Pending: <span id="pending-<%= fruit.getId() %>">
                                <% 
                                    int pendingCount = 0;
                                    if (pendingReserves != null) {
                                        for (Reserve r : pendingReserves) {
                                            if (r.getFruitId() == fruit.getId() && r.getStatus().equals("pending")) {
                                                pendingCount += r.getQuantity();
                                            }
                                        }
                                    }
                                    out.print(pendingCount);
                                %>
                                </span>
                                <br>
                                Approved: <span id="approved-<%= fruit.getId() %>">
                                <% 
                                    int approvedCount = 0;
                                    if (pendingReserves != null) {
                                        for (Reserve r : pendingReserves) {
                                            if (r.getFruitId() == fruit.getId() && r.getStatus().equals("approved")) {
                                                approvedCount += r.getQuantity();
                                            }
                                        }
                                    }
                                    out.print(approvedCount);
                                %>
                                </span>
                            </p>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <% } else { %>
            <p class="text-muted">No fruit data available.</p>
            <% } %>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %> 