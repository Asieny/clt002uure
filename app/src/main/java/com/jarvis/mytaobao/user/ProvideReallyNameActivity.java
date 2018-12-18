package com.jarvis.mytaobao.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApplication;
import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.guangxi.culturecloud.activitys.ChooseCityActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.model.Society;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/15 9:21
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ProvideReallyNameActivity extends BaseActivity implements View.OnClickListener {

    private EditText  mEdit_name;
    private EditText  mEdit_credentials_number;
    //    private EditText mEdit_id_number;
    private ImageView mImg_id_pic;
    private Button    mBt_insert_pic;
    private Button    mBt_submit;
    private ImageView mIv_back;
    //调用系统相册-选择图片
    private static final int    IMAGE         = 1;
    private              String mImgPath      = "";
    private              String IntnetImgPath = "";
    private ProgressDialog progressDialog;
    private TextView       mIv_shao;//选择提交地区
    private final int REQUEST_CODE_CITY = 1001;
    private String city;
    private String mUrl_address      = NetConfig.URL_HEAD_ADDRESS;//定位的服务器地址
    private String mGet_img_address  = NetConfig.IMG;   //定位的服务器图片地址
    private String mPost_img_address = NetConfig.URL_CHANGE_UPLOAD_BASE64;//定位的提交服务器地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_really_name);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mIv_shao = (TextView) findViewById(R.id.iv_shao);
        mEdit_name = (EditText) findViewById(R.id.edit_name);
        mEdit_credentials_number = (EditText) findViewById(R.id.edit_credentials_number);
        //        mEdit_id_number = (EditText) findViewById(R.id.edit_ID_number);
        mImg_id_pic = (ImageView) findViewById(R.id.img_ID_pic);
        mBt_insert_pic = (Button) findViewById(R.id.bt_insert_pic);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mIv_shao.setText(MyApplication.cityName);
        mBt_insert_pic.setOnClickListener(this);
        mBt_submit.setOnClickListener(this);
        mEdit_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String name = String.valueOf(mEdit_name.getText()).trim();
                if (name.equals("请填写身份证上的真实姓名") || name.equals("")) {
                    mEdit_name.setText("");
                }
                return false;
            }
        });
        mEdit_credentials_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String credentials_number = String.valueOf(mEdit_credentials_number.getText()).trim();
                if (credentials_number.equals("请填写身份证号码") || credentials_number.equals("")) {
                    mEdit_credentials_number.setText("");
                }
                return false;
            }
        });
        //        mEdit_id_number.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                String id_number = String.valueOf(mEdit_id_number.getText()).trim();
        //                if (id_number.equals("请输入身份证号")||id_number.equals("")){
        //                    mEdit_id_number.setText("");
        //                    return true;
        //                }else {
        //                    return false;
        //                }
        //            }
        //        });
        mIv_shao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProvideReallyNameActivity.this, ChooseCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_insert_pic:
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
            case R.id.bt_submit:
                String name = String.valueOf(mEdit_name.getText()).trim();
                String credentials_number = String.valueOf(mEdit_credentials_number.getText()).trim();
                //                String id_number = String.valueOf(mEdit_id_number.getText()).trim();
                if (name.equals("") || name.equals("请填写身份证上的真实姓名")) {
                    ToastUtils.makeShortText("姓名不能为空", getBaseContext());
                    return;
                }
                if (credentials_number.equals("") || credentials_number.equals("请填写身份证号码")) {
                    ToastUtils.makeShortText("证件号不能为空", getBaseContext());
                    return;
                }
                //                if (id_number.equals("")||id_number.equals("请输入身份证号")){
                //                    ToastUtils.makeShortText("身份证号不能为空",getBaseContext());
                //                    return;
                //                }
                //判断是否放入证件照
                if (mImgPath.equals("")) {
                    ToastUtils.makeShortText("请放入证件照", getBaseContext());
                    return;
                }
                SendPicInfo();
                break;
            default:
                break;
        }
    }

    private void SendPicInfo() {
        //判断完，先提交图片，获取图片网络地址
        String upImg = mPost_img_address;
        File file = new File(mImgPath);
        if (file.exists() && file.length() > 0) {
            Bitmap bmp = ((BitmapDrawable) mImg_id_pic.getDrawable()).getBitmap();
            String strByBase64 = Bitmap2StrByBase64(bmp);
            RequestParams param = new RequestParams();
            param.put("imgStr", strByBase64);
            HttpUtil.get(upImg, param, new HttpUtil.JsonHttpResponseUtil() {
                @Override
                public void onStart() {
                    super.onStart();
                    ProgressDialogUtil.startShow(ProvideReallyNameActivity.this, "正在上传图片，请稍后");
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
                    IntnetImgPath = recruitDetailInfo.getFileName();
                    if (result == 2) {
                        ToastUtils.makeShortText("图片上传成功", getBaseContext());
                    } else {
                        ToastUtils.makeShortText("图片上传失败", getBaseContext());
                    }
                    //提交所有数据到服务器
                    sendToIntnet();
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "文件不存在", Toast.LENGTH_LONG).show();
        }
    }

    public void sendToIntnet() {
        String insertSmrz = mUrl_address + "insertSmrz";
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        String name = String.valueOf(mEdit_name.getText()).trim();
        String edit_credentials_number = String.valueOf(mEdit_credentials_number.getText()).trim();

        RequestParams params = new RequestParams();
        params.put("user_id", userInfo.member_id);
        params.put("fkind", "身份证");
        params.put("real_name", name);
        params.put("id_number", edit_credentials_number);
        params.put("pic", IntnetImgPath);
        params.setUseJsonStreamer(true);
        HttpUtil.post(insertSmrz, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ProvideReallyNameActivity.this, "正在提交内容，请稍后");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
        //获取选择城市地址
        if (requestCode == REQUEST_CODE_CITY) {
            if (data != null) {
                city = data.getStringExtra("city") + "市";
                mIv_shao.setText(city);
                //根据修改地址，修改所需提交的服务器；
                //获取地址请求头
                getUrlHead(city);
            }
        }
    }

    //加载图片
    private void showImage(String imgPath) {
        //        Bitmap bm = BitmapFactory.decodeFile(imgPath);
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
        mImgPath = imgPath;
        mImg_id_pic.setImageBitmap(bm);
    }

    private void getUrlHead(String cityName) {
        RequestParams params = new RequestParams();
        params.put("area", cityName);
        HttpUtil.get(NetConfig.FIRSTURL, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ProvideReallyNameActivity.this, "正在修改提交地址，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    String ress = response.toString();
                    if (null != ress && !ress.equals("") && !ress.equals("{}")) {
                        Gson gson = new Gson();
                        Society urlAddressInfo = gson.fromJson(response.toString(), Society.class);
                        try {
                            Society.ApiAddressBean apiAddress = urlAddressInfo.getApiAddress();
                            String url_address = apiAddress.getUrl_address();
                            String get_img_address = apiAddress.getGet_img_address();
                            //修改所有链接
                            changeAllLinks(url_address, get_img_address);
                        } catch (Exception e) {
                            ToastUtils.makeShortText("跳转省会城市资讯", getBaseContext());
                        }
                    }
                }
            }
        });
    }

    private void changeAllLinks(String url_address, String get_img_address) {
        mUrl_address = url_address;
        mGet_img_address = get_img_address;
        mPost_img_address = url_address + "uploadBase64";
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
