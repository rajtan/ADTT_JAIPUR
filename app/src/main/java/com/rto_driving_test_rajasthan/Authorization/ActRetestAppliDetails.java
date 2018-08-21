package com.rto_driving_test_rajasthan.Authorization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.rto_driving_test_rajasthan.Functions;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.BaseRequest;
import retrofit.RequestReciever;
import utility.Base64Decode;
import utility.Config;
import utility.ErrorLayout;
import utility.MyImage;
import utility.ResizeImage;

public class ActRetestAppliDetails extends MediaPickerActivity {
    Context context;
    Activity activity;

    @BindView(R.id.btn_start_test)
    Button btSartTest;
    @BindView(R.id.btn_cancel_retest)
    Button btCancel;
    @BindView(R.id.view_diff)
    View viewDiff;
    @BindView(R.id.iv_date)
    ImageView img;


    @BindView(R.id.tv_name)
    TextView nametxt;
    @BindView(R.id.tv_id)
    TextView idtxt;
    @BindView(R.id.tv_ap_no)
    TextView ap_no_txt;
    @BindView(R.id.tv_nm)
    TextView nm_tv_txt;
    @BindView(R.id.tv_fath_nm)
    TextView fath_nm_txt;
    @BindView(R.id.tv_dob)
    TextView tv_dob_txt;
    @BindView(R.id.tv_dl_no)
    TextView tv_dl_txt;
    @BindView(R.id.tv_date)
    TextView tv_date_txt;
    @BindView(R.id.tv_time)
    TextView tv_time_txt;
    /*@BindView(R.id.tv_vehicle_class)
    TextView vehical_txt;*/







    String imagepath="";
    String path="";

    String photostatus,sDiff="";
    String nameApp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_retest_appli_details);
        ButterKnife.bind(this);
        sDiff=getIntent().getStringExtra("diff");
        photostatus=getIntent().getStringExtra("photostatus");
        nameApp=getIntent().getStringExtra("name");
        if(sDiff.equals("fresh") && (photostatus.equalsIgnoreCase("N")) )
        {

            setAppBar("CANDIDATE DETAIL (FRESH)", true);
        }
        else {
            setAppBar("CANDIDATE DETAIL (RETEST)", true);
        }


//        setAppBar(getAppString(R.string.retest_details), true);
//        setAppBar(getAppString(R.string.retest_details), true);

        //setAppBar(name,true);
        initViews();
        context = this;
        activity = this;
        checkClick=0;

        //Toast.makeText(getApplicationContext(),""+nameApp,Toast.LENGTH_LONG).show();

        setTextValue();

        if(sDiff.equals("fresh") && (photostatus.equalsIgnoreCase("N")) )
        {
            btSartTest.setText(getResources().getString(R.string.next));
            btCancel.setVisibility(View.VISIBLE);
            viewDiff.setVisibility(View.VISIBLE);
        }
        else if(sDiff.equalsIgnoreCase("fresh") && (photostatus.equalsIgnoreCase("Y")))
        {

            img.setImageBitmap(Config.APPLICANT_PIC_BASE64);
            btSartTest.setText(getResources().getString(R.string.start_test));
            btCancel.setVisibility(View.GONE);
            viewDiff.setVisibility(View.GONE);

        }
        else
        {
          img.setImageBitmap(Config.APPLICANT_PIC_BASE64);
            btSartTest.setText(getResources().getString(R.string.start_test));
            btCancel.setVisibility(View.GONE);
            viewDiff.setVisibility(View.GONE);
        }


       /* String s=Config.IMAGE_PATH;
        encodeImage(s);

        ResizeImage resize= new ResizeImage(ActRetestAppliDetails.this);


        //resize.compressImage(s);
        String comPath= resize.compressImage(s);
        Bitmap photo = BitmapFactory.decodeFile(comPath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 10, stream);
        byte[] byteArray = stream.toByteArray();
        imagepath = Base64Decode.encodeBytes(byteArray);*/

    }

    private String encodeImage(String path)
    {

        if(!TextUtils.isEmpty(path)) {
            File imagefile = new File(path);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);
            //Base64.de
            imagepath = encImage;
            return encImage;
        }
        else {
            return null;
        }


    }

    private void setTextValue() {


        nametxt.setText(Config.APPLICANT_FIRST_NAME+"\t"+Config.APPLICANT_LAST_NAME);
        idtxt.setText(Config.REF_NUMBER);
        ap_no_txt.setText(Config.RECEIPT_NUMBER);
        nm_tv_txt.setText(Config.APPLICANT_FIRST_NAME+"\t"+Config.APPLICANT_LAST_NAME);
        fath_nm_txt.setText(Config.SOWODO);
        tv_dob_txt.setText(Config.DOB);
        tv_dl_txt.setText(Config.LICENCE_NUMBER);
        tv_dob_txt.setText(Config.DOB);
      //  tv_date_txt.setText(Config.APPOINTMENT_DATE);


        String dob=Config.DOB;

        String dnt=Config.APPOINTMENT_DATE;

        if(!TextUtils.isEmpty(Config.APPOINTMENT_DATE))
        {
            String items[] = dnt.split(" ");
            tv_date_txt.setText(items[0]);
            if(items[1]!=null && items[2]!=null) {
                tv_time_txt.setText(items[1] + items[2]);
            }
        }
       else {

            tv_date_txt.setText("Not Updated");
            tv_time_txt.setText("Not Updated");

        }


        if(!TextUtils.isEmpty(Config.APPOINTMENT_DATE)) {
            String items[] = dob.split(" ");
            tv_dob_txt.setText(items[0]);
            if(items[1]!=null && items[2]!=null) {
                tv_dob_txt.setText(items[0] );
            }
        }
        else {

            tv_date_txt.setText("Not Updated");
            tv_time_txt.setText("Not Updated");

        }







    }

    @Override
    protected void onResume() {
        super.onResume();
//        callAPI();

    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    @OnClick({R.id.btn_start_test,R.id.btn_cancel_retest})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start_test:

                if(sDiff.equals("fresh") && photostatus.equalsIgnoreCase("N"))
                {
                    if(checkClick==0)
                    {
                        PickMedia(MediaPicker.Camera);

                    }
                    else
                    {


                        String s=Config.IMAGE_PATH;
                        encodeImage(s);

                        ResizeImage resize= new ResizeImage(ActRetestAppliDetails.this);


                        //resize.compressImage(s);
                        String comPath= resize.compressImage(s);
                        Bitmap photo = BitmapFactory.decodeFile(comPath);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 10, stream);
                        byte[] byteArray = stream.toByteArray();
                        imagepath = Base64Decode.encodeBytes(byteArray);

                        byte[] decodedString = Base64.decode(imagepath, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        Config.APPLICANT_PIC_BASE64=decodedByte;

                        callApi();



                    }

                }
                else if (sDiff.equalsIgnoreCase("retest"))
                {
                    Intent in = new Intent(context,VehicalSelectionActivity.class);
                    in.putExtra("type","retest");
                    startActivity(in);
                    finish();
                }

                else  if (sDiff.equalsIgnoreCase("fresh") && photostatus.equalsIgnoreCase("Y"))
                {

                    Intent in = new Intent(context,VehicalSelectionActivity.class);
                    in.putExtra("dataobject","");
                    in.putExtra("type","pending");
                    in.putExtra("picstatus",photostatus);
                    startActivity(in);
                    finish();

                }
                break;
            case R.id.btn_cancel_retest:

                finish();

                break;


        }
    }



    BaseRequest baseRequest;

    private void callApi() {


        baseRequest=new BaseRequest(this);

        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

               // Toast.makeText(getApplicationContext(),object.toString(),Toast.LENGTH_LONG).show();

                if(object!=null) {

                    try {
                        JSONArray data=(JSONArray)object;

                        JSONObject jsonObject=data.getJSONObject(0);
                        String img=jsonObject.getString("APPLICANT_PIC");

                        if(!TextUtils.isEmpty(img))
                        {
                            Intent in = new Intent(context,VehicalSelectionActivity.class);
                            in.putExtra("dataobject",jsonObject.toString());
                            in.putExtra("type","fresh");
                            startActivity(in);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_LONG).show();
                        }

                        /*Intent in1 = new Intent(context, ResultActivity.class);
                        in1.putExtra("dataobject",jsonObject.toString());

                        startActivity(in1);*/





                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Alredy submit",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

                Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();

            }
        });

        JsonObject jsonObject= Functions.getClient().getJsonMapObject("RTOCODE",Config.RTO_CODE,
                "DriverNo", String.valueOf(Config.DRIVER_NUMBER),
                "Receipt_No",Config.RECEIPT_NUMBER,
                "Ref_Number",Config.REF_NUMBER,
                "LL_NO",Config.LICENCE_NUMBER,
                "picture", imagepath);


        baseRequest.callAPIPost(1,jsonObject,"Get_Applicant_list.svc/Get_SaveApplicantBio/Get_BioInfo");


    }

    int checkClick=0;
    @Override
    protected void onSingleImageSelected(int starterCode, File fileUri, String imagPath, Bitmap bitmap, String compressPath) {
        if (!TextUtils.isEmpty(compressPath)) {
            path=compressPath; //camera
            checkClick=1;
            btSartTest.setText(getResources().getString(R.string.start_test));
            MyImage.displayImage(this,path,img);
            Config.IMAGE_PATH=path;





        }
    }

    @Override
    protected void onVideoCaptured(String videoPath) {

    }
    @Override
    protected void onMediaPickCanceled(MediaPicker reqCode) {
    }
}
