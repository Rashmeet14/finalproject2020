package com.cegep.saporiitaliano.main.orders.received;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Order;
import com.cegep.saporiitaliano.model.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class ReceivedOrdersFragment extends Fragment implements ReceivedOrderClickListener<Order> {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_received_orders, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);

        if (SaporiItalianoApplication.user.isAdmin) {
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Order> orders = new ArrayList<>();
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                DataSnapshot ordersSnapshot = userSnapshot.child("orders");
                                orders.addAll(getOrders(ordersSnapshot));
                            }
                            recyclerView.setAdapter(new ReceivedOrdersAdapter(orders, ReceivedOrdersFragment.this));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            final DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(SaporiItalianoApplication.user.id)
                    .child("orders");
            ordersReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Order> orders = getOrders(dataSnapshot);
                    recyclerView.setAdapter(new ReceivedOrdersAdapter(orders, ReceivedOrdersFragment.this));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onAcceptButtonClicked(Order order, int position) {
        order.orderStatus = "delivered";
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("/Users/" + order.ClientId + "/orders/" + order.key, order);
        FirebaseDatabase.getInstance().getReference().updateChildren(updateValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Order successfully accepted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to accept order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(Order order, int position) {

    }

    private List<Order> getOrders(DataSnapshot ordersSnapshot) {
        List<Order> orders = new ArrayList<>();
        for (DataSnapshot orderSnapshot : ordersSnapshot.getChildren()) {
            Order order = orderSnapshot.getValue(Order.class);
            if (!"pending".equals(order.orderStatus)) {
                continue;
            }

            order.key = orderSnapshot.getKey();

            List<OrderItem> orderItems = new ArrayList<>();
            long totalPrice = 0;
            long count = 0;
            DataSnapshot orderItemsSnapshot = orderSnapshot.child("orderItems");
            for (DataSnapshot orderItemSnapshot : orderItemsSnapshot.getChildren()) {
                OrderItem orderItem = orderItemSnapshot.getValue(OrderItem.class);
                orderItems.add(orderItem);

                totalPrice += orderItem.sum;
                count++;
            }

            order.totalPrice = totalPrice;
            order.count = count;
            order.orderItems = orderItems;
            orders.add(order);
        }

        return orders;
    }
}
