/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.macinternetservices.sablebusinessdirectory;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.Random;

import static com.macinternetservices.sablebusinessdirectory.MainActivity.context;
import static com.macinternetservices.sablebusinessdirectory.MainActivity.pref;

/**
 * Displays rationale for allowing the activity recognition permission and allows user to accept
 * the permission. After permission is accepted, finishes the activity so main activity can
 * show transitions.
 */
public class PermissionRationaleActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "PermissionRational";


    /* Id to identify Activity Recognition permission request. */
    //private static final int PERMISSION_BACKGROUD_LOCATION = 45;
    private static final int REQUEST_READ_PHONE_STATE = 110 ,
            REQUEST_ACCESS_FINE_LOCATION = 111,
            REQUEST_WRITE_STORAGE = 112,
            //REQUEST_INTERNET_STATE =113,
            REQUEST_ACCESS_COARSE_LOCATION = 114,
            REQUEST_READ_STORAGE = 115,
            REQUEST_CAMERA =116,
            //REQUEST_NETWORK = 117,
            REQUEST_CALL_PHONE = 118,
            REQUEST_BACKGROUND_LOCATION = 119,
            REQUEST_FOREGROUND_SERVICE = 120;


    TextSwitcher textSwitcher;
    ImageView imageView;
    Button deny_permission_request, approve_permission_request;
    private static final int FRAME_TIME_MS = 4000;

    private Handler imageSwitchHandler;
    ProgressBar progressBar;
    int version = Build.VERSION.SDK_INT;
    int reqVersion = Build.VERSION_CODES.Q;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_rationale);
        Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation imgAnimationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        /**
         * ABOUT US
         */
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
       imageView = findViewById(R.id.imageView);
       imageView.setVisibility(View.GONE);
        ImageView imageView = new ImageView(getApplicationContext());

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams params = new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);

        deny_permission_request =findViewById(R.id.deny_permission_request);
        deny_permission_request.setVisibility(View.GONE);
        approve_permission_request = findViewById(R.id.approve_permission_request);
        approve_permission_request.setVisibility(View.GONE);

        /*imageView2 = findViewById(R.id.imageView2);
        imageView2.setVisibility(View.GONE);
        ImageView imageView2 = new ImageView(getApplicationContext());

        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams imageView2params = new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView2.setLayoutParams(imageView2params); */


        imageSwitchHandler = new Handler();
        imageSwitchHandler.post(runnableCode);

        /**
         *  txt switchers for animations
         */
        //textSwitcherLayout = findViewById(R.id.textSwitcherLayout);
        LinearLayout textSwitcherLayout = new LinearLayout(getApplicationContext());
        ViewGroup.LayoutParams textSwitcherLayoutParams = new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textSwitcherLayout.setLayoutParams(textSwitcherLayoutParams);
        textSwitcherLayout.setAnimation(imgAnimationIn);
        textSwitcherLayout.setAnimation(imgAnimationOut);
        textSwitcherLayout.post(runnableCode);

        textSwitcher = findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(getApplicationContext());
            textView.setLayoutParams(new TextSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(22);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            return textView;
        });
    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    public void onClickApprovePermissionRequest(View view) {
        Log.e(TAG, "onClickApprovePermissionRequest()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionsQ();
        } else {
            checkPermissions();
        }


    }
    //@RequiresApi(api = Build.VERSION_CODES.Q)
    private  boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
         }  else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this,
                     new String[]{Manifest.permission.FOREGROUND_SERVICE},
                     REQUEST_FOREGROUND_SERVICE);
         } else {
            //Ask se to geo to settings and manually allow permissions
            showDialog("", "You have denied some permissions.  Allow all permissions at [Settings] > [Permissions]",
                    "Go to Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //Go to app settings
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    },
                    "No, Exit app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ;
                            fileList();
                        }
                    }, false);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private  boolean checkPermissionsQ() {
        //progressBar.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_PHONE_STATE);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
        } else  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);{
                //Show permission explanation dialog...
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Special Permissions Required");
                alertBuilder.setMessage("This app requires special permission to access your current location when running in the background.  Tap 'Continue' and select 'Allow all the time' from the next screen to receive alerts.");
                alertBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(PermissionRationaleActivity.this,
                                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                REQUEST_BACKGROUND_LOCATION);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        }  else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.FOREGROUND_SERVICE},
                    REQUEST_FOREGROUND_SERVICE);
        } else {
            //Ask se to geo to settings and manually allow permissions
            progressBar.setVisibility(View.GONE);
            showDialog("", "You have denied some permissions.  Allow all permissions at [Settings] > [Permissions]",
                    "Go to Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //Go to app settings
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    },
                    "No, Exit app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ;
                            fileList();
                        }
                    }, false);
        }
        return true;
    }

    public AlertDialog showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnClick,
                                  String negativeLabel, DialogInterface.OnClickListener negativeOnClick, boolean isCancelAble){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public void onClickDenyPermissionRequest(View view) {
        Log.e(TAG, "onClickDenyPermissionRequest()");
        showDialog("", "You have denied some permissions.  Allow all permissions at [Settings] > [Permissions]",
                "Go to Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //Go to app settings
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                },
                "No, Exit app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ;
                        fileList();
                    }
                }, false);
        //finish();
    }

    /*
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (/*ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || */
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            //ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                progressBar.setVisibility(View.VISIBLE);
                checkPermissionsQ();
            } else {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();            //h.postDelayed(r, 1500);
            }
        } else {
            if (/*ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || */
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            //ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED/* ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                progressBar.setVisibility(View.VISIBLE);
                checkPermissions();
            } else {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }


    /**
     * ANIMATIONS
     */


    private Runnable runnableCode = new Runnable() {

       int count=0;
       //progressBar.setVisibility(View.VISIBLE);


        // String image;
        @Override
        public void run() {
            Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

            String[] text = {
                    "Hello and welcome to The Sable Business Directory mobile app.",

                    "We make it easier to find, rate and review black owned businesses.",

                    "Our geo-search technology alerts you when you're near a registered black owned business.",

                    "It's FREE to register a business and get alerts.",

                    "Rate and review black owned everytime you shop.",

                    "We insure high quality feedback by requiring users to login before adding or reviewing a listing.",

                    "We need your permission to alert you when you're near a black owned business.",

                    "The app requires special permission to access your location when not running to alert you when you're near a registered black owned business.",

                    "Click begin and allow all of the following permissions when prompted."

            };

            int[] images = {R.mipmap.hello_foreground, R.mipmap.showing_right_foreground,
                    R.mipmap.one_of_akind_foreground, R.mipmap.showing_tablet_foreground, R.mipmap.holding_phone_foreground, R.mipmap.making_thumbs_up_foreground,
                    R.mipmap.online_reviews_foreground, R.mipmap.showing_with_left_hand_foreground, R.mipmap.smiling_peace_foreground};

            if (count == text.length) {
                count = 0;
            }
            switch (count) {

                case 8:
                    imageView.setImageResource(images[count]);
                    imageView.setAnimation(imgAnimationIn);
                    imageView.setVisibility(View.VISIBLE);

                    textSwitcher.setText(text[count]);
                    textSwitcher.setAnimation(imgAnimationIn);
                    textSwitcher.setVisibility(View.VISIBLE);

                    approve_permission_request.setAnimation(imgAnimationIn);
                    approve_permission_request.setVisibility(View.VISIBLE);

                    imageSwitchHandler.removeCallbacks(this);
                    count++;
                    break;
                case 2:
                case 4:
                case 6:
                case 1:
                case 3:
                case 5:
                case 7:
                default:
                    imageView.setImageResource(images[count]);
                    imageView.setAnimation(imgAnimationIn);
                    imageView.setVisibility(View.VISIBLE);

                    textSwitcher.setText(text[count]);
                    textSwitcher.setAnimation(imgAnimationIn);
                    textSwitcher.setVisibility(View.VISIBLE);

                    imageSwitchHandler.postDelayed(this, FRAME_TIME_MS);
                    count++;
                    break;
            }
        }
    };
}
