package com.macinternetservices.sablebusinessdirectory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import static com.macinternetservices.sablebusinessdirectory.MainActivity.firstName;

public class LoginActivity extends AppCompatActivity {

    private AccessTokenTracker accessTokenTracker;
    private LoginButton loginButton;
    private TextView tvSecureMsg, tvGreeting, textView6, textView7;
    private ImageView ivGreeter, ivLogo;
    CallbackManager fbLogincallbackManager;
    // Map<Object, Object> query = new HashMap<>();
    ArrayList<UserAuthModel> userinfo = new ArrayList<>();
    String baseURL = "https://www.thesablebusinessdirectory.com", userImage, userName, userEmail;
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    Button btnBack;
    SignInButton googleSignInButton;
    //private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    Animation imgAnimationIn, imgAnimationOut = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvSecureMsg = findViewById(R.id.tvSecureMsg);
        btnBack = findViewById(R.id.btnBack);
        //tvGreeting = findViewById(R.id.tvGreeting);
        //textView6 = findViewById(R.id.textView6);
        //textView7 = findViewById(R.id.textView7);
        ivGreeter = findViewById(R.id.ivGreeter);
        ivLogo = findViewById(R.id.ivLogo);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        googleSignInButton = findViewById(R.id.google_login_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        imgAnimationIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MarkerClusteringActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        // Defining the AccessTokenTracker
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // currentAccessToken is null if the user is logged out
                if (currentAccessToken != null) {
                    useLoginInformation(currentAccessToken);
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    //displayName.setText("Please try again...");
                }
            }
        };

        fbLogincallbackManager = CallbackManager.Factory.create();


        String EMAIL = "email";

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(fbLogincallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                ivGreeter.setAnimation(imgAnimationIn);
                //fooListingsLayout.setVisibility(View.VISIBLE);
                ivGreeter.setImageResource(R.mipmap.under_construction_foreground);
                //tvSecureMsg.setTextSize(16);
                tvSecureMsg.setAnimation(imgAnimationIn);
                tvSecureMsg.setText("Apologies looks like there was an error!\nWe're constantly working to improve our service.\nPlease try again later.");
                Log.w("GoolgeSignInResult", "handleSignInResult:error", exception);
                Log.w("GoogleSignInStatus: ", "signInResult:failed code=" + exception);
            }
        });

        LoginManager.getInstance().registerCallback(fbLogincallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // The ApiException status code indicates the detailed failure reason.
                        // Please refer to the GoogleSignInStatusCodes class reference for more information.
                        ivGreeter.setAnimation(imgAnimationIn);
                        //fooListingsLayout.setVisibility(View.VISIBLE);
                        ivGreeter.setImageResource(R.mipmap.under_construction_foreground);
                        //tvSecureMsg.setTextSize(16);
                        tvSecureMsg.setAnimation(imgAnimationIn);
                        tvSecureMsg.setText("Apologies looks like there was an error!\nWe're constantly working to improve our service.\nPlease try again later.");
                        Log.w("GoolgeSignInResult", "handleSignInResult:error", exception);
                        Log.w("GoogleSignInStatus: ", "signInResult:failed code=" + exception);
                    }
                });
        tvSecureMsg.setVisibility(View.VISIBLE);
        tvSecureMsg.startAnimation(imgAnimationIn);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("743643161501-bev9bim8g129polrljp92f8e6r3eff58.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbLogincallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            userName = (account.getDisplayName());
            userEmail = (account.getEmail());
            userImage = (account.getPhotoUrl().toString());
            MainActivity.googleAccessToken = account.getIdToken();
            //String GoogleAccessToken = account.getIdToken();
            //useGoogleLoginInformation(GoogleAccessToken);

            Map<String, String> query = new HashMap<>();
            query.put("access_token", account.getIdToken());
            getRetrofit(query);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //ivGreeter.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ivGreeter.setAnimation(imgAnimationIn);
            //fooListingsLayout.setVisibility(View.VISIBLE);
            ivGreeter.setImageResource(R.mipmap.under_construction_foreground);
            //tvSecureMsg.setTextSize(16);
            tvSecureMsg.setAnimation(imgAnimationIn);
            tvSecureMsg.setText("Apologies looks like there was an error!\nWe're constantly working to improve our service. Please try again later.");
            //Log.w("GoolgeSignInResult", "handleSignInResult:error", e);
            Log.w("GoogleSignInStatus: ", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    public void onStart() {
        super.onStart();

        // check for and start tracker if user is auth'd via facebook
        // if users is auth'd via facebook login to sable
        accessTokenTracker.startTracking();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useLoginInformation(accessToken);
        }

        // check for and start tracker if user is auth'd via facebook
        // if users is auth'd via facebook login to sable
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            useGoogleLoginInformation(account.getIdToken());
        }
    }

    public void onDestroy() {
        super.onDestroy();
        // We stop the tracking before destroying the activity
        accessTokenTracker.stopTracking();
    }

    private void useGoogleLoginInformation(final String accessToken) {
        Map<String, String> query = new HashMap<>();
        query.put("access_token", accessToken);
        getRetrofit(query);
    }

    private void useLoginInformation(final AccessToken accessToken) {

            //String mAccessToken = accessToken;
            //query.put("access_token", String.valueOf(accessToken));
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());



            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {


                    userName = object.getString("name");
                    userEmail = object.getString("email");
                    userImage = object.getString("picture");
                   // displayName.setText(object.getString("name"));
                   // emailID.setText(object.getString("email"));
                    //builder.build().load(object.getString("picture")).into(displayImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, String> query = new HashMap<>();
                query.put("access_token", accessToken.getToken());
                getRetrofit(query);
            }

        });


            // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
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
    public void getRetrofit(final Map<String, String> query) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);



        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //.addInterceptor(BasicAuthInterceptor(username, password))
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
        Call<UserAuthPOJO> call = service.getUserInfo(query);


        // get filtered data from BusinessListings class and add to recyclerView adapter for display on screen
        call.enqueue(new Callback<UserAuthPOJO>() {
            @Override
            public void onResponse(Call<UserAuthPOJO> call, Response<UserAuthPOJO> response) {
                ////Log.e("main_activity", " response " + response.body());
                if (response.isSuccessful()) {

                    // mListPost = response.body();
                    //progressBar.setVisibility(View.GONE); //hide progressBar
                    // loop through JSON response get parse and output to log

                   // for (int i = 0; i < response.body().size(); i++) {

                        // if (String.format(Locale.US, "%10.6f", response.body().get(i).getLatitude()).equals(String.format(Locale.US, "%10.6f", latitude)) &&
                        //         String.format(Locale.US, "%10.6f", response.body().get(i).getLongitude()).equals(String.format(Locale.US, "%10.6f", longitude))) {

                        userinfo.add(new UserAuthModel(UserAuthModel.IMAGE_TYPE,
                                response.body().getStatus(),
                                response.body().getMsg(),
                                response.body().getWpUserId(),
                                response.body().getCookie(),
                                response.body().getUserLogin(), userName, userImage, userEmail));

                        Intent MainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle locationMatchBundle = new Bundle();
                        locationMatchBundle.putParcelableArrayList("userinfo", userinfo);
                        MainActivity.putExtra("userinfo", userinfo);
                        startActivity(MainActivity);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                   // }
                }
            }

            @Override
            public void onFailure(Call<UserAuthPOJO> call, Throwable t) {

            }
        });
    }
}