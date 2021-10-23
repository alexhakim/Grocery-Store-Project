package com.coen390team11.GSAAPP;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    // declaring properties

    public String id = "";
    public String firstName = "";
    public String lastName = "";
    public String email = "";
    public Long mobile = Long.valueOf(0);
    public String gender = "";
    public int profileCompleted = 0;
    public long rewardsCardNumber = Long.valueOf(0);

    // constructor do not remove
    public User(){

    }
    
    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        mobile = in.readLong();
        gender = in.readString();
        profileCompleted = in.readInt();
        rewardsCardNumber = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeLong(mobile);
        parcel.writeString(gender);
        parcel.writeInt(profileCompleted);
        parcel.writeLong(rewardsCardNumber);
    }
}
