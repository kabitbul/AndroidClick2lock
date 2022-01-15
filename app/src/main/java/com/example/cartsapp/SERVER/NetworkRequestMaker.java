package com.example.cartsapp.SERVER;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.PixelCopy;

import androidx.appcompat.app.AppCompatActivity;


import com.example.cartsapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkRequestMaker {
    GetResultFromAsyncTask context;
    private ProgressDialog progress;
    public String dialogMessage="";
    public int whichFragment = 0;
    public String secretKey = "";
    public String userIdentity = "";
    public NetworkRequestMaker(GetResultFromAsyncTask context) {
        this.context = context;
    }

    public void SimpleGetRequestForJson(String url, String parameters){
        new SimpleGetRequestForJson().execute(url,parameters);
    }

    public void doGetRequestForJson(String url, String parameters){
        new GetRequestForJson().execute(url,parameters);
    }

    public void doPostRequest(String url, JSONObject parameters){
        new PostRequestMaker(url).execute(parameters);
    }

    // Task for JSON getting from Server with dialog
    public class SimpleGetRequestForJson extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder outputResult = new StringBuilder("");
            try {
                if(params[1] !=null && !params[1].isEmpty()){
                    if(params[0].contains("gold")){

                    }else{
                        params[0] = params[0] + "?" + params[1]+"&secret="+secretKey+"&userIdentity="+userIdentity;
                    }

                }
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
// con.connect();
                httpURLConnection.setDoInput(true);
                InputStream is;
                Log.d("Test", "response code" + httpURLConnection.getResponseCode());
                if (httpURLConnection.getResponseCode() == 200) {
                    is = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        outputResult.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return outputResult.toString();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progress.dismiss();
            context.onAsyncTaskDone(result);
        }
    }

    // Task for JSON getting from Server
    public class GetRequestForJson extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBar("");
// if(dialogMessage.isEmpty()) {
// setProgressBar("downloading Data");
// }else{
// setProgressBar(dialogMessage);
// }
            progress.show();
            progress.setCancelable(false);
            progress.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progress.setContentView(R.layout.progress_dialog);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder outputResult = new StringBuilder("");
            try {
                if(params[1] !=null && !params[1].isEmpty()){
                    if(params[0].contains("gold")){

                    }else{
                        params[0] = params[0] + "?" + params[1]+"&secret="+secretKey+"&userIdentity="+userIdentity;
                    }

                }
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
// con.connect();
                httpURLConnection.setDoInput(true);
                InputStream is;
                Log.d("Test", "response code" + httpURLConnection.getResponseCode());
                if (httpURLConnection.getResponseCode() == 200) {
                    is = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        outputResult.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return outputResult.toString();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progress.dismiss();
            context.onAsyncTaskDone(result);
        }
    }

    // Post Request Task
    public class PostRequestMaker extends AsyncTask<JSONObject, Integer, String> {
        String url;
        int apiCallResultCode = 0;
        public PostRequestMaker(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBar("");
            progress.show();
            progress.setCancelable(false);
            progress.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progress.setContentView(R.layout.progress_dialog);
        }

        @Override
        protected String doInBackground(JSONObject... params) {//Params is array of json object
            StringBuilder outputResult = new StringBuilder("");
            try {
                Log.d("test",this.url);
                URL url = new URL(this.url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept","application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                Log.d("test",params[0].getString("Email"));
//going to upload parameters to Server
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(params[0].toString());//send irst object

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

// now reading response from server
                Log.d("Test", "response code" + httpURLConnection.getResponseCode());
                InputStream is;
                apiCallResultCode = httpURLConnection.getResponseCode();
                if (httpURLConnection.getResponseCode() == 200) {

                    is = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        outputResult.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return outputResult.toString();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Test", "response ok" + result);
            progress.dismiss();
            context.onAsyncDoneWithRestultType(this.apiCallResultCode, result);

        }
    }
    public void setProgressBar(String message){

        progress = new ProgressDialog(((AppCompatActivity) context));
        secretKey = getDataSharedPreference("secret_key", (Activity) context);
        userIdentity = getDataSharedPreference("userId", (Activity) context);

       // Log.d("israr","secret: "+secretKey);
       // Log.d("israr","userId: "+userIdentity);

    }
    public String getDataSharedPreference(String key, Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sharedPreferences.getString(key,"");
    }
}
