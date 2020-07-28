package com.qwx.persist.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqin on 2020/7/28
 * <p>
 * email qqin@finbtc.net
 */
public class PersonProvider {
    public static List<PersonBean> createPersonE(){
        List<PersonBean> personBeans = new ArrayList<>();
        PersonBean personBean = new PersonBean("personOne", 24, true);
        personBeans.add(personBean);

        personBean = new PersonBean("personTwo", 28, false);
        personBeans.add(personBean);

        for (int i = 0; i < 10000; i++) {
            try {
                PersonBean clonedBean = (PersonBean) personBean.clone();
                personBeans.add(clonedBean);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return personBeans;
    }

    public static List<PersonBeanS> createPersonSs(){
        List<PersonBeanS> personBeans = new ArrayList<>();
        PersonBeanS personBean = new PersonBeanS("personOne", 24, true);
        personBeans.add(personBean);

        personBean = new PersonBeanS("personTwo", 28, false);
        personBeans.add(personBean);

        for (int i = 0; i < 10000; i++) {
            try {
                PersonBeanS clonedBean = (PersonBeanS) personBean.clone();
                personBeans.add(clonedBean);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return personBeans;
    }

    public static List<PersonBeanP> createPersonP(){
        List<PersonBeanP> personBeans = new ArrayList<>();
        PersonBeanP personBean = new PersonBeanP("personOne", 24, true);
        personBeans.add(personBean);

        personBean = new PersonBeanP("personTwo", 28, false);
        personBeans.add(personBean);

        for (int i = 0; i < 10000; i++) {
            try {
                PersonBeanP clonedBean = (PersonBeanP) personBean.clone();
                personBeans.add(clonedBean);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return personBeans;
    }
}
