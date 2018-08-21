package utility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.rto_driving_test_rajasthan.R;
import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


/**
 * Base activity class. This may be useful in<br/>
 * Implementing google analytics or <br/>
 * Any app wise implementation
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static List<Activity> sActivities = new ArrayList<Activity>();
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        sActivities.add(this);
        Fabric.with(this, new Crashlytics());
//        logUser();
        //Mint.initAndStartSession(this.getApplication(), "622caf45");
        Mint.initAndStartSession(this.getApplication(), "4643ed9b");
    }
//    private void logUser() {
//        // TODO: Use the current user's information
//        // You can call any combination of these three methods
//        Crashlytics.setUserIdentifier("shyam03041");
//        Crashlytics.setUserEmail("shyam.yaduvanshi@cnvg.in");
//        Crashlytics.setUserName("Shyam Yaduvanshi");
//    }

    public void setAppBar(String title, boolean isBackVisible) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBackVisible);
        getSupportActionBar().setDisplayShowHomeEnabled(isBackVisible);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(title);
        //   existingMsg
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   hideSoftKeyboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  hideSoftKeyboard();
    }


    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        try {
//            InputMethodManager inputMethodManager = (InputMethodManager) this
//                    .getSystemService(Activity
//                            .INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken
//                    (), 0);
//        } catch (Exception e) {
//
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        super.onDestroy();
    }

    public void finishAllActivities() {
        if (sActivities != null) {
            for (Activity activity : sActivities) {
                if (Config.DEBUG) {
                    Log.d("BaseActivity", "finishAllActivities activity=" + activity.getClass()
                            .getSimpleName());
                }
                activity.finish();
            }
        }
    }

    public static void finishAllActivitiesStatic() {
        if (sActivities != null) {
            for (Activity activity : sActivities) {
                if (Config.DEBUG) {
                    Log.d("BaseActivity", "finishAllActivities activity=" + activity.getClass()
                            .getSimpleName());
                }
                activity.finish();
            }
        }
    }

    public String getAppString(int id) {
        String str = "";
        if (!TextUtils.isEmpty(this.getResources().getString(id))) {
            str = this.getResources().getString(id);
        } else {
            str = "";
        }
        return str;
    }

}
