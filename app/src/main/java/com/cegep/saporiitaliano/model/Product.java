package com.cegep.saporiitaliano.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

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
        dest.writeString(this.imageUri);
        dest.writeString(this.name);
        dest.writeLong(this.price);
        dest.writeLong(this.quantity);
    }

    public Product() {
    }

    protected Product(Parcel in) {
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
}
