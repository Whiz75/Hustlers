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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.hustlers.R;
import com.example.hustlers.models.ApplicationModel;
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

public class applyDialogFragment extends DialogFragment {

    private MaterialToolbar toolbar;
    private TextInputEditText preview_et;
    private MaterialButton btnSubmit;
    private MaterialButton btnUpload;

    private Uri pdf_uri = null;

    //Image request code
    private int PICK_PDF_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    public applyDialogFragment() {
        // Required empty public constructor
    }

    String key;

    public applyDialogFragment(String key) {
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_apply_dialog, container, false);

        //call methods here
        init(view);
        myToolbar(view);
        requestStoragePermission(); //request storage permission
        choosePdf(view);

        return view;
    }

    private void init(ViewGroup view) {
        toolbar = view.findViewById(R.id.tool_bar);
        preview_et = view.findViewById(R.id.document_preview_et);
        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    private void myToolbar(ViewGroup view) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> dismiss());
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    private void choosePdf(ViewGroup view){
        btnUpload = view.findViewById(R.id.btn_upload);

        btnUpload.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_PDF_REQUEST);

        });
    }

    //test this here
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                pdf_uri = data.getData();
                UploadFile();
            } else
                Toast.makeText(getContext(), "NO FILE CHOSEN", Toast.LENGTH_SHORT).show();

        }

    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private void UploadFile() {

        btnSubmit.setOnClickListener(view -> {
            if (pdf_uri != null){

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                ApplicationModel applicationModel = new ApplicationModel();
                applicationModel.setApplicantId(FirebaseAuth.getInstance().getUid());

                if (pdf_uri == null) {
                    preview_et.setError("Choose pdf document...");
                    return;
                }

                FirebaseFirestore
                        .getInstance()
                        .collection("Applications")
                        .add(applicationModel)
                        .addOnSuccessListener(documentReference -> {

                            documentReference.update("job_key",documentReference.getId());

                            FirebaseStorage.
                                    getInstance()
                                    .getReference()
                                    .child("Applications")
                                    .child(documentReference.getId())
                                    .putFile(pdf_uri)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Uploaded Succesfully", Toast.LENGTH_SHORT).show();

                                        taskSnapshot
                                                .getStorage()
                                                .getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        FirebaseFirestore
                                                                .getInstance()
                                                                .collection("Applications")
                                                                .document(documentReference.getId())
                                                                .update("url", uri.toString());
                                                    }
                                                });
                                    }).addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e ->
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show());

            }else {
                Toast.makeText(getContext(),"Url empty!!!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void apply(ViewGroup view){
        //TextInputEditText preview_et = view.findViewById(R.id.document_preview_et);
        btnSubmit = view.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();

            ApplicationModel applicationModel = new ApplicationModel();
            applicationModel.setApplicantId(FirebaseAuth.getInstance().getUid());

            if (pdf_uri == null) {
                preview_et.setError("Choose pdf document...");
                return;
            }

            FirebaseFirestore
                    .getInstance()
                    .collection("Applications")
                    .add(applicationModel)
                    .addOnSuccessListener(documentReference -> {

                        documentReference.update("job_key",documentReference.getId());

                        FirebaseStorage.getInstance()
                                .getReference()
                                .child("Applications")
                                .child(documentReference.getId())
                                .putFile(pdf_uri)
                                .addOnSuccessListener(taskSnapshot ->
                                        taskSnapshot
                                                .getStorage()
                                                .getDownloadUrl()
                                                .addOnSuccessListener(uri ->
                                                        FirebaseFirestore
                                                                .getInstance()
                                                                .collection("Applications")
                                                                .document(documentReference.getId())
                                                                .update("url", uri.toString())));
                    }).addOnCompleteListener(task ->
                    Toast.makeText(getContext(), "Picture uploaded successful...",Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show());

        });
    }

}