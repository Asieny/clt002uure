package cn.com.mytest.framework;

/**
 * @author wangcw
 */
public abstract class AbsDataActivity extends AbsActivity {
	/*private DataManager mDManager;
	private Bundle mUserBundle;
	private boolean mPassUser = true;
	
	private Bundle emptyUserBundle() {
		if(mUserBundle == null) {
			mUserBundle = new Bundle();
		}else {
			mUserBundle.clear();
		}
		return mUserBundle;
	}
	
	public void setPassUser(boolean pass) {
		mPassUser = pass;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//UserHelper.unpack(getIntent().getBundleExtra(Const.EXTRA_USER_INFO));
		mDManager = new DataManager(this);
		mDManager.onCreate();
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if(mPassUser) {
			//intent.putExtra(Const.EXTRA_USER_INFO, UserHelper.pack(emptyUserBundle()));
		}else {
			UserHelper.packAndClear(emptyUserBundle());
		}
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(!mPassUser) UserHelper.unpack(mUserBundle);
		UserHelper.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	};
	
	public DataManager getDataManager() {
		return mDManager;
	}
	
	@Override
	protected void onRestart() {
		mDManager.onRestart();
		super.onRestart();
	}
	
	@Override
	protected void onStop() {
		mDManager.onStop();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mDManager.onDestroy();
		mDManager = null;
		super.onDestroy();
	}*/
}
