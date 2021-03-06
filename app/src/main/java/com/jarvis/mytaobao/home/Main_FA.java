package com.jarvis.mytaobao.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.jarvis.mytaobao.Data.Data;
import com.jarvis.mytaobao.cart.Cart_F;
import com.jarvis.mytaobao.discover.Discover_F;
import com.jarvis.mytaobao.tao.Tao_F;
import com.jarvis.mytaobao.user.User_F;
import com.javis.mytools.IBtnCallListener;
import com.zdp.aseo.content.AseoZdpAseo;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页
 *
 * @author wangcw
 */
public class Main_FA extends BaseActivity implements OnClickListener, IBtnCallListener {

    // 界面底部的菜单按钮
    private ImageView[] bt_menu    = new ImageView[5];
    // 界面底部的菜单按钮id
    private int[]       bt_menu_id = {R.id.iv_menu_0, R.id.iv_menu_1, R.id.iv_menu_2, R.id.iv_menu_3, R.id.iv_menu_4};
    //底部布局按钮的id
    private int[]       linear_id  = {R.id.linear0, R.id.linear1, R.id.linear2, R.id.linear3, R.id.linear4};

    // 界面底部的选中菜单按钮资源
    private int[] select_on  = {R.drawable.guide_home_on, R.drawable.guide_tfaccount_on, R.drawable.guide_discover_on, R.drawable.guide_cart_on, R.drawable.guide_account_on};
    // 界面底部的未选中菜单按钮资源
    private int[] select_off = {R.drawable.bt_menu_0_select, R.drawable.bt_menu_1_select, R.drawable.bt_menu_2_select, R.drawable.bt_menu_3_select, R.drawable.bt_menu_4_select};

    /**
     * 主界面
     */
    private Home_F     home_F;
    /**
     * 活动
     */
    private Tao_F      tao_F;
    /**
     * 空间界面
     */
    private Discover_F discover_F;
    /**
     * 广场界面
     */
    private Cart_F     cart_F;
    /**
     * 个人中心界面
     */
    private User_F     user_F;

    private LinearLayout linear_mine;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fa);
        getSaveData();
        initView();
        //		ToolLocation.requestLocation(Main_FA.this, new ToolLocation.InterfaceBDLocation() {
        //
        //			@Override
        //			public void onLocationSuccess(BDLocation location) {
        //				toast(location.getAddrStr());
        //			}
        //		}, true);
    }

    /**
     * 得到保存的购物车数据
     */
    private void getSaveData() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        AseoZdpAseo.initTimer(this);
        SharedPreferences sp = getSharedPreferences("SAVE_CART", Context.MODE_PRIVATE);
        int size = sp.getInt("ArrayCart_size", 0);
        for (int i = 0; i < size; i++) {
            hashMap.put("type", sp.getString("ArrayCart_type_" + i, ""));
            hashMap.put("color", sp.getString("ArrayCart_color_" + i, ""));
            hashMap.put("num", sp.getString("ArrayCart_num_" + i, ""));
            Data.arrayList_cart.add(hashMap);
        }

    }

    // 初始化组件
    private void initView() {
        // 找到底部菜单的按钮并设置监听
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
            //			bt_menu[i].setOnClickListener(this);
        }

        LinearLayout linear_home = (LinearLayout) findViewById(R.id.linear0);
        LinearLayout linear_act = (LinearLayout) findViewById(R.id.linear1);
        LinearLayout linear_space = (LinearLayout) findViewById(R.id.linear2);
        LinearLayout linear_square = (LinearLayout) findViewById(R.id.linear3);
        linear_mine = (LinearLayout) findViewById(R.id.linear4);
        linear_home.setOnClickListener(this);
        linear_act.setOnClickListener(this);
        linear_space.setOnClickListener(this);
        linear_square.setOnClickListener(this);
        linear_mine.setOnClickListener(this);

        // 初始化默认显示的界面
        if (home_F == null) {
            home_F = new Home_F();
            addFragment(home_F);
            showFragment(home_F);
        } else {
            showFragment(home_F);
        }
        // 设置默认首页为点击时的图片
        bt_menu[0].setImageResource(select_on[0]);
        AseoZdpAseo.init(this, AseoZdpAseo.SCREEN_TYPE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear0:
                // 主界面
                if (home_F == null) {
                    home_F = new Home_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(home_F);
                    showFragment(home_F);
                } else {
                    if (home_F.isHidden()) {
                        showFragment(home_F);
                    }
                }
                break;
            case R.id.linear1:
                // 活动
                if (tao_F == null) {
                    tao_F = new Tao_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    if (!tao_F.isHidden()) {
                        addFragment(tao_F);
                        showFragment(tao_F);
                    }
                } else {
                    if (tao_F.isHidden()) {
                        showFragment(tao_F);
                    }
                    tao_F.getDataFromIntnet();
                }

                break;
            case R.id.linear2:
                // 空间
                if (discover_F == null) {
                    discover_F = new Discover_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    if (!discover_F.isHidden()) {
                        addFragment(discover_F);
                        showFragment(discover_F);
                    }
                } else {
                    if (discover_F.isHidden()) {
                        showFragment(discover_F);
                        discover_F.reCreatView();
                    }
                }

                break;
            case R.id.linear3:
                // 广场
                if (cart_F != null) {
                    removeFragment(cart_F);
                    cart_F = null;
                }
                cart_F = new Cart_F();
                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                addFragment(cart_F);
                showFragment(cart_F);

                break;
            case R.id.linear4:
                // 我
                if (user_F == null) {
                    user_F = new User_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    if (!user_F.isHidden()) {
                        addFragment(user_F);
                        showFragment(user_F);
                    }
                } else {
                    if (user_F.isHidden()) {
                        showFragment(user_F);
                    }
                }

                break;
        }

        // 设置按钮的选中和未选中资源
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i].setImageResource(select_off[i]);
            if (v.getId() == linear_id[i]) {
                bt_menu[i].setImageResource(select_on[i]);
            }
        }
    }

    final int REQUEST_CODE_YANXUE = 1986;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_YANXUE:
                if (10086 == resultCode) {
                    //跳转个人中心
                    linear_mine.performClick();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加Fragment
     **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.show_layout, fragment);
        ft.commit();
    }

    /**
     * 删除Fragment
     **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /**
     * 显示Fragment
     **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        // 设置Fragment的切换动画
        ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (home_F != null) {
            ft.hide(home_F);
        }
        if (tao_F != null) {
            ft.hide(tao_F);
        }
        if (discover_F != null) {
            ft.hide(discover_F);
        }
        if (cart_F != null) {
            ft.hide(cart_F);
        }
        if (user_F != null) {
            ft.hide(user_F);
        }

        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    /**
     * 返回按钮的监听
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * Fragment的回调函数
     */
    @SuppressWarnings("unused")
    private IBtnCallListener btnCallListener;

    @Override
    public void onAttachFragment(Fragment fragment) {
        try {
            btnCallListener = (IBtnCallListener) fragment;
        } catch (Exception e) {
        }

        super.onAttachFragment(fragment);
    }

    /**
     * 响应从Fragment中传过来的消息
     */
    @Override
    public void transferMsg() {
        if (home_F == null) {
            home_F = new Home_F();
            addFragment(home_F);
            showFragment(home_F);
        } else {
            showFragment(home_F);
        }
        bt_menu[3].setImageResource(select_off[3]);
        bt_menu[0].setImageResource(select_on[0]);

        System.out.println("由Fragment中传送来的消息");
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
