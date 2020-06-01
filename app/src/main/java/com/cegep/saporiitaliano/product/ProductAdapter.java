package com.cegep.saporiitaliano.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Product;
import java.util.List;

class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<Product> products;

    private ProductItemClickListener<Product> productItemClickListener;

    ProductAdapter(List<Product> products, ProductItemClickListener<Product> productItemClickListener) {
        this.products = products;
        this.productItemClickListener = productItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(SaporiItalianoApplication.user.isAdmin ? R.layout.item_product : R.layout.item_product_client, parent, false);
        return new ProductViewHolder(view, productItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
