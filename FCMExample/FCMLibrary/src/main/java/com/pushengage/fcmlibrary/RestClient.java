package com.pushengage.fcmlibrary;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushengage.fcmlibrary.model.request.AddDynamicSegmentRequest;
import com.pushengage.fcmlibrary.model.request.AddProfileIdRequest;
import com.pushengage.fcmlibrary.model.request.AddSegmentRequest;
import com.pushengage.fcmlibrary.model.request.AddSubscriberRequest;
import com.pushengage.fcmlibrary.model.request.RemoveDynamicSegmentRequest;
import com.pushengage.fcmlibrary.model.request.SegmentHashArrayRequest;
import com.pushengage.fcmlibrary.model.request.UpdateSubscriberStatusRequest;
import com.pushengage.fcmlibrary.model.response.AddSubscriberResponse;
import com.pushengage.fcmlibrary.model.response.GenricResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RestClient {
    public static final String REQUESTED_WITH_VALUE = "XMLHttpRequest";
    private static final String REQUESTED_WITH = "X-Requested-with";
    private static final String AUTHORISATION_KEY = "BB-Access-Token";
    private static final String SECURE_TOKEN = "secure-token";
    private static final String REFRESH_TOKEN_KEY = "bb-token";
    private static final String DOMAIN_KEY = "x-domain";
    private static final String ORIGIN_KEY = "x-origin";
    private static final String WF_ID = "wfid";
    private static final String WF_NAME = "wfname";

    private static final String TAG = "RestClient";
    private static Retrofit unAuthorisedRetrofitClient;
    private static Retrofit authorisedRetrofitClient;
    private static Context globalContext;

    public RestClient() {
    }

    public static RTApiInterface getAuthorisedClient(Context context) {
        globalContext = context;
        authorisedRetrofitClient = getRetrofitClient(true, false, false);
        return authorisedRetrofitClient.create(RTApiInterface.class);
    }

    public static RTApiInterface getUnAuthorisedClient(Context context) {
        globalContext = context;
        unAuthorisedRetrofitClient = getRetrofitClient(false, false, false);
        return unAuthorisedRetrofitClient.create(RTApiInterface.class);
    }

    public static RTApiInterface getUnAuthorisedClient(Context context, boolean isBaseUrlChange) {
        globalContext = context;
        unAuthorisedRetrofitClient = getRetrofitClient(false, false, isBaseUrlChange);
        return unAuthorisedRetrofitClient.create(RTApiInterface.class);
    }

    public static RTApiInterface getRefreshToken(Context context) {
        globalContext = context;
        unAuthorisedRetrofitClient = getRetrofitClient(true, true, true);
        return unAuthorisedRetrofitClient.create(RTApiInterface.class);
    }

    public static Retrofit getRetrofitClient(final boolean isAuthenticateAdded, final boolean refreshToken, boolean isBaseUrlChange) {
        String baseUrl = "https://staging-dexter.pushengage.com/p/v1/";

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okClientBuilder = new OkHttpClient().newBuilder();

        okClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder request = chain.request().newBuilder();
                request.addHeader("content-type", "application/json");

                return chain.proceed(request.build());
            }
        });

//        if (BuildConfig.DEBUG) {
        okClientBuilder.addInterceptor(interceptor);
//        }

        okClientBuilder.readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofitClient;
    }


    public interface RTApiInterface {
        @POST("subscriber/add")
        Call<AddSubscriberResponse> addSubscriber(@Body AddSubscriberRequest addSubscriberRequest);

        @GET("subscriber/{id}")
        Call<GenricResponse> subscriberDetails(@Path("id") String id, @Query("fields") List<String> fields);

        @PUT("subscriber/{id}")
        Call<GenricResponse> updateSubscriberHash(@Path("id") String id, @Body JSONObject jsonObject);

        @GET("subscriber/{id}/attribute")
        Call<GenricResponse> getSubscriberAttributes(@Path("id") String id);

        @DELETE("subscriber/{id}/attribute")
        Call<GenricResponse> deleteSubscriberAttributes(@Path("id") String id, @Body List<String> value);

        @POST("subscriber/{id}/attribute")
        Call<GenricResponse> addAttributes(@Path("id") String id, @Body JSONObject jsonObject);

        @POST("subscriber/profile-id/add")
        Call<GenricResponse> addProfileId(@Body AddProfileIdRequest addProfileIdRequest);

        @POST("subscriber/segments/add")
        Call<GenricResponse> addSegments(@Body AddSegmentRequest addSegmentRequest);

        @POST("subscriber/segments/remove")
        Call<GenricResponse> removeSegments(@Body AddSegmentRequest addSegmentRequest);

        @POST("subscriber/dynamicSegments/add")
        Call<GenricResponse> addDynamicSegments(@Body AddDynamicSegmentRequest addDynamicSegmentRequest);

        @POST("subscriber/dynamicSegments/remove")
        Call<GenricResponse> removeDynamicSegments(@Body RemoveDynamicSegmentRequest removeDynamicSegmentRequest);

        @POST("subscriber/segments/segmentHashArray")
        Call<GenricResponse> getSegmentHashArray(@Body SegmentHashArrayRequest segmentHashArrayRequest);

        @GET("subscriber/check/{id}")
        Call<GenricResponse> checkSubscriberHash(@Path("id") String id);

        @POST("subscriber/updatetriggerstatus")
        Call<GenricResponse> updateTriggerStatus(@Query("swv") String  swv, @Query("bv") String  bv);

        @POST("subscriber/updatesubscriberstatus")
        Call<GenricResponse> updateSubscriberStatus(@Query("swv") String  swv, @Query("bv") String  bv, @Body UpdateSubscriberStatusRequest updateSubscriberStatusRequest);

    }
}