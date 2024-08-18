package Service;
import DAO.AccountDAO;
import Model.Account;
import java.sql.*;

public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

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

    public Account loginAccount(Account account) throws SQLException {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            throw new IllegalArgumentException("Invalid username/password");
        }
        return existingAccount;
    }
}
