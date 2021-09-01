package com.example.hustlers.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.hustlers.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Objects;

public class applyDialogFragment extends DialogFragment {

    private MaterialToolbar toolbar;
    private FirebaseAnalytics analytics;

    public applyDialogFragment() {
        // Required empty public constructor
    }

    String title;

    public applyDialogFragment(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics = FirebaseAnalytics.getInstance(getContext());
        analytics.setUserProperty("Favourite","value");
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_apply_dialog, container, false);

        //call methods here
        init(view);
        myToolbar(view);

        return view;
    }

    private void init(ViewGroup view)
    {
        toolbar = view.findViewById(R.id.tool_bar);
    }

    private void myToolbar(ViewGroup view) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> dismiss());
    }
}