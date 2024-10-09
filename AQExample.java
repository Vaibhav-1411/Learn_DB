package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class AQExample {

	    public static void enqueueMessage(String message) {
	        Connection connection = null;
	        CallableStatement callStmt = null;

	        try {
	            // Establish connection
	            connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "system", "orcl");
	            connection.setAutoCommit(false);

	            // Enqueue message directly
	            callStmt = connection.prepareCall("BEGIN DBMS_AQ.ENQUEUE(queue_name => 'queue_test', "
	                    + "enqueue_options => DBMS_AQ.ENQ_OPTIONS_T(), "
	                    + "message_properties => DBMS_AQ.MESSAGE_PROPERTIES_T(), "
	                    + "payload => ?, "
	                    + "msgid => ?);"
	                    + " END;");
	            callStmt.setString(1, message); // Set the message
	            callStmt.registerOutParameter(2, java.sql.Types.VARCHAR); // Register output for message ID

	            callStmt.execute();
	            connection.commit();
	            System.out.println("Message enqueued successfully.");

	        } catch (SQLException e) {
	            e.printStackTrace();
	            if (connection != null) {
	                try {
	                    connection.rollback();
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                }
	            }
	        } finally {
	            try {
	                if (callStmt != null) callStmt.close();
	                if (connection != null) connection.close();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

//	    public static void dequeueMessage() {
//	        Connection connection = null;
//	        CallableStatement callStmt = null;
//
//	        try {
//	            // Establish connection
//	            connection = DriverManager.getConnection("jdbc:oracle:thin:@//host:port/service", "username", "password");
//	            connection.setAutoCommit(false);
//
//	            // Dequeue message directly
//	            callStmt = connection.prepareCall("BEGIN DBMS_AQ.DEQUEUE(queue_name => 'my_queue', "
//	                    + "dequeue_options => DBMS_AQ.DEQ_OPTIONS_T(), "
//	                    + "message_properties => DBMS_AQ.MESSAGE_PROPERTIES_T(), "
//	                    + "payload => ?, "
//	                    + "msgid => ?); END;");
//	            callStmt.registerOutParameter(1, java.sql.Types.VARCHAR); // Register output for payload
//	            callStmt.registerOutParameter(2, java.sql.Types.VARCHAR); // Register output for message ID
//
//	            callStmt.execute();
//	            String message = callStmt.getString(1);
//	            System.out.println("Dequeued message: " + message);
//
//	            connection.commit();
//
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	            if (connection != null) {
//	                try {
//	                    connection.rollback();
//	                } catch (SQLException ex) {
//	                    ex.printStackTrace();
//	                }
//	            }
//	        } finally {
//	            try {
//	                if (callStmt != null) callStmt.close();
//	                if (connection != null) connection.close();
//	            } catch (SQLException ex) {
//	                ex.printStackTrace();
//	            }
//	        }
//	    }
	    
	    public static void main(String[] args) {
	        // Enqueue a message
	        enqueueMessage("Hello, this is a test message!");

//	        // Dequeue a message
//	        dequeueMessage();
	    }
	}
