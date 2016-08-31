package com.example.okhttp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by milton on 16/8/30.
 */
public class User implements Parcelable {


    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String a;

    public User() {
    }

    protected User(Parcel in) {
        this.a = in.readString();
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.a);
    }
}
