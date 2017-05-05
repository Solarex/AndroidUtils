package com.solarexsoft.androidutilsdemo.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 05/05/2017
 *    Desc:
 * </pre>
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        doTestStuff();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void doTestStuff();
}
