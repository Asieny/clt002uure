package com.guangxi.culturecloud.model;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/4 22:58
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ShowDetailInfo {

    /**
     * fmain_company : 公司
     * fname : 影剧院
     * faddress : 南通
     * sys_org_code : A02
     * left_remark : 试试
     * right_remark : 看看
     * fmobile : 188863727
     * fmain_people : 小王
     * create_by : admin
     * remain_ticket : 44
     * fdetail : <p>呵呵呵<br/></p>
     * total_ticket : 75
     * sys_company_code : A02
     * ftype : 讲座
     * price : 45
     * is_recommend : 推荐
     * fdate : 2017-12-06 16:58:12
     * bpm_status : 1
     * id : 40288a60609c3d2e01609c589c520018
     * create_date : 2017-12-28 16:59:01.0730000
     * is_yuyue : 1
     * fprompt : <p>哈哈哈</p>
     * create_name : 管理员
     */

    private ArrBean arr;

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String fmain_company;
        private String fname;
        private String faddress;
        private String sys_org_code;
        private String left_remark;
        private String right_remark;
        private String fmobile;
        private String fmain_people;
        private String create_by;
        private String fdetail;
        private int    total_ticket;
        private String sys_company_code;
        private String ftype;
        private float  price;
        private String is_recommend;
        private String fdate;
        private String bpm_status;
        private String id;
        private String create_date;
        private String is_yuyue;
        private String fprompt;
        private String create_name;
        private String newpic;
        private String label1;          //标签1
        private String label2;          //标签2
        private String label3;          //标签3
        private int    remain_ticket;  //余票
        private int    isFavourite;
        private int    isZan;

        /**
         * lng : 121.49361755958795
         * lat : 31.254552198584197
         */

        private double lng;//经度
        private double lat;//纬度
        /**
         * end_date : 2018-01-30 09:27
         */

        private String end_date;
        /**
         * tb_name : z_Culture
         */

        private String tb_name;
        /**
         * time_beizhu : 14:00
         */

        private String time_beizhu;
        /**
         * area_name : 柳州市
         */

        private String area_name;
        /**
         * url_address : http://220.248.107.62:8085/lzwhyapi/
         * get_img_address : http://220.248.107.62:8085/upFiles/
         */

        private String url_address;
        private String get_img_address;

        public int getIsZan() {
            return isZan;
        }

        public void setIsZan(int isZan) {
            this.isZan = isZan;
        }

        public int getIsFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(int isFavourite) {
            this.isFavourite = isFavourite;
        }

        public String getLabel1() {
            return label1;
        }

        public void setLabel1(String label1) {
            this.label1 = label1;
        }

        public String getLabel2() {
            return label2;
        }

        public void setLabel2(String label2) {
            this.label2 = label2;
        }

        public String getLabel3() {
            return label3;
        }

        public void setLabel3(String label3) {
            this.label3 = label3;
        }

        public String getNewpic() {
            return newpic;
        }

        public void setNewpic(String newpic) {
            this.newpic = newpic;
        }

        public String getFmain_company() {
            return fmain_company;
        }

        public void setFmain_company(String fmain_company) {
            this.fmain_company = fmain_company;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFaddress() {
            return faddress;
        }

        public void setFaddress(String faddress) {
            this.faddress = faddress;
        }

        public String getSys_org_code() {
            return sys_org_code;
        }

        public void setSys_org_code(String sys_org_code) {
            this.sys_org_code = sys_org_code;
        }

        public String getLeft_remark() {
            return left_remark;
        }

        public void setLeft_remark(String left_remark) {
            this.left_remark = left_remark;
        }

        public String getRight_remark() {
            return right_remark;
        }

        public void setRight_remark(String right_remark) {
            this.right_remark = right_remark;
        }

        public String getFmobile() {
            return fmobile;
        }

        public void setFmobile(String fmobile) {
            this.fmobile = fmobile;
        }

        public String getFmain_people() {
            return fmain_people;
        }

        public void setFmain_people(String fmain_people) {
            this.fmain_people = fmain_people;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public int getRemain_ticket() {
            return remain_ticket;
        }

        public void setRemain_ticket(int remain_ticket) {
            this.remain_ticket = remain_ticket;
        }

        public String getFdetail() {
            return fdetail;
        }

        public void setFdetail(String fdetail) {
            this.fdetail = fdetail;
        }

        public int getTotal_ticket() {
            return total_ticket;
        }

        public void setTotal_ticket(int total_ticket) {
            this.total_ticket = total_ticket;
        }

        public String getSys_company_code() {
            return sys_company_code;
        }

        public void setSys_company_code(String sys_company_code) {
            this.sys_company_code = sys_company_code;
        }

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(String is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getFdate() {
            return fdate;
        }

        public void setFdate(String fdate) {
            this.fdate = fdate;
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

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getIs_yuyue() {
            return is_yuyue;
        }

        public void setIs_yuyue(String is_yuyue) {
            this.is_yuyue = is_yuyue;
        }

        public String getFprompt() {
            return fprompt;
        }

        public void setFprompt(String fprompt) {
            this.fprompt = fprompt;
        }

        public String getCreate_name() {
            return create_name;
        }

        public void setCreate_name(String create_name) {
            this.create_name = create_name;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getTb_name() {
            return tb_name;
        }

        public void setTb_name(String tb_name) {
            this.tb_name = tb_name;
        }

        public String getTime_beizhu() {
            return time_beizhu;
        }

        public void setTime_beizhu(String time_beizhu) {
            this.time_beizhu = time_beizhu;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getUrl_address() {
            return url_address;
        }

        public void setUrl_address(String url_address) {
            this.url_address = url_address;
        }

        public String getGet_img_address() {
            return get_img_address;
        }

        public void setGet_img_address(String get_img_address) {
            this.get_img_address = get_img_address;
        }
    }
}
