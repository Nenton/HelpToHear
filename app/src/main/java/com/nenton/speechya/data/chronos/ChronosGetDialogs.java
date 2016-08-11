package com.nenton.speechya.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

public class ChronosGetDialogs extends ChronosOperation<List<Dialog>> {

    @Nullable
    @Override
    public List<Dialog> run() {
        DataManager mDataManager = DataManager.getInstanse();
        return mDataManager.getDialogs();
    }

    /**
     * This method returns a subclass of OperationResult class, related to the particular Operation
     * subclass, so that Chronos clients can distinguish results from different operations.
     *
     * @return OperationResult subclass, that will be created after the operation is complete.
     */
    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<Dialog>>> getResultClass() {
        return ChronosGetDialogs.Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<Dialog>> {

    }
}