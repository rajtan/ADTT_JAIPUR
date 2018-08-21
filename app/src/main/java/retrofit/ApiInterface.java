package retrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface ApiInterface {


    @POST
    Call<JsonElement> postData(@Url String remainingURL, @Body JsonObject jsonObject);
  //  Map<String, String> params

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL, @QueryMap Map<String, String> map);



}

