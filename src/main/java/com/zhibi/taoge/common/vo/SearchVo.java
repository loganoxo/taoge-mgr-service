package com.zhibi.taoge.common.vo;

/**
 * Created by QinHe on 2018-10-25.
 */
public class SearchVo extends BaseDomain {
    private static final long serialVersionUID = 1L;

    private String startDate;

    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
