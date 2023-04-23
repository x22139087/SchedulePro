package schedulePro.helpers;

public class UserContext {
    private static String loggedInUserId = "";

    public static void setLoggedInUserId(String userId) {
        loggedInUserId = userId;
    }

    public static String getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void clear() {
        loggedInUserId = "";
    }
}
