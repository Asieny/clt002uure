package cn.com.mytest.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <存放所有的list在最后退出时一起关闭>
 *  
 * @author  张伟伟
 * @version  [V1.00, 2015年5月26日]
 * @see  [相关类/方法]
 * @since V1.00
 */
public class PublicWay {
	public static List<Activity> activityList = new ArrayList<Activity>();
	
	public static int num = 4;
	
}
