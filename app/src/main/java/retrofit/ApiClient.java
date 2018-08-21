package retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utility.Config;


public class ApiClient {



    public static String BASE_URL=null;
    /* = "http://"+ Config.IP_ADDRESS+"/ADTT_SEVICE/";*/

    /*public static String BASE_URL = "http://182.72.228.66/ADTT_SEVICE/";
    public static String BASE_URL = "http://"+ Config.IP_ADDRESS+"/ADTT_SEVICE/";*/
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
      //  if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
     //   }
        return retrofit;
    }

    public static Retrofit getCustomClient(String baseURL) {

        Retrofit retrofit_ = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(getRequestHeader())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit_;

    }

    public static OkHttpClient okHttpClient = null;

    public static OkHttpClient getRequestHeader() {
        if (null == okHttpClient) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}
