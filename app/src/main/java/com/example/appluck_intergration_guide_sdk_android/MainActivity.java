package com.example.appluck_intergration_guide_sdk_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.appluck.sdk.AppLuckSDK;

public class MainActivity extends AppCompatActivity {

    private final static Handler HANDLER = new Handler();

    private Button btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAppLuckPlacement();

        findViewById(R.id.btn_activity2).setOnClickListener((v) -> {
            final Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });
        btnOpen = findViewById(R.id.btn_open);
        btnOpen.setVisibility(View.GONE);
        btnOpen.setOnClickListener((v) -> {
            //插屏打开
            AppLuckSDK.openInteractiveAds(MyApplication.APPLUCK_PLACEMENT_ID, 1, 1);
        });
        findViewById(R.id.btn_show).setOnClickListener((v) -> {
            //APPLUCK show
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID, 1);
        });
        findViewById(R.id.btn_hide).setOnClickListener((v) -> {
            //APPLUCK hide
            AppLuckSDK.hideInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID);
        });
        findViewById(R.id.btn_show2).setOnClickListener(view -> {
            //APPLUCK show2
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID2, 1);
        });
        findViewById(R.id.btn_hide2).setOnClickListener(view -> {
            //APPLUCK hide2
            AppLuckSDK.hideInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID2);
        });
    }

    public void showAppLuckPlacement() {
        //通过坐标指定到屏幕位置
        /*if (AppLuckSDK.isPlacementReady(MyApplication.APPLUCK_PLACEMENT_ID)) {
            AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID, 100, 600, 1);
            btnOpen.setVisibility(View.VISIBLE);
            return;
        }*/

        //通过获取SDK View,加到自己的Layout上
        if (AppLuckSDK.isPlacementReady(MyApplication.APPLUCK_PLACEMENT_ID)) {
            LinearLayout llAddView = findViewById(R.id.ll_add_view);
            View iconView = AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID, 1);
            View iconView2 = AppLuckSDK.showInteractiveEntrance(this, MyApplication.APPLUCK_PLACEMENT_ID2, 1);
            if (iconView != null) {
                if (iconView.getParent() != null) {
                    ViewGroup viewGroup = (ViewGroup) iconView.getParent();
                    viewGroup.removeView(iconView);
                }
                if (iconView2.getParent() != null) {
                    ViewGroup viewGroup = (ViewGroup) iconView2.getParent();
                    viewGroup.removeView(iconView2);
                }
                iconView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iconView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llAddView.addView(iconView);
                llAddView.addView(iconView2);
            }
            btnOpen.setVisibility(View.VISIBLE);
            return;
        }
        HANDLER.postDelayed(() -> {
            showAppLuckPlacement();
        }, 1000);
    }

}