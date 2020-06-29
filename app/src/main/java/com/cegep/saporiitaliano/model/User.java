package com.cegep.saporiitaliano.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String id;

    public String addressOne;
    public String addressTwo;
    public String city;
    public String state;
    public String zip;
    public String country;


    public String email;

    public String name;

    public String phone;

    public String password;

    public boolean isAdmin;

}
