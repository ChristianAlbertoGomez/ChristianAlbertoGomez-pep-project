package DAO;

import Util.ConnectionUtil;
import java.sql.*;
import Model.Account;

/**
 * AccountDAO.java
 * 
 * Created and documented by Christian Alberto Gomez
 * Date: August 21, 2024
 * 
 * This class handles database operations related to Account actions. It provides methods for creating an account, 
 * retrieving accounts by username or ID, and other related actions. The class uses JDBC for database interactions.
 */
public class AccountDAO {

    /**
     * Creates a new account in the database and returns the created account with the generated ID.
     * 
     * @param account the Account object to be created
     * @return the created Account object with its ID set
     * @throws SQLException if a database access error occurs
     */
    public Account createAccount(Account account) throws SQLException {
        String query = "INSERT INTO Account (username, password) VALUES (?, ?)";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setAccount_id(rs.getInt(1));
                }
            }
        }
        return account;
    }

    /**
     * Retrieves an account from the database by its username.
     * 
     * @param username the username of the account to be retrieved
     * @return the Account object with the specified username, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Account getAccountByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Account WHERE username = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    /**
     * Retrieves an account from the database by its ID.
     * 
     * @param accountId the ID of the account to be retrieved
     * @return the Account object with the specified ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Account getAccountById(int accountId) throws SQLException {
        String query = "SELECT * FROM Account WHERE account_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}
