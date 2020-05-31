package com.cegep.saporiitaliano.main.orders.received;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Order;
import java.util.List;

public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersViewHolder> {

    private List<Order> orders;

    private ReceivedOrderClickListener<Order> receivedOrderClickListener;

    ReceivedOrdersAdapter(List<Order> orders, ReceivedOrderClickListener<Order> receivedOrderClickListener) {
        this.orders = orders;
        this.receivedOrderClickListener = receivedOrderClickListener;
    }

    @NonNull
    @Override
    public ReceivedOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ReceivedOrdersViewHolder(inflater.inflate(R.layout.item_received_order, parent, false), receivedOrderClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedOrdersViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
