package toplistview.widget.zdh.com.toplistview;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;


public class StickyTopViewActitivy extends Activity{
	StickyTopListViewFinal mListView;
	FrameLayout mContainerLayout;
	MyAdapter mAdapter;

	boolean mIsInit = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setupView();
	}
	
	protected void setupView(){

		mAdapter = new MyAdapter();
		
		mContainerLayout = new FrameLayout(this);
		mContainerLayout.setBackgroundColor(getResources().getColor(R.color.holo_green_dark));
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lParams.gravity = Gravity.CENTER_HORIZONTAL;
		
		mListView = new StickyTopListViewFinal(this);
		mListView.setBackgroundColor(getResources().getColor(R.color.darker_gray));
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		
		
		mContainerLayout.addView(mListView);
		addContentView(mContainerLayout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	private class MyAdapter extends BaseAdapter {
		String[] datas = { "第一个", "置顶1", "第二个", "第三个","置顶2", "置顶3","第四个", "第五个", "置顶4","第6个", "置顶5","第7个", "第8个","置顶6", "第9个"};

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
			if (position == 1 || position == 4 || position == 5 || position == 8 || position == 10 || position == 13) {
				return 0;
			}else{
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 1 || position == 4 || position == 5 || position == 8 || position == 10 || position == 13) {
				
				final LinearLayout topViewImage = new LinearLayout(StickyTopViewActitivy.this);
				
				TextView myView;
				myView = new TextView(StickyTopViewActitivy.this);
				myView.setMinHeight(100+position*10);
				myView.setText(getItem(position).toString());
				topViewImage.addView(myView);
				topViewImage.setBackgroundResource(R.color.holo_green_light);
				
				StickyTopListViewFinal.EmptyView temConvertView = mListView.createEmptyView(position);
				temConvertView.setTag(position);
				
				mListView.insertTopView(topViewImage, temConvertView, mContainerLayout,position,null,null);
				
				convertView = temConvertView;
				return convertView;
			}else{
				TextView myView;
				if (convertView == null) {
					convertView = new LinearLayout(StickyTopViewActitivy.this);
					myView = new TextView(StickyTopViewActitivy.this);
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
