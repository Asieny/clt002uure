package cn.com.mytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcw
 */
public abstract class AbsAdapter<T> extends BaseAdapter {
	private final List<T> EMPTY = new ArrayList<T>();
	private List<T> mData;
	private LayoutInflater mInflater;

	public AbsAdapter(Context context) {
		this(context, null);
	}

	public AbsAdapter(Context context, List<T> data) {
		mInflater = LayoutInflater.from(context);
		mData = data == null ? EMPTY : data;
	}

	public void setDataSource(List<T> data) {
		mData = data == null ? EMPTY : data;
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return mData;
	}

	protected LayoutInflater getInflater() {
		return mInflater;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
