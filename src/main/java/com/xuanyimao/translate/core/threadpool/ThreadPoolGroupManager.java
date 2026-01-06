package com.xuanyimao.translate.core.threadpool;

import com.xuanyimao.translate.entity.htmldoc.ConfigData;
import com.xuanyimao.translate.entity.htmldoc.FileInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 管理线程池组。<br>
 * 每个浏览器(browser)窗口对应一个翻译用的线程池，在这个类通过browserId对翻译的线程池进行启动和停止
 * @author liuming
 *
 */
public class ThreadPoolGroupManager {

	
	private final static ThreadPoolGroupManager instance=new ThreadPoolGroupManager();
	private ThreadPoolGroupManager() {}
	public static ThreadPoolGroupManager getInstance() {
		return instance;
	}

	/** 翻译用的线程池Map，key为浏览器ID(browserId)，值为线程池对象 **/
	private Map<String, ThreadPoolManager> threadPoolManagerMap=new ConcurrentHashMap<String, ThreadPoolManager>();

	/**
	 * 启动翻译程序
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param browserId 浏览器ID
	 * @param oldFileList 旧的文件列表-用于加载已有的翻译数据
	 * @param dealFileList 待处理的文件列表
	 * @param configData   配置数据
	 */
	public void startTranslate(Long projectId, Long versionId, String browserId, List<FileInfo> oldFileList,
							   List<FileInfo> dealFileList, ConfigData configData) throws Exception{
		ThreadPoolManager tpm=threadPoolManagerMap.get(browserId);
		if(tpm!=null){
			tpm.stopTranslate();
			tpm=null;
		}
		tpm=new ThreadPoolManager(projectId, versionId, browserId, oldFileList, dealFileList, configData);
		threadPoolManagerMap.put(browserId,tpm);
	}

	/**
	 * 停止翻译
	 * @param browserId 浏览器ID
	 */
	public void stopTranslate(String browserId) {
		if(threadPoolManagerMap.get(browserId)!=null) {
			threadPoolManagerMap.get(browserId).stopTranslate();
			threadPoolManagerMap.remove(browserId);
		}
	}
	
	/**
	 * 获取线程池任务处理情况
	 * @author liuming
	 * @return
	 */
	public Map<String,Object> queueInfo(String browserId){
		if(threadPoolManagerMap.get(browserId)!=null) {
			return threadPoolManagerMap.get(browserId).queueInfo();
		}
		return null;
	}
}
