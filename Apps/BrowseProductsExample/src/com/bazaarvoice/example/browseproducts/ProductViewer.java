package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.RequestType;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductViewer extends Activity {
	
	public static final String TAG = "ProductViewer";

	private Context context = this;

	private final int PRODUCT_SEARCH = 0;
	private final int PRODUCT_CHOOSE = 1;
	
	private int state;
	
	private ArrayList<BazaarProduct> products;
	public BazaarProduct selectedProduct;
	
	private Spinner sort;
	private Spinner arrange;
	private Button searchButton;
	private EditText searchField;
	private SlidingDrawer drawer;
	private ProgressBar progress;
	private TextView searchText;
	private TextView sortText;
	private TextView arrangeText;
	private TextView loadingText;
	private TextView reviewText;
	private TextView searchFailText;
	
	private ListView reviewList;
	private ReviewAdapter adapter;
	private RelativeLayout middleField;
	private LinearLayout productScrollView;

	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        BazaarFunctions.setViewer(this);
        
        products = new ArrayList<BazaarProduct>();
        selectedProduct = new BazaarProduct();
        
        initializeViews();
        initializeReviewList();
        
        setUpSortSpinners();
        setUpSearchButton();
        state = PRODUCT_SEARCH;
    }
    
	private void initializeViews() {
		middleField = (RelativeLayout) findViewById(R.id.middleField);
		
		searchButton = (Button) findViewById(R.id.searchButton);
		searchField = (EditText) findViewById(R.id.searchField);
		drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		sort = (Spinner) findViewById(R.id.sortSpinner);
		arrange = (Spinner) findViewById(R.id.arrangeSpinner);
		progress = (ProgressBar) findViewById(R.id.progressBar);
		searchText = (TextView) findViewById(R.id.searchText);
		sortText = (TextView) findViewById(R.id.sortText);
		arrangeText = (TextView) findViewById(R.id.arrangeText);
		loadingText = (TextView) findViewById(R.id.loadingText);
		productScrollView = (LinearLayout) findViewById(R.id.productScrollView);
		reviewText = (TextView) findViewById(R.id.reviewText);
		searchFailText = (TextView) findViewById(R.id.searchFailText);
		
		
		/*
		 * Listener on search field to switch button from "Browse" to "Search"
		 */
		searchField.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0)
					searchButton.setText("Browse");
				else
					searchButton.setText("Search");
			}
			
		});
	}
    
    private void initializeReviewList() {
		reviewList = (ListView) findViewById(R.id.list);
		adapter = new ReviewAdapter(this);
		reviewList.setAdapter(adapter);
		
	}

    /*
     * Override to allow using back button to navigate back through activity
     * (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
	@Override
    public void onBackPressed(){
    	if(state == PRODUCT_SEARCH)
    		super.onBackPressed();
    	else{
    		setProductSearchView();
    		drawer.animateClose();
    	}
    		
    }

	private void setUpSearchButton() {
		searchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				state = PRODUCT_CHOOSE;
				productScrollView.removeAllViews();
				products.clear();
				setLoadingView();
				drawer.animateOpen();
				runSearchQuery();
			}
			
		});
		
	}
	
	private void setUpSortSpinners() {
		sort.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> a, View v,
					int selected, long arg3) {
				if(selected == 0){
					ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.DisabledArrange, android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					arrange.setAdapter(adapter);
				}
				else{
					ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.EnabledArrange, android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					arrange.setAdapter(adapter);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
			
		});
	}
	
	protected void runSearchQuery() {
		String searchPhrase = searchField.getText().toString();
		
		//Add sort to params if applicable
		String sortType = "";
		boolean ascending = false;
		switch(sort.getSelectedItemPosition()){
		case 1:
			sortType = "AverageOverallRating";
			ascending = (arrange.getSelectedItemPosition() == 0);
			break;
		case 2:
			sortType = "LastReviewTime";
			ascending = (arrange.getSelectedItemPosition() == 0);
			break;
		case 3:
			sortType = "TotalReviewCount";
			ascending = (arrange.getSelectedItemPosition() == 0);
			break;
		default:
			break;
		}
		
		BazaarFunctions.runProductSearchQuery(searchPhrase, sortType, ascending);
	}

	/*
	 * View Manipulation functions
	 */
	
	protected void setProductSearchView() {
		state = PRODUCT_SEARCH;
		
		//main field where controls/components go
		middleField.setVisibility(View.VISIBLE);
		
		//controls for search view
		searchButton.setEnabled(true);
		searchField.setVisibility(View.VISIBLE);
		sort.setVisibility(View.VISIBLE);
		arrange.setVisibility(View.VISIBLE);
		searchText.setVisibility(View.VISIBLE);
		sortText.setVisibility(View.VISIBLE);
		sortText.setVisibility(View.VISIBLE);
		arrangeText.setVisibility(View.VISIBLE);
		
		//components for loading view
		progress.setVisibility(View.INVISIBLE);
		loadingText.setVisibility(View.INVISIBLE);
		reviewList.setVisibility(View.INVISIBLE);
		
		//components for post-search display
		reviewText.setVisibility(View.INVISIBLE);
		searchFailText.setVisibility(View.INVISIBLE);
	}

	protected void setLoadingView() {
		state = PRODUCT_CHOOSE;
		
		//main field where controls/components go
		middleField.setVisibility(View.VISIBLE);
		
		//controls for search view
		searchButton.setEnabled(false);
		searchField.setVisibility(View.INVISIBLE);
		sort.setVisibility(View.INVISIBLE);
		arrange.setVisibility(View.INVISIBLE);
		searchText.setVisibility(View.INVISIBLE);
		sortText.setVisibility(View.INVISIBLE);
		sortText.setVisibility(View.INVISIBLE);
		arrangeText.setVisibility(View.INVISIBLE);
		reviewText.setVisibility(View.INVISIBLE);
		
		//components for post-search display
		reviewText.setVisibility(View.INVISIBLE);
		searchFailText.setVisibility(View.INVISIBLE);
		
		//components for loading view
		progress.setVisibility(View.VISIBLE);
		loadingText.setVisibility(View.VISIBLE);
	}
	
	protected void setReviewsView(){
		//main field where controls/components go
		middleField.setVisibility(View.INVISIBLE);
		
		//scrolls to top of list view
		reviewList.setSelection(0);
		reviewList.setVisibility(View.VISIBLE);
	}
	
	protected void setSearchFailView() {
		//components for loading view
		progress.setVisibility(View.INVISIBLE);
		loadingText.setVisibility(View.INVISIBLE);
		
		searchFailText.setVisibility(View.VISIBLE);
	}
	
	protected void clearScrollViewHighlights(){
		for (int i = 0; i < productScrollView.getChildCount(); i++){
			productScrollView.getChildAt(i).setBackgroundColor(Color.WHITE);
		}
	}
	
	
	
	/*
	 * Response helper classes
	 */
	
	protected class OnBazaarProductResponse implements OnBazaarResponse{

		@Override
		public void onException(String message, Throwable exception) {
			Log.e(TAG, "Error = " + message + "\n" + Log.getStackTraceString(exception));
		}

		@Override
		public void onResponse(JSONObject json) {
			Log.i(TAG, "Response = \n" + json);
			try {
				JSONArray results = json.getJSONArray("Results");
				
				if (results.length() == 0){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							searchFailText.setText("No products found");
							setSearchFailView();
						}
						
					});
					return;
				}
				
				for (int i = 0; i < results.length(); i++){
					final BazaarProduct newProduct = new BazaarProduct(results.getJSONObject(i));
					products.add(newProduct);
					
					final int curPos = i;
					newProduct.downloadImage(new OnImageDownloadComplete(){

						@Override
						public void onFinish(Bitmap image) {
							final ImageView insert = new ImageView(getApplicationContext());
							insert.setImageBitmap(Bitmap.createScaledBitmap(image, 120, 120, false));
							insert.setScaleType(ScaleType.CENTER);
							insert.setAdjustViewBounds(true);
							insert.setPadding(1, 1, 1, 1);
							
							insert.setTag(newProduct);
							
							insert.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View v) {
									if(selectedProduct.equals(v.getTag()))
										return;
									
									clearScrollViewHighlights();
									v.setBackgroundColor(Color.CYAN);
									
									reviewList.setVisibility(View.GONE);
									setLoadingView();
									
									selectedProduct = (BazaarProduct) v.getTag();
									adapter.notifyDataSetChanged();
									selectedProduct.downloadReviews(new OnBazaarReviewResponse(selectedProduct));
								}
								
							});
							
							
							
							//spin and wait for images to come in for results before this one
							while(productScrollView.getChildCount() < curPos){
								continue;
							}
							
							//change UI when first image comes in
							runOnUiThread(new Runnable(){

								@Override
								public void run() {
									productScrollView.addView(insert, curPos);
									if(productScrollView.getChildCount() == 1){
										progress.setVisibility(View.INVISIBLE);
										loadingText.setVisibility(View.INVISIBLE);
										
										reviewText.setVisibility(View.VISIBLE);
									}
								}
							});
						}
					});
				}
			} catch (JSONException exception) {
				Log.e(TAG, "Error = " + exception.getMessage() + "\n" + Log.getStackTraceString(exception));
			}
		}
	}

	protected class OnBazaarReviewResponse implements OnBazaarResponse{
		
		private BazaarProduct myProduct;

		public OnBazaarReviewResponse(BazaarProduct selectedProduct) {
			myProduct = selectedProduct;
		}

		@Override
		public void onException(String message, Throwable exception) {
			Log.e(TAG, "Error = " + message + "\n" + Log.getStackTraceString(exception));
			
		}

		@Override
		public void onResponse(JSONObject json) {
			Log.i(TAG, "Response = \n" + json);
			try{
				JSONArray results = json.getJSONArray("Results");
				
				if (results.length() == 0){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							searchFailText.setText("No reviews found");
							setSearchFailView();
						}
						
					});
					return;
				}
				
				for (int i = 0; i < results.length(); i++){
					final BazaarReview newReview = new BazaarReview(results.getJSONObject(i));
					
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							myProduct.addReview(newReview);
							myProduct.updateAvgRating();
							adapter.notifyDataSetChanged();
							setReviewsView();
						}
						
					});
					
					newReview.downloadImage(new OnImageDownloadComplete(){

						@Override
						public void onFinish(Bitmap image) {
							runOnUiThread(new Runnable(){

								@Override
								public void run() {
									adapter.notifyDataSetChanged();
								}
								
							});
						}
					});
				}
			}
			catch (JSONException exception){
				Log.e(TAG, "Error = " + exception.getMessage() + "\n" + Log.getStackTraceString(exception));
			}
		}
		
	}
		
	

	
	/*
	 * Custom adapter for generating the reviews list
	 */
	
	protected class ReviewAdapter extends BaseAdapter{
		
		private ArrayList<BazaarReview> reviews;
		private LayoutInflater inflater;
		private Context context;
		
		public ReviewAdapter(Context c){
			context = c;
			reviews = new ArrayList<BazaarReview>();
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			//+1 for header
			return reviews.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			reviews = selectedProduct.getReviews();
			
			//Header
			if(position == 0){
				if (convertView == null || ((ViewHolder) convertView.getTag()).avgRatingStars == null){
					convertView = inflater.inflate(R.layout.review_header, null);
					holder = new ViewHolder();
					holder.prodTitle = (TextView) convertView.findViewById(R.id.prodTitle);
					holder.avgRatingStars = (LinearLayout) convertView.findViewById(R.id.avgRatingStars);
					holder.numReviews = (TextView) convertView.findViewById(R.id.numReviews);
					holder.description = (TextView) convertView.findViewById(R.id.description);
					
					convertView.setTag(holder);
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}
				
				holder.avgRatingStars.removeAllViews();
				for (int i = 0; i < selectedProduct.getAverageRating(); i++){
					ImageView star = new ImageView(context);
					star.setImageResource(R.drawable.graphic_star_green);
					holder.avgRatingStars.addView(star);
				}
				
				holder.prodTitle.setText(selectedProduct.getName());
				holder.numReviews.setText(reviews.size() + ((reviews.size() > 1) ? " reviews" : " review") );
				holder.description.setText(selectedProduct.getDescription());
			}
			//Any normal review
			else{
				if (convertView == null || ((ViewHolder) convertView.getTag()).ratingStars == null){
					convertView = inflater.inflate(R.layout.review_item, null);
					holder = new ViewHolder();
					holder.ratingStars = (LinearLayout) convertView.findViewById(R.id.ratingStars);
					holder.title = (TextView) convertView.findViewById(R.id.title);
					holder.byLine = (TextView) convertView.findViewById(R.id.byLine);
					holder.review = (TextView) convertView.findViewById(R.id.review);
					holder.reviewIcon = (ImageView) convertView.findViewById(R.id.reviewIcon);
					
					convertView.setTag(holder);
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}
				
				try{
					holder.ratingStars.removeAllViews();
					for (int i = 0; i < reviews.get(position -1).getRating(); i++){
						ImageView star = new ImageView(context);
						star.setImageResource(R.drawable.graphic_star_green);
						holder.ratingStars.addView(star);
					}
					
					holder.title.setText(reviews.get(position -1).getTitle());
					
					String author = reviews.get(position -1).getAuthorId();
					String date = "on " + reviews.get(position -1).getDateString();
					holder.byLine.setText(Html.fromHtml("By <font color = \"#000000\"><b>" + author + "</font></b> " + date));
					
					holder.review.setText(reviews.get(position -1).getReviewText());
					holder.reviewIcon.setImageBitmap(reviews.get(position -1).getImageBitmap());
				}
				catch (IndexOutOfBoundsException exception){
					Log.w("ReviewResponse", Log.getStackTraceString(exception));
				}
			}
			return convertView;
		}
		
		class ViewHolder {
			TextView prodTitle;
			LinearLayout avgRatingStars;
            TextView numReviews;
            TextView description;
            
            LinearLayout ratingStars;
            TextView title;
            TextView byLine;
            TextView review;
            ImageView reviewIcon;
        }
		
	}
}
