package utils;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	
	
	/**
	 * Convert Dp to Pixel
	 */
	public static int dpToPx(float dp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}
	
	public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
	    if(myView.getId() == android.R.id.content)
	        return myView.getTop();
	    else
	        return myView.getTop() + getRelativeTop((View) myView.getParent());
	}
	
	public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
		if(myView.getId() == android.R.id.content)
			return myView.getLeft();
		else
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		//View view=listView.getChildAt(listView.getChildCount()-1);
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
