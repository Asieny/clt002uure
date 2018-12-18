package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/24 3:29
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class GoodsDetailsInfo {

    /**
     * goods_name : 国画中的水彩画法
     * sys_org_code : A02
     * tb_name : z_arts
     * goods_introduce : <p>水在中国画和西方水彩域中都具有重要的作用。无论是水墨画还是水彩画，都是用水作为媒介来完成绘画的。水在水彩画中尤为重要，水是水彩画的灵魂。中国画同样也善于用水，中国画讲究墨法即水法，用水分的多少和用笔的急缓轻重来决定墨色的变化。</p>
     * update_name : 管理员
     * update_date : 2018-02-07 10:44:46.1700000
     * goods_time : 2018-01-30
     * create_by : admin
     * newGoods_file : upload/files/20180204/216.jpg
     * sys_company_code : A02
     * goods_file : upload/files\20180204\216.jpg
     * bpm_status : 1
     * goods_business : 韩天衡美术馆
     * id : 8a8a8aa2615c64e801616083ea400035
     * goods_type : 书画
     * create_date : 2018-02-04 19:11:53.4070000
     * update_by : admin
     * business_id : 8a8a8aa26156f55601615acd99cb0025
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
        private String goods_name;
        private String sys_org_code;
        private String tb_name;
        private String goods_introduce;
        private String update_name;
        private String update_date;
        private String goods_time;
        private String create_by;
        private String newGoods_file;
        private String sys_company_code;
        private String goods_file;
        private String bpm_status;
        private String goods_business;
        private String id;
        private String goods_type;
        private String create_date;
        private String update_by;
        private String business_id;
        private String create_name;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getSys_org_code() {
            return sys_org_code;
        }

        public void setSys_org_code(String sys_org_code) {
            this.sys_org_code = sys_org_code;
        }

        public String getTb_name() {
            return tb_name;
        }

        public void setTb_name(String tb_name) {
            this.tb_name = tb_name;
        }

        public String getGoods_introduce() {
            return goods_introduce;
        }

        public void setGoods_introduce(String goods_introduce) {
            this.goods_introduce = goods_introduce;
        }

        public String getUpdate_name() {
            return update_name;
        }

        public void setUpdate_name(String update_name) {
            this.update_name = update_name;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
        }

        public String getGoods_time() {
            return goods_time;
        }

        public void setGoods_time(String goods_time) {
            this.goods_time = goods_time;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public String getNewGoods_file() {
            return newGoods_file;
        }

        public void setNewGoods_file(String newGoods_file) {
            this.newGoods_file = newGoods_file;
        }

        public String getSys_company_code() {
            return sys_company_code;
        }

        public void setSys_company_code(String sys_company_code) {
            this.sys_company_code = sys_company_code;
        }

        public String getGoods_file() {
            return goods_file;
        }

        public void setGoods_file(String goods_file) {
            this.goods_file = goods_file;
        }

        public String getBpm_status() {
            return bpm_status;
        }

        public void setBpm_status(String bpm_status) {
            this.bpm_status = bpm_status;
        }

        public String getGoods_business() {
            return goods_business;
        }

        public void setGoods_business(String goods_business) {
            this.goods_business = goods_business;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(String goods_type) {
            this.goods_type = goods_type;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getUpdate_by() {
            return update_by;
        }

        public void setUpdate_by(String update_by) {
            this.update_by = update_by;
        }

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public String getCreate_name() {
            return create_name;
        }

        public void setCreate_name(String create_name) {
            this.create_name = create_name;
        }
    }
}
