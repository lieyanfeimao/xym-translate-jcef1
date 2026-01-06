package com.xuanyimao.translate.entity.project;

/**
 * 项目
 */
public class Project implements Comparable<Project>{

	/** 数据id */
	private Long id;
	/** 显示的文本 */
	private String text;
	/** 排序序号，默认为0 */
	private Integer order;

	/** 父节点ID */
	private Long parentId;

	@Override
	public int compareTo(Project v) {
		if(this.order==null) this.order=0;
		if(v.getOrder()==null) v.setOrder(0);
		return this.order-v.getOrder();
	}

	/** 
	 * 获取  数据id 
	 * @return id 数据id 
	 */
	public Long getId() {
		return id;
	}

	/** 
	 * 设置 数据id
	 * @param id 数据id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/** 
	 * 获取  显示的文本 
	 * @return text 显示的文本 
	 */
	public String getText() {
		return text;
	}

	/** 
	 * 设置 显示的文本
	 * @param text 显示的文本
	 */
	public void setText(String text) {
		this.text = text;
	}

	/** 
	 * 获取  排序序号，默认为0 
	 * @return order 排序序号，默认为0 
	 */
	public Integer getOrder() {
		return order==null?0:order;
	}

	/** 
	 * 设置 排序序号，默认为0
	 * @param order 排序序号，默认为0
	 */
	public void setOrder(Integer order) {
		if(order==null) order=0;
		this.order = order;
	}

	/**
	 * 获取 父节点ID
	 *
	 * @return parentId 父节点ID
	 */
	public Long getParentId() {
		return this.parentId;
	}

	/**
	 * 设置 父节点ID
	 *
	 * @param parentId 父节点ID
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}




}
