package controller;

import db.UserDAO;
import model.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "WarehouseLoginServlet", urlPatterns = {"/warehouse/login"})
public class WarehouseLoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        
        if ("login".equals(action)) {
            // 处理登录
            handleLogin(request, response);
        } else if ("logout".equals(action)) {
            // 处理登出
            handleLogout(request, response);
        } else {
            // 显示登录页面
            request.getRequestDispatcher("/warehouse/login.jsp").forward(request, response);
        }
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String errorMsg = null;
        
        if (username == null || username.trim().isEmpty()) {
            errorMsg = "用户名不能为空！";
        } else if (password == null || password.trim().isEmpty()) {
            errorMsg = "密码不能为空！";
        }
        
        if (errorMsg != null) {
            request.setAttribute("errorMsg", errorMsg);
            request.getRequestDispatcher("/warehouse/login.jsp").forward(request, response);
            return;
        }
        
        try {
            User user = UserDAO.validateUser(username, password);
            
            if (user != null && user.isActive()) {
                if ("warehouse".equals(user.getRole())) {
                    // 登录成功，设置会话并重定向到仪表板
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    
                    response.sendRedirect(request.getContextPath() + "/warehouse/dashboard");
                } else {
                    errorMsg = "您没有仓库员工权限！";
                    request.setAttribute("errorMsg", errorMsg);
                    request.getRequestDispatcher("/warehouse/login.jsp").forward(request, response);
                }
            } else {
                errorMsg = "用户名或密码错误！";
                request.setAttribute("errorMsg", errorMsg);
                request.getRequestDispatcher("/warehouse/login.jsp").forward(request, response);
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            errorMsg = "数据库错误：" + e.getMessage();
            request.setAttribute("errorMsg", errorMsg);
            request.getRequestDispatcher("/warehouse/login.jsp").forward(request, response);
        }
    }
    
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 清除会话
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // 重定向到登录页面
        response.sendRedirect(request.getContextPath() + "/warehouse/login");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Warehouse Login Servlet";
    }
} 