package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2019/2/15 10:06
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CityAreaCodeInfo {

    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * fstatus : 0
         * city_name : 兴宁区
         * citycode : 450102
         * url_address : 111
         * param : 11
         * get_img_address : 11
         * pid : 2c90b4e56874873d01687493f2c2000b
         * post_img_address : 11
         * id : 2c90b4e56874873d0168749e9a3e002b
         * ip_address : 11
         */

        private String fstatus;
        private String city_name;
        private String citycode;
        private String url_address;
        private String param;
        private String get_img_address;
        private String pid;
        private String post_img_address;
        private String id;
        private String ip_address;

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public String getUrl_address() {
            return url_address;
        }

        public void setUrl_address(String url_address) {
            this.url_address = url_address;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getGet_img_address() {
            return get_img_address;
        }

        public void setGet_img_address(String get_img_address) {
            this.get_img_address = get_img_address;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPost_img_address() {
            return post_img_address;
        }

        public void setPost_img_address(String post_img_address) {
            this.post_img_address = post_img_address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIp_address() {
            return ip_address;
        }

        public void setIp_address(String ip_address) {
            this.ip_address = ip_address;
        }
    }
}
