package com.example.hustlers.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hustlers.R;
import com.example.hustlers.adapters.ApplicationsAdapter;
import com.example.hustlers.adapters.JobsAdapter;
import com.example.hustlers.models.ApplicationModel;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class JobsFragment extends Fragment implements JobsAdapter.ClickListener, ApplicationsAdapter.ClickListener {

    ViewGroup jobViewGroup;

    private List<ApplicationModel> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        jobViewGroup =(ViewGroup) inflater.inflate(R.layout.activity_jobs_fragment,container,false);
        getJobs(jobViewGroup);

        return jobViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getJobs(ViewGroup view) {

        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.jobs_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final ApplicationsAdapter adapter = new ApplicationsAdapter( context,list,this);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore
                .getInstance()
                .collection("Applications")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        for (DocumentChange dc: value.getDocumentChanges()){
                            switch (dc.getType()){
                                case ADDED:
                                    list.add(dc.getDocument().toObject(ApplicationModel.class));
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    list.add(dc.getNewIndex(), dc.getDocument().toObject(ApplicationModel.class));
                                    adapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    //to remove item
                                    list.remove(dc.getOldIndex());
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                });
    }


    @Override
    public void DeleteJobClick(int pos) {

    }

    @Override
    public void viewJob(int position,String pos, String title, String date) {

    }
}
