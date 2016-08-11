package com.nenton.speechya.ui.activities;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nenton.speechya.R;
import com.nenton.speechya.data.chronos.ChronosCreate;
import com.nenton.speechya.data.chronos.ChronosGetMessages;
import com.nenton.speechya.data.chronos.ChronosUpdateDialog;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.ui.adapters.MessageAdapter;
import com.nenton.speechya.utils.ConstantManager;
import com.redmadrobot.chronos.ChronosConnector;

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

//    private static final String API_KEY = "9f298c59-147e-411d-aa6a-035979a134bf";
    public static final int REQUEST_PERMISSION_CODE = 1;

    private RecyclerView mRecyclerView;
    private ImageView mSendMessage;
    private TextInputEditText mFieldForSendMessage;
    private Toolbar mToolbar;
    private SmoothProgressBar mSmoothProgressBar;
    private RelativeLayout mRelativeLayout;

    private Date mDateCreateDialog;
    private DataManager mDataManager;
    private Boolean mBoolean;
    private Menu menuToolbar;
    private List<Messages> mMessages;
    private MessageAdapter mMessageAdapter;
    private Recognizer recognizer;
    private ChronosConnector mChronosConnector;

    /**
     * Create view main activity
     * @param savedInstanceState saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.messages_recycle);
        mSendMessage = (ImageView) findViewById(R.id.send_message_view);
        mFieldForSendMessage = (TextInputEditText) findViewById(R.id.write_txt);
        mToolbar = (Toolbar) findViewById(R.id.users_toolbar);
        mSmoothProgressBar = (SmoothProgressBar) findViewById(R.id.mirror_progressbar);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.write_bar);
        mDataManager = DataManager.getInstanse();
        mChronosConnector = mDataManager.getChronosConnector();
        mChronosConnector.onCreate(this,savedInstanceState);
        mBoolean = true;
        mDateCreateDialog = new Date(mDataManager.getPreferencesManager().getCreateDateDialog());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpeechKit.getInstance().configure(getApplicationContext(), mDataManager.getPreferencesManager().getApiKey());
        setupToolbar();
        setupButton();
        setupRecycle();
    }

    /**
     * Start activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(MainActivity.this, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            recognizer = Recognizer.create(mDataManager.getPreferencesManager().getLanguageRecognizer(), Recognizer.Model.NOTES, MainActivity.this);
            recognizer.start();
        }
    }

    /**
     * Pause activity
     */
    @Override
    protected void onPause() {
        mChronosConnector.onPause();
        super.onPause();
        resetRecognizer();
    }

    /**
     * onResume().
     */
    @Override
    protected void onResume() {
        mChronosConnector.onResume();
        super.onResume();
    }

    /**
     * Set recognizer
     * @param recognizer new recognizer
     */
    public void setRecognizer(Recognizer recognizer) {
        this.recognizer = recognizer;
    }

    /**
     * @return current recognizer
     */
    public Recognizer getRecognizer() {
        return recognizer;
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

    /**
     * Setup recycle view
     */
    private void setupRecycle() {
        mChronosConnector.runOperation(new ChronosGetMessages(),true);

    }

    public void onOperationFinished(final ChronosGetMessages.Result result) {
        if (result.isSuccessful() && result.getOutput() != null) {
            mMessages = result.getOutput();
            mMessageAdapter = new MessageAdapter(mMessages, this, getSupportFragmentManager());
            mRecyclerView.setAdapter(mMessageAdapter);
            mRecyclerView.scrollToPosition(mMessages.size() - 1);
        }
    }

    /**
     * Setup button new dialog
     */
    private void setupButton() {
        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFieldForSendMessage.getText().toString().isEmpty()) {
                    Date currentDate = new Date();
                    Messages messages = new Messages(mFieldForSendMessage.getText().toString(), false, mDateCreateDialog.getTime(), currentDate.getTime());
                    mChronosConnector.runOperation(new ChronosCreate(messages),true);
                    mChronosConnector.runOperation(new ChronosUpdateDialog(mFieldForSendMessage.getText().toString(),currentDate),true);
//                    mDataManager.createNewMessage(messages);
//                    mDataManager.updateDialog(mFieldForSendMessage.getText().toString(), currentDate);
                    mMessages.add(messages);
                    mMessageAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                    mFieldForSendMessage.setText("");
                }
            }
        });
    }

    /**
     * Reset recognizer
     */
    private void resetRecognizer() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer = null;
        }
    }

    /**
     * Create and start new recognizer
     */
    private void createAndStartRecognizer() {
        final Context context = MainActivity.this;
        if (ContextCompat.checkSelfPermission(context, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            recognizer = Recognizer.create(mDataManager.getPreferencesManager().getLanguageRecognizer(), Recognizer.Model.NOTES, MainActivity.this);
            recognizer.start();
        }
    }

    /**
     * Reaction on start recording
     * @param recognizer current recognizer
     */
    @Override
    public void onRecordingBegin(Recognizer recognizer) {
        if (!mBoolean) {
            mBoolean = true;
            onCreateOptionsMenu(menuToolbar);
        }
        mSmoothProgressBar.setIndeterminate(true);
        mSmoothProgressBar.progressiveStart();
    }

    /**
     * Reaction on speech start
     * @param recognizer current recognizer
     */
    @Override
    public void onSpeechDetected(Recognizer recognizer) {
    }

    /**
     * Reaction on speech stop
     * @param recognizer current recognizer
     */
    @Override
    public void onSpeechEnds(Recognizer recognizer) {
        mSmoothProgressBar.progressiveStop();
    }

    /**
     * Reaction on stop recording
     * @param recognizer current recognizer
     */
    @Override
    public void onRecordingDone(Recognizer recognizer) {
    }

    /**
     * Data recording
     * @param recognizer current recognizer
     * @param bytes array bytes
     */
    @Override
    public void onSoundDataRecorded(Recognizer recognizer, byte[] bytes) {
    }

    /**
     * Upgrade power speech
     * @param recognizer current recognizer
     * @param power power speech
     */
    @Override
    public void onPowerUpdated(Recognizer recognizer, float power) {
        mSmoothProgressBar.setSmoothProgressDrawableSpeed(0.5f + 2 * power);
        mSmoothProgressBar.setSmoothProgressDrawableSeparatorLength((int) (getResources().getDimension(R.dimen.progress_50) - power * getResources().getDimension(R.dimen.progress_30)));
    }

    /**
     * Partial results
     * @param recognizer current recognizer
     * @param recognition available recognition
     * @param b speech not null or null
     */
    @Override
    public void onPartialResults(Recognizer recognizer, Recognition recognition, boolean b) {
    }

    /**
     * End result recognition
     * @param recognizer current recognizer
     * @param recognition recognition
     */
    @Override
    public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
        if (!recognition.getBestResultText().isEmpty()) {
            Date currentDate = new Date();
            Messages messages = new Messages(recognition.getBestResultText(), true, mDateCreateDialog.getTime(), currentDate.getTime());
            mChronosConnector.runOperation(new ChronosCreate(messages),true);
//            mDataManager.createNewMessage(messages);
            mChronosConnector.runOperation(new ChronosUpdateDialog(recognition.getBestResultText(), currentDate),true);
//            mDataManager.updateDialog(recognition.getBestResultText(), currentDate);
            mMessages.add(messages);
            mMessageAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mMessages.size() - 1);
        }
        createAndStartRecognizer();
    }

    /**
     * Error in process work recognizer
     * @param recognizer current recognizer
     * @param error error
     */
    @Override
    public void onError(Recognizer recognizer, ru.yandex.speechkit.Error error) {
        switch (error.getCode()) {
            case Error.ERROR_CANCELED:
                break;
            case Error.ERROR_NO_SPEECH:
                createAndStartRecognizer();
                showToast(ConstantManager.STRING_SPEECH_NOT_DETECTED);
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
            mSmoothProgressBar.setVisibility(View.VISIBLE);
        } else {
            getMenuInflater().inflate(R.menu.menu_messages_not, menu);
            mSmoothProgressBar.setVisibility(View.GONE);
        }
        menuToolbar = menu;
        return true;
    }

    /**
     * Item selected
     * @param item item
     * @return click or not click
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.micro) {
            if (Recognizer.isRecognitionAvailable()) {
                if (mBoolean) {
                    item.setIcon(R.drawable.ic_mic_off_white_24dp);
                    resetRecognizer();
                    mBoolean = false;
                    mSmoothProgressBar.setVisibility(View.GONE);
                } else {
                    createAndStartRecognizer();
                    item.setIcon(R.drawable.ic_mic_white_24dp);
                    mBoolean = true;
                    mSmoothProgressBar.setVisibility(View.VISIBLE);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean getBoolean() {
        return mBoolean;
    }


    /**
     * Show toast message
     * @param message message
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Check permissions
     * @param requestCode request code
     * @param permissions array permissions must be granted
     * @param grantResults grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {
            createAndStartRecognizer();
        } else {
            showToast(ConstantManager.STRING_NOT_GRANTED);
        }
    }
}