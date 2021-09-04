package com.example.hustlers.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.hustlers.R;
import com.example.hustlers.activities.MainActivity;
import com.example.hustlers.interfaces.FragmentClickInterface;
import com.example.hustlers.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpFragment extends Fragment {

    private MaterialButton btn_backToLogin, btn_sign_up_user;
    private TextInputEditText name_txt, surname_txt, email_txt, password_txt, confirm_pass_txt;

    private FloatingActionButton addImg_fab;
    private CircleImageView profileImage;

    private StorageReference storageReference;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;

    public SignUpFragment() {
        // Required empty public constructor
    }

    private FragmentClickInterface clickInterface;

    public SignUpFragment(FragmentClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up, container, false);

        //call methods here
        init(viewGroup);
        GoToSignIn(viewGroup);
        SignUpUser(viewGroup);
        //SignUp(viewGroup);
        selectImage(viewGroup);

        return viewGroup;
    }

    private void init(ViewGroup view) {

        addImg_fab =view.findViewById(R.id.addImg_fab);
        profileImage = view.findViewById(R.id.input_profile_Img);

        name_txt = view.findViewById(R.id.InputName);
        surname_txt = view.findViewById(R.id.InputLastname);
        email_txt = view.findViewById(R.id.InputEmail);
        password_txt = view.findViewById(R.id.InputPassword);
        confirm_pass_txt = view.findViewById(R.id.InputConfirmPassword);

        btn_sign_up_user = view.findViewById(R.id.sign_up_button);
        btn_backToLogin = view.findViewById(R.id.btn_have_account);

        //disable
        profileImage.setVisibility(View.GONE);
        addImg_fab.setVisibility(View.GONE);
    }

    private void GoToSignIn(ViewGroup view) {
        view.getContext();
        btn_backToLogin.setOnClickListener(v -> clickInterface.BtnLoginClick());
    }

    private void selectImage(ViewGroup view) {
        addImg_fab.setOnClickListener(v -> {
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
        }
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

    private String getFileExtention(Uri mUri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }


    private void SignUpUser(ViewGroup view) {

        btn_sign_up_user.setOnClickListener(view1 -> {

            try {
                final String name = Objects.requireNonNull(name_txt.getText()).toString().trim();
                final String lastName = Objects.requireNonNull(surname_txt.getText()).toString().trim();
                final String email = email_txt.getText().toString().trim();
                final String password = password_txt.getText().toString().trim();
                final String confirmPassword = confirm_pass_txt.getText().toString().trim();

                if (!validate()) {
                    return;
                }

                FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(authResult -> {

                            UserModel user = new UserModel();
                            user.setName(name);
                            user.setSurname(lastName);
                            user.setEmail(email);
                            user.setProfile(null);

                            FirebaseFirestore
                                    .getInstance()
                                    .collection("Users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(getContext(),"User registered successfully!",Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    });
                            }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            }catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        final String name = name_txt.getText().toString().trim();
        final String lastName = surname_txt.getText().toString().trim();
        final String email = email_txt.getText().toString().trim();
        final String password = password_txt.getText().toString().trim();
        final String confirmPassword = confirm_pass_txt.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(name)){
            name_txt.setError("Please enter name");
            isValid = false;
        }

        if (TextUtils.isEmpty(lastName)){
            name_txt.setError("Please enter surname");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)){
            name_txt.setError("Please enter email");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)){
            name_txt.setError("Please enter password");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            name_txt.setError("Please re-enter password");
            isValid = false;
        }

        if (!TextUtils.equals(password,confirmPassword)){
            Toast.makeText(getContext(),"Passwords do not match!!!",Toast.LENGTH_LONG).show();
            isValid = false;
        }

        return isValid;
    }
}