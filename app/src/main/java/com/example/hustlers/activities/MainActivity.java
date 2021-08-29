package com.example.hustlers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.hustlers.R;
import com.example.hustlers.dialogs.AddJobDialogFragment;
import com.example.hustlers.fragments.DashboardFragment;
import com.example.hustlers.fragments.JobsFragment;
import com.example.hustlers.fragments.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar tool_bar;
    private ChipNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call methods
        init();
        my_toolbar();
    }

    private void init() {
        tool_bar = findViewById(R.id.tool_bar);
        navigationBar = findViewById(R.id.bottom_nav);

        tool_bar.setNavigationIcon(R.drawable.ic_menu);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_host,new DashboardFragment()).commit();
        navigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
        tool_bar.setTitle("DASHBOARD");
        //call method
        listenToEvent();
    }

    private void my_toolbar()
    {
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddJobDialogFragment fragment = new AddJobDialogFragment();
                fragment.show(getSupportFragmentManager().beginTransaction(),"ADD JOB");
            }
        });
    }

    private void listenToEvent()
    {
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i){
                    case R.id.bottom_nav_dashboard:
                        fragment = new DashboardFragment();
                        tool_bar.setTitle("DASHBOARD");
                        break;
                    case R.id.bottom_nav_jobs:
                        fragment = new JobsFragment();
                        tool_bar.setTitle("JOBS");
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        tool_bar.setTitle("PROFILE");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_host,fragment).commit();
            }
        });
    }
}