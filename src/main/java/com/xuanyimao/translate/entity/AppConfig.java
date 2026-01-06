package com.xuanyimao.translate.entity;

import java.util.List;

/***
 * 程序配置信息，保存在APP根目录的config文件中
 * @author liuming
 *
 */
public class AppConfig implements Cloneable {

	/** ID - 企鹅 */
	private String txSecretId;

	/** key - 企鹅 */
	private String txSecretKey;

	/** ID - 百度 */
	private String bdAppId;

	/** key - 百度 */
	private String bdSecretKey;

	/**
	 * 获取 ID - 企鹅
	 *
	 * @return txSecretId ID - 企鹅
	 */
	public String getTxSecretId() {
		return this.txSecretId;
	}

	/**
	 * 设置 ID - 企鹅
	 *
	 * @param txSecretId ID - 企鹅
	 */
	public void setTxSecretId(String txSecretId) {
		this.txSecretId = txSecretId;
	}

	/**
	 * 获取 key - 企鹅
	 *
	 * @return txSecretKey key - 企鹅
	 */
	public String getTxSecretKey() {
		return this.txSecretKey;
	}

	/**
	 * 设置 key - 企鹅
	 *
	 * @param txSecretKey key - 企鹅
	 */
	public void setTxSecretKey(String txSecretKey) {
		this.txSecretKey = txSecretKey;
	}

	/**
	 * 获取 ID - 百度
	 *
	 * @return bdAppId ID - 百度
	 */
	public String getBdAppId() {
		return this.bdAppId;
	}

	/**
	 * 设置 ID - 百度
	 *
	 * @param bdAppId ID - 百度
	 */
	public void setBdAppId(String bdAppId) {
		this.bdAppId = bdAppId;
	}

	/**
	 * 获取 key - 百度
	 *
	 * @return bdSecretKey key - 百度
	 */
	public String getBdSecretKey() {
		return this.bdSecretKey;
	}

	/**
	 * 设置 key - 百度
	 *
	 * @param bdSecretKey key - 百度
	 */
	public void setBdSecretKey(String bdSecretKey) {
		this.bdSecretKey = bdSecretKey;
	}

	public AppConfig clone() throws CloneNotSupportedException {
		return (AppConfig) super.clone();
	}
}
