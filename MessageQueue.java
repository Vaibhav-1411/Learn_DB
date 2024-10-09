package oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

public class MessageQueue {
    private static final String jdbc_url = "jdbc:oracle:thin:@//localhost:1521/orcl"; // Update as per your DB configuration
    private static final String username = "system"; // Update with your DB username
    private static final String password = "orcl"; // Update with your DB password

    private static final Logger logger = Logger.getLogger(MessageQueue.class.getName());

    public static void main(String[] args) {
        Connection con = null;
        CallableStatement callstmt = null;

        try {
            con = DriverManager.getConnection(jdbc_url, username, password);
            logger.info("Database connected successfully.");

            String sql = "{call DBMS_AQ.ENQUEUE(?, ?, ?, ?, ?)}";
//            String sql = "{BEGIN DBMS_AQ.ENQUEUE(queue_name => ?, enqueue_options => ?, message_properties => ?, payload => ?, msgid => ?); END;";
            callstmt = con.prepareCall(sql);
            callstmt.setString(1, "queue_test"); // Queue name
            
            callstmt.setObject(2, null); // Enqueue option
            callstmt.setObject(3, null); // Message properties
            
            byte[] payload = "New Order Received".getBytes();
            callstmt.setBytes(4, payload); // Payload which holds the actual message
            
            callstmt.registerOutParameter(5, Types.BINARY);

            callstmt.execute();

            byte[] msgId = callstmt.getBytes(5);

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < msgId.length; i++) {
                String hex = Integer.toHexString(0xFF & msgId[i]);
                if (hex.length() == 1) 
                    hexString.append('0'); // Pad with leading zero if needed
                hexString.append(hex);
            }

            System.out.println("Message enqueued with ID: " + hexString.toString());

        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        catch (SQLException e) {
            logger.info("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (callstmt != null) callstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.info("Resources not closed properly: " + e.getMessage());
            }
        }
    }
}
