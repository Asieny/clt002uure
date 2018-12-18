package com.guangxi.culturecloud.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import cn.com.mytest.json.AbsJson;

public class CultureCrowdFundingInfo extends AbsJson<CultureCrowdFundingInfo> {
    @Expose
    public String activity_time;
    @Expose
    public int    raised_number;
    @Expose
    public int    remain_number;
    @Expose
    public String sys_org_code;
    @Expose
    public String release_subject;
    @Expose
    public int    isFavourite;
    @Expose
    public int    isZan;


    @Expose
    public String activity_area;
    @Expose
    public String pic;
    @Expose
    public String update_name;
    @Expose
    public String create_by;
    @Expose
    public int    fstatus;
    @Expose
    public String sys_company_code;
    @Expose
    public int    target_number;
    @Expose
    public String activity_address;
    @Expose
    public float  price;
    @Expose
    public String remain_time;
    @Expose
    public String id;
    @Expose
    public String create_date;
    @Expose
    public String update_by;
    @Expose
    public String zc_introduce;
    @Expose
    public String fpercent;
    @Expose
    public String fname;
    @Expose
    public String repay_remark;
    @Expose
    public String zc_end_time;
    @Expose
    public int    limit_number;
    @Expose
    public String label1;
    @Expose
    public String update_date;
    @Expose
    public String label2;
    @Expose
    public String label3;
    @Expose
    public String zc_begin_time;
    @Expose
    public String zc_contact;
    @Expose
    public int    bpm_status;
    @Expose
    public String zc_remark;
    @Expose
    public String warning_remark;
    @Expose
    public String activity_detail_address;
    @Expose
    public String create_name;
    @Expose
    public String tb_name;
    @Expose
    public String area_name;
    @Expose
    public String newpic;
    @Expose
    public double lat;
    @Expose
    public double lng;

    @Override
    public String toJson() {
        return toJsonWithAllFields(this);
    }

    @Override
    public CultureCrowdFundingInfo fromJson(String json) {
        return fromJsonWithAllFields(json, CultureCrowdFundingInfo.class);
    }

    @Override
    public boolean isBelongToMe(JSONObject json) {
        return false;
    }
}
