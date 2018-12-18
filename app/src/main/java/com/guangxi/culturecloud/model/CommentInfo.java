package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/5 9:49
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CommentInfo {

    /**
     * sys_org_code : A02
     * comment_date : 2018-01-30 00:00:00.0000000
     * headpic : 1.jpg
     * userid : 6
     * create_by : admin
     * sys_company_code : A02
     * activity_id : 40288aef608c9fd301608cca54aa0009
     * bpm_status : 1
     * comment_content : <p>44<br/></p>
     * id : 40288a6060bfa9210160bfb612d70019
     * create_date : 2018-01-04 13:47:48.8230000
     * username : 456
     * create_name : 管理员
     */

    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String sys_org_code;
        private String comment_date;
        private String headpic;
        private String userid;
        private String create_by;
        private String sys_company_code;
        private String activity_id;
        private String bpm_status;
        private String comment_content;
        private String id;
        private String create_date;
        private String username;
        private String create_name;
        /**
         * comment_pic :
         */

        private String comment_pic;

        public String getSys_org_code() {
            return sys_org_code;
        }

        public void setSys_org_code(String sys_org_code) {
            this.sys_org_code = sys_org_code;
        }

        public String getComment_date() {
            return comment_date;
        }

        public void setComment_date(String comment_date) {
            this.comment_date = comment_date;
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

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public String getSys_company_code() {
            return sys_company_code;
        }

        public void setSys_company_code(String sys_company_code) {
            this.sys_company_code = sys_company_code;
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

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCreate_name() {
            return create_name;
        }

        public void setCreate_name(String create_name) {
            this.create_name = create_name;
        }

        public String getComment_pic() {
            return comment_pic;
        }

        public void setComment_pic(String comment_pic) {
            this.comment_pic = comment_pic;
        }
    }
}
