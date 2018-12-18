package com.guangxi.culturecloud.model;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/29 16:05
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class HelpeRulerInfo {

    /**
     * helprule : <p><strong>活动预定流程</strong></p><p>点击某一活动,进入活动详情页,点击底部【立即预定】,填写预约信息,点击【确认预订】。<br/><br/><br/><br/><strong>注意事项</strong></p><p>1)部分活动预定需要用户达到规定的积分,或者每次,预定需要扣除部分积分,具体视不同活动而定。<br/>2)部分活动为秒杀活动,只可在限定时间段内才可预定,具体视不同活动而定,<br/></p><p><br/></p>
     */

    private ArrBean arr;

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String helprule;

        public String getHelprule() {
            return helprule;
        }

        public void setHelprule(String helprule) {
            this.helprule = helprule;
        }
    }
}
