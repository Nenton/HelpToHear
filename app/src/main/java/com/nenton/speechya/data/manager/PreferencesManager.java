package com.nenton.speechya.data.manager;

import android.content.SharedPreferences;

import com.nenton.speechya.utils.ConstantManager;
import com.nenton.speechya.utils.SpeechApplication;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    /**
     * Get shared preferences
     */
    public PreferencesManager() {
        this.mSharedPreferences = SpeechApplication.getSharedPreferences();
    }

    public void setCreateDateDialog(Long date){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(ConstantManager.CREATE_DATE_DIALOG,date);
        editor.apply();
    }

    public Long getCreateDateDialog(){
        return mSharedPreferences.getLong(ConstantManager.CREATE_DATE_DIALOG,0);
    }
}
