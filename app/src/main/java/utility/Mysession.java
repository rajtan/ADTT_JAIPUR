package utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by Deep on 9/8/2018.
 */

public class Mysession {

    private static final String PREF_NAME = "rajtest";

    public String machineip, testcompl;

    public Mysession(JSONObject jsonObject) {
        if (jsonObject != null) {
            machineip = jsonObject.optString("machineip", "");
            testcompl = jsonObject.optString("testcompl", "");
        }
    }


    public Mysession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        this.machineip = sharedPreferences.getString("machineip", "");
        this.testcompl = sharedPreferences.getString("testcompl", "");
    }

    public void persist(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("machineip", machineip);
        prefsEditor.putString("testcompl", testcompl);
        prefsEditor.commit();
    }

    public void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }

}
