package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageDAO.java
 * 
 * Created and documented by Christian Alberto Gomez
 * Date: August 21, 2024
 * 
 * This class handles database operations related to Message actions. It provides methods for creating, retrieving, 
 * updating, and deleting messages. The class uses JDBC for database interactions.
 */
public class MessageDAO {

    /**
     * Creates a new message in the database and returns the created message with the generated ID.
     * 
     * @param message the Message object to be created
     * @return the created Message object with its ID set
     * @throws SQLException if a database access error occurs
     */
    public Message createMessage(Message message) throws SQLException {
        String query = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());
            pstmt.executeUpdate();
    
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                }
            }
        }
        return message;
    }

    /**
     * Retrieves all messages from the database.
     * 
     * @return a list of all Message objects
     * @throws SQLException if a database access error occurs
     */
    public List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message";
        
        try (Connection conn = ConnectionUtil.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                                         rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        
        return messages;
    }

    /**
     * Retrieves a message from the database by its ID.
     * 
     * @param messageId the ID of the message to be retrieved
     * @return the Message object with the specified ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Message getMessageById(int messageId) throws SQLException {
        String query = "SELECT * FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, messageId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                }
            }
        }
        return null;
    }

    /**
     * Deletes a message from the database by its ID.
     * 
     * @param messageId the ID of the message to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deleteMessageById(int messageId) throws SQLException {
        String query = "DELETE FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, messageId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates an existing message in the database.
     * 
     * @param message the Message object with updated information
     * @throws SQLException if a database access error occurs
     */
    public void updateMessage(Message message) throws SQLException {
        String query = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, message.getMessage_text());
            pstmt.setInt(2, message.getMessage_id());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves messages from the database by the ID of the user who posted them.
     * 
     * @param accountId the ID of the user whose messages are to be retrieved
     * @return a list of Message objects posted by the specified user
     * @throws SQLException if a database access error occurs
     */
    public List<Message> getMessagesByUserId(int accountId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM Message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
                }
            }
        }
        return messages;
    }
}
