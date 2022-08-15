package com.orderpizaonline.pizzabella.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.utils.Common;


public class ThankYouActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    TextView createOtherPizza, viewYourOrders;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userSignup;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you_activity);
        context = this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        userSignup = firebaseDatabase.getReference(Common.USERS);
        createOtherPizza=findViewById(R.id.createOtherPizzaTV);
        viewYourOrders=findViewById(R.id.viewYourOrderTV);
        createOtherPizza.setOnClickListener(this);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        viewYourOrders.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v== createOtherPizza) {

            this.finish();
            Intent intent = new Intent(ThankYouActivity.this, MainActivity.class);
            intent.putExtra("showPizza", true);
            //finishAffinity();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else if (v == viewYourOrders){
            this.finish();
            Intent intent = new Intent(ThankYouActivity.this, MainActivity.class);
            intent.putExtra("showOrders", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            //finishAffinity();
            startActivity(intent);
        }else if (v == iv_back){
            this.finish();
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