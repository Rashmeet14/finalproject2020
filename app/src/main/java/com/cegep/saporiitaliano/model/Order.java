package com.cegep.saporiitaliano.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.List;

@IgnoreExtraProperties
public class Order {

    @Exclude
    public String key;

    public String ClientName;

    public String ClientId;

    public String orderDate;

    public String orderStatus;

    @Exclude
    public long totalPrice;

    @Exclude
    public long count;

    public List<OrderItem> orderItems;
}
