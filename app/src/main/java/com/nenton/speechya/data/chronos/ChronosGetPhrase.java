package com.nenton.speechya.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

public class ChronosGetPhrase extends ChronosOperation<List<StandartPhrase>> {

    @Nullable
    @Override
    public List<StandartPhrase> run() {
        return DataManager.getInstanse().getStandartPhrase();
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<StandartPhrase>>> getResultClass() {
        return ChronosGetPhrase.Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<StandartPhrase>> {

    }
}