package com.example.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class ReListView extends ListView implements OnScrollListener{
	
	private View headerView;
	private boolean hasOnLayout = false;
	private int headerHeight;
	private boolean isScrollToTop = false;
	private float preY = 0;
	
	public ReListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		headerView = LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
		headerView.measure(0, 0);
		headerHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -headerHeight, 0, 0);
		this.addHeaderView(headerView);
		
		this.setOnScrollListener(this);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(firstVisibleItem == 0){
			isScrollToTop = true;
		}else{
			isScrollToTop = false;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float downY = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(isScrollToTop){
				preY = downY;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(isScrollToTop){
				int paddingTop = (int) (downY - preY)/2;
				if(paddingTop < headerHeight){
					headerView.setPadding(0, paddingTop - headerHeight, 0, 0);
				}else{
					headerView.setPadding(0, 0, 0, 0);
				return true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isScrollToTop){
				int paddingTop = (int) (downY - preY)/2;
				if(paddingTop < headerHeight){
					headerView.setPadding(0, -headerHeight, 0, 0);
				}
			}
			break;
		default:
			break;
		}
		
		
		return super.onTouchEvent(event);
	}
}
