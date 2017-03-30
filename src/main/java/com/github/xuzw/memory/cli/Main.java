package com.github.xuzw.memory.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.xuzw.memory.api.MemoryRepository;
import com.github.xuzw.memory.api.MemoryRepositoryFileFormatException;
import com.github.xuzw.memory.cli.cmd.All;
import com.github.xuzw.memory.cli.cmd.Append;
import com.github.xuzw.memory.cli.cmd.Help;
import com.github.xuzw.memory.cli.cmd.OverActivity;
import com.github.xuzw.memory.cli.cmd.Preview;
import com.github.xuzw.memory.model.MemoryType;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午4:08:06
 */
public class Main {
    public static void main(String[] args) throws IOException, MemoryRepositoryFileFormatException, InstantiationException, IllegalAccessException {
        MemoryRepository memoryRepository = new MemoryRepository(Config.load().getMemoryRepositoryFilePath());
        if (args.length == 0) {
            // 预览
            new Preview().execute(memoryRepository);
            memoryRepository.close();
            return;
        }
        List<String> argList = _getArgList(args);
        String firstArg = argList.remove(0);
        if (All.class.getSimpleName().equalsIgnoreCase(firstArg)) {
            // 查看全部
            new All().execute(memoryRepository);
            memoryRepository.close();
            return;
        }
        if (Help.class.getSimpleName().equalsIgnoreCase(firstArg)) {
            // 帮助
            new Help().execute(argList);
            memoryRepository.close();
            return;
        }
        MemoryType memoryType = MemoryType.parse(firstArg);
        if (memoryType == null) {
            // 错误的参数
            System.out.println(String.format("不存在的记忆类型 %s\n", firstArg));
            memoryRepository.close();
            return;
        }
        if (MemoryType.activity_over == memoryType) {
            // 结束活动
            new OverActivity().execute(argList, memoryRepository);
            memoryRepository.close();
            return;
        }
        // 其它类型
        new Append().execute(memoryType, argList, memoryRepository);
        memoryRepository.close();
    }

    private static List<String> _getArgList(String[] args) {
        List<String> list = new ArrayList<>();
        for (String arg : args) {
            list.add(arg);
        }
        return list;
    }
}
