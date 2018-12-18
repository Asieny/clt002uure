package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/28 13:35
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ActRoomInfo {

    /**
     * hang : 4
     * sys_org_code : A02
     * playroom_phone : 222
     * playroom_area : 50
     * tb_name : z_Sports
     * pic : upload/files\20180326\dc3.jpg
     * update_name : 管理员
     * create_by : admin
     * playroom_remark : 法定假日除外
     * sys_company_code : A02
     * id : 402880e66261d93e016261dda2bd0001
     * create_date : 2018-03-26 18:32:17.8530000
     * update_by : admin
     * palyroom_device : 投影仪
     * address : 2
     * playroom_price : 30
     * Newpic : upload/files/20180326/dc3.jpg
     * list : [{"date":"2018-02-26","id":"402880e66261d93e016261def51b0009","time":[{"end_time":"09:00","begin_time":"08:00","id":"402880e66261d93e016261df1e71000b"},{"end_time":"10:00","begin_time":"09:00","id":"40288ae1626b043e01626b0aebb70014"},{"end_time":"13:00","begin_time":"12:00","id":"40288ae1626b043e01626b0b2d640017"},{"end_time":"14:30","begin_time":"13:30","id":"40288ae1626b043e01626b0b6f3a0019"}]},{"date":"2018-03-27","id":"402880e662628c090162628d3f210001","time":[{"end_time":"09:00","begin_time":"07:00","id":"40288ae1626b043e01626b0bf436001e"},{"end_time":"13:30","begin_time":"12:30","id":"40288ae1626b043e01626b0c238b0020"},{"end_time":"14:00","begin_time":"13:30","id":"40288ae1626b043e01626b0c98d90022"},{"end_time":"16:00","begin_time":"14:00","id":"40288ae1626b043e01626b0d0aee0024"}]},{"date":"2018-03-28","id":"40288ae1626b043e01626b095bd30006","time":[{"end_time":"08:00","begin_time":"07:00","id":"40288ae1626b043e01626b0d86bc0026"},{"end_time":"11:30","begin_time":"09:30","id":"40288ae1626b043e01626b0df02a0028"},{"end_time":"14:00","begin_time":"13:00","id":"40288ae1626b043e01626b0e1700002a"},{"end_time":"16:00","begin_time":"14:00","id":"40288ae1626b043e01626b0e4e9e002c"}]},{"date":"2018-03-29","id":"40288ae1626b043e01626b0972e40008","time":[{"end_time":"10:30","begin_time":"08:00","id":"40288ae1626b043e01626b0eaa75002e"},{"end_time":"13:30","begin_time":"12:30","id":"40288ae1626b043e01626b0ececb0030"},{"end_time":"15:00","begin_time":"14:00","id":"40288ae1626b043e01626b0eff240032"},{"end_time":"18:00","begin_time":"16:00","id":"40288ae1626b043e01626b0f41b40034"}]},{"date":"2018-03-30","id":"40288ae1626b043e01626b0994cd000a","time":[{"end_time":"09:30","begin_time":"08:30","id":"40288ae1626b043e01626b0f9b140036"},{"end_time":"11:30","begin_time":"10:40","id":"40288ae1626b043e01626b0fef070038"},{"end_time":"14:00","begin_time":"13:00","id":"40288ae1626b043e01626b101ba8003a"},{"end_time":"17:00","begin_time":"14:00","id":"40288ae1626b043e01626b105d9b003c"}]},{"date":"2018-03-31","id":"40288ae1626b043e01626b09b2c1000c","time":[{"end_time":"09:00","begin_time":"07:00","id":"40288ae1626b043e01626b10ebd9003e"},{"end_time":"12:00","begin_time":"10:30","id":"40288ae1626b043e01626b114fe30040"},{"end_time":"14:30","begin_time":"13:00","id":"40288ae1626b043e01626b1185d20042"},{"end_time":"17:30","begin_time":"15:30","id":"40288ae1626b043e01626b11d8c10044"}]},{"date":"2018-04-01","id":"40288ae1626b043e01626b09ee86000f","time":[{"end_time":"10:00","begin_time":"06:30","id":"40288ae1626b043e01626b1247fa0046"},{"end_time":"12:30","begin_time":"11:00","id":"40288ae1626b043e01626b1283420048"},{"end_time":"14:30","begin_time":"13:30","id":"40288ae1626b043e01626b12b35b004a"},{"end_time":"18:30","begin_time":"15:30","id":"40288ae1626b043e01626b12fb76004c"}]}]
     * label1 : 辅导
     * update_date : 2018-03-28 10:45:12.8070000
     * label2 : 辅导
     * playroom_iaccommodate : 20人
     * bpm_status : 1
     * playroom_name : 多媒体教室
     * business_id : 8a8a8aa26156f55601615ad5d675003c
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
        private String hang;
        private String sys_org_code;
        private String playroom_phone;
        private String playroom_area;
        private String tb_name;
        private String pic;
        private String update_name;
        private String create_by;
        private String playroom_remark;
        private String sys_company_code;
        private String id;
        private String create_date;
        private String update_by;
        private String palyroom_device;
        private String address;
        private String playroom_price;
        private String Newpic;
        private String label1;
        private String update_date;
        private String label2;
        private String playroom_iaccommodate;
        private String bpm_status;
        private String playroom_name;
        private String business_id;
        private String create_name;
        /**
         * date : 2018-02-26
         * id : 402880e66261d93e016261def51b0009
         * time : [{"end_time":"09:00","begin_time":"08:00","id":"402880e66261d93e016261df1e71000b"},{"end_time":"10:00","begin_time":"09:00","id":"40288ae1626b043e01626b0aebb70014"},{"end_time":"13:00","begin_time":"12:00","id":"40288ae1626b043e01626b0b2d640017"},{"end_time":"14:30","begin_time":"13:30","id":"40288ae1626b043e01626b0b6f3a0019"}]
         */

        private List<ListBean> list;

        public String getHang() {
            return hang;
        }

        public void setHang(String hang) {
            this.hang = hang;
        }

        public String getSys_org_code() {
            return sys_org_code;
        }

        public void setSys_org_code(String sys_org_code) {
            this.sys_org_code = sys_org_code;
        }

        public String getPlayroom_phone() {
            return playroom_phone;
        }

        public void setPlayroom_phone(String playroom_phone) {
            this.playroom_phone = playroom_phone;
        }

        public String getPlayroom_area() {
            return playroom_area;
        }

        public void setPlayroom_area(String playroom_area) {
            this.playroom_area = playroom_area;
        }

        public String getTb_name() {
            return tb_name;
        }

        public void setTb_name(String tb_name) {
            this.tb_name = tb_name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
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

        public String getPlayroom_remark() {
            return playroom_remark;
        }

        public void setPlayroom_remark(String playroom_remark) {
            this.playroom_remark = playroom_remark;
        }

        public String getSys_company_code() {
            return sys_company_code;
        }

        public void setSys_company_code(String sys_company_code) {
            this.sys_company_code = sys_company_code;
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

        public String getPalyroom_device() {
            return palyroom_device;
        }

        public void setPalyroom_device(String palyroom_device) {
            this.palyroom_device = palyroom_device;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPlayroom_price() {
            return playroom_price;
        }

        public void setPlayroom_price(String playroom_price) {
            this.playroom_price = playroom_price;
        }

        public String getNewpic() {
            return Newpic;
        }

        public void setNewpic(String Newpic) {
            this.Newpic = Newpic;
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

        public String getPlayroom_iaccommodate() {
            return playroom_iaccommodate;
        }

        public void setPlayroom_iaccommodate(String playroom_iaccommodate) {
            this.playroom_iaccommodate = playroom_iaccommodate;
        }

        public String getBpm_status() {
            return bpm_status;
        }

        public void setBpm_status(String bpm_status) {
            this.bpm_status = bpm_status;
        }

        public String getPlayroom_name() {
            return playroom_name;
        }

        public void setPlayroom_name(String playroom_name) {
            this.playroom_name = playroom_name;
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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String date;
            private String id;

            /**
             * end_time : 09:00
             * begin_time : 08:00
             * id : 402880e66261d93e016261df1e71000b
             */

            private List<TimeBean> time;
            /**
             * week : 星期二
             */

            private String         week;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<TimeBean> getTime() {
                return time;
            }

            public void setTime(List<TimeBean> time) {
                this.time = time;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public static class TimeBean {
                private String end_time;
                private String begin_time;
                private String id;
                /**
                 * isyuding : 1
                 */

                private int isyuding;

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

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getIsyuding() {
                    return isyuding;
                }

                public void setIsyuding(int isyuding) {
                    this.isyuding = isyuding;
                }
            }
        }
    }
}
