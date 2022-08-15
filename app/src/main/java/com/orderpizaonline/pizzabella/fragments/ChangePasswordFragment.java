package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;


public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    Button changePasswordBtn;
    Context context;
    EditText currentPassword,newPassword,confirmNewPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.change_password, container, false);
        context = container.getContext();
        ((MainActivity)context).setToolbarTitle("Change Password");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);
        changePasswordBtn =v.findViewById(R.id.changePasswordBtn);
        currentPassword=v.findViewById(R.id.currentPassword);
        newPassword=v.findViewById(R.id.newPassword);
        confirmNewPassword=v.findViewById(R.id.confirmNewPassword);
        changePasswordBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v== changePasswordBtn){
            if (!currentPassword.getText().toString().trim().equals(Session.getUserInfo().getPassword())){
                Toast.makeText(context,"No field can be null",Toast.LENGTH_SHORT).show();

            }else if (newPassword.getText().toString().trim().equals("") || confirmNewPassword.getText().toString().trim().equals("")){
                Toast.makeText(context,"No field can be null",Toast.LENGTH_SHORT).show();

            }else if (confirmNewPassword.getText().toString().trim().length()<6){
                Toast.makeText(context,"password length must b greater or equal to six",Toast.LENGTH_SHORT).show();

            }else {
                if (Session.getUserInfo().getId()!=null){
                    userSignup.child(Session.getUserInfo().getId()).child("password").setValue(newPassword.getText().toString().trim());
                    Toast.makeText(context,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                    ((MainActivity)context).onBackPressed();
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