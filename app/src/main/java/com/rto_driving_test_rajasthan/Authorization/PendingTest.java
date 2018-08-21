package com.rto_driving_test_rajasthan.Authorization;

import android.os.Bundle;

import com.rto_driving_test_rajasthan.R;

import utility.BaseActivity;

public class PendingTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_test);

        setAppBar("Pending Test",true);
    }
}
