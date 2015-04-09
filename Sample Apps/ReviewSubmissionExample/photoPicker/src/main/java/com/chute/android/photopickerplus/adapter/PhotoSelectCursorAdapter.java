package com.chute.android.photopickerplus.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplus.R;
import com.darko.imagedownloader.ImageLoader;

public class PhotoSelectCursorAdapter extends CursorAdapter implements OnScrollListener {

    public static final String TAG = PhotoSelectCursorAdapter.class.getSimpleName();

    private static LayoutInflater inflater = null;
    public ImageLoader loader;
    private final int dataIndex;
    public HashMap<Integer, String> tick;
    private boolean shouldLoadImages = true;
    private final DisplayMetrics displayMetrics;

    public PhotoSelectCursorAdapter(Context context, Cursor c) {
	super(context, c);
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	loader = ImageLoader.getLoader(context);
	dataIndex = c.getColumnIndex(MediaStore.Images.Media.DATA);
	displayMetrics = context.getResources().getDisplayMetrics();
	tick = new HashMap<Integer, String>();
    }

    public static class ViewHolder {
	public ImageView image;
	public ImageView tick;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
	ViewHolder holder = (ViewHolder) view.getTag();
	String path = cursor.getString(dataIndex);
	holder.image.setTag(path);
	holder.tick.setTag(cursor.getPosition());
	if (shouldLoadImages) {
	    loader.displayImage(Uri.fromFile(new File(path)).toString(), holder.image);
	} else {
	    loader.displayImage(null, holder.image);
	}
	holder.image.setLayoutParams(new RelativeLayout.LayoutParams(
		displayMetrics.widthPixels / 3 - 2, displayMetrics.widthPixels / 3 - 2));
	holder.image.setScaleType(ScaleType.CENTER_CROP);
	if (tick.containsKey(cursor.getPosition())) {
	    holder.tick.setVisibility(View.VISIBLE);
	    view.setBackgroundColor(context.getResources().getColor(R.color.orange));
	} else {
	    holder.tick.setVisibility(View.GONE);
	    view.setBackgroundColor(Color.BLACK);
	}
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	ViewHolder holder;
	View vi = inflater.inflate(R.layout.photos_select_adapter, null);
	holder = new ViewHolder();
	holder.image = (ImageView) vi.findViewById(R.id.imageViewThumb);
	holder.tick = (ImageView) vi.findViewById(R.id.imageTick);
	vi.setTag(holder);
	return vi;
    }

    @Override
    public String getItem(int position) {
	final Cursor cursor = getCursor();
	cursor.moveToPosition(position);
	return cursor.getString(dataIndex);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
	    int totalItemCount) {
	// Do nothing

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
	switch (scrollState) {
	case OnScrollListener.SCROLL_STATE_FLING:
	case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	    shouldLoadImages = false;
	    break;
	case OnScrollListener.SCROLL_STATE_IDLE:
	    shouldLoadImages = true;
	    notifyDataSetChanged();
	    break;
	}
    }

    public ArrayList<String> getSelectedFilePaths() {
	final ArrayList<String> photos = new ArrayList<String>();
	final Iterator<String> iterator = tick.values().iterator();
	while (iterator.hasNext()) {
	    photos.add(iterator.next());
	}
	return photos;
    }

    public boolean hasSelectedItems() {
	return tick.size() > 0;
    }

    public int getSelectedItemsCount() {
	return tick.size();
    }

    public void toggleTick(int position) {
	if (tick.containsKey(position)) {
	    tick.remove(position);
	} else {
	    tick.put(position, getItem(position));
	}
	notifyDataSetChanged();
    }
}
