package com.nenton.speechya.data.manager;

import android.content.SharedPreferences;

import com.nenton.speechya.utils.ConstantManager;
import com.nenton.speechya.utils.SpeechApplication;

import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.Vocalizer;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    /**
     * Get shared preferences
     */
    public PreferencesManager() {
        this.mSharedPreferences = SpeechApplication.getSharedPreferences();
    }

    /**
     * Save date create dialog
     *
     * @param createDateDialog long type date
     */
    public void setCreateDateDialog(Long createDateDialog) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(ConstantManager.CREATE_DATE_DIALOG, createDateDialog);
        editor.apply();
    }

    /**
     * @return current date create dialog
     */
    public Long getCreateDateDialog() {
        return mSharedPreferences.getLong(ConstantManager.CREATE_DATE_DIALOG, ConstantManager.LONG_NULL);
    }

    public void setLanguage(String language,String voiceVocalizer) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUGE_KEY, language);
        editor.putString(ConstantManager.SPEECH_VOICE_VOCALIZER_KEY, voiceVocalizer);
        editor.apply();
    }

    public String getLanguageRecognizer() {
        return mSharedPreferences.getString(ConstantManager.LANGUGE_KEY, Recognizer.Language.RUSSIAN);
    }

    public String getLanguageVocalizer() {
        return mSharedPreferences.getString(ConstantManager.SPEECH_VOICE_VOCALIZER_KEY, Vocalizer.Language.RUSSIAN);
    }

    public void setApiKey(String apiKey) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.API_KEY, apiKey);
        editor.apply();
    }

    public String getApiKey() {
        return mSharedPreferences.getString(ConstantManager.API_KEY, "");
    }

    public void setSpeechVoice(String voice) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.SPEECH_VOICE_KEY, voice);
        editor.apply();
    }

    public String getSpeechVoice() {
        return mSharedPreferences.getString(ConstantManager.SPEECH_VOICE_KEY, Vocalizer.Voice.ZAHAR);
    }
}