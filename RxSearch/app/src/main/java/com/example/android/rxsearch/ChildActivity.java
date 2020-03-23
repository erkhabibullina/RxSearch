package com.example.android.rxsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChildActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        fab = findViewById(R.id.floating_action_button);
        fab.bringToFront();
        fab.setOnClickListener(v -> {
            Context context = ChildActivity.this;
            Class destinationActivity = MainActivity.class;
            Intent intent = new Intent(context, destinationActivity);
            startActivity(intent);
        });
    }

}
