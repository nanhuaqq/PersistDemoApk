package com.qwx.persist.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import androidx.annotation.NonNull;

/**
 * Created by qqin on 2020/7/28
 * <p>
 * email qqin@finbtc.net
 */
public class PersonBeanP implements Parcelable, Cloneable {
    private String name;
    private int age;
    private boolean gender;

    public PersonBeanP() {
    }

    public PersonBeanP(String name, int age, boolean gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    protected PersonBeanP(Parcel in) {
        name = in.readString();
        age = in.readInt();
        gender = in.readByte() != 0;
    }

    public static final Creator<PersonBeanP> CREATOR = new Creator<PersonBeanP>() {
        @Override
        public PersonBeanP createFromParcel(Parcel in) {
            return new PersonBeanP(in);
        }

        @Override
        public PersonBeanP[] newArray(int size) {
            return new PersonBeanP[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeByte((byte) (gender ? 1 : 0));
    }
}
