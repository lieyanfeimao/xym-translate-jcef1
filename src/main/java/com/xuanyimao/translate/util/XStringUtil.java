package com.xuanyimao.translate.util;

/***
 * 字符处理
 * @author liuming
 *
 */
public class XStringUtil {
	
	/**
	 * 获取字符在字符串中出现的次数
	 * @author liuming
	 * @since 2023年9月25日
	 * @param c
	 * @return
	 */
	public static int getCharCount(String data,char c) {
		int count=0;
		for(int i=0;i<data.length();i++) {
			char c1=data.charAt(i);
			if(c==c1) {
				count++;
			}
		}
		return count;
	}
}
