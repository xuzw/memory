package com.github.xuzw.memory.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午1:55:53
 */
public class MemoryBuilder {
    private List<String> raw = new ArrayList<>();
    private Memory obj = new Memory();

    public MemoryBuilder() {
        obj.setRaw(raw);
    }

    public MemoryBuilder raw(String raw) {
        this.raw.add(raw);
        return this;
    }
    public MemoryBuilder raw(List<String> raw) {
        this.raw.addAll(raw);
        return this;
    }

    public MemoryBuilder timestamp(long timestamp) {
        obj.setTimestamp(timestamp);
        return this;
    }

    public MemoryBuilder uuid(String uuid) {
        obj.setUuid(uuid);
        return this;
    }

    public MemoryBuilder locale(String locale) {
        obj.setLocale(locale);
        return this;
    }

    public MemoryBuilder type(MemoryType type) {
        obj.setType(type.getName());
        return this;
    }

    public Memory build() {
        return obj;
    }
}
