package com.cegep.saporiitaliano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cegep.saporiitaliano.auth.SignInActivity;
import com.cegep.saporiitaliano.auth.SignUpActivity;

public class TermsAndCondition extends AppCompatActivity {

    private ImageView IV_backjobsearchJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        final String sessionId = getIntent().getStringExtra("ScreenName");
        IV_backjobsearchJob=findViewById(R.id.IV_backjobsearchJob);
        IV_backjobsearchJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sessionId.equals("EDITPROFILE")){
                    onBackPressed();
                }
                else if(sessionId.equals("SIGNUP")){
                    Intent intent = new Intent(TermsAndCondition.this, SignUpActivity.class);
                    intent.putExtra("dataLoded", "TERMS");
                    startActivity(intent);
                }

            }
        });
    }
}