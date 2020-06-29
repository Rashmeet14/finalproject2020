package com.cegep.saporiitaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cegep.saporiitaliano.auth.SignInActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditAddress extends AppCompatActivity {
private TextView addressLineOne,addressLineTwo,City,State,Zip,Country;
private EditText Et_addressone,Et_addresstwo,Et_city,Et_state,Et_zip,Et_country;
private Button updateAddress,changeAddress,ConfirmAddress,updateAddressCancel;
private LinearLayout Buttons,UpdateAddressEdit,ViewAddressText;
public String newAddess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        addressLineOne=findViewById(R.id.addressLineOne);
        addressLineTwo=findViewById(R.id.addressLineTwo);

        Buttons=findViewById(R.id.Buttons);
        UpdateAddressEdit=findViewById(R.id.UpdateAddressEdit);
        ViewAddressText=findViewById(R.id.ViewAddressText);
        updateAddressCancel=findViewById(R.id.updateAddressCancel);

        City=findViewById(R.id.City);
        State=findViewById(R.id.State);
        Zip=findViewById(R.id.Zip);
        Country=findViewById(R.id.Country);


        updateAddress=findViewById(R.id.updateAddress);


        Et_addressone=findViewById(R.id.Et_addressone);
        Et_addresstwo=findViewById(R.id.Et_addresstwo);
        Et_city=findViewById(R.id.Et_city);
        Et_state=findViewById(R.id.Et_state);
        Et_zip=findViewById(R.id.Et_zip);
        Et_country=findViewById(R.id.Et_country);

        changeAddress=findViewById(R.id.changeAddress);

        ConfirmAddress=findViewById(R.id.ConfirmAddress);

        updateAddressCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.setVisibility(View.VISIBLE);
                ViewAddressText.setVisibility(View.VISIBLE);
                UpdateAddressEdit.setVisibility(View.GONE);

            }
        });

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.setVisibility(View.GONE);
                ViewAddressText.setVisibility(View.GONE);
                UpdateAddressEdit.setVisibility(View.VISIBLE);
            }
        });





        ConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditAddress.this, PaymentGateway.class);
                startActivity(intent);
            }
        });




        addressLineOne.setText(SaporiItalianoApplication.user.addressOne);
        addressLineTwo.setText(SaporiItalianoApplication.user.addressTwo);
        City.setText(SaporiItalianoApplication.user.city);
        State.setText(SaporiItalianoApplication.user.state);
        Zip.setText(SaporiItalianoApplication.user.zip);
        Country.setText(SaporiItalianoApplication.user.country);

        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buttons.setVisibility(View.VISIBLE);
                ViewAddressText.setVisibility(View.VISIBLE);
                UpdateAddressEdit.setVisibility(View.GONE);


                HashMap<String, Object> result = new HashMap<>();
               final String  newAddessone=Et_addressone.getText().toString();
                final String  newAddesstwo=Et_addresstwo.getText().toString();
                final String  newCity=Et_city.getText().toString();
                final String  newState=Et_state.getText().toString();
                final String  newZip=Et_zip.getText().toString();
                final String  newCountry=Et_country.getText().toString();

                if(TextUtils.isEmpty(newAddessone)|| TextUtils.isEmpty(newAddesstwo)||TextUtils.isEmpty(newCity)||TextUtils.isEmpty(newState)||TextUtils.isEmpty(newZip)||TextUtils.isEmpty(newCountry)){
                    Toast.makeText(EditAddress.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                result.put("addressOne",newAddessone);
                result.put("addressTwo",newAddesstwo);
                result.put("city",newCity);
                result.put("state",newState);
                result.put("zip",newZip);
                result.put("country",newCountry);



                FirebaseDatabase.getInstance().getReference().child("Users").child(SaporiItalianoApplication.user.id).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        addressLineOne.setText(newAddessone);
                        addressLineTwo.setText(newAddesstwo);
                        City.setText(newCity);
                        State.setText(newState);
                        Zip.setText(newZip);
                        Country.setText(newCountry);

                        SaporiItalianoApplication.user.addressOne=newAddessone;
                        SaporiItalianoApplication.user.addressTwo=newAddesstwo;
                        SaporiItalianoApplication.user.city=newCity;
                        SaporiItalianoApplication.user.state=newState;
                        SaporiItalianoApplication.user.zip=newZip;
                        SaporiItalianoApplication.user.country=newCountry;

                        Toast.makeText(EditAddress.this, "Shipping Address is Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
    }
}