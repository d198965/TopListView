package toplistview.widget.zdh.com.toplistview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class StickyTopListViewFinal extends ListView implements
		AbsListView.OnScrollListener {
	public static final int STATUS_TOP = 0;
	public static final int STATUS_FOLLOW = 1;
	public static final int STATUS_PUSH_MOVE = 2;
	protected OnTopViewStatusChangeListener mOnTopViewStatusChangeListener;
	List<TopView> mTopViews;
	List<TopView> mSelfTopViews;
	public StickyTopListViewFinal(Context context) {
		this(context, null);
	}

	public StickyTopListViewFinal(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTopViews = new ArrayList<TopView>();
		mSelfTopViews = new  ArrayList<TopView>();
		setOnScrollListener(this);
	}

	public boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, 0, isTouchEvent);
	}

	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		if (scrollY < 0 && !clampedY && mTopViews != null
				&& !mTopViews.isEmpty()) {
			for (int k = mTopViews.size() -1; k >=0 ; k--) {
				mTopViews.get(k).setFllowEmptyViewLayoutParams(scrollY);
			}
			
			for (TopView topView : mSelfTopViews) {
				topView.setFllowEmptyViewLayoutParams(scrollY);
			}
		}
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mTopViews == null || mTopViews.isEmpty()) {
			return;
		}
		for (int k = mTopViews.size() -1; k >=0 ; k--) {
			mTopViews.get(k).setFllowEmptyViewLayoutParams(0);
		}
		
		for (TopView topView : mSelfTopViews) {
			topView.setFllowEmptyViewLayoutParams(0);
		}
	}	
	
	public EmptyView createEmptyView(int position){
		for (TopView topView : mTopViews) {
			if (topView.mPositon == position) {
				return topView.mEmptyView;
			}
		}
		
		for (TopView topView : mSelfTopViews) {
			if (topView.mPositon == position) {
				return topView.mEmptyView;
			}
		}
		
		EmptyView temConvertView = new EmptyView(getContext());
		return temConvertView;
	}
	
	/**
	 * 
	 * @param newTopView //需要置顶的控件
	 *
	 * @param emptyView //用于替换的空View
	 *
	 * @param containerLayout //ListView的父控件
	 *
	 * @param positon //数据的position
	 *
	 */
	public void insertTopView(final View newTopView, final EmptyView emptyView,
			final FrameLayout containerLayout, final int positon,OnTopViewLayoutChangeListener onTopViewLayoutChangeListener,OnTopViewStatusChangeListener onTopViewStatusChangeListener) {
		if (newTopView == null || positon < 0 || containerLayout == null
				|| emptyView == null || newTopView.getParent() != null) {
			return;
		}
		for (TopView temView : mTopViews) {
			if (temView.mPositon == positon) {
				return;
			}
		}
		emptyView.setBackgroundDrawable(newTopView.getBackground());
		final TopView topView = new TopView(getContext());
		topView.setEmptyView(emptyView);
		topView.mPositon = positon;
		topView.setOnTopViewLayoutChangeListner(onTopViewLayoutChangeListener);
		topView.setOnTopViewStatusChangeListener(onTopViewStatusChangeListener);
		topView.addView(newTopView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		
		FrameLayout.LayoutParams topViewParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		if (mTopViews.size() == 0) {
			containerLayout.addView(topView,containerLayout.getChildCount(), topViewParams);
			mTopViews.add(topView);
		}else{
			boolean isFind = false;
			for (int i = 0; i < mTopViews.size(); i++) {
				if (mTopViews.get(i).mPositon == positon) {
					return;//已经存在
				}
				else if (mTopViews.get(i).mPositon > positon) {
					isFind = true;
					topView.setNextView(mTopViews.get(i));
					if (i > 0) {
						mTopViews.get(i-1).setNextView(topView);
					}
					mTopViews.add(i,topView);
					containerLayout.addView(topView,i+1, topViewParams);
					isFind = true;
					break;
				}
			}
			
			//没找到，加到最后
			if (!isFind) {
				topView.setNextView(null);
				if (mTopViews.size() > 0) {
					mTopViews.get(mTopViews.size() - 1).setNextView(topView);
				}
				mTopViews.add(mTopViews.size(),topView);
				containerLayout.addView(topView,containerLayout.getChildCount(), topViewParams);
			}
		}
		
		topView.getViewTreeObserver().dispatchOnGlobalLayout();
		emptyView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (topView.mIsInit) {
					return;
				}
				if (emptyView.getParent() == StickyTopListViewFinal.this) {
					topView.setFllowEmptyViewLayoutParams(0);	
					topView.mIsInit = true;
				}
			}
		});
	}

	public void addTopView(final FrameLayout containerLayout,final View newTopView,final EmptyView emptyView,int position,OnTopViewLayoutChangeListener onTopViewLayoutChangeListener,OnTopViewStatusChangeListener onTopViewStatusChangeListener){
		for (int i = 0; i < mSelfTopViews.size(); i++) {
			if (mSelfTopViews.get(i).mPositon == position) {
				return;
			}
		}
		final TopView topView = new TopView(getContext());
		topView.addView(newTopView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		topView.mPositon = position;
		topView.setOnTopViewLayoutChangeListner(onTopViewLayoutChangeListener);
		topView.setOnTopViewStatusChangeListener(onTopViewStatusChangeListener);
		topView.setEmptyView(emptyView);
		mSelfTopViews.add(topView);
		FrameLayout.LayoutParams topViewParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		containerLayout.addView(topView,containerLayout.getChildCount(), topViewParams);

		topView.getViewTreeObserver().dispatchOnGlobalLayout();
		emptyView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (topView.mIsInit) {
					return;
				}
				if (emptyView.getParent() == StickyTopListViewFinal.this) {
					MarginLayoutParams lParams = topView.getMarginParams();
					lParams.topMargin = emptyView.getTop();
					topView.setLayoutParams(topView.getLayoutParams());	
					topView.mIsInit = true;
				}
			}
		});
	}
	
	public int getTopViewSize() {
		return mTopViews.size();
	}

	public int getTopViewStatus(int position){
		for (int i = 0; i < mTopViews.size(); i++) {
			if (mTopViews.get(i).mPositon == position) {
				return mTopViews.get(i).status;
			}
		}
		return -1;
	}
	
	public int getTopViewTopMargin(int position){
		for (int i = 0; i < mTopViews.size(); i++) {
			if (mTopViews.get(i).mPositon == position) {
				return mTopViews.get(i).getMarginParams().topMargin;
			}
		}
		return Integer.MIN_VALUE;
	}
	
	public int getNextTopViewTopMargin(int position){
		for (int i = 0; i < mTopViews.size(); i++) {
			if (mTopViews.get(i).mPositon == position) {
				if (mTopViews.get(i).mNextView != null) {
					return mTopViews.get(i).mNextView.getMarginParams().topMargin;
				}
			}
		}
		return Integer.MIN_VALUE;
	}
	
	public static class EmptyView extends View{
		protected TopView mTopView;
		public EmptyView(Context context) {
			this(context, null);
		}

		public EmptyView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
		}
	}
	
	private class TopView extends LinearLayout {
		private Integer status = STATUS_FOLLOW;
		
		public int mPositon;
		public boolean mIsInit =false;
		
		private TopView mNextView;
		private EmptyView mEmptyView;
		private List<OnTopViewLayoutChangeListener> mOnTopViewLayoutChangeListeners;
		private OnTopViewStatusChangeListener mOnTopViewStatusChangeListener;
		
		public TopView(Context context) {
			this(context, null);
		}

		public TopView(Context context, AttributeSet attrs) {
			super(context, attrs);
			mOnTopViewLayoutChangeListeners = new ArrayList<OnTopViewLayoutChangeListener>();
		}

		public void setOnTopViewLayoutChangeListner(
				OnTopViewLayoutChangeListener onTopViewLayoutChangeListener) {
			if (onTopViewLayoutChangeListener != null && mOnTopViewLayoutChangeListeners.indexOf(onTopViewLayoutChangeListener)<0) {
				mOnTopViewLayoutChangeListeners.add(onTopViewLayoutChangeListener);
			}
		}
		
		public void setOnTopViewStatusChangeListener(OnTopViewStatusChangeListener onStatusChangeListener){
			mOnTopViewStatusChangeListener = onStatusChangeListener;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int height = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.UNSPECIFIED);
			super.onMeasure(widthMeasureSpec, height);
		}
		
		public void setStatus(int newStatus){
			if (status == newStatus) {
				return;
			}
			//处理状态跨越问题
			if ((newStatus == STATUS_FOLLOW && status == STATUS_PUSH_MOVE)||(newStatus == STATUS_PUSH_MOVE && status == STATUS_FOLLOW)) {
				if (mOnTopViewStatusChangeListener != null) {
					mOnTopViewStatusChangeListener.onTopViewStatusChangeListener(mPositon, status, STATUS_TOP);
				}
				status = STATUS_TOP;
			}
			
			if (mOnTopViewStatusChangeListener != null) {
				mOnTopViewStatusChangeListener.onTopViewStatusChangeListener(mPositon, status, newStatus);
			}
			this.status = newStatus;
		}
		
		@Override
		public void setVisibility(int visibility) {
			mEmptyView.setVisibility(View.INVISIBLE);
			super.setVisibility(visibility);
		}

		public void setEmptyView(EmptyView emptyView) {
			mEmptyView = emptyView;
			getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							TopView.this.measure(getMeasuredWidthAndState(), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
							if (mEmptyView.getMeasuredHeight() != TopView.this.getMeasuredHeight()) {
								AbsListView.LayoutParams lParams = null;
								if (mEmptyView.getLayoutParams() == null) {
									lParams = new AbsListView.LayoutParams(
											AbsListView.LayoutParams.MATCH_PARENT,
											TopView.this.getMeasuredHeight());
								}else{
									lParams = (AbsListView.LayoutParams)mEmptyView.getLayoutParams();
									lParams.height = TopView.this.getMeasuredHeight();
								}
								mEmptyView.setLayoutParams(lParams);
								int emptyHeight = MeasureSpec.makeMeasureSpec(lParams.height, MeasureSpec.EXACTLY);
								mEmptyView.measure(mEmptyView.getMeasuredWidthAndState(), emptyHeight);
							}
						}
					});
		}
	
		public void setFllowEmptyViewLayoutParams(int scrollY) {
			if (mEmptyView == null) {
				return;
			}
			
			int firstPosition = StickyTopListViewFinal.this.getFirstVisiblePosition();
			boolean isUpListView = firstPosition >= mPositon;
			if (!isUpListView && status == STATUS_TOP) {
				setStatus(STATUS_FOLLOW);
				setLayoutParams(getLayoutParams());
			} else if (isUpListView) {
				if (mNextView != null && mNextView.mPositon < firstPosition
						&& (status != STATUS_PUSH_MOVE || getMarginParams().topMargin > -getMeasuredHeight())) {
					setStatus(STATUS_PUSH_MOVE);
					getMarginParams().topMargin = -getMeasuredHeight();
					setLayoutParams(getLayoutParams());
				}else if(status == STATUS_FOLLOW){
					setStatus(STATUS_TOP);
					getMarginParams().topMargin = 0;
					setLayoutParams(getLayoutParams());
				}else if (mNextView != null && mNextView.mPositon == firstPosition
						&& (status != STATUS_PUSH_MOVE)) {
					setStatus(STATUS_PUSH_MOVE);
					getMarginParams().topMargin = mNextView.getMarginParams().topMargin - getMeasuredHeight();
					setLayoutParams(getLayoutParams());
				}
				return;
			}

			if (status != STATUS_FOLLOW) {
				return;
			}
			int lastPositon = StickyTopListViewFinal.this.getLastVisiblePosition();
			int marginTop = 0;
			if (lastPositon < mPositon) {
				View lastVisibleView = StickyTopListViewFinal.this.getChildAt(StickyTopListViewFinal.this.getChildCount() -1);
				marginTop = lastVisibleView.getTop() + lastVisibleView.getMeasuredHeight();
			}else{
				marginTop = mEmptyView.getTop() -scrollY;
			}
			MarginLayoutParams lParams = (MarginLayoutParams) getLayoutParams();
			if (lParams.topMargin !=  marginTop) {
				lParams.topMargin = marginTop;
				setLayoutParams(lParams);
			}
		}

		@Override
		public void setLayoutParams(ViewGroup.LayoutParams params) {
			if (params != null
					&& mOnTopViewLayoutChangeListeners.size() >0) {
				for (int i = 0; i < mOnTopViewLayoutChangeListeners.size(); i++) {
					mOnTopViewLayoutChangeListeners.get(i)
					.onLayoutLocaitonChangeListener(mPositon,status,((MarginLayoutParams) params));
				}
				
			}
		
			super.setLayoutParams(params);
		}
		
		public MarginLayoutParams getMarginParams(){
			if(getLayoutParams() != null){
				return (MarginLayoutParams)getLayoutParams();
			}else{
				return null;
			}
		}

		public void setNextView(TopView nextView) {
			if (nextView == null) {
				mNextView = null;
				return;
			}
			mNextView = nextView;
			mNextView.setOnTopViewLayoutChangeListner(
					new OnTopViewLayoutChangeListener() {
						@Override
						public void onLayoutLocaitonChangeListener(int topViewPosition,int topViewStatus,
								MarginLayoutParams lp) {
							if ((lp.topMargin <= getMeasuredHeight()) && status == STATUS_TOP) {
								if (status != STATUS_PUSH_MOVE) {
									if (mOnTopViewStatusChangeListener != null) {
										mOnTopViewStatusChangeListener.onTopViewStatusChangeListener(mPositon, status, STATUS_PUSH_MOVE);
									}
								}
								MarginLayoutParams lParams = (MarginLayoutParams)getLayoutParams();
								lParams.topMargin = lp.topMargin-getMeasuredHeight();
								setLayoutParams(lParams);
								setStatus(STATUS_PUSH_MOVE);
							}else if (status == STATUS_PUSH_MOVE) {
								MarginLayoutParams lParams = (MarginLayoutParams)getLayoutParams();
								lParams.topMargin = lp.topMargin - getMeasuredHeight();
								int firstVisiblePosition = StickyTopListViewFinal.this.getFirstVisiblePosition();
								if (lParams.topMargin >= 0 && (firstVisiblePosition > mPositon || 
										(firstVisiblePosition == mPositon && mEmptyView.getTop() < 0))) {
									setStatus(STATUS_TOP);
									((MarginLayoutParams) getLayoutParams()).topMargin = 0;
								}else if(lParams.topMargin >= 0 && (firstVisiblePosition < mPositon ||
										(firstVisiblePosition == mPositon &&mEmptyView.getTop() >= 0))){
									setStatus(STATUS_FOLLOW);
									((MarginLayoutParams) getLayoutParams()).topMargin = mEmptyView.getTop();
								}
								setLayoutParams(lParams);
							}
						}
					});
		}

	}

	public interface OnTopViewLayoutChangeListener {
		void onLayoutLocaitonChangeListener(int preTopViewPosition, int preTopViewStatus, MarginLayoutParams lp);
	}
	
	public interface OnTopViewStatusChangeListener{
		void onTopViewStatusChangeListener(int position, int oldStatus, int newStatus);
	}
}
