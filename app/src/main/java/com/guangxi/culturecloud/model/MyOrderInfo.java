package com.guangxi.culturecloud.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import cn.com.mytest.json.AbsJson;

public class MyOrderInfo extends AbsJson<MyOrderInfo>
{
    @Expose
    public String member_id;
    @Expose
    public String fname;
    @Expose
    public String newpic;
    @Expose
    public String name;
    @Expose
    public String phone;
    @Expose
    public String amount;
    @Expose
    public String sys_org_code;
    @Expose
    public String ticket_count;
    @Expose
    public String pay_time;
    @Expose
    public String order_time;
    @Expose
    public String create_by;
    @Expose
    public String fstatus;

    @Expose
    public String table_name;
    @Expose
    public String area_name;

    @Expose
    public String sys_company_code;
    @Expose
    public String random_number;
    @Expose
    public String activity_id;
    @Expose
    public String bpm_status;
    @Expose
    public String id;
    @Expose
    public String order_num;
    @Expose
    public String create_date;
    @Expose
    public String create_name;
    @Expose
    public String fadress;
    @Expose
    public String address;
    @Expose
    public String lat;
    @Expose
    public String lng;
    @Expose
    public String begindate;
    @Expose
    public String activity_name;
    @Expose
    public String begin_time;
    @Expose
    public String url_address;
    @Expose
    public String get_img_address;

    @Override
    public String toJson()
    {
        return toJsonWithAllFields(this);
    }
    @Override
    public MyOrderInfo fromJson(String json)
    {
        return fromJsonWithAllFields(json, MyOrderInfo.class);
    }
    @Override
    public boolean isBelongToMe(JSONObject json)
    {
        return false;
    }
}
