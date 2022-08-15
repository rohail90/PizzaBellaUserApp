package com.orderpizaonline.pizzabella.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.databinding.ActivitySignupBinding;
import com.orderpizaonline.pizzabella.model.SignupInfo;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;
import com.orderpizaonline.pizzabella.viewmodels.SignupViewModel;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_login;
    private Button btn_signup;
    private SignupViewModel loginViewModel;
    private ActivitySignupBinding binding;

    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    FirebaseStorage firebaseStorage;
    String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        binding = DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);
        binding.setLifecycleOwner(this);
        binding.setSignupViewModel(loginViewModel);
        phoneNumber = getIntent().getStringExtra("phonenumber");
        context = this;
        new Session(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);
        firebaseStorage = FirebaseStorage.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
//        binding.editTextPhone.setText(phoneNumber.toString());

        loginViewModel.getUser().observe(this, new Observer<SignupInfo>() {
            @Override
            public void onChanged(@Nullable final SignupInfo loginUser) {

              /*  if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPhoneNumber())) {
                    binding.editTextPhone.setError("Enter an Phone Number");
                    binding.editTextPhone.requestFocus();
                }else if (!loginUser.isPhoneValid()){
                    binding.editTextPhone.setError("Phone Number should be 11 digits");
                    binding.editTextPhone.requestFocus();
                }
                else */
                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getName())) {
                    binding.editTextName.setError("Enter your Name");
                    binding.editTextName.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getEmail())) {
                    binding.editTextEmail.setError("Enter your Email Address");
                    binding.editTextEmail.requestFocus();
                } else if (!loginUser.isEmailValid()) {
                    binding.editTextEmail.setError("Enter a Valid E-mail");
                    binding.editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
                    binding.editTextPasswordl.setError("Enter a Password");
                    binding.editTextPasswordl.requestFocus();
                } else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.editTextPasswordl.setError("Enter at least 6 Digit password");
                    binding.editTextPasswordl.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getConfirmPassword())) {
                    binding.editTextConfirmPassword.setError("Enter Confirm Password");
                    binding.editTextConfirmPassword.requestFocus();
                } else if (!loginUser.isConfirmPasswordLengthGreaterThan5()) {
                    binding.editTextConfirmPassword.setError("Enter at least 6 Digit password");
                    binding.editTextConfirmPassword.requestFocus();
                } else if (!loginUser.isPasswordMatches()) {
                    binding.editTextConfirmPassword.setError("Passwords do not Match");
                    binding.editTextConfirmPassword.requestFocus();
                } else {

                    userSignup.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isFound = false;
                            UserInfo chat = null;

//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                chat = snapshot.getValue(UserInfo.class);
//                                if ((chat.getPhoneNumber().equals(phoneNumber) || chat.getEmail().equals(loginUser.getEmail()))) {
//                                    isFound = true;
//                                }
//                            }

                            if (!isFound) {
                                UserInfo vegeInfo = new UserInfo();
                                vegeInfo.setPhoneNumber(phoneNumber);
                                vegeInfo.setName(loginUser.getName());
                                vegeInfo.setEmail(loginUser.getEmail());
                                vegeInfo.setPassword(loginUser.getPassword());
                                vegeInfo.setFcmToken(Session.getFCMToken());
                                String path = userSignup.push().getKey();
                                vegeInfo.setId(path);
                                userSignup.child(path).setValue(vegeInfo);

                                Session.setLoginState(true);
                                Session.setUserInfo(vegeInfo);
                                //Toast.makeText(SignupActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });


        tv_login = findViewById(R.id.tv_login);
        btn_signup = findViewById(R.id.btn_signup);
        tv_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if (view == tv_login) {

            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }/*else if (view == btn_signup){
            Toast.makeText(this, "signup", Toast.LENGTH_SHORT).show();
        }*/


    }
}
