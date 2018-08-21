package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.BaseRequest;
import retrofit.RequestReciever;
import utility.BaseActivity;
import utility.Config;
import utility.ErrorLayout;

public class RetestActivity extends BaseActivity {
    @BindView(R.id.et_reference)
    EditText etRef;

    @BindView(R.id.et_receipt)
    EditText etRecei;

    Context context;
    String getMobi = "";

String sAppId="";

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public static String MY_PREF="ipadd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.act_retest_login);
        context = this;
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.login_title), true);
        initViews();
        sAppId=getIntent().getStringExtra("app_id");

        sp=getApplicationContext().getSharedPreferences(MY_PREF,Context.MODE_PRIVATE);

    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    @OnClick({R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                getMobi = etRef.getText().toString();
                if (validation()) {

                   /* Intent in = new Intent(context,ActRetestAppliDetails.class);
                    in.putExtra("diff","retest");
                    startActivity(in);*/

                  callApi();

                }
                break;

        }
    }


    BaseRequest baseRequest;
    private void callApi() {


        String ref_id="/"+etRef.getText().toString();
        String receipt="/"+etRecei.getText().toString();

        String url="Get_Applicant_list.svc/Get_Pending_recordsfor_retest"+ref_id+receipt;

        baseRequest=new BaseRequest(this);

        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {





                    JSONArray data=(JSONArray)object;

                    try {
                        if(data.length()>0)
                        {

                        JSONObject jsonObject=data.getJSONObject(0);

                        //jsonObject.getString("");




                        String img=jsonObject.getString("APPLICANT_PIC");

                        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //app_pic.setImageBitmap(decodedByte);

                        Config.APPLICANT_PIC_BASE64=decodedByte;
                        Config.REF_NUMBER=jsonObject.getString("Reference_Number");
                        Config.RECEIPT_NUMBER=jsonObject.getString("Receipt_Number");
                        Config.APPLICANT_FIRST_NAME=jsonObject.getString("Applicant_Name");
                        Config.APPLICANT_LAST_NAME=jsonObject.getString("Applicant_Last_Name");
                        Config.SOWODO=jsonObject.getString("So_Wo_Do");
                        Config.DOB=jsonObject.getString("Date_Of_Birth");
                        Config.LICENCE_NUMBER=jsonObject.getString("Licence_Number");
                        Config.TESTTYPE=jsonObject.optString("TEST_TYPE");
                        Config.DRIVER_NUMBER= Integer.parseInt(jsonObject.optString("DRIVER_NUMBER"));
                        Config.RTO_CODE=jsonObject.optString("RTO_CODE");

                        // Config.USERDETAILS="RETEST";
                        Intent in = new Intent(context,ActRetestAppliDetails.class);
                        in.putExtra("diff","retest");
                        in.putExtra("activityname","RETEST");
                        startActivity(in);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Data Not Found",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Toast.makeText(getApplicationContext(),""+message ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {
                Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();

            }
        });


            baseRequest.callAPIGET(1,new ArrayMap<String, String>(),url);


    }

    private boolean validation() {
         if (TextUtils.isEmpty(etRef.getText().toString().trim()))
        {
            errorLayout.showAlert(getAppString(R.string.enter_digit), ErrorLayout.MsgType.Error);
            return false;

        }
         else  if (TextUtils.isEmpty(etRecei.getText().toString().trim()))
         {
             errorLayout.showAlert(getAppString(R.string.reciept_no), ErrorLayout.MsgType.Error);
             return false;

         }
         else {
            return true;
        }
    }


}
