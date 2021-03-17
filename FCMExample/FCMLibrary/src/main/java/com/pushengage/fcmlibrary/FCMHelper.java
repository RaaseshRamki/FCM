package com.pushengage.fcmlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class FCMHelper extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks{

    private static Context context;
    private static String TAG = "FCMHelper";
    static FcmInterface fcmInterface;
    static Activity activity;
    public static final String PERMISSION_STATUS = "PERMISSION_STATUS", SUBSCRIBE = "SUBSCRIBE", SUBSCRIPTION_STATUS = "SUBSCRIPTION_STATUS", TOKEN = "TOKEN";


    private static final int RC_LOCATION_PERM = 124;
    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION};

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
                            checkLocationPermission();
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
        activity = FCMHelper.this;
        FirebaseApp.initializeApp(context);
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

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

    public static void checkLocationPermission() {
        /*f (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Toast.makeText(context, "Permission Already Granted", Toast.LENGTH_LONG).show();

        } else {
            FCMHelper activity = new FCMHelper();
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }*/
        locationAndContactsTask();
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }
*/

    @AfterPermissionGranted(RC_LOCATION_PERM)
    public static void locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            Toast.makeText(context, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    activity,
                    "Location Permission",
                    RC_LOCATION_PERM,
                    LOCATION);
        }
    }

    private static boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(context, LOCATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = "yes";
            String no = "no";

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    hasLocationAndContactsPermissions() ? yes : no,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
