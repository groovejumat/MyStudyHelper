package org.techtown.samplerelativelayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AppusagemainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appusagemain);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, AppusageFragment.newInstance())
                    .commit();
        }
    }
}
