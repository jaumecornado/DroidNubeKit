package moddity.net.droidnubekit;

/**
 * Created by jaume on 11/6/15.
 */
public class DroidNubeKitConstants {
    /** Current CloudKit endpoint */
    public final static String API_ENDPOINT = "https://api.apple-cloudkit.com";
    /** The current protocol of CloudKit API */
    public final static String PROTOCOL = "1";

    /**
     * The different environment types available on CloudKit
     */
    enum kEnvironmentType {
        kDevelopmentEnvironment,
        kProductionEnvironment;

        public String toString() {
            if(this == kDevelopmentEnvironment)
                return "development";
            return "production";
        }
    }

    enum kDatabaseType {
        kPublicDatabase,
        kPrivateDatabase;

        public String toString() {
            if(this == kPrivateDatabase)
                return "private";
            return "public";
        }
    }
}
