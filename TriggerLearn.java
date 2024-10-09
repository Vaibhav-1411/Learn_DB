package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TriggerLearn {
	private static final Logger logger = Logger.getLogger(TriggerLearn.class.getName());

	// Oracle DB URL, username, and password
	private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/orcl"; // Update accordingly
	private static final String USER = "system";
	private static final String PASS = "orcl";

	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			// Load the Oracle JDBC driver
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// Connect to the Oracle database
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			logger.info("Connected to Oracle database successfully!");

			// Insert into the department table (which triggers an insert into the employee
			// table)
			String sql = "INSERT INTO department (dept_id, dept_name) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 101); // dept_id
			pstmt.setString(2, "HR"); // dept_name

			// Execute the insert, which triggers the employee insertion
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				logger.info("A new department was inserted successfully, and the trigger was fired!");
			}

		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Oracle JDBC Driver not found", e);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "SQL Exception occurred", e);
		} 

	}

}
