package com.cegep.saporiitaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cegep.saporiitaliano.model.Product;
import com.cegep.saporiitaliano.product.ProductAdapter;
import com.cegep.saporiitaliano.product.ProductDetailActivity;
import com.cegep.saporiitaliano.product.ProductItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchJob extends AppCompatActivity implements ProductItemClickListener<Product> {
private ImageView IV_backjobsearchJob,seachbtn;
private LinearLayout emptySearchOrder;
    EditText searchtext;
    ArrayList<Product> products;
    ArrayList<Product> SearchArrayList;
    String flag="false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);
        IV_backjobsearchJob=findViewById(R.id.IV_backjobsearchJob);
        emptySearchOrder=findViewById(R.id.emptySearchOrder);
        seachbtn=findViewById(R.id.seachbtn);
        searchtext=findViewById(R.id.searchtext);
        final RecyclerView productsList = findViewById(R.id.recycler_view_searchjob);
        productsList.setHasFixedSize(true);
        IV_backjobsearchJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        seachbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchvalue=searchtext.getText().toString();
                int count=0;
                flag="false";
                if(!TextUtils.isEmpty(searchvalue)) {
                    List<Product> SearchArrayList = new ArrayList<>();

                    for(int i=0;i<products.size();i++) {
                        if (products.get(i).name.toLowerCase().contains(searchvalue.trim().toLowerCase())) {
                            //  Log.d("hhh", "onClick: "+enityInternshipViewJobArrayList.get(i).JobTitle);

                            Product temp = new Product();
                            temp.key =products.get(i).key;
                            temp.imageUri = products.get(i).imageUri;
                            temp.name = products.get(i).name;
                            temp.price = products.get(i).price;
                            temp.quantity = products.get(i).quantity;
                            temp.description =products.get(i).description;
                            temp.CategoryId=products.get(i).CategoryId;
                            temp.Weight=products.get(i).Weight;
                            flag="true";

                            SearchArrayList.add(temp);
                            emptySearchOrder.setVisibility(View.GONE);
                            productsList.setAdapter(new ProductAdapter(SearchArrayList, SearchJob.this));

                            count++;
                        }

                    }
                    if(flag.equals("false")){
                        SearchArrayList.clear();
                        productsList.setAdapter(new ProductAdapter(SearchArrayList, SearchJob.this));
                        emptySearchOrder.setVisibility(View.VISIBLE);

                        Toast.makeText(SearchJob.this, "Item not found", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(SearchJob.this, "blank search", Toast.LENGTH_SHORT).show();
                }


            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productsReference = databaseReference.child("Category");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 products = new ArrayList<>();
                for (DataSnapshot mainRoot : dataSnapshot.getChildren()) {
                    for(DataSnapshot child : mainRoot.child("CatData").getChildren()){
                        Product product = child.getValue(Product.class);
                        product.key = child.getKey();
                        products.add(product);
                    }

                }

                productsList.setAdapter(new ProductAdapter(products, SearchJob.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchJob.this, "Failed to fetch products list", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDeleteIconClicked(Product product, int position) {

    }

    @Override
    public void onItemClick(Product product, int position) {
        if (!SaporiItalianoApplication.user.isAdmin) {
            startActivity(ProductDetailActivity.getCallingIntent(this, product, product.CategoryId));
        }
    }
}