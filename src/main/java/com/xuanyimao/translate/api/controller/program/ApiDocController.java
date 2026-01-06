package com.xuanyimao.translate.api.controller.program;


import com.google.gson.Gson;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.anno.JsFunction;
import com.xuanyimao.translate.anno.JsObject;
import com.xuanyimao.translate.api.controller.common.BaseHtmlDocController;
import com.xuanyimao.translate.api.service.program.ApiDocService;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.entity.Message;
import com.xuanyimao.translate.entity.htmldoc.ConfigData;
import com.xuanyimao.translate.util.FileUtil;
import com.xuanyimao.translate.util.LogUtil;
import com.xuanyimao.translate.util.TranslateUtil;
import com.xuanyimao.translate.util.VersionUtil;

import java.io.File;

/**
 * API文档处理
 */
@JsClass(prefix = "apiDoc")
public class ApiDocController extends BaseHtmlDocController {

    @JsObject
    ApiDocService apiDocService;

    /**
     * 创建搜索框 - doc文档特有
     * @param projectId
     * @param versionId
     * @return
     */
    @JsFunction
    public Message createSearch(Long projectId,Long versionId) {
        try {
            String versionFolder= VersionUtil.getVersionFolder(projectId,versionId);
            //读取配置信息
            String path=versionFolder+ Constants.TRANSLATE_DATA_CONFIG_FILE;
            File file=new File(path);
            if(!file.exists()) {
                return Message.error("配置文件不存在,请先翻译再进行文件生成!");
            }
            String data= FileUtil.readFile(file);
            ConfigData configData=new Gson().fromJson(data, ConfigData.class);
            if(configData.getFileList()==null || configData.getFileList().isEmpty()) {
                return Message.error("请先进行翻译再使用本功能!");
            }

            path=versionFolder+Constants.TRANSLATE_DATA_FOLDER_LATEST+File.separator;
            File folder=new File(path);
            if(!folder.exists() || !folder.isDirectory()) {
                return Message.error("请先进行翻译再使用本功能!");
            }

            apiDocService.createSearch(folder,0,configData.getFileEncode(),configData.getFileEncode());
            return Message.success("创建完成,请点击“预览”查阅！");
        }catch(Exception e) {
            LogUtil.getLogger().error("创建搜索框异常,内部错误：{}",e.getMessage(),e);
            return Message.error("创建搜索框异常,内部错误:"+e.getMessage());
        }
    }

//    @JsObject
//    ApiDocService apiDocService;
//
//    /**
//     * 查询配置
//     * @return
//     */
//    @JsFunction(name="config")
//    public Message config(Long projectId,Long versionId) {
//        try {
//            if( projectId==null || versionId==null ){
//                return Message.error("参数错误");
//            }
//            return Message.success("查询成功",apiDocService.config(projectId,versionId));
//        } catch (Exception e) {
//            LogUtil.getLogger().error("查询失败：{}",e.getMessage(),e);
//            return Message.error("查询失败："+e.getMessage());
//        }
//    }
//
//    /**
//     * 初始化数据
//     * @param projectId 项目ID
//     * @param versionId 版本ID
//     * @param folderPath 需要翻译的文件目录
//     * @return
//     */
//    @JsFunction(name="initData")
//    public Message initData(Long projectId,Long versionId,String folderPath){
//        try{
//            if( projectId==null || versionId==null ){
//                return Message.error("参数错误");
//            }
//            File file=new File(folderPath);
//            if(!file.exists() || !file.isDirectory()) {
//                return Message.error("请选择一个文件目录");
//            }
//
//
//            return Message.success("初始化完成",apiDocService.initData(projectId,versionId,file));
//        } catch (Exception e) {
//            LogUtil.getLogger().error("初始化数据失败：{}",e.getMessage(),e);
//            return Message.error("初始化数据失败："+e.getMessage());
//        }
//    }
//
//    /**
//     * 查询文件树
//     * @param projectId 项目ID
//     * @param versionId 版本ID
//     * @return
//     */
//    @JsFunction(name="queryFileTree")
//    public Message queryFileTree(Long projectId,Long versionId){
//        try{
//            if( projectId==null || versionId==null ){
//                return Message.error("参数错误");
//            }
//
//            return Message.success("操作成功",apiDocService.queryFileTree(projectId,versionId));
//        } catch (Exception e) {
//            LogUtil.getLogger().error("初始化数据失败：{}",e.getMessage(),e);
//            return Message.error("初始化数据失败："+e.getMessage());
//        }
//    }
//
//    /**
//     * 保存配置
//     * @param projectId 项目ID
//     * @param versionId 版本ID
//     * @param newConfigData    配置内容
//     * @return
//     */
//    @JsFunction(name="saveConfig")
//    public Message saveConfig(Long projectId, Long versionId, ConfigData newConfigData){
//        try{
//            if( projectId==null || versionId==null || newConfigData==null ){
//                return Message.error("参数错误");
//            }
//            apiDocService.saveConfig(projectId,versionId,newConfigData);
//            return Message.success("保存配置成功");
//        } catch (Exception e) {
//            LogUtil.getLogger().error("保存配置失败：{}",e.getMessage(),e);
//            return Message.error("保存配置失败："+e.getMessage());
//        }
//    }
//
//    /**
//     * 启动翻译
//     * @param projectId
//     * @param versionId
//     * @param browserId
//     * @param files
//     * @param newConfigData
//     * @return
//     */
//    @JsFunction(name="startTranslate")
//    public Message startTranslate(Long projectId, Long versionId, String browserId
//            , List<String> files,ConfigData newConfigData) {
//        try {
//            if( projectId==null || versionId==null || newConfigData==null ){
//                return Message.error("参数错误");
//            }
//            if(StringUtils.isBlank(newConfigData.getPlatformName())) {
//                return Message.error("请选择翻译平台!");
//            }
//            if(StringUtils.isBlank(newConfigData.getSrcLanguage()) || StringUtils.isBlank(newConfigData.getDestLanguage())) {
//                return Message.error("请选择翻译语言!");
//            }
//            if(newConfigData.getSrcLanguage().equals(newConfigData.getDestLanguage())){
//                return Message.error("原文和译文语种不能一样!");
//            }
//
//            if(files==null || files.isEmpty()) {
//                return Message.error("请选择需要翻译的文件!");
//            }
//
//            if(StringUtils.isBlank(newConfigData.getFileEncode())) {
//                newConfigData.setFileEncode("utf-8");
//            }
//
//            if(newConfigData.getTranslateCount()==null || newConfigData.getTranslateCount()<=0) {
//                newConfigData.setTranslateCount(1);
//            }
//
//            apiDocService.startTranslate(projectId,versionId, browserId, files,newConfigData);
//
//            return Message.success("启动成功!");
//        }catch(Exception e) {
//            LogUtil.getLogger().error("内部错误：{}",e.getMessage(),e);
//            return Message.error("内部错误:"+e.getMessage());
//        }
//    }
//
//    /**
//     * 停止翻译
//     * @param browserId
//     * @return
//     */
//    @JsFunction(name="stopTranslate")
//    public Message stopTranslate(String browserId) {
//        try {
//            ThreadPoolGroupManager.getInstance().stopTranslate(browserId);
//            return Message.success();
//        }catch(Exception e) {
//            LogUtil.getLogger().error("停止翻译异常,内部错误：{}",e.getMessage(),e);
//            return Message.error("停止翻译异常,内部错误:"+e.getMessage());
//        }
//    }
//
//    /**
//     * 查询队列信息
//     * @param browserId
//     * @return
//     */
//    @JsFunction(name="queueInfo")
//    public Message queueInfo(String browserId) {
//        try {
//            return Message.success("操作成功!",ThreadPoolGroupManager.getInstance().queueInfo(browserId));
//        }catch(Exception e) {
//            LogUtil.getLogger().error("查询队列信息异常,内部错误：{}",e.getMessage(),e);
//            return Message.error("查询队列信息异常,内部错误:"+e.getMessage());
//        }
//    }

}
