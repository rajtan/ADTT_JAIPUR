package com.rto_driving_test_rajasthan.Authorization;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.rto_driving_test_rajasthan.Models.SessionParam;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;
import retrofit.ApiClient;
import utility.BaseActivity;

public class SplashActivity extends BaseActivity {
    SessionParam sessionParam;
    private static final int REQUEST_CODE_PERMISSION = 2;
Context context;

    SharedPreferences sp,spone;
    SharedPreferences.Editor editor;
    public static String MY_PREF="ipadd";
    public static String MYTESTPREF="testtrack";
    String testState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash_acivity);

        sp=getApplicationContext().getSharedPreferences(MY_PREF,Context.MODE_PRIVATE);
        spone=getApplicationContext().getSharedPreferences(MYTESTPREF,Context.MODE_PRIVATE);
        testState=spone.getString("teststate","");
        String ipaddress=sp.getString("ipaddress","");
        context = this;
        checkPermissions();

        if(!TextUtils.isEmpty(ipaddress))
        {

            ApiClient.BASE_URL="http://"+ipaddress+"/ADTT_data/";
        }

    }


    private  void checkPermissions(){

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==MockPackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
        else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==MockPackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);

        }
        else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==MockPackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
        else{

            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {

                    if(testState.equalsIgnoreCase("NO")){

                        startActivity(new Intent(getApplicationContext(),ApplicantInfoActivity.class));
                        finishAllActivities();
                    }
                    else {

                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finishAllActivities();
                    }


//                    if (Utility.isConnectingToInternet(context)) {
//                        new CheckVersion(context).execute();
//                    } else {
//                        Utility.showToast(context, "Please Check your internet connection");
//                    }

                }
            }, 1500);


        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    String getResponse = "";
    String result1 = "";
    public class CheckVersion extends AsyncTask<String, Void, String> {

        private final Context context;
        // MyDialog myDialog;
        ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        //Method to Encode to Base64
        private String endcodetoBase64(String s) throws UnsupportedEncodingException {
            byte[] byteArray = s.getBytes("UTF-8");
            return Base64.encodeToString(byteArray, 0);
        }

        public CheckVersion(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "Fetching data", "Please wait...", true);
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;

            try {
                //http://cnvg.in/cbseecm/api_datewiseList.php
                URL url = new URL(ApiClient.BASE_URL+"api_datewiseList.php");//Pankaj sir
                connection = (HttpURLConnection) url.openConnection();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("centerno", "");
                    jsonObject.put("password", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = jsonObject.toString();
                System.out.println("json logoin " + json);
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(60000);
                connection.setReadTimeout(60000);

                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(json);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                System.out.print("ResponseCode PostDLINFO1 ====  " + responseCode + "\nRespone === " + connection.getResponseMessage() + "\n");

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Response Code" + responseCode);
                output.append(System.getProperty("line.separator") + "Response Message " + connection.getResponseMessage());

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output PostDLINFO1 ===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                System.out.print("Resposne out put PostDLINFO1 ====  " + responseOutput.toString() + "\n");
                String str[] = responseOutput.toString().split("\\|");

                try {
                    final JSONObject objResonse = new JSONObject(responseOutput.toString());

                    if (objResonse.optInt("apiversion")==1) {
                        result1="True";
                        return result1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result1="False";
                }


            } catch (MalformedURLException e) {
                return getResponse;
            } catch (IOException e) {
                return getResponse;
            }

            return getResponse;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                if(result1.equalsIgnoreCase("True")){
//                    checkPermissions();

//                    startActivity(new Intent(SplashActivity.this, ActAppoimentList.class));
//                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//                    finishAllActivities();
                }
                else
                {
                    UpdateApp("Please Update Your App ?", 2);
                }

            }
            catch (Exception je){

            }
        }
    }
    Dialog logoutDlg = null;
    public void UpdateApp(String mes, final int dif) {
        try {
            if (logoutDlg != null) {
                logoutDlg.cancel();
            }
            logoutDlg = new Dialog(SplashActivity.this, R.style.MyDialogTheme1);
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
                        if (dif == 1) {

                        } else if (dif == 2) {
                            try {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.converge.cbse_ecl&hl=en"));
                                startActivity(i);
                                finishAllActivities();
                            } catch (Exception e) {
                                e.printStackTrace();
                                finishAllActivities();
                            }
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
                    finishAllActivities();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
