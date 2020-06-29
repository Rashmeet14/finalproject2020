package com.cegep.saporiitaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cegep.saporiitaliano.auth.SignInActivity;
import com.cegep.saporiitaliano.main.MainActivity;
import com.cegep.saporiitaliano.main.home.HomeFragment;
import com.cegep.saporiitaliano.main.mycart.MyCartAdapter;
import com.cegep.saporiitaliano.model.Order;
import com.cegep.saporiitaliano.model.OrderItem;
import com.cegep.saporiitaliano.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentGateway extends AppCompatActivity implements View.OnClickListener {
private Button makepayment;
private EditText cardnumber,cvv;

private  EditText expirtDate;
    float total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        makepayment=findViewById(R.id.makepayment);
        expirtDate=findViewById(R.id.expirtDate);
        cardnumber=(EditText) findViewById(R.id.cardnumber);
        cvv=findViewById(R.id.cvv);


        makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String card=cardnumber.getText().toString();
                String cardCvv=cvv.getText().toString();
                String expdatee=expirtDate.getText().toString();
                if (TextUtils.isEmpty(card) || TextUtils.isEmpty(cardCvv) ||TextUtils.isEmpty(expdatee)) {
                    Toast.makeText(PaymentGateway.this, "Please Fill all Details", Toast.LENGTH_SHORT).show();
                    return;
                }
if(card.length()<16 || card.length()>16 ){
    Toast.makeText(PaymentGateway.this, "Please 16 Digits Card Number", Toast.LENGTH_SHORT).show();
    return;
}
                if(cardCvv.length()<3 || cardCvv.length()>3){
                    Toast.makeText(PaymentGateway.this, "Please 3 Digits CVV Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(expdatee.length()<5 || expdatee.length()>5){
                    Toast.makeText(PaymentGateway.this, "Please Enter Valid Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                final List<OrderItem> orderItems = new ArrayList<>(SaporiItalianoApplication.products.size());

                for (Product product : SaporiItalianoApplication.products) {
                    total += product.quantity * product.price;
                    orderItems.add(product.getOrderItem());
                }

                if (orderItems.isEmpty()) {

                    return;
                }

                Order order = new Order();
                order.ClientId = SaporiItalianoApplication.user.id;
                order.ClientName = SaporiItalianoApplication.user.name;
                order.orderStatus = "pending";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                order.orderDate = sdf.format(new Date());
                order.orderItems = orderItems;
                Log.d("sa", "onClick: "+order.orderStatus);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference orderReference = databaseReference.child("Users").child(SaporiItalianoApplication.user.id).child("orders");
                orderReference.child(orderReference.push().getKey()).setValue(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PaymentGateway.this, "Order successfully placed", Toast.LENGTH_SHORT).show();
                                SaporiItalianoApplication.subtotal=0;

                                // cartTotalTextView.setText("$ "+SaporiItalianoApplication.subtotal);

                                final ArrayList<Product> productsDelete = new ArrayList<>(SaporiItalianoApplication.products);
                                SaporiItalianoApplication.products.removeAll(productsDelete);
                                Intent intent = new Intent(PaymentGateway.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PaymentGateway.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}