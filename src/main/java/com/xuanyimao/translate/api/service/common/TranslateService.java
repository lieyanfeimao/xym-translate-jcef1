package com.xuanyimao.translate.api.service.common;

import com.google.gson.Gson;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.core.threadpool.ThreadPoolGroupManager;
import com.xuanyimao.translate.entity.htmldoc.ConfigData;
import com.xuanyimao.translate.entity.htmldoc.FileInfo;
import com.xuanyimao.translate.entity.project.Version;
import com.xuanyimao.translate.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.*;

/**
 * 翻译文件
 */
@JsClass
public class TranslateService {



	/**
	 * 获取配置
	 * @return
	 */
	public Map<String,Object> config(Long projectId, Long versionId){
		Map<String,Object> result = new HashMap<>();

		String versionFolder=VersionUtil.getVersionFolder(projectId,versionId);
		result.put("versionFolder",versionFolder);
		result.put("appPath",ApplicationData.appPath);
		result.put("config",null);
		//文件分隔符
		result.put("fs",File.separator);
		try {
			String data = FileUtil.readFile(versionFolder+Constants.TRANSLATE_DATA_CONFIG_FILE);
			if (StringUtils.isNotBlank(data)) {
				result.put("config",new Gson().fromJson(data, ConfigData.class));
			}
		}catch (Exception e){
			LogUtil.getLogger().error("解析失败:{}",e.getMessage(),e);
		}
		return result;
	}


	/**
	 * 初始化数据
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param folder 需要翻译的文件目录
	 * @return
	 */
	public ConfigData initData(Long projectId,Long versionId,File folder) throws Exception {
		//项目版本的工作目录
		String versionFolder=VersionUtil.getVersionFolder(projectId,versionId);
		//原文件目录
		String oldPath=versionFolder+Constants.TRANSLATE_DATA_FOLDER_OLD;
		initFolder(oldPath);
		//修正用的文件
		String updatePath=versionFolder+Constants.TRANSLATE_DATA_FOLDER_UPDATE;
		initFolder(updatePath);
		//离线文件
		String latestPath=versionFolder+Constants.TRANSLATE_DATA_FOLDER_LATEST;
		initFolder(latestPath);
		//模板文件(用于生成离线文件)
		String modelPath=versionFolder+Constants.TRANSLATE_DATA_FOLDER_MODEL;
		initFolder(modelPath);

		//将folder目录下的文件复制到 工作目录
		FileUtils.copyDirectory(folder, new File(oldPath));
		FileUtils.copyDirectory(folder, new File(updatePath));
		FileUtils.copyDirectory(folder, new File(latestPath));

		//复制翻译所需的文件到指定目录
		String updateFolder=ApplicationData.appPath+File.separator+Constants.TRANSLATE_FILE_UPDATE_FOLDER;
		File file=new File(updateFolder);
		if(file.exists() && file.isDirectory()){
			FileUtils.copyDirectory(file, new File(updatePath));
		}

		String latestFolder=ApplicationData.appPath+File.separator+Constants.TRANSLATE_FILE_LATEST_FOLDER;
		file=new File(latestFolder);
		if(file.exists() && file.isDirectory()){
			FileUtils.copyDirectory(file, new File(latestPath));
		}

		ConfigData configData=readConfigData(versionFolder+Constants.TRANSLATE_DATA_CONFIG_FILE);
		if(configData==null){
			configData=initConfigData(projectId,versionId);
			FileUtil.saveFile(versionFolder+Constants.TRANSLATE_DATA_CONFIG_FILE,new Gson().toJson(configData));
		}

		return configData;
	}

	/**
	 * 初始化目录 - 删除再重新创建
	 * @param folderPath 目录路径
	 */
	public void initFolder(String folderPath){
		try {
			FileUtil.deleteFolder(folderPath);
			File folder=new File(folderPath);
			folder.mkdirs();
		} catch (Exception e) {
			LogUtil.getLogger().error("初始化目录失败:{}",e.getMessage(),e);
		}
	}

	/**
	 * 查询文件树
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @return
	 */
	public List<Map<String,Object>> queryFileTree(Long projectId,Long versionId){
		//项目版本的工作目录
		String versionFolder=VersionUtil.getVersionFolder(projectId,versionId);
		File file=new File(versionFolder+Constants.TRANSLATE_DATA_FOLDER_OLD);
		return TranslateUtil.queryFileTree(file);
	}

	/**
	 * 保存配置
	 * @param projectId
	 * @param versionId
	 * @param newConfigData
	 */
	public void saveConfig(Long projectId,Long versionId,ConfigData newConfigData){
		String path=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_CONFIG_FILE;
		ConfigData configData=readConfigData(path);
		if(configData==null){
			configData=initConfigData(projectId,versionId);
		}
		copyProperties(configData,newConfigData);

		FileUtil.saveFile(path,new Gson().toJson(configData));
	}

	/**
	 * 启动翻译程序
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param browserId 浏览器ID
	 * @param files     待翻译的文件列表
	 * @param newConfigData 配置数据
	 */
	public void startTranslate(Long projectId, Long versionId, String browserId, List<String> files,ConfigData newConfigData){
		String path=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_CONFIG_FILE;
		ConfigData configData=readConfigData(path);
		if(configData==null){
			configData=initConfigData(projectId,versionId);
		}
		copyProperties(configData,newConfigData);

		//获取已有的文件列表
		List<FileInfo> fileList=configData.getFileList();
		if(fileList==null){
			fileList=new ArrayList<FileInfo>();
			configData.setFileList(fileList);
		}

		//用于读取已有的翻译内容
		List<FileInfo> oldFileList=new ArrayList<FileInfo>();
		oldFileList.addAll(fileList);

		//准备翻译的文件
		List<FileInfo> dealFileList=new ArrayList<FileInfo>();
		//文件读取目录
		String oldPath=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_FOLDER_OLD+File.separator;

		for(String f:files){
			//获取文件的相对路径
			String p=f.substring(oldPath.length());
			boolean flag=false;
			for(FileInfo fileInfo:fileList) {
				if(p.equals(fileInfo.getPath())){
					dealFileList.add(fileInfo);
					flag=true;
					break;
				}
			}
			//这是新文件，添加到文件列表中
			if(!flag){
				FileInfo fileInfo=new FileInfo(UUID.randomUUID().toString().replace("-", ""), p);
				fileList.add(fileInfo);
				dealFileList.add(fileInfo);
			}
		}
		//保存配置
		FileUtil.saveFile(path,new Gson().toJson(configData));

		//启动翻译任务
		try {
			ThreadPoolGroupManager.getInstance().startTranslate(projectId,versionId,browserId,oldFileList,dealFileList,configData);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取配置数据，如果不存在，返回一个空对象
	 * @param path
	 * @return
	 */
	private ConfigData readConfigData(String path){
		try {
			String data = FileUtil.readFile(path);
			if (StringUtils.isNotBlank(data)) {
				return new Gson().fromJson(data, ConfigData.class);
			}
		}catch (Exception e){
			LogUtil.getLogger().error("解析配置信息失败:{}",e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 初始化配置数据
	 * @return
	 */
	private ConfigData initConfigData(Long projectId,Long versionId){
		ConfigData configData=new ConfigData();

//		Version version=VersionUtil.getVersion(projectId,versionId);
//		configData.setVersionFolder(Constants.TRANSLATE_DATA_FOLDER_NAME+File.separator+version.getUuid()+File.separator);

		configData.setRgx("^((?!class-use|module-summary|package-.*?|allclasses-.*?|index|index-all|overview-.*?|help-doc|constant-.*?).)*\\.html$");
		configData.setPlatformName("");
		configData.setDictPath("");
		configData.setSrcLanguage("");
		configData.setDestLanguage("");
		configData.setFileEncode("utf-8");
		configData.setTranslateCount(1);
		return configData;
	}

	/**
	 * 复制属性
	 * @param oldConfig 原配置
	 * @param newConfig 新配置
	 */
	private void copyProperties(ConfigData oldConfig,ConfigData newConfig){
		oldConfig.setFileEncode(newConfig.getFileEncode());
		oldConfig.setTranslateCount(newConfig.getTranslateCount());
		oldConfig.setRgx(newConfig.getRgx());
		oldConfig.setSrcLanguage(newConfig.getSrcLanguage());
		oldConfig.setDestLanguage(newConfig.getDestLanguage());
		oldConfig.setPlatformName(newConfig.getPlatformName());
		oldConfig.setDictPath(newConfig.getDictPath());
		oldConfig.setCheckVersionIds(newConfig.getCheckVersionIds());
	}
}
