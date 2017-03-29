package com.github.xuzw.memory;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.xuzw.memory.utils.DynamicField;
import com.github.xuzw.memory.utils.DynamicObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月30日 上午12:35:35
 */
public class DynamicObjectTest {
    public static void main(String[] args) {
        DynamicObject activity = new DynamicObject();
        activity.requiredField(new DynamicField().name("type"));
        activity.requiredField(new DynamicField().name("target"));
        activity.optionalField(new DynamicField().name("effect"));
        activity.optionalField(new DynamicField().name("sources"));
        activity.set(new ArrayList<>(Arrays.asList("下载", "脚本之家", "effect", "R语言实战(第2版) ([美]卡巴科弗) 中文pdf完整版[19MB]", "sources", "徐泽威")));
        System.out.println(activity.toJson().toJSONString());
    }
}
