package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class PointsPerUser implements Parcelable {


    public double points;

    public PointsPerUser(){

    }

    protected PointsPerUser(Parcel in) {
        points = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PointsPerUser> CREATOR = new Creator<PointsPerUser>() {
        @Override
        public PointsPerUser createFromParcel(Parcel in) {
            return new PointsPerUser(in);
        }

        @Override
        public PointsPerUser[] newArray(int size) {
            return new PointsPerUser[size];
        }
    };
}
