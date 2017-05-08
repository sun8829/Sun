package com.ody.library.commonbean;

/**
 * Created by Samuel on 2017/5/4.
 */

public class Ad {
    private long id;
    private String name;
    private long startTime;
    private long endTime;
    private int sort;
    private int type;
    private String title;
    private String content;
    private int refType;
    private long refId;
    private String linkUrl;
    private String imageUrl;
    private String imageTitle;
    private boolean goods;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getSort() {
        return sort;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getRefType() {
        return refType;
    }

    public long getRefId() {
        return refId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public boolean isGoods() {
        return goods;
    }
}
