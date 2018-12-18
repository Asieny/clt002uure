package cn.com.mytest.help;

import android.content.Context;
import cn.com.mytest.framework.AbsApp;
import cn.com.mytest.phone.Device;
import cn.com.mytest.util.SPref;

/**
 * @author wangcw
 */
public class SharedPrefHelper {

	public static class AppS {
		public static boolean getFirstLaunch(Context context) {
			return SPref.getSPref(context, AbsApp.class).getBoolean("first_launch", true);
		}

		public static void clearFirstLaunch(Context context) {
			SPref.edit(context, AbsApp.class).putBoolean("first_launch", false).commit();
		}
	}

	public static class DeviceS {
		public static String getUniqueId(Context context) {
			return SPref.getSPref(context, Device.class).getString("unique_id", null);
		}

		public static void saveUniqueId(Context context, String value) {
			SPref.edit(context, Device.class).putString("unique_id", value).commit();
		}
	}
//
//	public static class UserHelperS {
//		public static void saveToken(Context context, String value) {
//			SPref.saveAsFile(context, UserHelper.class, "token", value);
//		}
//
//		public static String getToken(Context context) {
//			return SPref.getFromFile(context, UserHelper.class, "token");
//		}
//
//		public static void saveAccountJson(Context context, String value) {
//			SPref.saveAsFile(context, UserHelper.class, "account", value);
//		}
//
//		//区分开的目的，解决跨进程之间共享同一个sharedpref文件时不同步导致值丢失的问题
//		public static String getAccountJson(Context context) {
//			return SPref.getFromFile(context, UserHelper.class, "account");
//		}
//
//		public static void saveAuthorityFlag(Context context, boolean value) {
//			SPref.saveBooleanAsFile(context, UserHelper.class, "authority_flag", value);
//		}
//
//		public static boolean getIsAuthorizeSuccess(Context context) {
//			return SPref.getBooleanFromFile(context, UserHelper.class, "authority_flag");
//		}
//	}
}
