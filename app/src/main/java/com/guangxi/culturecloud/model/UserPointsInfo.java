package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/2 14:11
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class UserPointsInfo {

    /**
     * fdate : 2018-04-02
     * id : a549ee10-5fce-4071-ba6a-ecabe874d6c8
     * type : 2
     */

    private List<UserpointBean> userpoint;
    /**
     * totoal_points : 150
     * userpoint : [{"fdate":"2018-04-03","id":"9d3e9167-e25d-42a8-a2a9-0f786b0ff20e","type":"2"},{"fdate":"2018-04-02","id":"a549ee10-5fce-4071-ba6a-ecabe874d6c8","type":"2"},{"fdate":"2018-04-02","id":"f6e92822-62a6-4259-ab47-43068b17e600","type":"4"}]
     */

    private ArrBean             arr;

    public List<UserpointBean> getUserpoint() {
        return userpoint;
    }

    public void setUserpoint(List<UserpointBean> userpoint) {
        this.userpoint = userpoint;
    }

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public static class UserpointBean {
        private String fdate;
        private String id;
        private String type;

        public String getFdate() {
            return fdate;
        }

        public void setFdate(String fdate) {
            this.fdate = fdate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ArrBean {
        private String totoal_points;
        /**
         * fdate : 2018-04-03
         * id : 9d3e9167-e25d-42a8-a2a9-0f786b0ff20e
         * type : 2
         */

        private List<UserpointBean> userpoint;

        public String getTotoal_points() {
            return totoal_points;
        }

        public void setTotoal_points(String totoal_points) {
            this.totoal_points = totoal_points;
        }

        public List<UserpointBean> getUserpoint() {
            return userpoint;
        }

        public void setUserpoint(List<UserpointBean> userpoint) {
            this.userpoint = userpoint;
        }

        public static class UserpointBean {
            private String fdate;
            private String id;
            private String type;

            public String getFdate() {
                return fdate;
            }

            public void setFdate(String fdate) {
                this.fdate = fdate;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
