package com.cegep.saporiitaliano.main.orders.received;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Order;
import com.cegep.saporiitaliano.model.OrderItem;

class ReceivedOrdersViewHolder extends RecyclerView.ViewHolder {

    private TextView orderIdTextView;

    private TextView orderTotalTextView;

    private TextView orderDateTextView;

    private TextView orderCountTextView;
    private  TextView orderstatusvalue;
    private ImageView deleteDeclined;

    private LinearLayout orders;

    private LayoutInflater inflater;

    private Order order;

    ReceivedOrdersViewHolder(@NonNull View itemView, final ReceivedOrderClickListener<Order> receivedOrderClickListener) {
        super(itemView);
        inflater = LayoutInflater.from(itemView.getContext());

        orderIdTextView = itemView.findViewById(R.id.order_id);
        orderTotalTextView = itemView.findViewById(R.id.order_total);
        orderDateTextView = itemView.findViewById(R.id.order_date);
        orderCountTextView = itemView.findViewById(R.id.order_count);
        orderstatusvalue=itemView.findViewById(R.id.orderstatusvalue);
        deleteDeclined=itemView.findViewById(R.id.deleteDeclined);
        orders = itemView.findViewById(R.id.orders);




        if (SaporiItalianoApplication.user.isAdmin) {
            itemView.findViewById(R.id.accept_order_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receivedOrderClickListener.onAcceptButtonClicked(order, getAdapterPosition());
                }
            });

            itemView.findViewById(R.id.decline_order_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receivedOrderClickListener.onDeclineButtonClicked(order, getAdapterPosition());
                }
            });
        } else {
            itemView.findViewById(R.id.cancel_order_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receivedOrderClickListener.onCancelButtonClicked(order, getAdapterPosition());
                }
            });
            deleteDeclined.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receivedOrderClickListener.onCancelButtonClicked(order, getAdapterPosition());
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    void bind(Order order) {
        this.order = order;
        orderIdTextView.setText("Order #" + order.key);
        orderTotalTextView.setText("$ " + order.totalPrice);
        orderDateTextView.setText(order.orderDate);
        orderCountTextView.setText(order.count + " Items");
        if(!SaporiItalianoApplication.user.isAdmin) {
            orderstatusvalue.setText(order.orderStatus + " Delivery");
            if(order.orderStatus.equals("declined")){
                deleteDeclined.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.cancel_order_button).setVisibility(View.GONE);
            }
        }


        orders.removeAllViews();
        for (OrderItem orderItem : order.orderItems) {
            View view = inflater.inflate(SaporiItalianoApplication.user.isAdmin ? R.layout.item_order : R.layout.item_order_client, orders, false);

            ImageView orderItemImage = view.findViewById(R.id.order_item_image);
            TextView orderName = view.findViewById(R.id.order_name);
            TextView orderPrice = view.findViewById(R.id.order_price);

            if (SaporiItalianoApplication.user.isAdmin) {
                Glide.with(orders)
                        .load(orderItem.imageUri)
                        .into(orderItemImage);
            }

            orderName.setText(orderItem.name + " x " + orderItem.quantity);
            orderPrice.setText("$ " + orderItem.sum);

            orders.addView(view);
        }
    }
}
