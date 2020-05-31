package com.cegep.saporiitaliano.main.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Category;

class CategoryViewHolder extends RecyclerView.ViewHolder {

    private ImageView categoryImageView;

    private TextView categoryNameTextView;

    CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryImageView = itemView.findViewById(R.id.category_image);
        categoryNameTextView = itemView.findViewById(R.id.category_name);
    }

    void bind(Category category) {
        int drawableImage = R.drawable.ic_herbs;
        switch (category.name) {
            case "Herbs":
                drawableImage = R.drawable.ic_herbs;
                break;
            case "Toppings":
                drawableImage = R.drawable.ic_toppings;
                break;
            case "Liquids":
                drawableImage = R.drawable.ic_liquids;
                break;
            case "Grains":
                drawableImage = R.drawable.ic_grains;
        }

        Glide.with(categoryImageView)
                .load(drawableImage)
                .into(categoryImageView);

        categoryNameTextView.setText(category.name);
    }
}
