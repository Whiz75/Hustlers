package com.example.hustlers.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hustlers.R;
import com.example.hustlers.adapters.JobsAdapter;
import com.example.hustlers.dialogs.ViewJobDialogFragment;
import com.example.hustlers.models.JobModel;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements JobsAdapter.ClickListener {

    private RecyclerView rv;
    private TextInputEditText et_search;

    ViewGroup dashboardViewGroup;

    private List<JobModel> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dashboardViewGroup =(ViewGroup)inflater.inflate(R.layout.activity_dashboard_fragment,container,false);
        initBinding(dashboardViewGroup);
        getAllJobs(dashboardViewGroup);

        return dashboardViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initBinding(ViewGroup view) {

        et_search =view.findViewById(R.id.et_search);
    }

    private void getAllJobs(ViewGroup view)
    {
        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.home_jobs_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final JobsAdapter adapter = new JobsAdapter( context,list,this);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore
                .getInstance()
                .collection("Jobs")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        for (DocumentChange dc: value.getDocumentChanges()){
                            switch (dc.getType()){
                                case ADDED:
                                    list.add(dc.getDocument().toObject(JobModel.class));
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    list.add(dc.getNewIndex(), dc.getDocument().toObject(JobModel.class));
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
    public void viewJob(String pos, String title, String date) {
        ViewJobDialogFragment fragment = new ViewJobDialogFragment(pos, title, date);
        fragment.show(getChildFragmentManager().beginTransaction(),"VIEW FULL JOB DETAILS");
    }
}
