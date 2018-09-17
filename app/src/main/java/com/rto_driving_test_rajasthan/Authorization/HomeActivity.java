package com.rto_driving_test_rajasthan.Authorization;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.Config;
import utility.ConnectionDetector;
import utility.ErrorLayout;

public class HomeActivity extends BaseActivity {
    public static String MY_PREF = "ipadd";
    public static String VEHICAL_TYPE = "vehical_type";
    public static String TRACKID = "track_id";
    public static String TRACKIDARR = "trackpref";
    public static String NEWTRACK = "newtrack";
    /*public static String TR2 = "trackpref2";
    public static String TR3 = "trackpref3";*/


    public static String TRACKPREFONE = "trackprefone";
    public static String TRACKPREFTWO = "trackpreftwo";
    Context context;
    Activity activity;
    @BindView(R.id.bt_fresh)
    Button btfresh;
    @BindView(R.id.bt_retest)
    Button btretest;
    @BindView(R.id.rl_longclick)
    RelativeLayout rl_click;
    @BindView(R.id.proceed_btn)
    LinearLayout proceed;
    @BindView(R.id.ipaddress)
    TextView ipAdd;
    @BindView(R.id.ipaddresstext)
    TextView ipaddresstext;
    @BindView(R.id.trackid_txt)
    TextView trackid;
    @BindView(R.id.testtype_txt)
    TextView vehicaltype;
    @BindView(R.id.spin_trackid)
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    Toolbar toolbar;
    SharedPreferences sp, sp1, sp2, sp3, trackpref, trackprefone, trackpreftwo,spone,sptwo;
    SharedPreferences.Editor editor, trackeditor, onetrackeditor, twotrackeditor;
    String trackidvalu;
    String testState;
    public static String MYTESTPREF="testtrack";

    public String MYPREFTWO="machineipsock";

    String track_id, testtype;

    String myspinvalue[];

    ArrayList<String> mylist;

    String myvalue[];


    /*
    * editortrackid.putString("trackset", str1);
        editortrackid.commit();

        trackeditor2.putString("trackset2",str2);
        trackeditor2.commit();


        trackeedtor3.putString("trackset3",str3);
        trackeedtor3.commit();
    *
    * */
    Dialog logoutDlg = null;
    private ErrorLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_diff_applicants);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_diff);
        toolbar.setTitle("AUTOMATED DRIVING TEST RAJASTHAN ");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mylist = new ArrayList<>();
        sp = getApplicationContext().getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        sp1 = getApplicationContext().getSharedPreferences(VEHICAL_TYPE, Context.MODE_PRIVATE);
        sp2 = getApplicationContext().getSharedPreferences(TRACKID, Context.MODE_PRIVATE);
        sp3 = getApplicationContext().getSharedPreferences(TRACKIDARR, Context.MODE_PRIVATE);
        trackpref = getApplicationContext().getSharedPreferences(NEWTRACK, Context.MODE_PRIVATE);
        trackprefone = getApplicationContext().getSharedPreferences(TRACKPREFONE, Context.MODE_PRIVATE);
        trackpreftwo = getApplicationContext().getSharedPreferences(TRACKPREFTWO, Context.MODE_PRIVATE);
        spone = getApplicationContext().getSharedPreferences(MYTESTPREF, Context.MODE_PRIVATE);
        sptwo = getApplicationContext().getSharedPreferences(MYPREFTWO, Context.MODE_PRIVATE);
    /*    trackpref1 = getApplicationContext().getSharedPreferences(TR2, Context.MODE_PRIVATE);
        trackpref2 = getApplicationContext().getSharedPreferences(TR3, Context.MODE_PRIVATE);*/

        testState=spone.getString("teststate","");
        editor = sp1.edit();
        trackeditor = trackpref.edit();
        onetrackeditor=trackprefone.edit();
        twotrackeditor=trackpreftwo.edit();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setAppBar(getAppString(R.string.select_applic), true);
        initViews();
        context = this;
        activity = this;


        testtype = sp2.getString("vehicaltype", "");
        track_id = sp1.getString("trackid", "");
        ipAdd.setText(sp.getString("ipaddress", ""));
        trackid.setText(sp1.getString("trackid", ""));
        String strtype = sp2.getString("vehicaltype", "");
        if (strtype.equalsIgnoreCase("FW")) {
            vehicaltype.setText("FOUR WHEELER");
        } else if (strtype.equalsIgnoreCase("TW")) {
            vehicaltype.setText("TWO WHEELER");
        } else {
            vehicaltype.setText("NOT SELECTED");
        }


        String trackdata = sp3.getString("trackset", "");
        String trackdataone=trackprefone.getString("tracksetone","");
        String trackdatatwo=trackpreftwo.getString("tracksettwo","");

        Log.e("TRACK1",trackdata);
        Log.e("TRACK2",trackdataone);
        Log.e("TRACK3",trackdatatwo);

        //myvalue=new String[]{trackdata};

        if(TextUtils.isEmpty(trackdata) && TextUtils.isEmpty(trackdataone) && TextUtils.isEmpty(trackdatatwo))
        {
            myvalue=new String[]{"SELECT TRACK"};
        }
        else if(TextUtils.isEmpty(trackdataone))
        {
            if(TextUtils.isEmpty(trackdatatwo)){
                myvalue=new String[]{trackdata};
                myspinner();
            }
            else {
                myvalue=new String[]{trackdata,trackdatatwo};
                myspinner();
            }

        }
        else if(TextUtils.isEmpty(trackdatatwo)){
            myvalue=new String[]{trackdata,trackdataone};
            myspinner();
        }
        else if(TextUtils.isEmpty(trackdata) && TextUtils.isEmpty(trackdataone)){
            myvalue=new String[]{trackdatatwo};
        }
        else if(TextUtils.isEmpty(trackdataone) && TextUtils.isEmpty(trackdatatwo)){
            myvalue=new String[]{trackdataone};
            myspinner();
        }
        else if(TextUtils.isEmpty(trackdatatwo) && TextUtils.isEmpty(trackdata)){
            myvalue=new String[]{trackdatatwo};
        }
        else {
            myvalue=new String[]{trackdata,trackdataone,trackdatatwo};
            myspinner();
        }



        /*String trackdata1 = trackpref1.getString("trackset2", "");
        String trackdata2 = trackpref2.getString("trackset3", "");*/
        //myvalue=new String[]{trackdata,trackdata1,trackdata2};
        //myspinner();
        //Log.e("TRACKDATATA??????", trackdata + "\n" + trackdata1 + "\n" + trackdata2);

        /*if ((!trackdata.equals(null) || !trackdata.equals("")) && (!trackdata1.equals(null) || !trackdata1.equals("")) && (!trackdata2.equals(null) || !trackdata2.equals(""))) {
            myvalue = new String[]{trackdata, trackdata1, trackdata2};
            myspinner();
        } else if ((!trackdata.equals(null) || !trackdata.equals("")) && (!trackdata1.equals(null) || !trackdata1.equals(""))) {
            myvalue = new String[]{trackdata, trackdata1};
            myspinner();
        } else if (!trackdata.equals(null) || trackdata.equals("")) {
            myvalue = new String[]{trackdata};
            myspinner();
        } else {
            myvalue = new String[]{};

        }*/


/*
            if(!TextUtils.isEmpty(trackdata)){

            }
            else if((!TextUtils.isEmpty(trackdata)) && (!TextUtils.isEmpty(trackdata1))){
                myvalue=new String[]{trackdata,trackdata1};
            }
            else if((!TextUtils.isEmpty(trackdata)) && (!TextUtils.isEmpty(trackdata1)) && (!TextUtils.isEmpty(trackdata2))){
                myvalue=new String[]{trackdata,trackdata1,trackdata2};
            }
            else {
                //myvalue=new String[]{};
            }*/




/*
            try {
                JSONArray jsonArray=new JSONArray(trackdata);
                for (int i=0;i<jsonArray.length();i++){

                    mylist.add(jsonArray.getString(i));
                    Log.e("myspinvalue",mylist.get(i));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(mylist.size()!=0)
            {
                if(mylist.size()==1)
                {
                    myspinvalue=new String[]{"TRACK 1"};
                }
                else if(mylist.size()==2){
                    myspinvalue=new String[]{"TRACK 1","TRACK 2"};
                }
                else if(mylist.size()==3){
                    myspinvalue=new String[]{"TRACK 1","TRACK 2","TRACK 3"};
                }

            }
            else {

                    myspinvalue=new String[]{"SELECT TRACK "};

            }
*/
        //Log.e("MYVALUE",""+myvalue.length);


        rl_click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                startActivity(new Intent(getApplicationContext(), ChangeIPActivity.class));
                finish();
                return false;
            }
        });

    }

   /* @OnLongClick(R.id.rl_longclick)
    public void rl_longclick()
    {
        Toast.makeText(getApplicationContext(),"click long press",Toast.LENGTH_LONG).show();

        startActivity(new Intent(getApplicationContext(),ChangeIPActivity.class));

        return ;


    }*/

    public void myspinner() {


        if (myvalue.length != 0) {

            String myval1= "";
            String myval2= "";
            try
            {
                if(!myvalue[0].equalsIgnoreCase("")){
                    myval1 = myvalue[0].toString();
                }else if (!myvalue[1].equalsIgnoreCase("")){
                    myval2 = myvalue[1].toString();
                }
                else if (!myvalue[0].equalsIgnoreCase("") && !myvalue[1].equalsIgnoreCase("")){
                    myval1 = myvalue[0].toString();
                    myval2 = myvalue[1].toString();
                    Log.e("myvalue[0]",""+myvalue[0]);
                    Log.e("myvalue[1]",""+myvalue[1]);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            if(myval1.equalsIgnoreCase("RJ1401") && myval2.equalsIgnoreCase("")){
                myspinvalue = new String[]{"TRACK 1"};
            }
            else if(myval1.equalsIgnoreCase("RJ1402") && myval2.equalsIgnoreCase("")){
                myspinvalue = new String[]{"TRACK 2"};
            }
            else if(myval1.equalsIgnoreCase("RJ1401") && myval2.equalsIgnoreCase("RJ1402")){
                myspinvalue = new String[]{"TRACK 1","TRACK 2"};
            }
            else {
                myspinvalue = new String[]{"SELECT TRACK "};

            }

           /* if (!TextUtils.isEmpty(myval1) && TextUtils.isEmpty(myval2)) {
                myspinvalue = new String[]{"TRACK 1"};
            } else if (!TextUtils.isEmpty(myval2) && TextUtils.isEmpty(myval1)) {
                myspinvalue = new String[]{"TRACK 2"};
            }
            else if(!TextUtils.isEmpty(myval1) && !TextUtils.isEmpty(myval2)){
                myspinvalue = new String[]{"TRACK 1","TRACK 2"};
            }
            else {

                myspinvalue = new String[]{"SELECT TRACK "};

            }*/

        } else {
            Log.e("VALUE IS 0", "VALUE 0");
        }

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.spin_track, myspinvalue);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String value = spinner.getSelectedItem().toString();
                Log.e("SPINITEM", value);
                Log.e("myvalue[i].toString();", myvalue[i].toString());

                if (value.equalsIgnoreCase("SELECT TRACK")) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT TRACK", Toast.LENGTH_SHORT).show();
                }
                else {
                    trackidvalu=myvalue[i].toString();
                }
                /*else if (value.equalsIgnoreCase("TRACK 1")) {

                    //Log.e("MYLISTVALUE",mylist.get(0));
                    trackidvalu = myvalue[0];

                      *//*  editor.putString("trackid",tr1);
                        editor.commit();*//*

                } else if (value.equalsIgnoreCase("TRACK 2")) {

                    //Log.e("MYLISTVALUE",mylist.get(1));
                    trackidvalu = myvalue[1];
                        *//*editor.putString("trackid",tr2);
                        editor.commit();*//*

                } else if (value.equalsIgnoreCase("TRACK 3")) {

                    //Log.e("MYLISTVALUE",mylist.get(2));
                    trackidvalu = myvalue[2];

                }*/

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        callAPI();
    }

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
       /* rl_click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getApplicationContext(),"click long press",Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(),ChangeIPActivity.class));
                return true;
            }
        });*/
    }

    @OnClick({R.id.bt_fresh, R.id.bt_retest, R.id.proceed_btn})/*,R.id.rl_longclick*/
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_fresh:

                if (validation()) {
                    Intent in = new Intent(context, ActAppoimentList.class);
                    startActivity(in);
                }
                break;
            case R.id.bt_retest:
                if (validation()) {
                    Intent in1 = new Intent(context, RetestActivity.class);
                    in1.putExtra("app_id", "1");
                    startActivity(in1);
                }
                break;

            case R.id.proceed_btn:
                if (validation()) {

                    if(testState.equalsIgnoreCase("NO")){
                        Config.MACHINE_IP=sptwo.getString("socket_machinip","");

                        startActivity(new Intent(getApplicationContext(),ApplicantInfoActivity.class));

                        finishAllActivities();
                    }
                    else {
                        Log.e("trackidvalu", trackidvalu);
                        trackeditor.putString("tracknewid", trackidvalu);
                        trackeditor.commit();


                        Intent intent = new Intent(getApplicationContext(), AppointmentCheckActivity.class);
                        intent.putExtra("trackid", track_id);
                        intent.putExtra("testtype", testtype);
                        startActivity(intent);
                        finish();
                        Config.MACHINE_IP = "";



                        //startActivity(new Intent(getApplicationContext(),AppointmentCheckActivity.class));
                        // Toast.makeText(getApplicationContext(), "Welcome to Check Today Appointment", Toast.LENGTH_SHORT).show();


                    }


                }

            /*case R.id.rl_longclick:
                Toast.makeText(getApplicationContext(),"click long press",Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(),ChangeIPActivity.class));
                break;*/


        }


    }

    private boolean validation() {

        if (TextUtils.isEmpty(sp.getString("ipaddress", ""))) {
            Toast.makeText(getApplicationContext(), "Please Set IP Address", Toast.LENGTH_LONG).show();
            return false;
        }
        /*else if(TextUtils.isEmpty(sp1.getString("trackid",""))){
            Toast.makeText(getApplicationContext(),"PLEASE SET TRACK ID",Toast.LENGTH_LONG).show();
            return false;
        }*/
        else if (TextUtils.isEmpty(sp2.getString("vehicaltype", ""))) {
            Toast.makeText(getApplicationContext(), "PLEASE SET VEHICAL TYPE", Toast.LENGTH_LONG).show();
            return false;
        } else if (!ConnectionDetector.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            logout("Wish to exit ?", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logout(String mes, final int dif) {
        try {
            if (logoutDlg != null) {
                logoutDlg.cancel();
            }
            logoutDlg = new Dialog(HomeActivity.this, R.style.MyDialogTheme1);
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
                        if (dif == 2) {
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
           *//* case R.id.action_add:
                //addSomething();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
                return true;*//*
            case R.id.action_settings:
                //startSettings();
                startActivity(new Intent(getApplicationContext(),TrackSettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
