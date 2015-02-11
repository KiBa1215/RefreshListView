package com.example.refreshlistview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	ReListView listView;
	String[] str = {"123", "456", "789", "123", "456", "789", "123", "456", "789", "123", "456", "789", "123", "456", "789"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ReListView)findViewById(R.id.listView);
		ListAdapter adapter = new ListAdapter();
		listView.setAdapter(adapter);
		listView.setOnListViewRefreshListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnListViewRefreshListener listener = new OnListViewRefreshListener() {
		
		@Override
		public void onPullUpRefresh() {
			handler.sendEmptyMessageDelayed(234, 2000);
		}
		
		@Override
		public void onPullDownRefresh() {
			handler.sendEmptyMessageDelayed(123, 2000);
		}
	};
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 123){
				listView.hideHeaderView();
			}else if(msg.what == 234){
				listView.hideFooterView();
			}
		}
		
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class ListAdapter extends BaseAdapter{

		
		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = new TextView(MainActivity.this);
			textView.setText(str[position]);
			textView.setTextColor(Color.BLACK);
			textView.setTextSize(24f);
			textView.setHeight(150);
			return textView;
		}
		
	}
}
