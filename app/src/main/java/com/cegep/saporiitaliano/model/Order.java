package com.cegep.saporiitaliano.model;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.List;

@IgnoreExtraProperties
public class Order {

    public String key;

    public String orderDate;

    public String orderStatus;

    public long totalPrice;

    public long count;

    public List<OrderItem> orderItems;
}
