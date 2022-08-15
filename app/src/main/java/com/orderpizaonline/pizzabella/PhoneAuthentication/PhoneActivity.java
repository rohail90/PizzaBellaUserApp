package com.orderpizaonline.pizzabella.PhoneAuthentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.utils.Common;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private EditText editText;
    private Button buttonContinue;
    private Boolean isFoogotOk = false;
    private ImageView back ;

    Context context = null;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);


        context = this;
        isFoogotOk = getIntent().getBooleanExtra("isForgot", false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        editText = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;
                userSignup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isFound = false;
                        String id = "";
                        UserInfo chat = null;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            chat = snapshot.getValue(UserInfo.class);
                            if (chat.getPhoneNumber().equals(phoneNumber)) {
                                isFound = true;
                                id = chat.getId();
                             /*   Session.setLoginState(true);
                                Session.setUserInfo(chat);
                                userSignup.child(chat.getId()).child("fcmToken").setValue(Session.getFCMToken());*/

                            }
                        }

                        if ((isFound && isFoogotOk) || (!isFound && !isFoogotOk)){
                           /* if (Session.getUserInfo() != null) {
                                if (Session.getUserInfo().getPhoneNumber().equals(phoneNumber)) {

                                    Intent intent = new Intent(PhoneActivity.this, VerifyPhoneActivity.class);
                                    intent.putExtra("phonenumber", phoneNumber);
                                    intent.putExtra("isForgot", getIntent().getBooleanExtra("isForgot", false));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(PhoneActivity.this, "Phone Number not Registered", Toast.LENGTH_SHORT).show();
                                }
                            } else {*/
                                buttonContinue.setClickable(false);
                                Intent intent = new Intent(PhoneActivity.this, VerifyPhoneActivity.class);
                                intent.putExtra("phonenumber", phoneNumber);
                                intent.putExtra("userid", id);
                                intent.putExtra("isForgot", getIntent().getBooleanExtra("isForgot", false));
                                startActivity(intent);
                            /*}*/

                        }else if (isFound && !isFoogotOk){
                            Toast.makeText(PhoneActivity.this, "Already  Registered", Toast.LENGTH_SHORT).show();

                        }else if (!isFound && isFoogotOk){
                            Toast.makeText(PhoneActivity.this, "Phone Number not Registered", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
    }

    @Override
    protected void onResume() {
        buttonContinue.setClickable(true);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view == back){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, SignupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }*/
}
