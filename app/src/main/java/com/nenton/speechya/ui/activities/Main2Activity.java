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
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.nenton.speechya.ui.adapters.StandartPhraseAdapter;
import com.nenton.speechya.utils.ConstantManager;

import java.util.Date;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DataManager mDataManager;
    private RecyclerView mRecyclerView;
    private List<StandartPhrase> standartPhrases;
    private StandartPhraseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDataManager = DataManager.getInstanse();
        setupToolbar();
        setupNavbar();
        setupStandartPhrase();
        setupSwipe();
        setupButton();
    }


    @Override
    protected android.app.Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.ADD_PHRASE:
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                final EditText inflate = (EditText) layoutInflater.inflate(R.layout.dialog_add_phrase, null);
                builder.setTitle("Добавить фразу");
                builder.setView(inflate)
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!inflate.getText().toString().isEmpty()) {
                                    StandartPhrase standartPhrase = new StandartPhrase(inflate.getText().toString(), (new Date()).getTime());
                                    standartPhrases.add(0, standartPhrase);
                                    mDataManager.createNewPhrase(standartPhrase);
                                    inflate.setText("");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inflate.setText("");
                                dialog.cancel();
                            }
                        });
                return builder.create();
            default:
                return null;
        }
    }

    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();
                StandartPhrase prev = standartPhrases.remove(fromPosition);
                standartPhrases.add(toPosition, prev);
                if (toPosition > fromPosition) {
                    mDataManager.updatePhrase(prev.getMId(), toPosition);
                    for (int i = (int) fromPosition; i < toPosition; i++) {
                        mDataManager.updatePhrase(standartPhrases.get(i).getMId(), i);
                    }
                } else {
                    mDataManager.updatePhrase(prev.getMId(), toPosition);
                    for (int i = (int) toPosition + 1; i < fromPosition + 1; i++) {
                        mDataManager.updatePhrase(standartPhrases.get(i).getMId(), i);
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
                mDataManager.deleteStandartPhrase(standartPhrase.getMId());
                int position = viewHolder.getAdapterPosition();
                standartPhrases.remove(position);
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

    private void setupStandartPhrase() {
        standartPhrases = mDataManager.getStandartPhrase();
        mRecyclerView = (RecyclerView) findViewById(R.id.standart_phrase);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StandartPhraseAdapter(standartPhrases);
        mRecyclerView.setAdapter(adapter);
    }

    private void setupNavbar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupButton() {
        ((Button) findViewById(R.id.new_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                mDataManager.getPreferencesManager().setCreateDateDialog(date.getTime());
                mDataManager.createNewDialog(new Dialog(date.getTime()));
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            showDialog(ConstantManager.ADD_PHRASE);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.history_dialogs) {
            Intent intent = new Intent(Main2Activity.this, DialogsActivity.class);
            startActivity(intent);
        } else if (id == R.id.settings) {

        } else if (id == R.id.exit) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
