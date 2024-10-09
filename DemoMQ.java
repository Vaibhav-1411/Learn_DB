package oracle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class DemoMQ {
	 private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl"; // Update as per your DB configuration
	    private static final String username = "system"; // Update with your DB username
	    private static final String password = "orcl"; // Update with your DB password
	    
	public static void main(String[] args) {
        // Enqueue a message
        enqueueMessage("Hello, AQ!");

        // Dequeue a message
// dequeueMessage();
    }

    public static void enqueueMessage(String message) {
        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(jdbc_url,username,password);
            conn.setAutoCommit(false); // Start transaction

            // Call the enqueue procedure
            cstmt = conn.prepareCall("{call DBMS_AQ.ENQUEUE(?, ?, ?, ?, ?)}");
            
            // Queue name
            cstmt.setString(1, "queue_test");
            
            // Enqueue options
            cstmt.setNull(2, Types.VARCHAR);
            
            // Message ID (output)
            cstmt.registerOutParameter(3, Types.VARCHAR);
            
            // Payload (message content)
            cstmt.setBytes(4, message.getBytes());
            
            // Message properties
            cstmt.setNull(5, Types.VARCHAR);

            cstmt.execute();
            conn.commit();

            String msgId = cstmt.getString(3); // Get the message ID
            System.out.println("Message enqueued: " + message + ", ID: " + msgId);
        } 
        
        
        catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Cleanup resources
            try {
                if (cstmt != null) cstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    public static void dequeueMessage() {
        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(jdbc_url,username,password);
            conn.setAutoCommit(false); // Start transaction

            // Call the dequeue procedure
            cstmt = conn.prepareCall("{call DBMS_AQ.DEQUEUE(?, ?, ?, ?, ?, ?)}");
            
         // Queue name
            cstmt.setString(1, "queue_test"); 
            
            // Dequeue options 
            cstmt.setNull(2, java.sql.Types.VARCHAR);
            
         // Payload type
            cstmt.setString(3, "RAW"); 
            cstmt.setNull(4, java.sql.Types.VARCHAR); // Message ID 
            cstmt.setNull(5, java.sql.Types.VARCHAR); // Options 
            cstmt.setString(6, ""); // Handle message (as output parameter)

            cstmt.execute();

            System.out.println("Message dequeued.");

            // Handle the dequeued message here (you might need to retrieve the payload)

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Cleanup resources
            try {
                if (cstmt != null) cstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}