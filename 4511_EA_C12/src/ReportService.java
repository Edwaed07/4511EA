package your;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.aib.util.DatabaseConnection;

public class ReportService {

    public List<ConsumptionReport> getConsumptionReports() {
        List<ConsumptionReport> reports = new ArrayList<>();
        // Example query; adjust according to your database schema
        String query = "SELECT branch, season, SUM(quantity) as consumption FROM reserve_records GROUP BY branch, season";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                ConsumptionReport report = new ConsumptionReport();
                report.setShopName(resultSet.getString("branch"));
                report.setSeason(resultSet.getString("season"));
                report.setConsumption(resultSet.getInt("consumption"));
                reports.add(report);
            }
        } catch (SQLException e) {
        }
        return reports;
    }
}