package com.nenton.speechya.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nenton.speechya.R;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

public class FragmentDialogResizeMessage extends DialogFragment{
    private String mMessage;
    private Vocalizer vocalizer;

    public void setMessage(String message) {
        mMessage = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater  = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_resize_message,null);
        ((TextView) view.findViewById(R.id.resize_message_txt)).setText(mMessage);
        ((TextView)view.findViewById(R.id.resize_message_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogResizeMessage.this.dismiss();
            }
        });
        final CircularProgressBar circularProgressBar = (CircularProgressBar)view.findViewById(R.id.resize_message_progressbar);
        ImageView imageView = (ImageView)view.findViewById(R.id.resize_message_say);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularProgressBar.setVisibility(View.VISIBLE);
                resetVocalizer();
                vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, mMessage, true, Vocalizer.Voice.ERMIL);
                vocalizer.setListener(new VocalizerListener() {
                    @Override
                    public void onSynthesisBegin(Vocalizer vocalizer) {

                    }

                    @Override
                    public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {

                    }

                    @Override
                    public void onPlayingBegin(Vocalizer vocalizer) {

                    }

                    @Override
                    public void onPlayingDone(Vocalizer vocalizer) {
                        circularProgressBar.setVisibility(View.GONE);
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
}
