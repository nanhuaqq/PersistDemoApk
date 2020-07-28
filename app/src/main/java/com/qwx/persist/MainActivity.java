package com.qwx.persist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qwx.persist.beans.PersonBean;
import com.qwx.persist.beans.PersonBeanP;
import com.qwx.persist.beans.PersonProvider;
import com.qwx.persist.utils.CrashHandlerUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_serializable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serial("serial.sr", PersonProvider.createPersonSs());
            }
        });

        findViewById(R.id.tv_deserial).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                deserial("serial.sr");
            }
        });

        findViewById(R.id.tv_externalizable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serial("serial2.sr", PersonProvider.createPersonE());
            }
        });

        findViewById(R.id.tv_deexternalizable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deserial("serial2.sr");
            }
        });

        findViewById(R.id.tv_parcelable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel parcel = Parcel.obtain();
                List<PersonBeanP> persons = PersonProvider.createPersonP();
                long start = System.currentTimeMillis();
                parcel.writeParcelableList(persons, 0);
                byte[] bytes = parcel.marshall();
                File file = new File(CrashHandlerUtils.getDiskCacheDir(MainActivity.this, MyApplication.CACHE_PATH), "serial3.sr");

                ByteArrayOutputStream bos = null;
                try {
                    bos = new ByteArrayOutputStream(bytes.length);
                    bos.write(bytes);
                    bos.writeTo(new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) {
                        try {
                            bos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                long end = System.currentTimeMillis();
                long elapse = end - start;
                ToastUtils.showShort("time => " + elapse);
            }
        });

        findViewById(R.id.tv_deparcelable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = System.currentTimeMillis();
                Parcel parcel = Parcel.obtain();
                File file = new File(CrashHandlerUtils.getDiskCacheDir(MainActivity.this, MyApplication.CACHE_PATH), "serial3.sr");
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int available = fis.available();
                    byte[] bytes = new byte[available];
                    fis.read(bytes);
                    parcel.unmarshall(bytes, 0, available);
                    parcel.setDataPosition(0);
                    List list = new ArrayList<PersonBeanP>();
                    parcel.readParcelableList(list, MainActivity.this.getClassLoader());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();
                long elapse = end - start;
                ToastUtils.showShort("time => " + elapse);
            }
        });
    }

    private void deserial(String filename) {
        File file = new File(CrashHandlerUtils.getDiskCacheDir(MainActivity.this, MyApplication.CACHE_PATH), filename);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            try {
                long start = System.currentTimeMillis();
                List<PersonBean> personBeans = (List<PersonBean>) ois.readObject();
                long end = System.currentTimeMillis();
                long elapse = end - start;
                ToastUtils.showShort("time => " + elapse);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serial(String filename, List<? extends Serializable> beans) {
        File file = new File(CrashHandlerUtils.getDiskCacheDir(MainActivity.this, MyApplication.CACHE_PATH), filename);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            long start = System.currentTimeMillis();
            oos.writeObject(beans);
            long end = System.currentTimeMillis();
            long elapse = end - start;
            ToastUtils.showShort("time => " + elapse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}