package com.example.appluck_intergration_guide_sdk_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appluck.sdk.AppLuckSDK;

public class MainActivity2 extends AppCompatActivity {

    private final static Handler HANDLER = new Handler();

    private Button btnOpen2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnOpen2 = findViewById(R.id.btn_open2);
        btnOpen2.setOnClickListener((v) -> {
            //激励打开
            AppLuckSDK.openInteractiveAds(MyApplication.APPLUCK_PLACEMENT_ID, 2);
        });
        btnOpen2.setVisibility(View.GONE);
        showAppLuckPlacement();
    }

    public void showAppLuckPlacement() {
        if (AppLuckSDK.isPlacementReady(MyApplication.APPLUCK_PLACEMENT_ID2)) {
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID2, 600, 600);
            btnOpen2.setVisibility(View.VISIBLE);
            return;
        }
        HANDLER.postDelayed(() -> {
            showAppLuckPlacement();
        }, 1000);
    }
}