/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

/**
 * TODO: Describe file here.
 */
abstract class ConversationsUgcView extends BVView {
    private Product product;

    ConversationsUgcView(Context context) {
        super(context);
    }

    ConversationsUgcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    ConversationsUgcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ConversationsUgcView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        this.product = product;
    }

    @Override
    public String getProductId() {
        if (product == null) {
            throw new IllegalStateException("Must associate a product with a " + getViewName());
        }
        return product.getId();
    }

    abstract String getViewName();

}
