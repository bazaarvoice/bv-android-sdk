package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bazaarvoice.bvsdkdemoandroid.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoConvApiFragment extends Fragment implements DemoConvApiContract.View {
    private DemoConvApiContract.Presenter presenter;
    @BindView(R.id.requiredIdTitle)
    TextView requiredIdTitle;
    @BindView(R.id.requiredIdInput)
    EditText requiredIdInput;
    @BindView(R.id.methodInput)
    Spinner methodInputSpinner;
    @BindView(R.id.filterType)
    Spinner filterTypeSpinner;
    @BindView(R.id.filterValue)
    TextView filterValueTV;
    @BindView(R.id.filterLayout)
    View filterSelection;
    private ProgressDialog progress;
    private boolean initialCreatedClick = true;

    public DemoConvApiFragment() {
    }

    public static DemoConvApiFragment newInstance() {
        return new DemoConvApiFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_conversations_demo, container, false);
        ButterKnife.bind(this, view);
        progress = new ProgressDialog(getContext());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.conv_api_methods, R.layout.bv_spinner_item);
        methodInputSpinner.setAdapter(adapter);
        methodInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> convApiMethods = Arrays.asList(getContext().getResources().getStringArray(R.array.conv_api_methods));
                String apiMethod = convApiMethods.get(position);
                DemoConvApiContract.ConvApiMethod convApiMethod = DemoConvApiContract.ConvApiMethod.from(apiMethod);
                if(!initialCreatedClick) {
                    updateFilterViewVisibility(apiMethod);
                }
                initialCreatedClick = false;
                presenter.onApiMethodChanged(convApiMethod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        requiredIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onRequiredIdChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setupFilterView();
        return view;
    }

    private void updateFilterViewVisibility(String apiMethod) {
        if (!apiMethod.equals("Display Reviews - Primary Filter")) {
            filterSelection.setVisibility(View.GONE);
            requiredIdTitle.setVisibility(View.VISIBLE);
            requiredIdInput.setVisibility(View.VISIBLE);
        } else {
            filterSelection.setVisibility(View.VISIBLE);
            requiredIdTitle.setVisibility(View.GONE);
            requiredIdInput.setVisibility(View.GONE);
        }
    }

    private void setupFilterView() {
        filterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onFilterTypeChanged(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterValueTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onFilterValueChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(DemoConvApiContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.runMethodButton)
    public void onRunMethodButtonTapped() {
        presenter.onRunMethodTapped();
    }

    @Override
    public void showDialogWithNotConfiguredMessage(String displayName) {
        String errorMessage = String.format(
                getString(R.string.view_demo_error_message),
                displayName, getString(R.string.demo_conversations)
        );
        showDialogWithMessage(errorMessage);
    }

    @Override
    public void showDialogWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }

    @Override
    public void showProgressWithTitle(String title) {
        progress.setTitle(title);
        progress.show();
    }

    @Override
    public void hideProgress() {
        progress.hide();
    }

    @Override
    public void showRequiredIdTitle(String title) {
        requiredIdTitle.setText(title);
    }

}
