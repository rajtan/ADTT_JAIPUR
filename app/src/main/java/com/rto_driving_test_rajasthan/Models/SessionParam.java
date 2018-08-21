package com.rto_driving_test_rajasthan.Models;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;


public class SessionParam {
    String PREF_NAME = "SessionPref";

public  String usr_id,fname,usr_email,usr_city,usr_mobi;
    public String schNm,state,sExmDate,sQpcode,sQpNm,sCls,sNoofStu,sCenNo;

      public SessionParam(JSONObject jsonObject) {
        if (jsonObject != null) {
            usr_id = jsonObject.optString("supid", "");
            fname = jsonObject.optString("supname", "");
            usr_email = jsonObject.optString("supemail", "");
            usr_city = jsonObject.optString("schoolcity", "");
            usr_mobi = jsonObject.optString("supmobile", "");


            schNm = jsonObject.optString("schoolname", "");
            state = jsonObject.optString("state", "");
            sExmDate = jsonObject.optString("examdate", "");
            sQpcode = jsonObject.optString("qpcode", "");
            sQpNm = jsonObject.optString("qpname", "");
            sCls = jsonObject.optString("class", "");
            sNoofStu = jsonObject.optString("noofstude", "");
            sCenNo = jsonObject.optString("centerno", "");
        }
    }

    public SessionParam(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        this.usr_id = sharedPreferences.getString("usr_id", "");
        this.fname = sharedPreferences.getString("fname", "");
        this.usr_email = sharedPreferences.getString("usr_email", "");
        this.usr_city = sharedPreferences.getString("city", "");
        this.usr_mobi = sharedPreferences.getString("mobile", "");

        this.schNm = sharedPreferences.getString("school_name", "");
        this.state = sharedPreferences.getString("u_state", "");
        this.sExmDate = sharedPreferences.getString("exam_date", "");
        this.sQpcode = sharedPreferences.getString("qp_code", "");
        this.sQpNm = sharedPreferences.getString("qp_name", "");
        this.sCls = sharedPreferences.getString("u_class", "");
        this.sNoofStu = sharedPreferences.getString("no_ofstude", "");
        this.sCenNo = sharedPreferences.getString("cent_no", "");

          }

    public void persist(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("usr_id", usr_id);
        prefsEditor.putString("fname", fname);
        prefsEditor.putString("usr_email", usr_email);
        prefsEditor.putString("city", usr_city);
        prefsEditor.putString("mobile", usr_mobi);

        prefsEditor.putString("school_name", schNm);
        prefsEditor.putString("u_state", state);
        prefsEditor.putString("exam_date", sExmDate);
        prefsEditor.putString("qp_code", sQpcode);
        prefsEditor.putString("qp_name", sQpNm);
        prefsEditor.putString("u_class", sCls);
        prefsEditor.putString("no_ofstude", sNoofStu);
        prefsEditor.putString("cent_no", sCenNo);
        prefsEditor.commit();
    }



    public void  clearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }



    @Override
    public String toString() {
        return "SessionParam [name=" + "]";
    }

//public String UsrId(Context context)
//{
//    SharedPreferences sharedPreferences = context.getSharedPreferences(
//            PREF_NAME, Context.MODE_PRIVATE);
//   usr_id = sharedPreferences.getString("usr_id", "");
//    return usr_id;
//}
//    public String UsrNm(Context context)
//    {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(
//                PREF_NAME, Context.MODE_PRIVATE);
//         fname = sharedPreferences.getString("fname", "");
//        return fname;
//    }
//    public String UsrEmail(Context context)
//    {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(
//                PREF_NAME, Context.MODE_PRIVATE);
//        usr_email = sharedPreferences.getString("usr_email", "");
//        return usr_email;
//    }
}
