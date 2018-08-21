package com.rto_driving_test_rajasthan.Authorization;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.Adapters.DateSelectAdapter;
import com.rto_driving_test_rajasthan.Adapters.OnItemClickAdapter;
import com.rto_driving_test_rajasthan.Models.AppoimentBean;
import com.rto_driving_test_rajasthan.Models.AppointmentModel;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.ApiClient;
import retrofit.BaseRequest;
import retrofit.RequestReciever;
import utility.BaseActivity;
import utility.Config;
import utility.ErrorLayout;

public class ActAppoimentList extends BaseActivity {

    Context context;
    RecyclerView recyViewAppoi;
    Activity activity;

    ArrayList<AppoimentBean> std10List = new ArrayList<AppoimentBean>();

    private List<AppointmentModel> list = new ArrayList<AppointmentModel>();

    @BindView(R.id.et_search_applicant)
    EditText edTxtSearch;
    @BindView(R.id.textheader)
    TextView txtheader;
    DateSelectAdapter dateSelectAdapter;

    String photostatus = "";

    ArrayList<AppointmentModel> appointmentModels = new ArrayList<>();
    final List<AppointmentModel> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.act_appoiment);
        context = this;
        activity = this;
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.mid_screen), true);
        initViews();
//        appointmentModels=new ArrayList<>();
        recyViewAppoi = (RecyclerView) findViewById(R.id.rc_view_select);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(activity);
        recyViewAppoi.setLayoutManager(mLayoutManager2);
//        recyViewAppoi.setItemAnimator(new DefaultItemAnimator());
//        recyViewAppoi.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));


        DummyData();

        addTextListener();



       /* edTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = edTxtSearch.getText().toString().trim();
                dateSelectAdapter.filter(searchText);

            }
        });*/
    }


    private void addTextListener() {


        edTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {


                filteredList.clear();

                query = query.toString().toLowerCase();


                for (int i = 0; i < appointmentModels.size(); i++) {
                    try {
                        final String text = appointmentModels.get(i).getApplicant_Name().toString().toLowerCase();
                        String ref = appointmentModels.get(i).getReference_Number().toString().toLowerCase();

                        if (text.contains(query.toString())) {
                            filteredList.add(appointmentModels.get(i));
                        } else if (ref.contains(query.toString())) {
                            filteredList.add(appointmentModels.get(i));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                recyViewAppoi.setLayoutManager(new LinearLayoutManager(ActAppoimentList.this));
                dateSelectAdapter = new DateSelectAdapter(ActAppoimentList.this, filteredList, new OnItemClickAdapter() {
                    @Override
                    public void click(int idofClickedItem, Object object, int position) {


                        //String id=appointmentModels.get(position).getReceipt_Number();

//                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class).putExtra("app_id",id));

                        AppointmentModel appointmentModel = new AppointmentModel();
                        // appointmentModels.add(appointmentModel);
                        filteredList.add(appointmentModel);


                        String name = filteredList.get(position).getApplicant_Name();

                        Config.APPLICANT_FIRST_NAME = filteredList.get(position).getApplicant_Name();
                        Config.APPLICANT_LAST_NAME = filteredList.get(position).getApplicant_Last_Name();
                        Config.RTO_CODE = filteredList.get(position).getRTO_CODE();
                        Config.APPOINTMENT_DATE = filteredList.get(position).getAppointment_Date();
                        Config.DRIVER_NUMBER = filteredList.get(position).getDRIVER_NUMBER();
                        Config.NUMBER = filteredList.get(position).getNumber();
                        Config.LICENCE_NUMBER = filteredList.get(position).getLicence_Number();
                        Config.DOB = filteredList.get(position).getDate_Of_Birth();
                        Config.RECEIPT_NUMBER = filteredList.get(position).getReceipt_Number();
                        Config.REF_NUMBER = filteredList.get(position).getReference_Number();
                        Config.SOWODO = filteredList.get(position).getSo_Wo_Do();
                        Config.TESTTYPE = filteredList.get(position).getTEST_TYPE();
                        String imgbyte = filteredList.get(position).getAPPLICANT_PIC().toString();
                        photostatus = filteredList.get(position).getPhoto_Status().toString();
                        byte[] decodedString = Base64.decode(imgbyte, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        Config.APPLICANT_PIC_BASE64 = decodedByte;


                        // Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();

                        Intent in = new Intent(getApplicationContext(), ActRetestAppliDetails.class);
                        in.putExtra("diff", "fresh");
                        in.putExtra("fresh", "fresh");
                        in.putExtra("name", name);
                        in.putExtra("activityname", "FRESH");
                        in.putExtra("photostatus", photostatus);
                        startActivity(in);


                    }
                });

                recyViewAppoi.setAdapter(dateSelectAdapter);
                dateSelectAdapter.notifyDataSetChanged();
                //filteredList.clear();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    BaseRequest baseRequest_list = null;

    private void callApi() {

        baseRequest_list = new BaseRequest(ActAppoimentList.this);


        baseRequest_list.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                JSONArray data = (JSONArray) object;
                if (appointmentModels != null) {
                    appointmentModels.clear();
                }
                appointmentModels = baseRequest_list.getDataList(data, AppointmentModel.class);
                dateSelectAdapter.setList(appointmentModels);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                if (appointmentModels != null) {
                    appointmentModels.clear();
                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                if (appointmentModels != null) {
                    appointmentModels.clear();
                }
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            baseRequest_list.callAPIGET(1, new ArrayMap<String, String>(), "Get_Applicant_list.svc/Get_Applicant/");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        callAPI();
        baseRequest_list = null;
        if (appointmentModels != null) {
            appointmentModels.clear();
        }

        System.out.println("details= " + ApiClient.BASE_URL);
        callApi();
    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }


    BaseRequest baseRequest;

//    private void callAPI() {
//        baseRequest = new BaseRequest(this);
//        baseRequest.setBaseRequestListner(new RequestReciever() {
//            @Override
//            public void onSuccess(int requestCode, String Json, Object object) {
//                try {
//                    JSONArray array = (JSONArray) object;
//                    std10List.clear();
//                    if (array != null && array.length() > 0) {
//                        for (int i = 0; i < array.length(); i++) {
//                            Gson gson = new Gson();
//                            JSONObject jo10 = null;
//                            try {
//                                jo10 = array.getJSONObject(i);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            AppoimentBean    stdData10 = gson.fromJson(jo10.toString(), AppoimentBean.class);
//                            std10List.add(stdData10);
//                        }
//
//
////                        Log.e("", "shyam Login object= " + object + " Json= " + Json);
//                    } else {
//                        Toast.makeText(mContext, "No Data", Toast.LENGTH_SHORT).show();
//                    }
//                    SetData();
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(int requestCode, String errorCode, String message) {
//                errorLayout.showAlert(message, ErrorLayout.MsgType.Error);
//
//            }
//
//            @Override
//            public void onNetworkFailure(int requestCode, String message) {
//                errorLayout.showAlert(message, ErrorLayout.MsgType.Error);
//
//            }
//        });
//
//
//        JsonObject object = Functions.getClient().getJsonMapObject("centerno", sCenNo,
//                "password",sPass
//
//        );
//        //http://cnvg.in/cbseecm/api_datewiseList.php
//        baseRequest.callAPIPost(1, object, getAppString(R.string.api_list));
//    }

    public void DummyData() {
        for (int i = 0; i < 5; i++) {
            AppoimentBean bean = new AppoimentBean();
            bean.setAppId(i + "");
            bean.setApPic("");
            bean.setApName("Rekesh Kumar Verma");
            bean.setApAppNo("305353503503");
            std10List.add(bean);
        }
        SetData();
    }

    public void SetData() {
        dateSelectAdapter = new DateSelectAdapter(ActAppoimentList.this, appointmentModels, new OnItemClickAdapter() {
            @Override
            public void click(int idofClickedItem, Object object, int position) {


                //String id=appointmentModels.get(position).getReceipt_Number();

//                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class).putExtra("app_id",id));

                AppointmentModel appointmentModel = new AppointmentModel();
                appointmentModels.add(appointmentModel);


                String name = appointmentModels.get(position).getApplicant_Name();

                Config.APPLICANT_FIRST_NAME = appointmentModels.get(position).getApplicant_Name();
                Config.APPLICANT_LAST_NAME = appointmentModels.get(position).getApplicant_Last_Name();
                Config.RTO_CODE = appointmentModels.get(position).getRTO_CODE();
                Config.APPOINTMENT_DATE = appointmentModels.get(position).getAppointment_Date();
                Config.DRIVER_NUMBER = appointmentModels.get(position).getDRIVER_NUMBER();
                Config.NUMBER = appointmentModels.get(position).getNumber();
                Config.LICENCE_NUMBER = appointmentModels.get(position).getLicence_Number();
                Config.DOB = appointmentModels.get(position).getDate_Of_Birth();
                Config.RECEIPT_NUMBER = appointmentModels.get(position).getReceipt_Number();
                Config.REF_NUMBER = appointmentModels.get(position).getReference_Number();
                Config.SOWODO = appointmentModels.get(position).getSo_Wo_Do();
                Config.TESTTYPE = appointmentModels.get(position).getTEST_TYPE();
                String imgbyte = appointmentModels.get(position).getAPPLICANT_PIC().toString();
                photostatus = appointmentModels.get(position).getPhoto_Status().toString();
                byte[] decodedString = Base64.decode(imgbyte, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Config.APPLICANT_PIC_BASE64 = decodedByte;


                // Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();

                Intent in = new Intent(getApplicationContext(), ActRetestAppliDetails.class);
                in.putExtra("diff", "fresh");
                in.putExtra("fresh", "fresh");
                in.putExtra("name", name);
                in.putExtra("activityname", "FRESH");
                in.putExtra("photostatus", photostatus);
                startActivity(in);


                // Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();


            }
        });


        recyViewAppoi.setAdapter(dateSelectAdapter);
    }


    Dialog logoutDlg = null;

    public void logout(String mes, final int dif) {
        try {
            if (logoutDlg != null) {
                logoutDlg.cancel();
            }
            logoutDlg = new Dialog(ActAppoimentList.this, R.style.MyDialogTheme1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
