package com.cegep.saporiitaliano.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Objects;

@IgnoreExtraProperties
public class Product implements Parcelable {

    @Exclude
    public String key;

    public String imageUri;

    public String name;
public String Weight;
    public long price;

    public long quantity;

    public String description;
    public String CategoryId;
    public long stockValue;

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
        dest.writeString(this.description);
        dest.writeString(this.CategoryId);
        dest.writeLong(this.stockValue);
        dest.writeString(this.Weight);
    }

    public Product() {
    }

    public Product(Parcel in) {
        this.key = in.readString();
        this.imageUri = in.readString();
        this.name = in.readString();
        this.price = in.readLong();
        this.quantity = in.readLong();
        this.description = in.readString();
        this.CategoryId=in.readString();
        this.stockValue=in.readLong();
        this.Weight=in.readString();
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
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description)&&
                Objects.equals(CategoryId, product.CategoryId)&&
                Objects.equals(stockValue,product.stockValue)&&
                Objects.equals(Weight,product.Weight);

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
        orderItem.description = description;
        return orderItem;
    }
}
