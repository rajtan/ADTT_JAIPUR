package com.rto_driving_test_rajasthan.Authorization;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.rto_driving_test_rajasthan.R;
import com.rto_driving_test_rajasthan.dao.AuthBfdCap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.ApiClient;
import utility.BaseActivity;
import utility.CommonFunctions;
import utility.Config;
import utility.MorphoTabletFPSensorDevice;

public class VerifyActivity extends BaseActivity implements AuthBfdCap, OnClickListener {
    @BindView(R.id.radiogroup)
    RadioGroup rg;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    String path = null;
    private Button btnVerify;
    private MorphoTabletFPSensorDevice fpSensorCap;

    private ImageView imgFP;
    boolean isWorking = false;
    RequestQueue requestQueue;
    String ll_no,dltest_seq,cov_cd,card_num,track_id,machine_id,cam_type="";
    int check=1;
    public boolean AuthComplete = false;
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);
        setAppBar("Authentication", true);
////String ll_no,dltest_seq,cov_cd,card_num,track_id,machine_id,cam_type="";
        Bundle bundle=getIntent().getExtras();
        ll_no=bundle.getString("ll_no");
        dltest_seq=bundle.getString("dltest_seq");
        cov_cd=bundle.getString("cov_cd");
        card_num=bundle.getString("card_num");
        track_id=bundle.getString("track_id");
        machine_id=bundle.getString("machine_id");
        cam_type=bundle.getString("cam_type");

        Log.e("BUNDEL_DATA",ll_no +"\n"+dltest_seq+"\n"+cov_cd+"\n"+card_num+"\n"+track_id+"\n"+machine_id+"\n"+cam_type);
        Log.e("LEFTTEMPLATEPATH",CommonFunctions.LEFTTEMPLATEPATH);
        requestQueue= Volley.newRequestQueue(this);
        initGUI();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.rb1:
                        check=1;
                        break;
                    case R.id.rb2:
                        check=2;
                        break;
                }
            }
        });
    }

    private void initGUI()
    {
        btnVerify = (Button) findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(this);
        imgFP = (ImageView) findViewById(R.id.imgFP);
        fpSensorCap = new MorphoTabletFPSensorDevice(this);
            fpSensorCap.open(this);
            fpSensorCap.setViewToUpdate(imgFP);

        AuthComplete = false;
        verify();
        Authenticate();
    }

    public int verifyFingerMatch(byte[] args)
    {
        int err = -1;
        if (path != null)
        {
            err = fpSensorCap.verifyMatchmy(args,args);
        }
        return err;
    }

    @Override
    public void onClick(View v)
    {
        if (v.equals(btnVerify))
        {
            AuthComplete = false;
            verify();
            Authenticate();
        }
    }

    private void verify()
    {
        imgFP.setImageBitmap(null);
    }

    @Override
    public void updateImageView(final ImageView im, final Bitmap bm, String mesg, final boolean flagComplete, final int captureError)
    {
        Log.e("CAPTUREERROR",""+captureError);
        runOnUiThread(new Runnable()

        {
            @Override
            public void run()
            {

                if (im != null)
                {
                    im.setImageBitmap(bm);
                }
                if (captureError == ErrorCodes.MORPHOERR_TIMEOUT)
                {
                    Toast.makeText(getApplicationContext(), "Capture Timeout", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (captureError == ErrorCodes.MORPHOERR_CMDE_ABORTED)
                {
                    return;
                }
                // int width = im.getWidth(), height = im.getHeight();
                if (flagComplete)
                {
                    if (path != null)
                    {
                        byte[] data = Base64.decode(path, Base64.DEFAULT);
                        Log.d("data",""+data);
                        Log.d("data",path);
                        final int err = verifyFingerMatch(data);
                        //final int err = verifyFingerMatch(CommonFunctions.pathToByte(path));
                        Log.e("temp",""+err);
                        CommonFunctions.alert(VerifyActivity.this, err, "Alert !", "");
                        path = null;
                        if (err == 0)
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
//                                    Toast.makeText(getApplicationContext(), "Successfully Captured", Toast.LENGTH_SHORT).show();

                         /*           Intent intent=new Intent(getApplicationContext(),ApplicantInfoActivity.class);
                                    intent.putExtra("","");
                                    //startActivity(new Intent(getApplicationContext(),ApplicantInfoActivity.class));
                                    finishAllActivities();*/

                                        getsocketapi();
                                }
                            });
                        }
                    }
                }
                handler.sendEmptyMessage(0);
            }

        });
    }

    private void getsocketapi() {
        //LLNO,DLTEST_SEQ ,COV_CD,CARD_NUMBER,TRACK_ID,MACHINE_ID,CAM_TYPE
        Log.e("BUNDEL_DATA",ll_no +"\n"+dltest_seq+"\n"+cov_cd+"\n"+card_num+"\n"+track_id+"\n"+machine_id+"\n"+cam_type);
        //String url1= ApiClient.BASE_URL+"ADTT_DataInter.svc/Set_ApplicationInfo/ll_no/dltest_seq/cov_cd/card_num/track_id/machine_id/cam_type";
        String url= ApiClient.BASE_URL+"ADTT_DataInter.svc/Set_ApplicationInfo/"+ll_no+"/"+dltest_seq+"/"+cov_cd+"/"+card_num+"/"+track_id+"/"+machine_id+"/"+cam_type;

        Log.e("MYSOCKETURL",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("SERVER IP",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("Data");
                    Log.e("jsonobject",jsonObject1.toString());
                    String machineip=jsonObject1.optString("MACHINE_IP");
                    Log.e("machineip",machineip);
                    Config.MACHINE_IP=machineip;
                   /* Intent intent=new Intent(getApplicationContext(),ApplicantInfoActivity.class);
                    intent.putExtra("act_type","VERIFY");
                    intent.putExtra("response","RESPONSE");
                    startActivity(intent);
                    finish();*/

                   onBackPressed();
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus)
            fpSensorCap.cancelLiveAcquisition();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        fpSensorCap.cancelLiveAcquisition();
    }
    String strRet = "";
    protected void Authenticate()
    {
        path = CommonFunctions.LEFTTEMPLATEPATH;
        /*if(check==1)
        {
            path = CommonFunctions.LEFTTEMPLATEPATH;
        }
        if(check==2)
        {
            path = CommonFunctions.RIGHTTEMPLATEPATH;
        }
        else {
            Toast.makeText(getApplicationContext(), "Please Select Thumb", Toast.LENGTH_SHORT).show();
        }*/

        if (path != null)
        {
            try
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            strRet = fpSensorCap.startCapture_New();
                        }
                        catch (Exception e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }.start();

//                Toast.makeText(getApplicationContext(), "Place your finger on the sensor ", Toast.LENGTH_LONG).show();
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*Intent intent=new Intent(getApplicationContext(),ApplicantInfoActivity.class);
        intent.putExtra("act_type","VERIFY");
        intent.putExtra("response","RESPONSE");
        startActivity(intent);
        finish();*/
        finish();

    }
}
