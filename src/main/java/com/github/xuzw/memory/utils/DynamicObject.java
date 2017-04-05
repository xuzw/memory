package com.github.xuzw.memory.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月30日 上午12:10:32
 */
public class DynamicObject {
    private List<DynamicField> requiredFields = new ArrayList<>();
    private List<DynamicField> optionalFields = new ArrayList<>();
    private List<String> raw = new ArrayList<>();

    public DynamicObject() {
    }

    public DynamicObject(DynamicObject prototype) {
        for (DynamicField i : prototype.requiredFields) {
            requiredField(new DynamicField(i));
        }
        for (DynamicField i : prototype.optionalFields) {
            optionalField(new DynamicField(i));
        }
    }

    public DynamicObject requiredField(DynamicField requiredField) {
        this.requiredFields.add(requiredField);
        return this;
    }

    public DynamicObject optionalField(DynamicField optionalField) {
        this.optionalFields.add(optionalField);
        return this;
    }

    public DynamicObject setRaw(List<String> raw) {
        this.raw = new ArrayList<>(raw);
        List<String> copy = new ArrayList<>(raw);
        for (DynamicField requiredField : requiredFields) {
            requiredField.setValue(copy.remove(0));
        }
        for (int i = 0; i < copy.size(); i += 2) {
            String name = copy.get(i);
            String value = copy.get(i + 1);
            get(name).setValue(value);
        }
        return this;
    }

    public DynamicField get(String name) {
        for (DynamicField requiredField : requiredFields) {
            if (requiredField.hasName(name)) {
                return requiredField;
            }
        }
        for (DynamicField optionalField : optionalFields) {
            if (optionalField.hasName(name)) {
                return optionalField;
            }
        }
        return null;
    }

    public List<DynamicField> getRequiredFields() {
        return requiredFields;
    }

    public List<DynamicField> getOptionalFields() {
        return optionalFields;
    }

    public List<String> getRaw() {
        return raw;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        for (DynamicField i : requiredFields) {
            json.put(i.getName(), i.getValue());
        }
        for (DynamicField i : optionalFields) {
            if (StringUtils.isNotBlank(i.getValue())) {
                json.put(i.getName(), i.getValue());
            }
        }
        return json;
    }

    public JSONObject toJsonExceptFirstRequiredField() {
        JSONObject json = toJson();
        json.remove(requiredFields.get(0).getName());
        return json;
    }
}
