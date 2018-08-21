package com.rto_driving_test_rajasthan.Home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.Authorization.ActShowTrack;
import com.rto_driving_test_rajasthan.Authorization.LoginActivity;
import com.rto_driving_test_rajasthan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.ErrorLayout;

import static com.rto_driving_test_rajasthan.R.id.toolbar_default;
public class ActNewDashboard extends BaseActivity {

    @BindView(R.id.user_iv)
    ImageView userIv;
    @BindView(R.id.tv_home)
    TextView tvHome;

    @BindView(toolbar_default)
    Toolbar tootlbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rl_home)
    RelativeLayout rlHome;

    @BindView(R.id.rl_share)
    RelativeLayout rlshare;

    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.iv_share)
    ImageView ivShar;
    @BindView(R.id.iv_logout)
    ImageView ivLogout;

    private ErrorLayout errorLayout;

    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;

//    String userId = "", userEmail = "", userNm = "", userCity = "", userMobil = "", schNm = "", state = "", sExmDate = "", sQpcode = "", sQpNm = "", sCls = "", sNoofStu = "", sCenNo, curDate = "";
    int year,month,day;;

    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.act_new_dashboard);
        ButterKnife.bind(this);
        context = this;
        activity = this;
        setAppBar(getAppString(R.string.home_title), true);
        tootlbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        tvTitle.setText(getAppString(R.string.home_title));
        initViews();

        tootlbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }


    DrawerLayout drawer;

    public void setAppBar(String title, boolean isBackVisible) {
        Toolbar toolbar = (Toolbar) findViewById(toolbar_default);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBackVisible);
        getSupportActionBar().setDisplayShowHomeEnabled(isBackVisible);
//        getSupportActionBar().setTitle(title);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.drawable.logo, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                logout("Wish to exit ?", 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @OnClick({R.id.user_iv, R.id.tv_home, R.id.tv_share,
            R.id.rl_share, R.id.rl_home,R.id.rl_logout,R.id.btn_submit_detail})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_logout:
                try {
                    logout("Are you sure\n" +
                            "want to\n" +
                            " log out ?", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                drawer.closeDrawer(GravityCompat.START);
                rlHome.setBackgroundColor(getResources().getColor(R.color.white));

                rlshare.setBackgroundColor(getResources().getColor(R.color.white));

                rlLogout.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
                ivHome.setImageResource(R.drawable.home_icon);
                ivShar.setImageResource(R.drawable.share_app);
                ivLogout.setImageResource(R.drawable.logout_hover);
                break;
            case R.id.user_iv:
                break;
            case R.id.tv_home:
                drawer.closeDrawer(GravityCompat.START);
                SetSideBar(0);
                break;
//            case R.id.tv_contact:
//                SetSideBar(1);
//                drawer.closeDrawer(GravityCompat.START);
//                break;
            case R.id.tv_share:
                SetSideBar(2);
                drawer.closeDrawer(GravityCompat.START);
//                shareApp();
//                openBrowser(ActNewDashboard.this,"https://www.google.co.in/");
                break;
//            case R.id.tv_rate:
//                SetSideBar(3);
//                drawer.closeDrawer(GravityCompat.START);
//                break;


            case R.id.btn_submit_detail:
                startActivity(new Intent(ActNewDashboard.this,ActShowTrack.class));

                break;

        }
    }

    Dialog logoutDlg = null;

    public void logout(String mes, final int dif) {
        try {
            if (logoutDlg != null) {
                logoutDlg.cancel();
            }
            logoutDlg = new Dialog(ActNewDashboard.this, R.style.MyDialogTheme1);
            logoutDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            logoutDlg.setContentView(R.layout.logout_app);
            logoutDlg.setCanceledOnTouchOutside(false);
            TextView tvTitle = (TextView) logoutDlg.findViewById(R.id.exittext);
            tvTitle.setText(mes);
            Button no = (Button) logoutDlg.findViewById(R.id.No);
            Button yes = (Button) logoutDlg.findViewById(R.id.yes);
            Button divi = (Button) logoutDlg.findViewById(R.id.divide_button);
            logoutDlg.show();
            if (dif == 3) {
//                divi.setVisibility(View.GONE);
//                yes.setVisibility(View.GONE);
                no.setText("Ok");
            } else {
                divi.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                no.setText("Cancel");
            }
            yes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        if (dif == 1) {
                            String PREF_NAME = "SessionPref";
                            SharedPreferences sharedPreferences = ActNewDashboard.this.getSharedPreferences(
                                    PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            prefsEditor.clear();
                            prefsEditor.commit();
                            Intent i = new Intent(ActNewDashboard.this,
                                    LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        } else if (dif == 2) {
                            finish();
                        }
                        if (logoutDlg != null) {
                            logoutDlg.dismiss();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (logoutDlg != null) {
                        logoutDlg.cancel();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ToastShow(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public void SetSideBar(int i) {
        switch (i) {
            case 0:
                rlHome.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
//                rlCon.setBackgroundColor(getResources().getColor(R.color.white));
                rlshare.setBackgroundColor(getResources().getColor(R.color.white));
//                rlRate.setBackgroundColor(getResources().getColor(R.color.white));
//                rlIndore.setBackgroundColor(getResources().getColor(R.color.white));
//                rlOther.setBackgroundColor(getResources().getColor(R.color.white));
                rlLogout.setBackgroundColor(getResources().getColor(R.color.white));


//                tvHome.setTextColor(getResources().getColor(R.color.white));
//                tvCon.setTextColor(getResources().getColor(R.color.text_primary));
//                tvshare.setTextColor(getResources().getColor(R.color.text_primary));
//                tvRate.setTextColor(getResources().getColor(R.color.text_primary));
//                tvIndore.setTextColor(getResources().getColor(R.color.text_primary));
//                tvOther.setTextColor(getResources().getColor(R.color.text_primary));
                ivHome.setImageResource(R.drawable.home_hover);
                ivShar.setImageResource(R.drawable.share_app);
                ivLogout.setImageResource(R.drawable.logout);

                break;
            case 1:
                rlHome.setBackgroundColor(getResources().getColor(R.color.white));
//                rlCon.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
                rlshare.setBackgroundColor(getResources().getColor(R.color.white));
//                rlRate.setBackgroundColor(getResources().getColor(R.color.white));
//                rlIndore.setBackgroundColor(getResources().getColor(R.color.white));
//                rlOther.setBackgroundColor(getResources().getColor(R.color.white));
                rlLogout.setBackgroundColor(getResources().getColor(R.color.white));

//                tvHome.setTextColor(getResources().getColor(R.color.text_primary));
//                tvCon.setTextColor(getResources().getColor(R.color.white));
//                tvshare.setTextColor(getResources().getColor(R.color.text_primary));
//                tvRate.setTextColor(getResources().getColor(R.color.text_primary));
//                tvIndore.setTextColor(getResources().getColor(R.color.text_primary));
//                tvOther.setTextColor(getResources().getColor(R.color.text_primary));
                ivHome.setImageResource(R.drawable.home_icon);
                ivShar.setImageResource(R.drawable.share_hover);
                ivLogout.setImageResource(R.drawable.logout);

                break;
            case 2:
                rlHome.setBackgroundColor(getResources().getColor(R.color.white));
//                rlCon.setBackgroundColor(getResources().getColor(R.color.white));
                rlshare.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
//                rlRate.setBackgroundColor(getResources().getColor(R.color.white));
                rlLogout.setBackgroundColor(getResources().getColor(R.color.white));
//                rlIndore.setBackgroundColor(getResources().getColor(R.color.white));
//                rlOther.setBackgroundColor(getResources().getColor(R.color.white));

//                tvHome.setTextColor(getResources().getColor(R.color.text_primary));
//                tvCon.setTextColor(getResources().getColor(R.color.text_primary));
//                tvshare.setTextColor(getResources().getColor(R.color.white));
//                tvRate.setTextColor(getResources().getColor(R.color.text_primary));
//                tvIndore.setTextColor(getResources().getColor(R.color.text_primary));
//                tvOther.setTextColor(getResources().getColor(R.color.text_primary));

                ivHome.setImageResource(R.drawable.home_icon);
                ivShar.setImageResource(R.drawable.share_hover);
                ivLogout.setImageResource(R.drawable.logout);
                break;
            case 3:
                rlHome.setBackgroundColor(getResources().getColor(R.color.white));
//                rlCon.setBackgroundColor(getResources().getColor(R.color.white));
                rlshare.setBackgroundColor(getResources().getColor(R.color.white));
//                rlRate.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
//                rlIndore.setBackgroundColor(getResources().getColor(R.color.white));
//                rlOther.setBackgroundColor(getResources().getColor(R.color.white));

//                tvHome.setTextColor(getResources().getColor(R.color.text_primary));
//                tvCon.setTextColor(getResources().getColor(R.color.text_primary));
//                tvshare.setTextColor(getResources().getColor(R.color.text_primary));
//                tvRate.setTextColor(getResources().getColor(R.color.white));
//                tvIndore.setTextColor(getResources().getColor(R.color.text_primary));
//                tvOther.setTextColor(getResources().getColor(R.color.text_primary));


                break;

            case 4:
                rlHome.setBackgroundColor(getResources().getColor(R.color.white));
//                rlCon.setBackgroundColor(getResources().getColor(R.color.white));
                rlshare.setBackgroundColor(getResources().getColor(R.color.white));
//                rlRate.setBackgroundColor(getResources().getColor(R.color.white));
//                rlIndore.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
//                rlOther.setBackgroundColor(getResources().getColor(R.color.white));


//                tvHome.setTextColor(getResources().getColor(R.color.text_primary));
//                tvCon.setTextColor(getResources().getColor(R.color.text_primary));
//                tvshare.setTextColor(getResources().getColor(R.color.text_primary));
//                tvRate.setTextColor(getResources().getColor(R.color.text_primary));
//                tvIndore.setTextColor(getResources().getColor(R.color.white));
//                tvOther.setTextColor(getResources().getColor(R.color.text_primary));
                break;
            case 5:
                rlHome.setBackgroundColor(getResources().getColor(R.color.white));
//                rlCon.setBackgroundColor(getResources().getColor(R.color.white));
                rlshare.setBackgroundColor(getResources().getColor(R.color.white));
//                rlRate.setBackgroundColor(getResources().getColor(R.color.white));
//                rlIndore.setBackgroundColor(getResources().getColor(R.color.white));
//                rlOther.setBackgroundColor(getResources().getColor(R.color.home_bg_color));
//
//                tvHome.setTextColor(getResources().getColor(R.color.text_primary));
//                tvCon.setTextColor(getResources().getColor(R.color.text_primary));
//                tvshare.setTextColor(getResources().getColor(R.color.text_primary));
//                tvRate.setTextColor(getResources().getColor(R.color.text_primary));
//                tvIndore.setTextColor(getResources().getColor(R.color.text_primary));
//                tvOther.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                break;
        }
    }

}
