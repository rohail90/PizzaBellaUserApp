package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.utils.Common;

import static com.orderpizaonline.pizzabella.utils.Utilities.emailValidator;


public class ContactUsFragment extends Fragment implements View.OnClickListener {
    Button sendEmailBtn;
    Context context;
    EditText name,email, subject,message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.contact_us, container, false);
        context = container.getContext();
        ((MainActivity)context).setToolbarTitle("Contact Us");

        sendEmailBtn=v.findViewById(R.id.sendEmail);
        sendEmailBtn.setOnClickListener(this);
        name=v.findViewById(R.id.name);
        email=v.findViewById(R.id.email);
        subject =v.findViewById(R.id.subject);
        message =v.findViewById(R.id.message);


        return v;
    }

    @Override
    public void onClick(View v) {
        if (v==sendEmailBtn){
            if (name.getText().toString().trim().equals("") || email.getText().toString().trim().equals("") || subject.getText().toString().trim().equals("") || message.getText().toString().trim().equals("")){
                Toast.makeText(context,"Please enter all info",Toast.LENGTH_SHORT).show();
            }else if (!emailValidator(email.getText().toString().trim())) {
                Toast.makeText(context, "Please enter valid email Id", Toast.LENGTH_SHORT).show();
            }else {
                sendEmail();
                // update
            }
        }


    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {Common.AdminEmail};

        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            getActivity().finish();
            Log.i("MAIL", "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
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