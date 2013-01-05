package com.blue.com;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	ArrayList<String> title_name_array;
	private LayoutInflater inflater=null;
	
	BaseAdapter adapter;
	Thread t;
	ListView lv;
	
	public LazyAdapter(Activity monitor,ArrayList<String> title_array,ListView lv)
	{
		activity=monitor;
		title_name_array=title_array;
		
		inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lv=lv;
		 inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		 
		
	}
	

	public int getCount() {
		// TODO Auto-generated method stub
		return title_name_array.size();
	}
	
	public ListView getListView(){
		return lv;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	
	public class ViewHolder
	{
		public TextView device_name;
	}

	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final View vi;
		final ViewHolder holder;
		
		vi=inflater.inflate(R.layout.list_item, null);
		
		holder=new ViewHolder();
		holder.device_name=(TextView)vi.findViewById(R.id.device_name);
		holder.device_name.setText(title_name_array.get(arg0));
		
		
			return vi;
		
	}


	
	
}
