package retrofit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rto_driving_test_rajasthan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Deepak m on 11/17/2016.
 */
public class BaseRequest<T> extends BaseRequestParser {
    private Context mContext;
    private ApiInterface apiInterface;
    private RequestReciever requestReciever;
    private boolean runInBackground = false;
    private Dialog dialog;
    private View loaderView = null;
    private int APINumber_ = 1;


    public BaseRequest(Context context) {
        mContext = context;

        apiInterface =
                ApiClient.getClient().create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    public BaseRequest(Context context, Fragment fm) {
        mContext = context;
        apiInterface =
                ApiClient.getClient().create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    public void setBaseRequestListner(RequestReciever requestListner) {
        this.requestReciever = requestListner;

    }

    public ArrayList<Object> getDataList(JSONArray mainArray, Class<T> t) {
        Gson gsm = null;
        ArrayList<Object> list = null;
        list = new ArrayList<>();
        if (null != mainArray) {

            for (int i = 0; i < mainArray.length(); i++) {
                gsm = new Gson();
                Object object = gsm.fromJson(mainArray.optJSONObject(i).toString(), t);
                list.add(object);
            }
        }
        return list;
    }

    public Callback<JsonElement> responseCallback = new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            String responseServer = "";
            hideLoader();
            if (null != response.body()) {
                JsonElement jsonElement = (JsonElement) response.body();
                if (null != jsonElement) {
                    responseServer = jsonElement.toString();
                }

            } else if (response.errorBody() != null) {
                responseServer = readStreamFully(response.errorBody().contentLength(),
                        response.errorBody().byteStream());
            }
            logFullResponse(responseServer, "OUTPUT");
            if (parseJson(responseServer)) {
                if (null != requestReciever) {
                    if (null != getDataArray()) {
                        requestReciever.onSuccess(APINumber_, responseServer, getDataArray());
                    } else if (null != getDataObject()) {
                        requestReciever.onSuccess(APINumber_, responseServer, getDataObject());
                    } else {
                        requestReciever.onSuccess(APINumber_, responseServer, message);

                    }
                }

            } else {
                if (null != requestReciever) {
                    requestReciever.onFailure(1, "" + mResponseCode, message);
                }
            }
        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {

            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(r, 1000);
           /* if (t.getMessage().startsWith("Unable to resolve")) {
               r.run();
            }*/


        }
    };


    public Callback<JsonElement> responseCallbackCustom = new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            String responseServer = "";
            hideLoader();
            if (null != response.body()) {
                JsonElement jsonElement = (JsonElement) response.body();
                if (null != jsonElement) {
                    responseServer = jsonElement.toString();
                }

            } else if (response.errorBody() != null) {
                responseServer = readStreamFully(response.errorBody().contentLength(),
                        response.errorBody().byteStream());
            }
            logFullResponse(responseServer, "OUTPUT");
            requestReciever.onSuccess(APINumber_, responseServer, null);


        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {

            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(r, 1000);
           /* if (t.getMessage().startsWith("Unable to resolve")) {
               r.run();
            }*/
        }
    };
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            hideLoader();
            if (null != requestReciever) {
                requestReciever.onNetworkFailure(APINumber_, "Network Error");
            }
        }
    };

    public void callAPIPost(final int APINumber, JsonObject jsonObject, String remainingURL) {//input data into the api
        APINumber_ = APINumber;
        showLoader();
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        logFullResponse(jsonObject.toString(), "INPUT");


        Call<JsonElement> call = apiInterface.postData(remainingURL, jsonObject);
        call.enqueue(responseCallback);
    }

    public void callAPIGET(final int APINumber, Map<String, String> map, String remainingURL) {
        APINumber_ = APINumber;
        showLoader();
        String baseURL = ApiClient.getClient().baseUrl().toString() + remainingURL;
        if (!baseURL.endsWith("?")) {
            baseURL = baseURL + "?";
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            baseURL = baseURL + entry.getKey() + "=" + entry.getValue() + "&";
        }
        System.out.println("BaseReq INPUT URL : " + baseURL);
        Call<JsonElement> call = apiInterface.postDataGET(remainingURL, map);
        call.enqueue(responseCallback);
    }

    public void callAPIGETCustomURL(final int APINumber, Map<String, String> map, String baseURL_) {
        APINumber_ = APINumber;

        showLoader();
        String baseURL = baseURL_;
        if (!baseURL.endsWith("?")) {
            baseURL = baseURL + "?";
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            baseURL = baseURL + entry.getKey() + "=" + entry.getValue() + "&";
        }
        System.out.println("BaseReq INPUT URL : " + baseURL);
        ApiInterface apiInterface_ = ApiClient.getCustomClient(baseURL_).create(ApiInterface.class);
        Call<JsonElement> call = apiInterface_.postDataGET("", map);
        call.enqueue(responseCallbackCustom);
    }

    public void logFullResponse(String response, String inout) {
        final int chunkSize = 3000;

        if (null != response && response.length() > chunkSize) {
            int chunks = (int) Math.ceil((double) response.length()
                    / (double) chunkSize);
            for (int i = 1; i <= chunks; i++) {
                if (i != chunks) {
                    Log.i("BaseReq",
                            inout + " : "
                                    + response.substring((i - 1) * chunkSize, i
                                    * chunkSize));
                } else {
                    Log.i("BaseReq",
                            inout + " : "
                                    + response.substring((i - 1) * chunkSize,
                                    response.length()));
                }
            }
        } else {

            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("BaseReq", inout + " : " + jsonObject.toString(jsonObject.length()));

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("BaseReq", " logFullResponse=>  " + response);
            }
        }
    }

    private String readStreamFully(long len, InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public Dialog getProgressesDialog(Context ct) {
        Dialog dialog = new Dialog(ct);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog_loader);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    public void showLoader() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.VISIBLE);
                } else if (null != dialog) {
                    dialog.show();
                }
            }
        }
    }

    public void hideLoader() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.GONE);
                } else if (null != dialog) {
                    dialog.dismiss();
                }
            }
        }
    }

    public int TYPE_NOT_CONNECTED = 0;
    public int TYPE_WIFI = 1;
    public int TYPE_MOBILE = 2;

    public int getConnectivityStatus(Context context) {
        if (null == context) {
            return TYPE_NOT_CONNECTED;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null != activeNetwork && activeNetwork.isConnected()) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;


    }
}
