package com.aib.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FruitServlet")
public class FruitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 處理 GET 請求，例如顯示庫存或預訂記錄
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 處理 POST 請求，例如更新庫存或提交預訂
        // 這裡之後會加入 MySQL 連接代碼
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}