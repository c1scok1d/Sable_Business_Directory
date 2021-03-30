package com.macinternetservices.sablebusinessdirectory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.location.Geofence;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import static com.macinternetservices.sablebusinessdirectory.MainActivity.GEOFENCE_EXPIRATION_IN_MILLISECONDS;


public class ListReviewActivity extends AppCompatActivity {

    //ImageButton btnEmail, btnTwitter, btnFacebook, btnReview;
    TextView tvFeatured, tvStatus, tvState, tvNoReviews,
            tvStreet, tvCity, tvZip, tvCountry, tvRating, tvId, tvWebsite, tvImages, tvOurReviews, tvBldNo,
            tvVideo, tvHours, tvIsOpen, tvLink, tvContent, tvPhone, tvBldgno, tvLatitude, tvLongitude, tvRatingCount, tvCategory, tvName, tvFirstRate, tvDistance;
    ImageView ivFeaturedImage;
    RatingBar simpleRatingBar;
    String /*title, content, city, state, zipcode, country, link, */baseURL = "https://www.thesablebusinessdirectory.com", username = "android_app",
            password = "mroK zH6o wOW7 X094 MTKy fwmY", status = "approved";
    //, post_type = "business", todayRange, isOpen;
    //Double latitude, longitude;
    //Integer category, id, rating;

    ProgressBar pDialog;

    ArrayList<ListingsModel> locationReview = new ArrayList<>();

    RecyclerView verticalRecyclerView, horizontalRecyclerView;

    HorizontalImageAdapter horizontalImageAdapter;
    VerticalReviewAdapter verticalReviewAdapter;
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    //String userName, userEmail, userImage;



    ArrayList<String> horizontalList = new ArrayList<>();
    ArrayList<ListReviewModel> verticalList;
    ArrayList<ListingsModel> locationFoo = new ArrayList<>();
    private static final int TOTAL_RETRIES = 3;
    //private static final String TAG = CallbackWithRetry.class.getSimpleName();
    private int retryCount = 0;
    LinearLayout reviewImagesRecyclerLayout, reviewRecyclerLayout, reviewBtnLayout, callBtnLayout, dirBtnLayout, websiteBtnLayout;
    RelativeLayout notLoggedInLayout;
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    ImageView expanded_image;

    //LinearLayoutManager hLayoutManager, vLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_review_activity);

        reviewImagesRecyclerLayout = findViewById(R.id.reviewImagesRecyclerLayout);
        reviewRecyclerLayout = findViewById(R.id.reviewRecyclerLayout);
        notLoggedInLayout = findViewById(R.id.notLoggedInLayout);
        notLoggedInLayout.setVisibility(View.GONE);
        tvImages = findViewById(R.id.tvImages);
        tvImages.setVisibility(View.GONE);
        tvOurReviews = findViewById(R.id.tvOurReviews);
        tvOurReviews.setVisibility(View.GONE);

        verticalList = new ArrayList<>();


       //Picasso.Builder builder = new Picasso.Builder(this);
        pDialog = new ProgressBar(this);

        LinearLayoutManager hLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalImageAdapter = new HorizontalImageAdapter(horizontalList, horizontalList, this);

        horizontalRecyclerView = findViewById(R.id.reviewImagesRecycler);
        horizontalRecyclerView.setHasFixedSize(true);
        horizontalRecyclerView.setLayoutManager(hLayoutManager);
        horizontalRecyclerView.setAdapter(horizontalImageAdapter);

        LinearLayoutManager vLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        verticalReviewAdapter = new VerticalReviewAdapter(verticalList, getApplicationContext());

        verticalRecyclerView =  findViewById(R.id.reviewRecycler);
        verticalRecyclerView.setHasFixedSize(true);
        verticalRecyclerView.setLayoutManager(vLayoutManager);
        verticalRecyclerView.setAdapter(verticalReviewAdapter);


        // Retrieve and cache the system's default "short" animation time.
        //shortAnimationDuration = getResources().getInteger(
          //      android.R.integer.config_shortAnimTime);
        simpleRatingBar = findViewById(R.id.simpleRatingBar);
        //simpleRatingBar.setNumStars(5);
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        //tvRating = findViewById(R.id.ratingBar);
        tvRatingCount = findViewById(R.id.tvRatingCount);
        tvBldNo = findViewById(R.id.tvBldgNo);
        tvStreet = findViewById(R.id.tvStreet);
        tvCity = findViewById(R.id.tvCity);
        tvState = findViewById(R.id.tvState);
        tvZip = findViewById(R.id.tvZip);
        tvCountry = findViewById(R.id.tvCountry);
        tvHours = findViewById(R.id.tvHours);
        tvIsOpen = findViewById(R.id.tvIsOpen);
        tvContent = findViewById(R.id.tvDescription);
        tvPhone = findViewById(R.id.tvPhone);
        //image = findViewById(R.id.ivImage);
        callBtnLayout = findViewById(R.id.callBtnLayout);
        //tvCall = findViewById(R.id.tvCall);
        dirBtnLayout = findViewById(R.id.dirBtnLayout);
        //tvLatitude = findViewById(R.id.tvLat);
        //tvLongitude = findViewById(R.id.tvLng);
        ivFeaturedImage = findViewById(R.id.ivFeaturedImage);
        tvWebsite = findViewById(R.id.tvWebsite);
        /*tvEmail = findViewById(R.id.tvEmail);
        tvTwitter = findViewById(R.id.tvTwitter);
        tvFacebook = findViewById(R.id.tvFacebook);
        btnEmail = findViewById(R.id.btnEmail);*/
       // btnTwitter = findViewById(R.id.btnTwitter);
        //btnFacebook = findViewById(R.id.btnFacebook);
        tvFeatured = findViewById(R.id.tvFeatured);
        tvDistance = findViewById(R.id.tvDistance);
        reviewBtnLayout = findViewById(R.id.reviewBtnLayout);
        tvLongitude = findViewById(R.id.tvLatitude);
        tvLatitude = findViewById(R.id.tvLongitude);
        tvLink = findViewById(R.id.tvLink);
        tvStatus = findViewById(R.id.tvStatus);
        tvCategory = findViewById(R.id.tvCategory);
        tvVideo = findViewById(R.id.tvVideo);
        tvFirstRate = findViewById(R.id.tvFirstRate);
        pDialog = findViewById(R.id.progressBar1);
        tvNoReviews = findViewById(R.id.tvNoReviews);
        expanded_image = findViewById(R.id.expanded_image);


        locationReview = this.getIntent().getExtras().getParcelableArrayList("locationReview");

        Map<String, String> query = new HashMap<>();
        String foo = locationReview.get(0).timestamp;
        Integer fooInt = locationReview.get(0).id;
        query.put("post", String.valueOf(locationReview.get(0).id));
        getPostReview(query);

        tvName.setText(locationReview.get(0).title);
        tvCategory.setText(locationReview.get(0).category);
        Picasso.get().load(locationReview.get(0).featured_image).into(ivFeaturedImage);
//        tvBldgno.setText(locationReview.get(0).bldgno);
        tvStreet.setText(locationReview.get(0).street);
        tvCity.setText(locationReview.get(0).city);
        tvState.setText(locationReview.get(0).state);
        tvCountry.setText(locationReview.get(0).country);
        tvZip.setText(locationReview.get(0).zipcode);
        simpleRatingBar.setRating(locationReview.get(0).rating);
        tvRatingCount.setText(String.valueOf(locationReview.get(0).ratingCount));
        tvPhone.setText(locationReview.get(0).phone);
        //tvEmail.setText(locationReview.get(0).email);
        tvWebsite.setText(locationReview.get(0).website);
        //tvTwitter.setText(locationReview.get(0).twitter);
        //tvFacebook.setText(locationReview.get(0).facebook);
//            tvVideo.setText(locationReview.get(0).video);
        tvHours.setText(locationReview.get(0).hours);
        tvIsOpen.setText(locationReview.get(0).isOpen);
//        tvContent.setText(locationReview.get(0).content);
        tvLink.setText(locationReview.get(0).link);
        tvLatitude.setText(String.valueOf(locationReview.get(0).latitude));
        tvLongitude.setText(String.valueOf(locationReview.get(0).longitude));
        tvId.setText(String.valueOf(locationReview.get(0).id));
 //       tvStatus.setText(locationReview.get(0).status);

        Location locationA = new Location("point A");
        locationA.setLatitude(locationReview.get(0).latitude); //listing lat
        locationA.setLongitude(locationReview.get(0).longitude); //listing lng


        Location locationB = new Location("point B");
        locationB.setLatitude(MainActivity.latitude); //device lat
        locationB.setLongitude(MainActivity.longitude); //device lng

        double distance = (locationA.distanceTo(locationB) * 0.000621371192); //convert meters to miles
        tvDistance.setText(String.format(Locale.US, "%10.2f", distance));

        if (locationReview.get(0).isOpen == "Closed now") {
            tvIsOpen.setTextColor(Color.rgb(255, 0, 0)); //red
        }

       /* if (locationReview.get(0).featured.equals(true)) {
                String featured = "FEATURED";
                tvFeatured.setText(featured);
                tvFeatured.setTextColor(Color.rgb(255, 128, 0)); //orange
            } */

        if (locationReview.get(0).ratingCount == 0) {
                String FirstRate = "Be the first to rate";
                tvFirstRate.setText(FirstRate);
            } else {
            tvFirstRate.setVisibility(View.GONE);
        }

        if (locationReview.get(0).hours == "null" || locationReview.get(0).isOpen == "null" || locationReview.get(0).hours == null || locationReview.get(0).isOpen == null) {
                tvIsOpen.setVisibility(View.GONE);
                tvHours.setVisibility(View.GONE);
            }
        else if (locationReview.get(0).hours == "Closed" || locationReview.get(0).isOpen == "Closed now") {
                String closed = "Closed";
                tvHours.setText(closed);
                tvHours.setTextColor(Color.rgb(255, 0, 0)); //red
                tvIsOpen.setText(closed);
                tvIsOpen.setTextColor(Color.rgb(255, 0, 0)); //red
            }
        else {
                String open = "Open";
                tvIsOpen.setText(open);
                tvIsOpen.setTextColor(Color.rgb(51, 165, 50)); //green
            }

        reviewBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * check validity of facebook access token
                 */
                //accessTokenTracker.startTracking();
                //Boolean isLoggedIn = accessToken != null;

                if (!isLoggedIn) {
                    Intent loginIntent = new Intent(v.getContext(),LoginActivity.class);
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    //Toast.makeText(getApplicationContext(),"User Not Logged In", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * for each array space if id != skip or else...
                     */

                    for (int i = 0; i < locationReview.size(); i++) {

                        if ((locationReview.get(i).id == Integer.parseInt(tvId.getText().toString()))) {

                            locationFoo.add((new ListingsModel(ListingsModel.IMAGE_TYPE,
                                    locationReview.get(i).id,
                                    locationReview.get(i).title,
                                    locationReview.get(i).link,
                                    locationReview.get(i).status,
                                    locationReview.get(i).category,
                                    locationReview.get(i).featured,
                                    locationReview.get(i).featured_image,
                                    locationReview.get(i).bldgno,
                                    locationReview.get(i).street,
                                    locationReview.get(i).city,
                                    locationReview.get(i).state,
                                    locationReview.get(i).country,
                                    locationReview.get(i).zipcode,
                                    locationReview.get(i).latitude,
                                    locationReview.get(i).longitude,
                                    locationReview.get(i).rating,
                                    locationReview.get(i).ratingCount,
                                    locationReview.get(i).phone,
                                    locationReview.get(i).email,
                                    locationReview.get(i).website,
                                    locationReview.get(i).twitter,
                                    locationReview.get(i).facebook,
                                    locationReview.get(i).video,
                                    //locationReview.get(i).hours,
                                    //locationReview.get(i).isOpen,
                                    locationReview.get(i).content,
                                    locationReview.get(i).logo,
                                    locationReview.get(i).featured_image,
                                    locationReview.get(i).content,
                                    new SimpleGeofence(locationReview.get(i).title, locationReview.get(i).latitude, locationReview.get(i).longitude,
                                            100, GEOFENCE_EXPIRATION_IN_MILLISECONDS, locationReview.get(i).featured_image,
                                            Geofence.GEOFENCE_TRANSITION_ENTER
                                                    | Geofence.GEOFENCE_TRANSITION_DWELL
                                                    | Geofence.GEOFENCE_TRANSITION_EXIT))));

                            Bundle locationReviewBundle = new Bundle();
                            locationReviewBundle.putParcelableArrayList("locationReview", locationFoo);

                            Intent LocationReview = new Intent(v.getContext(), ReviewActivity.class);
                            LocationReview.putExtra("locationReview", locationFoo);
                            startActivity(LocationReview);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            break;
                        } else {
                            ////Log.e("VerticalAdapter", "no matcon on locationReview");
                        }
                    }
                }
            }
        });

        if (!tvPhone.getText().toString().isEmpty() || tvPhone.getText().toString().equals("null") ) {
            callBtnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + tvPhone.getText().toString()));
                    startActivity(callIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
            });
        } else {
            callBtnLayout.setVisibility(View.GONE);
        }
        if (!tvLatitude.getText().toString().isEmpty() || tvLongitude.getText().toString().isEmpty()) {
            dirBtnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tvLatitude.getText().toString() + "," + tvLongitude.getText().toString());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }


        if (!tvWebsite.getText().toString().isEmpty() || tvWebsite.getText().toString().equals("null")) {
            tvWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse(tvWebsite.getText().toString()));
                    startActivity(website);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        } else {
            websiteBtnLayout.setVisibility(View.GONE);
        }
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

    private static Retrofit retrofit = null;
    public void getPostReview(final Map<String, String> query) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .addInterceptor(logging)
                .build();


        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);

        // pass JSON data to BusinessListings class for filtering
        Call<List<ListReviewPOJO>> call = service.getPostReview(query);


        // get filtered data from BusinessListings class and add to recyclerView adapter for display on screen
        call.enqueue(new Callback<List<ListReviewPOJO>>() {
            @Override
            public void onResponse(Call<List<ListReviewPOJO>> call, Response<List<ListReviewPOJO>> response) {
                if (response.isSuccessful()) {

                    pDialog.setVisibility(View.GONE); //hide progressBar

                    //if no reviews take user to review activity to be the first to review
                    if (response.body().isEmpty()) {
                        reviewImagesRecyclerLayout.setVisibility(View.GONE);
                        reviewRecyclerLayout.setVisibility(View.GONE);
                        notLoggedInLayout.setVisibility(View.VISIBLE);
                        String listingName = "<font color='#4FC1E9'>" +tvName.getText().toString()+"</font>";
                        tvNoReviews.setText(Html.fromHtml("Identify yourself via Facebook and use the rate button to be the first to rate " + listingName + "."));
                        if (isLoggedIn) {
                            tvNoReviews.setText(Html.fromHtml("Use the rate button to be the first to rate " + listingName + "."));
                        }
                        } else { //list each review

                        for (int i = 0; i < response.body().size(); i++) {
                            /**
                             * populate vertical recycler in Main Activity
                             */
                            verticalList.add(new ListReviewModel(ListReviewModel.IMAGE_TYPE,
                                    response.body().get(i).getId(),
                                    Html.fromHtml(response.body().get(i).getContent().getRendered()).toString(),
                                    response.body().get(i).getLink(),
                                    response.body().get(i).getAuthorName(),
                                    response.body().get(i).getCity(),
                                    response.body().get(i).getRegion(),
                                    response.body().get(i).getCountry(),
                                    response.body().get(i).getLatitude(),
                                    response.body().get(i).getLongitude(),
                                    response.body().get(i).getRating().getLabel(),
                                    response.body().get(i).getRating().getRating(),
                                    response.body().get(i).getDateGmt()));
                            tvOurReviews.setVisibility(View.VISIBLE);

                        }
                        verticalReviewAdapter.notifyDataSetChanged();

                        //if reviews have photos display images
                        for (int i = 0; i < response.body().size(); i++) {
                            if (!response.body().get(i).getImages().getRendered().isEmpty()) {
                                for (int n = 0; n < response.body().get(i).getImages().getRendered().size(); n++) {
                                    horizontalList.add(response.body().get(i).getImages().getRendered().get(n).getSrc());
                                    Glide.with(getApplicationContext()).load(response.body().get(i).getImages().getRendered().get(n).getSrc())
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .error(R.drawable.ic_launcher_background)
                                            .into(expanded_image);
                                }
                                horizontalImageAdapter.notifyDataSetChanged();
                                tvImages.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                }
            }
            @Override
            public void onFailure(Call<List<ListReviewPOJO>> call, Throwable t) {
                if (retryCount++ < TOTAL_RETRIES) {
                }
            }
        });

    }
}