package com.fyr.activiti.learning.constant;

public enum SecurityConstant {
    EXCLUDE("EXCLUDE", "排除"),
    IS_ACTIVED("IS_ACTIVED", "是否激活");

    SecurityConstant(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private final String name;
    private final String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
