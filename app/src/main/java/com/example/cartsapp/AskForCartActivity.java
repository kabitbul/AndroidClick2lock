package com.example.cartsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cartsapp.SERVER.URLs;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class AskForCartActivity extends AppCompatActivity {
    private String access_token = null;
    private String email = null;
    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_cart);
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
          access_token = b.getString("access_token");
          email = b.getString("email");
        }
        //get the userId
        Button btnScan = findViewById(R.id.btnTakeCart);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        }
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* IntentIntegrator intentIntegrator = new IntentIntegrator(
                        AskForCartActivity.this);
                intentIntegrator.setPrompt("scan to release cart");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                //initiate scan
                intentIntegrator.initiateScan();*/
                Dexter.withActivity(AskForCartActivity.this).withPermission(Manifest.permission.CAMERA)
                        .withListener((new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                               // scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
                               // scannerView.setResultHandler(AskForCartActivity.this);
                              //  scannerView.startCamera();
                                IntentIntegrator integrator = new IntentIntegrator(AskForCartActivity.this);
                                integrator.setPrompt("Scan Cart QR Code");
                                integrator.setOrientationLocked(false);
                                integrator.initiateScan();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(AskForCartActivity.this,"Please allow access to camera",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        })).check();

            }
        });
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );
        if(intentResult.getContents() != null){
            String requestURL = intentResult.getContents();
            int start = requestURL.indexOf("userId=")+7;
            String b4userId = requestURL.substring(0,start);
            String afterUserId = requestURL.substring(start+1, requestURL.length());
            String finalURL = b4userId + email + afterUserId;
            //open cart with modified url
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URLs.ROOT_URL)//.addConverterFactory(GsonConverterFactory.create())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();
            UnlockAPI unlockAPI = retrofit.create(UnlockAPI.class);
            //PostReg postReg = new PostReg(email,password,confirmPassword);
            Call<Void> call = unlockAPI.unlockCart("bearer " +access_token,finalURL);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(!response.isSuccessful())
                    {
                        Toast.makeText(AskForCartActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
                    }

                    if (response != null && response.body() != null)
                    {
                      // access_token = response.body().toString();
                     //   openAskCartActivity(access_token,email);
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AskForCartActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            }
            catch (Exception ex)
            {
                String  ts = ex.toString();
                Toast.makeText(AskForCartActivity.this, ts, Toast.LENGTH_LONG).show();
            }
        }
    }


}