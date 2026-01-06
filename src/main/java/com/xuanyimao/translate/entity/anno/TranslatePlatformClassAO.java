package com.xuanyimao.translate.entity.anno;

import com.xuanyimao.translate.core.translateplatform.TranslatePlatform;

/**
 * 翻译平台 - 注解信息
 */
public class TranslatePlatformClassAO implements Comparable<TranslatePlatformClassAO>{

    /** 程序名 **/
    private String name;
    /** 描述 **/
    private String desc;
    /** 排序序号 */
    private Integer order;
    /** 对象 */
    private TranslatePlatform platform;

    public TranslatePlatformClassAO(){}

    public TranslatePlatformClassAO(String name, String desc, Integer order, TranslatePlatform platform) {
        this.name = name;
        this.desc = desc;
        this.order = order;
        this.platform = platform;
    }

    @Override
    public int compareTo(TranslatePlatformClassAO v) {
        if(this.order==null) this.order=0;
        if(v.getOrder()==null) v.setOrder(0);
        return this.order-v.getOrder();
    }

    /**
     * 获取 程序名
     *
     * @return name 程序名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置 程序名
     *
     * @param name 程序名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 描述
     *
     * @return desc 描述
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * 设置 描述
     *
     * @param desc 描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取 排序序号
     *
     * @return order 排序序号
     */
    public Integer getOrder() {
        return this.order;
    }

    /**
     * 设置 排序序号
     *
     * @param order 排序序号
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * 获取 对象
     *
     * @return platform 对象
     */
    public TranslatePlatform getPlatform() {
        return this.platform;
    }

    /**
     * 设置 对象
     *
     * @param platform 对象
     */
    public void setPlatform(TranslatePlatform platform) {
        this.platform = platform;
    }
}
