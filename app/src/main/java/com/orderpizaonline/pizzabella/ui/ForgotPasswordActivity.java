package com.orderpizaonline.pizzabella.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button changePasswordBtn;
    Context context;
    EditText newPassword,confirmNewPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);
        changePasswordBtn =findViewById(R.id.changePasswordBtn);
        newPassword=findViewById(R.id.newPassword);
        confirmNewPassword=findViewById(R.id.confirmNewPassword);
        changePasswordBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v== changePasswordBtn){
            if (newPassword.getText().toString().trim().equals("") || confirmNewPassword.getText().toString().trim().equals("")){
                Toast.makeText(context,"No field can be null",Toast.LENGTH_SHORT).show();

            }else if (newPassword.getText().toString().trim().length()<6 || confirmNewPassword.getText().toString().trim().length()<6){
                Toast.makeText(context,"Password length should be greater or equal to six",Toast.LENGTH_SHORT).show();
            } else if (newPassword.getText().toString().trim().length() != confirmNewPassword.getText().toString().trim().length()){
                Toast.makeText(context,"Passwords does not match",Toast.LENGTH_SHORT).show();

            } else if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
                Toast.makeText(context,"Passwords does not match",Toast.LENGTH_SHORT).show();

            }else {
                String id = getIntent().getStringExtra("userid");
                if (id!=null && !id.equals("")){
                    userSignup.child(id).child("password").setValue(newPassword.getText().toString().trim());
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(context,"User Id null",Toast.LENGTH_SHORT).show();
                }
            }


        }

    }

    private void setNextPhoneFragment() {
        /*try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            VerificationCodeFragment hf = (VerificationCodeFragment) fm.findFragmentByTag("VerificationCodeFragment");
            if (hf == null) {
                hf = new VerificationCodeFragment();
            }
            ft.replace(R.id.main_frame, hf, "VerificationCodeFragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }*/


    }


}