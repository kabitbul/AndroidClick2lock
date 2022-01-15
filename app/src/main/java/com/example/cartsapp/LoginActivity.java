package com.example.cartsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView txtViewSignUp = (TextView) findViewById(R.id.txtViewSignUp);
        txtViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLRegisterActivity();
            }
        });
    }
    public  void openLRegisterActivity()
    {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}