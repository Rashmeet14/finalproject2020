package com.cegep.saporiitaliano.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String id;

    public String address;

    public String email;

    public String name;

    public String phone;

    public String password;

    public boolean isAdmin;

}
