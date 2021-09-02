package com.example.hustlers.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.hustlers.R;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ViewJobDialogFragment extends DialogFragment {

    private MaterialToolbar toolbar;
    private FirebaseAnalytics analytics;
    private MaterialTextView role_tv ;
    private MaterialTextView description_tv;
    private MaterialTextView experience_tv;
    private MaterialTextView location_tv;
    private MaterialTextView qualification_tv;
    private MaterialTextView salary_tv;
    private MaterialTextView date_tv;

    private MaterialButton btnApply;

    public ViewJobDialogFragment() {
        // Required empty public constructor
    }

    String key;

    public ViewJobDialogFragment(String key) {
        this.key = key;
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
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    @SuppressLint("CutPasteId")
    private void init(ViewGroup view)
    {
        toolbar = view.findViewById(R.id.tool_bar);

        role_tv = view.findViewById(R.id.job_role_tv);
        description_tv = view.findViewById(R.id.job_role_tv);
        experience_tv = view.findViewById(R.id.job_role_tv);
        location_tv = view.findViewById(R.id.job_role_tv);
        qualification_tv = view.findViewById(R.id.job_role_tv);
        salary_tv = view.findViewById(R.id.job_role_tv);
        date_tv = view.findViewById(R.id.job_role_tv);

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
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                        /*String role = documentSnapshot.get("job_role").toString();
                        String description = documentSnapshot.get("job_description").toString();
                        String experience = documentSnapshot.get("job_experiences").toString();
                        String location = documentSnapshot.get("job_location").toString();
                        String qualification = documentSnapshot.get("job_qualification").toString();
                        String salary = documentSnapshot.get("job_salary").toString();
                        String date = documentSnapshot.get("job_date").toString();*/

                        role_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_role")).toString());
                        description_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_description")).toString());
                        experience_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_experiences")).toString());
                        location_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_location")).toString());
                        qualification_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_qualification")).toString());
                        salary_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_salary")).toString());
                        date_tv.setText(Objects.requireNonNull(documentSnapshot.get("job_date")).toString());

                    }else {
                        Toast.makeText(getContext(),"There's no such record!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e ->
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show());
    }

    private void applyJob(ViewGroup view){
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),key,Toast.LENGTH_LONG).show();

                applyDialogFragment dlg = new applyDialogFragment(key);
                dlg.show(getChildFragmentManager().beginTransaction(),"GO TO APPLY JOB");
            }
        });
    }

}