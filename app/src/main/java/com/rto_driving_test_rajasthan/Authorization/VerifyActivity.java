package com.rto_driving_test_rajasthan.Authorization;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    String ll_no,dltest_seq,cov_cd,card_num,track_id,machine_id,camip="";
    String cam_type="0";
    int check=1;
    public boolean AuthComplete = false;
    Handler handler = new Handler(Looper.getMainLooper());

    SharedPreferences sp,spone,sptwo,spthree;
    public String MYPREF="testtrack";
    public String MYPREFONE="typetest";
    public String MYPREFTWO="machineipsock";
    public String MYPREFTHREE="camiptxt";
    SharedPreferences.Editor editor,editorone,editortwo,editorthree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);
        setAppBar("Authentication", true);

        sp=getApplicationContext().getSharedPreferences(MYPREF,MODE_PRIVATE);
        spone=getApplicationContext().getSharedPreferences(MYPREFONE,MODE_PRIVATE);
        sptwo=getApplicationContext().getSharedPreferences(MYPREFTWO,MODE_PRIVATE);
        spthree=getApplicationContext().getSharedPreferences(MYPREFTHREE,MODE_PRIVATE);
        editor=sp.edit();
        editorone=spone.edit();
        editortwo=sptwo.edit();
        editorthree=spthree.edit();

////String ll_no,dltest_seq,cov_cd,card_num,track_id,machine_id,cam_type="";
        Bundle bundle=getIntent().getExtras();
        ll_no=bundle.getString("ll_no");
        dltest_seq=bundle.getString("dltest_seq");
        cov_cd=bundle.getString("cov_cd");
        card_num=bundle.getString("card_num");
        track_id=bundle.getString("track_id");
        machine_id=bundle.getString("machine_id");
        cam_type=bundle.getString("cam_type");
        camip=bundle.getString("selectedcamip");

        Log.e("BUNDEL_DATA",ll_no +"\n"+dltest_seq+"\n"+cov_cd+"\n"+card_num+"\n"+track_id+"\n"+machine_id+"\n"+cam_type);
        Log.e("LEFTTEMPLATEPATH",CommonFunctions.LEFTTEMPLATEPATH);

       /* editor.putString("teststate","NO");
        editor.commit();
        editorone.putString("type","TW");
        editorone.commit();
        editortwo.putString("socket_machinip","192.168.10.115");
        editortwo.commit();
        Config.MACHINE_IP="192.168.10.115";
          editorthree.putString("camip",camip);
            editorthree.commit();

        onBackPressed();*/

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


                                        /* for testing only  start*/
                                    /*editor.putString("teststate","NO");
                                    editor.commit();
                                    editorone.putString("type","TW");
                                    editorone.commit();
                                    editortwo.putString("socket_machinip","192.168.10.115");
                                    editortwo.commit();
                                    editorthree.putString("camip","");
                                    editorthree.commit();
                                    Config.MACHINE_IP="192.168.10.115";


                                    onBackPressed();*/

                                    /* for testing only  end*/
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

    public void getsocketapi() {

       /* Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = "Current Time when AUTHORISE APICALL : " + mdformat.format(calendar.getTime());
        createtext(strDate);*/


        String url="";
        //LLNO,DLTEST_SEQ ,COV_CD,CARD_NUMBER,TRACK_ID,MACHINE_ID,CAM_TYPE
        Log.e("BUNDEL_DATA",ll_no +"\n"+dltest_seq+"\n"+cov_cd+"\n"+card_num+"\n"+track_id+"\n"+machine_id+"\n"+cam_type);
        //String url1= ApiClient.BASE_URL+"ADTT_DataInter.svc/Set_ApplicationInfo/ll_no/dltest_seq/cov_cd/card_num/track_id/machine_id/cam_type";
        if(cam_type.equalsIgnoreCase("") || cam_type.equalsIgnoreCase(null)){
            cam_type="0";
            url= ApiClient.BASE_URL+"ADTT_DataInter.svc/Set_ApplicationInfo/"+ll_no+"/"+dltest_seq+"/"+cov_cd+"/"+card_num+"/"+track_id+"/"+machine_id+"/"+cam_type;
            createtext("TWO WHEELER URL"+url);

            editor.putString("teststate","NO");
            editor.commit();
            editorone.putString("type","TW");
            editorone.commit();
            editorthree.putString("camip","");
            editorthree.commit();

        }
        else {
            url= ApiClient.BASE_URL+"ADTT_DataInter.svc/Set_ApplicationInfo/"+ll_no+"/"+dltest_seq+"/"+cov_cd+"/"+card_num+"/"+track_id+"/"+machine_id+"/"+cam_type;
            editor.putString("teststate","NO");
            editor.commit();
            editorone.putString("type","FW");
            editorone.commit();
            editorthree.putString("camip",camip);
            editorthree.commit();
        }


        //createtext(url);
        Log.e("MYSOCKETURL",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("SERVER IP",response);
                /*Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = "Current Time when AUTHORISE RESPONSE : " + mdformat.format(calendar.getTime());
                createtext(strDate);*/

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("Data");
                    Log.e("jsonobject",jsonObject1.toString());
                    String machineip=jsonObject1.optString("MACHINE_IP");
                    Log.e("machineip",machineip);
                    Config.MACHINE_IP=machineip;


                    editortwo.putString("socket_machinip",machineip);
                    editortwo.commit();

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

    private void createtext(String url) {

        //String url="http://192.168.20.40:1300/simpleserver/machineip";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date now = new Date();
        //String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt
        String fileName = "ADTT_JAIPUR_VERIFYTIMELOG" + ".txt";//like 2016_01_12.txt


        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"ADTT_JAIPUR_TIME_AUTH_LOG", "Log Files");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(url+"\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }
}
