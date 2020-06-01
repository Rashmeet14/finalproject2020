package com.cegep.saporiitaliano.main.mycart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
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

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartTotalTextView = view.findViewById(R.id.cart_total_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        placeOrderButton = view.findViewById(R.id.place_order_button);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final List<OrderItem> orderItems = new ArrayList<>(SaporiItalianoApplication.products.size());
        float total = 0f;
        for (Product product : SaporiItalianoApplication.products) {
            total += product.quantity * product.price;
            orderItems.add(product.getOrderItem());
        }

        cartTotalTextView.setText("$ " + total);

        recyclerView.setHasFixedSize(true);
        final ArrayList<Product> products = new ArrayList<>(SaporiItalianoApplication.products);
        recyclerView.setAdapter(new MyCartAdapter(products));
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderItems.isEmpty()) {
                    return;
                }

                Order order = new Order();
                order.ClientId = SaporiItalianoApplication.user.id;
                order.ClientName = SaporiItalianoApplication.user.name;
                order.orderStatus = "Pending";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                order.orderDate = sdf.format(new Date());
                order.orderItems = orderItems;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference orderReference = databaseReference.child("Users").child(SaporiItalianoApplication.user.id).child("orders");
                orderReference.child(orderReference.push().getKey()).setValue(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(requireContext(), "Order successfully placed", Toast.LENGTH_SHORT).show();
                                recyclerView.setAdapter(new MyCartAdapter(Collections.<Product>emptyList()));
                                cartTotalTextView.setText("$ 0.00");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
