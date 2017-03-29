package com.github.xuzw.memory.cli.cmd;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import com.github.xuzw.memory.api.MemoryRepository;
import com.github.xuzw.memory.api.MemoryRepository.MemoryWrapper;
import com.github.xuzw.memory.model.Memory;
import com.github.xuzw.memory.model.MemoryBuilder;
import com.github.xuzw.memory.model.MemoryType;
import com.github.xuzw.memory.utils.DynamicObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午11:02:35
 */
public class Append {
    private String _format(MemoryWrapper memoryWrapper, MemoryType memoryType, DynamicObject ext, MemoryRepository memoryRepository) {
        int index = memoryWrapper.getIndex();
        Memory memory = memoryWrapper.getMemory();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS");
        String time = dateFormat.format(memory.getTimestamp());
        String locale = memory.getLocale();
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("[%d] %s %s\n", index, memoryType.getName(), ext.getRequiredFields().get(0).getValue()));
        if (MemoryType.activity_over == memoryType) {
            List<String> raw = memoryRepository.get(ext.get("index").getInt()).getRaw();
            sb.append(MemoryType.activity_new.newExtDynamicObject().set(raw).toJsonExceptFirstRequiredField().toJSONString());
        } else {
            sb.append(ext.toJsonExceptFirstRequiredField().toJSONString());
        }
        sb.append("\n");
        sb.append(String.format("%s %s\n", locale, time));
        return sb.toString();
    }

    public void execute(List<String> args, MemoryRepository memoryRepository) throws IOException {
        String memoryTypeString = args.remove(0);
        MemoryType memoryType = MemoryType.parse(memoryTypeString);
        if (memoryType == null) {
            System.out.println(String.format("不存在的记忆类型 %s\n", memoryTypeString));
            return;
        }
        DynamicObject ext = memoryType.newExtDynamicObject().set(args);
        if (MemoryType.activity_over == memoryType) {
            int index = ext.get("index").getInt();
            if (index >= memoryRepository.size()) {
                System.out.println(String.format("不存在的活动 %d\n", ext.get("index").getInt()));
                return;
            }
        }
        MemoryBuilder memoryBuilder = new MemoryBuilder();
        memoryBuilder.raw(ext.getRaw());
        memoryBuilder.timestamp(System.currentTimeMillis());
        memoryBuilder.uuid(UUID.randomUUID().toString());
        memoryBuilder.locale(memoryRepository.getCurrentPlace());
        memoryBuilder.type(memoryType);
        MemoryWrapper memoryWrapper = memoryRepository.append(memoryBuilder.build());
        System.out.println(_format(memoryWrapper, memoryType, ext, memoryRepository));
    }
}
