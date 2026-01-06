package com.xuanyimao.translate.entity.anno;

import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.project.Version;

/**
 * 翻译程序 - 注解信息
 */
public class TranslateProgramClassAO implements Comparable<TranslateProgramClassAO>{

    /** 程序名 **/
    private String name;
    /** 描述 **/
    private String desc;
    /** 排序序号 */
    private Integer order;

    /**类*/
    private Class<?> clazz;

    /** 实例，这个实例用于打开指定页面等操作 */
    private TranslateProgram translateProgram;

    public TranslateProgramClassAO(){}

    public TranslateProgramClassAO(String name, String desc, Integer order, Class<?> clazz) {
        this.name = name;
        this.desc = desc;
        this.order = order;
        this.clazz = clazz;
    }

    public TranslateProgramClassAO(String name, String desc, Integer order, Class<?> clazz, TranslateProgram translateProgram) {
        this.name = name;
        this.desc = desc;
        this.order = order;
        this.clazz = clazz;
        this.translateProgram = translateProgram;
    }

    @Override
    public int compareTo(TranslateProgramClassAO v) {
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
        return this.order==null?0:this.order;
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
     * 获取 类
     *
     * @return clazz 类
     */
    public Class<?> getClazz() {
        return this.clazz;
    }

    /**
     * 设置 类
     *
     * @param clazz 类
     */
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 获取 实例，这个实例用于打开指定页面等操作
     *
     * @return translateProgram 实例，这个实例用于打开指定页面等操作
     */
    public TranslateProgram getTranslateProgram() {
        return this.translateProgram;
    }

    /**
     * 设置 实例，这个实例用于打开指定页面等操作
     *
     * @param translateProgram 实例，这个实例用于打开指定页面等操作
     */
    public void setTranslateProgram(TranslateProgram translateProgram) {
        this.translateProgram = translateProgram;
    }
}
