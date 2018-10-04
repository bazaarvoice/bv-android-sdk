/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bazaarvoice.bvsdkdemoandroid.R;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DemoSubmitDialogFragment extends DialogFragment {

    private static final String ARG_HINT = "arg_hint";

    private String hint;

    public DemoSubmitDialogFragment() {}

    public static DemoSubmitDialogFragment newInstance(String hint) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_HINT, hint);
        DemoSubmitDialogFragment demoSubmitDialogFragment = new DemoSubmitDialogFragment();
        demoSubmitDialogFragment.setArguments(arguments);
        return demoSubmitDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hint = getArguments().getString(ARG_HINT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_submit_review, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.submit_review, null)
                .setNegativeButton(R.string.cancel_review, null);

        EditText body = (EditText) view.findViewById(R.id.body);
        body.setHint(hint);

        return builder.create();
    }

}
