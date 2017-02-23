/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVTransactionEvent;
import com.bazaarvoice.bvandroidsdk.BVPixel;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVTransaction;
import com.bazaarvoice.bvsdkdemoandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoCartActivity extends AppCompatActivity {

    @BindView(R.id.cart_recycler_view) RecyclerView productRecyclerView;
    @BindView(R.id.cartEmptyTextView) TextView cartEmptyTextView;
    @BindView(R.id.cartCheckoutButton) Button cartCheckoutButton;

    private DemoCartAdapter demoCartAdapter;
    private  DemoCart cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cart = DemoCart.INSTANCE;
        setupRecyclerView();
        setupCheckoutButton();
        updateViewVisibility();
    }

    private void setupRecyclerView() {
        demoCartAdapter = new DemoCartAdapter();
        demoCartAdapter.setCart(cart);
        demoCartAdapter.setOnItemClickListener(new DemoCartAdapter.OnBvProductClickListener() {
            @Override
            public void onBvProductClickListener(BVProduct bvProduct, View row) {

            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                cart.remove(viewHolder.getAdapterPosition());
                demoCartAdapter.notifyDataSetChanged();

                if (cart.numberOfItems() == 0) {
                    updateViewVisibility();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(productRecyclerView);

        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productRecyclerView.setAdapter(demoCartAdapter);
    }

    private void setupCheckoutButton() {
        cartCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BVTransaction transaction = cart.completeTransaction();
                BVTransactionEvent event = new BVTransactionEvent(transaction);
                BVPixel.getInstance().track(event);
                Toast toast = Toast.makeText(DemoCartActivity.this, "Successfully Checked Out", Toast.LENGTH_LONG);
                toast.show();
                updateViewVisibility();
            }
        });
    }

    private void updateViewVisibility() {
        if (cart.numberOfItems() > 0) {
            cartEmptyTextView.setVisibility(View.GONE);
        }else {
            cartEmptyTextView.setVisibility(View.VISIBLE);
            cartCheckoutButton.setVisibility(View.GONE);
        }
    }
    public static void transitionTo(Activity activity) {
        Intent intent = new Intent(activity, DemoCartActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
