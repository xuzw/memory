package com.github.xuzw.memory.model;

import java.util.Arrays;
import java.util.List;

import com.github.xuzw.memory.utils.DynamicField;
import com.github.xuzw.memory.utils.DynamicObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午2:27:30
 */
public enum MemoryType {
    /**
     * 新实体
     */
    new_entity("新实体", Arrays.asList("new_entity", "entity", "e"), //
            new DynamicObject().requiredField(new DynamicField().name("name")) //
                    .optionalField(new DynamicField().name("shortNames").shortName("short")) //
                    .optionalField(new DynamicField().name("properties").shortName("prop")) //
                    .optionalField(new DynamicField().name("sources").shortName("source")) //
    ),
    /**
     * 新活动
     */
    new_activity("新活动", Arrays.asList("new_activity", "act", "a"), //
            new DynamicObject().requiredField(new DynamicField().name("type")) //
                    .requiredField(new DynamicField().name("target")) //
                    .optionalField(new DynamicField().name("effect")) //
                    .optionalField(new DynamicField().name("sources").shortName("source")) //
    ),
    /**
     * 结束活动
     */
    over_activity("结束活动", Arrays.asList("over_activity", "over", "o"), //
            new DynamicObject().requiredField(new DynamicField().name("index")) //
                    .optionalField(new DynamicField().name("sources").shortName("source")) //
    ),
    /**
     * 进入场所
     */
    into_place("进入场所", Arrays.asList("into_place", "into"), //
            new DynamicObject().requiredField(new DynamicField().name("target")) //
                    .optionalField(new DynamicField().name("effect")) //
                    .optionalField(new DynamicField().name("sources").shortName("source")) //
    ),
    /**
     * 谁
     */
    who("谁", Arrays.asList("who"), //
            new DynamicObject().requiredField(new DynamicField().name("name")) //
                    .optionalField(new DynamicField().name("shortNames").shortName("short")) //
                    .optionalField(new DynamicField().name("properties").shortName("prop")) //
                    .optionalField(new DynamicField().name("sources").shortName("source")) //
    );

    private String name;
    private List<String> shortNames;
    private DynamicObject extDynamicObjectPrototype;

    private MemoryType(String name, List<String> shortNames, DynamicObject extDynamicObjectPrototype) {
        this.name = name;
        this.shortNames = shortNames;
        this.extDynamicObjectPrototype = extDynamicObjectPrototype;
    }

    public String getName() {
        return name;
    }

    public List<String> getShortNames() {
        return shortNames;
    }

    public DynamicObject newExtDynamicObject() {
        return new DynamicObject(extDynamicObjectPrototype);
    }

    public static MemoryType parse(String name) {
        for (MemoryType memoryType : values()) {
            if (memoryType.getName().equals(name)) {
                return memoryType;
            }
            for (String shortName : memoryType.getShortNames()) {
                if (shortName.equals(name)) {
                    return memoryType;
                }
            }
        }
        return null;
    }
}
