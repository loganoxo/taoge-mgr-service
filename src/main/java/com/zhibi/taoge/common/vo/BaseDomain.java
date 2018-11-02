package com.zhibi.taoge.common.vo;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author qinhe
 */
public class BaseDomain implements Serializable {

    private static final long serialVersionUID = 3870028896024217898L;

    @Transient
    private JSONObject other = new JSONObject();

    public JSONObject getOther() {
        return other;
    }

    public void setOther(JSONObject other) {
        this.other = other;
    }
}
