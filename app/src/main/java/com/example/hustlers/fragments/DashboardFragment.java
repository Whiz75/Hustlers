package com.example.hustlers.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private EditText et_search;
    private JobsAdapter adapter;

    ViewGroup dashboardViewGroup;

    private List<JobModel> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dashboardViewGroup =(ViewGroup)inflater.inflate(R.layout.activity_dashboard_fragment,container,false);
        initBinding(dashboardViewGroup);
        getAllJobs(dashboardViewGroup);
        search();

        return dashboardViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initBinding(ViewGroup view) {
        et_search =view.findViewById(R.id.et_searchView);
    }

    private void search(){
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<JobModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (JobModel s : list) {
            //if the existing elements contains the search input
            if (s.getJob_title().contains(text.toLowerCase()) ||
                    s.getJob_qualification().contains(text.toUpperCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }

    private void getAllJobs(ViewGroup view)
    {
        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.home_jobs_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new JobsAdapter( context,list,this);
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
    public void viewJob(int position, String pos, String title, String date) {
        ViewJobDialogFragment fragment = new ViewJobDialogFragment(pos, title, date);
        fragment.show(getChildFragmentManager().beginTransaction(),"VIEW FULL JOB DETAILS");
    }
}
