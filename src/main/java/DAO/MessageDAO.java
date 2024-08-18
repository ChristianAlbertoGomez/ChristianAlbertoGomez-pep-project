package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    /*
     * The following methods are connected with the database. These methods are related with Message actions.
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

    public List<Message> getAllMessages() throws SQLException {

        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM Message";
        try (Connection conn = ConnectionUtil.getConnection(); 
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        return messages;
    }

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

    public void deleteMessageById(int messageId) throws SQLException {

        String query = "DELETE FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, messageId);
            pstmt.executeUpdate();
        }
    }

    public void updateMessage(Message message) throws SQLException {

        String query = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, message.getMessage_text());
            pstmt.setInt(2, message.getMessage_id());
            pstmt.executeUpdate();
        }
    }

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
