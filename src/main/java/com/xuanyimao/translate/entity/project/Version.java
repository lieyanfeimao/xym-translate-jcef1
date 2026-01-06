package com.xuanyimao.translate.entity.project;

/**
 * 版本
 */
public class Version implements Comparable<Version> {

    /** 数据ID */
    private Long id;
    /** 版本号 */
    private String name;
    /** 排序序号 */
    private Integer order;
    /** 备注 */
    private String remark;
    /** 程序名 */
    private String programName;

    /** 程序描述 */
    private String programDesc;

    /** uuid,也是版本的工作目录名 */
    private String uuid;

    @Override
    public int compareTo(Version v) {
        if(this.order==null) this.order=0;
        if(v.getOrder()==null) v.setOrder(0);
        return this.order-v.getOrder();
    }

    /**
     * 获取 数据ID
     *
     * @return id 数据ID
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置 数据ID
     *
     * @param id 数据ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 版本号
     *
     * @return name 版本号
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置 版本号
     *
     * @param name 版本号
     */
    public void setName(String name) {
        this.name = name;
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
        if (order == null) {
            order=0;
        }
        this.order = order;
    }

    /**
     * 获取 备注
     *
     * @return remark 备注
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * 设置 备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取 程序名
     *
     * @return programName 程序名
     */
    public String getProgramName() {
        return this.programName;
    }

    /**
     * 设置 程序名
     *
     * @param programName 程序名
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * 获取 程序描述
     *
     * @return programDesc 程序描述
     */
    public String getProgramDesc() {
        return this.programDesc;
    }

    /**
     * 设置 程序描述
     *
     * @param programDesc 程序描述
     */
    public void setProgramDesc(String programDesc) {
        this.programDesc = programDesc;
    }

    /**
     * 获取 uuid也是版本的工作目录名
     *
     * @return uuid uuid也是版本的工作目录名
     */
    public String getUuid() {
        return this.uuid;
    }

    /**
     * 设置 uuid也是版本的工作目录名
     *
     * @param uuid uuid也是版本的工作目录名
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
