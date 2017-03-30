package com.github.xuzw.memory.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午11:55:04
 */
public class DynamicField {
    private String name;
    private List<String> shortNames = new ArrayList<>();
    private String value;

    public DynamicField() {
    }

    public DynamicField(DynamicField prototype) {
        this.name = prototype.name;
        this.shortNames = prototype.shortNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getShortNames() {
        return shortNames;
    }

    public void setShortNames(List<String> shortNames) {
        this.shortNames = shortNames;
    }

    public String getValue() {
        return value;
    }

    public boolean isBlank() {
        return StringUtils.isBlank(value);
    }

    public int getInt() {
        return Integer.valueOf(value);
    }

    public List<String> getList() {
        List<String> list = new ArrayList<>();
        for (String part : value.split(",")) {
            if (StringUtils.isNotBlank(part)) {
                list.add(part);
            }
        }
        return list;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean hasName(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }
        if (shortNames != null) {
            for (String shortName : shortNames) {
                if (shortName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public DynamicField name(String name) {
        this.name = name;
        return this;
    }

    public DynamicField shortName(String shortName) {
        shortNames.add(shortName);
        return this;
    }

    public DynamicField value(String value) {
        this.value = value;
        return this;
    }
}
