package com.xuanyimao.translate.entity.translate;


/**
 * 每个文件的数据
 * @author liuming
 *
 */
public class TranslateData {
	/** id **/
	private String id;
	/**原文**/
	private String oldText;
	/**译文**/
	private String tlText;
	/**版本号*/
	private Integer version=0;
	
	
	/** 
	 * 获取  版本号 
	 * @return version 版本号 
	 */
	public Integer getVersion() {
		return version;
	}
	/** 
	 * 设置 版本号
	 * @param version 版本号
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	/***
	 * 更新版本号
	 * @author liuming
	 * @since 2023年9月26日
	 */
	public void updateVersion() {
		this.version++;
	}
	/** 
	 * 获取  id 
	 * @return id id 
	 */
	public String getId() {
		return id;
	}
	/** 
	 * 设置 id
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/** 
	 * 获取  原文 
	 * @return oldText 原文 
	 */
	public String getOldText() {
		return oldText;
	}
	/** 
	 * 设置 原文
	 * @param oldText 原文
	 */
	public void setOldText(String oldText) {
		this.oldText = oldText;
	}
	/** 
	 * 获取  译文 
	 * @return tlText 译文 
	 */
	public String getTlText() {
		return tlText;
	}
	/** 
	 * 设置 译文
	 * @param tlText 译文
	 */
	public void setTlText(String tlText) {
		this.tlText = tlText;
	}
	
	/**
	 * 
	 * @param id
	 * @param oldText 原文
	 * @param tlText 译文
	 */
	public TranslateData(String id, String oldText, String tlText) {
		super();
		this.id = id;
		this.oldText = oldText;
		this.tlText = tlText;
	}
	
	
}
