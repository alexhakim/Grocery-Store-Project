package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

    // declaring properties

    public int barcode = 0;
    public int isFood = 0;
    public String name = "";
    public double price = 0.0;
    public String quantity = "0";

    // constructor do not remove
    public Items(){
    }

    protected Items(Parcel in) {
        barcode = in.readInt();
        isFood = in.readInt();
        name = in.readString();
        price = in.readDouble();
        quantity = in.readString();

    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(barcode);
        parcel.writeInt(isFood);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(quantity);
    }
}
