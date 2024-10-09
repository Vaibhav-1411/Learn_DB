package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Test2345 {
	private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl";
	private static final String username = "system";
	private static final String password = "orcl";

	public static void main(String[] args) {
	        // Database connection parameters
	    	
	        
	        Scanner scanner = new Scanner(System.in);
	        Connection connection = null;

	        try {
	            // Establish database connection
	            connection = DriverManager.getConnection(jdbc_url, username, password);
	            CallableStatement callableStatement=null;

	            // Enable the job
	            callableStatement = connection.prepareCall("{call emp_pkg.manage_job(?)}");
	            callableStatement.setBoolean(1, true);
	            callableStatement.execute();
	            System.out.println("Job for adding employees is enabled.");

	  

	    		System.out.println("Enter choice:\n 1 for Insert.\n 2 for Delete. ");
	    		int ch = scanner.nextInt();

	    		if (ch == 1) {
	    			while (true) {
	    				// Prompt user for action
	    				System.out.println("Enter 0 to stop the job, or 1 to add employee data:");
	    				int choice = scanner.nextInt();

	    				if (choice == 0) {
	    					// Stop the job
	    					callableStatement = connection.prepareCall("{call emp_pkg.manage_job(?)}");
	    					callableStatement.setBoolean(1, false);
	    					callableStatement.execute();
	    					System.out.println("Job has been stopped.");
	    					break; // Exit the loop
	    				} else if (choice == 1) {
	    					// Get employee data from user
	    					System.out.print("Enter employee name: ");
	    					String name = scanner.next();
		                    System.out.print("Enter department: ");
		                    String department = scanner.next();
		                    System.out.print("Enter salary: ");
		                    String salary = scanner.next();
	
		                    // Call the add_emp procedure
		                    callableStatement = connection.prepareCall("{call emp_pkg.add_emp2(?, ?, ?)}");
		                    callableStatement.setString(1, name);
		                    callableStatement.setString(2, department);
		                    callableStatement.setString(3, salary);
		                    callableStatement.execute();
		                    System.out.println("Employee added: " + name);
		                } else {
		                    System.out.println("Invalid choice. Please enter 0 or 1.");
		                }
		            }
	    		}else {
	    			System.out.println("agvchjvcjbcjbc");
	    		}
		      
	        
	        } catch (SQLException e) {
		            e.printStackTrace();
		        } finally {
		            // Clean up resources
		            if (connection != null) {
		                try {
		                    connection.close();
		                } catch (SQLException e) {
		                    e.printStackTrace();
		                }
		            }
		            scanner.close();
		        }
		    }
		}
