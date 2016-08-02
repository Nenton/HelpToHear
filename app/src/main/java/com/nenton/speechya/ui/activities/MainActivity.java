package com.nenton.speechya.ui.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.ui.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognition;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.RecognizerListener;
import ru.yandex.speechkit.SpeechKit;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements RecognizerListener{


    private static final String API_KEY = "9f298c59-147e-411d-aa6a-035979a134bf";
    private static final int NOTES_CODE = 31;
    private static final int REQUEST_PERMISSION_CODE = 1;

    private Recognizer recognizer;

    ProgressBar mProgressBar;
    Button mButtonStart;
    List<Messages> mMessages;
    MessageAdapter mMessageAdapter;
    RecyclerView mRecyclerView;
    ImageView mSendMessage;
    TextInputEditText mFieldForSendMessage;
    Toolbar mToolbar;
    Date mDateCreateDialog;
    DataManager mDataManager;


    @Override
    protected void onPause() {
        super.onPause();
        resetRecognizer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(MainActivity.this, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, MainActivity.this);
            recognizer.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataManager = DataManager.getInstanse();
        mDateCreateDialog = new Date(mDataManager.getPreferencesManager().getCreateDateDialog());
        mButtonStart = (Button) findViewById(R.id.button);
        mProgressBar = (ProgressBar) findViewById(R.id.voice_power_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.messages_recycle);
        mSendMessage = (ImageView) findViewById(R.id.send_message_view);
        mFieldForSendMessage = (TextInputEditText) findViewById(R.id.write_txt);
        mToolbar = (Toolbar)findViewById(R.id.users_toolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpeechKit.getInstance().configure(getApplicationContext(), API_KEY);
        setupToolbar();
        setupButton();
        setupRecycle();
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecycle() {
        mMessages = mDataManager.getMessages();
        mMessageAdapter = new MessageAdapter(mMessages);
        mRecyclerView.setAdapter(mMessageAdapter);
        mRecyclerView.scrollToPosition(mMessages.size()-1);
    }

    private void setupButton() {
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAndStartRecognizer();
//                updateTextResult("");
//                // To start recognition create an Intent with required extras.
//                Intent intent = new Intent(MainActivity.this, RecognizerActivity.class);
//                // Specify the model for better results.
//                intent.putExtra(RecognizerActivity.EXTRA_MODEL, Recognizer.Model.NOTES);
//                // Specify the language.
//                intent.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Recognizer.Language.RUSSIAN);
//                // To get recognition results use startActivityForResult(),
//                // also don't forget to override onActivityResult().
//                startActivityForResult(intent, NOTES_CODE);
            }
        });
        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFieldForSendMessage.getText().toString().isEmpty()) {
                    Date currentDate = new Date();
                    Messages messages = new Messages(mFieldForSendMessage.getText().toString(),false,mDateCreateDialog,currentDate);
                    mDataManager.createNewMessage(messages);
                    mDataManager.updateDialog(mFieldForSendMessage.getText().toString(),currentDate);
                    mMessages.add(messages);
                    mMessageAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                    mFieldForSendMessage.setText("");
                }
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, requestCode, data);
//        if (requestCode == NOTES_CODE) {
//            if (resultCode == RecognizerActivity.RESULT_OK && data != null) {
//                final String result = data.getStringExtra(RecognizerActivity.EXTRA_RESULT);
//                updateTextResult(result);
//            } else if (resultCode == RecognizerActivity.RESULT_ERROR) {
//                String error = ((ru.yandex.speechkit.Error) data.getSerializableExtra(RecognizerActivity.EXTRA_ERROR)).getString();
//                updateTextResult(error);
//            }
//        }
//    }


    private void resetRecognizer() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer = null;
        }
    }

    @Override
    public void onRecordingBegin(Recognizer recognizer) {
        updateStatus("Recording begin");
    }

    @Override
    public void onSpeechDetected(Recognizer recognizer) {
        updateStatus("Speech detected");
    }

    @Override
    public void onSpeechEnds(Recognizer recognizer) {
        updateStatus("Speech ends");
    }

    @Override
    public void onRecordingDone(Recognizer recognizer) {
        updateStatus("Recording done");
    }

    @Override
    public void onSoundDataRecorded(Recognizer recognizer, byte[] bytes) {
    }

    @Override
    public void onPowerUpdated(Recognizer recognizer, float power) {
        updateProgress((int) (power * mProgressBar.getMax()));
    }

    @Override
    public void onPartialResults(Recognizer recognizer, Recognition recognition, boolean b) {
    }

    @Override
    public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
        if (!recognition.getBestResultText().isEmpty()) {
            Date currentDate = new Date();
            Messages messages = new Messages(recognition.getBestResultText(),true,mDateCreateDialog,currentDate);
            mDataManager.createNewMessage(messages);
            mDataManager.updateDialog(recognition.getBestResultText(),currentDate);
            mMessages.add(messages);
            mMessageAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mMessages.size() - 1);
        }
        updateProgress(0);
    }

    @Override
    public void onError(Recognizer recognizer, ru.yandex.speechkit.Error error) {
        if (error.getCode() == Error.ERROR_CANCELED) {
            updateProgress(0);
        } else {
            updateStatus("Error occurred " + error.getString());
        }
    }

    private void createAndStartRecognizer() {
        final Context context = MainActivity.this;
        if (ContextCompat.checkSelfPermission(context, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, MainActivity.this);
            recognizer.start();
        }
    }


    private void updateStatus(final String text) {
        mButtonStart.setText(text);
    }

    private void updateProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {
            createAndStartRecognizer();
        } else {
            updateStatus("Record audio permission was not granted");
        }
    }
}
