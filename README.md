# DroidNubeKit
An Apple's CloudKit library for Android. If you have an iOS App with CloudKit now you can use the contents on Android too.


## Set-Up
### Configure AndroidManifest.xml

You have to add the activity definition to Android Manifest. This activity opens a WebView to do the authentication with CloudKit

	<activity android:name="net.moddity.droidnubekit.ui.DNKWebViewAuthActivity" />
	
### Init DroidNubeKit

From your main activity

	DroidNubeKit.initNube(
                "YOUR TOKEN", //your api token
                "iCloud.net.moddity.droidnubekittest",
                DroidNubeKitConstants.kEnvironmentType.kDevelopmentEnvironment, //development or production
                this
        );