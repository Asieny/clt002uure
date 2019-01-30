package com.guangxi.culturecloud.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.DataCleanManager;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.jarvis.mytaobao.user.MessageEvent;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.com.mytest.file.FileUtil;
import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zww on 2017/12/19.
 * 设置页面
 */

public class SettingAvtivity extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView       ivBack;
    @InjectView(R.id.login)
    TextView        login;
    @InjectView(R.id.iv_refresh)
    ImageView       ivRefresh;
    @InjectView(R.id.title)
    LinearLayout    title;
    @InjectView(R.id.update_pwd)
    LinearLayout    updatePwd;
    @InjectView(R.id.newcode)
    TextView        newcode;
    @InjectView(R.id.update)
    LinearLayout    update;
    @InjectView(R.id.data)
    TextView        data;
    @InjectView(R.id.clean)
    LinearLayout    clean;
    @InjectView(R.id.btnexit)
    Button          btnexit;
    @InjectView(R.id.linear_head_img)
    LinearLayout    linear_head_img;
    @InjectView(R.id.circleImg)
    CircleImageView circleImg;

    private SharedPreferences        sp;
    private SharedPreferences.Editor ed;
    private static final int    IMAGE    = 1;//调用系统相册-选择图片
    private              String mImgPath = "";
    private ProgressDialog progressDialog;
    private String UserID         = NetConfig.UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        ButterKnife.inject(this);
        //获取用户ID
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        setUserHeadImg();
        try {
            data.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListener();
    }

    private void setUserHeadImg() {
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/icon_bitmap/UserHeadIcon.jpg";
        File file = new File(path);
        //判断是否登录保存过头像
        if (file.exists()){
            //工具类有缓存，清除，防止显示错误图片
            ImageLoaderUtil.clean();
            ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(path), circleImg);
//            ImageLoaderUtil.displayImageRoundIcon("file:/"+path,circleImg);
        }
//        String path =getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/icon_bitmap/UserHeadIcon.jpg";
//        ImageLoaderUtil.clean();
//        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(path), circleImg);
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        updatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingAvtivity.this, UpdatePwdActivity.class);
                startActivity(intent);
            }
        });
        clean.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cleanAlertDialog();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 版本检测方式2：带更新回调监听
                //                PgyUpdateManager.register(SettingAvtivity.this, new UpdateManagerListener() {
                //                    @Override
                //                    public void onUpdateAvailable(final String result) {
                //                        final AppBean appBean = getAppBeanFromString(result);
                //
                //                        new AlertDialog.Builder(SettingAvtivity.this,AlertDialog.THEME_HOLO_LIGHT).setTitle("更新")
                //                                .setMessage(appBean.getReleaseNote())
                //                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                //
                //                                    @Override
                //                                    public void onClick(DialogInterface dialog, int which) {
                //                                        startDownloadTask(SettingAvtivity.this, appBean.getDownloadURL());
                //                                    }
                //                                })
                //                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                //                                    @Override
                //                                    public void onClick(DialogInterface dialog, int which) {
                //                                        dialog.cancel();
                //                                    }
                //                                })
                //                                .show();
                //
                //                    }
                //
                //                    @Override
                //                    public void onNoUpdateAvailable() {
                //                        Toast.makeText(SettingAvtivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                //                    }
                //                });
            }
        });
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAlertDialog();
            }
        });
        linear_head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
    }

    /**
     * 警告对话框
     */
    @SuppressLint("NewApi")
    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("温馨提示");
        builder.setMessage("你确定要退出当前登录帐号吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp = getSharedPreferences(UserInfo.class.getName(), MODE_PRIVATE);
                ed = sp.edit();
                ed.remove("userinfo");
                ed.commit();
                //清除头像图片文件
                clearHeadImg();
                Intent intent = new Intent();
                //                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(SettingAvtivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void clearHeadImg() {
        String path = getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/icon_bitmap/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myIconFile = new File(path + "UserHeadIcon.jpg");
        if (myIconFile.exists()) {
            myIconFile.delete();
        }
        //清除头像后，修改用户界面的头像图片
        changeUserHeadPic();
    }

    private void changeUserHeadPic() {
        EventBus.getDefault().post(new MessageEvent("OUTLogin"));
    }

    @SuppressLint("NewApi")
    private void cleanAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("温馨提示");
        builder.setMessage("你确定要清空所有数据吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 清除缓存操作
                DataCleanManager.clearAllCache(SettingAvtivity.this);
                DataCleanManager.cleanSharedPreference(SettingAvtivity.this);
                try {
                    // 清除缓存后的操作
                    data.setText(DataCleanManager.getTotalCacheSize(SettingAvtivity.this));
                    FileUtil.deleteDir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
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
        //添加到mImgPath中
        mImgPath = imgPath;
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(mImgPath), circleImg);
        //上传头像
//        Bitmap bm = BitmapFactory.decodeFile(mImgPath);

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

        sendHeadImgToIntnet(bm);
        try {
            saveFile(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MessageEvent(mImgPath));
    }

    private void sendHeadImgToIntnet(Bitmap bm) {
        //提交图片，获取图片网络地址
        String upImg = NetConfig.UPLOAD_BASE64;
        if (mImgPath.equals("")) {
            //没有图片地址，不做处理

        } else {
            File file = new File(mImgPath);
            String strByBase64 = Bitmap2StrByBase64(bm);
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
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Gson gson = new Gson();
                        RecruitVolunteerInfo recruitDetailInfo;
                        recruitDetailInfo = gson.fromJson(response.toString(), RecruitVolunteerInfo.class);
                        int result = recruitDetailInfo.getResult();
                        String fileName = recruitDetailInfo.getFileName();//回传的头像地址
                        if (result == 2) {
                            ToastUtils.makeShortText("头像上传成功", getBaseContext());
                            //上传头像地址
                            sendHeadUrlToIntnet(fileName);
                        } else {
                            hideProgressDialog();
                            ToastUtils.makeShortText("头像上传失败", getBaseContext());
                        }
                    }
                });
            } else {
                Toast.makeText(getBaseContext(), "文件不存在", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendHeadUrlToIntnet(String fileName) {
        //提交图片，获取图片网络地址
        String insertPic = NetConfig.INSERTPIC;
        RequestParams param = new RequestParams();
        param.put("user_id", UserID);
        param.put("headpic", fileName);
        HttpUtil.get(insertPic, param, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
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
                if (result == 0) {
                    ToastUtils.makeShortText("头像上传失败", getBaseContext());
                } else {
                    ToastUtils.makeShortText("头像上传成功", getBaseContext());
                }
            }
        });
    }

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public File saveFile(Bitmap bm) throws IOException {
        //        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/w65/icon_bitmap/";
        String path = getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/icon_bitmap/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myIconFile = new File(path + "UserHeadIcon.jpg");
        if (myIconFile.exists()) {
            myIconFile.delete();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myIconFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myIconFile;
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
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

