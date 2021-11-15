package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class tempBag implements Parcelable {

    public String tempCurrentBag = "";
    public String withDuplicates = "";

    public tempBag(){
    }

    protected tempBag(Parcel in) {
        tempCurrentBag = in.readString();
        withDuplicates = in.readString();
    }

    public static final Creator<tempBag> CREATOR = new Creator<tempBag>() {
        @Override
        public tempBag createFromParcel(Parcel in) {
            return new tempBag(in);
        }

        @Override
        public tempBag[] newArray(int size) {
            return new tempBag[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tempCurrentBag);
        parcel.writeString(withDuplicates);
    }
}




