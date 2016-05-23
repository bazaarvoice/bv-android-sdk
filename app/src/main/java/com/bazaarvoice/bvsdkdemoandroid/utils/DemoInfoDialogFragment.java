/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bazaarvoice.bvsdkdemoandroid.R;

public class DemoInfoDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_MESSAGE = "arg_message";

    private String title;
    private String message;

    public DemoInfoDialogFragment() {}

    public static DemoInfoDialogFragment newInstance(String title, String message) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putString(ARG_MESSAGE, message);
        DemoInfoDialogFragment demoInfoDialogFragment = new DemoInfoDialogFragment();
        demoInfoDialogFragment.setArguments(arguments);
        return demoInfoDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(ARG_TITLE);
        message = getArguments().getString(ARG_MESSAGE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_info, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.okay, null);

        TextView messageTv = (TextView) view.findViewById(android.R.id.message);
        messageTv.setText(message);

        TextView titleTv = (TextView) view.findViewById(android.R.id.title);
        titleTv.setText(title);

        return builder.create();
    }

}
