package cn.com.mytest.util;

import java.text.DecimalFormat;

/**
 * @author wangcw
 */
public class NumberUtils {
	public static String formatFloat(double number, int group, int maxFrac, int minFrac) {
		return getFloatFormatter(group, maxFrac, minFrac).format(number);
	}

	public static DecimalFormat getFloatFormatter(int group, int maxFrac, int minFrac) {
		DecimalFormat formatter = new DecimalFormat();
		formatter.setGroupingSize(group);	//将整数部分按group位分节
		formatter.setMaximumFractionDigits(maxFrac);	//最多maxFrac位小数
		formatter.setMinimumFractionDigits(minFrac);	//最少minFrac位小数
		return formatter;
	}
}
