package com.example.hustlers.fragments;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hustlers.R;
import com.example.hustlers.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextInputEditText et_name,et_surname, et_email;
    private MaterialButton btnUpdate;
    private FloatingActionButton addImage;

    private CircleImageView profileImage;

    ViewGroup profileViewGroup;

    private StorageReference storageReference;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        profileViewGroup = (ViewGroup) inflater.inflate(R.layout.activity_profile_fragment,container,false);
        init(profileViewGroup);
        retrieve(profileViewGroup);
        selectImage(profileViewGroup);
        //update(imageUri);
        BtnUpdate(profileViewGroup);


        return profileViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {

        storageReference = FirebaseStorage.getInstance().getReference();

        et_name = view.findViewById(R.id.profile_name_et);
        et_surname = view.findViewById(R.id.profile_surname_et);
        et_email = view.findViewById(R.id.profile_email_et);

        addImage = view.findViewById(R.id.addImg_fab);
        profileImage = view.findViewById(R.id.profileImage);

        btnUpdate = view.findViewById(R.id.update_btn);
    }

    private void retrieve(View view) {
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                         et_name.setText(Objects.requireNonNull(documentSnapshot.get("name")).toString());
                         et_surname.setText(Objects.requireNonNull(documentSnapshot.get("surname")).toString());
                         et_email.setText(Objects.requireNonNull(documentSnapshot.get("email")).toString());

                        try {

                            RequestOptions placeholderOption = new RequestOptions();

                            if (documentSnapshot.get("profile").toString() != null) {
                                Glide.with(getContext())
                                        .applyDefaultRequestOptions(placeholderOption)
                                        .load(documentSnapshot.get("profile").toString())
                                        .into(profileImage);
                            }else {
                                placeholderOption.placeholder(R.drawable.ic_account);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void selectImage(View view) {
        addImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,GALLERY_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            //update user image
            update(imageUri);
        }
    }

    private void BtnUpdate(ViewGroup view){
        Context context = view.getContext();

        btnUpdate.setOnClickListener(view1 ->
                updateInfo());
    }

    private void updateInfo(){
        String name = et_name.getText().toString().trim();
        String surname = et_surname.getText().toString().trim();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("surname", surname);

        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .update(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "User record update successfully!!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void update(Uri uri) {

        try {
            final StorageReference filepath = storageReference
                    .child("Profile_images")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(System.currentTimeMillis()+"."+getFileExtention(uri));

            if (uri != null) {
                filepath
                        .putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> filepath
                                .getDownloadUrl()
                                .addOnSuccessListener(uri1 -> {
                                    try
                                    {
                                        try
                                        {
                                            FirebaseFirestore
                                                    .getInstance()
                                                    .collection("Users")
                                                    .document(FirebaseAuth.getInstance().getUid())
                                                    .update("profile", uri1.toString())
                                                    .addOnSuccessListener(unused ->
                                                            Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show());

                                        }catch(Exception e)
                                        {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                })).addOnFailureListener(e ->
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show());
            }else {
                Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileExtention(Uri mUri)
    {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
