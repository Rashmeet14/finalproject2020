package com.cegep.saporiitaliano.main.orders.delivered;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Order;
import com.cegep.saporiitaliano.model.OrderItem;

class DeliveredOrdersViewHolder extends RecyclerView.ViewHolder {

    private TextView orderIdTextView;

    private TextView orderTotalTextView;

    private TextView orderDateTextView;

    private TextView orderCountTextView;

    private LinearLayout orders;

    private LayoutInflater inflater;

    DeliveredOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        inflater = LayoutInflater.from(itemView.getContext());

        orderIdTextView = itemView.findViewById(R.id.order_id);
        orderTotalTextView = itemView.findViewById(R.id.order_total);
        orderDateTextView = itemView.findViewById(R.id.order_date);
        orderCountTextView = itemView.findViewById(R.id.order_count);
        orders = itemView.findViewById(R.id.orders);
    }

    @SuppressLint("SetTextI18n")
    void bind(Order order) {
        orderIdTextView.setText(order.key);
        orderTotalTextView.setText("$ " + order.totalPrice);
        orderDateTextView.setText(order.orderDate);
        orderCountTextView.setText(order.count + " Items");

        orders.removeAllViews();
        for (OrderItem orderItem : order.orderItems) {
            View view = inflater
                    .inflate(SaporiItalianoApplication.user.isAdmin ? R.layout.item_delivered : R.layout.item_delivered_client, orders, false);

            TextView orderName = view.findViewById(R.id.order_name);
            TextView orderPrice = view.findViewById(R.id.order_price);

            orderName.setText(orderItem.name + " x " + orderItem.quantity);
            orderPrice.setText("$ " + orderItem.sum);

            orders.addView(view);
        }
    }
}
