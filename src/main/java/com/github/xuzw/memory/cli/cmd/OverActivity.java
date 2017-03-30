package com.github.xuzw.memory.cli.cmd;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import com.github.xuzw.memory.api.MemoryRepository;
import com.github.xuzw.memory.api.MemoryRepository.MemoryWrapper;
import com.github.xuzw.memory.model.Entities;
import com.github.xuzw.memory.model.Memory;
import com.github.xuzw.memory.model.MemoryBuilder;
import com.github.xuzw.memory.model.MemoryType;
import com.github.xuzw.memory.utils.DynamicField;
import com.github.xuzw.memory.utils.DynamicObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月30日 上午11:43:50
 */
public class OverActivity {
    private MemoryType memoryType = MemoryType.activity_over;

    private String _format(MemoryWrapper memoryWrapper, DynamicObject ext, MemoryRepository memoryRepository) {
        int index = memoryWrapper.getIndex();
        Memory memory = memoryWrapper.getMemory();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS");
        String time = dateFormat.format(memory.getTimestamp());
        String locale = memory.getLocale();
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("[%d] %s %s\n", index, memoryType.getName(), ext.getRequiredFields().get(0).getValue()));
        List<String> raw = memoryRepository.get(ext.get("index").getInt()).getRaw();
        sb.append(MemoryType.activity_new.newExtDynamicObject().set(raw).toJsonExceptFirstRequiredField().toJSONString());
        sb.append("\n");
        sb.append(String.format("%s %s", locale, time));
        return sb.toString();
    }

    public void execute(List<String> args, MemoryRepository memoryRepository) throws IOException {
        DynamicObject ext = memoryType.newExtDynamicObject().set(args);
        DynamicField sources = ext.get("sources");
        if (sources != null && sources.isBlank()) {
            args.add("sources");
            args.add(Entities.xuzewei.getName());
            ext = memoryType.newExtDynamicObject().set(args);
        }
        int index = ext.get("index").getInt();
        if (index >= memoryRepository.size()) {
            System.out.println(String.format("不存在的活动 %d", index));
            return;
        }
        Memory memory = memoryRepository.get(index);
        MemoryType memoryType = MemoryType.parse(memory.getType());
        if (MemoryType.activity_new != memoryType) {
            System.out.println(String.format("不符合的类型 [%d] %s", index, memoryType.getName()));
            return;
        }
        if (memoryRepository.isAlreadyOver(index)) {
            System.out.println(String.format("活动已结束 %d", index));
            return;
        }
        MemoryBuilder memoryBuilder = new MemoryBuilder();
        memoryBuilder.raw(ext.getRaw());
        memoryBuilder.timestamp(System.currentTimeMillis());
        memoryBuilder.uuid(UUID.randomUUID().toString());
        memoryBuilder.locale(memoryRepository.getCurrentPlace());
        memoryBuilder.type(MemoryType.activity_over);
        MemoryWrapper memoryWrapper = memoryRepository.append(memoryBuilder.build());
        System.out.println(_format(memoryWrapper, ext, memoryRepository));
    }
}