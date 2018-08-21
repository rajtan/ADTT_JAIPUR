package utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by Prashant Maheshwari
 */
public class SessionParam {
    private static final String PREF_NAME = "MSeva";

    public String user_id, name;

    public SessionParam(JSONObject jsonObject) {
        if (jsonObject != null) {
            user_id = jsonObject.optString("user_id", "");
            name = jsonObject.optString("name", "");
                   }
    }


    public SessionParam(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        this.user_id = sharedPreferences.getString("user_id", "");
        this.name = sharedPreferences.getString("name", "");
           }

    public void persist(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("user_id", user_id);
        prefsEditor.putString("name", name);
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
