package com.nenton.speechya.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

public class ChronosCreate extends ChronosOperation<Void> {

    Dialog mDialog = null;
    StandartPhrase mStandartPhrase = null;
    Messages mMessages = null;

    private ChronosCreate(){}

    public ChronosCreate(Messages messages) {
        mMessages = messages;
    }

    public ChronosCreate(StandartPhrase standartPhrase) {
        mStandartPhrase = standartPhrase;
    }

    public ChronosCreate(Dialog dialog) {
        mDialog = dialog;
    }

    /**
     * The method for performing business-logic related work. Can contain time-consuming calls, but
     * should not perform any interaction with the UI, as it will be launched not in the Main
     * Thread.<br>
     * <p>
     * All exceptions thrown in this method will be encapsulated in OperationResult object, so it
     * will not cause app crash.
     *
     * @return the result of the operation.
     */
    @Nullable
    @Override
    public Void run() {
        DataManager instanse = DataManager.getInstanse();
        if (mDialog != null){
            instanse.createNewDialog(mDialog);
        }
        if (mStandartPhrase != null){
            instanse.createNewPhrase(mStandartPhrase);
        }
        if (mMessages != null) {
            instanse.createNewMessage(mMessages);
        }
        return null;
    }

    /**
     * This method returns a subclass of OperationResult class, related to the particular Operation
     * subclass, so that Chronos clients can distinguish results from different operations.
     *
     * @return OperationResult subclass, that will be created after the operation is complete.
     */
    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Void>> getResultClass() {
        return ChronosCreate.Result.class;
    }

    public final static class Result extends ChronosOperationResult<Void> {

    }
}