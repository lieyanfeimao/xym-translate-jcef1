package com.xuanyimao.translate.entity.htmldoc;

import java.util.List;

/**
 * 用于替换译文的数据对象
 * @author liuming
 *
 */
public class TranslateReplaceData {

	/**原文*/
	private String oldText;
	
	/**译文**/
	private String tlText;
	
	/**相对于父目录的路径*/
	private String pathPre;
	
	/**文件列表*/
	List<TranslateReplaceDataFile> trdFileList;
	
	/** 
	 * 获取  相对于父目录的路径 
	 * @return pathPre 相对于父目录的路径 
	 */
	public String getPathPre() {
		return pathPre;
	}
	/** 
	 * 设置 相对于父目录的路径
	 * @param pathPre 相对于父目录的路径
	 */
	public void setPathPre(String pathPre) {
		this.pathPre = pathPre;
	}
	/** 
	 * 获取  文件列表 
	 * @return trdFileList 文件列表 
	 */
	public List<TranslateReplaceDataFile> getTrdFileList() {
		return trdFileList;
	}
	/** 
	 * 设置 文件列表
	 * @param trdFileList 文件列表
	 */
	public void setTrdFileList(List<TranslateReplaceDataFile> trdFileList) {
		this.trdFileList = trdFileList;
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
}
