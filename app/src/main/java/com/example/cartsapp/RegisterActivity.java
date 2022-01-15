package com.example.cartsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cartsapp.SERVER.URLs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ACC_TOKEN = "acc_token";
    public static final String LAST_TOKEN_CREATE = "last_token_create";

    private  String acc_token;
    private  String last_token_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        if(acc_token != null && last_token_create != null)
        {
            //last_token_create.
        }
        setContentView(R.layout.activity_register);

        TextView txtHaveAcc = (TextView) findViewById(R.id.txtViewAlreadyHasAccount);
        txtHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });
        EditText emailText = findViewById(R.id.inputEmail);
        EditText passwordText = findViewById(R.id.inputPassword);
        EditText confirmPasswordText = findViewById(R.id.inputConfirmPassword);
        ProgressBar progressBar = findViewById(R.id.progressBarRegister);

        Button regBtn = findViewById(R.id.btnRegister);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();
                if (!password.equals(confirmPassword) || password == null || email == null) {
                    Toast.makeText(RegisterActivity.this, "confirm Password dont match", Toast.LENGTH_LONG).show();
                    return;
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URLs.ROOT_URL).addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                RegistrationAPI registrationAPI = retrofit.create(RegistrationAPI.class);
                PostReg postReg = new PostReg(email, password, confirmPassword);
                Call<String> call = registrationAPI.createPost(postReg);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String access_token = null;
                        if (response != null && response.body() != null) {
                            access_token = response.body().toString();
                            saveData(access_token);
                            openAskCartActivity(access_token, email);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }//On Click
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openAskCartActivity(String access_token, String email) {
        Intent intent = new Intent(this, AskForCartActivity.class);
        Bundle b = new Bundle();
        b.putString("access_token", access_token);
        b.putString("email", email);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void saveData(String access_token) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Date c = Calendar.getInstance().getTime();
       // Date simpleDate =new SimpleDateFormat("dd/MM/yyyy").parse(c.toString());
        editor.putString(ACC_TOKEN, access_token);
        editor.putString(LAST_TOKEN_CREATE,c.toString());

        editor.apply();
    }

    public void loadData() {
      SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
      acc_token = sharedPreferences.getString(ACC_TOKEN,"");
      last_token_create = sharedPreferences.getString(LAST_TOKEN_CREATE,"");
    }
}