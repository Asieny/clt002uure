package com.guangxi.culturecloud.model;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/1 14:00
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class BannerInfo {


    /**
     * newpic : upload/files/20180228/柳州.jpg
     */

    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String newpic;

        public String getNewpic() {
            return newpic;
        }

        public void setNewpic(String newpic) {
            this.newpic = newpic;
        }
    }
}
