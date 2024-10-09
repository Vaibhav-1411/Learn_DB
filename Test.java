package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Test {
	private static final Logger logger = Logger.getLogger(Test.class.getName());
	private static final String JDBC_URL = "jdbc:oracle:thin:@//localhost:1521/orcl"; // Update as per your DB URL
	private static final String JDBC_USER = "system"; // Update with your DB username
	private static final String JDBC_PASSWORD = "orcl"; // Update with your DB password

	public static void main(String[] args) {
		Connection connection = null; // Connection object to manage the database connection
		Statement stmt = null; // Statement object to execute SQL queries

		try {
			// Establish the database connection
			connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
			logger.info("Database connected successfully."); // Log successful connection

			// Create a Statement object
			stmt = connection.createStatement();

			// SQL query to insert a new record into the STUDENT table
			String insertSql = "INSERT INTO STUDENT (Eid, Ename, Salary) VALUES (107, 'Brown', 24000)";
			String insertSql2 = "INSERT INTO STUDENT (Eid, Ename, Salary) VALUES (108, 'Jenny', 36000)";
			
			// Execute the insert SQL query and log the result
			executeUpdate(stmt, insertSql, "Record inserted successfully.");
			executeUpdate(stmt, insertSql2, "Record inserted successfully.");

			// SQL query to update an existing record in the STUDENT table
			String updateSql = "UPDATE STUDENT SET Salary = 19000 WHERE Eid = 106";
			// Execute the update SQL query and log the result
			executeUpdate(stmt, updateSql, "Record updated successfully.");

			// SQL query to delete a record from the STUDENT table
//			String deleteSql = "DELETE FROM STUDENT WHERE Eid = 104";
			// Execute the delete SQL query and log the result
//			executeUpdate(stmt, deleteSql, "Record deleted successfully.");

		} catch (SQLException e) {
			// Log any SQL exceptions that occur
			logger.severe("Database connection or SQL operation failed: " + e.getMessage());
		} finally {
			// Clean up resources in the finally block to ensure they are closed
			try {
				if (stmt != null)
					stmt.close(); // Close the Statement object
				if (connection != null)
					connection.close(); // Close the Connection object
			} catch (SQLException e) {
				// Log any exceptions that occur while closing resources
				logger.severe("Failed to close resources: " + e.getMessage());
			}
		}
	}

	// Method to execute SQL update statements and log success messages
	private static void executeUpdate(Statement stmt, String sql, String successMessage) {
		try {
			// Execute the SQL update statement
			int rowsAffected = stmt.executeUpdate(sql);
			// Log the success message if rows are affected
			if (rowsAffected > 0) {
				logger.info(successMessage);
			} else {
				// Log if no records were affected
				logger.info("No record affected.");
			}
		} catch (SQLException e) {
			// Log any SQL exceptions that occur during execution
			logger.severe("SQL operation failed: " + e.getMessage());
		}
	}
}