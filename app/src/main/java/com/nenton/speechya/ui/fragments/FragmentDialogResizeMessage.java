package com.nenton.speechya.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.ui.activities.MainActivity;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class FragmentDialogResizeMessage extends DialogFragment {

    private String mMessage;
    private Vocalizer vocalizer;
    private MainActivity mMainActivity;
    private Context mContext;

    public void setMessage(String message, MainActivity mainActivity, Context context) {
        mMessage = message;
        mMainActivity = mainActivity;
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_resize_message, null);
        ((TextView) view.findViewById(R.id.resize_message_txt)).setText(mMessage);
        ((TextView) view.findViewById(R.id.resize_message_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogResizeMessage.this.dismiss();
            }
        });
        final CircularProgressBar circularProgressBar = (CircularProgressBar) view.findViewById(R.id.resize_message_progressbar);
        ImageView imageView = (ImageView) view.findViewById(R.id.resize_message_say);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularProgressBar.setVisibility(View.VISIBLE);
                resetVocalizer();
                vocalizer = Vocalizer.createVocalizer(DataManager.getInstanse().getPreferencesManager().getLanguageVocalizer(), mMessage, true, DataManager.getInstanse().getPreferencesManager().getSpeechVoice());
                vocalizer.setListener(new VocalizerListener() {
                    @Override
                    public void onSynthesisBegin(Vocalizer vocalizer) {
                    }

                    @Override
                    public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {
                    }

                    @Override
                    public void onPlayingBegin(Vocalizer vocalizer) {
                        resetRecognizer();
                    }

                    @Override
                    public void onPlayingDone(Vocalizer vocalizer) {
                        circularProgressBar.setVisibility(View.GONE);
                        if (mMainActivity.getBoolean()) {
                            createAndStartRecognizer();
                        }
                    }

                    @Override
                    public void onVocalizerError(Vocalizer vocalizer, Error error) {
                        circularProgressBar.setVisibility(View.GONE);
                    }
                });
                vocalizer.start();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    private void resetVocalizer() {
        if (vocalizer != null) {
            vocalizer.cancel();
            vocalizer = null;
        }
    }
    /**
     * Start record voice
     */
    private void createAndStartRecognizer() {
        if (ContextCompat.checkSelfPermission(mContext, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mMainActivity, new String[]{RECORD_AUDIO}, MainActivity.REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            mMainActivity.setRecognizer(Recognizer.create(DataManager.getInstanse().getPreferencesManager().getLanguageRecognizer(), Recognizer.Model.NOTES, mMainActivity));
            mMainActivity.getRecognizer().start();
        }
    }

    /**
     * Cancel recognizer
     */
    private void resetRecognizer() {
        if (mMainActivity.getRecognizer() != null) {
            mMainActivity.getRecognizer().cancel();
            mMainActivity.setRecognizer(null);
        }
    }
}