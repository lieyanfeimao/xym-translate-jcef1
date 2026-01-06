package com.xuanyimao.translate.core.translateprogram;

import java.util.Map;

import com.xuanyimao.translate.core.translateplatform.TranslatePlatform;
import com.xuanyimao.translate.entity.htmldoc.ConfigData;
import com.xuanyimao.translate.entity.htmldoc.FileInfo;
import com.xuanyimao.translate.util.FileUtil;

/***
 * 翻译程序
 * @author liuming
 *
 */
public abstract class TranslateProgram extends Thread {

	/** 浏览器的id **/
	protected String browserId;
	/** 项目ID **/
	protected Long projectId;
	/** 版本id **/
	protected Long versionId;
	/** 文件信息 **/
	protected FileInfo fileInfo;

	/**数据文件存储目录**/
	protected String dataDir;
	/** 版本工作目录 **/
	protected String versionFolder;
	/** 配置数据 */
	protected ConfigData configData;
	/** 翻译平台 */
	protected TranslatePlatform translatePlatform;
	/** 内存中缓存的翻译数据 */
	protected Map<String, String> caches;
    
    /**是否停止翻译**/
    protected boolean stop=false;

	/**
	 * 初始化数据
	 * @param browserId 浏览器ID
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param fileInfo  当前处理的文件
	 * @param versionFolder 版本目录
	 * @param dataDir 数据目录
	 * @param configData 配置数据
	 * @param translatePlatform 翻译平台
	 * @param caches 内存中的缓存
	 */
	public void initData(String browserId, Long projectId, Long versionId, FileInfo fileInfo,
						 String versionFolder, String dataDir, ConfigData configData,
						 TranslatePlatform translatePlatform, Map<String, String> caches){
		this.browserId=browserId;
		this.projectId=projectId;
		this.versionId=versionId;
		this.fileInfo=fileInfo;
		this.versionFolder=versionFolder;
		this.dataDir=dataDir;
		this.configData=configData;
		this.translatePlatform=translatePlatform;
		this.caches=caches;
	}


    /***
     * 读取待翻译的文件内容
     * @author liuming
     * @param filePath 文件路径
     * @return
     */
    public String readFile(String filePath) {
		return FileUtil.readFile(filePath,false,configData.getFileEncode());
    }
    
    
    /**
     * 保存文件
     * @author liuming
     * @since 2023年9月22日
     * @param filePath
     * @param data
     */
    public void writeFile(String filePath,String data) {
		FileUtil.saveFile(filePath,data,configData.getFileEncode());
    }
    
    /**
     * 解析数据
     * @author:liuming
     * @param data 文件内容
     */
    public abstract void parseData(String data);

    /***
     * 停止线程
     * @author liuming
     * @since 2023年9月22日
     */
    public void stopThread() {
		this.stop=true;
	}

	/**
	 * 启动翻译页面 的首页
	 * @return
	 */
	public abstract String startTranslatePage();

	/**
	 * 翻译修正页面 的首页
	 * @return
	 */
	public abstract String updateTranslatePage();

	/**
	 * 创建离线版本页面
	 * @return
	 */
	public abstract String createLatestPage();

	/**
	 * 预览离线文件 的首页
	 * @return
	 */
	public abstract String viewLatestPage();
}
