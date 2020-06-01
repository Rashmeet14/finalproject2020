package com.cegep.saporiitaliano.main.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.common.OnItemClickListener;
import com.cegep.saporiitaliano.model.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private List<Category> categories;

    private OnItemClickListener<Category> onItemClickListener;

    CategoryAdapter(List<Category> categories, OnItemClickListener<Category> onItemClickListener) {
        this.categories = categories;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CategoryViewHolder(
                inflater.inflate(SaporiItalianoApplication.user.isAdmin ? R.layout.item_category : R.layout.item_category_client, parent, false),
                onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
