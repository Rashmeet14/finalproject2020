package com.cegep.saporiitaliano.main.orders.received;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Order;
import com.cegep.saporiitaliano.model.OrderItem;

class ReceivedOrdersViewHolder extends RecyclerView.ViewHolder {

    private TextView orderIdTextView;

    private TextView orderTotalTextView;

    private TextView orderDateTextView;

    private TextView orderCountTextView;

    private LinearLayout orders;

    private LayoutInflater inflater;

    ReceivedOrdersViewHolder(@NonNull View itemView) {
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
            View view = inflater.inflate(R.layout.item_order, orders, false);

            ImageView orderItemImage = view.findViewById(R.id.order_item_image);
            TextView orderName = view.findViewById(R.id.order_name);
            TextView orderPrice = view.findViewById(R.id.order_price);

            Glide.with(orders)
                    .load(orderItem.imageUri)
                    .into(orderItemImage);
            orderName.setText(orderItem.name + " x " + orderItem.quantity);
            orderPrice.setText("$ " + orderItem.sum);

            orders.addView(view);
        }
    }
}
