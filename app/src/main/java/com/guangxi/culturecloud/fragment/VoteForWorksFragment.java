package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.R;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/16 16:43
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class VoteForWorksFragment extends Fragment implements View.OnClickListener {
    public  View                      mRootView;
    private TextView                  mTv_home; //首页
    private TextView                  mTv_ranking;//排行榜
    private TextView                  mTv_rule;//规则
    private TextView                  mTv_name_list;//获奖名单
    private AtSceneHomeFragment       atSceneHomeFragment;//首页fragment
    private AtSceneRankingFragment    atSceneRankingFragment;//..
    private AtSceneRuleFragment       atSceneRuleFragment;//...
    private AtSceneWinnerListFragment atSceneWinnerListFragment;//..
    private Button                    mBt_join_red;
    private ScrollView                mFirst_scroll;
    private LinearLayout              mLinear_honmedd;
    private int                       markWhich;
    private Button                    mBt_to_top;
    private TakePartInSceneFragment   mTakePartInSceneFragment;
    private boolean needShowDetails = false;//是否要展示投票详情页

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_vote_for_works, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mTv_home = (TextView) mRootView.findViewById(R.id.tv_home);
        mTv_ranking = (TextView) mRootView.findViewById(R.id.tv_ranking);
        mTv_rule = (TextView) mRootView.findViewById(R.id.tv_rule);
        mTv_name_list = (TextView) mRootView.findViewById(R.id.tv_name_list);
        mFirst_scroll = (ScrollView) mRootView.findViewById(R.id.first_scroll);
        mLinear_honmedd = (LinearLayout) mRootView.findViewById(R.id.linear_honmedd);
    }

    private void initData() {
        if (needShowDetails) {
            //首先展示投票界面
            VoteWorksDetailFragment voteWorksDetailFragment = new VoteWorksDetailFragment();
            FragmentTransaction mFt = getFragmentManager().beginTransaction();
            //进行fragment操作:
            mFt.add(R.id.frame_tab, voteWorksDetailFragment, "voteWorksDetailFragment");
            mFt.addToBackStack(null);
            voteWorksDetailFragment.setVoteData(AtSceneActivity.mSquare);
            //提交事务
            mFt.commit();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction mFt = fragmentManager.beginTransaction();
            changeTitelColor(mTv_home);
            //首先展示界面
            atSceneHomeFragment = new AtSceneHomeFragment();
            //进行fragment操作:
            mFt.add(R.id.frame_tab, atSceneHomeFragment, "atSceneHomeFragment");
            //提交事务
            mFt.commit();
            //显示参与按钮
            showRedButton();
            //给参与按钮设置点击事件
            setRedClickListener();
        }
        mTv_home.setOnClickListener(this);
        mTv_ranking.setOnClickListener(this);
        mTv_rule.setOnClickListener(this);
        mTv_name_list.setOnClickListener(this);

        Activity activity = getActivity();
        if (activity instanceof AtSceneActivity) {
            mBt_to_top = (Button) activity.findViewById(R.id.bt_to_top);
            mBt_to_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFirst_scroll.scrollTo(0, 0);
                }
            });
        }

//        mFirst_scroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        int[] location = new int[2];
//                        mLinear_honmedd.getLocationOnScreen(location);
//                        ToastUtils.makeShortText("" + location[1], getActivity());
//                        break;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction mFt = fragmentManager.beginTransaction();
        Fragment homeVoteWorksDetailFragment = fragmentManager.findFragmentByTag("HomeVoteWorksDetailFragment");
        if (homeVoteWorksDetailFragment != null) {
            fragmentManager.popBackStack();
        }
        if (!mBt_to_top.isShown()) {
            mBt_to_top.setVisibility(View.VISIBLE);
        }
        //        hideFragment(mTakePartInSceneFragment);
        //判断上面是否有发布广场的fragment，有的话弹出
        haveTakeFm();
        switch (v.getId()) {
            case R.id.tv_home:
                changeTitelColor(mTv_home);
                markWhich = 1;
                hideFragment(atSceneRankingFragment);
                hideFragment(atSceneRuleFragment);
                //首先展示投票界面
                if (atSceneHomeFragment == null) {
                    atSceneHomeFragment = new AtSceneHomeFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame_tab, atSceneHomeFragment, "atSceneHomeFragment");
                } else {
                    mFt.show(atSceneHomeFragment);
                }
                //提交事务
                mFt.commit();
                //显示参与按钮
                showRedButton();
                //给参与按钮设置点击事件
                setRedClickListener();
                break;
            case R.id.tv_ranking:
                changeTitelColor(mTv_ranking);
                markWhich = 2;
                hideFragment(atSceneHomeFragment);
                hideFragment(atSceneRuleFragment);
                //展示排行榜界面
                if (atSceneRankingFragment == null) {
                    atSceneRankingFragment = new AtSceneRankingFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame_tab, atSceneRankingFragment, "atSceneRankingFragment");
                } else {
                    if (atSceneRankingFragment.isHidden()) {
                        mFt.show(atSceneRankingFragment);
                    }
                }
                //提交事务
                mFt.commit();
                hideRedButton();
                break;
            case R.id.tv_rule:
                changeTitelColor(mTv_rule);
                markWhich = 3;
                hideRedButton();
                hideFragment(atSceneHomeFragment);
                hideFragment(atSceneRankingFragment);
                //展示排行榜界面
                if (atSceneRuleFragment == null) {
                    atSceneRuleFragment = new AtSceneRuleFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame_tab, atSceneRuleFragment, "atSceneRuleFragment");
                } else {
                    if (atSceneRuleFragment.isHidden()) {
                        mFt.show(atSceneRuleFragment);
                    }
                }
                //提交事务
                mFt.commit();
                mFirst_scroll.scrollTo(0, 0);
                break;
            case R.id.tv_name_list:
                ToastUtils.makeShortText("待开放，请快来加入", getActivity());
                //                hideRedButton();
                //                mBt_to_top.setVisibility(View.GONE);
                //                //展示排行榜界面
                //                if (atSceneWinnerListFragment == null) {
                //                    atSceneWinnerListFragment = new AtSceneWinnerListFragment();
                //                }
                //                //进行fragment操作:
                //                mFt.add(R.id.frame_initial, atSceneWinnerListFragment, "atSceneWinnerListFragment");
                //                if (markWhich == 1) {
                //                    atSceneWinnerListFragment.setMarkFromInfo("AtSceneHomeFragment");
                //                }
                //                mFt.addToBackStack(null);
                //                //提交事务
                //                mFt.commit();
                break;
            case R.id.img_no_intnet:
                break;
            default:
                break;
        }
    }

    private void haveTakeFm() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment mTakePartInSceneFragment = fragmentManager.findFragmentByTag("mTakePartInSceneFragment");
        if (mTakePartInSceneFragment != null && !mTakePartInSceneFragment.isDetached()) {
            fragmentManager.popBackStack();
        }
        //        mBt_join_red.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                FragmentManager fragmentManager = getFragmentManager();
        //                FragmentTransaction mFt = fragmentManager.beginTransaction();
        //                //首先展示投票界面
        //                mTakePartInSceneFragment = new TakePartInSceneFragment();
        //                //进行fragment操作:
        //                mFt.add(R.id.frame_tab, mTakePartInSceneFragment, "mTakePartInSceneFragment");
        //                //添加到回退栈
        //                mFt.addToBackStack(null);
        //                //提交事务
        //                mFt.commit();
        //                mBt_join_red.setVisibility(View.GONE);
        //            }
        //        });
    }

    //    public void setImgNoIntnetGone(boolean isShow){
    //        if (isShow){
    //            mImg_no_intnet.setVisibility(View.VISIBLE);
    //        }else {
    //            mImg_no_intnet.setVisibility(View.GONE);
    //        }
    //    }
    public void setNeedShowDetailsFragment(boolean needShowDetails) {
        this.needShowDetails = needShowDetails;
    }

    private void setRedClickListener() {
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            mBt_join_red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction mFt = fragmentManager.beginTransaction();
                    //首先展示投票界面
                    mTakePartInSceneFragment = new TakePartInSceneFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame_tab, mTakePartInSceneFragment, "mTakePartInSceneFragment");
                    //添加到回退栈
                    mFt.addToBackStack(null);
                    //提交事务
                    mFt.commit();
                    mBt_join_red.setVisibility(View.GONE);
                }
            });
        }
    }

    public void showRedButton() {
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            if (!mBt_join_red.isShown()) {
                mBt_join_red.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideRedButton() {
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            if (mBt_join_red.isShown()) {
                mBt_join_red.setVisibility(View.GONE);
            }
        }
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (fragment != null && !fragment.isHidden()) {
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    public void changeTitelColor(TextView view) {
        int id = view.getId();
        if (id == R.id.tv_home) {
            mTv_home.setTextColor(Color.parseColor("#ffffbb33"));
            mTv_ranking.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_rule.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_name_list.setTextColor(Color.parseColor("#6BC8EC"));
        }
        if (id == R.id.tv_ranking) {
            mTv_home.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_ranking.setTextColor(Color.parseColor("#ffffbb33"));
            mTv_rule.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_name_list.setTextColor(Color.parseColor("#6BC8EC"));
        }
        if (id == R.id.tv_rule) {
            mTv_home.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_ranking.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_rule.setTextColor(Color.parseColor("#ffffbb33"));
            mTv_name_list.setTextColor(Color.parseColor("#6BC8EC"));
        }
        if (id == R.id.tv_name_list) {
            mTv_home.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_ranking.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_rule.setTextColor(Color.parseColor("#6BC8EC"));
            mTv_name_list.setTextColor(Color.parseColor("#ffffbb33"));
        }

    }
}
