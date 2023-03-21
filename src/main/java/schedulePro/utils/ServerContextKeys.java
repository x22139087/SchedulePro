package schedulePro.utils;

import io.grpc.Context;
import io.grpc.Metadata;

import java.util.Objects;

public class ServerContextKeys {

    private static final Context.Key<String> USER_ID = Context.key("user-id");
    private static final Context.Key<String> USER_SESSION_ID = Context.key("sessionUserId");
    private static final Metadata.Key<String> AUTH_TOKEN = Metadata.Key.of("auth-token", Metadata.ASCII_STRING_MARSHALLER);

    public static Context withUserId(Context context, String userId) {
        return context.withValue(USER_ID, userId);
    }

    public static String getUserId(Context context) {
        return USER_ID.get(context);
    }


    public static Context withUserSessionId(Context context, String userSessionId) {
        return context.withValue(USER_SESSION_ID, userSessionId);
    }

    public static String getUserSessionId(Context context) {
        return USER_SESSION_ID.get(context);
    }

    public static Metadata withAuthToken(Metadata headers, String authToken) {
        Metadata result = new Metadata();
        headers.keys().forEach(key -> {
            result.put(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER), Objects.requireNonNull(headers.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER))));
        });
        result.put(AUTH_TOKEN, authToken);
        return result;
    }

    public static String getAuthToken(Metadata headers) {
        return headers.get(AUTH_TOKEN);
    }
}
