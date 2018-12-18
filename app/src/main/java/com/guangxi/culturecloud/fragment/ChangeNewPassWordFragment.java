package com.guangxi.culturecloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/2 9:23
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ChangeNewPassWordFragment extends Fragment implements View.OnClickListener {
    private View      mRootView;
    private ImageView mImg_back;
    private EditText  mEdit_first;//第一次密码
    private EditText  mEdit_second;//第二次密码
    private Button    mBt_submit;
    private String    mFirstPass;
    private String    mPhoneNum="";//需提交的电话号码

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_new_pass_word, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mEdit_first = (EditText) mRootView.findViewById(R.id.edit_first);
        mEdit_second = (EditText) mRootView.findViewById(R.id.edit_second);
        mBt_submit = (Button) mRootView.findViewById(R.id.bt_submit);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mBt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.bt_submit:
                mFirstPass = String.valueOf(mEdit_first.getText()).trim();
                String secondPass = String.valueOf(mEdit_second.getText()).trim();
                if (mFirstPass.equals("") || secondPass.equals("")) {
                    ToastUtils.makeShortText("新密码不能为空", getActivity());
                    return;
                }
                if (!mFirstPass.equals(secondPass)) {
                    ToastUtils.makeShortText("两次密码不一致，请重新输入", getActivity());
                } else {
                    SendToIntnet();
                }
                break;
        }
    }

    private void SendToIntnet() {
        String checkMessage = NetConfig.BACK_PSW;
        RequestParams params = new RequestParams();
        params.put("telephone", mPhoneNum);
        params.put("new_psw", mFirstPass);
        HttpUtil.get(checkMessage, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getActivity());
                    return;
                }
                String result = clubDetailInfo.getResult();
                if (result.equals("2")) {
                    ToastUtils.makeShortText(clubDetailInfo.getMessage(), getActivity());
                    getActivity().finish();
                }
            }
        });
    }

    public void setPhoneNum(String phoneNum){
        mPhoneNum = phoneNum;
    }
}
