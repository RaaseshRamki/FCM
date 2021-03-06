package com.pushengage.fcmlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pushengage.fcmlibrary.helper.Prefs;
import com.pushengage.fcmlibrary.model.request.AddDynamicSegmentRequest;
import com.pushengage.fcmlibrary.model.request.AddProfileIdRequest;
import com.pushengage.fcmlibrary.model.request.AddSegmentRequest;
import com.pushengage.fcmlibrary.model.request.AddSubscriberRequest;
import com.pushengage.fcmlibrary.model.request.RemoveDynamicSegmentRequest;
import com.pushengage.fcmlibrary.model.request.RemoveSegmentRequest;
import com.pushengage.fcmlibrary.model.request.SegmentHashArrayRequest;
import com.pushengage.fcmlibrary.model.request.UpdateSubscriberStatusRequest;
import com.pushengage.fcmlibrary.model.request.UpdateTriggerStatusRequest;
import com.pushengage.fcmlibrary.model.response.AddSubscriberResponse;
import com.pushengage.fcmlibrary.model.response.GenricResponse;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FCMHelper {

    private static Context context;
    private static String TAG = "FCMHelper";
    static FcmInterface fcmInterface;
    public static final String PERMISSION_STATUS = "PERMISSION_STATUS", SUBSCRIBE = "SUBSCRIBE", SUBSCRIPTION_STATUS = "SUBSCRIPTION_STATUS", TOKEN = "TOKEN";
    private static FusedLocationProviderClient fusedLocationClient;
    private static Prefs prefs;

    public static void getPermissionStatus() {
        //Android doesn't require special permissions for push notifications. By default it is enabled.
        fcmInterface.callback(PERMISSION_STATUS, true, "");
    }

    public static void subscribe(String topic) {
        //Enables or disables auto-initialization of Firebase Cloud Messaging.
        //When enabled, Firebase Cloud Messaging generates a registration token on app startup if there is no valid one and periodically sends data to the Firebase backend to validate the token.
        //This setting is persisted across app restarts and overrides the setting specified in your manifest.
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        //Subscribes to topic in the background.
        //The subscribe operation is persisted and will be retried until successful.
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, String.valueOf(task.isSuccessful()));
                        fcmInterface.callback(SUBSCRIBE, task.isSuccessful(), "");
                    }
                });
    }

    public static void getSubscriptionStatus() {
        //Firebase Cloud Messaging doesn't explicity provide a method or callback to check status of subscription.
        //Hence we consider that our device is subscribed if we get a token or else if it failed to return a token we consider it as not subscribed.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.d(TAG, String.valueOf(task.isSuccessful()));
                        fcmInterface.callback(SUBSCRIPTION_STATUS, task.isSuccessful(), "");
                    }
                });

    }

    public static void getSubscriptionToken() {
        //Returns the FCM registration token for this Firebase project.
        //This creates a Firebase Installations ID, if one does not exist, and sends information about the application and the device where it's running to the Firebase backend.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            fcmInterface.callback(TOKEN, task.isSuccessful(), "");
                            return;
                        } else {
                            // Get new FCM registration token
                            String token = task.getResult();
                            Log.d(TAG, token);
                            prefs.setDeviceToken(token);
                            checkLocationPermission(token);
                            fcmInterface.callback(TOKEN, task.isSuccessful(), token);
                        }
                    }
                });
    }

    private FCMHelper() {
        // Library Initialized here
    }

    private FCMHelper(Context context, FcmInterface fcmInterface) {
        // Library Initialized here
        this.context = context;
        this.fcmInterface = fcmInterface;
        prefs = new Prefs(context);
        FirebaseApp.initializeApp(context);
    }

    public static class Builder {
        private Context context;
        private FcmInterface fcmInterface;

        public Builder addContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder addFCMInterface(FcmInterface fcmInterface) {
            this.fcmInterface = fcmInterface;
            return this;
        }

        public FCMHelper build() {
            return new FCMHelper(context, fcmInterface);
        }

    }

    public interface FcmInterface {
        void callback(String type, Boolean status, String token);
    }

    public static void checkLocationPermission(String token) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Toast.makeText(context, "Permission Already Granted", Toast.LENGTH_LONG).show();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            String name = null, address = null, city = null, state = null, country = null, pinCode = null, lat = null, lng = null;
                            if (location != null) {
                                try {
                                    List<Address> addresses = getLocation(context, location.getLatitude(), location.getLongitude());
                                    if (addresses != null) {
                                        name = addresses.get(0).getFeatureName();
                                        address = addresses.get(0).getAddressLine(0);
                                        city = addresses.get(0).getLocality();
                                        pinCode = addresses.get(0).getPostalCode();
                                        country = addresses.get(0).getCountryName();
                                        state = addresses.get(0).getAdminArea();
                                        if (!TextUtils.isEmpty(address) && address.indexOf(city) != 0) {
                                            address = address.substring(0, address.indexOf(city) - 1);
                                        }
                                        if (TextUtils.isEmpty(state)) {
                                            state = addresses.get(0).getCountryName();
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error while fetching address");
                                }
                            }
                            String timeZone = getTimeZone();
                            String language = Locale.getDefault().getLanguage();
                            String siteId = "49438";
                            String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
                            String versionRelease = Build.VERSION.RELEASE;
                            String applicationName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
                            String packageName = context.getPackageName();
                            ;


                            AddSubscriberRequest addSubscriberRequest = new AddSubscriberRequest();
                            AddSubscriberRequest.BrowserInfo browserInfo = addSubscriberRequest.new BrowserInfo("android", versionRelease, language, deviceName);
                            AddSubscriberRequest.Subscription subscription = addSubscriberRequest.new Subscription(token, packageName);
                            AddSubscriberRequest.GeoInfo geoInfo = addSubscriberRequest.new GeoInfo(timeZone, country, state, city);
                            addSubscriberRequest = new AddSubscriberRequest(siteId, browserInfo, subscription, applicationName, geoInfo, false);
                            callAddSubscriberAPI(addSubscriberRequest);
                        }

                    });

        }
    }

    private static String getTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("z", Locale.getDefault());
        return date.format(currentLocalTime);
    }

    public static List<Address> getLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public static void callAddSubscriberAPI(AddSubscriberRequest addSubscriberRequest) {
        Call<AddSubscriberResponse> addSubscriberResponseCall = RestClient.getUnAuthorisedClient(context).addSubscriber(addSubscriberRequest);
        addSubscriberResponseCall.enqueue(new Callback<AddSubscriberResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddSubscriberResponse> call, @NonNull Response<AddSubscriberResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    AddSubscriberResponse addSubscriberResponse = response.body();
                    prefs.setHash(addSubscriberResponse.getData().getSubscriberHash());
                    Log.d(TAG, addSubscriberResponse.getData().getSubscriberHash());
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddSubscriberResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void getSubscriberHashDetails(List<String> values) {
        Call<GenricResponse> subscriberDetailsResponseCall = RestClient.getUnAuthorisedClient(context).subscriberDetails(prefs.getHash(), values);
        subscriberDetailsResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void updateSubscriberHashDetails(JSONObject jsonObject) {
        Call<GenricResponse> updateSubscriberDetailsResponseCall = RestClient.getUnAuthorisedClient(context).updateSubscriberHash(prefs.getHash(), jsonObject);
        updateSubscriberDetailsResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void getSubscriberAttributes() {
        Call<GenricResponse> getSubscriberAttributesResponseCall = RestClient.getUnAuthorisedClient(context).getSubscriberAttributes(prefs.getHash());
        getSubscriberAttributesResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void deleteSubscriberAttributes(List<String> values) {
        Call<GenricResponse> deleteSubscriberAttributesResponseCall = RestClient.getUnAuthorisedClient(context).deleteSubscriberAttributes(prefs.getHash(), values);
        deleteSubscriberAttributesResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void addSubscriberAttributes(JSONObject obj) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(obj.toString());
        Call<GenricResponse> addSubscriberAttributesResponseCall = RestClient.getUnAuthorisedClient(context).addAttributes(prefs.getHash(), jsonObject);
        addSubscriberAttributesResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void addProfileId(AddProfileIdRequest addProfileIdRequest) {
        Call<GenricResponse> addProfileIdResponseCall = RestClient.getUnAuthorisedClient(context).addProfileId(addProfileIdRequest);
        addProfileIdResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void addSegment(AddSegmentRequest addSegmentRequest) {
        addSegmentRequest.setDeviceTokenHash(prefs.getHash());
        Call<GenricResponse> addSegmentResponseCall = RestClient.getUnAuthorisedClient(context).addSegments(addSegmentRequest);
        addSegmentResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void removeSegment(RemoveSegmentRequest removeSegmentRequest) {
        removeSegmentRequest.setDeviceToken(prefs.getDeviceToken());
        Call<GenricResponse> removeSegmentResponseCall = RestClient.getUnAuthorisedClient(context).removeSegments(removeSegmentRequest);
        removeSegmentResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void addDynamicSegment(AddDynamicSegmentRequest addDynamicSegmentRequest) {
        addDynamicSegmentRequest.setDeviceToken(prefs.getDeviceToken());
        Call<GenricResponse> addDynamicSegmentResponseCall = RestClient.getUnAuthorisedClient(context).addDynamicSegments(addDynamicSegmentRequest);
        addDynamicSegmentResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void removeDynamicSegment(RemoveDynamicSegmentRequest removeDynamicSegmentRequest) {
        Call<GenricResponse> removeDynamicSegmentResponseCall = RestClient.getUnAuthorisedClient(context).removeDynamicSegments(removeDynamicSegmentRequest);
        removeDynamicSegmentResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void getSegmentHashArray(SegmentHashArrayRequest segmentHashArrayRequest) {
        segmentHashArrayRequest.setDeviceTokenHash(prefs.getHash());
        Call<GenricResponse> segmentHashArrayResponseCall = RestClient.getUnAuthorisedClient(context).getSegmentHashArray(segmentHashArrayRequest);
        segmentHashArrayResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void checkSubscriberHash() {
        Call<GenricResponse> checkSubscriberHashResponseCall = RestClient.getUnAuthorisedClient(context).checkSubscriberHash(prefs.getHash());
        checkSubscriberHashResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void updateTriggerStatus(UpdateTriggerStatusRequest updateTriggerStatusRequest) {
        updateTriggerStatusRequest.setDeviceTokenHash(prefs.getHash());
        Call<GenricResponse> updateTriggerStatusResponseCall = RestClient.getUnAuthorisedClient(context).updateTriggerStatus(updateTriggerStatusRequest);
        updateTriggerStatusResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void updateSubscriberStatus(UpdateSubscriberStatusRequest updateSubscriberStatusRequest) {
        updateSubscriberStatusRequest.setDeviceTokenHash(prefs.getHash());
        Call<GenricResponse> updateSubscriberStatusResponseCall = RestClient.getUnAuthorisedClient(context).updateSubscriberStatus(updateSubscriberStatusRequest);
        updateSubscriberStatusResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void notificationCLick() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("referer","https://pushengage.com/service-worker.js");
        Call<GenricResponse> notificationClickResponseCall = RestClient.getUnAuthorisedClient(context, headerMap).notificationClick(prefs.getHash(), "N-49438-37382-df4bfe55fc37266b552c991f83923fbcfb0fbbe4930cf355ed3e849daacc4ffb");
        notificationClickResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

    public static void notificationView() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("referer","https://pushengage.com/service-worker.js");
        Call<GenricResponse> notificationViewResponseCall = RestClient.getUnAuthorisedClient(context, headerMap).notificationView(prefs.getHash(),"N-49438-37382-df4bfe55fc37266b552c991f83923fbcfb0fbbe4930cf355ed3e849daacc4ffb");
        notificationViewResponseCall.enqueue(new Callback<GenricResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenricResponse> call, @NonNull Response<GenricResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    GenricResponse genricResponse = response.body();
                } else {
                    Log.e(TAG, "API Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenricResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure");
            }
        });
    }

}
