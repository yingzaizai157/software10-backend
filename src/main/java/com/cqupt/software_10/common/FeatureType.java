package com.cqupt.software_10.common;

// TODO 公共模块新增
public enum FeatureType {
    DIAGNOSIS(0, "is_demography"), // 按钮1
    EXAMINE(1, "is_physiological"), // 按钮2
    PATHOLOGY(2, "is_sociology"),
    VITAL_SIGNS(3, "is_sociology"); // 按钮3

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
