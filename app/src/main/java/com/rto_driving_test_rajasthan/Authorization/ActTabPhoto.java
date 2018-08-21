package com.rto_driving_test_rajasthan.Authorization;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rto_driving_test_rajasthan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;

public class ActTabPhoto extends BaseActivity {
    Context context;
    Activity activity;

    @BindView(R.id.pager)
    ViewPager pager;

    ViewPagerAdapter adapter;
    @BindView(R.id.tabs)
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Captured", "Non Captured"};//,"Collection"
    int Numboftabs = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_screen);
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.tab_photo), true);
//        initViews();
        context = this;
        activity = this;
        tabs.setEnabled(false);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        // Assigning ViewPager View and setting the adapter
//        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        // Assiging the Sliding Tab Layout View
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        callAPI();
    }

//    private ErrorLayout errorLayout;

//    private void initViews() {
//        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
//    }

    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.bt_four_wheeler:
//                Intent in = new Intent(context,ActTestReport.class);
//                    startActivity(in);
//                break;
//            case R.id.bt_two_wheeler:
//                Intent in1 = new Intent(context,ActTestReport.class);
//                startActivity(in1);
//                break;


        }
    }


}
