package com.xuanyimao.translate.common;

import java.io.File;

public class Constants {
	/** 软件名 **/
	public final static String APP_NAME="M翻译";
//	public final static String APPNAME="测试";
	/**版本号*/
	public final static String VERSION="1.0.0";
	/**logo文件名**/
	public final static String LOGO_FILE_NAME="logo.png";
	
	/**配置文件名**/
	public final static String CONFIG_FILENAME="config";
	
	/** 视图界面文件目录名 */
	public final static String VIEW_FOLDER="view";
	
	/**首页文件路径**/
	public final static String UI_INDEX_PAGE="index.html";
	
	/**js调用java方法，方法名属性**/
	public final static String JS_METHOD_NAME="method";
	/**js调用java方法，参数属性**/
	public final static String JS_METHOD_PARAM="params";
	
	/**翻译程序保存数据的目录名**/
	public final static String TRANSLATE_DATA_FOLDER_NAME="files"+ File.separator+"data";

	/**翻译程序保存版本数据的目录名**/
	public final static String TRANSLATE_DATA_VERSION_FOLDER_NAME="files"+ File.separator+"data"+File.separator+"version";

	/**翻译程序的配置文件名*/
	public final static String TRANSLATE_DATA_CONFIG_FILE="config";
	
	/**翻译程序保存每个文件翻译数据的目录名**/
	public final static String TRANSLATE_DATA_DATAS_FOLDER_NAME="datas";
	
	/**翻译程序需要用到的额外的文件目录，用于校对和生成最终的版本文件**/
	public final static String TRANSLATE_FILE_FOLDER_NAME="files"+ File.separator+"program";
	
	/**翻译修正页面需要引入的目录 - html通用**/
	public final static String TRANSLATE_FILE_UPDATE_FOLDER="files"+ File.separator+"program"+File.separator+"htmlDoc"+File.separator+"update";
	/**翻译最终版本页面需要引入的目录 - html通用**/
	public final static String TRANSLATE_FILE_LATEST_FOLDER="files"+ File.separator+"program"+File.separator+"htmlDoc"+File.separator+"latest";
	
	
	/**翻译的文件-原文件目录**/
	public final static String TRANSLATE_DATA_FOLDER_OLD="old";
	/**翻译的文件-用于翻译修正的文件目录**/
	public final static String TRANSLATE_DATA_FOLDER_UPDATE="update";
	/**翻译的文件-用于生成最终版本的模板文件目录**/
	public final static String TRANSLATE_DATA_FOLDER_MODEL="model";
	/**翻译的文件-生成的最终版本的文件目录**/
	public final static String TRANSLATE_DATA_FOLDER_LATEST="latest";
	
	/**tab栏首页的id**/
	public final static String TAB_INDEX_ID="index";
}
