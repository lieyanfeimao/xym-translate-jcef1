//package com.xuanyimao.translate.entity.translate;
//
//import java.util.List;
//
///**
// * 任务配置
// * @author liuming
// *
// */
//public class TaskConfig {
//	/**任务id**/
//	private String id;
//
//	/**使用的翻译平台**/
//	private String platform;
//	/**使用的翻译程序**/
//	private String program;
//	/**原文件编码**/
//	private String oldcode="UTF-8";
//	/**处理后的文件编码**/
//	private String outcode="UTF-8";
//	/**同时翻译的文件个数**/
//	private Integer count=1;
//	/**原文件目录**/
//	private String folderPath;
//
//	/**修正的首页路径*/
//	private String updateIndex="index.html";
//	/**预览的首页路径*/
//	private String previewIndex="index.html";
//
//	/**选择的需要翻译的文件**/
//	private List<TaskFile> files;
//
//	/**
//	 * 获取  任务id
//	 * @return id 任务id
//	 */
//	public String getId() {
//		return id;
//	}
//
//	/**
//	 * 设置 任务id
//	 * @param id 任务id
//	 */
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	/**
//	 * 获取  使用的翻译平台
//	 * @return platform 使用的翻译平台
//	 */
//	public String getPlatform() {
//		return platform;
//	}
//
//	/**
//	 * 设置 使用的翻译平台
//	 * @param platform 使用的翻译平台
//	 */
//	public void setPlatform(String platform) {
//		this.platform = platform;
//	}
//
//	/**
//	 * 获取  使用的翻译程序
//	 * @return program 使用的翻译程序
//	 */
//	public String getProgram() {
//		return program;
//	}
//
//	/**
//	 * 设置 使用的翻译程序
//	 * @param program 使用的翻译程序
//	 */
//	public void setProgram(String program) {
//		this.program = program;
//	}
//
//	/**
//	 * 获取  原文件编码
//	 * @return oldcode 原文件编码
//	 */
//	public String getOldcode() {
//		return oldcode;
//	}
//
//	/**
//	 * 设置 原文件编码
//	 * @param oldcode 原文件编码
//	 */
//	public void setOldcode(String oldcode) {
//		this.oldcode = oldcode;
//	}
//
//	/**
//	 * 获取  处理后的文件编码
//	 * @return outcode 处理后的文件编码
//	 */
//	public String getOutcode() {
//		return outcode;
//	}
//
//	/**
//	 * 设置 处理后的文件编码
//	 * @param outcode 处理后的文件编码
//	 */
//	public void setOutcode(String outcode) {
//		this.outcode = outcode;
//	}
//
//	/**
//	 * 获取  同时翻译的文件个数
//	 * @return count 同时翻译的文件个数
//	 */
//	public Integer getCount() {
//		return count;
//	}
//
//	/**
//	 * 设置 同时翻译的文件个数
//	 * @param count 同时翻译的文件个数
//	 */
//	public void setCount(Integer count) {
//		this.count = count;
//	}
//
//	/**
//	 * 获取  原文件目录
//	 * @return folderPath 原文件目录
//	 */
//	public String getFolderPath() {
//		return folderPath;
//	}
//
//	/**
//	 * 设置 原文件目录
//	 * @param folderPath 原文件目录
//	 */
//	public void setFolderPath(String folderPath) {
//		this.folderPath = folderPath;
//	}
//
//	/**
//	 * 获取  修正的首页路径
//	 * @return updateIndex 修正的首页路径
//	 */
//	public String getUpdateIndex() {
//		return updateIndex;
//	}
//
//	/**
//	 * 设置 修正的首页路径
//	 * @param updateIndex 修正的首页路径
//	 */
//	public void setUpdateIndex(String updateIndex) {
//		this.updateIndex = updateIndex;
//	}
//
//	/**
//	 * 获取  预览的首页路径
//	 * @return previewIndex 预览的首页路径
//	 */
//	public String getPreviewIndex() {
//		return previewIndex;
//	}
//
//	/**
//	 * 设置 预览的首页路径
//	 * @param previewIndex 预览的首页路径
//	 */
//	public void setPreviewIndex(String previewIndex) {
//		this.previewIndex = previewIndex;
//	}
//
//	/**
//	 * 获取  选择的需要翻译的文件
//	 * @return files 选择的需要翻译的文件
//	 */
//	public List<TaskFile> getFiles() {
//		return files;
//	}
//
//	/**
//	 * 设置 选择的需要翻译的文件
//	 * @param files 选择的需要翻译的文件
//	 */
//	public void setFiles(List<TaskFile> files) {
//		this.files = files;
//	}
//}
