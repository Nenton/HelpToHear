package com.nenton.speechya.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.ui.adapters.MessageAdapter;
import com.nenton.speechya.utils.ConstantManager;

import java.util.Date;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognition;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.RecognizerListener;
import ru.yandex.speechkit.SpeechKit;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements RecognizerListener {


    private static final String API_KEY = "9f298c59-147e-411d-aa6a-035979a134bf";
    private static final int REQUEST_PERMISSION_CODE = 1;

    private Recognizer recognizer;

    //    ProgressBar mProgressBar;
//    Button mButtonStart;
    List<Messages> mMessages;
    MessageAdapter mMessageAdapter;
    RecyclerView mRecyclerView;
    ImageView mSendMessage;
    TextInputEditText mFieldForSendMessage;
    Toolbar mToolbar;
    Date mDateCreateDialog;
    DataManager mDataManager;
//    RoundCornerProgressBar mCornerProgressBar;
    Boolean mBoolean;
    //    Boolean mBoolean2;
    Menu menuToolbar;
    SmoothProgressBar mSmoothProgressBar;

//    public static void dialogShowFromAdapter() {
//        showDialog(ConstantManager.SHOW_RESIZE_MESSAGE);
//    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id){
//            case ConstantManager.SHOW_RESIZE_MESSAGE:
//                LayoutInflater layoutInflater  = this.getLayoutInflater();
//                View view = layoutInflater.inflate(R.layout.dialog_resize_message,null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setView(view);
//                return builder.create();
//            default:
//                return null;
//        }
//    }

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
//        mButtonStart = (Button) findViewById(R.id.button);
//        mProgressBar = (ProgressBar) findViewById(R.id.voice_power_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.messages_recycle);
        mSendMessage = (ImageView) findViewById(R.id.send_message_view);
        mFieldForSendMessage = (TextInputEditText) findViewById(R.id.write_txt);
        mToolbar = (Toolbar) findViewById(R.id.users_toolbar);
//        mCornerProgressBar = (RoundCornerProgressBar) findViewById(R.id.round_corner_progress);
        mSmoothProgressBar = (SmoothProgressBar) findViewById(R.id.mirror_progressbar);
//        mCornerProgressBar.setMax(100f);
//        mBoolean2 = false;
        mBoolean = true;
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
        mMessageAdapter = new MessageAdapter(mMessages, new MessageAdapter.onGetFragmentManager() {
            @Override
            public FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }

        });
        mRecyclerView.setAdapter(mMessageAdapter);
        mRecyclerView.scrollToPosition(mMessages.size() - 1);
    }

    private void setupButton() {
//        mButtonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                createAndStartRecognizer();
//            }
//        });
        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFieldForSendMessage.getText().toString().isEmpty()) {
                    Date currentDate = new Date();
                    Messages messages = new Messages(mFieldForSendMessage.getText().toString(), false, mDateCreateDialog.getTime(), currentDate.getTime());
                    mDataManager.createNewMessage(messages);
                    mDataManager.updateDialog(mFieldForSendMessage.getText().toString(), currentDate);
                    mMessages.add(messages);
                    mMessageAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                    mFieldForSendMessage.setText("");
                }
            }
        });
    }

    private void resetRecognizer() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer = null;
        }
    }

    @Override
    public void onRecordingBegin(Recognizer recognizer) {
        if (!mBoolean) {
            mBoolean = true;
            onCreateOptionsMenu(menuToolbar);
        }
    }

    @Override
    public void onSpeechDetected(Recognizer recognizer) {
        mSmoothProgressBar.setIndeterminate(true);
        mSmoothProgressBar.progressiveStart();
    }

    @Override
    public void onSpeechEnds(Recognizer recognizer) {
        mSmoothProgressBar.progressiveStop();
    }

    @Override
    public void onRecordingDone(Recognizer recognizer) {
    }

    @Override
    public void onSoundDataRecorded(Recognizer recognizer, byte[] bytes) {
    }

    @Override
    public void onPowerUpdated(Recognizer recognizer, float power) {
//        updateProgress(power * mCornerProgressBar.getMax());
        mSmoothProgressBar.setSmoothProgressDrawableSpeed(0.5f + 2 * power);
        mSmoothProgressBar.setSmoothProgressDrawableSeparatorLength((int) (getResources().getDimension(R.dimen.progress_50) - power * getResources().getDimension(R.dimen.progress_30)));
    }

    @Override
    public void onPartialResults(Recognizer recognizer, Recognition recognition, boolean b) {
    }

    @Override
    public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
        if (!recognition.getBestResultText().isEmpty()) {
            Date currentDate = new Date();
            Messages messages = new Messages(recognition.getBestResultText(), true, mDateCreateDialog.getTime(), currentDate.getTime());
            mDataManager.createNewMessage(messages);
            mDataManager.updateDialog(recognition.getBestResultText(), currentDate);
            mMessages.add(messages);
            mMessageAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mMessages.size() - 1);
        }
        createAndStartRecognizer();
//        updateProgress(0f);
    }

    @Override
    public void onError(Recognizer recognizer, ru.yandex.speechkit.Error error) {
        switch (error.getCode()) {
            case Error.ERROR_CANCELED:
//                updateProgress(0f);
                break;
            case Error.ERROR_NO_SPEECH:
                createAndStartRecognizer();
                showToast("Голос не обнаружен");
                break;
            default:
                mBoolean = false;
                if (menuToolbar != null) {
                    onCreateOptionsMenu(menuToolbar);
                }
                showToast("Error occurred " + error.getString());
                break;
        }
// TODO: 03.08.2016 обработка ошибок
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (mBoolean) {
            getMenuInflater().inflate(R.menu.menu_messages, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_messages_not, menu);
        }
        menuToolbar = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.micro) {
            if (Recognizer.isRecognitionAvailable()) {
                if (mBoolean) {
                    item.setIcon(R.drawable.ic_mic_off_white_24dp);
                    resetRecognizer();
                    mBoolean = false;
                } else {
                    createAndStartRecognizer();
                    item.setIcon(R.drawable.ic_mic_white_24dp);
                    mBoolean = true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
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

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

//    private void updateStatus(final String text) {
//        mButtonStart.setText(text);
//    }

//    private void updateProgress(float progresss) {
////        mProgressBar.setProgress(progress);
////        mCornerProgressBar.setProgress(progresss);
//    }

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
            showToast("Record audio permission was not granted");
        }
    }
}
