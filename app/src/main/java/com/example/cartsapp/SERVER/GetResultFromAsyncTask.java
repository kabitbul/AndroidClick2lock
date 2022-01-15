package com.example.cartsapp.SERVER;

public interface GetResultFromAsyncTask {
    public void onAsyncTaskDone(String result);
    public void onAsyncDoneWithRestultType(int result_code, String response);
}