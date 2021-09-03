package com.example.hustlers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hustlers.R;
import com.example.hustlers.dialogs.AddJobDialogFragment;
import com.example.hustlers.fragments.DashboardFragment;
import com.example.hustlers.fragments.JobsFragment;
import com.example.hustlers.fragments.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar tool_bar;
    private ChipNavigationBar navigationBar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call methods
        init();
        navigationDrawer();
    }

    private void init() {
        tool_bar = findViewById(R.id.tool_bar);
        navigationBar = findViewById(R.id.bottom_nav);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        tool_bar.setNavigationIcon(R.drawable.ic_menu);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_host,new DashboardFragment()).commit();
        navigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
        tool_bar.setTitle("DASHBOARD");
        //call method
        listenToEvent();
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

    private void navigationDrawer() {
        tool_bar.setNavigationIcon(R.drawable.ic_menu);
        tool_bar.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.nav_home:
                    drawerLayout.closeDrawer(GravityCompat.END);
                    Toast.makeText(getApplicationContext(), "you clicked home", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_profile:
                    //call profile frag
                    Toast.makeText(getApplicationContext(), "you clicked profile", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_rate:
                    Toast.makeText(getApplicationContext(), "you clicked rate", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_share:
                    Toast.makeText(getApplicationContext(), "you clicked share", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_logout:
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setTitle("LOGOUT");
                    builder.setMessage("Are you sure you want to logout ?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginSignupActivity.class));
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    builder.show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });
    }
}