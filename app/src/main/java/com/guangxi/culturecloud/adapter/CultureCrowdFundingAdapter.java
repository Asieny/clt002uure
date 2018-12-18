package com.guangxi.culturecloud.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.model.CultureCrowdFundingInfo;
import com.guangxi.culturecloud.views.NumberProgressBar;
import com.guangxi.culturecloud.views.TimeTextView;
import com.guangxi.culturecloud.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.com.mytest.adapter.AbsAdapter;
import cn.com.mytest.util.ImageLoaderUtil;

/**
 * Created by zww on 2018/1/5.
 * 活动众筹adapter
 */

public class CultureCrowdFundingAdapter extends AbsAdapter<CultureCrowdFundingInfo> {

    private long mBetween;

    public CultureCrowdFundingAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_list_culture_crowdfunding, parent, false);
            viewHolder.lable1 = (TextView) convertView.findViewById(R.id.lable1);
            viewHolder.lable2 = (TextView) convertView.findViewById(R.id.lable2);
            viewHolder.lable3 = (TextView) convertView.findViewById(R.id.lable3);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);

            viewHolder.percent = (TextView) convertView.findViewById(R.id.percent);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.sy_time = (TimeTextView) convertView.findViewById(R.id.sy_time);
            viewHolder.target_number = (TextView) convertView.findViewById(R.id.target_number);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.numberbar = (NumberProgressBar) convertView.findViewById(R.id.numberbar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).raised_number >= getItem(position).target_number) {
            viewHolder.numberbar.setMax(getItem(position).raised_number);
            viewHolder.numberbar.setProgress(getItem(position).raised_number);
            viewHolder.target_number.setText(getItem(position).target_number + "");
        } else {
            viewHolder.numberbar.setMax(getItem(position).target_number);
            viewHolder.numberbar.setProgress(getItem(position).raised_number);
            viewHolder.target_number.setText(getItem(position).target_number + "");
        }
        if (!getItem(position).label1.equals("")) {
            viewHolder.lable1.setVisibility(View.VISIBLE);
            viewHolder.lable1.setText(getItem(position).label1);
        } else {
            viewHolder.lable1.setVisibility(View.GONE);
        }
        if (!getItem(position).label2.equals("")) {
            viewHolder.lable2.setVisibility(View.VISIBLE);
            viewHolder.lable2.setText(getItem(position).label2);
        } else {
            viewHolder.lable2.setVisibility(View.GONE);
        }
        if (!getItem(position).label3.equals("")) {
            viewHolder.lable3.setVisibility(View.VISIBLE);
            viewHolder.lable3.setText(getItem(position).label3);
        } else {
            viewHolder.lable3.setVisibility(View.GONE);
        }
        viewHolder.title.setText(getItem(position).fname);
        viewHolder.time.setText("众筹时间：" + getItem(position).zc_begin_time + "至" + getItem(position).zc_end_time);
        viewHolder.address.setText("活动地址：" + getItem(position).activity_detail_address);
        viewHolder.percent.setText(getItem(position).fpercent);
        viewHolder.number.setText(getItem(position).raised_number + "份");
        //计算距离时间
        String zc_end_time = getItem(position).zc_end_time;//2018-02-22 10:55:24
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long endTime = 0;
        try {
            java.util.Date end = dfs.parse(zc_end_time);
            endTime = end.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.sy_time.setTimes(endTime);
        //计算时间差，秒
        //        long currentTimeMillis = System.currentTimeMillis();
        //        mBetween = (endTime - currentTimeMillis) / 1000;
//        if (mBetween > 0) {
//            int day1 = (int) (mBetween / (24 * 3600));
//            int hour1 = (int) (mBetween % (24 * 3600) / 3600);
//            String hourtime = "";
//            if (hour1 < 9) {
//                hourtime = "0" + String.valueOf(hour1);
//            } else {
//                hourtime = String.valueOf(hour1);
//            }
//            int minute1 = (int) (mBetween % (24 * 3600) % 3600 / 60);
//            String minTime = "";
//            if (minute1 < 9) {
//                minTime = "0" + String.valueOf(minute1);
//            }else {
//                minTime = String.valueOf(minute1);
//            }
//            int second1 = (int) (mBetween % (24 * 3600) % 3600 % 60 % 60);
//            String sceTime = "";
//            if (second1 < 9) {
//                sceTime = "0" + String.valueOf(second1);
//            } else {
//                sceTime = String.valueOf(second1);
//            }
//            String time = day1 + "天" + hourtime + ":" + minTime + ":" + sceTime;
//            viewHolder.sy_time.setText(time);
//        } else {
//            viewHolder.sy_time.setText("已经结束");
//        }
        if (!getItem(position).pic.equals("")) {
            String picUrl = getItem(position).pic.split(",")[0];
            ImageLoaderUtil.displayImageIcon(NetConfig.IMG + picUrl.replace("\\", "/"), viewHolder.img);
        }
        return convertView;
    }

    class ViewHolder {
        TextView lable1;
        TextView lable2;
        TextView lable3;
        TextView title;
        TextView time;
        TextView address;

        TextView          percent;
        TextView          number;
        TimeTextView          sy_time;
        TextView          target_number;
        ImageView         img;
        NumberProgressBar numberbar;
    }
}
