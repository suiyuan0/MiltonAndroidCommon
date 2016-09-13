package com.milton.common.demo.activity.animations;

import android.os.Bundle;

import com.milton.common.demo.R;


public class DoorActivity extends AnimatedDoorActivity {

    @Override
    protected int layoutResId() {
        return R.layout.activity_animation_door;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
