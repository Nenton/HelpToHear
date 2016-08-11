package com.nenton.speechya.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nenton.speechya.data.chronos.ChronosDelete;
import com.nenton.speechya.data.chronos.ChronosUpdateDialog;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.ui.adapters.MessageAdapter;
import com.nenton.speechya.utils.ConstantManager;

import java.util.Date;

public class FragmentDialogDeleteMessage extends DialogFragment {
    private Messages mMessages;
    private MessageAdapter mMessageAdapter;
    private int mPosition;

    public void setMessage(Messages messages, MessageAdapter messageAdapter, int position) {
        mMessages = messages;
        mMessageAdapter = messageAdapter;
        mPosition = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(ConstantManager.STRING_DELETE_MESSAGE);
        builder.setPositiveButton(ConstantManager.STRING_DELETE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataManager.getInstanse().getChronosConnector().runOperation(new ChronosDelete(mMessages.getDateMessage(), ConstantManager.INT_CHRONOS_DELETE_MESSAGE), true);
                if (mMessageAdapter.getItemCount()-1 == 0){
                    DataManager.getInstanse().getChronosConnector().runOperation(new ChronosUpdateDialog("", new Date()), true);
                } else {
                    if (mPosition == mMessageAdapter.getItemCount() - 1) {
                        Messages messages = mMessageAdapter.getMessage(mPosition - 1);
                        DataManager.getInstanse().getChronosConnector().runOperation(new ChronosUpdateDialog(messages.getMessage(), new Date(messages.getDateMessage())), true);
                    }
                }
                mMessageAdapter.deleteMessage(mPosition);
                mMessageAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        })
                .setNegativeButton(ConstantManager.STRING_CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
