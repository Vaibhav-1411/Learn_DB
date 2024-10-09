package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class DemoLearn {

	private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl";
	private static final String username = "system";
	private static final String password = "orcl";

	private static final Logger logger = Logger.getLogger(DemoLearn.class.getName());

	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement deleteStmt = null;

		try {
			// Establish the database connection
			con = DriverManager.getConnection(jdbc_url, username, password);
			logger.info("Database connected successfully.");

			System.out.println("Choose an operation:\n" + "1 forINSERT\n" + "2 for UPDATE\n" + "3 for DELETE ");

			Scanner sc = new Scanner(System.in);
			int value = sc.nextInt();

			switch (value) {
			case 1:

				// INSERT //
				String insertquery = "INSERT INTO employee4(Eid, Ename, Salary, Department) VALUES(?,?,?,?)";
				stmt = con.prepareStatement(insertquery);
				stmt.setInt(1, 8);
				stmt.setString(2, "Gaurav");
				stmt.setInt(3, 16000);
				stmt.setString(4, "Intern");
				int rowAdd = stmt.executeUpdate();
				System.out.println("Insert operation performed successfully.");

				if (rowAdd > 0) {
					logger.info("Data updated successfully, and the trigger was fired!");
				} else {
					logger.info("No record affected.");
				}

				break;

			case 2:
				// UPDATE //
				String updateSql = "UPDATE employee4 SET Salary = ? WHERE Eid = ?";
				updateStmt = con.prepareStatement(updateSql);
				updateStmt.setInt(1, 15000);
				updateStmt.setInt(2, 3);
				int rowsUpdated = updateStmt.executeUpdate();
				System.out.println("UPDATE operation performed successfully.");
				if (rowsUpdated > 0) {
					logger.info("Data updated successfully, and the trigger was fired!");
				} else {
					logger.info("No record affected.");
				}

				break;

			case 3:

				// DELETE //
				String deleteSql = "DELETE FROM employee4 WHERE Eid = ?";
				deleteStmt = con.prepareStatement(deleteSql);
				deleteStmt.setInt(1, 2);
				int rowsDeleted = deleteStmt.executeUpdate();
				System.out.println("ID deleted.");
				if (rowsDeleted > 0) {
					logger.info("ID deleted successfully, and the trigger was fired!");
				} else {
					logger.info("No record affected.");
				}
				break;

			default:
				System.out.println("Enter valid input for INSERT, UPDATE, or DELETE.");
			}

			sc.close();

		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (deleteStmt != null) {
					deleteStmt.close();
				}
				if (updateStmt != null) {
					updateStmt.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
