package com.cegep.saporiitaliano.main.mycart;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.auth.SignInActivity;
import com.cegep.saporiitaliano.main.MainActivity;
import com.cegep.saporiitaliano.model.Product;
import com.cegep.saporiitaliano.model.User;
import com.cegep.saporiitaliano.product.ProductDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {
    List<Product> products;
    Context context;
    OnItemDeleteListener intefacCall;
    interface OnItemDeleteListener{
      void  onItemDelete(float count);
    }

    public MyCartAdapter(List<Product> products, Context context,OnItemDeleteListener intefacCall) {
        this.products = products;
        this.context = context;
        this.intefacCall = intefacCall;
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder
    {



        private ImageView productImageView;

        private TextView productNameTextView;

        private TextView productPriceTextView;

        private TextView productTotalPriceTextView;
        private ImageView delete_cart_item;
        private TextView cart_total_text;
        private ImageButton add_item_button_Cart,remove_item_buttonCart;
        private  TextView androidQuantityItemCart;

        MyViewHolder (View itemview)
        {
            super(itemview);

            this.productNameTextView = itemView.findViewById(R.id.product_name);
            this.productImageView = itemView.findViewById(R.id.product_image);
            this.productPriceTextView = itemView.findViewById(R.id.product_price);
            this.productTotalPriceTextView = itemView.findViewById(R.id.product_total_price);
            this.delete_cart_item=itemview.findViewById(R.id.delete_cart_item);
            this.add_item_button_Cart=itemview.findViewById(R.id.add_item_button_Cart);
            this.remove_item_buttonCart=itemview.findViewById(R.id.remove_item_buttonCart);
            this.androidQuantityItemCart=itemview.findViewById(R.id.androidQuantityItemCart);
            //this.cart_total_text=itemview.findViewById(R.id.cart_total_text);
//           this.jobbutton=(Button) itemview.findViewById(R.id.jobbutton);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li= LayoutInflater.from(parent.getContext());
        View view=li.inflate(R.layout.item_my_cart,parent,false);
        MyViewHolder myViewHolder= new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        TextView productNameTextView=holder.productNameTextView;
        TextView productPriceTextView=holder.productPriceTextView;
        TextView productTotalPriceTextView=holder.productTotalPriceTextView;
        ImageView  productImageView=holder.productImageView;
        ImageView delete_cart_item=holder.delete_cart_item;
        ImageButton add_item_button_Cart=holder.add_item_button_Cart;
        ImageButton remove_item_buttonCart=holder.remove_item_buttonCart;
        final TextView androidQuantityItemCart=holder.androidQuantityItemCart;


        Glide.with(context)
                .load(products.get(position).imageUri)
                .into(productImageView);

        productNameTextView.setText(products.get(position).name+"");
        productPriceTextView.setText("$ " + products.get(position).price+"");
        productTotalPriceTextView.setText("$ " + products.get(position).quantity * products.get(position).price);
        androidQuantityItemCart.setText(""+products.get(position).quantity);

        holder.delete_cart_item.setOnClickListener(new View.OnClickListener() {

            int stockQuantity=0;
            @Override
            public void onClick(View v) {

                Log.d("catid", "onClick: "+products.get(position).CategoryId);
                Log.d("itemid", "onClick: "+products.get(position).key);


               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("Category").child(products.get(position).CategoryId).child("CatData").child(products.get(position).key);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Product product = dataSnapshot.getValue(Product.class);
                           // Log.d("dscds", "onDataChange: "+product.quantity);
                          stockQuantity += (int) product.quantity;
                          addValueToFirebase(stockQuantity);





                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //Toast.makeText(SignInActivity.this, "Failed to load login details", Toast.LENGTH_SHORT).show();
                    }
                    public  void addValueToFirebase(int stockQuantity){

                        HashMap<String, Object> result = new HashMap<>();
                        int newquantity= (int) (stockQuantity+products.get(position).quantity);
                        result.put("quantity",newquantity);

                        FirebaseDatabase.getInstance().getReference().child("Category").child(products.get(position).CategoryId).child("CatData").child(products.get(position).key).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                float itemTotalPrice=products.get(position).quantity * products.get(position).price;
                                SaporiItalianoApplication.products.remove(products.get(position));
                                products.remove(position);
                                notifyItemRemoved(position);// JUST TO REFRESH THE RECYCLER VIEW
                                notifyItemRangeChanged(position,products.size());

                                SaporiItalianoApplication.subtotal=SaporiItalianoApplication.subtotal-itemTotalPrice;

                                intefacCall.onItemDelete(SaporiItalianoApplication.subtotal);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }

                });









            }
        });

        holder.add_item_button_Cart.setOnClickListener(new View.OnClickListener() {

            int stockQuantityy=0;
            int itemQuantity= Integer.parseInt(androidQuantityItemCart.getText().toString());
            @Override
            public void onClick(View view) {

                Log.d("sgkins", String.valueOf(products.get(position).stockValue));
               if(itemQuantity==10){
                   Toast.makeText(context, "You can't add more then 10", Toast.LENGTH_SHORT).show();
                 return;
                      }

               if(itemQuantity==(products.get(position).stockValue)){
                  // Log.d("stockVadss", "in condition");
                   Toast.makeText(context, "You have crossed the Stock Limit", Toast.LENGTH_SHORT).show();
                   return;
               }

              //  Log.d("stockValue", String.valueOf(products.get(position).stockValue));
                itemQuantity++;
                androidQuantityItemCart.setText(""+itemQuantity);
                products.get(position).quantity=itemQuantity;

                SaporiItalianoApplication.subtotal=SaporiItalianoApplication.subtotal+
                        products.get(position).price;

                intefacCall.onItemDelete(SaporiItalianoApplication.subtotal);


                Product temp = new Product();
                temp.key = products.get(position).key;
                temp.imageUri = products.get(position).imageUri;
                temp.name = products.get(position).name;
                temp.price = products.get(position).price;
                temp.quantity = itemQuantity;
                temp.description =products.get(position).description;
                temp.CategoryId=products.get(position).CategoryId;
                SaporiItalianoApplication.products.set(position,temp);
                notifyDataSetChanged();



            }
        });


        holder.remove_item_buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemQuantity= Integer.parseInt(androidQuantityItemCart.getText().toString());

                if(itemQuantity==1){
                    return;
                }
                itemQuantity--;
                androidQuantityItemCart.setText(""+itemQuantity);

                SaporiItalianoApplication.subtotal=SaporiItalianoApplication.subtotal-
                        products.get(position).price;

                intefacCall.onItemDelete(SaporiItalianoApplication.subtotal);

                products.get(position).quantity=itemQuantity;
                Product temp = new Product();
                temp.key = products.get(position).key;
                temp.imageUri = products.get(position).imageUri;
                temp.name = products.get(position).name;
                temp.price = products.get(position).price;
                temp.quantity = itemQuantity;
                temp.description =products.get(position).description;
                temp.CategoryId=products.get(position).CategoryId;
                SaporiItalianoApplication.products.set(position,temp);
                notifyDataSetChanged();
            }
        });
        //  Glide.with(context).load(entityNewsArrayList.get(position).thumb).into(img_thumb);

    }



    @Override
    public int getItemCount() {
        return products.size();
    }


}
