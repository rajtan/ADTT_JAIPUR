package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.rto_driving_test_rajasthan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.ErrorLayout;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.mobile_et)
    EditText mobileEt;
    Context context;
    String getMobi = "";
    String getResponse = "";
    String result1 = "";
String sAppId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_login);
        context = this;
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.login_title), true);
        initViews();
        sAppId=getIntent().getStringExtra("app_id");
    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    @OnClick({R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                getMobi = mobileEt.getText().toString();
                if (validation()) {
                    Intent in = new Intent(context,ActRetestAppliDetails.class);
                    in.putExtra("diff","fresh");
                    startActivity(in);

//                    if (Utility.isConnectingToInternet(context)) {
//                        new PostLoginData(context).execute();
//                    } else {
//                        Utility.showToast(context, "Please Check your internet connection");
//                    }
                }
                break;

        }
    }

    private boolean validation() {
         if (TextUtils.isEmpty(mobileEt.getText().toString().trim()))
        {
            errorLayout.showAlert(getAppString(R.string.enter_digit), ErrorLayout.MsgType.Error);
            return false;

        }  else {
            return true;
        }
    }

/*    public class PostLoginData extends AsyncTask<String, Void, String> {

        private final Context context;
        // MyDialog myDialog;
        ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        //Method to Encode to Base64
        private String endcodetoBase64(String s) throws UnsupportedEncodingException {
            byte[] byteArray = s.getBytes("UTF-8");
            return Base64.encodeToString(byteArray, 0);
        }

        public PostLoginData(Context c) {
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
                    jsonObject.put("centerno", getMobi);
//                    jsonObject.put("password", getPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = jsonObject.toString();
                System.out.println("json PostDLINFO1 " + json);
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

                    if (objResonse.optString("status").equalsIgnoreCase("True")) {
                        result1="True";

                        JSONArray ja = new JSONArray(objResonse.optString("result"));
                        getResponse = ja.toString();

                        return getResponse;

                    } else if (objResonse.optString("status").equalsIgnoreCase("False")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result1="False";
                                Utility.showToast(context, objResonse.optString("message"));

                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utility.showToast(context, "Some thing went wrong!");
                                result1="False";
                            }
                        });
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
                        Intent in = new Intent(context,ActNewDashboard.class);
//                        in.putExtra("centerno", getMobi);
//                        in.putExtra("password", getPassword);
                        startActivity(in);

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Utility.showToast(context,"No data");
                            }
                        });
                    }
            }
            catch (Exception je){

            }
        }
    }*/
}
