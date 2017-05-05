package com.solarexsoft.androidutilsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.solarexsoft.androidutilsdemo.activities.TestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void TestActivity(View view){
        startActivity(new Intent(this, TestActivity.class));
    }
}
