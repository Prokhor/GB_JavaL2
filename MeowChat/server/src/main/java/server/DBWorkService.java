package server;

import org.sqlite.core.DB;

import java.sql.ResultSet;

public class DBWorkService implements AuthService, LogService {

    private DataBaseConnection dbc;

    public DBWorkService() {
        dbc = new DataBaseConnection();
        try {
            dbc.connect("org.sqlite.JDBC", "jdbc:sqlite:MeowChatDB.db");
            System.out.println("Connected to DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            ResultSet rsUsers = dbc.getAllUsers();
            while (rsUsers.next()) {
                if (rsUsers.getString("user_login").equalsIgnoreCase(login) && rsUsers.getString("user_password").equals(password)) {
                    return rsUsers.getString("user_nickname");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            dbc.registerUser(login, password, nickname);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void logUserMessage(int user_id, int to_user_id, String message, String message_datetime) {
        try {
            dbc.logUserMessage(user_id, to_user_id, message, message_datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logUserMessage(int user_id, String message, String message_datetime) {
        try {
            dbc.logUserMessage(user_id, message, message_datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getUserIDByNickname(String nickname) {
        try {
            return dbc.getUserIDByNickName(nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DB.SQLITE_NULL;
    }

    public boolean changeNickname(String login, String newNickname){
        try {
            dbc.changeUserNickname(login, newNickname);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        dbc.disconnect();
    }
}