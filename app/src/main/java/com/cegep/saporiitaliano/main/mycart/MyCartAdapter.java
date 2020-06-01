package com.cegep.saporiitaliano.main.mycart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Product;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartViewHolder> {

    private List<Product> products;

    public MyCartAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_cart, parent, false);
        return new MyCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
