package com.javis.Adapter;

import com.guangxi.culturecloud.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_GridView extends BaseAdapter {
private Context context;
private int[] data;
	private String[] names;
	public Adapter_GridView(Context context,int[] data,String[] names){
		
		this.context=context;
		this.data=data;
		this.names = names;
	}
	
	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		HolderView holderView=null;
		if (currentView==null) {
			holderView=new HolderView();
			currentView=LayoutInflater.from(context).inflate(R.layout.adapter_grid_home, null);
			holderView.iv_pic=(ImageView) currentView.findViewById(R.id.iv_adapter_grid_pic);
			holderView.tv_name = (TextView) currentView.findViewById(R.id.tv_adapter_grid_name);
			currentView.setTag(holderView);
		}else {
			holderView=(HolderView) currentView.getTag();
		}
		
		
		holderView.iv_pic.setImageResource(data[position]);
		holderView.tv_name.setText(names[position]);
		
		return currentView;
	}
	
	
	public class HolderView {
		
		private ImageView iv_pic;
		
		private TextView tv_name;
		
	}

}
