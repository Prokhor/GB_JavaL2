package server;

import commands.Command;

import java.sql.*;

public class DataBaseConnection {

    private Connection connection;

    public void connect(String driver, String connectionString) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        this.connection = DriverManager.getConnection(connectionString);
    }

    public void disconnect() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    this.connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public int registerUser(String login, String password, String nickname) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.REGISTER_USER);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, nickname);

        return preparedStatement.executeUpdate();
    }

    public int changeUserNickname(String login, String newNickname) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.CHANGE_NICKNAME_BY_LOGIN);
        preparedStatement.setString(1, newNickname);
        preparedStatement.setString(2, login);

        return preparedStatement.executeUpdate();
    }

    public int deleteUserByLogin(String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.DELETE_USER_BY_LOGIN);
        preparedStatement.setString(1, login);

        return preparedStatement.executeUpdate();
    }

    public int getUserIDByNickName(String nickname) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.GET_USERID_BY_NICKNAME);
        preparedStatement.setString(1, nickname);

        return preparedStatement.executeQuery().getInt("user_id");
    }

    public ResultSet getAllUsers() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.GET_ALL_USERS);

        return preparedStatement.executeQuery();
    }

    public int logUserMessage(int user_id, int to_user_id, String message, String message_datetime) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.LOG_USER_MESSAGE);
        preparedStatement.setInt(1, user_id);
        preparedStatement.setInt(2, to_user_id);
        preparedStatement.setString(3, message);
        preparedStatement.setString(4, message_datetime);

        return preparedStatement.executeUpdate();
    }

    public int logUserMessage(int user_id, String message, String message_datetime) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(Command.LOG_USER_PRIVATE_MESSAGE);
        preparedStatement.setInt(1, user_id);
        preparedStatement.setString(2, message);
        preparedStatement.setString(3, message_datetime);

        return preparedStatement.executeUpdate();
    }
}