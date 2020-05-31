package com.cegep.saporiitaliano.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    public String key;

    public String imageUri;

    public String name;

    public long price;

    public long quantity;
}
