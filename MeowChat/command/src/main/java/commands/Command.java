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
}
