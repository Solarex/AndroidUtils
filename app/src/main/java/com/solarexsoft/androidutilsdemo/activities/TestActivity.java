package com.solarexsoft.androidutilsdemo.activities;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solarexsoft.androidutils.utils.ActivityUtils;
import com.solarexsoft.androidutilsdemo.BuildConfig;
import com.solarexsoft.androidutilsdemo.R;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 05/05/2017
 *    Desc:
 * </pre>
 */

public class TestActivity extends BaseActivity {
    private static final String TAG = "TestActivity";
    private TextView tv;
    private Button btn;
    private String pkgName;
    private String clsName;

    @Override
    public int getLayoutId() {
        return R.layout.test_activity;
    }

    @Override
    public void initView() {
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
    }

    @Override
    public void doTestStuff() {
        pkgName = getPackageName();
        clsName = pkgName + ".activities.MainActivity";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "doTestStuff: pkgName = " + pkgName + ",clsName: " + clsName);
        }
        tv.setText("MainActivity isExists: " + ActivityUtils.isActivityExists(pkgName, clsName));
        tv.append("\nLaunchActivity: " + ActivityUtils.getLaunchActivity(pkgName));
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                tv.append("\nTopActivity: " + ActivityUtils.getTopActivity());
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.launchActivity(pkgName, clsName);
            }
        });
    }
}
