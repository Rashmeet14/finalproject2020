package com.cegep.saporiitaliano;

import android.app.Application;
import com.cegep.saporiitaliano.model.Product;
import com.cegep.saporiitaliano.model.SignUpUser;
import com.cegep.saporiitaliano.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaporiItalianoApplication extends Application {

    public static User user;
    public static SignUpUser signUpUser;
          public static float subtotal;
    public static ArrayList<Product> products = new ArrayList<>();
}
