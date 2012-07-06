package com.bazaarvoice.example.browseproducts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * MainActivity.java <br>
 * BrowseProductExample<br>
 * 
 * <p>
 * This is a simple product search screen. It allows the user to enter a search
 * string then passes control on to the next view.
 * 
 * <p>
 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class MainActivity extends Activity {

	private EditText searchField;

	/**
	 * Called when the activity is first created. Sets up listeners on the
	 * search field and button.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		searchField = (EditText) findViewById(R.id.searchField);
		searchField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					performSearch();
					return true;
				}
				return false;
			}

		});

		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				performSearch();
			}

		});
	}

	/**
	 * Launches the next activity and passes along the search term.
	 */
	protected void performSearch() {
		Intent intent = new Intent(this, ProductsActivity.class);
		intent.putExtra("searchTerm", searchField.getText().toString());
		startActivity(intent);
	}
}
