package com.xuanyimao.translate.entity.htmldoc;

import org.jsoup.nodes.Element;

/**
 * HTML文档通用的实体类
 * @author liuming
 */
public class DocElement {
    
	/**目标元素*/
    private Element elem;
    
    /**待翻译的内容*/
    private String text;
    
    /**标识*/
    private int flag=0;
    
    /**翻译元素**/
    private Element tlElem;
    
    /**模板元素**/
    private Element modelElem;
    
    /**译文**/
    private String tlText;
    
    /**id字符串**/
    private String id;


	/**
	 * 获取 目标元素
	 *
	 * @return elem 目标元素
	 */
	public Element getElem() {
		return this.elem;
	}

	/**
	 * 设置 目标元素
	 *
	 * @param elem 目标元素
	 */
	public void setElem(Element elem) {
		this.elem = elem;
	}

	/**
	 * 获取 待翻译的内容
	 *
	 * @return text 待翻译的内容
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * 设置 待翻译的内容
	 *
	 * @param text 待翻译的内容
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获取 标识
	 *
	 * @return flag 标识
	 */
	public int getFlag() {
		return this.flag;
	}

	/**
	 * 设置 标识
	 *
	 * @param flag 标识
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * 获取 翻译元素
	 *
	 * @return tlElem 翻译元素
	 */
	public Element getTlElem() {
		return this.tlElem;
	}

	/**
	 * 设置 翻译元素
	 *
	 * @param tlElem 翻译元素
	 */
	public void setTlElem(Element tlElem) {
		this.tlElem = tlElem;
	}

	/**
	 * 获取 模板元素
	 *
	 * @return modelElem 模板元素
	 */
	public Element getModelElem() {
		return this.modelElem;
	}

	/**
	 * 设置 模板元素
	 *
	 * @param modelElem 模板元素
	 */
	public void setModelElem(Element modelElem) {
		this.modelElem = modelElem;
	}

	/**
	 * 获取 译文
	 *
	 * @return tlText 译文
	 */
	public String getTlText() {
		return this.tlText;
	}

	/**
	 * 设置 译文
	 *
	 * @param tlText 译文
	 */
	public void setTlText(String tlText) {
		this.tlText = tlText;
	}

	/**
	 * 获取 id字符串
	 *
	 * @return id id字符串
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置 id字符串
	 *
	 * @param id id字符串
	 */
	public void setId(String id) {
		this.id = id;
	}
}
