package com.xuanyimao.translate.entity.htmldoc;

public class TranslateReplaceDataItem {
	
	/**文件id**/
	private String fileId;
	
	/**文件路径**/
	private String filePath;
	
	/**数据id**/
	private String dataId;
	
	/**原文*/
	private String oldText;
	
	/**译文**/
	private String tlText;

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
	 * 获取  数据id 
	 * @return dataId 数据id 
	 */
	public String getDataId() {
		return dataId;
	}

	/** 
	 * 设置 数据id
	 * @param dataId 数据id
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
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
