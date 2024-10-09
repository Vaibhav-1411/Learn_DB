package oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class HistoryTable {
	private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl";
	private static final String username = "system";
	private static final String password = "orcl";

	private static final Logger logger = Logger.getLogger(DemoTest.class.getName());

	public static void main(String[] args)  {
		Connection con = null;
		CallableStatement callstmt = null;

		try {
			con = DriverManager.getConnection(jdbc_url, username, password);
		
		logger.info("Database connected successfully.");

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter choice:\n 1 for Insert.\n 2 for Delete. ");
		int choice = sc.nextInt();

		if (choice == 1) {

			System.out.println("Enter ID to add: ");
			int eid = sc.nextInt();
			sc.nextLine();

			System.out.println("Enter Name: ");
			String name = sc.nextLine();

			System.out.println("Enter Department: ");
			String department = sc.nextLine();

			System.out.println("Enter Salary: ");
			int salary = sc.nextInt();
			sc.nextLine();

			String insertsql = "{call insert_emp(?,?,?,?)}";
			callstmt = con.prepareCall(insertsql);

			callstmt.setInt(1, eid);
			callstmt.setString(2, name);
			callstmt.setString(3, department);
			callstmt.setInt(4, salary);

			boolean row = callstmt.execute();
			System.out.println("Employee added.");

			if (row!=true) {
				logger.info("A new employee was inserted successfully, and the trigger was fired!");
			} else {
				logger.info("No record affected.");
			}
			
			
			
		} else if (choice == 2) {
			
			System.out.println("Enter ID to delete: ");
			int delId=sc.nextInt();
			
			String deletesql = "{call delete_emp(?)}";
			callstmt = con.prepareCall(deletesql);
			
			callstmt.setInt(1, delId);
			
			boolean row = callstmt.execute();
			System.out.println("Employee Deleted.");

			if (row!=true) {
				logger.info("Employee deleted successfully, and the trigger was fired!");
			} else {
				logger.info("No record affected.");
			}			

		} else {
			System.out.println("Enter Valid Input.");
		}

		sc.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

}
