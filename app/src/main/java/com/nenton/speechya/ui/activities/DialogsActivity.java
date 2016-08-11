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
import com.nenton.speechya.data.chronos.ChronosDelete;

import com.nenton.speechya.data.chronos.ChronosGetDialogs;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.ui.adapters.DialogsAdapter;
import com.nenton.speechya.utils.ConstantManager;
import com.redmadrobot.chronos.ChronosConnector;

import java.util.List;

public class DialogsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private List<Dialog> mDialogs;
    private DialogsAdapter dialogsAdapter;
    private ChronosConnector mChronosConnector;

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        mChronosConnector.onResume();
        super.onResume();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mChronosConnector.onPause();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        mToolbar = (Toolbar)findViewById(R.id.dialogs_toolbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.dialogs_recycle);
        mDataManager = DataManager.getInstanse();
        mChronosConnector = mDataManager.getChronosConnector();
        mChronosConnector.onCreate(this,savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupToolbar();
        setupRecycle();
        setupSwipe();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Setup recycle view
     */
    private void setupRecycle() {
        mChronosConnector.runOperation(new ChronosDelete(ConstantManager.INT_CHRONOS_DELETE_DIALOGS),true);
        mChronosConnector.runOperation(new ChronosGetDialogs(),true);
//        mDialogs = mDataManager.getDialogs();
//        dialogsAdapter = new DialogsAdapter(mDialogs);
//        mRecyclerView.setAdapter(dialogsAdapter);
    }

    public void onOperationFinished(final ChronosGetDialogs.Result result) {
        if (result.isSuccessful() && result.getOutput() != null) {
            mDialogs = result.getOutput();
            dialogsAdapter = new DialogsAdapter(mDialogs);
            mRecyclerView.setAdapter(dialogsAdapter);
        }
    }

    /**
     * Setup swipe for recycle
     */
    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Dialog dialog = mDialogs.get(viewHolder.getAdapterPosition());
                mChronosConnector.runOperation(new ChronosDelete(dialog.getId(), ConstantManager.INT_CHRONOS_DELETE_DIALOG),true);
                mChronosConnector.runOperation(new ChronosDelete(dialog.getDateCreateDialog(), ConstantManager.INT_CHRONOS_DELETE_MESSAGES),true);
//                mDataManager.deleteDialog(dialog.getId());
//                mDataManager.deleteMessages(dialog.getDateCreateDialog());
                mDialogs.remove(viewHolder.getAdapterPosition());
                dialogsAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

}