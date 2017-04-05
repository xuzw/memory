package com.github.xuzw.memory.cli.cmd;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年4月5日 下午1:37:06
 */
public class Version {
    public static final String cmd = "version";
    private static final String version = "2017.04.05.1704";

    public void execute() {
        System.out.println(version);
    }
}
