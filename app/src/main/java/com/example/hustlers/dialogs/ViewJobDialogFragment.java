package com.example.hustlers.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hustlers.R;

import com.example.hustlers.models.JobModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewJobDialogFragment extends DialogFragment {

    private MaterialToolbar toolbar;

    private MaterialTextView role_tv ;
    private MaterialTextView description_tv;
    private MaterialTextView experience_tv;
    private MaterialTextView location_tv;
    private MaterialTextView qualification_tv;
    private MaterialTextView salary_tv;
    private MaterialTextView date_tv;

    private MaterialButton btnApply;

    List<JobModel> mList = new ArrayList<>();

    public ViewJobDialogFragment() {
        // Required empty public constructor
    }

    String key;
    String title;
    String date;
    int pos;

    public ViewJobDialogFragment(String key, String title, String date) {
        this.key = key;
        this.title = title;
        this.date = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(getDialog())
                .getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_view_job_dialog, container, false);

        //call methods here
        init(view);
        myToolbar(view);
        getJobDetails(view);
        applyJob(view);

        return view;
    }

    private void init(ViewGroup view) {
        toolbar = view.findViewById(R.id.tool_bar);

        role_tv = view.findViewById(R.id.job_role_tv);
        description_tv = view.findViewById(R.id.job_description_tv);
        experience_tv = view.findViewById(R.id.job_experience_tv);
        location_tv = view.findViewById(R.id.job_location_tv);
        qualification_tv = view.findViewById(R.id.job_qualification_tv);
        salary_tv = view.findViewById(R.id.job_salary_tv);
        date_tv = view.findViewById(R.id.job_closing_date_tv);

        btnApply = view.findViewById(R.id.btn_apply);
    }

    private void myToolbar(ViewGroup view) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> dismiss());
    }

    private void getJobDetails(ViewGroup view) {

        Context context = view.getContext();
        Toast.makeText(getContext(),key,Toast.LENGTH_LONG).show();

        FirebaseFirestore
                .getInstance()
                .collection("Jobs")
                .document(key)
                .addSnapshotListener((value, error) -> {

                    if (value != null){
                        description_tv.setText(value.get("job_description").toString());
                        role_tv.setText(value.get("job_role").toString());
                        experience_tv.setText(value.get("job_experiences").toString());
                        location_tv.setText(value.get("job_location").toString());
                        qualification_tv.setText(value.get("job_qualification").toString());
                        salary_tv.setText("R"+value.get("job_salary").toString());
                        date_tv.setText(value.get("job_date").toString());
                    }
                });
    }

    private void applyJob(ViewGroup view){
        btnApply.setOnClickListener(view1 -> {
            Toast.makeText(getContext(),key,Toast.LENGTH_LONG).show();

            //display apply dialog
            applyDialogFragment dlg = new applyDialogFragment(key,title,date);
            dlg.show(getChildFragmentManager().beginTransaction(),"GO TO APPLY JOB");
        });
    }

}