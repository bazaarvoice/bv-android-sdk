package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments;

import java.util.List;

// Abstract base class for all items in our RecyclerView list.
public abstract class ReviewItem {

    private ReviewItem() {} // Prevents other classes from extending it

    /**
     * Represents a section with a title and a list of chips.
     * The constructor now correctly accepts an int for the icon resource.
     */
    public static class ChipSection extends ReviewItem {
        public final String title;
        public final List<String> chips;
        public final int iconRes; // This now correctly holds the icon ID

        public ChipSection(String title, List<String> chips, int iconRes) {
            this.title = title;
            this.chips = chips;
            this.iconRes = iconRes;
        }
    }

    // Represents a card with a title and a list of quotes.
    public static class QuoteCard extends ReviewItem {
        public final String title;
        public final List<String> quotes;

        public QuoteCard(String title, List<String> quotes) {
            this.title = title;
            this.quotes = quotes;
        }
    }

    // Represents the "Show more" / "Show less" button.
    public static class Toggle extends ReviewItem {
        public boolean isExpanded;

        public Toggle(boolean isExpanded) {
            this.isExpanded = isExpanded;
        }
    }
}