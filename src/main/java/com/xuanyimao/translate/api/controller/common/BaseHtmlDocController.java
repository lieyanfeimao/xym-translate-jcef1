package com.xuanyimao.translate.api.controller.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.anno.JsFunction;
import com.xuanyimao.translate.anno.JsObject;
import com.xuanyimao.translate.api.service.common.LatestService;
import com.xuanyimao.translate.api.service.common.TranslateService;
import com.xuanyimao.translate.api.service.common.UpdateService;
import com.xuanyimao.translate.api.service.project.VersionService;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.core.threadpool.ThreadPoolGroupManager;
import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.Message;
import com.xuanyimao.translate.entity.htmldoc.ConfigData;
import com.xuanyimao.translate.entity.htmldoc.FileInfo;
import com.xuanyimao.translate.entity.htmldoc.TranslateReplaceData;
import com.xuanyimao.translate.entity.htmldoc.TranslateReplaceDataFile;
import com.xuanyimao.translate.entity.project.Version;
import com.xuanyimao.translate.entity.translate.TranslateData;
import com.xuanyimao.translate.util.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/***
 * 基础接口 - 包含更新和生成离线文件。
 * @author liuming
 *
 */
@JsClass(prefix = "baseHtml")
public class BaseHtmlDocController {

	@JsObject
	TranslateService translateService;

	@JsObject
	UpdateService updateService;

	@JsObject
	LatestService latestService;

	@JsObject
	VersionService versionService;

	/******************************************   翻译    ******************************************/
	/**
	 * 查询配置
	 * @return
	 */
	@JsFunction(name="config")
	public Message config(Long projectId,Long versionId) {
		try {
			if( projectId==null || versionId==null ){
				return Message.error("参数错误");
			}
			return Message.success("查询成功",translateService.config(projectId,versionId));
		} catch (Exception e) {
			LogUtil.getLogger().error("查询失败：{}",e.getMessage(),e);
			return Message.error("查询失败："+e.getMessage());
		}
	}

	/**
	 * 初始化数据
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param folderPath 需要翻译的文件目录
	 * @return
	 */
	@JsFunction(name="initData")
	public Message initData(Long projectId,Long versionId,String folderPath){
		try{
			if( projectId==null || versionId==null ){
				return Message.error("参数错误");
			}
			File file=new File(folderPath);
			if(!file.exists() || !file.isDirectory()) {
				return Message.error("请选择一个文件目录");
			}


			return Message.success("初始化完成",translateService.initData(projectId,versionId,file));
		} catch (Exception e) {
			LogUtil.getLogger().error("初始化数据失败：{}",e.getMessage(),e);
			return Message.error("初始化数据失败："+e.getMessage());
		}
	}

	/**
	 * 查询文件树
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @return
	 */
	@JsFunction(name="queryFileTree")
	public Message queryFileTree(Long projectId,Long versionId){
		try{
			if( projectId==null || versionId==null ){
				return Message.error("参数错误");
			}

			return Message.success("操作成功",translateService.queryFileTree(projectId,versionId));
		} catch (Exception e) {
			LogUtil.getLogger().error("初始化数据失败：{}",e.getMessage(),e);
			return Message.error("初始化数据失败："+e.getMessage());
		}
	}

	/**
	 * 保存配置
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param newConfigData    配置内容
	 * @return
	 */
	@JsFunction(name="saveConfig")
	public Message saveConfig(Long projectId, Long versionId, ConfigData newConfigData){
		try{
			if( projectId==null || versionId==null || newConfigData==null ){
				return Message.error("参数错误");
			}
			translateService.saveConfig(projectId,versionId,newConfigData);
			return Message.success("保存配置成功");
		} catch (Exception e) {
			LogUtil.getLogger().error("保存配置失败：{}",e.getMessage(),e);
			return Message.error("保存配置失败："+e.getMessage());
		}
	}

	/**
	 * 启动翻译
	 * @param projectId
	 * @param versionId
	 * @param browserId
	 * @param files
	 * @param newConfigData
	 * @return
	 */
	@JsFunction(name="startTranslate")
	public Message startTranslate(Long projectId, Long versionId, String browserId
			, List<String> files,ConfigData newConfigData) {
		try {
			if( projectId==null || versionId==null || newConfigData==null ){
				return Message.error("参数错误");
			}
			if(StringUtils.isBlank(newConfigData.getPlatformName())) {
				return Message.error("请选择翻译平台!");
			}
			if(StringUtils.isBlank(newConfigData.getSrcLanguage()) || StringUtils.isBlank(newConfigData.getDestLanguage())) {
				return Message.error("请选择翻译语言!");
			}
			if(newConfigData.getSrcLanguage().equals(newConfigData.getDestLanguage())){
				return Message.error("原文和译文语种不能一样!");
			}

			if(files==null || files.isEmpty()) {
				return Message.error("请选择需要翻译的文件!");
			}

			if(StringUtils.isBlank(newConfigData.getFileEncode())) {
				newConfigData.setFileEncode("utf-8");
			}

			if(newConfigData.getTranslateCount()==null || newConfigData.getTranslateCount()<=0) {
				newConfigData.setTranslateCount(1);
			}

			translateService.startTranslate(projectId,versionId, browserId, files,newConfigData);

			return Message.success("启动成功!");
		}catch(Exception e) {
			LogUtil.getLogger().error("内部错误：{}",e.getMessage(),e);
			return Message.error("内部错误:"+e.getMessage());
		}
	}

	/**
	 * 停止翻译
	 * @param browserId
	 * @return
	 */
	@JsFunction(name="stopTranslate")
	public Message stopTranslate(String browserId) {
		try {
			ThreadPoolGroupManager.getInstance().stopTranslate(browserId);
			return Message.success();
		}catch(Exception e) {
			LogUtil.getLogger().error("停止翻译异常,内部错误：{}",e.getMessage(),e);
			return Message.error("停止翻译异常,内部错误:"+e.getMessage());
		}
	}

	/**
	 * 查询队列信息
	 * @param browserId
	 * @return
	 */
	@JsFunction(name="queueInfo")
	public Message queueInfo(String browserId) {
		try {
			return Message.success("操作成功!",ThreadPoolGroupManager.getInstance().queueInfo(browserId));
		}catch(Exception e) {
			LogUtil.getLogger().error("查询队列信息异常,内部错误：{}",e.getMessage(),e);
			return Message.error("查询队列信息异常,内部错误:"+e.getMessage());
		}
	}


	/******************************************   修正    ******************************************/
	/**
	 * 根据文件ID查询翻译数据
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param fileId    文件ID
	 * @return
	 */
	@JsFunction(name="queryDataByFileId")
	public Message queryDataByFileId(Long projectId,Long versionId,String fileId){
		try {
			String filePath=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+fileId;
			File file=new File(filePath);
			if(!file.exists()) {
				return Message.error("很抱歉,数据文件不存在或已损坏,请对此文件重新进行翻译!");
			}
			return Message.success("操作成功",FileUtil.readFile(file));
		}catch(Exception e) {
			LogUtil.getLogger().error("查询翻译数据异常：{}",e.getMessage(),e);
			return Message.error("查询翻译数据异常:"+e.getMessage());
		}
	}


	/**
	 * 更新翻译数据
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param fileId  文件id
	 * @param dataId  数据id
	 * @param tlText  翻译文本
	 * @return
	 */
	@JsFunction(name="updateTranslateData")
	public Message updateTranslateData(Long projectId,Long versionId,String fileId,String dataId,String tlText) {
		try {
			if(StringUtils.isBlank(dataId)) {
				return Message.error("非法参数");
			}

			String filePath=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+fileId;
			File file=new File(filePath);
			if(!file.exists()) {
				return Message.error("很抱歉,数据文件不存在或已损坏,请对此文件重新进行翻译!");
			}
			List<TranslateData> tdList=new Gson().fromJson(FileUtil.readFile(file), new TypeToken<List<TranslateData>>(){}.getType());
			for(TranslateData td:tdList) {
				if(dataId.equals(td.getId())) {
					td.setTlText(tlText);
					td.updateVersion();
					
					//重新写入到文件
					FileUtil.saveFile(filePath, new Gson().toJson(tdList));
					return Message.success("更新成功",td);
				}
			}
			
			return Message.error("未找到对应的数据");
		}catch(Exception e) {
			LogUtil.getLogger().error("更新翻译数据失败：{}",e.getMessage(),e);
			return Message.error("更新翻译数据失败:"+e.getMessage());
		}
	}
	
	/**
	 * 查询需要替换的数据
	 * @author liuming
	 * @since 2023年9月27日
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param fileId  文件id
	 * @param dataId  数据id
	 * @return
	 */
	@JsFunction(name="queryReplaceDataList")
	public Message queryReplaceDataList(Long projectId,Long versionId,String fileId,String dataId) {
		try {
			TranslateReplaceData trd=new TranslateReplaceData();

			String versionFolder=VersionUtil.getVersionFolder(projectId,versionId);

			List<TranslateReplaceDataFile> trdfList=new ArrayList<TranslateReplaceDataFile>();
			trd.setTrdFileList(trdfList);
			//获取待翻译的句子
			String filePath=versionFolder+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+fileId;
			File file=new File(filePath);
			if(!file.exists()) {
				return Message.error("很抱歉,数据文件不存在或已损坏,请对此文件重新进行翻译!");
			}
			List<TranslateData> tdList=new Gson().fromJson(FileUtil.readFile(file), new TypeToken<List<TranslateData>>(){}.getType());
			String content="";
			if(tdList!=null) {
				for(TranslateData td:tdList) {
					if(dataId.equals(td.getId())) {
						content=td.getOldText();
						trd.setOldText(td.getOldText());
						trd.setTlText(td.getTlText());
						break;
					}
				}
			}
//			System.out.println(content);
			if(StringUtils.isBlank(content)) {
				return Message.error("用于替换的数据内容为空，操作失败!");
			}
			
			//读取配置文件
			String configPath=versionFolder+Constants.TRANSLATE_DATA_CONFIG_FILE;
			System.out.println(configPath);
			ConfigData configData=new Gson().fromJson(FileUtil.readFile(configPath), ConfigData.class);
//			TaskConfig taskConfig=new Gson().fromJson(FileUtil.read(configPath), TaskConfig.class);

			List<FileInfo> files = configData.getFileList();
			if(files!=null) {
				for(FileInfo tf:files) {
					if(fileId.equals(tf.getId())) {
						int count= XStringUtil.getCharCount(tf.getPath(),File.separatorChar);
				    	String pathPre="";
				    	for(int i=0;i<count;i++) {
				    		pathPre+="../";
				    	}
				    	trd.setPathPre(pathPre);
					}
			    	
					updateService.queryReplaceData(versionFolder, tf, dataId, content, trdfList);
				}
			}
			
			return Message.success("操作成功",trd);
		}catch(Exception e) {
			e.printStackTrace();
			return Message.error("内部错误:"+e.getMessage());
		}
	}
	
	/**
	 * 替换译文
	 * @author liuming
	 * @since 2023年9月28日
	 * @param projectId 项目ID
	 * @param versionId 版本ID
	 * @param fileId  文件id
	 * @param datas  替换的数据
	 * @return
	 */
	@JsFunction(name="replaceData")
	public Message replaceData(Long projectId,Long versionId,String fileId,String dataId,List<String> datas) {
		try {
			if(datas==null || datas.isEmpty()) {
				return Message.error("请选择需要替换译文的数据");
			}
			String versionFolder=VersionUtil.getVersionFolder(projectId,versionId);
			//获取待翻译的句子
			String filePath=versionFolder+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+fileId;
			File file=new File(filePath);
			if(!file.exists()) {
				return Message.error("很抱歉,数据文件不存在或已损坏,请对此文件重新进行翻译!");
			}
			List<TranslateData> tdList=new Gson().fromJson(FileUtil.readFile(file), new TypeToken<List<TranslateData>>(){}.getType());
			//用于替换的译文
			String tlText="";
			if(tdList!=null) {
				for(TranslateData td:tdList) {
					if(dataId.equals(td.getId())) {
						tlText=td.getTlText();
						break;
					}
				}
			}
			
			//key 为 文件id ,value中为 数据id列表
			Map<String, List<String>> map=new HashMap<String, List<String>>();
			for(String str:datas) {
				String[] arr=str.split("\\|");
				if(map.get(arr[0])==null) {
					map.put(arr[0], new ArrayList<String>());
				}
				map.get(arr[0]).add(arr[1]);
			}
//			System.out.println(map);
			
			Iterator<Entry<String, List<String>>> it = map.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, List<String>> nt = it.next();
				List<String> dataIdList=nt.getValue();
				filePath=versionFolder+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+nt.getKey();
				file=new File(filePath);
				
				tdList=new Gson().fromJson(FileUtil.readFile(file), new TypeToken<List<TranslateData>>(){}.getType());
				for(TranslateData td:tdList) {
					if(dataIdList.contains(td.getId())) {
//						System.out.println("找到了数据:"+td.getId());
						td.setTlText(tlText);
					}
				}
				FileUtil.saveFile(file, new Gson().toJson(tdList));
				
			}
			return Message.success("替换完成");
		}catch(Exception e) {
			e.printStackTrace();
			return Message.error("内部错误:"+e.getMessage());
		}
	}


	/****************  离线文件  *******************/
	/**
	 * 查询所有可生成离线文件的文件
	 * @param projectId 项目id
	 * @param versionId 版本id
	 * @return
	 */
	@JsFunction(name="queryLatestFiles")
	public Message queryLatestFiles(Long projectId,Long versionId) {
		try {
			String path=VersionUtil.getVersionFolder(projectId,versionId)+Constants.TRANSLATE_DATA_CONFIG_FILE;
			File file=new File(path);
			if(!file.exists()) {
				return Message.error("请先进行翻译再使用本功能!");
			}

			String data=FileUtil.readFile(file);
			if(StringUtils.isBlank(data)) {
				return Message.error("请先进行翻译再使用本功能!");
			}

			ConfigData configData=new Gson().fromJson(data, ConfigData.class);
			if(configData.getFileList()==null || configData.getFileList().isEmpty()) {
				return Message.error("请先进行翻译再使用本功能!");
			}


			return Message.success("", latestService.queryLatestFiles(configData.getFileList()));
		}catch(Exception e) {
			e.printStackTrace();
			return Message.error("内部错误:"+e.getMessage());
		}
	}

	/**
	 * 生成文件
	 * @param projectId
	 * @param versionId
	 * @param ids
	 * @return
	 */
	@JsFunction(name="createFiles")
	public Message createFiles(Long projectId,Long versionId,List<String> ids) {
		try {
			if(ids==null || ids.isEmpty()) {
				return Message.error("请选择要生成的文件!");
			}
			String versionFolder=VersionUtil.getVersionFolder(projectId,versionId);
			//读取配置信息
			String path=versionFolder+Constants.TRANSLATE_DATA_CONFIG_FILE;
			File file=new File(path);
			if(!file.exists()) {
				return Message.error("配置文件不存在,请先翻译再进行文件生成!");
			}
			String data=FileUtil.readFile(path);
			if(StringUtils.isBlank(data)) {
				return Message.error("请先进行翻译再使用本功能!");
			}

			ConfigData configData=new Gson().fromJson(data, ConfigData.class);
			if(configData.getFileList()==null || configData.getFileList().isEmpty()) {
				return Message.error("请先进行翻译再使用本功能!");
			}

			//获取文件列表
			List<FileInfo> fileList = configData.getFileList();
			//需要生成的文件列表
			List<FileInfo> createList=new ArrayList<FileInfo>();
			for(FileInfo fileInfo:fileList) {
				if(ids.contains(fileInfo.getId())) {
					createList.add(fileInfo);
				}
			}


			int errorCount=0;
			StringBuffer sb=new StringBuffer();
			for(FileInfo fileInfo:createList) {
				try {
					latestService.createFile(versionFolder,fileInfo,configData.getFileEncode());
				}catch (Exception e) {
					LogUtil.getLogger().error("生成文件 {} 异常:{}",fileInfo.getPath(),e.getMessage(),e);
					errorCount++;
					sb.append(fileInfo.getPath()).append("<br/>");
				}
			}
			if(sb.length()>0) {
				sb.insert(0,"<div style=\"color:red;\"><br/>生成失败的文件:<br/>");
				sb.append("</div>");
			}
			sb.insert(0,"共需生成"+createList.size()+"个文件,成功"+(createList.size()-errorCount)+"个,失败"+errorCount+"个");

			return Message.success(sb.toString());
		}catch(Exception e) {
			e.printStackTrace();
			return Message.error("内部错误:"+e.getMessage());
		}
	}

	/**
	 * 获取阅览离线文件 首页
	 * @param projectId
	 * @param versionId
	 * @return
	 */
	@JsFunction(name = "viewLatestPage")
	public Message viewLatestPage(Long projectId,Long versionId){
		try{
			if( projectId==null || versionId==null ){
				return Message.error("参数错误");
			}
			Version version=versionService.findVersion(projectId,versionId);
			if(version==null){
				return Message.error("版本不存在或已被删除");
			}
			//获取翻译程序对象
			TranslateProgram translateProgram=TranslateUtil.getTranslateProgram(version.getProgramName());
			if(translateProgram==null){
				return Message.error("程序不存在或已被删除");
			}

			return Message.success("操作成功","../../"+translateProgram.viewLatestPage().replace("{uuid}",version.getUuid()));
		}catch (Exception e) {
			LogUtil.getLogger().error("获取阅览离线文件页面失败：{}",e.getMessage(),e);
			return Message.error("获取阅览离线文件页面失败："+e.getMessage());
		}
	}

	/**
	 * 创建离线压缩包
	 * @param path         文件保存路径
	 * @param projectId    项目id
	 * @param versionId    版本id
	 * @return
	 */
	@JsFunction
	public Message createLatestPkg(String path,Long projectId,Long versionId){
		try{
			if( projectId==null || versionId==null ){
				return Message.error("参数错误");
			}
			if(StringUtils.isBlank(path)) {
				return Message.error("文件存储路径不能为空");
			}
			if(!path.endsWith(".zip")) path+=".zip";

			String folder=VersionUtil.getVersionFolder(projectId,versionId)+ Constants.TRANSLATE_DATA_FOLDER_LATEST;
			File file=new File(folder);
			FileUtil.zip(file.listFiles(),path);

			file=new File(path);
			Runtime.getRuntime().exec("explorer " + file.getParent());

			return Message.success("导出离线压缩包成功");
		}catch (Exception e) {
			LogUtil.getLogger().error("创建词库文件失败：{}",e.getMessage(),e);
			return Message.error("创建词库文件失败："+e.getMessage());
		}
	}
}
