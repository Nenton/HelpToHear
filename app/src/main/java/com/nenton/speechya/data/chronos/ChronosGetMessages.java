package com.nenton.speechya.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Messages;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

public class ChronosGetMessages extends ChronosOperation<List<Messages>> {

    @Nullable
    @Override
    public List<Messages> run() {
        return DataManager.getInstanse().getMessages();
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<Messages>>> getResultClass() {
        return ChronosGetMessages.Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<Messages>> {

    }
}
