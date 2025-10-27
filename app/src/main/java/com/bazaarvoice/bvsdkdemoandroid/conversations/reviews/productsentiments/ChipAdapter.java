package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bazaarvoice.bvsdkdemoandroid.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipViewHolder> {

    private final Context context;
    private final List<String> filterList;

    public ChipAdapter(Context context, List<String> filterList) {
        this.context = context;
        this.filterList = filterList;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter_chip, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        String filterText = filterList.get(position);
        holder.chip.setText(filterText);

        // Handle chip clicks
        holder.chip.setOnClickListener(v -> {
            boolean isChecked = holder.chip.isChecked();
            Toast.makeText(context, filterText + (isChecked ? " selected" : " deselected"), Toast.LENGTH_SHORT).show();
            // You can add your filtering logic here
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    // ViewHolder class for a single chip
    public static class ChipViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = (Chip) itemView;
        }
    }
}