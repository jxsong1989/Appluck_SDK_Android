package com.example.appluck_intergration_guide_sdk_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appluck.sdk.AppLuckSDK;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final static Handler HANDLER = new Handler();

    private Button btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnActivity2 = findViewById(R.id.btn_activity2);
        btnActivity2.setOnClickListener((v) -> {
            final Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });
        btnOpen = findViewById(R.id.btn_open);
        btnOpen.setOnClickListener((v) -> {
            //插屏打开
            AppLuckSDK.openInteractiveAds(MyApplication.APPLUCK_PLACEMENT_ID, 1, 1);
        });
        btnOpen.setVisibility(View.GONE);
        final Button btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener((v) -> {
            //APPLUCK show
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID, 600, 600, 1);
        });
        final Button btnHide = findViewById(R.id.btn_hide);
        btnHide.setOnClickListener((v) -> {
            //APPLUCK hide
            AppLuckSDK.hideInteractiveEntrance(this,MyApplication.APPLUCK_PLACEMENT_ID);
        });
        showAppLuckPlacement();
    }

    public void showAppLuckPlacement() {
        if (AppLuckSDK.isPlacementReady(MyApplication.APPLUCK_PLACEMENT_ID)) {
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID, 600, 600, 1);
            btnOpen.setVisibility(View.VISIBLE);
            return;
        }
        HANDLER.postDelayed(() -> {
            showAppLuckPlacement();
        }, 1000);
    }

}