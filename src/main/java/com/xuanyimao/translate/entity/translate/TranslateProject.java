//package com.xuanyimao.translate.entity.translate;
//
//import java.util.List;
//
///**
// * 翻译项目
// * @author liuming
// *
// */
//public class TranslateProject implements Comparable<TranslateProject>{
//
//	/**计划的唯一标识**/
//	private String id;
//	/**翻译计划名**/
//	private String name;
//	/**排序序号,默认为0**/
//	private Integer order;
//
//	/**项目描述**/
//	private String desc;
//
//	/**翻译任务列表**/
//	private List<TranslateProjectTask> children;
//
//	/**是否父级元素*/
//	private Boolean parent=true;
//
//	/**
//	 * 获取  计划的唯一标识
//	 * @return id 计划的唯一标识
//	 */
//	public String getId() {
//		return id;
//	}
//
//	/**
//	 * 设置 计划的唯一标识
//	 * @param id 计划的唯一标识
//	 */
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	/**
//	 * 获取  翻译计划名
//	 * @return name 翻译计划名
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * 设置 翻译计划名
//	 * @param name 翻译计划名
//	 */
//	public void setName(String name) {
//		this.name = name;
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
//	 * 获取  翻译任务列表
//	 * @return children 翻译任务列表
//	 */
//	public List<TranslateProjectTask> getChildren() {
//		return children;
//	}
//
//	/**
//	 * 设置 翻译任务列表
//	 * @param children 翻译任务列表
//	 */
//	public void setChildren(List<TranslateProjectTask> children) {
//		this.children = children;
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
//	@Override
//	public int compareTo(TranslateProject tp) {
//		if(this.order==null) this.order=-1;
//		if(tp.getOrder()==null) tp.setOrder(-1);
//		return this.order-tp.getOrder();
//	}
//
//}
