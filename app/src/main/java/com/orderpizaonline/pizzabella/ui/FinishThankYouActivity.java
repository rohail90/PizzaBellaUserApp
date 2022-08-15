package com.orderpizaonline.pizzabella.ui;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.orderpizaonline.pizzabella.R;

public class FinishThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_finish_thank_you);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        findViewById(R.id.tv_thankyou_container).startAnimation(loadAnimation);
        findViewById(R.id.tv_thankyou_2).startAnimation(loadAnimation2);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FinishThankYouActivity.this.finish();
            }
        }, 2000);
    }

    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}