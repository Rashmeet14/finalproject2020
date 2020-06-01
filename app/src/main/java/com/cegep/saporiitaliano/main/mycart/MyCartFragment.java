package com.cegep.saporiitaliano.main.mycart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Product;
import java.util.ArrayList;

public class MyCartFragment extends Fragment {

    private TextView cartTotalTextView;

    private RecyclerView recyclerView;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartTotalTextView = view.findViewById(R.id.cart_total_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        float total = 0f;
        for (Product product : SaporiItalianoApplication.products) {
            total += product.quantity * product.price;
        }

        cartTotalTextView.setText("$ " + total);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MyCartAdapter(new ArrayList<Product>(SaporiItalianoApplication.products)));
    }
}
