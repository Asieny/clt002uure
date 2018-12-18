package com.guangxi.culturecloud.model;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/2 9:11
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class IntegralRuleInfo {

    /**
     * rule : <p style="-webkit-tap-highlight-color: transparent; margin-top: 0px; margin-bottom: 40px; padding: 0px; line-height: 50px; color: rgb(38, 38, 38); font-family: 微软雅黑, &#39;Hiragino Sans GB&#39;; font-size: 28px; white-space: normal;">文化引领品质生活</p><p style="-webkit-tap-highlight-color: transparent; margin-top: 0px; margin-bottom: 40px; padding: 0px; line-height: 50px; color: rgb(38, 38, 38); font-family: 微软雅黑, &#39;Hiragino Sans GB&#39;; font-size: 28px; white-space: normal;">文化云是一款聚焦文化领域，提供公众文化生活和消费的互联网平台；目前已汇聚全上海22万场文化活动、5500余文化场馆，上万家文化社团，为公众提供便捷和有品质的文化生活服务。</p><p style="-webkit-tap-highlight-color: transparent; margin-top: 0px; margin-bottom: 40px; padding: 0px; line-height: 50px; color: rgb(38, 38, 38); font-family: 微软雅黑, &#39;Hiragino Sans GB&#39;; font-size: 28px; white-space: normal;">您可以通过以下方式访问文化云，获得免费公益文化活动机会。</p><p style="-webkit-tap-highlight-color: transparent; margin-top: 0px; margin-bottom: 40px; padding: 0px; line-height: 50px; color: rgb(38, 38, 38); font-family: 微软雅黑, &#39;Hiragino Sans GB&#39;; font-size: 28px; white-space: normal;">想要预定热门免费公益的文化活动，您必须是我们的认证会员：</p><p style="-webkit-tap-highlight-color: transparent; margin-top: 0px; margin-bottom: 40px; padding: 0px; line-height: 50px; color: rgb(38, 38, 38); font-family: 微软雅黑, &#39;Hiragino Sans GB&#39;; font-size: 28px; white-space: normal;">无论您是在PC端、微信端、亦或是APP端发现文化云，只需用手机号码注册，就能成为我们的会员。</p><p><br/></p>
     */

    private ArrBean arr;

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        private String rule;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }
    }
}
