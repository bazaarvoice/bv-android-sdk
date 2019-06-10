package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


public class BVCustomViewTest extends BVBaseTest {

    private boolean called;

    @Before
    public void setUp() {
        called = false;
    }

    @Test
    public void bvContainerOnDrawShouldCalledOnAddedToViewHierarchy() {
        BVContainerView bvContainerView = new BVContainerView(ApplicationProvider.getApplicationContext().getApplicationContext()) {
            @Override
            void init() {
                super.init();
                called = true;
            }

            @Override
            public void onAddedToViewHierarchy() {
                super.onAddedToViewHierarchy();
                assertTrue(called);
            }
        };
        assertNotNull(bvContainerView);
        bvContainerView.onDraw(null);
    }

    @Test
    public void bvViewInitShouldBeCalled() {
        String productId = UUID.randomUUID().toString();
        BVView bvView = new BVView(ApplicationProvider.getApplicationContext().getApplicationContext()) {

            @Override
            void init() {
                super.init();
                called = true;
            }


            @Override
            public String getProductId() {
                return productId;
            }

            @Override
            public void onAddedToViewHierarchy() {
                super.onAddedToViewHierarchy();
                assertTrue(called);
            }
        };
        bvView.onDraw(null);
    }

    @Test
    public void bvViewOnTabShouldBeCalledWithActionUpEvent() {
        String productId = UUID.randomUUID().toString();
        BVView bvView = new BVView(ApplicationProvider.getApplicationContext().getApplicationContext()) {

            @Override
            void init() {
                super.init();
                called = true;
            }


            @Override
            public String getProductId() {
                return productId;
            }

            @Override
            public void onTap() {
                super.onTap();
                assertTrue(called);
            }
        };

        MotionEvent motionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0.0f, 0.0f, 0);
        bvView.dispatchTouchEvent(motionEvent);
    }

    @Test
    public void bvRecyclerViewOnTabShouldBeCalledWithActionUpEvent() {
        String productId = UUID.randomUUID().toString();
        BVRecyclerView bvView = new BVRecyclerView(ApplicationProvider.getApplicationContext().getApplicationContext()) {

            @Override
            void init(Context context, @Nullable AttributeSet attr) {
                super.init(context, attr);
                called = true;
            }

            @Override
            public String getProductId() {
                return productId;
            }

            @Override
            public void onAddedToViewHierarchy() {
                super.onAddedToViewHierarchy();
                assertTrue(called);
            }
        };

        bvView.onDraw(null);
        assertEquals(productId, bvView.getProductId());
    }

}

