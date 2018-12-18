package com.jarvis.mytaobao.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/22 10:58
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class MapNavigationActivity extends BaseActivity implements View.OnClickListener {

    private SDKBroadCast   mSdkBroadCast;
    private MapView        mMapView;
    private BaiduMap       mBaiduMap;
    public  LocationClient mLocationClient;
    private double mlatitude  = 32.079313;//终点纬度
    private double mlongitude = 120.99037;//终点经度
    private double mLocalLat  = 32.079313;//我的纬度
    private double mLocalLon  = 120.99037;//我的经度
    private LatLng    ArtLatlng;
    private ImageView mIv_back;
    private Button    mButton_check_lines;
    private ListView  mListView;
    private String[] toOtherMap = {"用百度地图导航", "用高德地图道航", "取消"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        initSDK();
        setContentView(R.layout.activity_map_navigation);
        Intent intent = getIntent();
        mlongitude = intent.getDoubleExtra("mLng", mlongitude);
        mlatitude = intent.getDoubleExtra("mLat", mlatitude);
        ArtLatlng = new LatLng(mlatitude, mlongitude);
        initView();
        initData();
        myLocalPotion();
        mButton_check_lines.setOnClickListener(this);
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.baiduMap);
        mBaiduMap = mMapView.getMap();
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mButton_check_lines = (Button) findViewById(R.id.button_check_lines);
    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);// 设置缩放到15级，默认是12级
        mBaiduMap.setMapStatus(mapStatusUpdate);
        //设置中心点
        MapStatusUpdate mapStatusUpdatePoint = MapStatusUpdateFactory
                .newLatLng(ArtLatlng);
        mBaiduMap.setMapStatus(mapStatusUpdatePoint);
        drawaMarking();
    }

    private void initSDK() {
        SDKInitializer.initialize(getApplicationContext());
        //注册一个广播，用来提示是否有网。
        mSdkBroadCast = new SDKBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        registerReceiver(mSdkBroadCast, filter);
    }

    //绘制marker覆盖物
    private void drawaMarking() {
        ArrayList<BitmapDescriptor> mBitmaps = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor bitDes = BitmapDescriptorFactory.fromResource(R.drawable.sh_icon_grid_recently_p);
        mBitmaps.add(bitDes);
        mBitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.sh_icon_grid_recently_n));
        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(ArtLatlng)
                .icons(mBitmaps)   //设置marker多图标
                .title("AnySearch");
        mBaiduMap.addOverlay(mMarkerOptions);

    }

    //我的位置
    private void myLocalPotion() {
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplication());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return;
                mLocalLat = bdLocation.getLatitude();
                mLocalLon = bdLocation.getLongitude();
                MyLocationData dada = new MyLocationData.Builder()
                        .latitude(bdLocation.getLatitude())// 设置纬度
                        .longitude(bdLocation.getLongitude())// 设置经度
                        .build();
                mBaiduMap.setMyLocationData(dada);// 显示定位信息 只有打开定位图层 才有效
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mBaiduMap.setMyLocationEnabled(true);// 打开定位图层
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        //定位關閉
        //        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        unregisterReceiver(mSdkBroadCast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_check_lines:
                //弹出popupwindow让用户选择跳转地图
                final PopupWindow popupWindow = new PopupWindow();
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(LayoutInflater.from(MapNavigationActivity.this).inflate(R.layout.popup_to_othermap, null));
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                //显示popupwindow,指定位置
                popupWindow.showAtLocation(mButton_check_lines, Gravity.BOTTOM, 0, 0);
                //
                mListView = (ListView) popupWindow.getContentView().findViewById(R.id.lv_club_type);
                MyAdapterType myAdapter = new MyAdapterType();
                mListView.setAdapter(myAdapter);

                //给条目设置点击事件
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            toBaiduMap();
                        } else if (position == 1) {
                            toGaodeMap();
                        } else {
                            popupWindow.dismiss();
                        }
                    }
                });
                break;
        }
    }

    private void toGaodeMap() {
        //头部 后面的sourceApplicaiton填自己APP的名字//com.jarvis.mytaobaotest
        String GAODE_HEAD = "androidamap://route?sourceApplication=MyApplication";
        //起点经度
        String GAODE_SLON = "&slon=";
        //起点纬度
        String GAODE_SLAT = "&slat=";
        //起点名字
        String GAODE_SNAME = "&sname=";
        //终点经度
        String GAODE_DLON = "&dlon=";
        //终点纬度
        String GAODE_DLAT = "&dlat=";
        //终点名字
        String GAODE_DNAME = "&dname=";
        // dev 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
        // t = 1(公交) =2（驾车） =4(步行)
        String GAODE_MODE = "&dev=0&t=2";
        //高德地图包名
        String GAODE_PKG = "com.autonavi.minimap";

        //检测安装和唤起
        if (checkMapAppsIsExist(MapNavigationActivity.this, GAODE_PKG)) {
//            LocationUtils mStart = new LocationUtils(mLocalLon, mLocalLat, "我的位置");
//            LocationUtils mEnd = new LocationUtils(mlongitude, mlatitude, "终点");
//            BD09ToGCJ02(mStart, mEnd);
            CoodinateCovertor covertor = new CoodinateCovertor();
            LngLat lngLat = new LngLat(mLocalLon,mLocalLat);
            LngLat startlngLat = covertor.bd_decrypt(lngLat);
            LngLat lngLat2 = new LngLat(mlongitude,mlatitude);
            LngLat endlngLat = covertor.bd_decrypt(lngLat2);

            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(GAODE_HEAD + GAODE_SLAT + startlngLat.getLantitude() + GAODE_SLON + startlngLat.getLongitude() +
                    GAODE_SNAME + "我的位置" + GAODE_DLAT + endlngLat.getLantitude() + GAODE_DLON + endlngLat.getLongitude() +
                    GAODE_DNAME + "终点" + GAODE_MODE));
            startActivity(intent);
        } else {
            Toast.makeText(MapNavigationActivity.this, "高德地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private void toBaiduMap() {
        //头部 添加相应地区
        String BAIDU_HEAD = "baidumap://map/direction?region=0";
        //起点的经纬度
        String BAIDU_ORIGIN = "&origin=";
        //终点的经纬度
        String BAIDU_DESTINATION = "&destination=";
        //路线规划方式
        String BAIDU_MODE = "&mode=driving";
        //百度地图的包名
        String BAIDU_PKG = "com.baidu.BaiduMap";

        //检测地图是否安装和唤起
        if (checkMapAppsIsExist(MapNavigationActivity.this, BAIDU_PKG)) {
            Toast.makeText(MapNavigationActivity.this, "百度地图已经安装", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setData(Uri.parse(BAIDU_HEAD + BAIDU_ORIGIN + mLocalLat
                    + "," + mLocalLon + BAIDU_DESTINATION + mlatitude + "," + mlongitude
                    + BAIDU_MODE));
            startActivity(intent);
        } else {
            Toast.makeText(MapNavigationActivity.this, "百度地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    class SDKBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(getBaseContext(), "没有网络", Toast.LENGTH_SHORT).show();
            } else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(getBaseContext(), "AK校验失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyAdapterType extends BaseAdapter {

        @Override
        public int getCount() {
            return toOtherMap.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(getBaseContext(), R.layout.list_item_othermap, null);
            TextView tvListType = (TextView) convertView.findViewById(R.id.tv_list_type);
            tvListType.setText(toOtherMap[position]);
            return convertView;
        }
    }

    /**
     * 检测地图应用是否安装
     *
     * @param context
     * @param packagename
     * @return
     */
    public boolean checkMapAppsIsExist(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 坐标转换
     *
     * @param mStart
     * @param mEnd
     */
    public void BD09ToGCJ02(LocationUtils mStart, LocationUtils mEnd) {
        LatLng newStart = convertBaiduToGPS(new LatLng(mStart.getLatitude(), mStart.getLongitude()));
        LatLng newEnd = convertBaiduToGPS(new LatLng(mEnd.getLatitude(), mEnd.getLongitude()));
        mStart.setLatitude(newStart.latitude);
        mStart.setLongitude(newStart.longitude);

        mEnd.setLatitude(newEnd.latitude);
        mEnd.setLongitude(newEnd.longitude);

    }

    /**
     * 将百度地图坐标转换成GPS坐标
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng convertBaiduToGPS(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
        double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
        BigDecimal bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
    }

    /**
     * Created by qiang on 2017/2/24.
     * 坐标点的经纬度和名字
     */

    public class LocationUtils {
        private double longitude;
        private double latitude;
        private String name;

        public LocationUtils() {
        }

        public LocationUtils(double longitude, double latitude, String name) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.name = name;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "LocationUtils{" +
                    "longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    /** 经纬度点封装
     * Created by 明明如月 on 2017-03-22.
     */
    public class LngLat {
        private double longitude;//经度
        private double lantitude;//维度

        public LngLat() {
        }

        public LngLat(double longitude, double lantitude) {
            this.longitude = longitude;
            this.lantitude = lantitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLantitude() {
            return lantitude;
        }

        public void setLantitude(double lantitude) {
            this.lantitude = lantitude;
        }

        @Override
        public String toString() {
            return "LngLat{" +
                    "longitude=" + longitude +
                    ", lantitude=" + lantitude +
                    '}';
        }
    }
    /** 百度地图坐标和火星坐标转换
     * Created by 明明如月 on 2017-03-22.
     */
    public class CoodinateCovertor {


        private  double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

        /**
         * 对double类型数据保留小数点后多少位
         *  高德地图转码返回的就是 小数点后6位，为了统一封装一下
         * @param digit 位数
         * @param in 输入
         * @return 保留小数位后的数
         */
         double dataDigit(int digit,double in){
            return new   BigDecimal(in).setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();

        }

        /**
         * 将火星坐标转变成百度坐标
         * @param lngLat_gd 火星坐标（高德、腾讯地图坐标等）
         * @return 百度坐标
         */

        public  LngLat bd_encrypt(LngLat lngLat_gd)
        {
            double x = lngLat_gd.getLongitude(), y = lngLat_gd.getLantitude();
            double z = sqrt(x * x + y * y) + 0.00002 * sin(y * x_pi);
            double theta = atan2(y, x) + 0.000003 * cos(x *  x_pi);
            return new LngLat(dataDigit(6,z * cos(theta) + 0.0065),dataDigit(6,z * sin(theta) + 0.006));

        }
        /**
         * 将百度坐标转变成火星坐标
         * @param lngLat_bd 百度坐标（百度地图坐标）
         * @return 火星坐标(高德、腾讯地图等)
         */
         LngLat bd_decrypt(LngLat lngLat_bd)
        {
            double x = lngLat_bd.getLongitude() - 0.0065, y = lngLat_bd.getLantitude() - 0.006;
            double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
            double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
            return new LngLat( dataDigit(6,z * cos(theta)),dataDigit(6,z * sin(theta)));

        }

        //测试代码
        public  void main(String[] args) {
            LngLat lngLat_bd = new LngLat(120.153192,30.25897);
            System.out.println(bd_decrypt(lngLat_bd));
        }
    }
}
