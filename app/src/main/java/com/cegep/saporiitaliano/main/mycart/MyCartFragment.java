package com.cegep.saporiitaliano.main.mycart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.saporiitaliano.EditAddress;
import com.cegep.saporiitaliano.PaymentGateway;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.auth.SignInActivity;
import com.cegep.saporiitaliano.main.MainActivity;
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

@SuppressWarnings("ConstantConditions")
public class MyCartFragment extends Fragment {

    private TextView cartTotalTextView;

    private RecyclerView recyclerView;

    private Button placeOrderButton;
    private LinearLayout emptyCart;

    public MyCartFragment() {
        // Required empty public constructor
    }
    float total = 0;
    String flag="false";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartTotalTextView = view.findViewById(R.id.cart_total_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        placeOrderButton = view.findViewById(R.id.place_order_button);
        emptyCart=view.findViewById(R.id.emptyCart);

        for (Product product : SaporiItalianoApplication.products) {
            total += product.quantity * product.price;
            flag="true";
        }
        if(flag.equals("true")&& total>0){
            emptyCart.setVisibility(View.GONE);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


SaporiItalianoApplication.subtotal=total;
        cartTotalTextView.setText("$ " + SaporiItalianoApplication.subtotal);

        recyclerView.setHasFixedSize(true);
        final ArrayList<Product> products = new ArrayList<>(SaporiItalianoApplication.products);
      //  recyclerView.setAdapter(new MyCartAdapter(products));


        MyCartAdapter customAdapter= new MyCartAdapter(products, getActivity(), new MyCartAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(float count) {

                cartTotalTextView.setText("$ " + count);
            }
        });
        recyclerView.setAdapter(customAdapter);


       /* placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaporiItalianoApplication.subtotal=0;

                cartTotalTextView.setText("$ "+SaporiItalianoApplication.subtotal);

                if(SaporiItalianoApplication.products.size()>0) {
                    Intent intent = new Intent(getActivity(), PaymentGateway.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(requireContext(), "Your Cart is Empty", Toast.LENGTH_SHORT).show();

                }

            }
        });*/
       placeOrderButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

            //   SaporiItalianoApplication.subtotal=0;

             //  cartTotalTextView.setText("$ "+SaporiItalianoApplication.subtotal);

               if(SaporiItalianoApplication.products.size()>0) {
                   Intent intent = new Intent(getActivity(), EditAddress.class);
                   startActivity(intent);
               }
               else
               {
                   Toast.makeText(requireContext(), "Your Cart is Empty", Toast.LENGTH_SHORT).show();

               }

           }
       });
    }
}
