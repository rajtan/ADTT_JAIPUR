package com.rto_driving_test_rajasthan.Authorization;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.util.ArrayMap;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.rto_driving_test_rajasthan.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.BaseRequest;
import retrofit.RequestReciever;
import utility.BaseActivity;
import utility.Config;
import utility.ErrorLayout;
import utility.HttpHandler;

import static java.net.Proxy.Type.HTTP;

public class ActShowTrack extends BaseActivity {
    Context context;
    Activity activity;

    @BindView(R.id.bt_two_wheeler)
    Button btTwoWheel;
    @BindView(R.id.bt_four_wheeler)
    Button btFourWheel;

    @BindView(R.id.starttime)
    TextView starttext;
    @BindView(R.id.endtime)
    TextView endtext;

    ProgressDialog pDialog;
    String myresponse;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show_track);
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.show_track), true);
        initViews();

        requestQueue= Volley.newRequestQueue(this);
        context = this;
        activity = this;



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

    @OnClick({R.id.bt_two_wheeler,R.id.bt_four_wheeler})
    public void onClick(View view) {
        //http://"+macnip+":1300"+"/simpleserver/
        String url="http://192.168.10.115:1300/simpleserver";
        switch (view.getId()) {
            case R.id.bt_four_wheeler:
               /* Intent in = new Intent(context,ActTestReport.class);
                    startActivity(in);*/

                //callSocketApi();
                //callretrosocket();
               // callvollysocket();


               /* try {
                    //callservice();

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                //callServer();

                new ConnecttoServer().execute();


                break;
            case R.id.bt_two_wheeler:
               /* Intent in1 = new Intent(context,ActTestReport.class);
                startActivity(in1);*/


                break;


        }
    }


    public class ConnecttoServer extends AsyncTask<Void,Void,Void>{

        Socket socket;
        String inputLine;
        int count = 0;
        BufferedReader in;
        String starttme,endtime;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            PrintWriter out = null;
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                starttme = "Current Time when CALL API : " + mdformat.format(calendar.getTime());

                socket= new Socket("192.168.10.115",1300);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                out.println("GET /simpleserver/  HTTP/1.0");
                out.println();
                out.flush();

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                while ((inputLine = in.readLine()) != null) {
                    count++;
                    System.out.println(count);
                    System.out.println(inputLine);

                    String output=stripHtml(inputLine);
                    Log.e("HTML_OUTPUT",output);



                }

                in.close();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = "Current Time when GET RESPONSE  : " + mdformat.format(calendar.getTime());


                starttext.setText(starttme);
                endtext.setText(strDate);
                System.out.println("PRINTING HERE!!!");

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    private void callServer() {

        try {

            Socket socket= new Socket("192.168.10.115",1300);
            //socket.isConnected();

            if(socket.isConnected())
            {

                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

                out.println("GET /simpleserver/  HTTP/1.0");
                out.println();
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inputLine;
                int count = 0;




                while ((inputLine = in.readLine()) != null) {
                    count++;
                    System.out.println(count);
                    System.out.println(inputLine);

                    String output=stripHtml(inputLine);
                    Log.e("HTML_OUTPUT",output);

                }

                in.close();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = "Current Time when CALL API : " + mdformat.format(calendar.getTime());

                endtext.setText(strDate);
                System.out.println("PRINTING HERE!!!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callservice() throws IOException {

        new DemoTask().execute();


    }


    class DemoTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... arg0) {
            //Your implementation

            try {

               /* Socket socket = new Socket("192.168.10.115/simpleserver",1300);

                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                out.println("GET /index.html HTTP/1.0");
                out.println();
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String inputLine;
                int count = 0;

                while ((inputLine = in.readLine()) != null) {
                    count++;
                    System.out.println(count);
                    System.out.println(inputLine);
                }

                in.close();
                System.out.println("PRINTING HERE!!!");
                Log.e("PRINTING HERE","");*/

                Socket socket = new Socket("youtube.com",80);

                PrintWriter out = new PrintWriter(new BufferedWriter(new
                        OutputStreamWriter(socket.getOutputStream())));

      out.println("GET http://www.youtube.com/yts/img/favicon_48-vflVjB_Qk.png HTTP/1.0");
                        out.println();
                out.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            // TODO: do something with the feed
        }
    }

    private void callvollysocket() {
        String url="https://192.168.10.115:1300/simpleserver";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = "Current Time when CALL API : " + mdformat.format(calendar.getTime());
        //createtext(strDate);

        Log.e("CURRENT TIME",strDate);


        //String url=ApiClient.BASE_URL+"simpleserver/"+Config.MACHINE_IP;
        // String url=newurl+"simpleserver/"+Config.MACHINE_IP;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("CURRENT TIME","MYRESPONSETIME"+System.currentTimeMillis());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = "Current Time when RESPONSE : " + mdformat.format(calendar.getTime());
               // createtext(strDate);

                Log.e("CURRENT TIME",strDate);



                StringTokenizer stringTokenizer=new StringTokenizer(stripHtml(response),"#");
                String value=stringTokenizer.nextToken();
                String value1=stringTokenizer.nextToken();
                int myval=Integer.parseInt(value1);
                if(myval==0){
                    Toast.makeText(ActShowTrack.this, "TEST NOT TRIGGER", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();


                    // Config.MACHINE_IP="";
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {



                String message=null;
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    BaseRequest baseRequest;

    private void callretrosocket() {
        String url="http://192.168.10.115:1300/simpleserver";
    baseRequest=new BaseRequest(this);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                Log.e("JSON",Json);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("JSON","onFailure"+message);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

                Log.e("JSON","onNetworkFailure"+message);

            }
        });

        baseRequest.callAPIGET(1,new ArrayMap<String, String>(),"simpleserver");
    }

    private void callSocketApi() {
        new StartTest().execute();

    }




    private class StartTest extends AsyncTask<Void,Void,Void> {
        //String url="http://"+macnip+":1300"+"/simpleserver/";
        String url="http://192.168.10.115:1300/simpleserver";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ActShowTrack.this);
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

            HttpHandler httpHandler=new HttpHandler();
            myresponse=httpHandler.makeServiceCall(url);
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
            //createtext(strDate);



            StringTokenizer stringTokenizer=new StringTokenizer(stripHtml(myresponse),"#");
            String value=stringTokenizer.nextToken();
            String value1=stringTokenizer.nextToken();
            int myval=Integer.parseInt(value1);
            if(myval==0){
                Toast.makeText(ActShowTrack.this, "TEST NOT TRIGGER", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();


                // Config.MACHINE_IP="";
            }


        }
    }

    public String stripHtml(String html)
    {
        return Html.fromHtml(html).toString();
    }
}
