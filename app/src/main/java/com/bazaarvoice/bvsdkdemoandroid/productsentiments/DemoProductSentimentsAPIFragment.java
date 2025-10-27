package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;

public class DemoProductSentimentsAPIFragment extends Fragment {

    private DemoRouter router;
           private String productId = "";

    /**
     * Factory method to create a new instance of this fragment.
     * @return A new instance of fragment DemoProductSentimentsAPIFragment.
     */
    public static DemoProductSentimentsAPIFragment newInstance() {
        return new DemoProductSentimentsAPIFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Note: Using the layout from the original Kotlin class.
        // You may want to rename this layout to match the new class name.
        return inflater.inflate(R.layout.frag_progressive_submission_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // It's safer to use getContext() within or after onViewCreated.
        if (getContext() != null) {
            router = new DemoRouter(getContext());
        }

        EditText productIdInput = view.findViewById(R.id.input_productid);
        productIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    productId = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        Button loadDemoBtn = view.findViewById(R.id.btnProgressiveSubmissionDemoLoad);
        loadDemoBtn.setOnClickListener(v -> {
            // Check for null before using the router, equivalent to Kotlin's '?.' safe call
            if (router != null) {
                router.transitionToProductSentimentsActivity(productId);
            }
        });
    }
}
