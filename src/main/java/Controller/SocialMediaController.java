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
    private final int num = 0;

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
            Account createdAccount = accountService.registerAccount(account);
            if (createdAccount != null) {
                context.status(201).json(createdAccount);
            } else {
                context.status(400).result("Account registration failed.");
            }
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
                context.status(401).result("Invalid username or password.");
            }
        } catch (Exception e) {
            context.status(500).result("Error logging in user: " + e.getMessage());
        }
    }

    private void createMessage(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
                context.status(400).result("Message text cannot be empty and must be under 255 characters.");
                return;
            }
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage != null) {
                context.status(201).json(createdMessage);
            } else {
                context.status(400).result("Message creation failed.");
            }
        } catch (Exception e) {
            context.status(500).result("Error creating message: " + e.getMessage());
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
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            boolean deleted = messageService.deleteMessageById(messageId);
            if (deleted) {
                context.status(200).result("Message deleted successfully.");
            } else {
                context.status(404).result("Message not found.");
            }
        } catch (Exception e) {
            context.status(500).result("Error deleting message: " + e.getMessage());
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
            if (!messages.isEmpty()) {
                context.status(200).json(messages);
            } else {
                context.status(404).result("No messages found for this user.");
            }
        } catch (Exception e) {
            context.status(500).result("Error retrieving user messages: " + e.getMessage());
        }
    }

    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}
