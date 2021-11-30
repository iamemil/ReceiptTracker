package com.eismayilzada.receipttracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.ItemListAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.Comparator;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private EditText receiptText;
    private Button searchBtn;
    private RecyclerView mRecyclerView;
    private ItemListAdapter mAdapter;
    private LinkedList<Receipt> mItemList = new LinkedList<>();
    private RequestQueue queue;
    private final String sharedPrefFileName = "com.eismayilzada.receipttracker.sharedprefs.pref";
    private static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(sharedPrefFileName,MODE_PRIVATE);

        mRecyclerView = findViewById(R.id.receiptRecyclerView);
        receiptText = findViewById(R.id.receiptText);
        searchBtn = findViewById(R.id.searchBtn);

        mAdapter = new ItemListAdapter(this, mItemList );
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(sharedPreferences.getInt("receiptsCount",0) >0){
            readReceiptsFromMemory();
        }

//5QRDHY2EzxUQ 6nvCCc8eMr86 DjFfKjgP1TQb AXTmRftzUwCr F1j8pXfD7KtG
    }

    public void onSearchBtnClick(View view){
        String receiptIdText = receiptText.getText().toString();
        if(!receiptIdText.isEmpty()){
            System.out.println("Receipt ID "+receiptIdText);
            addReceipt(receiptIdText);
            receiptText.setText("");
        }else{
            Toast.makeText(view.getContext(), "Receipt ID can't be empty", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void onResetMemoryBtnClick(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mItemList.clear();
        mAdapter.notifyDataSetChanged();
        System.out.println("reset clicked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveReceiptsToMemory(mItemList);
    }

    void addReceipt(String receiptId){
        queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://monitoring.e-kassa.gov.az/pks-portal/1.0.0/documents/" + receiptId;
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response!=null){
                        String storeName = response.getJSONObject("cheque").getString("storeName");
                        String totalSum = response.getJSONObject("cheque").getJSONObject("content").getString("sum");
                        String currency = response.getJSONObject("cheque").getJSONObject("content").getString("currency");
                        if(mItemList.stream().map(Receipt::getReceiptId).filter(c->c.toString().equals(receiptId)).count()==0){
                            Receipt newReceipt = new Receipt(storeName,receiptId,totalSum,currency);
                            mItemList.add(newReceipt);
                            mRecyclerView.getLayoutManager().scrollToPosition(0);
                            mAdapter.notifyDataSetChanged();
                        }
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


    void saveReceiptsToMemory(LinkedList<Receipt> receipts){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("receiptsCount",receipts.size());
        editor.apply();
        for (int i=0;i<receipts.size();i++){
            editor.putString("receipt-"+i,receipts.get(i).receiptId);
            editor.apply();
        }
        editor.apply();
    }

    void readReceiptsFromMemory(){
        int size = sharedPreferences.getInt("receiptsCount",0);
        for (int i =0;i<size;i++){
            addReceipt(sharedPreferences.getString("receipt-"+i," "));
        }

    }
}