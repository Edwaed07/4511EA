import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    // 数据库URL、用户名和密码
    private static final String URL = "jdbc:mysql://localhost:3306/aib_bakery?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "passw0rd"; // 确保这是你MySQL的实际密码
    
    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        try {
            // 使用Properties对象设置连接属性
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("useSSL", "false");
            
            // 注册JDBC驱动并建立连接
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, props);
        } catch (ClassNotFoundException e) {
            throw new SQLException("找不到MySQL JDBC驱动", e);
        }
    }
    
    // 关闭连接
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("关闭数据库连接时出错: " + e.getMessage());
            }
        }
    }
}