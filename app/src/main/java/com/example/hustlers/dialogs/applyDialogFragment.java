package com.example.hustlers.dialogs;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.hustlers.R;
import com.example.hustlers.models.ApplicationModel;
import com.example.hustlers.models.JobModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class applyDialogFragment extends DialogFragment {

    private MaterialToolbar toolbar;
    private TextInputEditText preview_et;
    private MaterialTextView url_text;

    private MaterialButton btnSubmit;
    private MaterialButton btnUpload;

    private ImageView pdfImage;

    private Uri pdf_uri = null;

    //Image request code
    private int PICK_PDF_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    public applyDialogFragment() {
        // Required empty public constructor
    }

    String key;
    String title;
    String date;

    public applyDialogFragment(String key, String title, String date) {
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_apply_dialog, container, false);

        //call methods here
        init(view);
        myToolbar(view);
        requestStoragePermission(); //request storage permission
        choosePdf(view);

        Toast.makeText(getContext(),key,Toast.LENGTH_LONG).show();

        return view;
    }

    private void init(ViewGroup view) {
        toolbar = view.findViewById(R.id.tool_bar);
        //preview_et = view.findViewById(R.id.document_preview_et);
        url_text = view.findViewById(R.id.url_text);

        pdfImage = view.findViewById(R.id.pdf_icon);
        pdfImage.setVisibility(View.GONE);

        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    private void myToolbar(ViewGroup view) {
        Context context = view.getContext();

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

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        // your operation....
                        pdf_uri = data.getData();

                        //set the image to be visible
                        pdfImage.setVisibility(View.VISIBLE);
                        //call method to upload the Cv
                        UploadFile();

                        String path = pdf_uri.getPath();
                        url_text.setText(path);
                    }
                }
            });

    private void choosePdf(ViewGroup view){
        btnUpload = view.findViewById(R.id.btn_upload);

        btnUpload.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_PDF_REQUEST);
            launchSomeActivity.launch(intent);
        });
    }

    //test this here
    /*@Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_PDF_REQUEST) {

            pdf_uri = data.getData();
            UploadFile();

            String path = data.getData().getPath();
            url_text.setText(path);
            *//*url_text.setText(data.getData().toString() + "===");
            String scheme = pdf_uri.getScheme();
            if (scheme.equals("file")) {
                url_text.setText(pdf_uri.getLastPathSegment() + "File selected!");
            } else if (scheme.equals("content")) {
                String[] proj = {MediaStore.Images.Media.TITLE};
                Cursor cursor = getContext().getContentResolver().query(pdf_uri, proj, null, null, null);
                if (cursor != null && cursor.getCount() != 0) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                    cursor.moveToFirst();
                    url_text.setText(cursor.getString(columnIndex) + "File selected!");
                }
                if (cursor != null) {
                    cursor.close();
                }
            }*//*
        }
    }*/

    private void UploadFile() {

        btnSubmit.setOnClickListener(view -> {
            if (pdf_uri != null){

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                /*SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();*/

                ApplicationModel applicationModel = new ApplicationModel();
                applicationModel.setApplicantId(FirebaseAuth.getInstance().getUid());
                applicationModel.setJob_key(key);
                applicationModel.setJob_title(title);
                applicationModel.setJob_date(date);

                if (pdf_uri == null) {
                    preview_et.setError("Choose pdf document...");
                    return;
                }

                FirebaseFirestore
                        .getInstance()
                        .collection("Applications")
                        .document(key)
                        .set(applicationModel)
                        .addOnSuccessListener(unused ->

                                FirebaseStorage.
                                getInstance()
                                .getReference()
                                .child("Applications")
                                .child(key)
                                .putFile(pdf_uri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    progressDialog.dismiss();
                                    taskSnapshot
                                            .getStorage()
                                            .getDownloadUrl()
                                            .addOnSuccessListener(uri ->
                                                    FirebaseFirestore
                                                    .getInstance()
                                                    .collection("Applications")
                                                    .document(key)
                                                    .update("url", uri.toString()));
                                    Toast.makeText(getContext(),"Application wassuccessful...",Toast.LENGTH_LONG).show();

                                }).addOnFailureListener(e ->
                                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show())).addOnFailureListener(e ->
                        Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show());
            }else {
                Toast.makeText(getContext(),"Url empty!!!",Toast.LENGTH_LONG).show();
            }
        });
    }
}