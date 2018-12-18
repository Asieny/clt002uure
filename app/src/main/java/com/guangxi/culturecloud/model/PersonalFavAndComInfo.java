package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/21 14:00
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class PersonalFavAndComInfo {

    /**
     * area_name : 柳州市
     * fmain_company : 上海博物馆
     * activity_name : aa
     * url_address : http://220.248.107.62:8085/lzwhyapi/
     * comment_type : 1
     * tb_name : z_library
     * activity_id : safda1a
     * comment_date : 2018-03-20 21:53:09
     * comment_content : 123456
     * id : feb0e7c536674b58873e2aff4e63dc9d
     * comment_pic : aa.jpg
     * userid : 32sfsafa
     */

    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String area_name;
        private String fmain_company;
        private String activity_name;
        private String url_address;
        private String comment_type;
        private String tb_name;
        private String activity_id;
        private String comment_date;
        private String comment_content;
        private String id;
        private String comment_pic;
        private String userid;
        /**
         * faddress : 上冻速度达到
         * pic_address : aa.pig
         * end_time : 2018-04-19
         * begin_time : 2018-03-20
         * favourite_date : 2018-03-20 19:26:47
         */

        private String faddress;
        private String pic_address;
        private String end_time;
        private String begin_time;
        private String favourite_date;
        /**
         * get_img_address : http://220.248.107.62:8085/upFiles/
         */

        private String get_img_address;

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getFmain_company() {
            return fmain_company;
        }

        public void setFmain_company(String fmain_company) {
            this.fmain_company = fmain_company;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getUrl_address() {
            return url_address;
        }

        public void setUrl_address(String url_address) {
            this.url_address = url_address;
        }

        public String getComment_type() {
            return comment_type;
        }

        public void setComment_type(String comment_type) {
            this.comment_type = comment_type;
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

        public String getComment_date() {
            return comment_date;
        }

        public void setComment_date(String comment_date) {
            this.comment_date = comment_date;
        }

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComment_pic() {
            return comment_pic;
        }

        public void setComment_pic(String comment_pic) {
            this.comment_pic = comment_pic;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getFaddress() {
            return faddress;
        }

        public void setFaddress(String faddress) {
            this.faddress = faddress;
        }

        public String getPic_address() {
            return pic_address;
        }

        public void setPic_address(String pic_address) {
            this.pic_address = pic_address;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getFavourite_date() {
            return favourite_date;
        }

        public void setFavourite_date(String favourite_date) {
            this.favourite_date = favourite_date;
        }

        public String getGet_img_address() {
            return get_img_address;
        }

        public void setGet_img_address(String get_img_address) {
            this.get_img_address = get_img_address;
        }
    }
}
