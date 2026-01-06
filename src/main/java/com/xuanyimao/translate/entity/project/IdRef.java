package com.xuanyimao.translate.entity.project;

/**
 * 用于文件ID的类
 */
public class IdRef {

    /** 数值 */
    private long value;

    public IdRef() {
        this.value=0;
    }

    public IdRef(long value) {
        this.value = value;
    }

    /**
     * 获取 数值
     *
     * @return value 数值
     */
    public long getValue() {
        return this.value;
    }

    /**
     * 设置 数值
     *
     * @param value 数值
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     * 自增
     */
    public void increment(){
        this.value++;
    }
}
