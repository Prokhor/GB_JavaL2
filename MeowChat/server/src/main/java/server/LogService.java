package server;

public interface LogService {
    void logUserMessage(int user_id, int to_user_id, String message, String message_datetime);
    void logUserMessage(int user_id, String message, String message_datetime);
    int getUserIDByNickname(String nickname);
}
