package com.cegep.saporiitaliano.main.orders.delivered;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class DeliveredOrdersFragment extends Fragment {

    private RecyclerView recyclerView;

    public DeliveredOrdersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered_orders, container, false);
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
                            recyclerView.setAdapter(new DeliveredOrdersAdapter(orders));
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
                    recyclerView.setAdapter(new DeliveredOrdersAdapter(orders));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private List<Order> getOrders(@NonNull DataSnapshot dataSnapshot) {
        List<Order> orders = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Order order = snapshot.getValue(Order.class);
            if (!"delivered".equals(order.orderStatus)) {
                continue;
            }

            order.key = snapshot.getKey();

            List<OrderItem> orderItems = new ArrayList<>();
            long totalPrice = 0;
            long count = 0;
            DataSnapshot orderItemsSnapshot = snapshot.child("orderItems");
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
