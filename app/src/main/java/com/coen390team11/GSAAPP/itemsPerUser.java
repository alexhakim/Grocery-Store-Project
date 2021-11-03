package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class itemsPerUser implements Parcelable {

    public List<String> items;

    public itemsPerUser(){
    }

    protected itemsPerUser(Parcel in) {
        items = in.createStringArrayList();
    }

    public static final Creator<itemsPerUser> CREATOR = new Creator<itemsPerUser>() {
        @Override
        public itemsPerUser createFromParcel(Parcel in) {
            return new itemsPerUser(in);
        }

        @Override
        public itemsPerUser[] newArray(int size) {
            return new itemsPerUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(items);
    }
}
