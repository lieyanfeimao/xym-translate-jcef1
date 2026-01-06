package com.xuanyimao.translate.entity;

/***
 * 用于前端的下拉选择框
 * @author liuming
 *
 */
public class ComboxValue {
	/** 值 */
	private String value;
    /** 展示的文本 */
    private String text;

	/**
	 * 
	 * @param value 值
	 * @param text 显示的文本
	 */
	public ComboxValue(String value, String text) {
		super();
		this.value = value;
		this.text = text;
	}

	/**
	 * 获取 展示的文本
	 *
	 * @return text 展示的文本
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * 设置 展示的文本
	 *
	 * @param text 展示的文本
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获取 值
	 *
	 * @return value 值
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * 设置 值
	 *
	 * @param value 值
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
