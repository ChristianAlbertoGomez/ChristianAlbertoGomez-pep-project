package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;
import java.sql.*;

/**
 * MessageService.java
 * 
 * Created and documented by Christian Alberto Gomez
 * Date: August 21, 2024
 * 
 * This class provides services related to messages. It uses the MessageDAO class to perform database operations 
 * and includes methods for creating, retrieving, updating, and deleting messages, as well as retrieving messages 
 * by user ID.
 */
public class MessageService {

    private final MessageDAO messageDAO = new MessageDAO();

    /**
     * Creates a new message after validating the message text.
     * 
     * @param message the Message object containing message details
     * @return the created Message object with its ID set
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the message text is blank or exceeds 255 characters
     */
    public Message createMessage(Message message) throws SQLException {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters");
        }
        message.setTime_posted_epoch(1669947792); // Set a fixed time for the message
        return messageDAO.createMessage(message);
    }

    /**
     * Retrieves all messages from the database.
     * 
     * @return a list of Message objects
     * @throws SQLException if a database access error occurs
     */
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a message by its ID.
     * 
     * @param messageId the ID of the message to retrieve
     * @return the Message object with the specified ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Deletes a message by its ID if it exists.
     * 
     * @param messageId the ID of the message to delete
     * @return true if the message was deleted, false if the message did not exist
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteMessageById(int messageId) throws SQLException {
        Message message = messageDAO.getMessageById(messageId);
        if (message == null) {
            return false; // Handle the case where the message doesn't exist
        }
        messageDAO.deleteMessageById(messageId);
        return true;
    }

    /**
     * Updates an existing message after validating the message text.
     * 
     * @param message the Message object with updated details
     * @return the updated Message object
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the message text is blank or exceeds 255 characters, or if the message does not exist
     */
    public Message updateMessage(Message message) throws SQLException {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters");
        }
        Message existingMessage = messageDAO.getMessageById(message.getMessage_id());
        if (existingMessage == null) {
            throw new IllegalArgumentException("Message does not exist");
        }
        message.setPosted_by(existingMessage.getPosted_by());
        message.setTime_posted_epoch(existingMessage.getTime_posted_epoch());
        messageDAO.updateMessage(message);
        return messageDAO.getMessageById(message.getMessage_id());
    }

    /**
     * Retrieves messages posted by a specific user.
     * 
     * @param accountId the ID of the user whose messages to retrieve
     * @return a list of Message objects posted by the specified user
     * @throws SQLException if a database access error occurs
     */
    public List<Message> getMessagesByUserId(int accountId) throws SQLException {
        return messageDAO.getMessagesByUserId(accountId);
    }
}
