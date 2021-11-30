package com;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eismayilzada.receipttracker.R;
import com.eismayilzada.receipttracker.Receipt;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    private LinkedList<Receipt> mItemList;
    private LayoutInflater mInflater;

    public ItemListAdapter(Context context, LinkedList<Receipt> itemList){
        mInflater = LayoutInflater.from(context);
        this.mItemList = itemList;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recy_item, parent, false);
        return new ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Receipt mCurrent = mItemList.get(position);
        holder.storeNameTextView.setText(mCurrent.getStoreName());
        holder.receiptIdTextView.setText(mCurrent.getReceiptId());
        holder.receiptAmountTextView.setText(mCurrent.getTotalSum() + " "+mCurrent.getCurrency());
        //holder.currencyTextView.setText(mCurrent.getCurrency());

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        public final TextView storeNameTextView;
        //public final TextView totalSumTextView;
        public final TextView receiptIdTextView;
        public final TextView receiptAmountTextView;


        final ItemListAdapter mAdapter;

        public ItemViewHolder(View itemView, ItemListAdapter adapter) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            receiptIdTextView = itemView.findViewById(R.id.receiptIdTextView);
            receiptAmountTextView = itemView.findViewById(R.id.receiptAmountTextView);
            this.mAdapter = adapter;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Toast.makeText(view.getContext(), mItemList.get(position).getStoreName(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
