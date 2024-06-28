package com.cqupt.software_10.common;

// TODO 公共模块新增
public enum FeatureType {
    DIAGNOSIS(0, "population"), // 按钮1
    EXAMINE(1, "physiology"), // 按钮2
    PATHOLOGY(2, "society"),
    VITAL_SIGNS(3, "society"); // 按钮3

    private final int code;
    private final String name;

    FeatureType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
