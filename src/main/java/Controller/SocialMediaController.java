package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerUser);
        app.post("login",this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/:message_id", this::getMessageById);
        app.delete("/messages/:message_id", this::deleteMessageById);
        app.patch("/messages/:message_id", this::updateMessageById);
        app.get("/accounts/:account_id/messages", this::getMessagesByUserId);

        return app;
    }

    private void registerUser(Context context){
        try {
            
        } catch (Exception e) {
            
        }
    }

    private void loginUser(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void createMessage(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    private void getAllMessages(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    private void getMessageById(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    private void deleteMessageById(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    private void updateMessageById(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    
    private void getMessagesByUserId(Context context){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }



    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}