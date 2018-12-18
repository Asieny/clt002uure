package cn.com.mytest.framework;

import java.util.List;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import cn.com.mytest.adapter.AbsAdapter;
import cn.com.mytest.anno.Injector;

/**
 * @author wangcw
 */
public abstract class AbsListViewFragment<V extends AbsListView, D, A extends AbsAdapter<D>> extends AbsFragment {
	private V mListView;
	private A mAdapter;

	public void updateListData(List<D> data) {
		getAdapter().setDataSource(data);
	}

	@SuppressWarnings("unchecked")
	protected V getListView() {
		if (mListView == null) {
			mListView = (V)getCreatedView().findViewById(listViewId());
		}
		return mListView;
	}

	protected A getAdapter() {
		if (mAdapter == null) {
			mAdapter = newAdapter();
			//不要重复调用setAdapter(), 否则会滚动到开头，而最好的办法就是在创建的时候set
			((AdapterView<ListAdapter>)getListView()).setAdapter(mAdapter);
		}
		return mAdapter;
	}

	private int listViewId() {
		return Injector.listViewID(getClass());
	}

	protected abstract A newAdapter();
}
