package pl.coderslab;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users  SET username = ?, email = ?, password = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";

    private static final String DELETEUSERNAME_USER_QUERY =
            "DELETE FROM users WHERE username = ?";

    private static final String FIND_ALL_USER_QUERY = "SELECT * FROM users";


    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return getUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        while (resultSet.next()) {
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setUserName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
        }
        return user;
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            System.out.println("Podaj ID użytkownika do skasowania");
            statement.setInt(1, scanner.nextInt());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUsername(String username) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETEUSERNAME_USER_QUERY);
            statement.setString(1, "Tomasz");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USER_QUERY);
            ResultSet resultSet = statement.executeQuery();
            return getUsers(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User[] findEmail() {
        try (Connection conn = DbUtil.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Podaj domenę email której szukamy?");
            String keyword = scanner.nextLine();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE email LIKE ?");
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();
            return getUsers(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }

    private User[] getUsers(ResultSet resultSet) throws SQLException {
        User[] users = new User[0];
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setUserName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            users = addToArray(user, users);
        }
        return users;
    }
}
