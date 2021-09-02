package com.example.hustlers.dialogs;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.hustlers.R;
import com.example.hustlers.activities.LoginSignupActivity;
import com.example.hustlers.models.ApplicationModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ResetPasswordDialogFragment extends DialogFragment {

    private MaterialToolbar toolbar;
    private TextInputEditText et_email;

    private MaterialButton btnSubmit;

    public ResetPasswordDialogFragment() {
        // Required empty public constructor
    }

    String key;

    public ResetPasswordDialogFragment(String key) {
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
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_recover_password_dialog, container, false);

        //call methods here
        init(view);
        myToolbar(view);
        resetPasswordBtn(view);

        return view;
    }

    private void init(ViewGroup view) {
        toolbar = view.findViewById(R.id.tool_bar);
        et_email = view.findViewById(R.id.reset_password_et);
        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    private void myToolbar(ViewGroup view) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> dismiss());
    }

    public void resetPasswordBtn(View view) {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String resetMail = Objects.requireNonNull(et_email.getText()).toString();
                    if (resetMail.isEmpty()){
                        et_email.setError("Required field!");
                        return;
                    }

                    FirebaseAuth.
                            getInstance()
                            .sendPasswordResetEmail(resetMail).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Email link sent to " + resetMail + ", please check your mails.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginSignupActivity.class));
                        } else {
                            Toast.makeText(getContext(), "No user with email " + resetMail, Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to send link to " + resetMail + ", " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}