package com.github.xuzw.memory.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.xuzw.memory.api.MemoryRepository;
import com.github.xuzw.memory.api.MemoryRepositoryFileFormatException;
import com.github.xuzw.memory.cli.cmd.Append;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午4:08:06
 */
public class Main {
    public static void main(String[] args) throws IOException, MemoryRepositoryFileFormatException, InstantiationException, IllegalAccessException {
        MemoryRepository memoryRepository = new MemoryRepository(Config.load().getMemoryRepositoryFilePath());
        if (args.length == 0) {
            new com.github.xuzw.memory.cli.cmd.List().execute(memoryRepository);
        } else {
            List<String> list = new ArrayList<>();
            for (String arg : args) {
                list.add(arg);
            }
            new Append().execute(list, memoryRepository);
        }
        memoryRepository.close();
    }
}
