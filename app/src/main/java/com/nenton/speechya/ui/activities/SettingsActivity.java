package com.nenton.speechya.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.utils.Config;
import com.nenton.speechya.utils.ConstantManager;

import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.Vocalizer;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private AppCompatSpinner mSpinnerLanguage;
    private AppCompatSpinner mSpinnerVoice;
    private TextInputLayout mTextInputLayout;
    private Button mButton;
    private ImageView mImageView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar = (Toolbar)findViewById(R.id.settings_toolbar);
        mSpinnerLanguage = (AppCompatSpinner)findViewById(R.id.spinner_language);
        mSpinnerVoice = (AppCompatSpinner)findViewById(R.id.spinner_voice);
        mTextInputLayout = (TextInputLayout)findViewById(R.id.edit_api_key_field);
        mButton = (Button)findViewById(R.id.edit_api_key_button);
        mImageView = (ImageView)findViewById(R.id.information_api);
        mEditText = (EditText)findViewById(R.id.edit_txt_api_key);
        setupToolbar();
        setupData();
        setupInfo();
        if (savedInstanceState != null){
            mEditText.setText(savedInstanceState.getString(ConstantManager.STRING_EDIT_API_SETTINGS));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ConstantManager.STRING_EDIT_API_SETTINGS,mEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * Setup info go to site developer account
     */
    private void setupInfo() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Config.YANDEX_API_DEVELOPER);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    /**
     * Setup data
     */
    private void setupData() {

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstanse().getPreferencesManager().setApiKey(mTextInputLayout.getEditText().getText().toString());
                mTextInputLayout.setHint(DataManager.getInstanse().getPreferencesManager().getApiKey());
            }
        });

        switch (DataManager.getInstanse().getPreferencesManager().getLanguageRecognizer()){
            case Recognizer.Language.RUSSIAN:
                mSpinnerLanguage.setSelection(0);
                break;
            case Recognizer.Language.ENGLISH:
                mSpinnerLanguage.setSelection(1);
                break;
        }

        switch (DataManager.getInstanse().getPreferencesManager().getSpeechVoice()){
            case Vocalizer.Voice.ALYSS:
                mSpinnerVoice.setSelection(0);
                break;
            case Vocalizer.Voice.ERMIL:
                mSpinnerVoice.setSelection(1);
                break;
            case Vocalizer.Voice.JANE:
                mSpinnerVoice.setSelection(2);
                break;
            case Vocalizer.Voice.OMAZH:
                mSpinnerVoice.setSelection(3);
                break;
            case Vocalizer.Voice.ZAHAR:
                mSpinnerVoice.setSelection(4);
                break;
        }

        mTextInputLayout.setHint(DataManager.getInstanse().getPreferencesManager().getApiKey());
        mSpinnerVoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        DataManager.getInstanse().getPreferencesManager().setSpeechVoice(Vocalizer.Voice.ALYSS);
                        break;
                    case 1:
                        DataManager.getInstanse().getPreferencesManager().setSpeechVoice(Vocalizer.Voice.ERMIL);
                        break;
                    case 2:
                        DataManager.getInstanse().getPreferencesManager().setSpeechVoice(Vocalizer.Voice.JANE);
                        break;
                    case 3:
                        DataManager.getInstanse().getPreferencesManager().setSpeechVoice(Vocalizer.Voice.OMAZH);
                        break;
                    case 4:
                        DataManager.getInstanse().getPreferencesManager().setSpeechVoice(Vocalizer.Voice.ZAHAR);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        DataManager.getInstanse().getPreferencesManager().setLanguage(Recognizer.Language.RUSSIAN,Vocalizer.Language.RUSSIAN);
                        break;
                    case 1:
                        DataManager.getInstanse().getPreferencesManager().setLanguage(Recognizer.Language.ENGLISH,Vocalizer.Language.ENGLISH);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
