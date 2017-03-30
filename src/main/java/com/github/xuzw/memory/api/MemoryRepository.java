package com.github.xuzw.memory.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import com.alibaba.fastjson.JSON;
import com.github.xuzw.memory.model.Entities;
import com.github.xuzw.memory.model.Memory;
import com.github.xuzw.memory.model.MemoryType;
import com.github.xuzw.memory.utils.DynamicObject;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午3:05:30
 */
public class MemoryRepository {
    private static final String unknow_place = "未知";

    private String path;
    private FileWriterWithEncoding writer;
    private FileReader reader;
    private BufferedReader bReader;
    private List<Memory> memories = new ArrayList<>();

    public MemoryRepository(String path) throws IOException, MemoryRepositoryFileFormatException {
        this.path = path;
        writer = new FileWriterWithEncoding(path, MemoryRepositoryFileFormat.encoding, true);
        reader = new FileReader(path);
        bReader = new BufferedReader(reader);
        _load();
    }

    public String getPath() {
        return path;
    }

    public MemoryWrapper append(Memory memory) throws IOException {
        MemoryWrapper memoryWrapper = new MemoryWrapper(memory, memories.size());
        memories.add(memory);
        writer.append(JSON.toJSONString(memory) + MemoryRepositoryFileFormat.line_separator);
        return memoryWrapper;
    }

    private void _load() throws IOException, MemoryRepositoryFileFormatException {
        memories.clear();
        Memory memory = null;
        while ((memory = _read()) != null) {
            memories.add(memory);
        }
    }

    private Memory _read() throws IOException, MemoryRepositoryFileFormatException {
        String line = bReader.readLine();
        if (line == null) {
            return null;
        }
        return JSON.parseObject(line, Memory.class);
    }

    public List<Memory> getMemories() {
        return memories;
    }

    public Memory get(int index) {
        return memories.get(index);
    }

    public int size() {
        return memories.size();
    }

    public void close() {
        IOUtils.closeQuietly(writer);
        IOUtils.closeQuietly(bReader);
        IOUtils.closeQuietly(reader);
    }

    public String getCurrentPlace() {
        DynamicObject lastIntoPlace = null;
        for (Memory memory : memories) {
            MemoryType memoryType = MemoryType.parse(memory.getType());
            if (MemoryType.into_place == memoryType) {
                DynamicObject ext = MemoryType.into_place.newExtDynamicObject().set(memory.getRaw());
                if (ext.get("sources").getList().contains(Entities.xuzewei.getName())) {
                    lastIntoPlace = ext;
                }
            }
        }
        return lastIntoPlace == null ? unknow_place : lastIntoPlace.get("target").getValue();
    }

    public Memory getIfAlreadyOver(int index) {
        for (Memory memory : memories) {
            MemoryType memoryType = MemoryType.parse(memory.getType());
            if (MemoryType.activity_over == memoryType) {
                DynamicObject ext = MemoryType.activity_over.newExtDynamicObject().set(memory.getRaw());
                if (ext.get("index").getInt() == index) {
                    return memory;
                }
            }
        }
        return null;
    }

    public boolean isAlreadyOver(int index) {
        return getIfAlreadyOver(index) != null;
    }

    public static class MemoryWrapper {
        private Memory memory;
        private int index;

        public MemoryWrapper(Memory memory, int index) {
            this.memory = memory;
            this.index = index;
        }

        public Memory getMemory() {
            return memory;
        }

        public int getIndex() {
            return index;
        }
    }
}
