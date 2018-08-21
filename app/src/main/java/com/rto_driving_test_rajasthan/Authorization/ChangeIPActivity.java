package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.ApiClient;
import retrofit.BaseRequest;
import retrofit.RequestReciever;
import utility.BaseActivity;

public class ChangeIPActivity extends BaseActivity {


    public static String MY_PREF = "ipadd";
    public static String VEHICAL_TYPE = "vehical_type";
    public static String TRACKID = "track_id";
    public static String TRACK_PREF = "trackpref";
    public static String TRACK_PREFONE = "trackprefone";
    public static String TRACK_PREFTWO = "trackpreftwo";


/*    public static String TRAACK2PREF = "trackpref2";
    public static String TRACK3PREF = "trackpref3";*/


    @BindView(R.id.edit_ip)
    EditText ipEdit;
    @BindView(R.id.et_track)
    EditText trackET;
    @BindView(R.id.btn_set)
    LinearLayout btn_set;
    String ip, trackid, veh_type;
    BaseRequest baseRequest = null;
    SharedPreferences sp, sp1, sp2, sptrack2, sptrack3;
    SharedPreferences.Editor editor, editor1, editor2, trackeditor2, trackeedtor3;
    String typeveh;
    String[] value = {"SELECT VEHICAL TYPE", "FOUR WHEELER", "TWO WHEELER"};
    ArrayAdapter arrayAdapter;
    @BindView(R.id.spi_type)
    Spinner spinner_type;
    String type;
    @BindView(R.id.et_track1)
    EditText track1;
    @BindView(R.id.et_track2)
    EditText track2;
    @BindView(R.id.et_track3)
    EditText track3;
    @BindView(R.id.btn_save)
    Button save;
    String mytrack[];
    String mytrack_key[];

    SharedPreferences tracksetting, tracksettingone, tracksettingtwo;
    SharedPreferences.Editor editortrackid, editortrackidone, editortrackidtwo;

    ArrayList<String> liststr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip);

        ButterKnife.bind(this);

        arrayAdapter = new ArrayAdapter(this, R.layout.spin_item, value);
        spinner_type.setAdapter(arrayAdapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //type=spinner_type.getSelectedItem().toString();
                // Log.e("SPINNER_TYPE",type);
                String typespin = spinner_type.getSelectedItem().toString();
                // String type=
                //String [] value={"SELECT VEHICAL TYPE","FOUR WHEELER","TWO WHEELER"};
                if (typespin.equalsIgnoreCase("SELECT VEHICAL TYPE")) {
                    type = "SELECT VEHICAL TYPE";
                } else if (typespin.equalsIgnoreCase("FOUR WHEELER")) {
                    type = "FW";
                } else if (typespin.equalsIgnoreCase("TWO WHEELER")) {
                    type = "TW";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp = getApplicationContext().getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();
        sp1 = getApplicationContext().getSharedPreferences(VEHICAL_TYPE, Context.MODE_PRIVATE);
        editor1 = sp1.edit();

        tracksetting = getApplicationContext().getSharedPreferences(TRACK_PREF, Context.MODE_PRIVATE);
        editortrackid = tracksetting.edit();

        tracksettingone = getApplicationContext().getSharedPreferences(TRACK_PREFONE, Context.MODE_PRIVATE);
        editortrackidone = tracksettingone.edit();

        tracksettingtwo = getApplicationContext().getSharedPreferences(TRACK_PREFTWO, Context.MODE_PRIVATE);
        editortrackidtwo = tracksettingtwo.edit();


        sp2 = getApplicationContext().getSharedPreferences(TRACKID, Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        /*sptrack2 = getApplicationContext().getSharedPreferences(TRAACK2PREF, Context.MODE_PRIVATE);
        trackeditor2 = sptrack2.edit();

        sptrack3 = getApplicationContext().getSharedPreferences(TRACK3PREF, Context.MODE_PRIVATE);
        trackeedtor3 = sptrack3.edit();*/


        String strIP = sp.getString("ipaddress", "");
        String trackdata = tracksetting.getString("trackset", "");

        String trackdataone=tracksettingone.getString("tracksetone","");
        String trackdatatwo=tracksettingtwo.getString("tracksettwo","");

        Log.e("TRACKONE",trackdata);
        Log.e("TRACKTWO",trackdataone);
        Log.e("TRACKTHREE",trackdatatwo);

        if(TextUtils.isEmpty(trackdata) && TextUtils.isEmpty(trackdataone) && TextUtils.isEmpty(trackdatatwo))
        {
            //myvalue=new String[]{"SELECT TRACK"};
            Log.e("BLANK","All Field Blank");
        }
        else if(TextUtils.isEmpty(trackdataone))
        {
            if(TextUtils.isEmpty(trackdatatwo)){
                track1.setText(trackdata);
            }
            else {
                track1.setText(trackdata);
                track3.setText(trackdatatwo);
            }

        }
        else if(TextUtils.isEmpty(trackdatatwo)){

            track1.setText(trackdata);
            track2.setText(trackdataone);
        }
        else if(TextUtils.isEmpty(trackdata) && TextUtils.isEmpty(trackdataone)){
            //myvalue=new String[]{trackdatatwo};
            track3.setText(trackdatatwo);
        }
        else if(TextUtils.isEmpty(trackdataone) && TextUtils.isEmpty(trackdatatwo)){

            track2.setText(trackdataone);
        }
        else if(TextUtils.isEmpty(trackdatatwo) && TextUtils.isEmpty(trackdata)){
            //myvalue=new String[]{trackdatatwo};
        }
        else {
            /*myvalue=new String[]{trackdata,trackdataone,trackdatatwo};
            myspinner();*/
            track1.setText(trackdata);
            track2.setText(trackdataone);
            track3.setText(trackdatatwo);
        }



        String vehicalset = sp2.getString("vehicaltype", "");


       /* if(!TextUtils.isEmpty(trackst)){
            track1.setText(trackst);
        }
        else if(!TextUtils.isEmpty(trackst) && !TextUtils.isEmpty(tracktype1)){
            track1.setText(trackst);
            track2.setText(tracktype1);
        }

        else if(!TextUtils.isEmpty(trackst) && !TextUtils.isEmpty(tracktype1) || !TextUtils.isEmpty(tracktype2)){
            track1.setText(trackst);
            track2.setText(tracktype1);
            track3.setText(tracktype2);

        }*/
        //Log.e("VEHICALL>>>",vehicalset);

        if (!TextUtils.isEmpty(strIP)) {
            ipEdit.setText("" + strIP);
        }


        liststr = new ArrayList<>();


        Log.e("VEHICALL>>>", vehicalset);
        if (vehicalset.equals("SELECT VEHICAL TYPE")) {
            spinner_type.setSelection(0);
        } else if (vehicalset.equals("FW")) {
            spinner_type.setSelection(1);
            Log.e("VEHICALL>>>", vehicalset);
        } else if (vehicalset.equals("TW")) {
            spinner_type.setSelection(2);
        }


        ip = ipEdit.getText().toString();
        trackid = trackET.getText().toString();


        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // baseRequest=new BaseRequest(getApplicationContext());
                ApiClient.BASE_URL = null;
                //Config.IP_ADDRESS=ipEdit.getText().toString();

                String s = ipEdit.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    editor.putString("ipaddress", s);
                    editor.commit();
                }


                String track = trackET.getText().toString();
               /* editor1.putString("trackid",track);
                editor1.commit();*/

                String veh = type;
                editor2.putString("vehicaltype", veh);
                editor2.commit();


                ApiClient.BASE_URL = "http://" + sp.getString("ipaddress", "") + "/ADTT_Data/";
//                BaseRequest baseRequest_list=new BaseRequest(ChangeIPActivity.this);
                //  callApi();

                if (TextUtils.isEmpty(track1.getText().toString()) && TextUtils.isEmpty(track2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), " ENTER TRACK 1 AND TRACK 2", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(track1.getText().toString()) && TextUtils.isEmpty(track3.getText().toString())) {
                    Toast.makeText(getApplicationContext(), " ENTER TRACK 1 AND TRACK 3", Toast.LENGTH_SHORT).show();
                } else {
                    addtrack();
                    Toast.makeText(getApplicationContext(), "New IP configuration set up" + "\n" + ApiClient.BASE_URL, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }


            }
        });

    }

    //    BaseRequest baseRequest_list=null;
    private void callApi() {

        BaseRequest baseRequest_list = new BaseRequest(ChangeIPActivity.this);
        baseRequest_list.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();

            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            baseRequest_list.callAPIGET(1, new ArrayMap<String, String>(), "Get_Applicant_list.svc/Get_Applicant/");
        }

    }

    public void removeNullsFrom(@Nullable JSONArray array) throws JSONException {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {

                String str = array.getString(i);
                Log.e("STRT", str);
            }
        }
    }

    private void addtrack() {
        String str1 = track1.getText().toString().trim();
        String str2 = track2.getText().toString().trim();
        String str3 = track3.getText().toString().trim();

        mytrack = new String[]{str1, str2, str3};
        mytrack_key = new String[]{"TRACK 1", "TRACK 2", "TRACK 3"};
        String mytrackarray = Arrays.toString(mytrack);
        Log.e("mytrackarray", mytrackarray);

        editortrackid.putString("trackset", str1);
        editortrackid.commit();

        editortrackidone.putString("tracksetone", str2);
        editortrackidone.commit();

        editortrackidtwo.putString("tracksettwo", str3);
        editortrackidtwo.commit();

       /* trackeditor2.putString("trackset2",str2);
        trackeditor2.commit();


        trackeedtor3.putString("trackset3",str3);
        trackeedtor3.commit();*/



        /*String myvar1=sptrack2.getString("trackset2","");
        String myvar2=sptrack3.getString("trackset3","");

        Log.e("MYVALR",myvar1+"\n"+myvar2);*/



       /* for (int i = 0; i < mytrack.length; i++) {
            Log.e("MYTRACK", "" + mytrack[i]);


        }*/

      /*  if(mytrack.length==0)
        {
            Toast.makeText(getApplicationContext(), "PLEASE SET ATLEAST ONE TRACK ID", Toast.LENGTH_SHORT).show();

        }else {

           *//* startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();*//*
        }*/


    }
}
