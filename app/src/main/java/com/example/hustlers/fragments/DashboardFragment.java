package com.example.hustlers.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hustlers.R;

public class DashboardFragment extends Fragment {

    RecyclerView rv;
    ViewGroup dashboardViewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dashboardViewGroup =(ViewGroup)inflater.inflate(R.layout.activity_dashboard_fragment,container,false);
        //initBinding(dashboardViewGroup);

        return dashboardViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initBinding(ViewGroup view) {
        /*rv = view.findViewById(R.id.db_requests_rv);

        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(DashboardFragment.this.getActivity()));*/
    }
}
