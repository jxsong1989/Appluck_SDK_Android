package com.example.appluck_intergration_guide_sdk_android;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.appluck.sdk.AppLuckSDK;

public class MyApplication extends Application {
    public final static String APPLUCK_PLACEMENT_ID = "q842c2e079a1b32c8";

    public final static String APPLUCK_PLACEMENT_ID2 = "q842c2e0a9a1e19c3";

    @Override
    public void onCreate() {
        super.onCreate();
        AppLuckSDK.setListener(new AppLuckSDK.AppLuckSDKListener() {
            @Override
            public void onInitSuccess() {
                Toast.makeText(MyApplication.this, "AppLuck SDK Init Success.", Toast.LENGTH_SHORT).show();
                AppLuckSDK.loadPlacement(APPLUCK_PLACEMENT_ID, "icon", 150, 150);
                AppLuckSDK.loadPlacement(APPLUCK_PLACEMENT_ID2, "icon", 150, 150);
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
        AppLuckSDK.init(APPLUCK_PLACEMENT_ID);
    }
}
