package cn.com.mytest.framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.mytest.anno.Injector;
import cn.com.mytest.framework.EventDelegater.EventReceiver;
import cn.com.mytest.framework.EventDelegater.PeriodMode;
import cn.com.mytest.widget.CustomProgressDialog;

/**
 * @author wangcw
 */
public class AbsFragment extends Fragment {
	private View mCreatedView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mCreatedView = inflater.inflate(Injector.layoutID(getClass()), container, false);
		Injector.inject(this, mCreatedView, AbsFragment.class);

		return mCreatedView;
	}

/*@Override
	public View getView() {
	}*/

	/**注意父类有个{@link #getView()}**/
	public View getCreatedView() {
		return mCreatedView;
	}

	public void sendLocalEvent(String eventName, Bundle data) {
		EventDelegater.sendLocalEvent(getActivity(), eventName, data);
	}

	public void sendGlobalEvent(String eventName, Bundle data) {
		EventDelegater.sendGlobalEvent(getActivity(), eventName, data);
	}

	public void hostingLocalEventReceiver(String eventName, PeriodMode periodMode, EventReceiver receiver) {
		ensureEventDelegaterMade();
		mEventDelegater.hostingLocalEventReceiver(eventName, periodMode, receiver);
	}

	public void hostingGlobalEventReceiver(String eventName, PeriodMode periodMode, EventReceiver receiver) {
		ensureEventDelegaterMade();
		mEventDelegater.hostingGlobalEventReceiver(eventName, periodMode, receiver);
	}

	public void unhostingLocalEventReceiver(String eventName) {
		ensureEventDelegaterMade();
		mEventDelegater.unhostingLocalEventReceiver(eventName);
	}

	public void unhostingGlobalEventReceiver(String eventName) {
		ensureEventDelegaterMade();
		mEventDelegater.unhostingGlobalEventReceiver(eventName);
	}

	private void ensureEventDelegaterMade() {
		if (mEventDelegater == null) mEventDelegater = new EventDelegater(getActivity());
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mEventDelegater != null) mEventDelegater.onStart();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mEventDelegater != null) mEventDelegater.onActivityCreated(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mEventDelegater != null) mEventDelegater.onResume();
	}

	@Override
	public void onPause() {
		if (mEventDelegater != null) mEventDelegater.onPause();
		super.onPause();
	}

	@Override
	public void onStop() {
		if (mEventDelegater != null) mEventDelegater.onStop();
		super.onStop();
	}

	private EventDelegater mEventDelegater;
	CustomProgressDialog dialog;

	public void showProgress()
	{
		dialog = CustomProgressDialog.createDialog(getActivity());
		if (dialog != null)
		{
			dialog.show();
		}
	}

	public void closeProgress()
	{
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
	}
}
