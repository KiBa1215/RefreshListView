package com.example.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ReListView extends ListView implements OnScrollListener{
	
	private OnListViewRefreshListener onRefreshListener;
	
	private View headerView;
	private int headerHeight;// the height of headerView
	private boolean isScrollToTop = false;
	private int firstVisibleItem = -1;
	private float preY = 0;// Y coordinate of the first tap 
	private int state = -1;// PULLING_DOWN | PULLED_DOWN
	private final int PULLING_DOWN = 0;
	private final int PULLED_DOWN = 1;
	private final int REFRESHING = 2;
	
	private TextView headerTextView;
	private ProgressBar headerProgressBar;
	private ImageView headerImageArrow;
	
	private RotateAnimation rotateUpAnimation, rotateDownAnimation;
	private int rotateState = -1;
	private final int ROTATE_UP = 3;
	private final int ROTATE_DOWN = 4;
	
	public ReListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		headerView = LayoutInflater.from(context).inflate(R.layout.listview_header_layout, null);
		headerTextView = (TextView)headerView.findViewById(R.id.listview_header_textview);
		headerProgressBar = (ProgressBar)headerView.findViewById(R.id.listview_header_progressbar);
		headerImageArrow = (ImageView)headerView.findViewById(R.id.listview_header_arrow);
		
		initArrowAnimation();
		
		headerView.measure(0, 0);
		headerHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -headerHeight, 0, 0);
		this.addHeaderView(headerView);
		
		this.setOnScrollListener(this);
	}

	private void initArrowAnimation(){
		rotateUpAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateUpAnimation.setDuration(500);
		rotateUpAnimation.setFillAfter(true);
		rotateDownAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateDownAnimation.setDuration(500);
		rotateDownAnimation.setFillAfter(true);
	}
	
	public void setOnRefreshListener(OnListViewRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		if(firstVisibleItem == 0){// to know if the listView is scrolled to top
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
				int deltaY = (int) (downY - preY)/2;
				int paddingTop = deltaY - headerHeight;
				if(this.firstVisibleItem == 0 && paddingTop < 0){
					state = PULLING_DOWN;
					// set text
					headerTextView.setText("Pull down to refresh");
					headerProgressBar.setVisibility(View.INVISIBLE);
					// make the arrow rotate 180°
					if(rotateState != ROTATE_DOWN){
						headerImageArrow.startAnimation(rotateDownAnimation);
						rotateState = ROTATE_DOWN;
					}
				}else if(this.firstVisibleItem == 0 && paddingTop > 0){
					state = PULLED_DOWN;
					headerTextView.setText("Release to refresh");
					// make the arrow rotate 180°
					if(rotateState != ROTATE_UP){
						headerImageArrow.startAnimation(rotateUpAnimation);
						rotateState = ROTATE_UP;
					}
				}
				headerView.setPadding(0, paddingTop, 0, 0);
				return super.onTouchEvent(event);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			if(isScrollToTop){
				if(state == PULLING_DOWN){
					headerView.setPadding(0, -headerHeight, 0, 0);
				}else if(state == PULLED_DOWN){
					headerView.setPadding(0, 0, 0, 0);
					headerTextView.setText("Refreshing");
					headerProgressBar.setVisibility(View.VISIBLE);
					state = REFRESHING;
					// on refresh
					if(this.onRefreshListener != null){
						this.onRefreshListener.onRefresh();
					}
				}
			}
			break;
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	
	
}
