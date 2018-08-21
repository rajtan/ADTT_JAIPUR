package retrofit;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lenovo on 11/18/2016.
 */
public abstract class BaseRequestParser {
    public String message = "Something going wrong.";
    public String mResponseCode = "0";

    private JSONObject mRespJSONObject = null;

    public boolean parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                mRespJSONObject = new JSONObject(json);
                if (null != mRespJSONObject) {
                    mResponseCode = mRespJSONObject.optString("code",
                            "Response code not found");
                    message = mRespJSONObject.optString("success message",
                            "Something going wrong.");
                    if (mResponseCode.equalsIgnoreCase("200")) {
                        return true;
                    } else {
                        return false;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    public JSONArray getDataArray() {
        if (null == mRespJSONObject) {
            return null;
        }

        try {
            return mRespJSONObject.optJSONArray("Result");////data,result
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public Object getDataObject() {
        if (null == mRespJSONObject) {
            return null;
        }

        try {
            return mRespJSONObject.optJSONObject("Result");//data,result
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
