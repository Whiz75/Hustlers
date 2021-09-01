package com.example.hustlers.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hustlers.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextInputEditText et_name,et_surname, et_email;
    private MaterialButton btnUpdate;

    ViewGroup profileViewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        profileViewGroup = (ViewGroup) inflater.inflate(R.layout.activity_profile_fragment,container,false);
        init(profileViewGroup);
        retrieve(profileViewGroup);
        update(profileViewGroup);

        return profileViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        et_name = view.findViewById(R.id.profile_name_et);
        et_surname = view.findViewById(R.id.profile_surname_et);
        et_email = view.findViewById(R.id.profile_email_et);

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
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void update(View view) {
        //String name = et_name.getText().toString();
        //String surname = et_surname.getText().toString();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name",et_name.getText().toString());
        userMap.put("surname",et_surname.getText().toString());

        btnUpdate.setOnClickListener(view1 ->

            FirebaseFirestore
            .getInstance()
            .collection("Users")
            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .addSnapshotListener((value, error) -> {
                if (value != null) {
                    if (Objects.requireNonNull(value.get("name")).toString() == et_name.getText().toString()) {
                        Toast.makeText(getContext(),"Name is the same!!!",Toast.LENGTH_LONG).show();
                    }

                    if (value.get("surname").toString() == et_surname.getText().toString()){
                        Toast.makeText(getContext(),"Surname is the same!!!",Toast.LENGTH_LONG).show();
                    }
                }
            }));

        /*Toast.makeText(getContext(),"Update!!!",Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();*/
    }
}
