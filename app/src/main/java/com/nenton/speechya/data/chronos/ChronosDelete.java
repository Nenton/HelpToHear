package com.nenton.speechya.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.utils.ConstantManager;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

public class ChronosDelete extends ChronosOperation<Void>{

    DataManager mDataManage = DataManager.getInstanse();
    long mLong;
    int mInt;

    public ChronosDelete(long aLong, int anInt){
        mLong = aLong;
        mInt = anInt;
    }

    public ChronosDelete(int anInt){
        mInt = anInt;
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
        switch (mInt){
            case ConstantManager.INT_CHRONOS_DELETE_DIALOG:
                mDataManage.deleteDialog(mLong);
                break;
            case ConstantManager.INT_CHRONOS_DELETE_PHRASE:
                mDataManage.deleteStandartPhrase(mLong);
                break;
            case ConstantManager.INT_CHRONOS_DELETE_MESSAGE:
                mDataManage.deleteMessage(mLong);
                break;
            case ConstantManager.INT_CHRONOS_DELETE_MESSAGES:
                mDataManage.deleteMessages(mLong);
                break;
            case ConstantManager.INT_CHRONOS_DELETE_DIALOGS:
                mDataManage.deleteDialogs();
                break;
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
        return ChronosDelete.Result.class;
    }

    public final static class Result extends ChronosOperationResult<Void> {

    }
}
