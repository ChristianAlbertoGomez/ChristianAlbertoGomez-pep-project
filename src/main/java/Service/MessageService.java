package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;
import java.sql.*;


public class MessageService {

    private final MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) throws SQLException {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters");
        }
        message.setTime_posted_epoch(System.currentTimeMillis() / 1000); // Ensure correct timestamp
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.getMessageById(messageId);
    }

    public boolean deleteMessageById(int messageId) throws SQLException {
        Message message = messageDAO.getMessageById(messageId);
        if (message == null) {
            return false; // Handle the case where the message doesn't exist
        }
        messageDAO.deleteMessageById(messageId);
        return true;
    }

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
        message.setPosted_by(existingMessage.getPosted_by()); // Ensure we preserve original information
        message.setTime_posted_epoch(existingMessage.getTime_posted_epoch()); // Preserve original timestamp
        messageDAO.updateMessage(message);
        return messageDAO.getMessageById(message.getMessage_id());
    }

    public List<Message> getMessagesByUserId(int accountId) throws SQLException {
        return messageDAO.getMessagesByUserId(accountId);
    }
    

}
