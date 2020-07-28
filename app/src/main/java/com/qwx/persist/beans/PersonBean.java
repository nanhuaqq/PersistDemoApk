package com.qwx.persist.beans;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import androidx.annotation.NonNull;

/**
 * Created by qqin on 2020/7/28
 * <p>
 * email qqin@finbtc.net
 */
public class PersonBean implements Externalizable, Cloneable {
    private String name;
    private int age;
    private boolean gender;

    public PersonBean() {
    }

    public PersonBean(String name, int age, boolean gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
        out.writeBoolean(gender);
    }

    @Override
    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        this.name = (String) in.readObject();
        this.age = in.readInt();
        this.gender = in.readBoolean();
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
