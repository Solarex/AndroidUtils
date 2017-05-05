package com.solarexsoft.androidutilsdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.solarexsoft.androidutils.utils.Utils;
import com.solarexsoft.androidutilsdemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);
    }

    public void TestActivity(View view){
        startActivity(new Intent(this, TestActivity.class));
    }
}
