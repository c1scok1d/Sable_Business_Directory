package com.macinternetservices.sablebusinessdirectory;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.macinternetservices.sablebusinessdirectory.model.Person;
import com.ramotion.circlemenu.CircleMenuView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import org.acra.*;

public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback/*,
        ActivityCompat.OnRequestPermissionsResultCallback*/ {
    CoreConfigurationBuilder builder;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.JSON);

        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setReportAsFile(true)
                .setReportFileName("errorReport.txt")
                .setMailTo("admin@thesablebusinessdirectory.com")
                .setSubject("Sable Mobile App Error Report")
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class)
                .setResText(R.string.acraErrorMesage)
                .setResCommentPrompt(R.string.acraAddComment)
                .setEnabled(true);
        builder.setReportContent(ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE);
    }

    /**
     * permissions request code
     */
    // private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    //private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
    //      Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    public static Double latitude, longitude;

    public static TextView tvMore, tvUserName, tvWpUserId, tvCity, tvCategories, tvLoading, noListingsTextView, fooListingsTextView;
    Button  btnShowListings;
    LoginButton login_button2;
    RecyclerView verticalRecyclerView, featuredRecyclervView, recentListingsRecyclervView, recentReviewsRecyclervView;
    //ProgressBar progressBar;
    LinearLayoutManager mLayoutManager, featuredRecyclerViewLayoutManager,
            recentListingsRecyclerViewLayoutManager, recentReviewsRecyclerViewLayoutManager;
    LinearLayout recentReviewsLayout, recentReviewsRecyclerLayout;
    RelativeLayout loadingLayout, noListingsAnimationLayout;


    VerticalAdapter verticalAdapter;
    FeaturedListAdapter featuredListAdapter;
    RecentListingsAdapter recentListingsAdapter;
    RecentReviewListingsAdapter recentReviewListingsAdapter;

    Animation imgAnimationIn, imgAnimationOut = null;

    public static String baseURL = "https://www.thesablebusinessdirectory.com", radius, address, state, country,
            zipcode, city, street, bldgno, todayRange, username = "android_app", isOpen, email,
            password = "mroK zH6o wOW7 X094 MTKy fwmY", userName, userEmail, userImage, userId, firstName = "", lastName;

    public static ArrayList<ListingsModel> verticalList = new ArrayList<>();
    public static ArrayList<String> listingName = new ArrayList<>();
    public static ArrayList<ListingsModel> featuredList = new ArrayList<>();
    public static ArrayList<ListingsModel> recentList = new ArrayList<>();
    public static ArrayList<RecentReviewListingsModel> recentReviewList = new ArrayList<>();
    public static ArrayList<ListingsModel> locationMatch = new ArrayList<>();
    public static ArrayList<Person> mapLocations = new ArrayList<>();
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS
            * DateUtils.HOUR_IN_MILLIS;
    static public boolean geofencesAlreadyRegistered = false;
    public static HashMap<String, SimpleGeofence> geofences = new HashMap<String, SimpleGeofence>();
    ArrayList<String> userActivityArray = new ArrayList<>();
    ImageView ivUserImage, spokesperson, ivLoading, noListingsImageView, fooListingImageView, ivSettings, ivAlertOn, ivAlertOff;
    ProgressBar spinner;

    private static final int FRAME_TIME_MS = 8000;

    Cache cache;

    AutoCompleteTextView searchView;
    public static LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();

    public static GoogleMap mMap;
    //private boolean isRestore;

    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    //private FusedLocationProviderClient fusedLocationClient;

    LocationManager locationManager;
    Location location;
    public static Marker currentMarker;
    CallbackManager fbLogincallbackManager;
    //private AccessTokenTracker accessTokenTracker;
    public static boolean isLoggedIn = false;
    boolean kickItOff = true;


    public static AccessToken accessToken = AccessToken.getCurrentAccessToken();

    TextSwitcher textSwitcher, textSwitcher3;
    private SlidingUpPanelLayout mLayout;


    //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static Retrofit retrofit;

    // check for Internet Connectivity
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("thesablebusinessdirectory.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * cache JSON based on network connectivity
     */

    static Interceptor onlineInterceptor = chain -> {
        okhttp3.Response response = chain.proceed(chain.request());
        int maxAge = 300; // read from cache for 60 seconds even if there is internet connection
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAge)
                .removeHeader("Pragma")
                .build();
    };

    static Interceptor offlineInterceptor = chain -> {
        Request request = chain.request();
        if (!isInternetAvailable()) {
            int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    };

    ImageSwitcher imageSwitcher3;
    LinearLayout  textSwitcher3Layout, sliderLayout;
    private Handler imageSwitchHandler;
    SharedPreferences pref;


    public static Context context;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
            }
        }
    };

    /**
     * @param savedInstanceState
     */
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(getLayoutId());
        setCache(getApplicationContext());

        /******* Create SharedPreferences *******/

        pref = getApplicationContext().getSharedPreferences("com.example.sable.PREFERENCE_FILE_KEY", MODE_PRIVATE);
        //SharedPreferences.Editor editor = pref.edit();

/*

// Storing data as KEY/VALUE pair

    editor.putBoolean("key_name1", true);           // Saving boolean - true/false
    editor.putInt("key_name2", "int value");        // Saving integer
    editor.putFloat("key_name3", "float value");    // Saving float
    editor.putLong("key_name4", "long value");      // Saving long
    editor.putString("key_name5", "string value");  // Saving string

    // Save the changes in SharedPreferences
    editor.commit(); // commit changes


// Get SharedPreferences data

// If value for key not exist then return second param value - In this case null

    pref.getBoolean("key_name1", null);         // getting boolean
    pref.getInt("key_name2", null);             // getting Integer
    pref.getFloat("key_name3", null);           // getting Float
    pref.getLong("key_name4", null);            // getting Long
    pref.getString("key_name5", null);          // getting String




// Deleting Key value from SharedPreferences

    editor.remove("key_name3"); // will delete key key_name3
    editor.remove("key_name4"); // will delete key key_name4

    // Save the changes in SharedPreferences
    editor.commit(); // commit changes

// Clear all data from SharedPreferences

     editor.clear();
     editor.commit(); // commit changes

*/

      /*  // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }*/
        //This starts the access token tracking


        // Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        //Animation imgAnimationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        //isRestore = savedInstanceState != null;
        ivSettings = findViewById(R.id.btnSettings);
        loadingLayout = findViewById(R.id.loadingLayout);
        tvLoading = findViewById(R.id.tvLoading);
        tvLoading.setVisibility(View.GONE);
        tvMore = findViewById(R.id.tvMore);
        tvMore.setVisibility(View.GONE);
        sliderLayout = findViewById(R.id.sliderLayout);
        sliderLayout.setVisibility(View.GONE);
        //progressBar = findViewById(R.id.progressBar1);
        recentReviewsRecyclerLayout = findViewById(R.id.recentReviewsRecyclerLayout);
        recentReviewsRecyclerLayout.setVisibility(View.GONE);
        recentReviewsLayout = findViewById(R.id.recentReviewsLayout);
        recentReviewsLayout.setVisibility(View.GONE);
        ivLoading = findViewById(R.id.ivLoading);
        ivLoading.setVisibility(View.GONE);
        noListingsImageView = findViewById(R.id.noListingsImageView);
        noListingsImageView.setVisibility(View.GONE);
        noListingsTextView = findViewById(R.id.noListingsTextView);
        noListingsTextView.setVisibility(View.GONE);
        fooListingImageView = findViewById(R.id.fooListingsImageView);
        fooListingImageView.setVisibility(View.GONE);
        fooListingsTextView = findViewById(R.id.fooListingsTextView);
        fooListingsTextView.setVisibility(View.GONE);
        spinner = findViewById(R.id.progressBar1);
        imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        imgAnimationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);


        /**
         * ABOUT US
         */

        login_button2 = findViewById(R.id.login_button2);
        login_button2.setVisibility(View.GONE);

        /*login_button3 = findViewById(R.id.login_button3);
        login_button3.setVisibility(View.GONE); */

        facebookLogin();


        textSwitcher3 = findViewById(R.id.textSwitcher3);
        textSwitcher3.setFactory(() -> {
            TextView textView = new TextView(getApplicationContext());
            textView.setLayoutParams(new TextSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(22);
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            return textView;
        });

        imageSwitcher3 = findViewById(R.id.imageSwitcher3);
        imageSwitcher3.setAnimation(imgAnimationIn);
        imageSwitcher3.setAnimation(imgAnimationOut);
        imageSwitcher3.setVisibility(View.GONE);


        ImageView imageView3 = new ImageView(getApplicationContext());
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);

        ViewGroup.LayoutParams imageView3params = new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageView3.setLayoutParams(imageView3params);

        imageSwitchHandler = new Handler();
        imageSwitchHandler.post(runnableCode);

        /**
         *  strt fuckin' around with getting linearLayouts to fade in and out
         */

        textSwitcher3Layout = findViewById(R.id.textSwitcher3Layout);
        textSwitcher3Layout.setAnimation(imgAnimationIn);
        textSwitcher3Layout.setAnimation(imgAnimationOut);
        textSwitcher3Layout.setVisibility(View.GONE);

        ViewGroup.LayoutParams textSwitcherLayoutParams = new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout textSwitcher3Layout = new LinearLayout(getApplicationContext());
        textSwitcher3Layout.setLayoutParams(textSwitcherLayoutParams);

        textSwitcher3Layout.post(runnableCode);

        /**
         * end fuckin' around with getting lienarlayouts to fade in and out
         */

///END ABOUT US////

        /**
         * Featured Listings
         */
        featuredListAdapter = new FeaturedListAdapter(featuredList, getApplicationContext());
        featuredRecyclervView = findViewById(R.id.featuredListingsRecyclerView);
        featuredRecyclervView.setHasFixedSize(true);
        featuredRecyclervView.setAdapter(featuredListAdapter);
        featuredRecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclervView.setLayoutManager(featuredRecyclerViewLayoutManager);

        /**
         * Recent Listings
         */
        recentListingsAdapter = new RecentListingsAdapter(recentList, getApplicationContext());
        recentListingsRecyclervView = findViewById(R.id.recentListingsRecyclerView);
        recentListingsRecyclervView.setHasFixedSize(true);
        recentListingsRecyclervView.setAdapter(recentListingsAdapter);
        recentListingsRecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recentListingsRecyclervView.setLayoutManager(recentListingsRecyclerViewLayoutManager);

        /**
         * Recent Reviews
         */

        recentReviewListingsAdapter = new RecentReviewListingsAdapter(recentReviewList, getApplicationContext());
        recentReviewsRecyclervView = findViewById(R.id.recentReviewsRecyclerView);
        recentReviewsRecyclervView.setHasFixedSize(true);
        recentReviewsRecyclervView.setAdapter(recentReviewListingsAdapter);
        recentReviewsRecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recentReviewsRecyclervView.setLayoutManager(recentReviewsRecyclerViewLayoutManager);

        tvUserName = findViewById(R.id.tvUserName);
        ivUserImage = findViewById(R.id.ivUserImage);
        tvWpUserId = findViewById(R.id.tvWpUserId);
        textSwitcher = findViewById(R.id.textSwitcher1);

        /*
            BEGIN vertical Recycler View
         */
        verticalRecyclerView = findViewById(R.id.verticalRecyclerView);
        mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        verticalRecyclerView.setLayoutManager(mLayoutManager);
        verticalList = new ArrayList<>();
        locationMatch = new ArrayList<>();


        verticalAdapter = new VerticalAdapter(verticalList, userName, userEmail, userImage, userId, MainActivity.this);
        featuredListAdapter = new FeaturedListAdapter(featuredList, MainActivity.this);
        recentListingsAdapter = new RecentListingsAdapter(recentList, MainActivity.this);
        verticalRecyclerView.setAdapter(verticalAdapter);
        verticalRecyclerView.setNestedScrollingEnabled(false);

        featuredRecyclervView.setAdapter(featuredListAdapter);
        featuredRecyclervView.setNestedScrollingEnabled(false);

        recentListingsRecyclervView.setAdapter(recentListingsAdapter);
        recentListingsRecyclervView.setNestedScrollingEnabled(false);

        spokesperson = findViewById(R.id.spokesperson);
        tvCity = findViewById(R.id.tvCity);
        tvMore = findViewById(R.id.tvMore);

       /* btnShowListings = findViewById(R.id.btnShowListings);
        btnShowListings.setVisibility(View.GONE);
        searchView = findViewById(R.id.search);
        searchView.setVisibility(View.GONE); */

        ivAlertOn = findViewById(R.id.ivAlertOn);
        ivAlertOn.setVisibility(View.GONE);
        ivAlertOff = findViewById(R.id.ivAlertOff);
        ivAlertOff.setVisibility(View.GONE);


        /*btnShowListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MarkerClusteringActivity.class));
                finish();
            }
        }); */


        /**
         *  directory search
         */
        /*ArrayAdapter<String> searchViewAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, listingName);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                Map<String, String> query = new HashMap<>();
                query.put("per_page", "100");

                spinner.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                loadingLayout.setAnimation(imgAnimationIn);
                ivLoading.setImageResource(R.mipmap.online_reviews_foreground);
                String listingName = "<font color='#4FC1E9'>" + parent.getItemAtPosition(pos).toString() + "</font>";
                tvLoading.setText(Html.fromHtml(("Thank you for your waiting while we load reviews for " + listingName)));

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(new BasicAuthInterceptor(username, password))
                        .addInterceptor(logging)
                        .addInterceptor(offlineInterceptor)
                        .addInterceptor(onlineInterceptor)
                        .cache(cache)
                        .build();

                retrofit = null;
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
                RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
                // pass JSON data to BusinessListings class for filtering
                Call<List<BusinessListings>> call = service.search(query);

                // get filtered data from BusinessListings class and add to recyclerView adapter for display on screen
                call.enqueue(new Callback<List<BusinessListings>>() {
                    @Override
                    public void onResponse(Call<List<BusinessListings>> call, Response<List<BusinessListings>> response) {

                        // loop through JSON response get parse and output to log
                        for (int i = 0; i < response.body().size(); i++) {
                            if (parent.getItemAtPosition(pos).toString().equals(response.body().get(i).getTitle().getRaw())) {
                                ArrayList<ListingsModel> locationReview = new ArrayList<>();
                                Intent showReviews = new Intent(getApplicationContext(), ListReviewActivity.class);
                                locationReview.add((new ListingsModel(ListingsModel.IMAGE_TYPE,
                                        response.body().get(i).getId(),
                                        response.body().get(i).getTitle().getRaw(),
                                        response.body().get(i).getLink(),
                                        response.body().get(i).getStatus(),
                                        response.body().get(i).getPostCategory().get(0).getName(),
                                        response.body().get(i).getFeatured(),
                                        response.body().get(i).getFeaturedImage().getSrc(),
                                        response.body().get(i).getBldgNo(),
                                        response.body().get(i).getStreet(),
                                        response.body().get(i).getCity(),
                                        response.body().get(i).getRegion(),
                                        response.body().get(i).getCountry(),
                                        response.body().get(i).getZip(),
                                        response.body().get(i).getLatitude(),
                                        response.body().get(i).getLongitude(),
                                        response.body().get(i).getRating(),
                                        response.body().get(i).getRatingCount(),
                                        response.body().get(i).getPhone(),
                                        response.body().get(i).getEmail(),
                                        response.body().get(i).getWebsite(),
                                        response.body().get(i).getTwitter(),
                                        response.body().get(i).getFacebook(),
                                        response.body().get(i).getVideo(),
                                        todayRange,
                                        isOpen,
                                        response.body().get(i).getLogo(),
                                        response.body().get(i).getContent().getRaw(),
                                        response.body().get(i).getFeaturedImage().getThumbnail(),
                                        response.body().get(i).getTitle().getRaw(), new SimpleGeofence(response.body().get(i).getTitle().getRaw(), response.body().get(i).getLatitude(), response.body().get(i).getLongitude(),
                                        100, GEOFENCE_EXPIRATION_IN_MILLISECONDS, response.body().get(i).getFeaturedImage().getThumbnail(),
                                        Geofence.GEOFENCE_TRANSITION_ENTER
                                                | Geofence.GEOFENCE_TRANSITION_DWELL
                                                | Geofence.GEOFENCE_TRANSITION_EXIT))));
                                Bundle locationReviewBundle = new Bundle();
                                locationReviewBundle.putParcelableArrayList("locationReviewBundle", locationReview);
                                showReviews.putExtra("locationReview", locationReview);
                                startActivity(showReviews);
                                finish();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BusinessListings>> call, Throwable t) {
                        ////Log.e("CategoryNumber", " response: " + t);
                    }
                });
            }
        });
        searchView.setThreshold(1);
        searchView.setAdapter(searchViewAdapter);

        if (!userActivityArray.isEmpty()) {
            userActivityArray = this.getIntent().getExtras().getStringArrayList("userActivityArray");
        } */


        /***
         *  BEGIN SLIDE UP
         */


        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // Log.i("onPanelSlide", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (String.valueOf(newState).equals("COLLAPSED")) {
                    tvMore.setTextColor(Color.parseColor("#ffffff"));
                    tvMore.setBackgroundResource(R.drawable.buttonshape);
                    tvMore.setText("Tap For More");
                } else {
                    tvMore.setTextColor(Color.parseColor("#000000"));
                    tvMore.setBackgroundResource(R.drawable.buttonshape_wht);
                    tvMore.setText("Tap To Close");
                }
            }
        });
        mLayout.setFadeOnClickListener(view -> mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED));


        final CircleMenuView menu = findViewById(R.id.circle_menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationEnd| index: " + index);
                switch(index){
                    case 1:
                    case 4: //clear and reload
                        kickItOff = true;
                        mapLocations = new ArrayList<>();
                        geofences = new HashMap<String, SimpleGeofence>();
                        Map<String, String> query = new HashMap<>();

                        if(pref.getString("lastKnownLat", String.valueOf(location.getLatitude())).isEmpty()
                                ||  pref.getString("lastKnownLat", String.valueOf(location.getLatitude())).equals(null)
                                ||  pref.getString("lastKnownLng", String.valueOf(location.getLongitude())).isEmpty()
                                ||  pref.getString("lastKnownLng", String.valueOf(location.getLongitude())).equals(null)){
                            query.put("latitude", String.valueOf(location.getLatitude()));
                            query.put("longitude", String.valueOf(location.getLongitude()));
                            query.put("order", "asc");
                            query.put("orderby", "distance");
                        } else {
                            query.put("latitude", pref.getString("lastKnownLat", String.valueOf(location.getLatitude())));
                            query.put("longitude", pref.getString("lastKnownLng", String.valueOf(location.getLongitude())));
                            query.put("order", "asc");
                            query.put("orderby", "distance");
                        }

                        getRetrofit(query);

                        break;
                    case 2: //search popup
                        //searchView.setVisibility(View.VISIBLE);
                        break;
                    case 3: //add listing
                        if (!isLoggedIn) {
                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(loginIntent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            Toast.makeText(getApplicationContext(), "User must be logged in to add a business listing.", Toast.LENGTH_LONG).show();
                        } else {
                            Intent addListingIntent = new Intent(MainActivity.this, AddListingActivity.class);
                            startActivity(addListingIntent);
                        }
                        break;
                    case 5: //login
                        if (!isLoggedIn) {
                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(loginIntent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } else {
                            //logout
                        }
                        break;
                    default:
                        buildAlertMessageEnableAlerts();
                        break;
                }
            }
        });
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMapAsync(this);

       locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      // location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        spinner.setVisibility(View.VISIBLE); //hide progressBar
        if (isLoggedIn) {
            tvLoading.setText(Html.fromHtml(("Thanks for your patience " + "<font color='#4FC1E9'>" + firstName + "</font>" + "we are searing our database to see if there are any registered black owned businesses near you.")));
        } else {
            tvLoading.setText("Thanks for your patience we are searching our database to see if there are any registered black owned businesses near you.");
        }

        ivLoading.setAnimation(imgAnimationIn);
        ivLoading.setVisibility(View.VISIBLE);
        ivLoading.setImageResource(R.mipmap.online_reviews_foreground);
        tvLoading.setAnimation(imgAnimationIn);
        tvLoading.setVisibility(View.VISIBLE);

    }

    @SuppressLint("MissingPermission")
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("MissingPermission")
    public void onResume() {
        super.onResume();
        kickItOff = true;
        mapLocations = new ArrayList<>();
        geofences = new HashMap<String, SimpleGeofence>();
        /*
            start location listener to get current location minimum alert 30 secs 400M
         */
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,
                    4800, LocationListener);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            buildAlertMessageNoGps();
        }
        /* first run check */
        if (pref.getBoolean("firstrun", true)) {
            pref.edit().putBoolean("alertOn", true).apply();
            pref.edit().putBoolean("firstrun", false).apply();
        }
        Map<String, String> query = new HashMap<>();

            if(pref.getString("lastKnownLat", String.valueOf(location.getLatitude())).isEmpty()
                    ||  pref.getString("lastKnownLat", String.valueOf(location.getLatitude())).equals(null)
                    ||  pref.getString("lastKnownLng", String.valueOf(location.getLongitude())).isEmpty()
                    ||  pref.getString("lastKnownLng", String.valueOf(location.getLongitude())).equals(null)){
                query.put("latitude", String.valueOf(location.getLatitude()));
                query.put("longitude", String.valueOf(location.getLongitude()));
                query.put("order", "asc");
                query.put("orderby", "distance");
            } else {
                query.put("latitude", pref.getString("lastKnownLat", String.valueOf(location.getLatitude())));
                query.put("longitude", pref.getString("lastKnownLng", String.valueOf(location.getLongitude())));
                query.put("order", "asc");
                query.put("orderby", "distance");
            }

            spinner.setVisibility(View.VISIBLE); //hide progressBar
            if (isLoggedIn) {
                tvLoading.setText(Html.fromHtml(("Thanks for your patience " + "<font color='#4FC1E9'>" + firstName + "</font>" + "we are searing our database to see if there are any registered black owned businesses near you.")));
            } else {
                tvLoading.setText("Thanks for your patience we are searching our database to see if there are any registered black owned businesses near you.");
            }
            ivLoading.setAnimation(imgAnimationIn);
            ivLoading.setVisibility(View.VISIBLE);
            tvLoading.setAnimation(imgAnimationIn);
            tvLoading.setVisibility(View.VISIBLE);

            getRetrofit(query);
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GeolocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageEnableAlerts() {
        String message;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (pref.getBoolean("alertOn", true)) {
            if (isLoggedIn) {
                message = "Hello <font color='#4FC1E9'>" + firstName + "!</font> \nWould you like to disable alerts when you are near a black owned business?";

            } else {
                message = "Would you like to disable alerts when you are near a black owned business?";
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            pref.edit().putBoolean("alertOn", false).apply();
                            ivAlertOff.setVisibility(View.VISIBLE);
                            ivAlertOn.setVisibility(View.GONE);
                            if (isMyServiceRunning()) {
                                stopService(new Intent(MainActivity.this, GeolocationService.class));
                                Toast.makeText(MainActivity.this, "You will not be alerted by Sable when near a black owned business", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            pref.edit().putBoolean("alertOn", true).apply();
                            ivAlertOff.setVisibility(View.GONE);
                            ivAlertOn.setVisibility(View.VISIBLE);
                            if (!isMyServiceRunning()) {
                                startService(new Intent(MainActivity.this, GeolocationService.class));
                            }
                        }
                    });
        } else {
            if (isLoggedIn) {
                message = "Hello<font color='#4FC1E9'>" + firstName + "!</font> \nWould you like to enable alerts when you are near a black owned business?";
            } else {
                message = "Would you like to enable alerts when you are near a black owned business?";
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            pref.edit().putBoolean("alertOn", true).apply();
                            ivAlertOn.setVisibility(View.VISIBLE);
                            ivAlertOff.setVisibility(View.GONE);
                            startService(new Intent(MainActivity.this, GeolocationService.class));
                            if (!isMyServiceRunning()) {
                                startService(new Intent(MainActivity.this, GeolocationService.class));
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            pref.edit().putBoolean("alertOn", false).apply();
                            ivAlertOff.setVisibility(View.VISIBLE);
                            ivAlertOn.setVisibility(View.GONE);
                            if (isMyServiceRunning()) {
                                stopService(new Intent(MainActivity.this, GeolocationService.class));
                                Toast.makeText(MainActivity.this, "You will not be alerted by Sable when near a black owned business", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Double lastKnownLat = null, lastKnownLng = null;
    @Override
    protected void onPause() {
        super.onPause();
        pref.edit().putString("lastKnownLat", String.valueOf(location.getLatitude())).apply();
        pref.edit().putString("lastKnownLng", String.valueOf(location.getLongitude())).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        deleteCache(getApplicationContext());
    }

    private final Thread.UncaughtExceptionHandler handleAppCrash =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(@NotNull Thread thread, Throwable error) {
                    Log.e("error", error.toString());
                    String stackTrace = Log.getStackTraceString(error);
                    String message = error.getMessage();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rchatman@macinternetservices.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Sable Crash log file");
                    intent.putExtra(Intent.EXTRA_TEXT, stackTrace);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
                    startActivity(intent);

                }
            };

    protected void facebookLogin() {
        fbLogincallbackManager = CallbackManager.Factory.create();

        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
                useLoginInformation(accessToken);
            }
        };
        login_button2.setPermissions(Arrays.asList("email", "public_profile"));

        login_button2.registerCallback(fbLogincallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("tag", " facebook login response " + loginResult.getAccessToken());
                        userId = loginResult.getAccessToken().getUserId();
                        useLoginInformation(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook Login Error ", " response " + exception);
                        Toast.makeText(MainActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        isLoggedIn = accessToken != null && !accessToken.isExpired();
    }

    public void useLoginInformation(final AccessToken accessToken) {

        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Picasso.Builder facebookImageBuilder = new Picasso.Builder(getApplicationContext());
                try {


                    /*  userId = object.getString("id");*/
                    Toast.makeText(MainActivity.this, "" + userId, Toast.LENGTH_SHORT).show();

                    userName = object.getString("first_name") + object.getString("last_name");
                    userEmail = object.getString("email");
                    userImage = object.getJSONObject("picture").getJSONObject("data").getString("url");

                    Glide.with(MainActivity.this).load(userImage).into(ivUserImage);
                    String[] parts = (object.getString("name").split(" "));
                    firstName = parts[0];
                    lastName = parts[1];

                    facebookImageBuilder.build().load(object.getJSONObject("picture").getJSONObject("data").getString("url")).into(ivUserImage);
                    facebookImageBuilder.build().load("https://graph.facebook.com/" + userId + "/picture?type=normal").into(ivUserImage);


                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    Map<String, String> query = new HashMap<>();
                    query.put("access_token", accessToken.getToken());
                    loginUser(query);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    // facebook login activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbLogincallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * calculates the distance between two locations in MILES
     */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return dist; // output distance, in MILES
    }

    /**
     * Location listener to get device current lat/lng
     */
    LocationListener LocationListener = new LocationListener() {

        /**
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
           /* latitude = location.getLatitude();
            longitude = location.getLongitude(); */
            if (geofences.size() == 0 || mapLocations.size() == 0) {
                Map<String, String> query = new HashMap<>();
                query.put("latitude", String.valueOf(location.getLatitude()));
                query.put("longitude", String.valueOf(location.getLongitude()));
                query.put("order", "asc");
                query.put("orderby", "distance");

                getRetrofit(query);
            } else{
                double lat2 = location.getLatitude();
                double lng2 = location.getLongitude();
                double lat1 = Double.valueOf(pref.getString("lastKnownLat", String.valueOf(location.getLatitude())));
                double lng1 = Double.valueOf(pref.getString("lastKnownLng", String.valueOf(location.getLongitude())));

                // lat1 and lng1 are the values of a previously stored location
                if (distance(lat1, lng1, lat2, lng2) > 3 || kickItOff) { // if distance < 0.1 miles we take locations as equal
                    kickItOff = false;
                    if (pref.getBoolean("alertOn", true)) {
                        if (!isMyServiceRunning()) {
                            startService(new Intent(MainActivity.this, GeolocationService.class));
                        }
                    }
                    setMarkers();
                }
                if (pref.getBoolean("alertOn", true)) {
                    if (!isMyServiceRunning()) {
                        startService(new Intent(MainActivity.this, GeolocationService.class));
                    }
                }
                if (isLoggedIn) {
                    if (currentMarker != null)
                        currentMarker.remove();
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title("You are here!").snippet("Double tap\nanywhere on\nthe map to zoom")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                } else {
                    if (currentMarker != null)
                        currentMarker.remove();
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title("Welcome <font color='#4FC1E9'>" + firstName + "!</font>").snippet("Double tap\nanywhere on\nthe map to zoom")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }
        }

        /**
         * @param provider
         * @param status
         * @param extras
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        /**
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {
        }

    };

    /**
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationClickListener(this);
       /* mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                TextView title = view.findViewById(R.id.venue_name);
                title.setTextColor(Color.BLACK);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                ImageView greeter = (ImageView) view.findViewById(R.id.imageView1);
                greeter.setImageResource(R.mipmap.hello_foreground);
                greeter.setScaleType(ImageView.ScaleType.FIT_XY);

                TextView snippet = view.findViewById(R.id.textView2);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                return view;
            }
        }); */
        if (geofences.size() > 0 && mapLocations.size() > 0) {
            if (pref.getBoolean("alertOn", true)) {
                ivAlertOn.setVisibility(View.VISIBLE);
                ivAlertOff.setVisibility(View.GONE);
                if (!isMyServiceRunning()) {
                    startService(new Intent(MainActivity.this, GeolocationService.class));
                }
            }
            setMarkers();
        }
    }

    /**
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Getting current location...", Toast.LENGTH_SHORT).show();
        Map<String, String> query = new HashMap<>();

        query.put("latitude", String.valueOf(latitude));
        query.put("longitude", String.valueOf(longitude));
        //query.put("distance", "5");
        query.put("order", "asc");
        query.put("orderby", "distance");
        getRetrofit(query); //api call; pass current lat/lng to check if current location in database
        ////Log.e("onMyLocationButtonClick", "Listings query executed by onMyLocationButtonClick");
        getReviews();
        ////Log.e("onMyLocationButtonClick", "Review query executed by onMyLocationButtonClick");
        //setAddress(latitude, longitude);
        return false;
    }

    /**
     * @param location
     */
    @Override
    public void onMyLocationClick(Location location) {
        Toast.makeText(this, "Getting current location...", Toast.LENGTH_SHORT).show();
        Map<String, String> query = new HashMap<>();

        query.put("latitude", String.valueOf(latitude));
        query.put("longitude", String.valueOf(longitude));
        //query.put("distance", "5");
        query.put("order", "asc");
        query.put("orderby", "distance");
        getRetrofit(query); //api call; pass current lat/lng to check if current location in database
        ////Log.e("onMyLocationClick", "Listings query executed by onMyLocationClick");
        getReviews();
        ////Log.e("onMyLocationClick", "Review query executed by onMyLocationClick");
        //setAddress(latitude, longitude);
    }

    public void setCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        File cacheFoo = context.getCacheDir();
        //  cache = new Cache(new File(context.getCacheDir(), "sable-cache"), cacheSize);
        cache = new Cache(cacheFoo, cacheSize);
        //Log.e("This is Cache:", "cacheFile: " +cache);

    }


    /**
     * Query API for listings data
     * set URL and make call to API
     */

    public class BasicAuthInterceptor implements Interceptor {

        private String credentials;

        public BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }

    }

    //Cache cache = new Cache(new File(context.getCacheDir(), "http-cache"), 10 * 1024 * 1024);


    /**
     * Retrofit API call to get listings
     *
     * @param query
     */
    public void getRetrofit(final Map<String, String> query) {
        //int i;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .addInterceptor(logging)
                .addInterceptor(offlineInterceptor)
                .addInterceptor(onlineInterceptor)
                //.addNetworkInterceptor(provideCacheInterceptor)
                .cache(cache)
                .build();

        retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        verticalList = new ArrayList<>();
        locationMatch = new ArrayList<>();
        listingName = new ArrayList<>();
        recentList = new ArrayList<>();
        featuredList = new ArrayList<>();
        recentReviewList = new ArrayList<>();
        // pass JSON data to BusinessListings class for filtering
        Call<List<BusinessListings>> call = service.getPostInfo(query);

        call.enqueue(new Callback<List<BusinessListings>>() {
            @Override
            public void onResponse(Call<List<BusinessListings>> call, Response<List<BusinessListings>> response) {
                if (response.raw().cacheResponse() != null) {
                    Log.e("Network", "Response came from cache");

                } else {
                    Log.e("Network", "Response came from server");
                }
                if (response.body().isEmpty()) {
                    spinner.setVisibility(View.GONE); //hide progressBar
                    login_button2.setVisibility(View.VISIBLE);
                    loadingLayout.setAnimation(imgAnimationOut);
                    loadingLayout.setVisibility(View.GONE);
                    //searchView.setAnimation(imgAnimationIn);
                    //searchView.setVisibility(View.VISIBLE);
                    noListingsImageView.setAnimation(imgAnimationIn);
                    noListingsImageView.setVisibility(View.VISIBLE);
                    noListingsTextView.setAnimation(imgAnimationIn);
                    noListingsTextView.setVisibility(View.VISIBLE);
                    //noListingsTextView.setTextSize(16);

                    if(isLoggedIn) {
                        noListingsTextView.setText("This is terrible " + firstName +"!!!!\n\nLooks like there aren't any black owned businesses near you in our directory.\n" +
                                "Tap Add (+) from the menu to add any black owned business you visit to our directory.");
                    } else {
                        noListingsTextView.setText("This is terrible!!!!\n\nLooks like there aren't any black owned businesses near you in our directory.\n" +
                                "Tap add (+) to add any black owned business you visit to our directory.");
                    };
                    return;
                }
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        BusinessListings.BusinessHours businessHours = response.body().get(i).getBusinessHours();
                        if (businessHours == null) {
                            String today = "null";
                        } else {
                            todayRange = response.body().get(i).getBusinessHours().getRendered().getExtra().getTodayRange();
                            isOpen = response.body().get(i).getBusinessHours().getRendered().getExtra().getCurrentLabel();
                        }
                        /**
                         * onLocationMatch
                         * if device lat/lng equals stored listing lat/lng locationMatch = true
                         * add all matching data to array and launch Review Activity
                         *
                         */

                        /**
                         * populate vertical recycler in Main Activity
                         */
                        verticalList.add(new ListingsModel(ListingsModel.IMAGE_TYPE,
                                response.body().get(i).getId(),
                                response.body().get(i).getTitle().getRaw(),
                                response.body().get(i).getLink(),
                                response.body().get(i).getStatus(),
                                response.body().get(i).getPostCategory().get(0).getName(),
                                response.body().get(i).getFeatured(),
                                response.body().get(i).getFeaturedImage().getSrc(),
                                response.body().get(i).getBldgNo(),
                                response.body().get(i).getStreet(),
                                response.body().get(i).getCity(),
                                response.body().get(i).getRegion(),
                                response.body().get(i).getCountry(),
                                response.body().get(i).getZip(),
                                response.body().get(i).getLatitude(),
                                response.body().get(i).getLongitude(),
                                response.body().get(i).getRating(),
                                response.body().get(i).getRatingCount(),
                                response.body().get(i).getPhone(),
                                response.body().get(i).getEmail(),
                                response.body().get(i).getWebsite(),
                                response.body().get(i).getTwitter(),
                                response.body().get(i).getFacebook(),
                                response.body().get(i).getVideo(),
                                todayRange,
                                isOpen,
                                response.body().get(i).getLogo(),
                                response.body().get(i).getContent().getRaw(),
                                response.body().get(i).getFeaturedImage().getThumbnail(),
                                response.body().get(i).getTitle().getRaw(), new SimpleGeofence(response.body().get(i).getTitle().getRaw(), response.body().get(i).getLatitude(), response.body().get(i).getLongitude(),
                                8046, GEOFENCE_EXPIRATION_IN_MILLISECONDS, response.body().get(i).getFeaturedImage().getThumbnail(),
                                Geofence.GEOFENCE_TRANSITION_ENTER
                                        | Geofence.GEOFENCE_TRANSITION_DWELL
                                        | Geofence.GEOFENCE_TRANSITION_EXIT)));
                        geofences.put(response.body().get(i).getTitle().getRaw(), new SimpleGeofence(response.body().get(i).getTitle().getRaw(), response.body().get(i).getLatitude(), response.body().get(i).getLongitude(),
                                8046, GEOFENCE_EXPIRATION_IN_MILLISECONDS, response.body().get(i).getFeaturedImage().getThumbnail(),
                                Geofence.GEOFENCE_TRANSITION_ENTER
                                        | Geofence.GEOFENCE_TRANSITION_DWELL
                                        | Geofence.GEOFENCE_TRANSITION_EXIT));
                        verticalAdapter.notifyDataSetChanged();

                        listingName.add(response.body().get(i).getTitle().getRaw());
                           /* try {
                                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss", Locale.US);
                                Date created = sdf.parse(response.body().get(i).getDateGmt());
                                Date currentTime = Calendar.getInstance().getTime();
                                date1 = String.valueOf(created);
                                date2 = String.valueOf(currentTime);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            boolean isRecent = date1 != null && date2 != null && date1.compareTo(date2) < 30;
                            if (isRecent) {
                                recentList.add(new ListingsModel(ListingsModel.IMAGE_TYPE,
                                        response.body().get(i).getId(),
                                        response.body().get(i).getTitle().getRaw(),
                                        response.body().get(i).getLink(),
                                        response.body().get(i).getStatus(),
                                        response.body().get(i).getPostCategory().get(0).getName(),
                                        response.body().get(i).getFeatured(),
                                        response.body().get(i).getFeaturedImage().getSrc(),
                                        response.body().get(i).getBldgNo(),
                                        response.body().get(i).getStreet(),
                                        response.body().get(i).getCity(),
                                        response.body().get(i).getRegion(),
                                        response.body().get(i).getCountry(),
                                        response.body().get(i).getZip(),
                                        response.body().get(i).getLatitude(),
                                        response.body().get(i).getLongitude(),
                                        response.body().get(i).getRating(),
                                        response.body().get(i).getRatingCount(),
                                        response.body().get(i).getPhone(),
                                        response.body().get(i).getEmail(),
                                        response.body().get(i).getWebsite(),
                                        response.body().get(i).getTwitter(),
                                        response.body().get(i).getFacebook(),
                                        response.body().get(i).getVideo(),
                                        todayRange,
                                        isOpen,
                                        response.body().get(i).getLogo(),
                                        response.body().get(i).getContent().getRaw(),
                                        response.body().get(i).getFeaturedImage().getThumbnail(),
                                        response.body().get(i).getTitle().getRaw(), new SimpleGeofence(response.body().get(i).getTitle().getRaw(), response.body().get(i).getLatitude(), response.body().get(i).getLongitude(),
                                        8046, GEOFENCE_EXPIRATION_IN_MILLISECONDS, response.body().get(i).getFeaturedImage().getThumbnail(),
                                        Geofence.GEOFENCE_TRANSITION_ENTER
                                                | Geofence.GEOFENCE_TRANSITION_DWELL
                                                | Geofence.GEOFENCE_TRANSITION_EXIT)));
                                recentListingsAdapter.notifyDataSetChanged();
                            }
                            boolean isFeatured = response.body().get(i).getFeatured();
                            if (isFeatured) {
                                featuredList.add(new ListingsModel(ListingsModel.IMAGE_TYPE,
                                        response.body().get(i).getId(),
                                        response.body().get(i).getTitle().getRaw(),
                                        response.body().get(i).getLink(),
                                        response.body().get(i).getStatus(),
                                        response.body().get(i).getPostCategory().get(0).getName(),
                                        response.body().get(i).getFeatured(),
                                        response.body().get(i).getFeaturedImage().getSrc(),
                                        response.body().get(i).getBldgNo(),
                                        response.body().get(i).getStreet(),
                                        response.body().get(i).getCity(),
                                        response.body().get(i).getRegion(),
                                        response.body().get(i).getCountry(),
                                        response.body().get(i).getZip(),
                                        response.body().get(i).getLatitude(),
                                        response.body().get(i).getLongitude(),
                                        response.body().get(i).getRating(),
                                        response.body().get(i).getRatingCount(),
                                        response.body().get(i).getPhone(),
                                        response.body().get(i).getEmail(),
                                        response.body().get(i).getWebsite(),
                                        response.body().get(i).getTwitter(),
                                        response.body().get(i).getFacebook(),
                                        response.body().get(i).getVideo(),
                                        todayRange,
                                        isOpen,
                                        response.body().get(i).getLogo(),
                                        response.body().get(i).getContent().getRaw(),
                                        response.body().get(i).getFeaturedImage().getThumbnail(),
                                        response.body().get(i).getTitle().getRaw(), new SimpleGeofence(response.body().get(i).getTitle().getRaw(), response.body().get(i).getLatitude(), response.body().get(i).getLongitude(),
                                        8046, GEOFENCE_EXPIRATION_IN_MILLISECONDS, response.body().get(i).getFeaturedImage().getThumbnail(),
                                        Geofence.GEOFENCE_TRANSITION_ENTER
                                                | Geofence.GEOFENCE_TRANSITION_DWELL
                                                | Geofence.GEOFENCE_TRANSITION_EXIT)));
                                featuredListAdapter.notifyDataSetChanged();
                            }

                            /**
                             * categories on top of the map
                             */

                        LatLng latlng = new LatLng(response.body().get(i).getLatitude(), response.body().get(i).getLongitude());
                        latLngBoundsBuilder.include(latlng);
                        mapLocations.add(new Person(latlng,
                                response.body().get(i).getTitle().getRaw(),
                                response.body().get(i).getFeaturedImage().getThumbnail(),
                                response.body().get(i).getContent().getRaw(),
                                response.body().get(i).getRating(),
                                response.body().get(i).getRatingCount(),
                                response.body().get(i).getCity(),
                                response.body().get(i).getRegion(),
                                response.body().get(i).getFeaturedImage().getThumbnail()));
                    }
                    spinner.setVisibility(View.GONE);
                    ivLoading.setAnimation(imgAnimationOut);
                    ivLoading.setVisibility(View.GONE);
                    tvLoading.setAnimation(imgAnimationOut);
                    tvLoading.setVisibility(View.GONE);

                    double lat2 = location.getLatitude();
                    double lng2 = location.getLongitude();
                    double lat1 = Double.valueOf(pref.getString("lastKnownLat", String.valueOf(location.getLatitude())));
                    double lng1 = Double.valueOf(pref.getString("lastKnownLng", String.valueOf(location.getLongitude())));

                    // lat1 and lng1 are the values of a previously stored location
                    if (distance(lat1, lng1, lat2, lng2) > 3 || kickItOff) { // if distance < 0.1 miles we take locations as equal
                        kickItOff = false;
                        if (pref.getBoolean("alertOn", true)) {
                            if (!isMyServiceRunning()) {
                                startService(new Intent(MainActivity.this, GeolocationService.class));
                            }
                        }
                        setMarkers();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BusinessListings>> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                ivLoading.setVisibility(View.VISIBLE);
                ivLoading.setAnimation(imgAnimationIn);
                tvLoading.setVisibility(View.VISIBLE);
                tvLoading.setAnimation(imgAnimationIn);
                if (isLoggedIn) {
                    tvLoading.setText(Html.fromHtml(("Thanks for your patience " + "<font color='#4FC1E9'>" + firstName + "</font>" + "we are searching our database to see if there are any registered black owned businesses near you.")));
                } else {
                    tvLoading.setText("Thanks for your patience we are searching our database to see if there are any registered black owned businesses near you.");
                }
                getRetrofit(query);
            }
        });
    }
    //END Retrofit API call to get listings

    /**
     * Retrofit API call to get reviews
     */

    public void getReviews() {
        retrofit = null;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .addInterceptor(logging)
                .addInterceptor(offlineInterceptor)
                .addInterceptor(onlineInterceptor)
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);

        // pass JSON data to BusinessListings class for filtering
        Call<List<ListReviewPOJO>> call = service.getReviews();


        // get filtered data from BusinessListings class and add to recyclerView adapter for display on screen
        call.enqueue(new Callback<List<ListReviewPOJO>>() {
            @Override
            public void onResponse(Call<List<ListReviewPOJO>> call, Response<List<ListReviewPOJO>> response) {
                if (response.raw().cacheResponse() != null) {
                    Log.e("Network", "Reviews response came from cache");
                } else {
                    Log.e("Network", "Reviews response came from server");
                }
                if (response.isSuccessful() && response.body().size() > 0) {
                    // tvLoading.setAnimation(imgAnimationIn);
                    // tvLoading.setText("Loading recent reviews...");

                    for (int i = 0; i < response.body().size(); i++) {
                        /**
                         * populate vertical recycler in Main Activity
                         */
                        if (response.body().get(i).getImages().getRendered().size() != 0) {

                            recentReviewList.add(new RecentReviewListingsModel(RecentReviewListingsModel.IMAGE_TYPE,
                                    response.body().get(i).getId(),
                                    response.body().get(i).getLink(),
                                    response.body().get(i).getAuthorName(),
                                    response.body().get(i).getRating().getRating(),
                                    response.body().get(i).getDateGmt(),
                                    response.body().get(i).getImages().getRendered().get(0).getSrc(),
                                    response.body().get(i).getContent().getRendered(),
                                    response.body().get(i).getParent()));
                        }

                        recentReviewListingsAdapter.notifyDataSetChanged();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<ListReviewPOJO>> call, Throwable t) {
                getReviews();
            }
        });

    }

    //Retrofit retrofit = null;
    public void loginUser(final Map<String, String> query) {
        Retrofit retrofit;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .addInterceptor(logging)
                .addInterceptor(offlineInterceptor)
                .addInterceptor(onlineInterceptor)
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);

        // pass JSON data to BusinessListings class for filtering
        Call<UserAuthPOJO> call = service.getUserInfo(query);


        // get filtered data from BusinessListings class and add to recyclerView adapter for display on screen
        call.enqueue(new Callback<UserAuthPOJO>() {
            @Override
            public void onResponse(Call<UserAuthPOJO> call, Response<UserAuthPOJO> response) {
                if (response.isSuccessful()) {
                    userId = String.valueOf(response.body().getWpUserId());
                    Toast.makeText(MainActivity.this, "" + userId, Toast.LENGTH_SHORT).show();
                } else {
                    // do some stuff
                }
            }

            @Override
            public void onFailure(Call<UserAuthPOJO> call, Throwable t) {
                //do some stuff
            }
        });
    }

    /**
     * more slider shit
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.5f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    } //END OF SLIDER SHIT

    /**
     * @param context CLEAR CACHE on close
     */

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * ABOUT US ANIMATIONS
     */


    private Runnable runnableCode = new Runnable() {
        int count = 0;


        // String image;
        @Override
        public void run() {
            //Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            //Animation imgAnimationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);


            String[] text = {
                    "Welcome to The Sable Business Directory!\n",


                    "The Sable Business Directory is designed to help those wanting users find black owned" +
                            "businesses and service providers.",

                    "We provide a one of a kind online platform that alerts users when they are near a black owned" +

                    "We are combining geo-search, social media and e-commerce technologies to make it easier users to find, rate " +
                            "and review black owned businesses and service providers.",

                    "We promote high quality products and services by encouraging, customers maintain the directory by adding, rating " +
                            " and reviewing the black owned businesses and service providers they frequent.",

                    "Our combined technologies then compile those listings, ratings and reviews to " +
                            "provide a directory that alerts users of black owned business and service providers " +
                            "near their current location.",

                    "Owners and providers benefit because 88% of people trust online reviews. Online reviews are an important way you can increase " +
                            "sales. This is especially important for local businesses and service providers.",

                    "Adding and reviewing listings is free and easy. To protect the privacy of our users and insure high quality feedback " +
                            "we require users to login via a verified social media account before adding or reviewing a listing.",

                    "Simple tap the button below to begin adding and reviewing black owned businesses using your Facebook account."

            };

            int[] images = {R.mipmap.hello_foreground, R.mipmap.showing_right_foreground,
                    R.mipmap.one_of_akind_foreground, R.mipmap.showing_tablet_foreground, R.mipmap.holding_phone_foreground, R.mipmap.making_thumbs_up_foreground,
                    R.mipmap.online_reviews_foreground, R.mipmap.showing_with_left_hand_foreground, R.mipmap.smiling_peace_foreground};

            if (count == text.length) {
                count = 0;
            }
            switch (count) {

                case 8:
                    imageSwitcher3.setImageResource(images[count]);
                    imageSwitcher3.setAnimation(imgAnimationIn);
                    imageSwitcher3.setVisibility(View.VISIBLE);

                    textSwitcher3.setText(text[count]);
                    textSwitcher3Layout.setAnimation(imgAnimationIn);
                    textSwitcher3Layout.setVisibility(View.VISIBLE);

                    login_button2.setAnimation(imgAnimationIn);
                    login_button2.setVisibility(View.VISIBLE);

                    imageSwitchHandler.postDelayed(this, FRAME_TIME_MS);
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
                    login_button2.setAnimation(imgAnimationOut);
                    login_button2.setVisibility(View.GONE);

                    imageSwitcher3.setImageResource(images[count]);
                    imageSwitcher3.setAnimation(imgAnimationIn);
                    imageSwitcher3.setVisibility(View.VISIBLE);

                    textSwitcher3.setText(text[count]);
                    textSwitcher3Layout.setAnimation(imgAnimationIn);
                    textSwitcher3Layout.setVisibility(View.VISIBLE);

                    imageSwitchHandler.postDelayed(this, FRAME_TIME_MS);
                    count++;
                    break;
            }
        }
    };

    //create map markers and begin geofence monitoring
    protected void setMarkers() {
        Intent intent = new Intent(this, MarkerClusteringActivity.class);
        startActivity(intent);
    }

    protected GoogleMap getMap() {
        return mMap;
    }
}