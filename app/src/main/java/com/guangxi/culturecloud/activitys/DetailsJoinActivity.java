package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/26 9:03
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class DetailsJoinActivity extends BaseActivity {

    private EditText mName;
    private TextView mType;
    private EditText mContacts;
    private EditText mPhoneNum;
    private EditText mIntroduction;
    private Button   mSubmit;
    private Button   mChoiceType;
    private String[] clubTypes = {"民营演艺公司", "社区活动团队", "国有剧团", "民办非企业单位", "基金会/协会", "其他"};
    private boolean  isChoiced = false;
    private ImageView      mIv_back;
    private ProgressDialog progressDialog;
    private String UserID    = NetConfig.UserID;
    private int    whichType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_join);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mName = (EditText) findViewById(R.id.edit_name);
        mType = (TextView) findViewById(R.id.tv_type);
        mContacts = (EditText) findViewById(R.id.edit_contacts);
        mPhoneNum = (EditText) findViewById(R.id.edit_phone_num);
        mIntroduction = (EditText) findViewById(R.id.edit_introduction);
        mChoiceType = (Button) findViewById(R.id.bt_choice_type);
        mSubmit = (Button) findViewById(R.id.bt_submit);

    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(mName.getText()).trim();
                String contacts = String.valueOf(mContacts.getText()).trim();
                String phoneNum = String.valueOf(mPhoneNum.getText()).trim();
                String introduction = String.valueOf(mIntroduction.getText()).trim();

                if (name.equals("请输入名称") || name.equals("")) {
                    Toast.makeText(getBaseContext(), "社团名称不能为空", Toast.LENGTH_SHORT).show();
                } else if (contacts.equals("请输入联系人") || contacts.equals("")) {
                    Toast.makeText(getBaseContext(), "联系人不能为空", Toast.LENGTH_SHORT).show();
                } else if (phoneNum.equals("请输入电话") || phoneNum.equals("")) {
                    Toast.makeText(getBaseContext(), "电话不能为空", Toast.LENGTH_SHORT).show();
                } else if (introduction.equals("请输入简介") || introduction.equals("")) {
                    Toast.makeText(getBaseContext(), "简介不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (!RegexUtils.checkMobile(phoneNum)) {
                        Toast.makeText(DetailsJoinActivity.this, R.string.tip_account_regex_not_right,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    //提交表单社团申请信息给服务器
                    SendInfo();
                }
            }
        });
        mChoiceType.setOnClickListener(new View.OnClickListener() {
            private ListView mListViewType;

            @Override
            public void onClick(View v) {
                if (isChoiced == true) {
                    mChoiceType.setText("选择");
                } else {
                    mChoiceType.setText("取消");
                }
                isChoiced = !isChoiced;
                //弹出popupwindow让用户选择社团类型
                final PopupWindow popupWindow = new PopupWindow(getBaseContext());
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(LayoutInflater.from(getBaseContext()).inflate(R.layout.popup_club_type, null));
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                //显示popupwindow,并指定位置
                popupWindow.showAtLocation(mChoiceType, Gravity.CENTER, 0, 0);
                //找到社团类型展示条目
                mListViewType = (ListView) popupWindow.getContentView().findViewById(R.id.lv_club_type);
                MyAdapterType myAdapter = new MyAdapterType();
                mListViewType.setAdapter(myAdapter);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (popupWindow.isShowing()) {
                            mChoiceType.setText("取消");
                            isChoiced = true;
                        } else {
                            mChoiceType.setText("选择");
                            isChoiced = false;
                        }

                    }
                });
                //给社团类型的条目设置点击事件
                mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //找到显示社团类型的textview
                        mType.setText(clubTypes[position]);
                        popupWindow.dismiss();
                        mChoiceType.setText("选择");
                    }
                });
            }

            class MyAdapterType extends BaseAdapter {

                @Override
                public int getCount() {
                    return clubTypes.length;
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

                    convertView = View.inflate(getBaseContext(), R.layout.list_item_club_type, null);
                    TextView tvListType = (TextView) convertView.findViewById(R.id.tv_list_type);
                    tvListType.setText(clubTypes[position]);
                    whichType = position;
                    return convertView;
                }
            }
        });
    }

    private void SendInfo() {
        String jion = NetConfig.INSERT_LOVESOCIETY;
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            RequestParams params = new RequestParams();
            params.put("member_id", UserID);
            params.put("fname", mName.getText());
            params.put("fproperties", whichType);
            params.put("fcontacts", mContacts.getText());
            params.put("phone", mPhoneNum.getText());
            params.put("fintroduction", mIntroduction.getText());
            params.setUseJsonStreamer(true);
            HttpUtil.post(jion, params, new HttpUtil.JsonHttpResponseUtil() {
                @Override
                public void onStart() {
                    super.onStart();
                    ProgressDialogUtil.startShow(DetailsJoinActivity.this, "正在提交，请稍后");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    ProgressDialogUtil.hideDialog();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (statusCode == 200) {
                        ClubDetailInfo clubDetailInfo;
                        Gson gson = new Gson();
                        clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                        String result = clubDetailInfo.getResult();
                        String message = clubDetailInfo.getMessage();
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                        if (result.equals("2")) {
                            finish();
                        }
                    }
                }
            });
        }
    }
}
