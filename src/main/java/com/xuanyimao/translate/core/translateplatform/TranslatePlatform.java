package com.xuanyimao.translate.core.translateplatform;


import com.xuanyimao.translate.entity.translate.TranslateResult;

import java.util.Map;

/**
 * 翻译平台
 */
public interface TranslatePlatform {

	/**
	 * 验证翻译程序是否可正常使用
	 * @return
	 */
	boolean validate();

	/**
	 * 获取翻译内容
	 * @param translateData 原文
	 * @param from 原文语种
	 * @param to   译文语种
	 * @return
	 */
	TranslateResult getTranslateData(String translateData,String from,String to);

	/**
	 * 获取翻译内容 - 将英文翻译为中文
	 * @param translateData 原文
	 * @return
	 */
	TranslateResult getTranslateData(String translateData);

	/**
	 * 获取翻译语言列表
	 * @return
	 */
	Map<String,String> getTranslateLanguage();
}
