package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class deleteItem implements Parcelable {

    public String itemDeleted;

    public deleteItem(){}

    protected deleteItem(Parcel in) {
        itemDeleted = in.readString();
    }

    public static final Creator<deleteItem> CREATOR = new Creator<deleteItem>() {
        @Override
        public deleteItem createFromParcel(Parcel in) {
            return new deleteItem(in);
        }

        @Override
        public deleteItem[] newArray(int size) {
            return new deleteItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemDeleted);
    }
}
