package cn.com.mytest.util;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * @author wangcw
 */
public class UIUtils {
	public static int dip2pix(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	public static int sp2pix(Context context, int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
	}

	public static int getLocationCenterX(View view) {
		int[] arrLocation = new int[2];
		view.getLocationInWindow(arrLocation);
		return view.getMeasuredWidth() / 2 + arrLocation[0];
	}

	public static void hideView(View view) {
		if (view != null) view.setVisibility(View.GONE);
	}

	public static void showView(View view) {
		if (view != null) view.setVisibility(View.VISIBLE);
	}

	public static void toggleView(View view) {
		if (view != null) {
			if (view.getVisibility() == View.VISIBLE)
				view.setVisibility(View.GONE);
			else
				view.setVisibility(View.VISIBLE);
		}
	}

	public static void invisibleView(View view) {
		if (view != null) view.setVisibility(View.INVISIBLE);
	}

	public static void setScreenOnFlag(Activity activity) {
		setScreenOnFlag(activity.getWindow());
	}

	public static void setScreenOnFlag(Window window) {
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public static void setText(Activity activity, int viewId, String value) {
		((TextView) activity.findViewById(viewId)).setText(value);
	}

	public static void setWindowBrightness(Activity activity, float screenBrightness) {
		setWindowBrightness(activity.getWindow(), screenBrightness);
	}

	public static void setWindowBrightness(Window window, float screenBrightness) {
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.screenBrightness = screenBrightness;
		window.setAttributes(layoutParams);
	}
}
