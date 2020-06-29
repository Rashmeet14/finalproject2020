package com.cegep.saporiitaliano.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.main.home.HomeFragment;
import com.cegep.saporiitaliano.main.mycart.MyCartFragment;
import com.cegep.saporiitaliano.main.orders.delivered.DeliveredOrdersFragment;
import com.cegep.saporiitaliano.main.orders.received.ReceivedOrdersFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SaporiItalianoApplication.user.isAdmin) {
            setTheme(R.style.AppTheme_Dark);
        }

            FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);
        if(!SaporiItalianoApplication.user.isAdmin) {
            updateToken();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, new HomeFragment())
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.my_cart).setVisible(!SaporiItalianoApplication.user.isAdmin);
        bottomNavigationView.getMenu().findItem(R.id.received_orders)
                .setTitle(SaporiItalianoApplication.user.isAdmin ? R.string.received_orders : R.string.pending_delivery);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.settings:
                        fragment = new SettingsFragment();
                        break;

                    case R.id.received_orders:
                        fragment = new ReceivedOrdersFragment();
                        break;

                    case R.id.delivered_orders:
                        fragment = new DeliveredOrdersFragment();
                        break;



                    case R.id.my_cart:
                        fragment = new MyCartFragment();
                        break;
                }

                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_view, fragment)
                            .commit();
                }
                return true;
            }
        });
    }

    private void updateToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) { String newToken = instanceIdResult.getToken();
                Log.d("tokennnnnn",newToken);

                updateTokentoFirebase(newToken);
            }
        });
    }

    private void updateTokentoFirebase(String newToken) {

        HashMap<String, Object> result = new HashMap<>();
        result.put("PushToken",newToken);



        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference users = databaseReference.child("Users").child(SaveLoginUser.user.id).child("password");

        FirebaseDatabase.getInstance().getReference().child("Users").child(SaporiItalianoApplication.user.id).updateChildren(result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
