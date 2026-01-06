package com.xuanyimao.translate.entity.translate;

public class TranslateResult {
	
	/**结果，true 翻译成功  false 翻译失败**/
	private boolean ok;
	/** 数据内容 **/
	private String data;
	/** 
	 * 获取  结果，true 翻译成功  false 翻译失败 
	 * @return ok 结果，true 翻译成功  false 翻译失败 
	 */
	public boolean isOk() {
		return ok;
	}
	/** 
	 * 设置 结果，true 翻译成功  false 翻译失败
	 * @param ok 结果，true 翻译成功  false 翻译失败
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	/** 
	 * 获取  数据内容 
	 * @return data 数据内容 
	 */
	public String getData() {
		return data;
	}
	/** 
	 * 设置 数据内容
	 * @param data 数据内容
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	
	public TranslateResult(boolean ok, String data) {
		super();
		this.ok = ok;
		this.data = data;
	}
	
	public static TranslateResult success(String data) {
		return new TranslateResult(true, data);
	}
	
	public static TranslateResult error(String data) {
		return new TranslateResult(false, data);
	}
}
