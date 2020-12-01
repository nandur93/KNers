package com.ndu.sanghiang.kners.smartqap.inline.sqlite.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ItemCodesAdapter extends RecyclerView.Adapter<ItemCodesAdapter.ItemCodeViewHolder> {

    private final List<ItemCode> itemCodeList;

    public static class ItemCodeViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView item_code;
        public TextView item_desc;
        public TextView singkatan;
        public TextView kode_sachet;
        public TextView asset_location;
        public TextView asset_status;
        //public TextView dot;
        public TextView timestamp;

        public ItemCodeViewHolder(View view) {
            super(view);
            //dot = view.findViewById(R.id.dot);
            id = view.findViewById(R.id.assetId);
            item_code = view.findViewById(R.id.itemCode);
            item_desc = view.findViewById(R.id.itemDesc);
            singkatan = view.findViewById(R.id.singkatan);
            kode_sachet = view.findViewById(R.id.kodeSachet);
        }
    }


    public ItemCodesAdapter(List<ItemCode> assetList) {
        this.itemCodeList = assetList;
    }

    @NonNull
    @Override
    public ItemCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_code_list_row, parent, false);

        return new ItemCodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemCodeViewHolder holder, final int position) {
        ItemCode itemCode = itemCodeList.get(position);
        holder.id.setText(String.valueOf(position + 1));
        holder.item_code.setText(itemCode.getItem_code());
        String item_desc_string = itemCode.getItem_desc();
        if (item_desc_string.length() <= 23) {
            holder.item_desc.setText(item_desc_string);
        } else {
            holder.item_desc.setText(item_desc_string.substring(0, Math.min(item_desc_string.length(), 23)).concat("..."));
        }
        holder.singkatan.setText(itemCode.getSingkatan());

        // Displaying dot from HTML character code
        //holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        //holder.timestamp.setText(formatDate(asset.getTimestamp()));
        holder.itemView.setOnClickListener(v -> Log.d("TAG", "onClick: " + position));
    }

    @Override
    public int getItemCount() {
        return itemCodeList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /*https://stackoverflow.com/questions/42363135/get-position-of-specific-cardview-in-recyclerview-without-clicking-scrolling*/
    public int getItemCodePos(String itemCode) {
        for (int i = 0; i < itemCodeList.size(); i++) {
            if (itemCodeList.get(i).getItem_code().equals(itemCode)) {
                return i;
            }
        }
        return 0;
    }

    public String getItemDesc(int position) {
        return itemCodeList.get(position).getItem_desc();
    }

    /*https://stackoverflow.com/a/37562572/7772358*/
    /*public void filter(String text, DatabaseHelper db, String lokalExport) {
        itemCodeList.clear();
        if (text.isEmpty()) {
            itemCodeList.addAll(db.getAllItemCode(lokalExport));
        } else {
            text = text.toLowerCase();
            try {
                for (Asset asset : db.getAllAssetsByDept(assetLocation)) {
                    if (asset.getAsset_pic().toLowerCase().contains(text) ||
                            asset.getAsset_desc().toLowerCase().contains(text) ||
                            asset.getAsset_code().toLowerCase().contains(text) ||
                            asset.getAsset_rfid().toLowerCase().contains(text) ||
                            asset.getAsset_location().toLowerCase().contains(text) ||
                            asset.getTimestamp().toLowerCase().contains(text)) {
                        assetList.add(asset);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }*/
}
