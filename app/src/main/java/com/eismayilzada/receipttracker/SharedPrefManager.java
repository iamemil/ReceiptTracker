package com.eismayilzada.receipttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.ims.RcsUceAdapter;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.LinkedList;

public class SharedPrefManager {
    public static final String PREFS_NAME = "APP_Shared_Prefs";
    public static final String RECEIPTS = "USER_RECEIPTS";

    public SharedPrefManager() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveReceipts(Context context, LinkedList<Receipt> receipts) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(receipts);

        editor.putString(RECEIPTS, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Receipt receipt) {
        LinkedList<Receipt> receipts = getReceipts(context);
        if (receipts == null)
            receipts = new LinkedList<Receipt>();
        receipts.add(receipt);
        saveReceipts(context, receipts);
    }

    public void removeReceipt(Context context, Receipt receipt) {
        LinkedList<Receipt> receipts = getReceipts(context);
        if (receipts != null) {
            receipts.remove(receipt);
            saveReceipts(context, receipts);
        }
    }

    public LinkedList<Receipt> getReceipts(Context context) {
        SharedPreferences settings;
        LinkedList<Receipt> receipts;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(RECEIPTS)) {
            String jsonReceipts = settings.getString(RECEIPTS, null);
            Gson gson = new Gson();
            Receipt[] receiptItems = gson.fromJson(jsonReceipts,
                    Receipt[].class);

            receipts = (LinkedList<Receipt>) Arrays.asList(receiptItems);
            receipts  = new LinkedList<Receipt>(receipts);
        } else
            return null;

        return (LinkedList<Receipt>) receipts;
    }
}
