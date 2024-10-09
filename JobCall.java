package oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Logger;

public class JobCall {

	private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl";
	private static final String username = "system";
	private static final String password = "orcl";

	private static final Logger logger = Logger.getLogger(JobCall.class.getName());

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Connection con = null;
		CallableStatement callstmt = null;

		try {
			con = DriverManager.getConnection(jdbc_url, username, password);
			logger.info("Database connected successfully.");

			System.out.println("Enter choice:\n 1 for Insert.\n 2 for Delete.\n 3 for View By ID.");
			int choice = sc.nextInt();
			sc.nextLine();
			boolean bl = true;

			if (choice == 1) {

				while (bl == true) {
					System.out.println("Enter 1 to Add an employee or 0 to stop:");
					int ch = sc.nextInt();
					sc.nextLine();

					if (ch == 0) {

						String manageJobSQL = "{CALL emp_pkg.manage_add_job(?)}";
						callstmt = con.prepareCall(manageJobSQL);
						callstmt.setInt(1, 0);
						callstmt.execute();
						System.out.println("ADD EMPLOYEE JOB Stopped....");
						bl = false;
						break;
					} else if (ch != 1) {
						System.out.println("Invalid choice. Please enter 1 to add or 0 to stop.");
						continue;
					} else {

						String manageJobSQL = "{CALL emp_pkg.manage_add_job(?)}";
						callstmt = con.prepareCall(manageJobSQL);
						callstmt.setInt(1, 1);
						callstmt.execute();
						System.out.println("ADD EMPLOYEE JOB Start....");
					}

					System.out.println("Enter employee name:");
					String name = sc.nextLine();

					System.out.println("Enter department:");
					String department = sc.nextLine();

					System.out.println("Enter salary:");
					String salary = sc.nextLine();

					String sql = "BEGIN emp_pkg.add_emp(?, ?, ?); END;";
					callstmt = con.prepareCall(sql);

					callstmt.setString(1, name);
					callstmt.setString(2, department);
					callstmt.setString(3, salary);

					callstmt.execute();
					
					System.out.println("Employee added and job enabled.");
					bl = true;

				}

			} else if (choice == 2) {

				while (true) {
					System.out.println("Enter 1 to Delete an employee or 0 to stop:");
					int ch = sc.nextInt();
					sc.nextLine();
					if (ch == 0) {
						String manageJobSQL = "{CALL emp_pkg.manage_del_job(?)}";
						callstmt = con.prepareCall(manageJobSQL);
						callstmt.setInt(1, 0);
						callstmt.execute();
						System.out.println("Delete Employee JOB Stopped....");
						break;
					} else if (ch != 1) {
						System.out.println("Invalid choice. Please enter 1 to add or 0 to stop.");
						continue;
					} else {
						String manageJobSQL = "{CALL emp_pkg.manage_del_job(?)}";
						callstmt = con.prepareCall(manageJobSQL);
						callstmt.setInt(1, 1);
						callstmt.execute();
						System.out.println("Delete Employee JOB Start....");
					}
					System.out.println("Enter ID to delete: ");
					int delId = sc.nextInt();
					sc.nextLine();

					String deletesql = "{CALL emp_pkg.del_emp(?)}";
					callstmt = con.prepareCall(deletesql);
					callstmt.setInt(1, delId);
					callstmt.execute();
					
					System.out.println("ID: " + delId + " deleted");
					bl = true;
				}
			}
//			else if(choice == 3){
//				String viewquery = "{CALL emp_pkg.view_emp(?)}";
//				callstmt = con.prepareCall(viewquery);
//				callstmt.setInt(1, 9);
//				ResultSet rs=callstmt.executeQuery();
//				while(rs.next()) {
//					System.out.println("EId: "+rs.getInt("eid"));
//					System.out.println("Name: "+rs.getString("name"));
//					System.out.println("Department: "+rs.getString("department"));
//					System.out.println("Salary: "+rs.getString("salary"));
//				}
//				}
			else if (choice == 3) {

				System.out.println("Enter Id:");
				int num = sc.nextInt();
				sc.nextLine();

				String sql = "Select eid,ename,department,salary from employee2 WHERE eid = " + num; //for display specific ID data
//				String sql ="SELECT * FROM employee2 ORDER BY eid";//for display all data of table
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					int ID = rs.getInt("eid");
					String name = rs.getString("ename");
					String department = rs.getString("department");
					String salary = rs.getString("salary");

					System.out.println("Id is: " + ID);
					System.out.println("name is: " + name);
					System.out.println("Department is: " + department);
					System.out.println("Salary is: " + salary);
					System.out.println("--------------------------------");
				} 

			} else {
				System.out.println("Invalid Input.");
			}

			
			
		} catch (SQLException e) {
			logger.info("SQL error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (callstmt != null)
					callstmt.close();
				if (con != null)
					con.close();
				sc.close();
			} catch (SQLException e) {
				logger.warning("Error closing resources: " + e.getMessage());
			}
		}
	}
}