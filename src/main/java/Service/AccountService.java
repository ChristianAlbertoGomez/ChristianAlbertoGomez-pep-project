package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.*;

/**
 * AccountService.java
 * 
 * Created and documented by Christian Alberto Gomez
 * Date: August 21, 2024
 * 
 * This class provides services related to user accounts. It uses the AccountDAO class to perform database operations 
 * and includes methods for registering and logging in users, as well as checking if an account exists.
 */
public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Registers a new user account if the username and password meet the required criteria.
     * 
     * @param account the Account object containing user details
     * @return the created Account object with its ID set
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the username is blank, the password is too short, or the username already exists
     */
    public Account registerAccount(Account account) throws SQLException {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username can't be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        return accountDAO.createAccount(account);
    }

    /**
     * Logs in a user by checking if the provided username and password match an existing account.
     * 
     * @param account the Account object containing user login details
     * @return the Account object if login is successful
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the username/password is invalid
     */
    public Account loginAccount(Account account) throws SQLException {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            throw new IllegalArgumentException("Invalid username/password");
        }
        return existingAccount;
    }

    /**
     * Checks if an account exists in the database by its ID.
     * 
     * @param accountId the ID of the account to check
     * @return true if the account exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean doesAccountExist(int accountId) throws SQLException {
        return accountDAO.getAccountById(accountId) != null;
    }
}
