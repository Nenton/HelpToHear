package com.nenton.speechya.ui.activities;

import android.graphics.Canvas;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.nenton.speechya.ui.adapters.DialogsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    DataManager mDataManager;
    List<Dialog> mDialogs;
    DialogsAdapter dialogsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        mDataManager = DataManager.getInstanse();
        mToolbar = (Toolbar)findViewById(R.id.dialogs_toolbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.dialogs_recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupToolbar();
        setupRecycle();
        setupSwipe();
    }

    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Dialog dialog = mDialogs.get(viewHolder.getAdapterPosition());
                mDataManager.deleteDialog(dialog.getId());
                int position = viewHolder.getAdapterPosition();
                mDialogs.remove(position);
                dialogsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecycle() {
        mDialogs = mDataManager.getDialogs();
        dialogsAdapter = new DialogsAdapter(mDialogs);
        mRecyclerView.setAdapter(dialogsAdapter);
    }
}
