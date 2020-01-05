package com.xxun.watch.xunpet.bean;


import net.minidev.json.JSONObject;

/**
 * Created by huangyouyang on 2017/9/16.
 */

public class XunPetRankBean {

    /**
     * "PL": {
     "date": "20170915",
     "cityName": "南通市",
     "cityCount": 1717,
     "totalOrder": 258738,
     "totalCount": 361225,
     "cityOrder": 1209
     }
     */

    public String date;
    public String cityName;
    public int cityCount;
    public int cityOrder;
    public int totalCount;
    public int totalOrder;

    public static XunPetRankBean toBeanFromJson(JSONObject jsonObject) {
        XunPetRankBean rankBean = new XunPetRankBean();
        try {
            rankBean.date = (String) jsonObject.get("date");
            rankBean.cityName = (String) jsonObject.get("cityName");
            rankBean.cityCount = (int) jsonObject.get("cityCount");
            rankBean.cityOrder = (int) jsonObject.get("cityOrder");
            rankBean.totalCount = (int) jsonObject.get("totalCount");
            rankBean.totalOrder = (int) jsonObject.get("totalOrder");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rankBean;
    }
}
