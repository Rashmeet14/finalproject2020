package com.cegep.saporiitaliano.model;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.List;

@IgnoreExtraProperties
public class Category {

    public List<Product> products;

    public String name;
}