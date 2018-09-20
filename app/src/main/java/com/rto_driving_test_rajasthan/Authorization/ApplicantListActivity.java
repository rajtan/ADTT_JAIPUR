package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.Models.Applicantdata;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utility.BaseActivity;
import utility.CommonFunctions;
import utility.Config;

/**
 * Created by Deep on 9/10/2018.
 */

public class ApplicantListActivity extends BaseActivity {

    String response_infodata;
    SharedPreferences responsedata;
    public static String MY_RESPONSE="response";
    private RecyclerView recyclerView;
    ArrayList<Applicantdata> applicantdatas;
    String dashcamStr,terminalStr="";
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicantlist);
        setAppBar("Appointment List",true);
        applicantdatas=new ArrayList<>();
        responsedata=getApplicationContext().getSharedPreferences(MY_RESPONSE, Context.MODE_PRIVATE);

        editor=responsedata.edit();
        response_infodata=responsedata.getString("response_info","");

        recyclerView = (RecyclerView) findViewById(R.id.rv_cente_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ApplicantListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //dummydata();
        applicantvalue(response_infodata);
    }

    private void applicantvalue(String response_infodata) {

        Applicantdata applicantdata=null;
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(response_infodata);
            Log.e("JSONOBJECT1", jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("Result");
            JSONArray jsonArray = jsonObject1.getJSONArray("Applicant_Data");
            final JSONArray dashcam = jsonObject1.getJSONArray("DashBoard_Camlist");
            final JSONArray teminalarr = jsonObject1.getJSONArray("Terminal_list");

            dashcamStr=dashcam.toString();
            terminalStr=teminalarr.toString();

           /* if(teminalarr.length()>0){
                terminalStr=teminalarr.toString();
            }
            else {
                terminalStr="0";
            }*/


            Log.e("dashcam",dashcam.toString());
            Log.e("teminalarr",teminalarr.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                applicantdata=new Applicantdata();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                //LLNO,DLTEST_SEQ ,COV_CD,CARD_NUMBER,TRACK_ID,MACHINE_ID,CAM_TYPE

                applicantdata.setAPPLNO(jsonObject2.optString("APPLNO"));
                applicantdata.setLLNO(jsonObject2.getString("LLNO"));
                applicantdata.setDOB(jsonObject2.optString("DOB"));
                applicantdata.setFIRST_NAME(jsonObject2.getString("FIRST_NAME"));
                //applicantdata.setCARD_NUMBER(jsonObject2.optString("CARD_NUMBER"));
                //applicantdata.setDLTEST_SEQ(jsonObject2.optString("DLTEST_SEQ"));
                applicantdata.setIMAGE(jsonObject2.getString("IMAGE"));
                applicantdata.setTHUMB(jsonObject2.getString("THUMB"));
                applicantdata.setCOV_CD(jsonObject2.getString("COV_CD"));
                applicantdata.setDLTEST_SEQ(jsonObject2.getString("DLTEST_SEQ"));
                applicantdata.setCARD_NUMBER(jsonObject2.getString("CARD_NUMBER"));
                applicantdata.setIS_Valid(jsonObject2.getString("IS_Valid"));
                applicantdata.setLL_ISSUED_DT(jsonObject2.getString("LL_ISSUED_DT"));
                applicantdata.setLL_OLA_CODE(jsonObject2.getString("LL_OLA_CODE"));
                applicantdata.setLL_VALID_FROM(jsonObject2.getString("LL_VALID_FROM"));
                applicantdata.setLL_VALID_TO(jsonObject2.getString("LL_VALID_TO"));
                applicantdata.setSWD_FIRST_NAME(jsonObject2.getString("SWD_FIRST_NAME"));


                    applicantdatas.add(applicantdata);


            }
            setAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void dummydata() {

        Applicantdata applicantdata=null;

        for (int i=0;i<10;i++){

            /*("APPLNO",appno);
            ("FIRST_NAME",appName);
            ("DOB",dobstr);
            ("LLNO",llno);
            ("DLTEST_SEQ",dlseq);
            ("IMAGE",image);
            ("COV_CD",covcd);
            ("CARD_NUMBER",cardnum);
            ("IS_Valid",isvalid);
            ("THUMB",thumb);*/

            applicantdata=new Applicantdata();
            applicantdata.setAPPLNO("112"+i);
            applicantdata.setFIRST_NAME("Deepak Kumar Dubey"+i);
            applicantdata.setDOB("20/07/85"+i);
            applicantdata.setLLNO("2078596"+i);
            applicantdata.setDLTEST_SEQ("1"+i);
            applicantdata.setIMAGE("");
            applicantdata.setCOV_CD("2");
            applicantdata.setCARD_NUMBER("201952");
            applicantdata.setIS_Valid("null");
            applicantdata.setTHUMB("Rk1SACAyMAAAAAEUAAABAAGQAMUAxQEAAABUKUBMAAooUECjAB2QV4CTACQUV4DEACQUSkC7AECUXYBMAE60SkB3AE6kXUCcAFUcXYBkAFwkV4C9AHGcXUAnAH0wXUDEAIscXUAuAIu4XUASAJS0SkDgAKwMSkBrALMwXYDuALOkV0CAAMEwXUAnANEwXUBTANYgXYDuANYkV0AuAN0sXUDCAOQsXUB5APksXYCoAQWoXUDnAQ4kXUAuARUoV4CoASEsXUBDASokXUCAAS8gXUBrATEkXYAgATicSoCtAUIgXUDwAVAYXUB3AVIkXUDLAVcYXUA8AWcgSkC0AWcUXYCTAWwUXYDsAWwYUIB5AXMUXQAA");
            applicantdatas.add(applicantdata);
            //dashcamStr="["+"{"+"CAM_IP"+":"+"192.168.40.10"+"}"+"]";
            //terminalStr="["+"{"+"MACHINE_ID"+":"+"192.168.10.115"+"}"+"]"+"," + "{"+"MACHINE_IP"+":"+"RJ1401"+"}";
        }

        setAdapter();

    }

    private void setAdapter() {

        SelecteMarAdpt mAdapter = new SelecteMarAdpt(applicantdatas);
        recyclerView.setAdapter(mAdapter);

    }

    private class SelecteMarAdpt extends RecyclerView.Adapter<SelecteMarAdpt.MyViewHolder>  {
        ArrayList<Applicantdata> applicantdatas;

        public SelecteMarAdpt(ArrayList<Applicantdata> applicantdatas) {

            this.applicantdatas=applicantdatas;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.applicant_list,parent, false);
            return new SelecteMarAdpt.MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            final String appno=applicantdatas.get(position).getAPPLNO();
            final String appName=applicantdatas.get(position).getFIRST_NAME();
            final String dobstr=applicantdatas.get(position).getDOB();
            final String llno=applicantdatas.get(position).getLLNO();
            final String dlseq=applicantdatas.get(position).getDLTEST_SEQ();
            final String image=applicantdatas.get(position).getIMAGE();
            final String covcd=applicantdatas.get(position).getCOV_CD();
            final String thumb=applicantdatas.get(position).getTHUMB();
            final String cardnum=applicantdatas.get(position).getCARD_NUMBER();
            final String isvalid=applicantdatas.get(position).getIS_Valid();






            holder.appno.setText(applicantdatas.get(position).getAPPLNO());
            holder.appName.setText(applicantdatas.get(position).getFIRST_NAME());
            holder.dob.setText(applicantdatas.get(position).getDOB());
            holder.llNO.setText(applicantdatas.get(position).getLLNO());
            holder.headTxt.setText(applicantdatas.get(position).getFIRST_NAME());
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //Config.APPLICANT_PIC_BASE64 = decodedByte;
            holder.imageView.setImageBitmap(decodedByte);


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(ApplicantListActivity.this, "CLICK", Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject jsonObject1=new JSONObject();
                        JSONObject result=new JSONObject();

                        JSONObject jsonObject=new JSONObject();


                        jsonObject.put("APPLNO",appno);
                        jsonObject.put("FIRST_NAME",appName);
                        jsonObject.put("DOB",dobstr);
                        jsonObject.put("LLNO",llno);
                        jsonObject.put("DLTEST_SEQ",dlseq);
                        jsonObject.put("IMAGE",image);
                        jsonObject.put("COV_CD",covcd);
                        jsonObject.put("CARD_NUMBER",cardnum);
                        jsonObject.put("IS_Valid",isvalid);
                        jsonObject.put("THUMB",thumb);

                        JSONArray jsonArray=new JSONArray();
                        JSONArray dash=new JSONArray(dashcamStr);
                        JSONArray termi=new JSONArray(terminalStr);
                        jsonArray.put(jsonObject);
                        result.put("Applicant_Data",jsonArray);
                        result.put("DashBoard_Camlist",dash);
                        result.put("Terminal_list",termi);
                        jsonObject1.put("Result",result);

                        Log.e("JSONOBJECT",jsonObject1.toString());
                        String response=jsonObject1.toString();
                        editor.putString("response_info",response);
                        editor.commit();

                        startActivity(new Intent(getApplicationContext(),ApplicantInfoActivity.class));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return applicantdatas.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView appno;
            TextView appName;
            TextView dob;
            TextView llNO;
            TextView headTxt;
            ImageView imageView;
            LinearLayout linearLayout;


            public MyViewHolder(View itemView) {
                super(itemView);

                appName=(TextView)itemView.findViewById(R.id.txtName);
                appno=(TextView)itemView.findViewById(R.id.txtappno);
                dob=(TextView)itemView.findViewById(R.id.txtdob);
                llNO=(TextView)itemView.findViewById(R.id.txtllno);
                linearLayout=(LinearLayout)itemView.findViewById(R.id.lin_list);
                headTxt=(TextView)itemView.findViewById(R.id.headtextname);
                imageView=(ImageView)itemView.findViewById(R.id.img_applicant);

            }
        }
    }


    /*public class SelecteMarAdpt extends RecyclerView.Adapter<SelecteMarAdpt.MyViewHolder> {
        private List<Applicantdata> moviesList;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView col1,col2,col3,col4;
            //            EditText etCol3,etCol4;
            LinearLayout lyMain;
            RelativeLayout rlRm;
            public MyViewHolder(View view)
            {
                super(view);
                col1 = (TextView) view.findViewById(R.id.tv_row1_col1);
                col2 = (TextView) view.findViewById(R.id.tv_row1_col2);
//                col3 = (TextView) view.findViewById(R.id.tv_row1_col3);
//                col4 = (TextView) view.findViewById(R.id.tv_row1_col4);
//                etCol4 = (EditText) view.findViewById(R.id.et_row1_col4);
                lyMain = (LinearLayout) view.findViewById(R.id.ly_main_color);
                rlRm = (RelativeLayout) view.findViewById(R.id.rl_remove);
            }
        }

        public SelecteMarAdpt(List<MarkerData> moviesList) {
            this.moviesList = moviesList;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public SelecteMarAdpt.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_selected_marker_home,parent, false);
            return new SelecteMarAdpt.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(SelecteMarAdpt.MyViewHolder holder, final int position) {
            MarkerData BeanPlaceOrd = moviesList.get(position);
            holder.col1.setText(String.valueOf(position+1));
            holder.col2.setText(BeanPlaceOrd.getTitle());
            holder.rlRm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arSelectedMarker.remove(position);
                    notifyDataSetChanged();
                }
            });

//            holder.col3.setText(BeanPlaceOrd.getAdvCard());
//            holder.col4.setText(BeanPlaceOrd.getAdvCard());
//            holder.etCol3.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    std10List.get(position).setOrder(s.toString());
//                }
//            });
//            holder.etCol4.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    std10List.get(position).setRetur(s.toString());
//                }
//            });

//            int pos=position%2;
//            switch (pos)
//            {
//                case 0:
//                    holder.lyMain.setBackgroundColor(context.getResources().getColor(R.color.place_order_bg));
//                    break;
//                case 1:
//                    holder.lyMain.setBackgroundColor(context.getResources().getColor(R.color.white));
//                    break;
//            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();//moviesList.size()
        }
    }*/


}
