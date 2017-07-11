package com.ody.library.commonbean;

import com.ody.library.base.BaseBean;

import java.util.List;

/**
 * Created by Samuel on 2017/5/4.
 */

public class AdBean extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    private static class Data {
        private List<Ad> ad_banner;

        public void setAd_banner(List<Ad> ad_banner) {
            this.ad_banner = ad_banner;
        }

        public List<Ad> getAd_banner() {
            return ad_banner;
        }
    }
}
