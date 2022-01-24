package com.icicisecurities.idirectalpha;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Log;
import com.shephertz.app42.paas.sdk.android.push.PushNotification;
import com.shephertz.app42.paas.sdk.android.push.PushNotificationService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App42API.initialize(
                getApplicationContext(),
                "941b846198d8bf0757a8c9016545f4358f3ab1d2f3c19735d399afbe9c976c6d",
                "512e9692e9f670ab60d2484d0f8e79c360ba20731b673d1eb6f8fb891f586465");
        App42Log.setDebug(true);
        App42API.setLoggedInUser("Aarti");
        initializeNotification();
    }
    private void initializeNotification()
    {
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        PushNotificationService pushNotificationService = App42API.buildPushNotificationService();

                        pushNotificationService.storeDeviceToken("Aarti", token, new App42CallBack() {
                            public void onSuccess(Object response)
                            {
                                PushNotification pushNotification  = (PushNotification)response;
                                System.out.println("userName is " + pushNotification.getUserName());
                                System.out.println("DeviceToken is " +  pushNotification.getDeviceToken());
//            App42Log.setDebug(true);
//            getPushServiceObj().trackPush();
//            App42Log.setDebug(true);

                            }
                            public void onException(Exception ex)
                            {
                                System.out.println("Exception Message"+ex.getMessage());
                            }
                        });
                        // Log and toast

                    }
                });



    }
}