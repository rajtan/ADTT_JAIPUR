package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;


public class TrackSettingActivity extends BaseActivity {

    @BindView(R.id.et_track1)
    EditText track1;
    @BindView(R.id.et_track2)
    EditText track2;
    @BindView(R.id.et_track3)
    EditText track3;
    @BindView(R.id.btn_save)
    Button save;

    String mytrack[];
    String mytrack_key [];

    SharedPreferences tracksetting;
    public static String TRACK_PREF="trackpref";
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_setting);
        ButterKnife.bind(this);
        setAppBar("ADD TRACK", true);

        tracksetting=getApplicationContext().getSharedPreferences(TRACK_PREF, Context.MODE_PRIVATE);
        editor=tracksetting.edit();
    }

    @OnClick(R.id.btn_save)
    public void Onclick(View view) {

        switch (view.getId()) {
            case R.id.btn_save:
                addtrack();
                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
        }
    }

    private void addtrack() {
        String str1 = track1.getText().toString().trim();
        String str2 = track2.getText().toString().trim();
        String str3 = track3.getText().toString().trim();

        mytrack = new String[]{str1, str2, str3};
        mytrack_key=new String[]{"TRACK 1","TRACK 2","TRACK 3"};
        String mytrackarray=Arrays.toString(mytrack);
        Log.e("mytrackarray",mytrackarray);
        editor.putString("trackset",mytrackarray);
        editor.commit();

        for (int i = 0; i < mytrack.length; i++) {
            Log.e("MYTRACK", "" + mytrack[i]);


        }
        
        if(mytrack.length==0)
        {
            Toast.makeText(getApplicationContext(), "PLEASE SET ATLEAST ONE TRACK ID", Toast.LENGTH_SHORT).show();
            
        }else {

            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }

        

    }
}
