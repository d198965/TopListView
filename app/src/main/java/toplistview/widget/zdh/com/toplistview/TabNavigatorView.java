package toplistview.widget.zdh.com.toplistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabNavigatorView extends LinearLayout{
	private int selectedIndex;
	public TabNavigatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabNavigatorView(Context context) {
		this(context,null);
		setMinimumHeight(300);
		setOrientation(HORIZONTAL);
		setBackgroundResource(R.color.colorAccent);
	}
	
	public void setTabArray(String[] tabs,int selectedIndex){
		for(int k=0;k<tabs.length;k++){
			String textString = tabs[k];
			View temView = LayoutInflater.from(getContext()).inflate(R.layout.tab_item,null);
			((TextView)temView.findViewById(R.id.tab_item)).setText(textString);
			if (selectedIndex == k) {
				temView.setSelected(true);
			}else{
				temView.setSelected(false);
			}
			LayoutParams temLayoutParams = new LayoutParams(0,LayoutParams.MATCH_PARENT);
			temLayoutParams.weight = 1;
			addView(temView,temLayoutParams);
		}
	}
	
	public void setSelectedIndex(int selectedIndex){
		this.selectedIndex = selectedIndex;
		for(int k=0;k<getChildCount();k++){
			if(k == selectedIndex){
				getChildAt(k).setSelected(true);
			}else{
				getChildAt(k).setSelected(false);
			}
		}
	}
	
	public int getSelectedIndex(){
		return selectedIndex;
	}
	
}
