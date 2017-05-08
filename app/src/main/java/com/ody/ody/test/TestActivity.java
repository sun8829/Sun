package com.ody.ody.test;

import android.app.Activity;
import android.os.Bundle;

import com.github.mzule.activityrouter.annotation.Router;
import com.ody.ody.R;

@Router("test")
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
