package commands;

public class Command {
    public static final String QUIT = "/q";
    public static final String AUTH_SUCCESS = "/authSuccess";
    public static final String AUTH = "/auth";
    public static final String AUTH_FAILED = "system: [authenticate failed]\n";
    public static final String PERSONAL_MSG = "/w";
    public static final String SUCCESS_CONNECT = "system: [connected to chat as %s]\n";
    public static final String NOT_DELIVERED = "system: [message is not delivered]\n";
    public static final String SERVER_STARTED = "system: Server started";
    public static final String KICKED_BY_SERVER = "system: Kicked by server";
    public static final String CLIENT_CONNECTED = "system: [client [%s] connected as %s]\n";
    public static final String LOGIN_IS_BUSY = "system: [login [%s] already is busy]\n";
    public static final String DISCONNECT = "system: [%s disconnected]\n";
    public static final String CLIENT_LIST = "/clientList";
    public static final String CONNECTED_TO_CHAT = "system: [%s connected to chat]";
    public static final String DISCONNECTED_FROM_CHAT = "system: [%s disconnected from chat]";
    public static final String REGISTER_CLIENT = "/reg";
    public static final String PASSWORDS_NOT_MATCH = "system: [passwords don`t match]\n";
    public static final String REGISTER_SUCCESS = "/regSuccess";
    public static final String REGISTER_FAILED = "/regFailed";
    public static final String REGISTER_CLIENT_SUCCESS = "system: [register new client success]\n";
    public static final String REGISTER_CLIENT_FAILED = "system: [register failed (login or nickname already is busy)]\n";
    public static final String CHANGE_MY_NICK = "/chn";
    public static final String TOO_LONG_NICK = "system: [new nickname is too long]\n";
    public static final String BAD_NICKNAME = "system: [bad nickname (spaces are not available)]\n";
    public static final String CHANGED_NICK = "i`m changed nickname to [%s]\n";

    /* SQL Commands */
    public static final String REGISTER_USER = "INSERT INTO users (user_login, user_password, user_nickname) VALUES (?, ?, ?);";
    public static final String DELETE_USER_BY_LOGIN = "UPDATE users SET user_isActive = 0 WHERE user_login = ?;";
    public static final String CHANGE_NICKNAME_BY_LOGIN = "UPDATE users SET user_nickname = ? WHERE user_login = ?;";
    public static final String GET_USERID_BY_NICKNAME = "SELECT user_id FROM users WHERE user_nickname = ?;";
    public static final String GET_NICKNAME_BY_LOGIN_AND_PASSWORD = "SELECT user_nickname FROM users WHERE user_login = ? AND user_password = ?;";
    public static final String GET_ALL_USERS = "SELECT user_login, user_password, user_nickname, user_isActive FROM users;";
    public static final String LOG_USER_MESSAGE = "INSERT INTO log_users_messages (log_user_id, log_to_user_id, log_message, log_message_datetime) VALUES (?, ?, ?, ?)";
    public static final String LOG_USER_PRIVATE_MESSAGE = "INSERT INTO log_users_messages (log_user_id, log_message, log_message_datetime) VALUES (?, ?, ?)";
    public static final String GET_TOP_LAST_MESSAGES = "select" +
            " case " +
            " when uto.user_id > 0 then lum.log_message_datetime || ' [' || u.user_nickname || '] private to [' || uto.user_nickname || ']: ' || lum.log_message" +
            " else lum.log_message_datetime || ' [' || u.user_nickname || ']: ' || lum.log_message" +
            " end as [msg]" +
            " from log_users_messages lum" +
            " inner join users u on lum.log_user_id = u.user_id" +
            " left join users uto on lum.log_to_user_id = uto.user_id" +
            " order by lum.log_id desc" +
            " limit ?;";

}