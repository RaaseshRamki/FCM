package com.pushengage.fcmlibrary;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushengage.fcmlibrary.model.request.AddSubscriberRequest;
import com.pushengage.fcmlibrary.model.response.AddSubscriberResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

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
        String baseUrl = "https://staging-dexter.pushengage.com/";

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
        @POST("p/v1/subscriber/add")
        Call<AddSubscriberResponse> addSubscriber(@Body AddSubscriberRequest addSubscriberRequest);

//        @GET("licence/getChannelConfig")
//        Call<BBChannelConfigResponse> channelConfig();

    }
}