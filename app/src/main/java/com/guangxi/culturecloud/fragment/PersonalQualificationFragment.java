package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.R;
import com.jarvis.mytaobao.user.ProvideQualificationsActivity;
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
 * @创建时间 2018/1/15 13:15
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class PersonalQualificationFragment extends Fragment implements View.OnClickListener {
    private View              mRootView;
    private EditText          mEdit_name;
    private EditText          mEdit_job;
    private EditText          mEdit_year;
    private EditText          mEdit_introduce;
    private RecyclerView      mRec_photo_apply;
    private CheckBox          mCheck_rule;
    private TextView          mTv_rule;
    private Button            mBt_submit;
    private GridLayoutManager mLayoutManager;
    private MyAdapter         mMyAdapter;
    private List<Bitmap>      mBitmapList;
    private static final int    IMAGE    = 1;//调用系统相册-选择图片
    private              String mImgPath = "";
    private ProgressDialog progressDialog;
    private String intnetImgPath     = "";
    private String UserID            = NetConfig.UserID;
    private String mUrl_address      = NetConfig.URL_HEAD_ADDRESS;//定位的服务器地址
    private String mPost_img_address = NetConfig.URL_CHANGE_UPLOAD_BASE64;//定位的提交服务器地址
    private ProvideQualificationsActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_personal_qualification, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mEdit_name = (EditText) mRootView.findViewById(R.id.edit_name);
        mEdit_job = (EditText) mRootView.findViewById(R.id.edit_job);
        mEdit_year = (EditText) mRootView.findViewById(R.id.edit_year);
        mEdit_introduce = (EditText) mRootView.findViewById(R.id.edit_introduce);
        mRec_photo_apply = (RecyclerView) mRootView.findViewById(R.id.rec_photo_apply);
        mCheck_rule = (CheckBox) mRootView.findViewById(R.id.check_rule);
        mTv_rule = (TextView) mRootView.findViewById(R.id.tv_rule);
        mBt_submit = (Button) mRootView.findViewById(R.id.bt_submit);
    }

    private void initData() {
        mActivity = (ProvideQualificationsActivity) getActivity();
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        UserID = userInfo.member_id;
        mBt_submit.setOnClickListener(this);

        //添加初始展示的图片
        Resources res = getActivity().getResources();
        Bitmap mBm = BitmapFactory.decodeResource(res, R.drawable.add_picture);
        if (mBitmapList == null) {
            mBitmapList = new ArrayList<Bitmap>();
            mBitmapList.add(mBm);
        }
        //需上传的照片墙
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mMyAdapter = new MyAdapter((ArrayList<Bitmap>) mBitmapList);
        // 设置布局管理器
        mRec_photo_apply.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRec_photo_apply.setAdapter(mMyAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                String name = String.valueOf(mEdit_name.getText()).trim();
                String job = String.valueOf(mEdit_job.getText()).trim();
                String year = String.valueOf(mEdit_year.getText()).trim();
                String introduce = String.valueOf(mEdit_introduce.getText()).trim();
                boolean checked = mCheck_rule.isChecked();
                if (name.equals("") || name.equals("请输入个人姓名")) {
                    ToastUtils.makeShortText("姓名不能为空", getActivity());
                    return;
                }
                if (job.equals("") || job.equals("请输入头衔/职位")) {
                    ToastUtils.makeShortText("头衔/职位不能为空", getActivity());
                    return;
                }
                if (year.equals("") || year.equals("请输入从业年限")) {
                    ToastUtils.makeShortText("从业年限不能为空", getActivity());
                    return;
                }
                if (introduce.equals("") || introduce.equals("请输入自我介绍")) {
                    ToastUtils.makeShortText("自我介绍不能为空", getActivity());
                    return;
                }
                if (checked == false) {
                    ToastUtils.makeShortText("请先同意文化云平台使用协议和规则", getActivity());
                    return;
                }
                //提交
                SendPic();
                break;
            default:
                break;
        }
    }

    private void SendPic() {
        mPost_img_address = mActivity.getPost_img_address();
        //判断完，先提交图片，获取图片网络地址
        String upImg = mPost_img_address;
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
                        ProgressDialogUtil.startShow(getActivity(), "正在上传图片，请稍后");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtil.hideDialog();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Gson gson = new Gson();
                        RecruitVolunteerInfo recruitDetailInfo;
                        recruitDetailInfo = gson.fromJson(response.toString(), RecruitVolunteerInfo.class);
                        int result = recruitDetailInfo.getResult();
                        intnetImgPath = recruitDetailInfo.getFileName();
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

    private void sendToIntnet() {
        mUrl_address = mActivity.getUrl_address();
        String insertZzrzPersonnel = mUrl_address + "insertZzrzPersonnel";
        String name = String.valueOf(mEdit_name.getText()).trim();
        String job = String.valueOf(mEdit_job.getText()).trim();
        String year = String.valueOf(mEdit_year.getText()).trim();
        String introduce = String.valueOf(mEdit_introduce.getText()).trim();

        RequestParams params = new RequestParams();
        params.put("personnel_name", name);
        params.put("personel_job", job);
        params.put("job_age", year);
        params.put("personel_introduce", introduce);
        params.put("personel_work", intnetImgPath);
        params.put("user_id", UserID);
        params.setUseJsonStreamer(true);
        HttpUtil.post(insertZzrzPersonnel, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(getActivity(), "正在提交内容，请稍后");
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
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    if (result.equals("2")) {
                        getActivity().finish();
                    }
                }
            }

        });
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
