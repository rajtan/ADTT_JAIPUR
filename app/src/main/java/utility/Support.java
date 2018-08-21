package utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 14/6/16.
 */
public class Support {
    static Context mContext;
    private static String PREFERENCE = "Patnapark";

    public Support(Context c) {
        this.mContext = c;
    }

//    public static Typeface ButtonFont(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(),
//                Constants.FONT_STYLE);
//        return tf;
//    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void app_msg(final Context mContext, final String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setMessage(Html.fromHtml(/*"<font color='#D35400'>" +*/ msg + " "   /*+ "</font>"*/));
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
    }
//    public static void writeParkINFO(Context context, String key, ParkInfo value) {
//
//        try
//        {
//            mContext  = context;
//            SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
//            SharedPreferences.Editor editor = settings.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(value);
//            editor.putString(key, json);
//            editor.commit();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public static ParkInfo readParkInfo(Context context, String key) {
//
//        ParkInfo value = new ParkInfo();
//        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE, 0);
//
//        if (prefs.contains(key)) {
//            Gson gson = new Gson();
//            String json = prefs.getString(key, "");
//            value = gson.fromJson(json,
//                    new TypeToken<ParkInfo>() {
//                    }.getType());
//            return value;
//        }
//        return null;
//    }

  /*  public static void showDialog(Context context) {



        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sharescreen);

        int[] imageId = {
                R.drawable.google,
                R.drawable.whatss,
                R.drawable.twiter,
                R.drawable.message,
                R.drawable.camera,
                R.drawable.fb,
        };

        GridView gridview = (GridView) dialog.findViewById(R.id.gridView);

        gridview.setAdapter(new CustomShareAdapter(context,imageId));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                dialog.dismiss();
            }



        });

        dialog.show();


    }*/

}
