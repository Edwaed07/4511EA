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

@WebServlet(name = "WarehouseRegisterServlet", urlPatterns = {"/warehouse/register"})
public class WarehouseRegisterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        
        if ("register".equals(action)) {
            // 处理注册
            registerWarehouseStaff(request, response);
        } else {
            // 显示注册页面
            request.getRequestDispatcher("/warehouse/register.jsp").forward(request, response);
        }
    }
    
    private void registerWarehouseStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String location = request.getParameter("location");
        String errorMsg = null;
        
        // 验证输入
        if (username == null || username.trim().isEmpty()) {
            errorMsg = "用户名不能为空！";
        } else if (password == null || password.trim().isEmpty()) {
            errorMsg = "密码不能为空！";
        } else if (!password.equals(confirmPassword)) {
            errorMsg = "两次密码输入不一致！";
        } else if (location == null || location.trim().isEmpty()) {
            errorMsg = "仓库位置不能为空！";
        }
        
        if (errorMsg != null) {
            request.setAttribute("errorMsg", errorMsg);
            request.getRequestDispatcher("/warehouse/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("warehouse");
            user.setLocation(location);
            user.setActive(true);
            
            int userId = UserDAO.addUser(user);
            
            if (userId > 0) {
                // 注册成功，设置会话并重定向到仪表板
                user.setId(userId);
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                response.sendRedirect(request.getContextPath() + "/warehouse/dashboard");
            } else {
                errorMsg = "注册失败，请重试！";
                request.setAttribute("errorMsg", errorMsg);
                request.getRequestDispatcher("/warehouse/register.jsp").forward(request, response);
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            errorMsg = "数据库错误：" + e.getMessage();
            request.setAttribute("errorMsg", errorMsg);
            request.getRequestDispatcher("/warehouse/register.jsp").forward(request, response);
        }
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
        return "Warehouse Staff Registration Servlet";
    }
} 