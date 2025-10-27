package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;
import java.util.List;
import com.bazaarvoice.bvsdkdemoandroid.R;


public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Define constants for each view type
    private static final int TYPE_CHIP_SECTION = 0;
    private static final int TYPE_QUOTE_CARD = 1;
    private static final int TYPE_TOGGLE = 2;

    private final Context context;
    private final List<ReviewItem> allItems; // Holds all possible items
    private final List<ReviewItem> displayItems; // Holds only the items to be displayed
    private boolean isExpanded = false;

    public ReviewAdapter(Context context, List<ReviewItem> allItems) {
        this.context = context;
        this.allItems = allItems;
        this.displayItems = new ArrayList<>();
        updateDisplayList(); // Initial setup of the list to be displayed
    }

    /**
     * Rebuilds the list that is actually displayed by the RecyclerView
     * based on the isExpanded state.
     */
    private void updateDisplayList() {
        displayItems.clear();
        for (ReviewItem item : allItems) {
            // Chip sections are always visible
            if (item instanceof ReviewItem.ChipSection) {
                displayItems.add(item);
            }
            // Quote cards are only visible when expanded
            else if (item instanceof ReviewItem.QuoteCard) {
                if (isExpanded) {
                    displayItems.add(item);
                }
            }
        }
        // Always add the toggle button at the end of the displayed list
        displayItems.add(new ReviewItem.Toggle(isExpanded));
    }

    @Override
    public int getItemViewType(int position) {
        ReviewItem item = displayItems.get(position);
        if (item instanceof ReviewItem.ChipSection) return TYPE_CHIP_SECTION;
        if (item instanceof ReviewItem.QuoteCard) return TYPE_QUOTE_CARD;
        if (item instanceof ReviewItem.Toggle) return TYPE_TOGGLE;
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_CHIP_SECTION:
                return new ChipSectionViewHolder(inflater.inflate(R.layout.item_chip_section, parent, false));
            case TYPE_QUOTE_CARD:
                return new QuoteCardViewHolder(inflater.inflate(R.layout.item_quote_card, parent, false));
            case TYPE_TOGGLE:
                return new ToggleViewHolder(inflater.inflate(R.layout.item_toggle_button, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReviewItem item = displayItems.get(position);
        if (holder.getItemViewType() == TYPE_CHIP_SECTION) {
            bindChipSection((ChipSectionViewHolder) holder, (ReviewItem.ChipSection) item);
        } else if (holder.getItemViewType() == TYPE_QUOTE_CARD) {
            bindQuoteCard((QuoteCardViewHolder) holder, (ReviewItem.QuoteCard) item);
        } else if (holder.getItemViewType() == TYPE_TOGGLE) {
            bindToggle((ToggleViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    private void bindChipSection(ChipSectionViewHolder holder, ReviewItem.ChipSection item) {
        holder.title.setText(item.title);
        holder.title.setCompoundDrawablesWithIntrinsicBounds(item.iconRes, 0, 0, 0);

        holder.chipContainer.removeAllViews();
        for (String chipText : item.chips) {
            Chip chip = new Chip(context);
            chip.setText(chipText);
            if ("Cons".equals(item.title)) {
                chip.setChipStrokeColorResource(R.color.red);
                chip.setChipStrokeWidth(1f);
            }else{
                chip.setChipStrokeColorResource(R.color.green);
                chip.setChipStrokeWidth(1f);
            }
            holder.chipContainer.addView(chip);
        }
    }

    private void bindQuoteCard(QuoteCardViewHolder holder, ReviewItem.QuoteCard item) {
        holder.title.setText(item.title);
        holder.quotesContainer.removeAllViews();
        for (String quoteText : item.quotes) {
            TextView textView = new TextView(context);
            textView.setText(quoteText);
            textView.setPadding(0, 8, 0, 8);
            holder.quotesContainer.addView(textView);
        }
    }

    private void bindToggle(ToggleViewHolder holder) {
        holder.buttonText.setText(isExpanded ? "Show less" : "Show more");
        holder.itemView.setOnClickListener(v -> {
            // Toggle the state
            isExpanded = !isExpanded;
            // Rebuild the display list based on the new state
            updateDisplayList();
            // Notify the adapter that the entire data set has changed
            notifyDataSetChanged();
        });
    }

    // --- ViewHolder Classes ---

    public static class ChipSectionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        FlexboxLayout chipContainer;
        public ChipSectionViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.section_title);
            chipContainer = view.findViewById(R.id.chip_container);
        }
    }

    public static class QuoteCardViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout quotesContainer;
        public QuoteCardViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.quote_card_title);
            quotesContainer = view.findViewById(R.id.quotes_container);
        }
    }

    public static class ToggleViewHolder extends RecyclerView.ViewHolder {
        TextView buttonText;
        public ToggleViewHolder(View view) {
            super(view);
            buttonText = view.findViewById(R.id.toggle_button_text);
        }
    }
}