package server;

import java.sql.ResultSet;
import java.sql.SQLException;

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
            return dbc.getUserNicknameByLoginAndPassword(login, password);
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
        return -1;
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

    public ResultSet loadUserHistory(int user_id, int limit){
        try {
            return dbc.getUserHistory(user_id, limit);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        dbc.disconnect();
    }
}