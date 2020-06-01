package com.cegep.saporiitaliano.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.common.OnItemClickListener;
import com.cegep.saporiitaliano.model.Category;
import com.cegep.saporiitaliano.product.ProductListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener<Category> {

    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        if (SaporiItalianoApplication.user.isAdmin) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> categories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if (category != null) {
                        category.key = snapshot.getKey();
                        categories.add(category);
                    }
                }

                recyclerView.setAdapter(new CategoryAdapter(categories, HomeFragment.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Category category, int position) {
        startActivity(ProductListActivity.getCallingIntent(requireContext(), category.key));
    }
}
