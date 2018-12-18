package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/4 10:41
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ClubDetailInfo {

    /**
     * activity_name : z_love_society
     * sys_org_code : A02
     * first_img : upload/files\20171229\Chrysanthemum.jpg
     * detail_title_sx : 1313
     * title : 1212
     * update_name : 管理员
     * create_by : admin
     * sumFavourite : 0
     * sys_company_code : A02
     * listPic : [{"create_by":"admin","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","love_society_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9b2a81b0003","pic":"upload/files\\20180103\\Koala.jpg","create_date":"2018-01-03 09:46:21.5970000","create_name":"管理员","pic_name":"测试"},{"create_by":"admin","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","love_society_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9b2f36d0008","pic":"upload/files\\20180103\\Desert.jpg","create_date":"2018-01-03 09:46:40.8770000","create_name":"管理员","pic_name":"测试2"},{"create_by":"admin","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","love_society_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9bbeb92002a","pic":"upload/files\\20180103\\Lighthouse.jpg","create_date":"2018-01-03 09:56:28.6900000","create_name":"管理员","pic_name":"二期e"},{"create_by":"admin","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","love_society_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9bc07c5002c","pic":"upload/files\\20180103\\Koala.jpg","create_date":"2018-01-03 09:56:35.9100000","create_name":"管理员","pic_name":"厄齐尔"}]
     * smrz : 0
     * members : 2131
     * logo : upload/files\20180103\Jellyfish.jpg
     * id : 4028f39a609d16ba01609d7a37e0000f
     * create_date : 2017-12-28 22:15:20.8000000
     * update_by : admin
     * isFavourite : 0
     * isJiaohua : 0
     * title_sx : 13
     * detail_title : 113
     * jiaohua : 31232
     * label1 : 标签1
     * update_date : 2018-01-03 09:49:05.7070000
     * label2 : 标签2
     * label3 : 标签3
     * zzrz : 0
     * sumJiaohua : 0
     * bpm_status : 1
     * listVideo : [{"create_by":"admin","video_name":"给个","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","lovesociety_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9b4972a0012","video":"upload/files\\20180103\\b56bbfb8fb4560408f45c46f6f04e48e.mp4","create_date":"2018-01-03 09:48:28.3300000","create_name":"管理员"},{"create_by":"admin","video_name":"扶持","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","lovesociety_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9b4e25c0016","video":"upload/files\\20180103\\b56bbfb8fb4560408f45c46f6f04e48e.mp4","create_date":"2018-01-03 09:48:47.5800000","create_name":"管理员"},{"create_by":"admin","video_name":"二期淡淡的","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","lovesociety_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9bc43a5002e","video":"upload/files\\20180103\\98d27da9a347b3db81f9381396ed1dd3.mp4","create_date":"2018-01-03 09:56:51.2370000","create_name":"管理员"},{"create_by":"admin","video_name":" 大 淡淡的","fstatus":"1","sys_company_code":"A02","sys_org_code":"A02","bpm_status":"1","lovesociety_id":"4028f39a609d16ba01609d7a37e0000f","id":"8a8a8aa260b99e520160b9bc6bde0031","video":"upload/files\\20180103\\b56bbfb8fb4560408f45c46f6f04e48e.mp4","create_date":"2018-01-03 09:57:01.5330000","create_name":"管理员"}]
     * society_detail : <p>efefreffehewew w dw d w d dhh阿写啊写撒东西啊对我的氛围的</p>
     * create_name : 管理员
     */

    private ArrBean arr;
    private String  result;
    private String  message;
    /**
     * validateCode : 928790
     * valid : true
     */

    private String  validateCode;
    private boolean valid;

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String activity_name;
        private String sys_org_code;
        private String first_img;
        private String logo;
        private String update_date;
        private int    sumJiaohua;
        private String bpm_status;
        private String society_detail;
        private String create_name;
        private String title_sx;
        private String detail_title;
        private String update_name;
        private String create_by;
        private int    sumFavourite;
        private String sys_company_code;
        private String id;
        private String create_date;
        private String update_by;
        private String detail_title_sx;//社团详细介绍
        private String title;           //社团名称
        private int    smrz;         //是否实名认证
        private int    zzrz;        //是否资质认证
        private int    members;     //社团成员数
        private int    isFavourite;     //是否关注
        private int    isJiaohua;       //今日是否浇花
        private int    jiaohua;         //浇花数
        private String label1;          //标签1
        private String label2;          //标签2
        private String label3;          //标签3
        private String newfirst_img;       //社团展示照
        private String newlogo;       //社团logo

        private String result;
        private String message;
        /**
         * fname : “铿锵军鼓”社团活动
         */

        private String fname;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * create_by : admin
         * fstatus : 1
         * sys_company_code : A02
         * sys_org_code : A02
         * bpm_status : 1
         * love_society_id : 4028f39a609d16ba01609d7a37e0000f
         * id : 8a8a8aa260b99e520160b9b2a81b0003
         * pic : upload/files\20180103\Koala.jpg
         * create_date : 2018-01-03 09:46:21.5970000
         * create_name : 管理员
         * pic_name : 测试
         */

        private List<ListPicBean>   listPic;
        /**
         * create_by : admin
         * video_name : 给个
         * fstatus : 1
         * sys_company_code : A02
         * sys_org_code : A02
         * bpm_status : 1
         * lovesociety_id : 4028f39a609d16ba01609d7a37e0000f
         * id : 8a8a8aa260b99e520160b9b4972a0012
         * video : upload/files\20180103\b56bbfb8fb4560408f45c46f6f04e48e.mp4
         * create_date : 2018-01-03 09:48:28.3300000
         * create_name : 管理员
         */

        private List<ListVideoBean> listVideo;

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getSys_org_code() {
            return sys_org_code;
        }

        public void setSys_org_code(String sys_org_code) {
            this.sys_org_code = sys_org_code;
        }

        public String getNewlogo() {
            return newlogo;
        }

        public void setNewlogo(String newlogo) {
            this.newlogo = newlogo;
        }

        public String getNewfirst_img() {
            return newfirst_img;
        }

        public void setNewfirst_img(String newfirst_img) {
            this.newfirst_img = newfirst_img;
        }

        public String getFirst_img() {
            return first_img;
        }

        public void setFirst_img(String first_img) {
            this.first_img = first_img;
        }

        public String getDetail_title_sx() {
            return detail_title_sx;
        }

        public void setDetail_title_sx(String detail_title_sx) {
            this.detail_title_sx = detail_title_sx;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdate_name() {
            return update_name;
        }

        public void setUpdate_name(String update_name) {
            this.update_name = update_name;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public int getSumFavourite() {
            return sumFavourite;
        }

        public void setSumFavourite(int sumFavourite) {
            this.sumFavourite = sumFavourite;
        }

        public String getSys_company_code() {
            return sys_company_code;
        }

        public void setSys_company_code(String sys_company_code) {
            this.sys_company_code = sys_company_code;
        }

        public int getSmrz() {
            return smrz;
        }

        public void setSmrz(int smrz) {
            this.smrz = smrz;
        }

        public int getMembers() {
            return members;
        }

        public void setMembers(int members) {
            this.members = members;
        }

        public String getLogo() {
            return logo;
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

        public int getIsFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(int isFavourite) {
            this.isFavourite = isFavourite;
        }

        public int getIsJiaohua() {
            return isJiaohua;
        }

        public void setIsJiaohua(int isJiaohua) {
            this.isJiaohua = isJiaohua;
        }

        public String getTitle_sx() {
            return title_sx;
        }

        public void setTitle_sx(String title_sx) {
            this.title_sx = title_sx;
        }

        public String getDetail_title() {
            return detail_title;
        }

        public void setDetail_title(String detail_title) {
            this.detail_title = detail_title;
        }

        public int getJiaohua() {
            return jiaohua;
        }

        public void setJiaohua(int jiaohua) {
            this.jiaohua = jiaohua;
        }

        public String getLabel1() {
            return label1;
        }

        public void setLabel1(String label1) {
            this.label1 = label1;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
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

        public int getZzrz() {
            return zzrz;
        }

        public void setZzrz(int zzrz) {
            this.zzrz = zzrz;
        }

        public int getSumJiaohua() {
            return sumJiaohua;
        }

        public void setSumJiaohua(int sumJiaohua) {
            this.sumJiaohua = sumJiaohua;
        }

        public String getBpm_status() {
            return bpm_status;
        }

        public void setBpm_status(String bpm_status) {
            this.bpm_status = bpm_status;
        }

        public String getSociety_detail() {
            return society_detail;
        }

        public void setSociety_detail(String society_detail) {
            this.society_detail = society_detail;
        }

        public String getCreate_name() {
            return create_name;
        }

        public void setCreate_name(String create_name) {
            this.create_name = create_name;
        }

        public List<ListPicBean> getListPic() {
            return listPic;
        }

        public void setListPic(List<ListPicBean> listPic) {
            this.listPic = listPic;
        }

        public List<ListVideoBean> getListVideo() {
            return listVideo;
        }

        public void setListVideo(List<ListVideoBean> listVideo) {
            this.listVideo = listVideo;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public static class ListPicBean {
            private String create_by;
            private String fstatus;
            private String sys_company_code;
            private String sys_org_code;
            private String bpm_status;
            private String love_society_id;
            private String id;
            private String pic;
            private String create_date;
            private String create_name;
            private String pic_name;

            private String newpic;

            public String getNewpic() {
                return newpic;
            }

            public void setNewpic(String newpic) {
                this.newpic = newpic;
            }

            public String getCreate_by() {
                return create_by;
            }

            public void setCreate_by(String create_by) {
                this.create_by = create_by;
            }

            public String getFstatus() {
                return fstatus;
            }

            public void setFstatus(String fstatus) {
                this.fstatus = fstatus;
            }

            public String getSys_company_code() {
                return sys_company_code;
            }

            public void setSys_company_code(String sys_company_code) {
                this.sys_company_code = sys_company_code;
            }

            public String getSys_org_code() {
                return sys_org_code;
            }

            public void setSys_org_code(String sys_org_code) {
                this.sys_org_code = sys_org_code;
            }

            public String getBpm_status() {
                return bpm_status;
            }

            public void setBpm_status(String bpm_status) {
                this.bpm_status = bpm_status;
            }

            public String getLove_society_id() {
                return love_society_id;
            }

            public void setLove_society_id(String love_society_id) {
                this.love_society_id = love_society_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public String getCreate_name() {
                return create_name;
            }

            public void setCreate_name(String create_name) {
                this.create_name = create_name;
            }

            public String getPic_name() {
                return pic_name;
            }

            public void setPic_name(String pic_name) {
                this.pic_name = pic_name;
            }
        }

        public static class ListVideoBean {
            private String create_by;
            private String video_name;
            private String fstatus;
            private String sys_company_code;
            private String sys_org_code;
            private String bpm_status;
            private String lovesociety_id;
            private String id;
            private String video;
            private String create_date;
            private String create_name;

            public String getCreate_by() {
                return create_by;
            }

            public void setCreate_by(String create_by) {
                this.create_by = create_by;
            }

            public String getVideo_name() {
                return video_name;
            }

            public void setVideo_name(String video_name) {
                this.video_name = video_name;
            }

            public String getFstatus() {
                return fstatus;
            }

            public void setFstatus(String fstatus) {
                this.fstatus = fstatus;
            }

            public String getSys_company_code() {
                return sys_company_code;
            }

            public void setSys_company_code(String sys_company_code) {
                this.sys_company_code = sys_company_code;
            }

            public String getSys_org_code() {
                return sys_org_code;
            }

            public void setSys_org_code(String sys_org_code) {
                this.sys_org_code = sys_org_code;
            }

            public String getBpm_status() {
                return bpm_status;
            }

            public void setBpm_status(String bpm_status) {
                this.bpm_status = bpm_status;
            }

            public String getLovesociety_id() {
                return lovesociety_id;
            }

            public void setLovesociety_id(String lovesociety_id) {
                this.lovesociety_id = lovesociety_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public String getCreate_name() {
                return create_name;
            }

            public void setCreate_name(String create_name) {
                this.create_name = create_name;
            }
        }
    }
}
