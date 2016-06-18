package toplistview.widget.zdh.com.toplistview;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TabStickyTopViewActitivy extends Activity{
	StickyTopListViewFinal mListView;
	FrameLayout mContainerLayout;
	MyAdapter mAdapter;
	protected TabNavigatorView mNavigatorView;
	
	private int mTopViewPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setupView();
	}
	
	protected void setupView(){
		mAdapter = new MyAdapter();
		
		mNavigatorView = new TabNavigatorView(TabStickyTopViewActitivy.this);
		String []tabs = {"第一蓝","第二蓝","第三蓝"};
		mNavigatorView.setTabArray(tabs,0);
		mContainerLayout = new FrameLayout(this);
		mContainerLayout.setBackgroundColor(getResources().getColor(R.color.holo_green_dark));
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lParams.gravity = Gravity.CENTER_HORIZONTAL;
		
		mListView = new StickyTopListViewFinal(this);
		mListView.setBackgroundColor(getResources().getColor(R.color.darker_gray));
		mListView.setAdapter(mAdapter);
		
//		
		mAdapter.notifyDataSetChanged();
		
		
		mContainerLayout.addView(mListView);
		addContentView(mContainerLayout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	private StickyTopListViewFinal.OnTopViewStatusChangeListener onTopViewStatusChangeListener = new StickyTopListViewFinal.OnTopViewStatusChangeListener() {
		
		@Override
		public void onTopViewStatusChangeListener(int position, int oldStatus,
				int newStatus) {
			if (newStatus == StickyTopListViewFinal.STATUS_TOP) {
				mTopViewPosition = position;
			}
//			if (position == 6 && newStatus == StickyTopListView2.STATUS_TOP) {
//				mNavigatorView.setSelectedIndex(1);
//				Log.e("newStatus", ""+4);
//			}else if (position == 9 && newStatus == StickyTopListView2.STATUS_TOP) {
//				mNavigatorView.setSelectedIndex(2);
//				Log.e("newStatus", ""+8);
//			}else if (position == 2 && newStatus == StickyTopListView2.STATUS_TOP) {
//				mNavigatorView.setSelectedIndex(0);
//				Log.e("newStatus", ""+2);
//			}
		}
	};
	
private StickyTopListViewFinal.OnTopViewLayoutChangeListener mOnTopViewLayoutChangeListener = new StickyTopListViewFinal.OnTopViewLayoutChangeListener() {
        
        @Override
        public void onLayoutLocaitonChangeListener(int position, int status,
                MarginLayoutParams lp) {
                if (position == 5 && status == StickyTopListViewFinal.STATUS_FOLLOW) {
                    if (lp.topMargin < mNavigatorView.getHeight() && lp.topMargin >0) {
                    	if (mNavigatorView.getSelectedIndex() == 1) {
							return;
						}
                        mNavigatorView.setSelectedIndex(1);
                        Log.e("newStatus", ""+4+";"+"fllow");
                        return;
                    }
                }else if (position == 9 && status == StickyTopListViewFinal.STATUS_FOLLOW) {
                    if (lp.topMargin < mNavigatorView.getHeight() && lp.topMargin >0) {
                    	if (mNavigatorView.getSelectedIndex() == 2) {
							return;
						}
                        mNavigatorView.setSelectedIndex(2);
                        Log.e("newStatus", ""+8+";"+"fllow");
                        return;
                    }
                }else if (position == 2 && status == StickyTopListViewFinal.STATUS_FOLLOW) {
                    if (lp.topMargin < mNavigatorView.getHeight() && lp.topMargin >0) {
                    	if (mNavigatorView.getSelectedIndex() == 0) {
							return;
						}
                        mNavigatorView.setSelectedIndex(0);
                        Log.e("newStatus", ""+2+";"+"fllow");
                        return;
                    }
                }else if (mTopViewPosition == 5 && mNavigatorView.getSelectedIndex() != 1 && mListView.getNextTopViewTopMargin(5) > mNavigatorView.getHeight()) {
                	mNavigatorView.setSelectedIndex(1);
                	 Log.e("newStatus", ""+4+";"+"fllow");
				}else if (mTopViewPosition == 9 && mNavigatorView.getSelectedIndex() != 2) {
					mNavigatorView.setSelectedIndex(2);
					Log.e("newStatus", ""+8+";"+"fllow");
				}else if (mTopViewPosition == 2 && mNavigatorView.getSelectedIndex() != 0 && mListView.getNextTopViewTopMargin(2) > mNavigatorView.getHeight()) {
					mNavigatorView.setSelectedIndex(0);
					 Log.e("newStatus", ""+2+";"+"fllow");
				}
                
        }
    };
	
	private class MyAdapter extends BaseAdapter {
		String[] datas = { "第一个", "置顶1","ss", "导航", "第三个","置顶2", "置顶3","第四个", "第五个", "置顶4","第6个", "置顶5","第7个", "第8个","置顶6", "第9个"};

		public MyAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return datas[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 1 || position == 2 || position == 5 || position == 6 || position == 9 || position == 11 || position == 14) {
				return 0;
			}else if (position == 3) {
				return 1;
			}else{
				return 2;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getItemViewType(position) == 0) {
				
				final LinearLayout topViewImage = new LinearLayout(TabStickyTopViewActitivy.this);
				
				if (position != 2) {
					TextView myView;
					myView = new TextView(TabStickyTopViewActitivy.this);
					myView.setMinHeight(100+position*10);
					myView.setText(getItem(position).toString());
					topViewImage.addView(myView);
					topViewImage.setBackgroundResource(R.color.holo_green_light);
					topViewImage.setVisibility(View.INVISIBLE);
					topViewImage.setAlpha(0.5f);
					StickyTopListViewFinal.EmptyView temConvertView = mListView.createEmptyView(position);
					mListView.insertTopView(topViewImage, temConvertView, mContainerLayout,position,mOnTopViewLayoutChangeListener,onTopViewStatusChangeListener);
					convertView = temConvertView;
				}else{
					StickyTopListViewFinal.EmptyView temConvertView = mListView.createEmptyView(position);
					mListView.insertTopView(topViewImage, temConvertView, mContainerLayout,position,mOnTopViewLayoutChangeListener,onTopViewStatusChangeListener);
					convertView = temConvertView;
				}
				
				return convertView;
			}else if (getItemViewType(position) == 1) {
				StickyTopListViewFinal.EmptyView temConvertView = mListView.createEmptyView(position);
//				mNavigatorView.setAlpha(0.3f);
//				mNavigatorView.setVisibility(View.INVISIBLE);
				mListView.addTopView(mContainerLayout,mNavigatorView,temConvertView,position,null,null);
				return temConvertView;
			}else{
				TextView myView;
				if (convertView == null) {
					convertView = new LinearLayout(TabStickyTopViewActitivy.this);
					myView = new TextView(TabStickyTopViewActitivy.this);
					((LinearLayout)convertView).addView(myView);
					myView.setMinHeight(position*30);
					convertView.setTag(myView);
				} else {
					myView = (TextView) convertView.getTag();
					myView.setMinHeight(position*30);
				}
				myView.setText(getItem(position).toString());
				return convertView;
			}

		}

	}
}
