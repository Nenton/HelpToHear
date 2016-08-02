package com.nenton.speechya.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.DaoMaster;
import com.nenton.speechya.data.storage.models.DaoSession;
import com.nenton.speechya.data.storage.models.StandartPhrase;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpeechApplication extends Application{

    private static Context sContext;
    public static SharedPreferences sSharedPreferences;
    private static DaoSession sDaoSession;

    /**
     * Create shared preferences
     */
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,ConstantManager.NAME_BD);
        Database database = helper.getWritableDb();
        sDaoSession = new DaoMaster(database).newSession();
        Stetho.initializeWithDefaults(this);
        setupStandartPhrase();
    }

    private void setupStandartPhrase() {
        List<StandartPhrase> standartPhrases = new ArrayList<>();
        standartPhrases.add(new StandartPhrase("Привет",new Date().getTime()));
        standartPhrases.add(new StandartPhrase("Крикните",new Date().getTime()));
        DataManager.getInstanse().setStandartPhrase(standartPhrases);
    }

    /**
     * @return This shared preferences
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
