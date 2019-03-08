package cn.com.mytest.util;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import cn.com.mytest.R;


/**
 * 图片加载+缓存
 *
 * @author wangcw
 */
public class ImageLoaderUtil {

    // 默认头像
    static DisplayImageOptions defalutOptionsIcon = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_no_pic)
            .showImageForEmptyUri(R.mipmap.icon_no_pic)
            .showImageOnFail(R.mipmap.icon_no_pic)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    // 渐变显示图片
    static DisplayImageOptions fadeOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_no_pic)
            .showImageForEmptyUri(R.mipmap.icon_no_pic)
            .showImageOnFail(R.mipmap.icon_no_pic)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(2000))
            .build();

    //圆形
    public static DisplayImageOptions getRound(ImageView img) {
        int height = img.getLayoutParams().height;
        DisplayImageOptions roundOptionsIcon =
                new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_no_pic)
                        .showImageForEmptyUri(R.mipmap.icon_no_pic)
                        .showImageOnFail(R.mipmap.icon_no_pic)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .considerExifParams(true)
                        .displayer(new RoundedBitmapDisplayer(height))
                        .build();
        return roundOptionsIcon;
    }

    //圆角
    public static DisplayImageOptions getRound2(ImageView img) {
        int height = img.getLayoutParams().height;
        DisplayImageOptions roundOptionsIcon =
                new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_no_pic)
                        .showImageForEmptyUri(R.mipmap.icon_no_pic)
                        .showImageOnFail(R.mipmap.icon_no_pic)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .considerExifParams(true)
                        .displayer(new RoundedBitmapDisplayer(40))
                        .build();
        return roundOptionsIcon;
    }

    /**
     * 加载图片
     *
     * @param url
     * @param img
     */
    public static void displayImageIcon(String url, ImageView img) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, img, defalutOptionsIcon);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param img
     */
    public static void displayImageRoundIcon(String url, ImageView img) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, img, getRound(img));
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param img
     */
    public static void displayImageRoundIcon2(String url, ImageView img) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, img, getRound2(img));
    }

    /**
     * 加载渐变图片
     *
     * @param url
     * @param img
     */
    public static void displayImageFade(String url, ImageView img) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, img, fadeOptions);
    }

    /**
     * 加载渐变图片
     *
     * @param url
     * @param img
     */
    public static void displayImageFade(String url, ImageView img, int res) {
        DisplayImageOptions options =
                new DisplayImageOptions.Builder().showImageOnLoading(res)
                        .showImageForEmptyUri(res)
                        .showImageOnFail(res)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .displayer(new FadeInBitmapDisplayer(2000))
                        .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, img, options);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param img
     */
    public static void displayImage(String url, ImageView img, DisplayImageOptions options) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, img, options);
    }

    /**
     * 本地图片
     *
     * @param resource
     * @param img
     */
    public static void displayLocalImage(int resource, ImageView img) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("drawable://" + resource, img, getRound(img));
    }

    /**
     * 本地图片
     *
     * @param
     * @param img
     */
    public static void displayLocalImage(String path, ImageView img) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("file://" + path, img, getRound(img));
    }

    /**
     * 清除缓存图片
     */
    public static void clean() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearDiskCache();
    }

}
