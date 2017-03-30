package com.github.xuzw.memory.cli.cmd;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.github.xuzw.activity.api.utils.DurationFormat;
import com.github.xuzw.activity.model.Activity;
import com.github.xuzw.activity.model.ActivityBuilder;
import com.github.xuzw.memory.api.MemoryRepository;
import com.github.xuzw.memory.model.Memory;
import com.github.xuzw.memory.model.MemoryType;
import com.github.xuzw.memory.utils.DynamicObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月30日 下午12:13:02
 */
public class Preview {
    private Map<Integer, Activity> activities = new TreeMap<Integer, Activity>();

    private void _parseActivities(MemoryRepository memoryRepository) {
        for (int i = 0; i < memoryRepository.size(); i++) {
            Memory memory = memoryRepository.get(i);
            MemoryType memoryType = MemoryType.parse(memory.getType());
            if (MemoryType.activity_new == memoryType) {
                DynamicObject ext = memoryType.newExtDynamicObject().set(memory.getRaw());
                ActivityBuilder activityBuilder = new ActivityBuilder();
                activityBuilder.timestamp(memory.getTimestamp());
                activityBuilder.uuid(memory.getUuid());
                activityBuilder.locale(memory.getLocale());
                activityBuilder.type(ext.get("type").getValue());
                activityBuilder.target(ext.get("target").getValue());
                activityBuilder.sources(ext.get("sources").getList());
                activityBuilder.effect(ext.get("effect").getValue());
                activities.put(i, activityBuilder.build());
            } else if (MemoryType.activity_over == memoryType) {
                DynamicObject ext = memoryType.newExtDynamicObject().set(memory.getRaw());
                int index = ext.get("index").getInt();
                Activity activity = activities.get(index);
                activity.setDur(memory.getTimestamp() - activity.getTimestamp());
            } else if (MemoryType.into_place == memoryType) {
                DynamicObject ext = memoryType.newExtDynamicObject().set(memory.getRaw());
                ActivityBuilder activityBuilder = new ActivityBuilder();
                activityBuilder.timestamp(memory.getTimestamp());
                activityBuilder.uuid(memory.getUuid());
                activityBuilder.locale(memory.getLocale());
                activityBuilder.type(memoryType.getName());
                activityBuilder.target(ext.get("target").getValue());
                activityBuilder.sources(ext.get("sources").getList());
                activityBuilder.effect(ext.get("effect").getValue());
                activities.put(i, activityBuilder.build());
            }
        }
    }

    private String _formatActivity(Integer index, Activity activity) {
        String type = activity.getType();
        String locale = activity.getLocale();
        long dur = activity.getDur();
        boolean done = dur > 0;
        StringBuffer sb = new StringBuffer();
        if (done) {
            sb.append(String.format("[%d] %s 已完毕 ( 时长 %s )\n", index, type, new DurationFormat(dur).getString()));
        } else {
            sb.append(String.format("[%d] %s 进行中... ( %s ) ...\n", index, type, new DurationFormat(System.currentTimeMillis() - activity.getTimestamp()).getString()));
        }
        sb.append(activity.getTarget());
        String effect = activity.getEffect();
        if (StringUtils.isNoneBlank(effect)) {
            sb.append(" ").append(effect);
        }
        sb.append("\n");
        String time = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS").format(activity.getTimestamp());
        sb.append(String.format("%s %s\n", locale, time));
        return sb.toString();
    }

    private String _formatIntoPlace(Integer index, Activity activity) {
        String type = activity.getType();
        String locale = activity.getLocale();
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("[%d] %s %s\n", index, type, activity.getTarget()));
        String effect = activity.getEffect();
        if (StringUtils.isNoneBlank(effect)) {
            sb.append(effect).append("\n");
        }
        String time = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS").format(activity.getTimestamp());
        sb.append(String.format("%s %s\n", locale, time));
        return sb.toString();
    }

    public void execute(MemoryRepository memoryRepository) {
        _parseActivities(memoryRepository);
        for (Integer index : activities.keySet()) {
            Activity activity = activities.get(index);
            if (MemoryType.into_place.getName().equals(activity.getType())) {
                System.out.println(_formatIntoPlace(index, activity));
            } else {
                System.out.println(_formatActivity(index, activity));
            }
        }
    }
}
