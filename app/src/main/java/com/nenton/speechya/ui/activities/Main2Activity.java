package com.nenton.speechya.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.nenton.speechya.R;
import com.nenton.speechya.data.chronos.ChronosCreate;
import com.nenton.speechya.data.chronos.ChronosDelete;
import com.nenton.speechya.data.chronos.ChronosGetPhrase;
import com.nenton.speechya.data.chronos.ChronosUpdatePhrase;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.nenton.speechya.ui.adapters.StandartPhraseAdapter;
import com.nenton.speechya.utils.ConstantManager;
import com.redmadrobot.chronos.ChronosConnector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private Button mButton;
    private Toolbar mToolbar;

    private DataManager mDataManager;
    private List<StandartPhrase> standartPhrases;
    private StandartPhraseAdapter adapter;
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
        setContentView(R.layout.activity_main2);

        mRecyclerView = (RecyclerView)findViewById(R.id.standart_phrase);
        mButton = (Button)findViewById(R.id.new_dialog);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mDataManager = DataManager.getInstanse();
        mChronosConnector = mDataManager.getChronosConnector();
        mChronosConnector.onCreate(this,savedInstanceState);
        setupToolbar();
        setupNavbar();
        setupStandartPhrase();
        setupSwipe();
        setupButton();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavbar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupStandartPhrase() {
//        standartPhrases = mDataManager.getStandartPhrase();
        mChronosConnector.runOperation(new ChronosGetPhrase(),true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new StandartPhraseAdapter(standartPhrases);
//        mRecyclerView.setAdapter(adapter);
    }

    public void onOperationFinished(final ChronosGetPhrase.Result result) {
        if (result.isSuccessful() && result.getOutput() != null) {
            standartPhrases = result.getOutput();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new StandartPhraseAdapter(standartPhrases);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();
//                List<Long> sortPositions = new ArrayList<>();
//                if (toPosition > fromPosition){
//                    for (int i = 0; i <= toPosition-fromPosition; i++) {
//                        sortPositions.add(standartPhrases.get(fromPosition+i).getSortPosition());
//                    }
//                } else {
//                    for (int i = 0; i <= fromPosition - toPosition; i++) {
//                        sortPositions.add(standartPhrases.get(toPosition+i).getSortPosition());
//                    }
//                }
//                StandartPhrase sPto = standartPhrases.get(toPosition);
                StandartPhrase sPfrom = standartPhrases.remove(fromPosition);

                standartPhrases.add(toPosition, sPfrom);
                List<Long> longs = new ArrayList<>();
                if (toPosition > fromPosition){
                    for (int i = fromPosition; i < toPosition; i++) {
                        longs.add(standartPhrases.get(i).getSortPosition());
                    }
                }
//                mChronosConnector.runOperation(new ChronosUpdatePhrase(standartPhrases.get(toPosition).getMId(),standartPhrases.get(fromPosition).getSortPosition()),true);
//                mChronosConnector.runOperation(new ChronosUpdatePhrase(standartPhrases.get(fromPosition).getMId(), standartPhrases.get(toPosition).getSortPosition()),true);
//                long sortPosition = sPfrom.getSortPosition();
//                long sort;
                if (toPosition > fromPosition) {
//                    mDataManager.updatePhrase(prev.getMId(), toPosition);
                    mChronosConnector.runOperation(new ChronosUpdatePhrase(standartPhrases.get(fromPosition).getMId(), sPfrom.getSortPosition()),true);
//                    sortPosition = standartPhrases.get(fromPosition).getSortPosition();
                    for (int i = 0; i <= toPosition-fromPosition - 1; i++) {
//                        mDataManager.updatePhrase(standartPhrases.get(i).getMId(), i);
                        mChronosConnector.runOperation(new ChronosUpdatePhrase(standartPhrases.get(i + fromPosition + 1).getMId(), longs.get(i)),true);
//                        sortPosition = standartPhrases.get(i).getSortPosition();
                    }
                } else {
//                    mDataManager.updatePhrase(prev.getMId(), toPosition);
                    mChronosConnector.runOperation(new ChronosUpdatePhrase(standartPhrases.get(fromPosition).getMId(), sPfrom.getSortPosition()),true);
                    for (int i = toPosition; i < fromPosition; i++) {
//                        mDataManager.updatePhrase(standartPhrases.get(i).getMId(), i);
                        mChronosConnector.runOperation(new ChronosUpdatePhrase(standartPhrases.get(i).getMId(), standartPhrases.get(i+1).getSortPosition()),true);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                StandartPhrase standartPhrase = standartPhrases.get(viewHolder.getAdapterPosition());
//                mDataManager.deleteStandartPhrase(standartPhrase.getMId());
                mChronosConnector.runOperation(new ChronosDelete(standartPhrase.getMId(),ConstantManager.INT_CHRONOS_DELETE_PHRASE),true);
                standartPhrases.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setupButton() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                mDataManager.getPreferencesManager().setCreateDateDialog(date.getTime());
//                mDataManager.createNewDialog(new Dialog(date.getTime()));
                mChronosConnector.runOperation(new ChronosCreate(new Dialog(date.getTime())),true);
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected android.app.Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.ADD_PHRASE:
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                final EditText inflate = (EditText) layoutInflater.inflate(R.layout.dialog_add_phrase, null);
                builder.setTitle(ConstantManager.STRING_ADD_PHRASE);
                builder.setView(inflate)
                        .setPositiveButton(ConstantManager.STRING_ADD, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!inflate.getText().toString().isEmpty()) {
                                    StandartPhrase standartPhrase = new StandartPhrase(inflate.getText().toString(), (new Date()).getTime());
                                    standartPhrases.add(ConstantManager.INT_NULL, standartPhrase);
//                                    mDataManager.createNewPhrase(standartPhrase);
                                    mChronosConnector.runOperation(new ChronosCreate(standartPhrase),true);
                                    inflate.setText(ConstantManager.STRING_EMPTY);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton(ConstantManager.STRING_CANCEL, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inflate.setText(ConstantManager.STRING_EMPTY);
                                dialog.cancel();
                            }
                        });
                return builder.create();
            default:
                return null;
        }
    }

    /**
     * Action on back pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Make menu toolbar
     * @param menu current menu
     * @return create or not create
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    /**
     * Action on click item
     * @param item current item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showDialog(ConstantManager.ADD_PHRASE);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Action on click nav item
     * @param item current item
     * @return create true
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.history_dialogs) {
            Intent intent = new Intent(Main2Activity.this, DialogsActivity.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(Main2Activity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.exit) {
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}