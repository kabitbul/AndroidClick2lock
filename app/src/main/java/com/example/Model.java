package com.example;

import android.app.Application;
public class Model {
    private static Model sInstance = null;
    public  static Model getInstance(Application application){
        if(sInstance == null){
            sInstance = new Model(application);
        }
        return sInstance;
    }

    private  final Application mApplication;
    private Model(Application application) {mApplication = application;}
    public Application getApplication() {return mApplication;}
    public  void  register(String email,String password, String ConfirmPassword)
    {

    }
}
