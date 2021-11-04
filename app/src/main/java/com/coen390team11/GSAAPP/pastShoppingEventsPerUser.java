package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class pastShoppingEventsPerUser implements Parcelable {

    public int purchaseCompleted0 = 0;
    public int purchaseCompleted1 = 0;
    public int purchaseCompleted2 = 0;
    public int purchaseCompleted3 = 0;
    public int purchaseCompleted4 = 0;
    public int purchaseCompleted5 = 0;
    public int purchaseCompleted6 = 0;
    public int purchaseCompleted7 = 0;
    public int purchaseCompleted8 = 0;
    public int purchaseCompleted9 = 0;
    public String shoppingEvent0 = "";
    public String shoppingEvent1 = "";
    public String shoppingEvent2 = "";
    public String shoppingEvent3 = "";
    public String shoppingEvent4 = "";
    public String shoppingEvent5 = "";
    public String shoppingEvent6 = "";
    public String shoppingEvent7 = "";
    public String shoppingEvent8 = "";
    public String shoppingEvent9 = "";

    protected pastShoppingEventsPerUser(Parcel in) {
        purchaseCompleted0 = in.readInt();
        purchaseCompleted1 = in.readInt();
        purchaseCompleted2 = in.readInt();
        purchaseCompleted3 = in.readInt();
        purchaseCompleted4 = in.readInt();
        purchaseCompleted5 = in.readInt();
        purchaseCompleted6 = in.readInt();
        purchaseCompleted7 = in.readInt();
        purchaseCompleted8 = in.readInt();
        purchaseCompleted9 = in.readInt();
        shoppingEvent0 = in.readString();
        shoppingEvent1 = in.readString();
        shoppingEvent2 = in.readString();
        shoppingEvent3 = in.readString();
        shoppingEvent4 = in.readString();
        shoppingEvent5 = in.readString();
        shoppingEvent6 = in.readString();
        shoppingEvent7 = in.readString();
        shoppingEvent8 = in.readString();
        shoppingEvent9 = in.readString();
    }

    public static final Creator<pastShoppingEventsPerUser> CREATOR = new Creator<pastShoppingEventsPerUser>() {
        @Override
        public pastShoppingEventsPerUser createFromParcel(Parcel in) {
            return new pastShoppingEventsPerUser(in);
        }

        @Override
        public pastShoppingEventsPerUser[] newArray(int size) {
            return new pastShoppingEventsPerUser[size];
        }
    };

    public pastShoppingEventsPerUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(purchaseCompleted0);
        parcel.writeInt(purchaseCompleted1);
        parcel.writeInt(purchaseCompleted2);
        parcel.writeInt(purchaseCompleted3);
        parcel.writeInt(purchaseCompleted4);
        parcel.writeInt(purchaseCompleted5);
        parcel.writeInt(purchaseCompleted6);
        parcel.writeInt(purchaseCompleted7);
        parcel.writeInt(purchaseCompleted8);
        parcel.writeInt(purchaseCompleted9);
        parcel.writeString(shoppingEvent0);
        parcel.writeString(shoppingEvent1);
        parcel.writeString(shoppingEvent2);
        parcel.writeString(shoppingEvent3);
        parcel.writeString(shoppingEvent4);
        parcel.writeString(shoppingEvent5);
        parcel.writeString(shoppingEvent6);
        parcel.writeString(shoppingEvent7);
        parcel.writeString(shoppingEvent8);
        parcel.writeString(shoppingEvent9);
    }
}
