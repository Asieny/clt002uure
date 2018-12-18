package cn.com.mytest.framework;

/**
 * @author wangcw
 */
public abstract class Config {
	public static Config get() {
		return AbsApp.get().mConfig;
	}
/*
	public static final String DEFAULT_NET_ENCODING	= "UTF-8";
	public static final int DEFAULT_NET_TIMEOUT		= 5000;

	public final String packageName;
	public final String versionName;
	public final int versionCode;

	*//**存储卡上的一个目录名。注意不是系统为当前App创建的私有目录**//*
	public final String appDirName;

	public final String dbName;
	public final int dbVersion;

	public final FsSize memoryLimit;

	public final String STATUS;
	public final ApiStatus SUCCESS;

	public final int netTimeoutMs;
	public final String netParamsEncoding;

	public Config(Context context) {
		AbsApp.get().mConfig = this;

		packageName = context.getPackageName();
		versionName = Manifest.getVersionName(context);
		versionCode = Manifest.getVersionCode(context);
		appDirName = getRootDirName();

		memoryLimit = new FsSize(((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass());

		dbName = getDBName();
		dbVersion = getDBVersion();

		netTimeoutMs = getNetTimeoutMs();
		netParamsEncoding = getParamsEncoding();

		STATUS = getApiStatusKey();
		SUCCESS = getApiStatusSuccess();

		checkApisConflict();
	}

	public final void checkApiConfig(Api<?> api) {	//isApiConfigured
		if(apiNames == null || !apiNames.contains(api.name + api.baseUrl))
			throw new AndroidRuntimeException("请先将该Api配置信息加入到Config里（继承Config类）。" +
					"baseUrl: " + api.baseUrl + ", name: " + api.name);
	}

	private Set<String> apiNames;
	private void checkApisConflict() {
		Api<?>[] apis = getApis();
		if(apis != null && apis.length > 0) {
			apiNames = new HashSet<String>();
			Map<String, Api<?>> map = new HashMap<String, Api<?>>(apis.length);
			String key;
			for(Api<?> api : apis) {
				key = api.name + api.baseUrl;
				apiNames.add(key);
				if(api.cacheTimeMS >= 0) {
					if(map.containsKey(key)) {
						throw new AndroidRuntimeException("不应该有多个name、baseUrl都相同的Api配置");
					}
					map.put(key, api);
				}
			}
		}
	}

	*//**获取当前最新版本的所有数据库表**//*
	public abstract Table<?, ?>[] getTables();

	public abstract IUpgrader getUpgrader();

	*//**获取当前最新版本的所有网络请求缓存配置**//*
	public abstract Api<?>[] getApis();
	public abstract Map<String, String> getDefaultHeaders();
	public abstract ApiStatus[] getApiStatus();

	*//**应该忽略的状态，等同于成功。如(3, "课程已过期")和(501, "未开通此课程")等**//*
	public abstract ApiStatus[] getIrgnoreStatus();
	public abstract ApiStatus[] getUserInvalidStatus();

	///////////////////////////////////////////////////////

	protected abstract String getRootDirName();
	protected abstract String getDBName();
	protected abstract int getDBVersion();

	protected int getNetTimeoutMs() {
		return DEFAULT_NET_TIMEOUT;
	}

	protected String getParamsEncoding() {
		return DEFAULT_NET_ENCODING;
	}

	protected String getApiStatusKey() {
		return "Status";
	}

	protected abstract ApiStatus getApiStatusSuccess();*/
}
