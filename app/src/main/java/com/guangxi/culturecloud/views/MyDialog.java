package com.guangxi.culturecloud.views;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ImageView;

import com.guangxi.culturecloud.R;


/**
 * 自定义dialog类图片放大
 * 
 * @author zww
 * 
 */
public class MyDialog extends Dialog {
	private Window window = null;
	public ImageView img = null;
	public MyDialog(Context context) {
		super(context);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);

	}

	public void setDialog(int layoutResID) {
		setContentView(LayoutInflater.from(getContext()).inflate(layoutResID, null));
		img = (ImageView)findViewById(R.id.img);
		window = getWindow(); // 得到对话框
		window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画
		// 设置触摸对话框意外的地方取消对话框
		setCanceledOnTouchOutside(true);
		// 阻止返回键响应
		setCancelable(true);
	}

	// 如果自定义的Dialog声明为局部变量，就可以调用下面两个方法进行显示和消失，全局变量则无所谓
	/**
	 * 显示dialog
	 */
	public void showDialog() {
		show();
	}

	/**
	 * 关闭dialog
	 */
	public void Closedialog() {
		dismiss();
	}
}
