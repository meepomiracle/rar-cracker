package com.cracker.domain;

/**
 * Created by hadoop on 2016/9/25.
 */
public class CrackerParam {
    /**
     * 密码最大长度
     */
    private int len;

    /**
     * 同时处理的线程数量
     */
    private int threadNum;
    /**
     * 是否包含所有数字，0-9
     */
    private boolean isIncluedDigit;

    /**
     * 是否包含26个字母
     */
    private boolean isIncluedChar;

    /**
     * 是否包含大写字母
     */
    private boolean isIncluedUpperCase;

    public CrackerParam(int len, int threadNum, boolean isIncluedDigit, boolean isIncluedChar, boolean
            isIncluedUpperCase, boolean isInlucedSpecial, boolean isUsedDict) {
        this.len = len;
        this.threadNum = threadNum;
        this.isIncluedDigit = isIncluedDigit;
        this.isIncluedChar = isIncluedChar;
        this.isIncluedUpperCase = isIncluedUpperCase;
        this.isInlucedSpecial = isInlucedSpecial;
        this.isUsedDict = isUsedDict;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public boolean isIncluedUpperCase() {

        return isIncluedUpperCase;
    }

    public void setIncluedUpperCase(boolean incluedUpperCase) {
        isIncluedUpperCase = incluedUpperCase;
    }

    /**
     * 是否包含特殊字符
     */
    private boolean isInlucedSpecial;

    /**
     * 是否使用字典
     */
    private boolean isUsedDict;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public boolean isIncluedDigit() {
        return isIncluedDigit;
    }

    public void setIncluedDigit(boolean incluedDigit) {
        isIncluedDigit = incluedDigit;
    }

    public boolean isIncluedChar() {
        return isIncluedChar;
    }

    public void setIncluedChar(boolean incluedChar) {
        isIncluedChar = incluedChar;
    }

    public boolean isInlucedSpecial() {
        return isInlucedSpecial;
    }

    public void setInlucedSpecial(boolean inlucedSpecial) {
        isInlucedSpecial = inlucedSpecial;
    }

    public boolean isUsedDict() {
        return isUsedDict;
    }

    public void setUsedDict(boolean usedDict) {
        isUsedDict = usedDict;
    }
}
