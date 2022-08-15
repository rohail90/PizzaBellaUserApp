package com.orderpizaonline.pizzabella.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.PhoneAuthentication.PhoneActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.databinding.ActivityLoginBinding;
import com.orderpizaonline.pizzabella.model.LoginInfo;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.model.UserModel;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;
import com.orderpizaonline.pizzabella.utils.Utilities;
import com.orderpizaonline.pizzabella.viewmodels.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_forgot_password, tv_signup, tv_skip;
    private Button btn_login;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private ConstraintLayout facebook_layout, google_layout;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    FirebaseStorage firebaseStorage;
    private int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient=null;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
// FacebookSdk.sdkInitialize(context);
// AppEventsLogger.activateApp(getBaseContext());
        callbackManager = CallbackManager.Factory.create();

// FacebookSdk.setAutoLogAppEventsEnabled(true);
// FacebookSdk.setAdvertiserIDCollectionEnabled(true);
// OPTIONALLY the following two lines if AutoInitEnabled is set to false in manifest:
// FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.sdkInitialize(this);
        FacebookSdk.fullyInitialize();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
// App code
                        Log.d("USER", "onSuccess AAAA fb login: "+loginResult.getAccessToken().getUserId()+" ");

                        callGraphAPI(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
// App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
// App code
                    }
                });
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){

        }
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }

        context = this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);
        firebaseStorage = FirebaseStorage.getInstance();

        loginViewModel.getUser().observe(this, new Observer<LoginInfo>() {
            @Override
            public void onChanged(@Nullable final LoginInfo loginUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPhoneNumber())) {
                    binding.editTextPhone.setError("Enter an Phone Number");
                    binding.editTextPhone.requestFocus();
                }else if (!loginUser.isPhoneValid()){
                    binding.editTextPhone.setError("Phone Number should be 10 digits");
                    binding.editTextPhone.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
                    binding.editTextPassword.setError("Enter a Password");
                    binding.editTextPassword.requestFocus();
                }
                else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.editTextPassword.setError("Enter at least 6 Digit password");
                    binding.editTextPassword.requestFocus();
                }
                else {

                    userSignup.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isFound = false;
                            UserInfo chat = null;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                chat = snapshot.getValue(UserInfo.class);
                                if ((chat.getPhoneNumber().equals("+92"+loginUser.getPhoneNumber()) && chat.getPassword().equals(loginUser.getPassword()))) {
                                    isFound = true;

                                    Session.setLoginState(true);
                                    Session.setUserInfo(chat);
                                    userSignup.child(chat.getId()).child("fcmToken").setValue(Session.getFCMToken());

                                }
                            }

                            if (isFound){
                                //Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        tv_signup = findViewById(R.id.tv_signup);
        btn_login = findViewById(R.id.btn_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);

        facebook_layout = findViewById(R.id.facebook_layout);
        google_layout = findViewById(R.id.google_layout);
        tv_skip = findViewById(R.id.tv_skip);
        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        google_layout.setOnClickListener(this);
        facebook_layout.setOnClickListener(this);

    }
    private void resetToDefaultView(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        /*getView().setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));
        progressBar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);*/
    }
    private void callGraphAPI(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        OnFbSuccess(response);
                    }
                });
        Bundle parameters = new Bundle();
//Explicitly we need to specify the fields to get values else some values will be null.
        parameters.putString("fields", "id,birthday,email,first_name,gender,last_name,link,location,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void OnFbSuccess(GraphResponse graphResponse) {
        UserModel userModel = getUserModelFromGraphResponse(graphResponse);
        if(userModel!=null) {
            handleFBSignInResult(userModel);
            Log.d("USER", "OnFbSuccess: "+userModel.userEmail+" "+userModel.userName);
        }
    }
    private UserModel getUserModelFromGraphResponse(GraphResponse graphResponse)
    {
        UserModel userModel = new UserModel();
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            userModel.userName = jsonObject.getString("name");
            userModel.userEmail = jsonObject.getString("email");
            String id = jsonObject.getString("id");
            String profileImg = "http://graph.facebook.com/"+ id+ "/picture?type=large";
            userModel.profilePic = profileImg;
            Log.i("USER",profileImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }

    public void OnFbError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }


    @Override
    public void onClick(View view) {
        /*if (view == btn_login){
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        }else */if (view == tv_signup){
            Intent intent = new Intent(LoginActivity.this, PhoneActivity.class);
            startActivity(intent);
            //finish();

        }else if (view == tv_forgot_password){
            Intent intent = new Intent(LoginActivity.this, PhoneActivity.class);
            intent.putExtra("isForgot", true);
            startActivity(intent);
            //finish();
        }else if (view == google_layout){
            signIn();
            /*Intent intent = new Intent(LoginActivity.this, PhoneActivity.class);
            intent.putExtra("isForgot", true);
            startActivity(intent);
            finish();*/
        }else if (view == facebook_layout){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
            /*Intent intent = new Intent(LoginActivity.this, PhoneActivity.class);
            intent.putExtra("isForgot", true);
            startActivity(intent);
            finish();*/
        }else if (view == tv_skip){
            // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
// The Task returned from this call is always completed, no need to attach
// a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleFBSignInResult(UserModel userModel) {
        try {

// Signed in successfully, show authenticated UI.
            Log.d("USER", "handleSignInResult: "+userModel.userEmail+" "+userModel.userName);
            UserInfo userInfo=new UserInfo();
            userInfo.setEmail(userModel.userEmail);
            userInfo.setName(userModel.userName);
            Session.setUserInfo(userInfo);
            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
            userSignup.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isFound = false;
                    UserInfo chat = null;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        chat = snapshot.getValue(UserInfo.class);
                        if ((userModel.userName.equals(chat.getName()))) {
                            isFound = true;
                            break;
                        }
                    }

                    if (!isFound){
                        Log.d("USER", "!isFound onDataChange: "+userModel.userEmail);

                        UserInfo vegeInfo=new UserInfo();
                        vegeInfo.setPhoneNumber("");
                        vegeInfo.setEmail(userModel.userEmail);
                        vegeInfo.setName(userModel.userName);
                        vegeInfo.setPassword("");
                        vegeInfo.setFcmToken(Session.getFCMToken());
                        String path = userSignup.push().getKey();
                        vegeInfo.setId(path);
                        userSignup.child(path).setValue(vegeInfo);

                        Session.setLoginState(true);
                        Session.setUserInfo(vegeInfo);
                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Log.d("USER", "isFound onDataChange: "+userModel.userEmail);
                        Session.setLoginState(true);
                        Session.setUserInfo(chat);
                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


// updateUI(account);
        } catch (Exception e) {
// The ApiException status code indicates the detailed failure reason.
// Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("USER", "handleSignInResult signInResult:failed code=" + e.getMessage());
// updateUI(null);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

// Signed in successfully, show authenticated UI.
            Log.d("USER", "handleSignInResult: "+account.getEmail()+" "+account.getDisplayName()+" token id: "+account.getIdToken());
            UserInfo userInfo=new UserInfo();
            userInfo.setEmail(account.getEmail());
            userInfo.setName(account.getDisplayName());
            Session.setUserInfo(userInfo);
            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
            userSignup.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isFound = false;
                    UserInfo chat = null;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        chat = snapshot.getValue(UserInfo.class);
                        if ((account.getEmail().equals(chat.getEmail()))) {
                            isFound = true;
                            Log.d("USER", "onDataChange: "+account.getEmail());
                            break;
                        }
                    }

                    if (!isFound){
                        Log.d("USER", "!isFound onDataChange: "+account.getEmail());

                        UserInfo vegeInfo=new UserInfo();
                        vegeInfo.setPhoneNumber("");
                        vegeInfo.setName(account.getDisplayName());
                        vegeInfo.setEmail(account.getEmail());
                        vegeInfo.setPassword("");
                        vegeInfo.setFcmToken(Session.getFCMToken());
                        String path = userSignup.push().getKey();
                        vegeInfo.setId(path);
                        userSignup.child(path).setValue(vegeInfo);

                        Session.setLoginState(true);
                        Session.setUserInfo(vegeInfo);
                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Log.d("USER", "isFound onDataChange: "+account.getEmail());
                        Session.setLoginState(true);
                        Session.setUserInfo(chat);
                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


// updateUI(account);
        } catch (ApiException e) {
// The ApiException status code indicates the detailed failure reason.
// Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("USER", "handleSignInResult signInResult:failed code=" + e.getStatusCode());
// updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Session(this);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null) {
            UserInfo userInfo=new UserInfo();
            userInfo.setEmail(account.getEmail());
            userInfo.setName(account.getDisplayName());
            Session.setUserInfo(userInfo);

            Log.d("USER", "onStart: "+account.getEmail()+" "+account.getDisplayName());
            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
// Intent intent = Intent(this@SignInActivity, UserProfile::class.java)
// startActivity(intent)
        }
    }

}
