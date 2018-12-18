package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/5 9:09
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AdmireInfo {

    /**
     * arr : [{"gc_zanSum":0,"gc_disZanSum":1,"ftype":"0","tb_name":"z_Theater","activity_id":"8a8a8aa260d380070160d3c50420003e","bpm_status":"1","id":"98539063-36a6-4ab0-8285-8422da8d4660","zan_date":"2018-01-23 16:09:11.9370000","headpic":"3.jpg","userid":"8","username":"tianji"},{"gc_zanSum":0,"gc_disZanSum":0,"ftype":"0","tb_name":"z_Theater","activity_id":"8a8a8aa260d380070160d3c50420003e","bpm_status":"1","id":"6be6eb1f-5fe2-42c9-ae08-aa3231045659","zan_date":"2018-01-23 15:57:10.7800000","headpic":"123.jpg","userid":"7","username":"广告歌"}]
     * countZan : 2
     */

    private int           countZan;
    /**
     * gc_zanSum : 0
     * gc_disZanSum : 1
     * ftype : 0
     * tb_name : z_Theater
     * activity_id : 8a8a8aa260d380070160d3c50420003e
     * bpm_status : 1
     * id : 98539063-36a6-4ab0-8285-8422da8d4660
     * zan_date : 2018-01-23 16:09:11.9370000
     * headpic : 3.jpg
     * userid : 8
     * username : tianji
     */

    private List<ArrBean> arr;

    public int getCountZan() {
        return countZan;
    }

    public void setCountZan(int countZan) {
        this.countZan = countZan;
    }

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private int    gc_zanSum;
        private int    gc_disZanSum;
        private String ftype;
        private String tb_name;
        private String activity_id;
        private String bpm_status;
        private String id;
        private String zan_date;
        private String headpic;
        private String userid;
        private String username;

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

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public String getTb_name() {
            return tb_name;
        }

        public void setTb_name(String tb_name) {
            this.tb_name = tb_name;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
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

        public String getZan_date() {
            return zan_date;
        }

        public void setZan_date(String zan_date) {
            this.zan_date = zan_date;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
