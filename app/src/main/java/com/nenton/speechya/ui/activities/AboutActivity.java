package com.nenton.speechya.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.speechya.R;
import com.nenton.speechya.utils.ConstantManager;

public class AboutActivity extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mTextView = (TextView)findViewById(R.id.txt_about_yandex);
        mImageView = (ImageView)findViewById(R.id.img_about_yandex);
        mToolbar = (Toolbar)findViewById(R.id.about_toolbar);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goInSiteSdk();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goInSiteSdk();
            }
        });
        setupToolbar();
    }

    /**
     * Provide user on sine with sdk speech
     */
    private void goInSiteSdk() {
        Uri uri = Uri.parse(ConstantManager.STRING_SITE_SDK);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
}
