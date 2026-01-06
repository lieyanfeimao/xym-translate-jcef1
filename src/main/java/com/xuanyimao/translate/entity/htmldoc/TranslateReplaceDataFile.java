package com.xuanyimao.translate.entity.htmldoc;

import java.util.List;

/***
 * 用于替换译文的数据对象 - 文件
 * @author liuming
 *
 */
public class TranslateReplaceDataFile {

	/**文件id**/
	private String fileId;
	
	/**文件路径**/
	private String filePath;
	
	/**数据列表**/
	List<TranslateReplaceDataItem> trdiList;

	/** 
	 * 获取  文件id 
	 * @return fileId 文件id 
	 */
	public String getFileId() {
		return fileId;
	}

	/** 
	 * 设置 文件id
	 * @param fileId 文件id
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/** 
	 * 获取  文件路径 
	 * @return filePath 文件路径 
	 */
	public String getFilePath() {
		return filePath;
	}

	/** 
	 * 设置 文件路径
	 * @param filePath 文件路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/** 
	 * 获取  数据列表 
	 * @return trdiList 数据列表 
	 */
	public List<TranslateReplaceDataItem> getTrdiList() {
		return trdiList;
	}

	/** 
	 * 设置 数据列表
	 * @param trdiList 数据列表
	 */
	public void setTrdiList(List<TranslateReplaceDataItem> trdiList) {
		this.trdiList = trdiList;
	}
	
	
}
