
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TestDatabaseServlet", urlPatterns = {"/test-database"})
public class TestDatabaseServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>数据库连接测试</title>");            
            out.println("</head>");
            out.println("<body>");
            
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                out.println("<h2>数据库连接成功!</h2>");
                
                // 测试查询
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM fruits LIMIT 3");
                
                out.println("<h3>水果表数据:</h3>");
                out.println("<table border='1'>");
                out.println("<tr><th>ID</th><th>名称</th><th>产地</th></tr>");
                
                while(rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("id") + "</td>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("source_country") + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
                
            } catch (Exception e) {
                out.println("<h2>数据库连接错误:</h2>");
                out.println("<p>" + e.getMessage() + "</p>");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
            
            out.println("</body>");
            out.println("</html>");
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
}