package net.moddity.droidnubekit;


import net.moddity.droidnubekit.interfaces.CloudKitService;

import retrofit.RestAdapter;

/**
 * Created by Jaume Cornadó on 11/6/15.
 */
public class DroidNubeKit {

    /** The singleton instance */
    private static DroidNubeKit instance;

    /** The CloudKit Service reference */
    private CloudKitService cloudKitService;

    /** The API Token */
    private String apiToken;

    /** If it's production or development */
    private DroidNubeKitConstants.kEnvironmentType environmentType;

    private String appContainerIdentifier;

    /**
     * Initializes the CloudKit Service
     */
    private DroidNubeKit() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DroidNubeKitConstants.API_ENDPOINT)
                .build();

        cloudKitService = restAdapter.create(CloudKitService.class);
    }

    private static DroidNubeKit getInstance() {
        if(getInstance() == null)
            instance = new DroidNubeKit();
        return instance;
    }

    //----------------------
    // Public methods
    //----------------------
    public static void initNube(String apiToken, String appContainerIdentifier, DroidNubeKitConstants.kEnvironmentType environmentType) {
        DroidNubeKit.getInstance().apiToken = apiToken;
        DroidNubeKit.getInstance().environmentType = environmentType;
        DroidNubeKit.getInstance().appContainerIdentifier = appContainerIdentifier;
    }

    public static void getZones(DroidNubeKitConstants.kDatabaseType databaseType) {

    }
}
