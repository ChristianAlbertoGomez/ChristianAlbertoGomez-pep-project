package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message msg){
        return null;
    }

    public List<Message> getAllMessages(){
        return null;
    }

    public Message getMessageById(int msgId){
        return null;
    }

    public Message deleteMessageById(Message msg){
        return null;
    }

    public Message updateMessage(Message msg){
        return null;
    }

    public List<Message> getMessagesByUserId(int accountId){
        return null;
    }

}
