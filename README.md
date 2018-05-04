# DroidNubeKit
An Apple's CloudKit library for Android. If you have an iOS App with CloudKit now you can use the contents on Android too.

It's like dancing with the devil.

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
        
## Configure your models

The models must extend <b>DNKObject</b> and uses annotations to give knowledge of your schema to the lib.

### Annotations
This project uses annotations to know some things about your scheme

	//Defines the recordtype. Must match one on CloudKit dashboard.
	@RecordType("Name")

	//The record ID
	@RecordName
	
	//Defines one field and his type. Ex: @CKField(DNKFieldTypes.STRING) 
	@CKField(DNKFieldTypes)
	
	//A list of objects references AKA: REFERENCE_LIST Ex: @CKReference(CarExtras.class) 
	@CKReference(Class)


An example of a car model will be:

	@RecordType("Car")
	public class Car extends DNKObject implements Serializable {

    	@RecordName
    	public String carID;
    	
    	@CKField(DNKFieldTypes.REFERENCE)
    	private CarBrand carBrand;

    	@CKField(DNKFieldTypes.STRING)
    	public String comment;

    	@CKReference(CarExtras.class)
    	public List<CarExtras> extras = new ArrayList<>();
    	
    }