package DAO;
import Util.ConnectionUtil;
import java.sql.*;
import Model.Account;

public class AccountDAO{
    /*
     * The following methods are connected with the database. These methods are related with Account actions.
     */

     public Account createAccount(Account account)throws SQLException{
        
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

     public Account getAccountByUsername(String username)throws SQLException{

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

     public Account getAccountById(int accountId) throws SQLException {
        
        String query = "SELECT * FROM Account WHERE account_id = ?";
        try (Connection conn = ConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
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