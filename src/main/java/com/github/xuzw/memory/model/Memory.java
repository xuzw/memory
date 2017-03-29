package com.github.xuzw.memory.model;

import java.util.List;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午1:53:41
 */
public class Memory {
    private List<String> raw;
    private long timestamp;
    private String uuid;
    private String locale;
    private String type;

    public List<String> getRaw() {
        return raw;
    }

    public void setRaw(List<String> raw) {
        this.raw = raw;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
