package com.guangxi.culturecloud.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import java.io.Serializable;

import cn.com.mytest.json.AbsJson;

public class UserInfo extends AbsJson<UserInfo> implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7184759155932142798L;
    @Expose
    public String member_id;
    @Expose
    public String username;
    @Expose
    public String telephone;
    @Expose
    public String psw;


    @Override
    public String toJson()
    {
        return toJsonWithAllFields(this);
    }
    @Override
    public UserInfo fromJson(String json)
    {
        return fromJsonWithAllFields(json, UserInfo.class);
    }
    @Override
    public boolean isBelongToMe(JSONObject json)
    {
        return false;
    }



}
