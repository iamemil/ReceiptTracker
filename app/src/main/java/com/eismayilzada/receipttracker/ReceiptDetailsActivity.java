package com.eismayilzada.receipttracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptDetailsActivity extends AppCompatActivity {

    private TextView receiptDetailsIdTextView;
    private TextView receiptDetailsStoreNameTextView;
    private TextView receiptDetailsAmountTextView;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);

        receiptDetailsIdTextView = findViewById(R.id.receiptDetailsIdTextView);
        receiptDetailsStoreNameTextView = findViewById(R.id.receiptDetailsStoreNameTextView);
        receiptDetailsAmountTextView = findViewById(R.id.receiptDetailsAmountTextView);
        Intent intent = getIntent();
        receiptDetailsIdTextView.setText(intent.getStringExtra("receiptId"));
        receiptDetailsStoreNameTextView.setText(intent.getStringExtra("storeName"));
        receiptDetailsAmountTextView.setText(intent.getStringExtra("amount"));
        //fillReceiptDetails(intent.getStringExtra("receiptId"));
    }


    void fillReceiptDetails(String receiptId){
        queue = Volley.newRequestQueue(ReceiptDetailsActivity.this);
        String url = "https://monitoring.e-kassa.gov.az/pks-portal/1.0.0/documents/" + receiptId;
        //System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response!=null){
                        String storeName = response.getJSONObject("cheque").getString("storeName");
                        String totalSum = response.getJSONObject("cheque").getJSONObject("content").getString("sum");
                        String currency = response.getJSONObject("cheque").getJSONObject("content").getString("currency");
                        receiptDetailsStoreNameTextView.setText(storeName);
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Receipt Tracker API", "Failed to get data: "+error);
            }
        });
        queue.add(jsonObjectRequest);
    }

}