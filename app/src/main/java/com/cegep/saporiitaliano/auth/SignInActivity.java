package com.cegep.saporiitaliano.auth;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.cegep.saporiitaliano.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final String client = getString(R.string.client);
        final String clientName = getString(R.string.client_name);
        final String adminName = getString(R.string.admin_name);

        final TextInputLayout usernameInputLayout = findViewById(R.id.username_input);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //noinspection ConstantConditions
                usernameInputLayout.setHint(client.equals(tab.getText().toString()) ? clientName : adminName);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}