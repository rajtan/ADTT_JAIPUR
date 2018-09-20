package com.rto_driving_test_rajasthan.Authorization;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rto_driving_test_rajasthan.Models.Camtype;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.CommonFunctions;
import utility.Config;
import utility.ConnectionDetector;
import utility.HttpHandler;


public class ApplicantInfoActivity extends BaseActivity {
    public static String MY_RESPONSE = "response";
    public static String VEHICALTYPE = "track_id";
    public String MYPREF = "testtrack";
    public String MYPREFVEHICAL = "typetest";
    public String MYPREFTWO = "machineipsock";
    public String MYPREFTHREE = "camiptxt";
    RequestQueue requestQueue;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.iv_date)
    ImageView imageView;
    @BindView(R.id.spi_cam)
    Spinner spDist;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> ad;
    ArrayAdapter<String> adapterterminal;
    ArrayList<String> ad_ter;
    ArrayList<String> machineip;
    @BindView(R.id.spi_ter)
    Spinner spinnerterminal;
    @BindView(R.id.tv_app)
    TextView tv_app;
    @BindView(R.id.tv_dob)
    TextView tv_dob;
    @BindView(R.id.biometricLin)
    LinearLayout biometric;
    @BindView(R.id.start_btn)
    LinearLayout startbtn;
    @BindView(R.id.start_img)
    ImageView imgstart;
    @BindView(R.id.start_imgbike)
    ImageView imgstartbike;
    @BindView(R.id.selectcam_card)
    CardView selectCamcardView;
    @BindView(R.id.btncard)
    CardView btncardvi;
    @BindView(R.id.card_termin)
    CardView selectterminal;
    @BindView(R.id.homelin)
    LinearLayout linearhome;
    @BindView(R.id.selectterminal_txt)
    TextView sel_ter_txt;
    @BindView(R.id.msg_lin)
    LinearLayout msgliner;
    @BindView(R.id.txt_terminal)
    TextView teminaltext;
    @BindView(R.id.txt_camip)
    TextView selectcam;
    @BindView(R.id.selectcam_txt)
    TextView selectCamTXT;
    ProgressDialog progressDialog = null;
    String myresponse, act_type;
    //LLNO,DLTEST_SEQ ,COV_CD,CARD_NUMBER,TRACK_ID,MACHINE_ID,CAM_TYPE
    String ll_no, dltest_seq, cov_cd, card_num, track_id, machine_id, cam_type, camip = "";
    String macnip = "";
    String mcamip = "";
    SharedPreferences responsedata, vehstatus, trackstaus, vehicaltype, sptwo, spthree;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editortrack, editortwo;
    String response_infodata;

    int check = 0;
    String vehtype = "";
    Dialog logoutDlg = null;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_info);
        ButterKnife.bind(this);
        setAppBar("Applicant Information", true);
        responsedata = getApplicationContext().getSharedPreferences(MY_RESPONSE, Context.MODE_PRIVATE);
        vehstatus = getApplicationContext().getSharedPreferences(VEHICALTYPE, Context.MODE_PRIVATE);
        trackstaus = getApplicationContext().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        vehicaltype = getApplicationContext().getSharedPreferences(MYPREFVEHICAL, Context.MODE_PRIVATE);
        sptwo = getApplicationContext().getSharedPreferences(MYPREFTWO, Context.MODE_PRIVATE);
        spthree = getApplicationContext().getSharedPreferences(MYPREFTHREE, Context.MODE_PRIVATE);
        editortrack = trackstaus.edit();
        editortwo = sptwo.edit();
        String trackst = trackstaus.getString("teststate", "");
        String vehicalt = vehicaltype.getString("type", "");


        response_infodata = responsedata.getString("response_info", "");
        vehtype = vehstatus.getString("vehicaltype", "");
        Log.e("VEHICAL TYPE", vehtype);



        if (trackst.equalsIgnoreCase("NO") && vehicalt.equalsIgnoreCase("FW")) {

            imgstartbike.setVisibility(View.GONE);
            startbtn.setVisibility(View.VISIBLE);
            imgstart.setVisibility(View.VISIBLE);
            spinnerterminal.setVisibility(View.GONE);
            teminaltext.setText(Config.MACHINE_IP);
            teminaltext.setVisibility(View.VISIBLE);
            mcamip = spthree.getString("camip", "");
            selectcam.setText("" + mcamip);
            spDist.setVisibility(View.GONE);
            selectcam.setVisibility(View.VISIBLE);


        } else if (trackst.equalsIgnoreCase("NO") && vehicalt.equalsIgnoreCase("TW")) {

            imgstart.setVisibility(View.GONE);
            imgstartbike.setVisibility(View.VISIBLE);
            selectCamcardView.setVisibility(View.GONE);
            selectCamTXT.setVisibility(View.GONE);
            spinnerterminal.setVisibility(View.GONE);
            teminaltext.setText(Config.MACHINE_IP);
            teminaltext.setVisibility(View.VISIBLE);


            selectcam.setVisibility(View.GONE);


        } else {

            Log.e("trackstatus", "TRACK STATUS IS NULL");

            teminaltext.setText(Config.MACHINE_IP);
            teminaltext.setVisibility(View.GONE);
            spinnerterminal.setVisibility(View.VISIBLE);
            selectcam.setVisibility(View.GONE);
        }

        Toast.makeText(getApplicationContext(), "vehical type"+vehtype.toString(), Toast.LENGTH_SHORT).show();

        if (vehtype.equalsIgnoreCase("FW")) {
            imgstartbike.setVisibility(View.GONE);
            imgstart.setVisibility(View.VISIBLE);
           /* imgstart.setVisibility(View.GONE);
            imgstartbike.setVisibility(View.VISIBLE);*/

        } else if (vehtype.equalsIgnoreCase("TW")) {

            imgstart.setVisibility(View.GONE);
            imgstartbike.setVisibility(View.VISIBLE);
            selectCamcardView.setVisibility(View.GONE);
            selectCamTXT.setVisibility(View.GONE);
        } else {

            imgstart.setVisibility(View.VISIBLE);
            imgstartbike.setVisibility(View.VISIBLE);
            selectCamcardView.setVisibility(View.VISIBLE);
            selectCamTXT.setVisibility(View.VISIBLE);

        }






        //act_type=getIntent().getStringExtra("act_type");
        //response=getIntent().getStringExtra("response");
        //Log.e("act_type",act_type);

        Log.e("MACHINE IP", Config.MACHINE_IP);
       /* if(TextUtils.isEmpty(Config.MACHINE_IP))
        {
            startbtn.setVisibility(View.GONE);
            *//*startbtn.setVisibility(View.VISIBLE);*//*
        }
        else {
            startbtn.setVisibility(View.VISIBLE);
        }*/

        requestQueue = Volley.newRequestQueue(this);
        if (!ConnectionDetector.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
        } else {
            userInfovoll(response_infodata);

            /*Log.e("RESPONSE_DATA",response);
            if(act_type.equals("APPOINT_CHECK"))
            {

            }
            else {
                userInfovoll(response_infodata);

            }*/

        }
    }

    @OnClick({R.id.biometricLin, R.id.start_btn, R.id.homelin})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.biometricLin:
                //startActivity(new Intent(getApplicationContext(),ApplicantInfoActivity.class));
                //String ll_no,dltest_seq,cov_cd,card_num,track_id,machine_id,cam_type="";
                Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ll_no", ll_no);
                bundle.putString("dltest_seq", dltest_seq);
                bundle.putString("cov_cd", cov_cd);
                bundle.putString("card_num", card_num);
                bundle.putString("track_id", track_id);
                bundle.putString("machine_id", machine_id);
                bundle.putString("cam_type", cam_type);
                bundle.putString("leftthumb", cam_type);
                bundle.putString("rightthumb", cam_type);
                bundle.putString("selectedcamip", camip);
                /*intent.putExtra("leftthumb", CommonFunctions.LEFTTEMPLATEPATH);
                intent.putExtra("rightthumb",CommonFunctions.RIGHTTEMPLATEPATH);*/
                intent.putExtras(bundle);
                startActivity(intent);
                //finish();

                break;
            case R.id.homelin:
                onBackPressed();
                break;

            case R.id.start_btn:

                /*Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = "Current Time when Button Click : " + mdformat.format(calendar.getTime());
                createtext(strDate);*/

                if (!TextUtils.isEmpty(Config.MACHINE_IP)) {

                    //callSOCKETAPI();
                    //callSOCKETAPIHTTP();

                   /* editortwo.clear();
                    editortwo.commit();
                    startActivity(new Intent(getApplicationContext(),AppointmentCheckActivity.class));
                    finish();*/

                    webViewSocketApi();


                }
                /*else if(trackstaus.getString("teststate","").equalsIgnoreCase("NO")){
                    callSOCKETAPI();
                }*/
                else {
                    Toast.makeText(getApplicationContext(), "PLEASE AUTHONTICATE APPLICANT", Toast.LENGTH_SHORT).show();

                }

        }
    }

    private void callSOCKETAPIHTTP() {

        new StartTest().execute();
    }

    public void webViewSocketApi() {

        String url = "http://" + Config.MACHINE_IP + ":1300" + "/simpleserver/";

        Ion.with(getApplicationContext()).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {

                if (!TextUtils.isEmpty(result)) {
                    String output = stripHtml(result);

                    StringTokenizer stringTokenizer = new StringTokenizer(output, "#");
                    String value = stringTokenizer.nextToken();
                    String value1 = stringTokenizer.nextToken();
                    int myval = Integer.parseInt(value1);
                    if (myval == 0) {
                        Toast.makeText(ApplicantInfoActivity.this, "TEST NOT TRIGGER", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AppointmentCheckActivity.class));
                        finish();
                        //Config.MACHINE_IP="";
                        editortrack.putString("teststate", "YES");
                        editortrack.commit();

                        editortwo.putString("socket_machinip", "");
                        editortwo.commit();

                        Config.MACHINE_IP = "";
                    }

                }


            }
        });

    }

    private void callSOCKETAPI() {

        String url = "http://" + macnip + ":1300" + "/simpleserver/";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = "Current Time when CALL API : " + mdformat.format(calendar.getTime());
        // createtext(strDate);


        //String url=ApiClient.BASE_URL+"simpleserver/"+Config.MACHINE_IP;
        // String url=newurl+"simpleserver/"+Config.MACHINE_IP;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = "Current Time when RESPONSE : " + mdformat.format(calendar.getTime());
                createtext(strDate);*/


                StringTokenizer stringTokenizer = new StringTokenizer(stripHtml(response), "#");
                String value = stringTokenizer.nextToken();
                String value1 = stringTokenizer.nextToken();
                int myval = Integer.parseInt(value1);
                if (myval == 0) {
                    Toast.makeText(ApplicantInfoActivity.this, "TEST NOT TRIGGER", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AppointmentCheckActivity.class));
                    finish();
                    Config.MACHINE_IP = "";
                    editortrack.putString("teststate", "YES");
                    editortrack.commit();

                    // Config.MACHINE_IP="";
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), "SERVER RESPONSE" + message, Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), "SERVER RESPONSE" + message, Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), "SERVER RESPONSE" + message, Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), "SERVER RESPONSE" + message, Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), "SERVER RESPONSE" + message, Toast.LENGTH_SHORT).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), "SERVER RESPONSE" + message, Toast.LENGTH_SHORT).show();
                }

            }
        });
        requestQueue.add(stringRequest);
    }

    private void createtext(String url) {

        //String url="http://192.168.20.40:1300/simpleserver/machineip";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date now = new Date();
        //String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt
        String fileName = "ADTT_JAIPUR_TIME_LOG" + ".txt";//like 2016_01_12.txt


        try {
            File root = new File(Environment.getExternalStorageDirectory() + File.separator + "ADTT_JAIPUR", "Log Files");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(url + "\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    private void userInfovoll(String response1) {
        Log.e("RESPONSE DATA", response1);
        try {
            JSONObject jsonObject = new JSONObject(response1);
            Log.e("JSONOBJECT1", jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("Result");
            JSONArray jsonArray = jsonObject1.getJSONArray("Applicant_Data");
            final JSONArray dashcam = jsonObject1.getJSONArray("DashBoard_Camlist");
            final JSONArray teminalarr = jsonObject1.getJSONArray("Terminal_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                //LLNO,DLTEST_SEQ ,COV_CD,CARD_NUMBER,TRACK_ID,MACHINE_ID,CAM_TYPE
                ll_no = jsonObject2.optString("APPLNO");
                dltest_seq = jsonObject2.optString("DLTEST_SEQ");
                cov_cd = jsonObject2.optString("COV_CD");
                card_num = jsonObject2.optString("CARD_NUMBER");
                tv_name.setText("" + jsonObject2.getString("FIRST_NAME"));
                tv_id.setText("" + jsonObject2.getString("LLNO"));
                String imgbyte = jsonObject2.getString("IMAGE");
                tv_app.setText("" + jsonObject2.optString("APPLNO"));
                tv_dob.setText("" + jsonObject2.optString("DOB"));
                CommonFunctions.LEFTTEMPLATEPATH = jsonObject2.optString("THUMB");
                Log.e("LEFTTEMPLATEPATH", CommonFunctions.LEFTTEMPLATEPATH);
                byte[] decodedString = Base64.decode(imgbyte, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Config.APPLICANT_PIC_BASE64 = decodedByte;
                imageView.setImageBitmap(decodedByte);
            }
            Camtype camtype = null;
            ad = new ArrayList<>();
            //ad.add("SELECT DASHBOARD CAM");
            Log.e("DASHBOARD CAMERA ARRAY", "" + dashcam.length());
            if (dashcam != null) {
                for (int j = 0; j < dashcam.length(); j++) {
                    JSONObject jsonObject2 = dashcam.getJSONObject(j);
                    String listcam = jsonObject2.optString("CAM_IP");
                    Log.e("LISTCAM", listcam);
                    ad.add(listcam);
                }
                Log.e("listcam", "" + ad.size());
                Log.e("jsonArray", jsonArray.toString());
            } else {
                Log.e("DASHBOARD CAMLIST", "DASHBOARD CAM IS NULL BECAUSE ITS BIKE TEST");
            }
            ad_ter = new ArrayList<>();
            machineip = new ArrayList<>();
            //ad_ter.add("SELECT TERMINAL");
            for (int k = 0; k < teminalarr.length(); k++) {
                JSONObject ter = teminalarr.getJSONObject(k);
                String testtu = ter.optString("MACHINE_ID");
                String machine_ip = ter.optString("MACHINE_IP");
                machineip.add(machine_ip);
                ad_ter.add(testtu);
            }
            arrayAdapter = new ArrayAdapter<String>(ApplicantInfoActivity.this, R.layout.spin_item, ad);
            spDist.setAdapter(arrayAdapter);

            if (ad_ter.size() > 0) {
                msgliner.setVisibility(View.GONE);
                biometric.setVisibility(View.VISIBLE);
                adapterterminal = new ArrayAdapter<String>(ApplicantInfoActivity.this, R.layout.spin_item, ad_ter);
                spinnerterminal.setAdapter(adapterterminal);
            } else {


                biometric.setVisibility(View.GONE);
                selectCamcardView.setVisibility(View.INVISIBLE);
                selectterminal.setVisibility(View.INVISIBLE);
                selectCamTXT.setVisibility(View.INVISIBLE);
                sel_ter_txt.setVisibility(View.INVISIBLE);
                btncardvi.setVisibility(View.INVISIBLE);
                msgliner.setVisibility(View.VISIBLE);


            }


            spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    try {
                        JSONObject jsonObject2 = dashcam.getJSONObject(i);
                        Log.e("CAM TYPE", jsonObject2.optString("CAM_TYPE"));
                        cam_type = jsonObject2.optString("CAM_TYPE");
                        camip = jsonObject2.optString("CAM_IP");
                        Log.e("CAMIP", camip);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerterminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String machineID = spinnerterminal.getSelectedItem().toString();
                    machine_id = spinnerterminal.getSelectedItem().toString();
                    macnip = machineip.get(i);
                    Log.e("machineippp>>>", macnip);
                    Log.e("MACHINE_ID", machineID);

                    try {
                        JSONObject terminal = teminalarr.getJSONObject(i);
                        track_id = terminal.optString("TRACK_ID");
                        Log.e("TRACK", track_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*progressDialog=new ProgressDialog(ApplicantInfoActivity.this);
        progressDialog.setMessage("PLEASE WAIT FETCHING APPLICANT INFORMATION");
        progressDialog.setTitle("TRYING TO FETCH");
        progressDialog.show();
        //http://192.168.1.229/ADTT_Data/
        Log.e("BASEURL",ApiClient.BASE_URL);
        String myurl="http://192.168.1.229:1300/simpleserver/";
        String url = ApiClient.BASE_URL+"ADTT_DataInter.svc/Get_ApplicantInfo/RJ1401/FW";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                if(progressDialog.isShowing())
                {
                    progressDialog.cancel();
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
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(Config.MACHINE_IP)) {
            startbtn.setVisibility(View.GONE);
            /*startbtn.setVisibility(View.VISIBLE);*/
        } else {
            startbtn.setVisibility(View.VISIBLE);
            spinnerterminal.setEnabled(false);
            spDist.setEnabled(false);
            msgliner.setEnabled(false);
            biometric.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                //addSomething();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                return true;
//            case R.id.action_settings:
//                //startSettings();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            //logout("Wish to exit ?", 2);
            //onBackPressed();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finishAllActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logout(String mes, final int dif) {
        try {
            if (logoutDlg != null) {
                logoutDlg.cancel();
            }
            logoutDlg = new Dialog(ApplicantInfoActivity.this, R.style.MyDialogTheme1);
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

    private class StartTest extends AsyncTask<Void, Void, Void> {
        String url = "http://" + macnip + ":1300" + "/simpleserver/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ApplicantInfoActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String strDate = "Current Time when CALL API : " + mdformat.format(calendar.getTime());
            //createtext(strDate);

            HttpHandler httpHandler = new HttpHandler();
            myresponse = httpHandler.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (pDialog.isShowing())
                pDialog.dismiss();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String strDate = "Current Time when RESPONSE : " + mdformat.format(calendar.getTime());
            // createtext(strDate);


            StringTokenizer stringTokenizer = new StringTokenizer(stripHtml(myresponse), "#");
            String value = stringTokenizer.nextToken();
            String value1 = stringTokenizer.nextToken();
            int myval = Integer.parseInt(value1);
            if (myval == 0) {
                Toast.makeText(ApplicantInfoActivity.this, "TEST NOT TRIGGER", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AppointmentCheckActivity.class));
                finish();
                Config.MACHINE_IP = "";
                editortrack.putString("teststate", "YES");
                editortrack.commit();

                // Config.MACHINE_IP="";
            }


        }
    }
}
