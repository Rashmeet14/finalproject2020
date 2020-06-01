package com.cegep.saporiitaliano.main.orders.delivered;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Order;
import java.util.List;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersViewHolder> {

    private List<Order> orders;

    DeliveredOrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public DeliveredOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DeliveredOrdersViewHolder(
                inflater.inflate(SaporiItalianoApplication.user.isAdmin ? R.layout.item_delivered_order : R.layout.item_delivered_order_client,
                                 parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveredOrdersViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
