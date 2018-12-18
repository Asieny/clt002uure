package com.guangxi.culturecloud.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import cn.com.mytest.json.AbsJson;

/**
 * 大活动实体
 */
public class BigEventInfo extends AbsJson<BigEventInfo>
{
    @Expose
    public String ftime;
    @Expose
    public String fmain_company;



    @Expose
    public String fname;
    @Expose
    public String tb_name;
    @Expose
    public String faddress;
    @Expose
    public String sys_org_code;
    @Expose
    public String left_remark;
    @Expose
    public String right_remark;
    @Expose
    public String tb_type;
    @Expose
    public int total_ticket;
    @Expose
    public String fmobile;
    @Expose
    public String fmain_people;
    @Expose
    public String create_by;
    @Expose
    public String fdetail;
    @Expose
    public int remain_ticket;
    @Expose
    public double price;
    @Expose
    public String fdate;
    @Expose
    public String id;
    @Expose
    public String is_yuyue;
    @Expose
    public String newpic;
    @Expose
    public String sys_company_code;
    @Expose
    public String is_recommend;
    @Expose
    public String bpm_status;
    @Expose
    public String create_date;
    @Expose
    public String fprompt;
    @Expose
    public String create_name;
    @Expose
    public String label1;
    @Expose
    public String label2;
    @Expose
    public String label3;

    @Override
    public String toJson()
    {
        return toJsonWithAllFields(this);
    }
    @Override
    public BigEventInfo fromJson(String json)
    {
        return fromJsonWithAllFields(json, BigEventInfo.class);
    }
    @Override
    public boolean isBelongToMe(JSONObject json)
    {
        return false;
    }
}
