package com.xuanyimao.translate.core.threadpool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.core.translateplatform.TranslatePlatform;
import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.htmldoc.ConfigData;
import com.xuanyimao.translate.entity.htmldoc.FileInfo;
import com.xuanyimao.translate.entity.project.Version;
import com.xuanyimao.translate.entity.translate.TranslateData;
import com.xuanyimao.translate.util.*;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * 线程池管理
 * @author liuming
 *
 */
public class ThreadPoolManager {

	/**线程池对象*/
    private ThreadPoolExecutor executor;
	
    /**同时翻译的最大进程数*/
    private int maxCount=3;
    /**翻译队列*/
    private List<TranslateProgram> queueList=new Vector<TranslateProgram>();

	/** id,这个id是浏览器的id **/
	private String browserId;
	
	/** 翻译用的缓存 **/
	Map<String, String> tlCaches=new HashMap<String, String>();

	/**
	 * 初始化线程池管理类
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param browserId 浏览器ID
	 * @param oldFileList 旧的文件列表-用于加载已有的翻译数据
	 * @param dealFileList 待处理的文件列表
	 * @param configData   配置数据
	 */
	public ThreadPoolManager(Long projectId, Long versionId, String browserId, List<FileInfo> oldFileList, List<FileInfo> dealFileList, ConfigData configData) throws Exception {
		this.browserId=browserId;
		this.maxCount=configData.getTranslateCount();

		Version version=VersionUtil.getVersion(projectId,versionId);
		//读取并加载缓存
		//获取数据目录
		String dataDir= VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator;
		File file=new File(dataDir);
		if(!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		//加载词库数据到内存
		readCache(dataDir,oldFileList,configData.getDictPath(),configData.getCheckVersionIds());

		System.out.println("缓存数据总数："+tlCaches.size());

		//初始化线程池
		executor=new ThreadPoolExecutor(this.maxCount,Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		//获取翻译平台
		TranslatePlatform translatePlatform=TranslateUtil.getTranslatePlatform(configData.getPlatformName());

		//生成文件处理程序，并加入线程池。每个文件由一个线程处理
		for(FileInfo fileInfo:dealFileList){
			TranslateProgram translateProgram=TranslateUtil.createTranslateProgram(version.getProgramName());

			//初始化程序数据
			translateProgram.initData(browserId,projectId,versionId,fileInfo,
					VersionUtil.getVersionFolder(projectId,versionId),
					dataDir,configData,translatePlatform,tlCaches);

			queueList.add(translateProgram);
			executor.execute(translateProgram);
		}
	}

	/**
	 * 读取词库并加载到内存
	 * @param dataDir 当前项目版本的数据目录
	 * @param oldFiles 旧的文件列表
	 * @param dictPath 词库路径
	 * @param versionList  做为词库的版本文件列表
	 */
	public void readCache(String dataDir,List<FileInfo> oldFiles,String dictPath,List<String> versionList){
		//读取词典文件
		if(StringUtils.isNotBlank(dictPath)) {
			//使用 ; 拆分出路径数组
			String[] path=dictPath.split(";");
			for(String pathItem:path) {
				//路径不为空，读取文件
				if(StringUtils.isNotBlank(pathItem.trim())) {
					try {
						String data=FileUtil.readFile(pathItem,false);
						if(StringUtils.isNotBlank(data)) {
//							System.out.println(pathItem);
//							System.out.println(data);

							Map<String, String> dictMap = new Gson().fromJson(data, new TypeToken<Map<String,String>>(){}.getType());
							tlCaches.putAll(dictMap);
						}
					}catch (Exception e) {
						LogUtil.getLogger().error("词库文件加载失败:{}",e.getMessage(),e);
					}
				}
			}
		}

		//加载选择的项目版本的翻译数据
		if(versionList!=null && !versionList.isEmpty()) {
			for(String id:versionList) {
				readCacheByTaskId(id);
			}
		}

		//加载当前版本已存在的翻译数据
		for(FileInfo fileInfo:oldFiles) {
			try{
				String data=FileUtil.readFile(dataDir+fileInfo.getId(),false);
				if(StringUtils.isNotBlank(data)) {
					List<TranslateData> list=GsonUtil.jsonToList(data,TranslateData.class);
					for(TranslateData translateData:list) {
						tlCaches.put(translateData.getOldText(), translateData.getTlText());
					}
				}
			}catch (Exception e) {
				LogUtil.getLogger().error("翻译数据加载失败:{}",e.getMessage(),e);
			}
		}

	}

	private void readCacheByTaskId(String id) {
		// 格式：projectId-versionId
		String[] ids=id.split("-");
		if(ids.length!=2 || StringUtils.isBlank(ids[0]) || StringUtils.isBlank(ids[1])) {
			return;
		}
		Long projectId=0L;
		Long versionId=0L;
		try{
			projectId=Long.valueOf(ids[0]);
			versionId=Long.valueOf(ids[1]);
		}catch (Exception e) {
			LogUtil.getLogger().error("id解析失败:{}",e.getMessage(),e);
			return;
		}
		//数据目录路径。已翻译的数据保存在此目录下
		String dataDir=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator;
		File file=new File(dataDir);
		if(file.exists() && file.isDirectory()) {
			//遍历目录下所有文件
			File[] files = file.listFiles();
			for(File f:files) {
				try {
					String data=FileUtil.readFile(f.getAbsolutePath(),false);
					if(StringUtils.isNotBlank(data)) {
						List<TranslateData> list=GsonUtil.jsonToList(data,TranslateData.class);
						for(TranslateData translateData:list) {
							tlCaches.put(translateData.getOldText(), translateData.getTlText());
						}
					}
				}catch(Exception e) {
					LogUtil.getLogger().error("已翻译数据加载失败:{}",e.getMessage(),e);
				}
			}
		}
	}
	
	
	/**
     * 设置最大线程数
     * @author:liuming
     */
    public void setMaxCount(int maxCount){
        if(maxCount==this.maxCount) return;
        this.maxCount=maxCount;
        //设置线程池大小
        executor.setCorePoolSize(this.maxCount);
    }
    

    /**
	 * 停止翻译
	 * @author liuming
	 */
	public void stopTranslate() {
		//主动停止翻译线程
		for(TranslateProgram thread:queueList) {
			if(thread!=null) thread.stopThread();
		}
		queueList=null;
        
        // 销毁线程池
        executor.shutdown();
	}
    
	
	/**
	 * 获取线程池任务处理情况
	 * @author liuming
	 * @since 2023年9月26日
	 * @return
	 */
	public Map<String,Object> queueInfo() {
//		System.out.println(executor.getActiveCount()+" - "+executor.getTaskCount()+" - "+executor.getQueue().size());
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("total", queueList.size());
//		map.put("activeCount", executor.getActiveCount());
//		map.put("taskCount", executor.getTaskCount());
//		
//		map.put("queueCount", executor.getQueue().size());
		map.put("completedCount", executor.getCompletedTaskCount());
		
		return map;
	}
}
