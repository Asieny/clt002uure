package com.javis.Adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvis.mytaobao.Data.Book;
import com.guangxi.culturecloud.R;

public class Adapter_ListView_book extends BaseAdapter {
	private Context context;
	private List<Book> arrayList;

	public Adapter_ListView_book(Context context, List<Book> books) {

		this.context = context;
		this.arrayList = books;
	}

	public Adapter_ListView_book(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		return (arrayList != null && arrayList.size() == 0) ? 0 : arrayList
				.size();
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
	public View getView(final int position, View currentView, ViewGroup arg2) {
		HolderView holderView = null;
		if (currentView == null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(
					R.layout.adapter_listview_book, null);
			holderView.tv_name = (TextView) currentView
					.findViewById(R.id.tv_name);
			holderView.tv_address = (TextView) currentView
					.findViewById(R.id.tv_address);
			holderView.tv_time = (TextView) currentView
					.findViewById(R.id.tv_time);
			holderView.image = (ImageView) currentView.findViewById(R.id.img_face);
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}
		if (arrayList.size() != 0) {
			holderView.tv_name.setText(arrayList.get(position).getName());
			holderView.tv_address.setText(arrayList.get(position).getAddress());
			holderView.tv_time.setText(arrayList.get(position).getTime());
			holderView.image.setImageResource(arrayList.get(position)
					.getImage());

		}
		return currentView;
	}

	public class HolderView {
		private ImageView image;
		private TextView tv_address;
		private TextView tv_name;
		private TextView tv_time;

	}

}
