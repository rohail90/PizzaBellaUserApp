package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;
import com.orderpizaonline.pizzabella.utils.Utilities;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView continueBtn;
    Context context;
    Button updateBtn;
    EditText phoneNo, name, email;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    ImageView back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.my_profile, container, false);
        context = container.getContext();
        ((MainActivity) context).setToolbarTitle("My Profile");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);
        updateBtn = v.findViewById(R.id.updateProfileBtn);
        updateBtn.setOnClickListener(this);
        back = v.findViewById(R.id.back);
        back.setOnClickListener(this);
        phoneNo = v.findViewById(R.id.phoneNumber);
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email_et);
        name.setText(Session.getUserInfo().getName());
        phoneNo.setText(Session.getUserInfo().getPhoneNumber());
        email.setText(Session.getUserInfo().getEmail());


        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == updateBtn) {
            if (phoneNo.getText().toString().trim().equals("") || name.getText().toString().trim().equals("") || email.getText().toString().trim().equals("")) {
                Toast.makeText(context, "Please provide all info", Toast.LENGTH_SHORT).show();
            } else if (phoneNo.getText().toString().trim().length() < 13 || phoneNo.getText().toString().trim().length() > 13) {
                Toast.makeText(context, "Please enter valid phone No", Toast.LENGTH_SHORT).show();

            } else if (!Utilities.emailValidator(email.getText().toString().trim())) {
                email.setError("Please enter valid email.");
                email.requestFocus();

            } else {
                // update
                UserInfo chat = Session.getUserInfo();
                if (chat != null) {
                    chat.setEmail(email.getText().toString().trim());
                    chat.setPhoneNumber(phoneNo.getText().toString().trim());
                    chat.setName(name.getText().toString().trim());
                    Session.setUserInfo(chat);

                    userSignup.child(chat.getId()).setValue(chat);
                    Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    ((MainActivity) context).onBackPressed();

                } else {
                    Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (v == back){
            ((MainActivity) context).onBackPressed();
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