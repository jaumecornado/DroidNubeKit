package net.moddity.droidnubekit;

/**
 * Created by jaume on 11/6/15.
 */
public class DroidNubeKitConstants {
    /** Current CloudKit endpoint */
    public final static String API_ENDPOINT = "https://api.apple-cloudkit.com";
    /** The current protocol of CloudKit API */
    public final static String PROTOCOL = "1";
    /** The extra key to pass on webview intent */
    public final static String WEBVIEW_REDIRECT_URL_EXTRA = "WEBVIEW_REDIRECT_URL_EXTRA";
    /** The extra key to pass pattern on the webview */
    public final static String WEBVIEW_REDIRECT_PATTERN_EXTRA = "WEBVIEW_REDIRECT_PATTERN_EXTRA";
    /** The prefix appended by CloudKit on redirect Pattern */
    public final static String WEBVIEW_REDIRECT_URL_PREFIX = "cloudkit-";
    /** The login endpoint of auth redirect */
    public final static String WEBVIEW_REDIRECT_LOGIN_ENDPOINT = "login";
    /** Shared preferences name */
    public final static String CLOUDKIT_SHARED_PREFERENCES = "CLOUDKIT_SHARED_PREFERENCES";
    /** Session key */
    public final static String CLOUDKIT_SESSION_KEY = "CK_SESSION_KEY";

    /**
     * The different environment types available on CloudKit
     */
    public enum kEnvironmentType {
        kDevelopmentEnvironment,
        kProductionEnvironment;

        public String toString() {
            if(this == kDevelopmentEnvironment)
                return "development";
            return "production";
        }
    }

    public enum kDatabaseType {
        kPublicDatabase,
        kPrivateDatabase;

        public String toString() {
            if(this == kPrivateDatabase)
                return "private";
            return "public";
        }
    }
}
