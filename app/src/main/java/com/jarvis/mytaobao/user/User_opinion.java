package com.jarvis.mytaobao.user;

import android.app.Activity;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.activitys.UseHelpActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
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
 * 意见反馈界面
 *
 * @author http://yecaoly.taobao.com
 */
public class User_opinion extends BaseActivity implements OnClickListener {

    private ImageView      mImg_back;
    private RelativeLayout mRelative_help;
    private EditText       mEdit_comment;
    private TextView       mTv_leve_word;
    private Button         mBt_submit;
    private ProgressDialog progressDialog;
    private String UserID = NetConfig.UserID;
    private RecyclerView mRec_photo_apply;
    private List<Bitmap> mBitmapList = new ArrayList<Bitmap>();
    private GridLayoutManager mLayoutManager;
    private static final int    IMAGE         = 1;//调用系统相册-选择图片
    private              String mImgPath      = "";//图片本地地址
    private              String IntnetImgPath = "";//图片上传到服务器地址
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_opinion);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mRelative_help = (RelativeLayout) findViewById(R.id.relative_help);
        mEdit_comment = (EditText) findViewById(R.id.edit_comment);
        mTv_leve_word = (TextView) findViewById(R.id.tv_leve_word);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
        mRec_photo_apply = (RecyclerView) findViewById(R.id.rec_photo_apply);
    }

    private void initData() {
        mImg_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRelative_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UseHelpActivity.class);
                startActivity(intent);
            }
        });
        mEdit_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String comment = String.valueOf(mEdit_comment.getText()).trim();
                if (comment.equals("您有什么意见请提给我们")) {
                    mEdit_comment.setText("");
                    return true;
                } else {
                    return false;
                }
            }
        });
        mEdit_comment.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;
            private int mark = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
                mark++;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //                mTv_leve_word.
            }

            @Override
            public void afterTextChanged(Editable s) {
                //                editStart = mEdit_comment.getSelectionStart();
                //                editEnd = mEdit_comment.getSelectionEnd();
                if (mark < 2) {
                    mTv_leve_word.setText("剩余" + 120 + "个字");
                } else {
                    mTv_leve_word.setText("剩余" + (120 - temp.length()) + "个字");
                }
                if (temp.length() > 120) {
                    mTv_leve_word.setText("字数超出限制，请删除");
                    Toast.makeText(getBaseContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
                    //                    s.delete(editStart - 1, editEnd);
                    //                    int tempSelection = editStart;
                    //                    mEdit_comment.setText(s);
                    //                    mEdit_comment.setSelection(tempSelection);
                }
            }
        });

        //添加初始展示的图片
        Resources res = getResources();
        Bitmap mBm = BitmapFactory.decodeResource(res, R.drawable.add_picture);
        mBitmapList.add(mBm);

        //需上传的照片墙
        mLayoutManager = new GridLayoutManager(getBaseContext(), 3, GridLayoutManager.VERTICAL, false);
        mMyAdapter = new MyAdapter((ArrayList<Bitmap>) mBitmapList);
        // 设置布局管理器
        mRec_photo_apply.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRec_photo_apply.setAdapter(mMyAdapter);

        mBt_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEdit_commentText = String.valueOf(mEdit_comment.getText()).trim();
                if (mEdit_commentText.equals("您有什么意见请提给我们") || mEdit_commentText.equals("")) {
                    ToastUtils.makeShortText("输入内容不能为空", getBaseContext());
                    return;
                }

                final String UrlInsertHelp = NetConfig.INSERT_HELP;
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
                    //判断用户是否要提交图片
                    String upImg = NetConfig.UPLOAD_BASE64;
                    if (mImgPath.equals("")) {
                        //为空直接提交评论内容
                        sendToIntnet(UrlInsertHelp, mEdit_commentText, "");
                    } else {
                        //不为空先提交图片，在提交内容
                        File file = new File(mImgPath);
                        String strByBase64 = Bitmap2StrByBase64(mBitmapList.get(1));
                        if (file.exists() && file.length() > 0) {
                            RequestParams param = new RequestParams();
                            param.put("imgStr", strByBase64);
                            HttpUtil.get(upImg, param, new HttpUtil.JsonHttpResponseUtil() {
                                @Override
                                public void onStart() {
                                    super.onStart();
                                    ProgressDialogUtil.startShow(User_opinion.this, "正在上传图片，请稍后");
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
                                    if (result == 2) {
                                        IntnetImgPath = recruitDetailInfo.getFileName();
                                        ToastUtils.makeShortText("图片上传成功", getBaseContext());
                                    } else {
                                        ToastUtils.makeShortText("图片上传失败", getBaseContext());
                                    }
                                    //提交所有数据到服务器
                                    sendToIntnet(UrlInsertHelp, mEdit_commentText, IntnetImgPath);
                                }
                            });
                        } else {
                            Toast.makeText(getBaseContext(), "文件不存在", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void sendToIntnet(String UrlInsertHelp, String mEdit_commentText, String IntnetImgPath) {
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        params.put("help_content", mEdit_commentText);
        params.setUseJsonStreamer(true);
        HttpUtil.post(UrlInsertHelp, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(User_opinion.this, "正在提交，请稍后");
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

    @Override
    public void onClick(View v) {

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
            Cursor c = getBaseContext().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
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
        File newFile = new CompressHelper.Builder(this)
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
