package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class DemoTest {

	private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl";
	private static final String username = "system";
	private static final String password = "orcl";

	private static final Logger logger = Logger.getLogger(DemoTest.class.getName());

	public static void main(String[] args) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement deleteStmt = null;

		try {
			// Establishing a connection to the database
			con = DriverManager.getConnection(jdbc_url, username, password);
			logger.info("Database connected successfully.");
			
			Scanner sc = new Scanner(System.in);

			

			boolean run = true;

			while (run==true) {
				System.out.println("Choose an operation:\n" + "1 forINSERT\n" + "2 for UPDATE\n" + "3 for DELETE "+ "4 for CLOSE");
				int value = sc.nextInt();

				switch (value) {

				// INSERT //
				case 1:

					String insertquery = "INSERT INTO employee4(Eid, Ename, Salary, Department) VALUES(?, ?, ?, ?)";
					stmt = con.prepareStatement(insertquery);

					System.out.println("Enter ID: ");
					int eid = sc.nextInt();
					sc.nextLine();
					System.out.println("Enter Name: ");
					String Ename = sc.nextLine();
					System.out.println("Enter Salary: ");
					int salary = sc.nextInt();
					sc.nextLine();
					System.out.println("Enter Department: ");
					String dept = sc.nextLine();

					stmt.setInt(1, eid);
					stmt.setString(2, Ename);
					stmt.setInt(3, salary);
					stmt.setString(4, dept);

					int row = stmt.executeUpdate();
					System.out.println("Insert operation performed successfully.");

					if (row > 0) {
						logger.info("A new department was inserted successfully, and the trigger was fired!");
					} else {
						logger.info("No record affected.");
					}
					break;

				// UPDATE //
				case 2:

					System.out.println("Enter New Salary: ");
					int newSalary = sc.nextInt();
					System.out.println("Enter Id to be updated: ");
					eid = sc.nextInt();
					String updateSql = "UPDATE employee4 SET Salary = ? WHERE Eid = ?";
					updateStmt = con.prepareStatement(updateSql);
					updateStmt.setInt(1, newSalary);
					updateStmt.setInt(2, eid);

					int rowsUpdated = updateStmt.executeUpdate();
					System.out.println("UPDATE operation performed successfully.");
					if (rowsUpdated > 0) {
						logger.info("Data updated successfully, and the trigger was fired!");
					} else {
						logger.info("No record affected.");
					}
					break;

				// DELETE //
				case 3:

					String deleteSql = "DELETE FROM employee4 WHERE Eid = ?";
					deleteStmt = con.prepareStatement(deleteSql);
					System.out.println("Enter ID to delete: ");
					eid = sc.nextInt();
					deleteStmt.setInt(1, eid);

					int rowsDeleted = deleteStmt.executeUpdate();
					System.out.println("ID deleted.");
					if (rowsDeleted > 0) {
						logger.info("ID deleted successfully, and the trigger was fired!");
					} else {
						logger.info("No record affected.");
					}
					break;
					
					
				case 4:
					run=false;
					System.out.println("Terminated Program");
					break;
					

				default:
					System.out.println("Enter valid input for INSERT, UPDATE, or DELETE.");
					run=true;

				}
			}//while close

			sc.close();

		}//try close

	

		
		
		catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (deleteStmt != null)
					deleteStmt.close();
				if (updateStmt != null)
					updateStmt.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
