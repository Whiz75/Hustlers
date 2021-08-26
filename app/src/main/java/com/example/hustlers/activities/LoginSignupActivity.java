package com.example.hustlers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hustlers.R;
import com.example.hustlers.fragments.LoginFragment;
import com.example.hustlers.fragments.SignUpFragment;
import com.example.hustlers.interfaces.FragmentClickInterface;

public class LoginSignupActivity extends AppCompatActivity implements FragmentClickInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        if(savedInstanceState == null) {
            LoginFragment frag = new LoginFragment((FragmentClickInterface) this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_host, frag)
                    .commit();
        }
    }

    @Override
    public void BtnLoginClick() {

        LoginFragment frag = new LoginFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.frag_host, frag, "SIGN IN FRAG")
                .commit();
    }

    @Override
    public void BtnSignupClick() {

        SignUpFragment frag = new SignUpFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                .replace(R.id.frag_host, frag, "SIGN UP FRAGMENT")
                .commit();
    }
}