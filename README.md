# Appluck Android SDK Integration Guide
=========

[中文](https://github.com/jxsong1989/Appluck_SDK_Android/blob/master/README-CN.md)
<br/>
<br/>
[GitHub Repository](https://github.com/jxsong1989/Appluck_SDK_Android)
<br/>


Requirements
--------
MinSDK 21 (Android 5.0) and above

## 1. Download Appluck SDK
 [AppLuckSDK.aar][alup]

## 2. Integrate the SDK
1. In the Android Studio Project Navigator, select Project. You will see the libs folder in the app module of your Android Studio project.
2. Add the AppLuckSDK.aar file to the libs folder.
3. In the build.gradle file of your app module, make sure to add the following to the dependencies block:
```groovy
dependencies {
  implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
}
```
4. In the AndroidManifest.xml file of your app module, make sure to add the following to the manifest block:
```xml
 <uses-permission android:name="android.permission.INTERNET" />
 <uses-permission android:name="com.google.android.gms.permission.AD_ID" />
```

## 3. Configure Dependencies
You can include dependencies in either of the following ways, choose one:

* Using Gradle

  ```groovy
  implementation 'androidx.appcompat:appcompat:1.3.0'
  implementation 'com.google.android.material:material:1.4.0'
  // Common utilities
  implementation 'org.apache.commons:commons-lang3:3.3.2'
  // HTTP requests
  implementation 'org.jsoup:jsoup:1.11.2'
  // JSON processing
  implementation 'com.alibaba:fastjson:1.1.72.android'
  // Image loading and caching
  implementation 'com.github.bumptech.glide:glide:4.14.2'
  // Google Advertising ID (GAID)
  implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'
  ```

 * Manual Integration

   * Unzip libs.zip and place all contents into Assets/Plugins/Android


## 4. Start Integration

### 4.1 Initialize SDK

#### 4.1.1 Fill in the Initialization Success Callback.

```java
AppLuckSDK.setListener(new AppLuckSDK.AppLuckSDKListener() {
            @Override
            public void onInitSuccess() {
                Toast.makeText(MyApplication.this, "AppLuck SDK Init Success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInitFailed(Error error) {
                Log.e("AppLuckSDK", "Init Failed.", error);
                Toast.makeText(MyApplication.this, "AppLuck SDK Init Failed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlacementLoadSuccess(String placementId) {
                Toast.makeText(MyApplication.this, placementId + " Load Success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInteractiveAdsHidden(String s, int i) {
                
            }
	    
	    @Override
            public void onInteractiveAdsDisplayed(String s) {

            }

            /**
             *
             * @param placementId
             * @param interaction INTERACTIVE_PLAY 活动参与; INTERACTIVE_CLICK 广告点击
             */
            @Override
            public void onUserInteraction(String placementId, String interaction) {
                Toast.makeText(MyApplication.this, placementId + " " + interaction, Toast.LENGTH_SHORT).show();
            }
        });
  ```

### 4.1.2 Initialization

```java
// Please run on the main thread
// placementId - Ad placement ID. The plugin will automatically preload this location. If there are multiple ad placements in the product, it is recommended to pass in the ID of the most important one, i.e., the one with the highest expected exposure.
// Contact your operations team to obtain the production environment placementId.
AppLuckSDK.init(placementId);
  ```

### 4.2 Set up Ad Placement Entrance

Appluck supports two methods of ad placement entrance:

- Use the encapsulated method to load the entrance (recommended)
  - Simply provide the width, height, and position of the entrance for display. Appluck will recommend materials based on the system and optimize them in real-time based on click-through rates.
- Set up the entrance manually (suitable for cases where there are special requirements for the entrance, or in some scenarios where you want to directly open interactive ads)
  - When you want to open interactive ads, call our provided method to open Appluck's activity page.

#### 4.2.1 Use Appluck's Encapsulated Entrance

1. Fill in the placement loading success callback

  ```java
AppLuckSDK.setListener(new AppLuckSDK.AppLuckSDKListener() {
	    //loadedPlacementId - Successfully loaded placementId
            @Override
            public void onPlacementLoadSuccess(String placementId) {
                Toast.makeText(MyApplication.this, placementId + " Load Success.", Toast.LENGTH_SHORT).show();
            }
        });
  ```

2. Load Placement Material

  ```java
// placementId - Ad placement id
// creative type - Material type, currently only supports icon
// width - Material width at the entrance position
// height - Material height at the entrance position
if (AppLuckSDK.isSDKInit()) {
    AppLuckSDK.loadPlacement(placementId, "icon", 150, 150);
}
  ```

3. Display the ad placement. In a preloading scenario, execute the following code periodically until the ad placement is successfully displayed. You can refer to the [demo][demo] for specific implementation.

```java
// Display at specified coordinates (pixels)
if (AppLuckSDK.isSDKInit()) {
    if (AppLuckSDK.isPlacementReady(placementId)) {
        // this: Current Activity
        // placementId
        // top: y-coordinate
        // left: x-coordinate
        // times: Default to 1
        AppLuckSDK.showInteractiveEntrance(this, placementId, top, left, times);
    }
}

// Get the view for self-display
if (AppLuckSDK.isSDKInit()) {
    if (AppLuckSDK.isPlacementReady(placementId)) {
        LinearLayout llAddView = "{your Layout}";
        // this: Current Activity
        // placementId
        // times: Default to 1
        View iconView = AppLuckSDK.showInteractiveEntrance(this, placementId, 1);
        // Display iconView
        if (iconView != null) {
            if (iconView.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) iconView.getParent();
                viewGroup.removeView(iconView);
            }
            iconView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llAddView.addView(iconView);
        }
    }
}
  ```
4. Hide Placement

```java
// activity - The activity where the entrance is located
// placementId - Ad placement id
AppLuckSDK.hideInteractiveEntrance(activity, placementId);
  ```


#### 4.2.2 Set up Entrance Manually

- For scenes directly opening interactive ads, please call the following:

```java
if (AppLuckSDK.isSDKInit()) {
    // Invoke the webview and load the activity, please pass in placementId
    // mode 
    //-- 0. Default mode: Suitable for fixed entrance scenes such as floating banner, users can freely close the interactive ad interface.
    //-- 1. Interstitial mode: Suitable for interstitial scenes, users can close it after 10 seconds.
    //-- 2. Reward mode: Suitable for reward scenes, users can close the interactive ad interface after participating in the activity {times} times, and closing the interface triggers the reward callback.
    // times
    //-- Used to limit the number of activity participations required by users when mode is 2 (reward mode).
    AppLuckSDK.openInteractiveAds(placementId, mode, times);
}
```

- Set up the entrance manually, wait for Appluck to preload before displaying

### 4.3 Other Events

```java
AppLuckSDK.setListener(new AppLuckSDK.AppLuckSDKListener() {
    // SDK initialization failure    
    @Override
    public void onInitFailed(Error error) {
        Log.e("AppLuckSDK", "Init Failed.", error);
        // Toast.makeText(MyApplication.this, "AppLuck SDK Init Failed.", Toast.LENGTH_SHORT).show();
    }
    
    // AppLuck Close Callback
    // status 0: Normal close; 1: Completed incentive task
    @Override
    public void onInteractiveAdsHidden(String placementId, int i) {
        // Your code here
    }
});
```

[alup]: https://github.com/jxsong1989/Appluck_SDK_Android/releases/tag/v1.2.2
[demo]: https://github.com/jxsong1989/Appluck_SDK_Android/blob/master/app/src/main/java/com/example/appluck_intergration_guide_sdk_android/MainActivity.java
