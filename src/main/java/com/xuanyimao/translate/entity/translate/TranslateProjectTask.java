//package com.xuanyimao.translate.entity.translate;
//
///**
// * 翻译任务
// * @author liuming
// *
// */
//public class TranslateProjectTask  implements Comparable<TranslateProjectTask>{
//	/**计划的id**/
//	private String id;
//
//	/**父级id*/
//	private String parentId;
//
//	/**计划名，一般填版本号*/
//	private String name;
//
//	/**项目描述**/
//	private String desc;
//
//	/**排序序号,默认为0**/
//	private Integer order;
//
//	/**是否父级元素*/
//	private Boolean parent=false;
//
//	/**
//	 * 获取  计划的id
//	 * @return id 计划的id
//	 */
//	public String getId() {
//		return id;
//	}
//
//	/**
//	 * 设置 计划的id
//	 * @param id 计划的id
//	 */
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	/**
//	 * 获取  计划名，一般填版本号
//	 * @return name 计划名，一般填版本号
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * 设置 计划名，一般填版本号
//	 * @param name 计划名，一般填版本号
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	/**
//	 * 获取  项目描述
//	 * @return desc 项目描述
//	 */
//	public String getDesc() {
//		return desc;
//	}
//
//	/**
//	 * 设置 项目描述
//	 * @param desc 项目描述
//	 */
//	public void setDesc(String desc) {
//		this.desc = desc;
//	}
//
//	/**
//	 * 获取  排序序号,默认为0
//	 * @return order 排序序号,默认为0
//	 */
//	public Integer getOrder() {
//		return order;
//	}
//
//	/**
//	 * 设置 排序序号,默认为0
//	 * @param order 排序序号,默认为0
//	 */
//	public void setOrder(Integer order) {
//		this.order = order;
//	}
//
//	/**
//	 * 获取  是否父级元素
//	 * @return parent 是否父级元素
//	 */
//	public Boolean getParent() {
//		return parent;
//	}
//
//	/**
//	 * 设置 是否父级元素
//	 * @param parent 是否父级元素
//	 */
//	public void setParent(Boolean parent) {
//		this.parent = parent;
//	}
//
//	/**
//	 * 获取  父级id
//	 * @return parentId 父级id
//	 */
//	public String getParentId() {
//		return parentId;
//	}
//
//	/**
//	 * 设置 父级id
//	 * @param parentId 父级id
//	 */
//	public void setParentId(String parentId) {
//		this.parentId = parentId;
//	}
//
//	@Override
//	public int compareTo(TranslateProjectTask tpt) {
//		if(this.order==null) this.order=-1;
//		if(tpt.getOrder()==null) tpt.setOrder(-1);
//		return this.order-tpt.getOrder();
//	}
//
//}
