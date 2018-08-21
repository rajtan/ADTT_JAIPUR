package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rto_driving_test_rajasthan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.ErrorLayout;

public class ActResultOld extends BaseActivity {

    Context context;
    @BindView(R.id.toolbar_default)
    Toolbar tootlbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.act_result);
        context = this;
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.result), true);
        tootlbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        tvTitle.setText(getAppString(R.string.result));
        tootlbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ActShowRoute.this,LoginActivity.class));
                finish();
            }
        });
        initViews();
    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    @OnClick({})//R.id.login_btn
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.login_btn:
//                getMobi = mobileEt.getText().toString();
//                if (validation()) {
//                    Intent in = new Intent(context,ActNewDashboard.class);
//                    startActivity(in);
//
////                    if (Utility.isConnectingToInternet(context)) {
////                        new PostLoginData(context).execute();
////                    } else {
////                        Utility.showToast(context, "Please Check your internet connection");
////                    }
//                }
//                break;

        }
    }

}
