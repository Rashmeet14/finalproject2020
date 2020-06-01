package com.cegep.saporiitaliano.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Objects;

@IgnoreExtraProperties
public class Product implements Parcelable {

    @Exclude
    public String key;

    public String imageUri;

    public String name;

    public long price;

    public long quantity;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.imageUri);
        dest.writeString(this.name);
        dest.writeLong(this.price);
        dest.writeLong(this.quantity);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.key = in.readString();
        this.imageUri = in.readString();
        this.name = in.readString();
        this.price = in.readLong();
        this.quantity = in.readLong();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return price == product.price &&
                quantity == product.quantity &&
                Objects.equals(key, product.key) &&
                Objects.equals(imageUri, product.imageUri) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, imageUri, name, price, quantity);
    }

    public OrderItem getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.id = key;
        orderItem.imageUri = imageUri;
        orderItem.name = name;
        orderItem.price = price;
        orderItem.quantity = quantity;
        orderItem.sum = price * quantity;
        return orderItem;
    }
}
