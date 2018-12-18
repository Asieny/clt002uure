package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.CultureVolunteerActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/27 14:58
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class SignUpVolunteerFragment extends Fragment {
    private View     mRootView;
    private TextView mTitel;
    private EditText mName;     //姓名
    private EditText mPhoneNum; //电话
    private EditText mAge;      //年龄
    private EditText mIntroduce;//自我介绍
    private TextView mEducation;    //显示学历
    private TextView mClubVolunteer;//志愿者团体
    private CheckBox mCheckb_man;   //选择性别（男）
    private CheckBox mCheckb_wom;   //选择性别（女）
    private String mSex = "0";//记录性别
    private Button            mChoiceEducation;//选择学历
    private Button            mChoiceClub;     //选择团体
    private RecyclerView      mRec_photo_apply; //添加照片墙
    private GridLayoutManager mLayoutManager;
    private Button            mWantApply;      //提交报名
    private boolean isChoicedclub      = false; //是否已选择意向志愿者团体
    private boolean isChoicedEducation = false; //是否已选择学历
    private ProgressDialog progressDialog;
    private static final int IMAGE = 1;//调用系统相册-选择图片

    private String[]     educationTypes = {"初中", "高中", "大专", "本科", "硕士", "博士"};
    private String[]     clubTypes      = {"不限", "上海爱乐乐团志愿者", "文化体验师", "上海国际喜剧节志愿者", "文化云志愿者", "中华艺术宫", "上海话剧艺术中心", "上海当代艺术博物馆", "上海博物馆"};
    private List<Bitmap> mBitmapList    = new ArrayList<Bitmap>();
    private MyAdapter      mMyAdapter;
    private ImageView      mIv_back;
    private RelativeLayout mRelative;
    private LinearLayout   mLl_want_to;
    private boolean isFromDetail  = false;//是否来自详情
    private String  actID         = "";
    private String  mImgPath      = "";
    private String  IntnetImgPath = "";
    private String  UserID        = NetConfig.UserID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_signup_volunteer, null);
        initView();
        initData();
        return mRootView;
    }

    public void setIsFromDetail(boolean mIsFromDetail) {
        isFromDetail = mIsFromDetail;
    }

    public void setActID(String actId) {
        actID = actId;
    }

    private void initView() {
        mRelative = (RelativeLayout) mRootView.findViewById(R.id.relative);
        mLl_want_to = (LinearLayout) mRootView.findViewById(R.id.ll_want_to);
        mTitel = (TextView) mRootView.findViewById(R.id.login);
        mIv_back = (ImageView) mRootView.findViewById(R.id.iv_back);

        mName = (EditText) mRootView.findViewById(R.id.edit_name);
        mPhoneNum = (EditText) mRootView.findViewById(R.id.edit_phone_num);
        mAge = (EditText) mRootView.findViewById(R.id.edit_age);
        mIntroduce = (EditText) mRootView.findViewById(R.id.et_introduce);

        mEducation = (TextView) mRootView.findViewById(R.id.tv_education);
        mClubVolunteer = (TextView) mRootView.findViewById(R.id.tv_club_volunteer);

        mCheckb_man = (CheckBox) mRootView.findViewById(R.id.checkb_man);
        mCheckb_wom = (CheckBox) mRootView.findViewById(R.id.checkb_wom);
        mChoiceEducation = (Button) mRootView.findViewById(R.id.bt_choice_education);
        mChoiceClub = (Button) mRootView.findViewById(R.id.bt_choice_club);
        mWantApply = (Button) mRootView.findViewById(R.id.bt_want_apply);

        mRec_photo_apply = (RecyclerView) mRootView.findViewById(R.id.rec_photo_apply);
    }

    private void initData() {
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        //给整个布局设置一个空点击事件，防止有点击事件渗透到前一个fragment上
        mRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //设置标题
        mTitel.setText("文化云·文化志愿者报名");
        //根据从哪个界面跳转来判断 意向志愿者是否显示
        if (isFromDetail) {
//            actID = CultureVolunteerActivity.actID;
            mLl_want_to.setVisibility(View.GONE);
        }
        //性别选择
        mCheckb_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckb_wom.setChecked(false);
                    mSex = "0";
                }
            }
        });
        mCheckb_wom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckb_man.setChecked(false);
                    mSex = "1";
                }
            }
        });

        //点击学历选择按钮，弹出popupwindow选择
        mChoiceEducation.setOnClickListener(new View.OnClickListener() {
            private ListView mListViewType;

            @Override
            public void onClick(View v) {
                if (isChoicedEducation == true) {
                    mChoiceEducation.setText("选择");
                } else {
                    mChoiceEducation.setText("取消");
                }
                isChoicedEducation = !isChoicedEducation;
                //弹出popupwindow让用户选择学历
                final PopupWindow popupWindow = new PopupWindow(getActivity());
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.popup_club_type, null));
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                //显示popupwindow,指定位置
                popupWindow.showAtLocation(mChoiceEducation, Gravity.CENTER, 0, 0);
                //找到学历展示条目
                mListViewType = (ListView) popupWindow.getContentView().findViewById(R.id.lv_club_type);
                MyAdapterType myAdapter = new MyAdapterType();
                mListViewType.setAdapter(myAdapter);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (popupWindow.isShowing()) {
                            mChoiceEducation.setText("取消");
                            isChoicedEducation = true;
                        } else {
                            mChoiceEducation.setText("选择");
                            isChoicedEducation = false;
                        }
                    }
                });
                //给学历条目设置点击事件
                mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //找到显示社团类型的textview
                        mEducation.setText(educationTypes[position]);
                        popupWindow.dismiss();
                        mChoiceEducation.setText("选择");
                    }
                });
            }

            class MyAdapterType extends BaseAdapter {

                @Override
                public int getCount() {
                    return educationTypes.length;
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

                    convertView = View.inflate(getActivity(), R.layout.list_item_club_type, null);
                    TextView tvListType = (TextView) convertView.findViewById(R.id.tv_list_type);
                    tvListType.setText(educationTypes[position]);
                    return convertView;
                }
            }
        });
        //设置意向志愿者团体选择项
        mChoiceClub.setOnClickListener(new View.OnClickListener() {
            private ListView mListViewType;

            @Override
            public void onClick(View v) {
                if (isChoicedclub == true) {
                    mChoiceClub.setText("选择");
                } else {
                    mChoiceClub.setText("取消");
                }
                isChoicedclub = !isChoicedclub;
                //弹出popupwindow让用户选择团体
                final PopupWindow popupWindow = new PopupWindow(getActivity());
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.popup_club_type, null));
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                //显示popupwindow,并指定位置
                popupWindow.showAtLocation(mChoiceClub, Gravity.CENTER, 0, 0);
                //找到团体展示条目
                mListViewType = (ListView) popupWindow.getContentView().findViewById(R.id.lv_club_type);
                MyAdapterType myAdapter = new MyAdapterType();
                mListViewType.setAdapter(myAdapter);
                mListViewType.setDivider(new ColorDrawable(Color.WHITE));
                mListViewType.setDividerHeight(1);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (popupWindow.isShowing()) {
                            mChoiceClub.setText("取消");
                            isChoicedclub = true;
                        } else {
                            mChoiceClub.setText("选择");
                            isChoicedclub = false;
                        }
                    }
                });
                //给团体条目设置点击事件
                mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //找到显示社团类型的textview
                        if (position == 0) {
                            actID = "";
                            mClubVolunteer.setText("不限");
                        } else {
                            actID = CultureVolunteerActivity.IDList.get(position - 1);
                            mClubVolunteer.setText(CultureVolunteerActivity.NameList.get(position - 1));
                        }
                        popupWindow.dismiss();
                        mChoiceClub.setText("选择");
                    }
                });
            }

            class MyAdapterType extends BaseAdapter {

                @Override
                public int getCount() {
                    return CultureVolunteerActivity.NameList.size() + 1;
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

                    convertView = View.inflate(getActivity(), R.layout.list_item_club_type, null);
                    TextView tvListType = (TextView) convertView.findViewById(R.id.tv_list_type);
                    if (position == 0) {
                        tvListType.setText("不限");
                    } else {
                        tvListType.setText(CultureVolunteerActivity.NameList.get(position - 1));
                    }
                    return convertView;
                }
            }
        });
        //提交信息
        mWantApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取填写的信息   //并判断某些需填项是否为空
                UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
                if (userInfo == null) {
                    UserID = NetConfig.UserID;
                } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
                    UserID = NetConfig.UserID;
                } else {
                    UserID = userInfo.member_id;
                }

                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (String.valueOf(mName.getText()).trim().equals("")) {
                        Toast.makeText(getActivity(), "姓名不能为空", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(mPhoneNum.getText()).trim().equals("")) {
                        Toast.makeText(getActivity(), "电话不能为空", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(mAge.getText()).trim().equals("")) {
                        Toast.makeText(getActivity(), "年龄不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!RegexUtils.checkMobile(String.valueOf(mPhoneNum.getText()).trim())) {
                            Toast.makeText(getActivity(), R.string.tip_account_regex_not_right,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            //判断完，先提交图片，获取图片网络地址
                            String upImg = NetConfig.URL_CHANGE_UPLOAD_BASE64;
                            if (mImgPath.equals("")) {
                                //直接提交到服务器，不走上传图片
                                sendToIntnet();
                            } else {
                                File file = new File(mImgPath);
                                String strByBase64 = Bitmap2StrByBase64(mBitmapList.get(1));
                                if (file.exists() && file.length() > 0) {
                                    RequestParams param = new RequestParams();
                                    param.put("imgStr", strByBase64);
                                    HttpUtil.get(upImg, param, new HttpUtil.JsonHttpResponseUtil() {
                                        @Override
                                        public void onStart() {
                                            super.onStart();
                                            showProgressDialog("正在上传图片，请稍后");
                                        }

                                        @Override
                                        public void onFinish() {
                                            super.onFinish();
                                            hideProgressDialog();
                                        }

                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            Gson gson = new Gson();
                                            RecruitVolunteerInfo recruitDetailInfo;
                                            recruitDetailInfo = gson.fromJson(response.toString(), RecruitVolunteerInfo.class);
                                            int result = recruitDetailInfo.getResult();
                                            IntnetImgPath = recruitDetailInfo.getFileName();
                                            if (result == 2) {
                                                ToastUtils.makeShortText("图片上传成功", getActivity());
                                            } else {
                                                ToastUtils.makeShortText("图片上传失败", getActivity());
                                            }
                                            //提交所有数据到服务器
                                            sendToIntnet();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }
        });
        //设置左上图标后退键
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
                //显示前一个已隐藏的fragment
            }
        });
        //添加初始展示的图片
        Resources res = getActivity().getResources();
        Bitmap mBm = BitmapFactory.decodeResource(res, R.drawable.add_picture);
        mBitmapList.add(mBm);

        //需上传的照片墙
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mMyAdapter = new MyAdapter((ArrayList<Bitmap>) mBitmapList);
        // 设置布局管理器
        mRec_photo_apply.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRec_photo_apply.setAdapter(mMyAdapter);
    }

    private void sendToIntnet() {
        String registerVolunteer = NetConfig.REGISTER_VOLUNTEER;
        String Fname = String.valueOf(mName.getText()).trim();
        String fmobile = String.valueOf(mPhoneNum.getText()).trim();
        String fage = String.valueOf(mAge.getText()).trim();
        String Fsex = mSex;
        String Graduate = String.valueOf(mEducation.getText()).trim();
        String introduction = String.valueOf(mIntroduce.getText()).trim();
        String Photo = IntnetImgPath;
        String Volunteer_activity_id = actID;

        RequestParams params = new RequestParams();
        params.put("member_id", UserID);
        params.put("Fname", Fname);
        params.put("fmobile", fmobile);
        params.put("fage", fage);
        params.put("Fsex", Fsex);
        params.put("Graduate", Graduate);
        params.put("introduction", introduction);
        params.put("Photo", Photo);
        params.put("Volunteer_activity_id", Volunteer_activity_id);
        params.setUseJsonStreamer(true);
        HttpUtil.post(registerVolunteer, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("正在提交内容，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
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
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    if (result.equals("2")) {
                        //弹出回退栈最上面的fragment
                        getFragmentManager().popBackStackImmediate(null, 0);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册返回，获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imgPath) {
        //        Bitmap bm = BitmapFactory.decodeFile(imaePath);

        //压缩图片
        File file = new File(imgPath);
        File newFile = new CompressHelper.Builder(getActivity())
                .setMaxWidth(720)  // 默认最大宽度为720
                .setMaxHeight(960) // 默认最大高度为960
                .setQuality(100)    // 默认压缩质量为80
                .setFileName("sendPic") // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
        Bitmap bm = BitmapFactory.decodeFile(newFile.getPath());

        //添加到bitmap集合中
        if (mBitmapList.size() < 2) {
            mBitmapList.add(bm);
        } else {
            mBitmapList.set(1, bm);
        }
        mImgPath = imgPath;
        mMyAdapter.notifyDataSetChanged();
    }

    //RecyclerView的适配器
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<Bitmap> mData;

        public MyAdapter(ArrayList<Bitmap> data) {
            this.mData = data;
        }

        public void updateData(ArrayList<Bitmap> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_recy_item, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position == 0) {
                //第一个条目不显示删除按键
                holder.img_delet.setVisibility(View.GONE);
                holder.img_add_photo.setImageBitmap(mBitmapList.get(position));
                // 设置点击事件添加图片
                holder.img_add_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //调用相册
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, IMAGE);
                    }
                });
            } else {
                holder.img_add_photo.setImageBitmap(mBitmapList.get(position));
            }

            holder.img_delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBitmapList.remove(position);
                    mImgPath = "";
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            //展示条目数
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_add_photo;
            ImageView img_delet;

            public ViewHolder(View itemView) {
                super(itemView);
                img_add_photo = (ImageView) itemView.findViewById(R.id.img_add_photo);
                img_delet = (ImageView) itemView.findViewById(R.id.img_delet);
            }
        }
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setInverseBackgroundForced(false);//对话框后面的窗体不获得焦点
        progressDialog.setCanceledOnTouchOutside(false);//旁击不消失
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
