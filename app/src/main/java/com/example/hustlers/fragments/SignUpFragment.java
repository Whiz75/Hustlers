package com.example.hustlers.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hustlers.R;
import com.example.hustlers.activities.MainActivity;
import com.example.hustlers.interfaces.FragmentClickInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {

    private MaterialButton btn_backToLogin, btn_sign_up_user;
    private TextInputEditText name_txt, surname_txt, email_txt, password_txt, confirm_pass_txt;

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
        //SignUpUser(viewGroup);
        SignUp(viewGroup);

        return viewGroup;
    }

    private void init(ViewGroup view) {

        name_txt = view.findViewById(R.id.InputName);
        surname_txt = view.findViewById(R.id.InputLastname);
        email_txt = view.findViewById(R.id.InputEmail);
        password_txt = view.findViewById(R.id.InputPassword);
        confirm_pass_txt = view.findViewById(R.id.InputConfirmPassword);

        btn_sign_up_user = view.findViewById(R.id.sign_up_button);
        btn_backToLogin = view.findViewById(R.id.btn_have_account);
    }

    private void GoToSignIn(ViewGroup view) {
        view.getContext();
        btn_backToLogin.setOnClickListener(v -> clickInterface.BtnLoginClick());
    }

    private void SignUp(ViewGroup view) {
        btn_sign_up_user.setOnClickListener(view1 -> {
            Toast.makeText(getContext(),"clicked",Toast.LENGTH_LONG).show();
            try {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SignUpUser(ViewGroup view) {

        //initialize firebase auth object
        btn_sign_up_user.setOnClickListener(v -> {
            /*final String name = name_txt.getText().toString().trim();
            final String lastName = surname_txt.getText().toString().trim();
            final String email = email_txt.getText().toString().trim();
            final String password = password_txt.getText().toString().trim();
            final String confirmPassword = confirm_pass_txt.getText().toString().trim();*/
        });
    }
}