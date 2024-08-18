package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.List;

public class SocialMediaController {

    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/example-endpoint", this::exampleHandler);

        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    private void registerUser(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
    
            // Check for blank username
            if (account.getUsername() == null || account.getUsername().isBlank()) {
                context.status(400).result(""); // Return 400 for blank username
                return;
            }
    
            // Check for short password
            if (account.getPassword() == null || account.getPassword().length() < 4) {
                context.status(400).result(""); // Return 400 for short password
                return;
            }
    
            // Register the account
            Account createdAccount = accountService.registerAccount(account);
            if (createdAccount != null) {
                context.status(200).json(createdAccount); // Use 200 for successful registration
            } else {
                context.status(400).result(""); // Handle any other issues that arise
            }
    
        } catch (IllegalArgumentException e) {
            context.status(400).result(""); // Return 400 for validation issues (like duplicate username)
        } catch (Exception e) {
            context.status(500).result("Error registering user: " + e.getMessage());
        }
    }

    private void loginUser(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.loginAccount(account);
            if (loggedInAccount != null) {
                context.status(200).json(loggedInAccount);
            } else {
                context.status(401).result(""); // This line may be unnecessary if loginAccount() always returns an Account or throws an exception
            }
        } catch (IllegalArgumentException e) {
            context.status(401).result(""); // Set status to 401 for invalid credentials
        } catch (Exception e) {
            context.status(500).result("Error logging in user: " + e.getMessage());
        }
    }

    private void createMessage(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
    
            // Validate message text
            if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
                context.status(400).result("");
                return;
            }
    
            // Check if the account exists
            if (!accountService.doesAccountExist(message.getPosted_by())) {
                context.status(400).result("");
                return;
            }
    
            // Create the message
            Message createdMessage = messageService.createMessage(message);
    
            if (createdMessage == null || createdMessage.getMessage_id() <= 0) {
                context.status(500).result("");
                return;
            }
    
            context.status(200).json(createdMessage);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("");
        }
    }

    private void getAllMessages(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
            context.status(200).json(messages);
        } catch (Exception e) {
            context.status(500).result("Error retrieving messages: " + e.getMessage());
        }
    }

    private void getMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                context.status(200).json(message);
            } else {
                context.status(404).result("Message not found.");
            }
        } catch (Exception e) {
            context.status(500).result("Error retrieving message: " + e.getMessage());
        }
    }

    private void deleteMessageById(Context context) {
        try {
            // Get the message ID from the URL
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            
            // Check if the message exists
            Message message = messageService.getMessageById(messageId);
            if (message == null) {
                context.status(200).result(""); // Message not found, return status 200 with empty body
                return;
            }
            
            // Delete the message
            messageService.deleteMessageById(messageId);
            
            // Return the deleted message
            context.status(200).json(message);
        } catch (Exception e) {
            context.status(500).result(""); // Server error
        }
    }

    private void updateMessageById(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
                context.status(400).result("Message text cannot be empty and must be under 255 characters.");
                return;
            }
            Message updatedMessage = messageService.updateMessage(message);
            if (updatedMessage != null) {
                context.status(200).json(updatedMessage);
            } else {
                context.status(404).result("Message not found or update failed.");
            }
        } catch (Exception e) {
            context.status(500).result("Error updating message: " + e.getMessage());
        }
    }

    private void getMessagesByUserId(Context context) {
        try {
            int accountId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUserId(accountId);
            if (messages.isEmpty()) {
                context.status(200).json(messages); // Return status 200 with empty list
            } else {
                context.status(200).json(messages);
            }
        } catch (Exception e) {
            context.status(500).result("Error retrieving user messages: " + e.getMessage());
        }
    }

    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}
