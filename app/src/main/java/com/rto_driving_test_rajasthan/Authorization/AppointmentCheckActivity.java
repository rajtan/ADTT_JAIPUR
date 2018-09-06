package com.rto_driving_test_rajasthan.Authorization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rto_driving_test_rajasthan.Models.Camtype;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.ApiClient;
import utility.BaseActivity;
import utility.CommonFunctions;
import utility.Config;
import utility.ConnectionDetector;


public class AppointmentCheckActivity extends BaseActivity {
    
    @BindView(R.id.proceedbtn_Appcheck)
    LinearLayout proceedbtn;

    @BindView(R.id.mTextField)
    TextView mTextField;
    ProgressDialog progressDialog=null;
    RequestQueue requestQueue;
    String trackid,testtype;
    SharedPreferences responsedata,vehtype,trackingid,trackpref;
    SharedPreferences.Editor editor;
    public static String MY_RESPONSE="response";
    public static String VEHICALTYPE="vehical_type";
    public static String TRACK_ID="track_id";
    public static String NEWTRACK="newtrack";
    CountDownTimer countDownTimer;
    boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_check);
        ButterKnife.bind(this);
        responsedata=getApplicationContext().getSharedPreferences(MY_RESPONSE, Context.MODE_PRIVATE);
        vehtype=getApplicationContext().getSharedPreferences(VEHICALTYPE, Context.MODE_PRIVATE);
        trackingid=getApplicationContext().getSharedPreferences(TRACK_ID, Context.MODE_PRIVATE);
        trackpref=getApplicationContext().getSharedPreferences(NEWTRACK,Context.MODE_PRIVATE);
        editor=responsedata.edit();

        //trackid=vehtype.getString("trackid","");
        trackid=trackpref.getString("tracknewid","");
        testtype=trackingid.getString("vehicaltype","");
        Log.e("VEHICAL TYPE",trackid+testtype);

       /* trackid=getIntent().getStringExtra("trackid");
        testtype=getIntent().getStringExtra("testtype");*/

        requestQueue= Volley.newRequestQueue(this);
        //timer();
        if (!ConnectionDetector.isConnected(getApplicationContext())) {

            Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();

        }else {

            userInfovoll();
        }

    }

    private void timer() {
        countDownTimer=new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                isRunning=true;
                mTextField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("WAIT!");
                //timer();
                isRunning=false;
                userInfovoll();
            }
        }.start();
    }

    @OnClick(R.id.proceedbtn_Appcheck)
    public void onclick(View v){
        switch (v.getId())
        {
            case R.id.proceedbtn_Appcheck:
               // startActivity(new Intent(getApplicationContext(),ApplicantInfoActivity.class));
                Toast.makeText(getApplicationContext(), "Going To Next", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfovoll() {
        progressDialog=new ProgressDialog(AppointmentCheckActivity.this);
        progressDialog.setMessage("PLEASE WAIT FETCHING APPLICANT INFORMATION");
        progressDialog.setTitle("TRYING TO FETCH");
        progressDialog.show();
        //http://192.168.1.229/ADTT_Data/
        Log.e("BASEURL", ApiClient.BASE_URL);
        //String myurl="http://192.168.1.229:1300/simpleserver/";
        String url = ApiClient.BASE_URL+"ADTT_DataInter.svc/Get_ApplicantInfo/"+trackid+"/"+testtype;
        Log.e("URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                if(progressDialog.isShowing())
                {
                    progressDialog.cancel();
                }


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String result=jsonObject.optString("Result");

                    Log.e("RESULT",result);

                    if(jsonObject.optString("Result").equals("null") || jsonObject.optString("Result").equals("") || jsonObject.optString("Result").equals(null) )
                    {

                     //countDownTimer.start();
                        timer();
                    }
                    else {

                            if(isRunning==true)
                            {
                                countDownTimer.cancel();
                            }
                        editor.putString("response_info",response);
                        editor.commit();

                        Intent intent=new Intent(getApplicationContext(),ApplicantInfoActivity.class);
                        /*intent.putExtra("response",response);
                        intent.putExtra("act_type","APPOINT_CHECK");*/
                        startActivity(intent);
                        finish();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
                if(progressDialog.isShowing())
                {
                    progressDialog.cancel();
                }
                timer();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        if(isRunning==true)
        {
            countDownTimer.cancel();
        }
        //countDownTimer.cancel();
    }
}
