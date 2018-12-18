package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/26 10:11
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class Square {

    /**
     * gc_zanSum : 0
     * gc_disZanSum : 0
     * zan_date : 2018-01-25 10:40:48.8600000
     * userid : 8
     * gc_voteSum : 0
     * newHeadpic : 3.jpg
     * ftype : 1
     * picList : [{"z_zan_id":"11004403-3334-4fe7-9ced-87b859180de6","newFpic":"11.jpg","fpic":"11.jpg","id":"84B39246-19BF-4EA5-AA68-D5149DF6E332"},{"z_zan_id":"11004403-3334-4fe7-9ced-87b859180de6","newFpic":"22.jpg","fpic":"22.jpg","id":"C88026F4-E2AF-4FB1-AC6F-B8ECC8169CE0"},{"z_zan_id":"11004403-3334-4fe7-9ced-87b859180de6","newFpic":"33.jpg","fpic":"33.jpg","id":"77C8F589-76D9-4D8A-ABF5-A9F0EFDE410F"},{"z_zan_id":"11004403-3334-4fe7-9ced-87b859180de6","newFpic":"44.jpg","fpic":"44.jpg","id":"75B5DA49-C932-460C-B06C-ECA3ED3DFC69"},{"z_zan_id":"11004403-3334-4fe7-9ced-87b859180de6","newFpic":"55.jpg","fpic":"55.jpg","id":"4FB473F1-0163-4AFA-81BD-DAAA7B8018FE"},{"z_zan_id":"11004403-3334-4fe7-9ced-87b859180de6","newFpic":"66.jpg","fpic":"66.jpg","id":"413AC3AA-CC0A-40FC-901C-ABC2ECA7CA46"}]
     * bpm_status : 1
     * id : 11004403-3334-4fe7-9ced-87b859180de6
     * fcontent : safdaa
     * gc_type : 123afsdf
     * username : tianji
     */

    private int               gc_zanSum;
    private int               gc_disZanSum;
    private String            zan_date;
    private String            userid;
    private int               gc_voteSum;
    private String            newHeadpic;
    private String            ftype;
    private String            bpm_status;
    private String            id;
    private String            fcontent;
    private String            gc_type;
    private String            username;
    /**
     * z_zan_id : 11004403-3334-4fe7-9ced-87b859180de6
     * newFpic : 11.jpg
     * fpic : 11.jpg
     * id : 84B39246-19BF-4EA5-AA68-D5149DF6E332
     */

    private List<PicListBean> picList;
    /**
     * fname : 经典影剧院：《红霞》
     * tb_name : z_Theater
     * remainCountGcZan : 0
     * newpic : upload/files/20180110/d.jpg
     * activity_id : 8a8a8aa260d380070160d3c50420003e
     */

    private String            fname;
    private String            tb_name;
    private int               remainCountGcZan;
    private String            newpic;
    private String            activity_id;
    private int               zan;
    private int               diszan;

    /**
     * get_img_address : http://220.248.107.62:8085/upFiles/
     */

    private String            get_img_address;
    /**
     * url_address : http://220.248.107.62:8085/lzwhyapi/
     */

    private String            url_address;
    /**
     * pic_address : upload/files/20180321/Chrysanthemum.jpg
     */

    private String            pic_address;
    /**
     * activity_name : 家乡#爱国诗朗诵
     */

    private String            activity_name;

    public int getGc_zanSum() {
        return gc_zanSum;
    }

    public void setGc_zanSum(int gc_zanSum) {
        this.gc_zanSum = gc_zanSum;
    }

    public int getGc_disZanSum() {
        return gc_disZanSum;
    }

    public void setGc_disZanSum(int gc_disZanSum) {
        this.gc_disZanSum = gc_disZanSum;
    }

    public String getZan_date() {
        return zan_date;
    }

    public void setZan_date(String zan_date) {
        this.zan_date = zan_date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getGc_voteSum() {
        return gc_voteSum;
    }

    public void setGc_voteSum(int gc_voteSum) {
        this.gc_voteSum = gc_voteSum;
    }

    public String getNewHeadpic() {
        return newHeadpic;
    }

    public void setNewHeadpic(String newHeadpic) {
        this.newHeadpic = newHeadpic;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getBpm_status() {
        return bpm_status;
    }

    public void setBpm_status(String bpm_status) {
        this.bpm_status = bpm_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFcontent() {
        return fcontent;
    }

    public void setFcontent(String fcontent) {
        this.fcontent = fcontent;
    }

    public String getGc_type() {
        return gc_type;
    }

    public void setGc_type(String gc_type) {
        this.gc_type = gc_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PicListBean> getPicList() {
        return picList;
    }

    public void setPicList(List<PicListBean> picList) {
        this.picList = picList;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getTb_name() {
        return tb_name;
    }

    public void setTb_name(String tb_name) {
        this.tb_name = tb_name;
    }

    public int getRemainCountGcZan() {
        return remainCountGcZan;
    }

    public void setRemainCountGcZan(int remainCountGcZan) {
        this.remainCountGcZan = remainCountGcZan;
    }

    public String getNewpic() {
        return newpic;
    }

    public void setNewpic(String newpic) {
        this.newpic = newpic;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getDiszan() {
        return diszan;
    }

    public void setDiszan(int diszan) {
        this.diszan = diszan;
    }

    public String getGet_img_address() {
        return get_img_address;
    }

    public void setGet_img_address(String get_img_address) {
        this.get_img_address = get_img_address;
    }

    public String getUrl_address() {
        return url_address;
    }

    public void setUrl_address(String url_address) {
        this.url_address = url_address;
    }

    public String getPic_address() {
        return pic_address;
    }

    public void setPic_address(String pic_address) {
        this.pic_address = pic_address;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public static class PicListBean {
        private String z_zan_id;
        private String newFpic;
        private String fpic;
        private String id;

        public String getZ_zan_id() {
            return z_zan_id;
        }

        public void setZ_zan_id(String z_zan_id) {
            this.z_zan_id = z_zan_id;
        }

        public String getNewFpic() {
            return newFpic;
        }

        public void setNewFpic(String newFpic) {
            this.newFpic = newFpic;
        }

        public String getFpic() {
            return fpic;
        }

        public void setFpic(String fpic) {
            this.fpic = fpic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
