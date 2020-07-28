package com.qwx.persist.beans;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import androidx.annotation.NonNull;

/**
 * Created by qqin on 2020/7/28
 * <p>
 * email qqin@finbtc.net
 */
public class PersonBeanS implements Serializable, Cloneable {
    private String name;
    private int age;
    private boolean gender;

    public PersonBeanS() {
    }

    public PersonBeanS(String name, int age, boolean gender) {
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

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
