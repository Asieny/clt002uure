package com.guangxi.culturecloud.model;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/29 12:57
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class Society {


    /**
     * activity_name :
     * detail_title : 上海芭蕾舞团
     * detail_title_sx : 高端
     * fenshi : 100
     * first_img : aa.jpg
     * id : asdf2323e23
     * jiaohua : 20
     * members : 152
     * pic : bb.jpg
     * smrz : 1
     * society_detail : 上海芭蕾舞团成立于1979年
     * title : 上海芭蕾舞团
     * title_sx : 高端
     * zzrz : 1
     */

    private String activity_name;
    private String title;
    private String detail_title;        //详情社团名称
    private int    fenshi;              //粉丝
    private String newfirst_img;        //社团图片url地址
    private String logo;                //小图片类似唱片
    private String id;                  //id
    private int    jiaohua;             //浇花数
    private int    members;             //成员
    private String pic;                 //头像图片url地址
    private int    smrz;                //实名认证
    private String society_detail;      //社团简介
    private String detail_title_sx;
    private String title_sx;
    private int    zzrz;                //资质认证
    private String label1;              //社团标签1
    private String label2;              //社团标签2
    private String label3;              //社团标签3
    /**
     * fname : 新成路街道书画社
     */

    private String fname;//社团名称
    /**
     * sumJiaohua : 5
     */

    private int    sumJiaohua;
    /**
     * url_address : http://220.248.107.62:8084/whyapi/
     */

    private ApiAddressBean apiAddress;

    public String getLabel1() {
        return label1;
    }
    public void setLabel1(String label1) {
        this.label1 = label1;
    }
    public void setLabel2(String label2) {
        this.label2 = label2;
    }
    public String getLabel2() {
        return label2;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }
    public String getLabel3() {
        return label3;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getDetail_title() {
        return detail_title;
    }

    public void setDetail_title(String detail_title) {
        this.detail_title = detail_title;
    }

    public String getDetail_title_sx() {
        return detail_title_sx;
    }

    public void setDetail_title_sx(String detail_title_sx) {
        this.detail_title_sx = detail_title_sx;
    }

    public int getFenshi() {
        return fenshi;
    }

    public void setFenshi(int fenshi) {
        this.fenshi = fenshi;
    }

    public String getNewfirst_img() {
        return newfirst_img;
    }

    public void setNewfirst_img(String newfirst_img) {
        this.newfirst_img = newfirst_img;
    }

    public String getlogo() {
        return newfirst_img;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getJiaohua() {
        return jiaohua;
    }

    public void setJiaohua(int jiaohua) {
        this.jiaohua = jiaohua;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSmrz() {
        return smrz;
    }

    public void setSmrz(int smrz) {
        this.smrz = smrz;
    }

    public String getSociety_detail() {
        return society_detail;
    }

    public void setSociety_detail(String society_detail) {
        this.society_detail = society_detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_sx() {
        return title_sx;
    }

    public void setTitle_sx(String title_sx) {
        this.title_sx = title_sx;
    }

    public int getZzrz() {
        return zzrz;
    }

    public void setZzrz(int zzrz) {
        this.zzrz = zzrz;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getSumJiaohua() {
        return sumJiaohua;
    }

    public void setSumJiaohua(int sumJiaohua) {
        this.sumJiaohua = sumJiaohua;
    }

    public ApiAddressBean getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(ApiAddressBean apiAddress) {
        this.apiAddress = apiAddress;
    }

    public static class ApiAddressBean {
        private String url_address;
        /**
         * get_img_address : http://220.248.107.62:8085/upFiles
         * post_img_address :
         */

        private String get_img_address;
        private String post_img_address;

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

        public String getPost_img_address() {
            return post_img_address;
        }

        public void setPost_img_address(String post_img_address) {
            this.post_img_address = post_img_address;
        }
    }
}
