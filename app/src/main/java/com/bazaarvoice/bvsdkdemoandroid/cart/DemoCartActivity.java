/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVPixel;
import com.bazaarvoice.bvandroidsdk.BVTransaction;
import com.bazaarvoice.bvandroidsdk.BVTransactionEvent;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductDatabase;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoCartActivity extends AppCompatActivity {

    @Inject
    DemoBVPersistableProductDatabase demoBVPersistableProductDatabase;

    @BindView(R.id.cart_recycler_view)
    RecyclerView productRecyclerView;
    @BindView(R.id.cartEmptyTextView) TextView cartEmptyTextView;
    @BindView(R.id.cartCheckoutButton) Button cartCheckoutButton;

    private DemoCartAdapter demoCartAdapter;
    private  DemoCart cart;
    private HandlerThread bgThread = new HandlerThread("database_thread");

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

        DemoApp.getAppComponent(this).inject(this);

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
        bgThread.start();
        Looper looper = bgThread.getLooper();
        Handler handler = new Handler(looper);
        cartCheckoutButton.setOnClickListener(view -> {
            for (int i = 0; i < cart.getProducts().size(); i++) {
                BVDisplayableProductContent productContent = cart.getProducts().get(i);
                handler.post(() -> {
                    DemoBVPersistableProductContent content =
                            new DemoBVPersistableProductContent(
                                    productContent.getId(),
                                    productContent.getDisplayName(),
                                    productContent.getDisplayImageUrl(),
                                    productContent.getAverageRating());
                    demoBVPersistableProductDatabase.demoBVPersistableProductContentDao().insert(content);
                });
            }
            BVTransaction transaction = cart.completeTransaction();
            BVTransactionEvent event = new BVTransactionEvent(transaction);
            BVPixel.getInstance().track(event);
            Toast toast = Toast.makeText(DemoCartActivity.this, "Successfully Checked Out", Toast.LENGTH_LONG);
            toast.show();
            updateViewVisibility();
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
    public static void transitionTo(Context activityContext) {
        Intent intent = new Intent(activityContext, DemoCartActivity.class);
        activityContext.startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgThread.quit();
    }
}
