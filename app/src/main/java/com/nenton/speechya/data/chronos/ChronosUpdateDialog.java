package com.nenton.speechya.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nenton.speechya.data.manager.DataManager;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.Date;

public class ChronosUpdateDialog extends ChronosOperation<Void>{

    private String mString;
    private Date mDate;

    public ChronosUpdateDialog(String lastMessage, Date lastUpdate) {
        mString = lastMessage;
        mDate = lastUpdate;
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
        DataManager.getInstanse().updateDialog(mString,mDate);
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
        return ChronosUpdateDialog.Result.class;
    }
    public final static class Result extends ChronosOperationResult<Void> {

    }
}
