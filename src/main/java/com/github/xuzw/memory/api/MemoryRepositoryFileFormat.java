package com.github.xuzw.memory.api;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月29日 下午2:03:56
 */
public class MemoryRepositoryFileFormat {
    public static final String line_separator = "\n";
    public static final String encoding = "utf-8";

    public static class MetadataOfNextLine {
        public static final String type_value = "_MetadataOfNextLine";
        private String sign;
        private String type = type_value;

        public MetadataOfNextLine() {
        }

        public MetadataOfNextLine(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
