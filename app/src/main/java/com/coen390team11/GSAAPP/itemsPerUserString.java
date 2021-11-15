package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class itemsPerUserString implements Parcelable {

    public String currentBag;

    public  itemsPerUserString(){
    }

    protected itemsPerUserString(Parcel in) {
        currentBag = in.readString();
    }

    public static final Creator<itemsPerUserString> CREATOR = new Creator<itemsPerUserString>() {
        @Override
        public itemsPerUserString createFromParcel(Parcel in) {
            return new itemsPerUserString(in);
        }

        @Override
        public itemsPerUserString[] newArray(int size) {
            return new itemsPerUserString[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(currentBag);
    }
}
