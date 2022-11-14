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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnActivity2 = findViewById(R.id.btn_activity2);
        btnActivity2.setOnClickListener((v) -> {
            final Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });

        showAppLuckPlacement();
    }

    public void showAppLuckPlacement() {
        if (AppLuckSDK.isPlacementReady(MyApplication.APPLUCK_PLACEMENT_ID)) {
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID, 600, 600);
            return;
        }
        HANDLER.postDelayed(() -> {
            showAppLuckPlacement();
        }, 1000);
    }

}