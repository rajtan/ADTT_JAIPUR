package com.rto_driving_test_rajasthan.Authorization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rto_driving_test_rajasthan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.ErrorLayout;

public class ActShowTrack extends BaseActivity {
    Context context;
    Activity activity;

    @BindView(R.id.bt_two_wheeler)
    Button btTwoWheel;
    @BindView(R.id.bt_four_wheeler)
    Button btFourWheel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show_track);
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.show_track), true);
        initViews();
        context = this;
        activity = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
//        callAPI();
    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    @OnClick({R.id.bt_two_wheeler,R.id.bt_four_wheeler})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_four_wheeler:
                Intent in = new Intent(context,ActTestReport.class);
                    startActivity(in);
                break;
            case R.id.bt_two_wheeler:
                Intent in1 = new Intent(context,ActTestReport.class);
                startActivity(in1);
                break;


        }
    }


}
