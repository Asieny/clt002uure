package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.views.NumberProgressBar;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/31 16:45
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class TakePartInSceneFragment extends Fragment implements View.OnClickListener {
    public  View              mRootView;
    private TextView          mTv_lable_1;//标签1
    private TextView          mTv_lable_2;//标签2
    private TextView          mTv_lable_3;//标签3
    private EditText          mEdit_comment;//书写内容
    private RecyclerView      mRecyclerView_pic;//要提交的图片
    private Button            mBt_submit;//提交按钮
    private GridLayoutManager mLayoutManager;
    private MyAdapter         mMyAdapter;
    private              List<Bitmap> mBitmapList = new ArrayList<Bitmap>();//给recyclerview添加的bitmap集合
    private static final int          IMAGE       = 1;//调用系统相册-选择图片
    private              String       mImgPath    = "";//记录相册返回的本地图片地址
    private              List<String> mPicUrlList = new ArrayList<>();//记录图片上传网络后，服务器返回的图片在服务器上的地址
    private              List<String> mState      = new ArrayList<>();//记录图片上传网络成功还是失败
    private ProgressDialog progressDialog;
    private String UserID        = NetConfig.UserID;
    private String gc_type       = "1";
    private String IntnetImgPath = "";//记录上传图片后，服务器返回的地址
    private Button       mBt_join_red;
    private LinearLayout mLinear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_take_part_in_scene, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mLinear = (LinearLayout) mRootView.findViewById(R.id.linear);
        mTv_lable_1 = (TextView) mRootView.findViewById(R.id.tv_lable_1);
        mTv_lable_2 = (TextView) mRootView.findViewById(R.id.tv_lable_2);
        mTv_lable_3 = (TextView) mRootView.findViewById(R.id.tv_lable_3);
        mEdit_comment = (EditText) mRootView.findViewById(R.id.edit_comment);
        mRecyclerView_pic = (RecyclerView) mRootView.findViewById(R.id.recyclerView_take_part_in);
        mBt_submit = (Button) mRootView.findViewById(R.id.bt_submit);
    }

    private void initData() {
        mLinear.setOnClickListener(this);
        mTv_lable_1.setOnClickListener(this);
        mTv_lable_2.setOnClickListener(this);
        mTv_lable_3.setOnClickListener(this);
        mBt_submit.setOnClickListener(this);

        //添加初始展示的图片
        Resources res = getActivity().getResources();
        Bitmap mBm = BitmapFactory.decodeResource(res, R.drawable.add_picture);
        mBitmapList.add(mBm);
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView_pic.setLayoutManager(mLayoutManager);
        mMyAdapter = new MyAdapter((ArrayList<Bitmap>) mBitmapList);
        // 设置布局管理器
        mRecyclerView_pic.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView_pic.setAdapter(mMyAdapter);
    }

    @Override
    public void onClick(View v) {
        String comment = String.valueOf(mEdit_comment.getText()).trim();
        switch (v.getId()) {
            case R.id.linear:
                break;
            case R.id.tv_lable_1:
                gc_type = "我在现场";
                mTv_lable_1.setTextColor(Color.YELLOW);
                mTv_lable_2.setTextColor(getResources().getColor(R.color.vm_black_87));
                mTv_lable_3.setTextColor(getResources().getColor(R.color.vm_black_87));
                break;
            case R.id.tv_lable_2:
                gc_type = "戏剧";
                mTv_lable_1.setTextColor(getResources().getColor(R.color.vm_black_87));
                mTv_lable_2.setTextColor(Color.YELLOW);
                mTv_lable_3.setTextColor(getResources().getColor(R.color.vm_black_87));
                break;
            case R.id.tv_lable_3:
                gc_type = "音乐会";
                mTv_lable_1.setTextColor(getResources().getColor(R.color.vm_black_87));
                mTv_lable_2.setTextColor(getResources().getColor(R.color.vm_black_87));
                mTv_lable_3.setTextColor(Color.YELLOW);
                break;
            case R.id.bt_submit:
                if (comment.equals("") || comment.equals("请输入文字")) {
                    ToastUtils.makeShortText("内容不能为空", getActivity());
                    return;
                }
                if (mPicUrlList.size() == 0) {
                    ToastUtils.makeShortText("请上传现场活动图片", getActivity());
                    return;
                }
                //广场发布
                sendComment(comment);
                break;
        }
    }

    private void sendPic(String upImg, Bitmap bm, final int postion) {
        File file = new File(mImgPath);
        if (file.exists() && file.length() > 0) {
            String strByBase64 = Bitmap2StrByBase64(bm);
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
                    RecruitVolunteerInfo recruitDetailInfo = gson.fromJson(response.toString(), RecruitVolunteerInfo.class);
                    int result = recruitDetailInfo.getResult();
                    if (result == 2) {
                        ToastUtils.makeShortText("图片上传成功", getActivity());
                        IntnetImgPath = recruitDetailInfo.getFileName();//IntnetImgPath=aa.jpg
                        mPicUrlList.set(postion-1,IntnetImgPath);
                        mState.set(postion-1, "成功");
                    } else {
                        ToastUtils.makeShortText("图片上传失败", getActivity());
                        mPicUrlList.set(postion-1,"");
                        mState.set(postion-1, "失败");
                    }
                    mMyAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_LONG).show();
        }
    }


    //RecyclerView的适配器
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<Bitmap> mData;

        public MyAdapter(ArrayList<Bitmap> data) {
            this.mData = data;
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
                holder.mProgressBar.setVisibility(View.GONE);
                holder.tv_pro_uping.setVisibility(View.GONE);
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
                holder.mProgressBar.setVisibility(View.VISIBLE);
                holder.tv_pro_uping.setVisibility(View.VISIBLE);
                String upSorF = mState.get(position - 1);
                if (upSorF.equals("")) {
                    holder.mProgressBar.setMax(100);
                    holder.mProgressBar.setProgress(50);
                    holder.tv_pro_uping.setText("正在上传");
                } else if (upSorF.equals("成功")) {
                    holder.mProgressBar.setMax(100);
                    holder.mProgressBar.setProgress(100);
                    holder.tv_pro_uping.setText("上传成功");
                } else {
                    holder.mProgressBar.setMax(100);
                    holder.mProgressBar.setProgress(0);
                    holder.tv_pro_uping.setText("上传失败");
                }
            }

            holder.img_delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBitmapList.remove(position);
                    mState.remove(position - 1);
                    mPicUrlList.remove(position - 1);
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
            ImageView         img_add_photo;
            ImageView         img_delet;
            NumberProgressBar mProgressBar;
            TextView          tv_pro_uping;

            public ViewHolder(View itemView) {
                super(itemView);
                img_add_photo = (ImageView) itemView.findViewById(R.id.img_add_photo);
                img_delet = (ImageView) itemView.findViewById(R.id.img_delet);
                mProgressBar = (NumberProgressBar) itemView.findViewById(R.id.progressbar_upimg);
                tv_pro_uping = (TextView) itemView.findViewById(R.id.tv_pro_uping);
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
//        Bitmap bm = BitmapFactory.decodeFile(imgPath);

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

        mImgPath = imgPath;
        //添加到bitmap集合中
        mBitmapList.add(bm);
        mPicUrlList.add("");
        mState.add("");
        mMyAdapter.notifyDataSetChanged();
        //上传图片
        sendPic(NetConfig.UPLOAD_BASE64, bm, mBitmapList.size() - 1);
    }

    private void sendComment(final String comment) {
        sendToInnet(comment, gc_type);
    }

    private void sendToInnet(String comment, String type) {
        String insert_gc = NetConfig.INSERT_GC;
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID =NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            RequestParams params = new RequestParams();
            params.put("userid", UserID);
            params.put("fcontent", comment);
            params.put("gc_type", type);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < mPicUrlList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("fpic", mPicUrlList.get(i));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            params.put("entryList", jsonArray);
            params.setUseJsonStreamer(true);
            HttpUtil.post(insert_gc, params, new HttpUtil.JsonHttpResponseUtil() {
                @Override
                public void onStart() {
                    super.onStart();
                    ProgressDialogUtil.startShow(getActivity(), "正在提交，请稍后");
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
                            FragmentManager fragmentManager = getFragmentManager();
                            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                            if (backStackEntryCount > 1) {
                                fragmentManager.popBackStack();
                            } else {
                                getActivity().finish();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            mBt_join_red.setVisibility(View.VISIBLE);
        }
    }

    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 30, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
